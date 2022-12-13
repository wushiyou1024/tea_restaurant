package com.xmut.tearestaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.dto.DishDto;
import com.xmut.tearestaurant.dto.SetmealDto;
import com.xmut.tearestaurant.entity.Category;
import com.xmut.tearestaurant.entity.Setmeal;
import com.xmut.tearestaurant.service.CategoryService;
import com.xmut.tearestaurant.service.SetmealDishService;
import com.xmut.tearestaurant.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bless_Wu
 * @Description 套餐管理
 * @create 2022-10-31 21:14
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(tags = "套餐相关的接口")
@CrossOrigin
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    @ApiOperation(value = "新增套餐接口欧")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
//        log.info(""+setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true),
            @ApiImplicitParam(name = "name", value = "套餐名称", required = false),
    })
    public R<Page> page(int page, int pageSize, String name) {
        //1.构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);

        //2.条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //3.添加查询条件
        queryWrapper.like(name != null, Setmeal::getName, name);
        //4.排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }


    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
//        log.info(ids.toString());
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 起手停售操作
     * 采用rest风格
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> status(@PathVariable Long status, @RequestParam List<Long> ids) {
//        System.out.println(ids+"---"+status);
        //拿到需要修改的状态和id
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status.intValue());
            setmealService.updateById(setmeal);
        } ;
        //根据id修改状态
        return R.success("修改成功");
    }

    /**
     * 根据id查询套餐信息，然后将此信息渲染到修改套餐详情中
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
//        DishDto dishDto = dishService.getByIdWithFlavor(id);
//        return R.success(dishDto);
//        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = setmealService.getSetmealAndSetmealDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     */
    @PutMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
//        System.out.println(setmealDto);
          setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }
}
