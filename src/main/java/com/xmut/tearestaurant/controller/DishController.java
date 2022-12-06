package com.xmut.tearestaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.dto.DishDto;
import com.xmut.tearestaurant.entity.Category;
import com.xmut.tearestaurant.entity.Dish;
import com.xmut.tearestaurant.entity.DishFlavor;
import com.xmut.tearestaurant.service.CategoryService;
import com.xmut.tearestaurant.service.DishFlavorService;
import com.xmut.tearestaurant.service.DishService;
import com.xmut.tearestaurant.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Bless_Wu
 * @Description 菜品管理
 * @create 2022-10-29 21:12
 */
@RestController
@RequestMapping("/dish")
@Slf4j
@CrossOrigin
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "dish", allEntries = true)
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
//        String key="dish_"+dishDto.getCategoryId()+"_1";
//        redisTemplate.delete(key);
        return R.success("新增成功");
    }

    /**
     * 菜品信息分页 js在food.js中
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象clone 拷贝的是页数之类的
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //获取列表页 这边要将查询出来的list中的分类名称改好 然后再赋值给dto对象 最后将新的list 给到上面的dto
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        for (Dish dish : records) {
            DishDto dishDto = new DishDto();
            //先拿到原来的list中的分类id
            Long categoryId = dish.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * 使用的rest风格
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "dish", allEntries = true)
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
//        //清理某个分类redis
//        String key = "dish_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(key);
        return R.success("新增成功");
    }


    /**
     * 根据条件查询菜品数据
     * 项目优化2022-11-4 。使用springcache优化缓存
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "dish", key = "#dish.categoryId+'_'+#dish.status")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtos = null;
//        String key = "dish" + dish.getCategoryId() + "_" + dish.getStatus();

        //优化阶段2022-11-3
        //从redis中获取缓存数据
//        dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //如果存在 直接返回， 不用查询数据库
//        if (dishDtos != null) return R.success(dishDtos);
        //如果不存在 再查询数据库，将查询到的数据存入redis
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        dishDtos = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
//        redisTemplate.opsForValue().set(key, dishDtos,60, TimeUnit.MINUTES);
        return R.success(dishDtos);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }


     @PostMapping("/status")
    public R<String> status(){
           return null;
     }
}
