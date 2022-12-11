package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.common.CustomException;
import com.xmut.tearestaurant.dto.DishDto;
import com.xmut.tearestaurant.entity.Dish;
import com.xmut.tearestaurant.entity.DishFlavor;
import com.xmut.tearestaurant.mapper.DishMapper;
import com.xmut.tearestaurant.service.DishFlavorService;
import com.xmut.tearestaurant.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:52
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到dish表
        this.save(dishDto);
        Long id = dishDto.getId();//菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味到dishflavor表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);
        //删除口味表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * //根据id查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //1.查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //2.查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    public void deleteWithFlavor(List<Long> ids) {
        //1.先看看要删除的菜品是否是停售的状态
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Dish::getId, ids);
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if (count > 0)
            throw new CustomException("请先停售！");
        //2.可以删除的话先删除dish
        this.removeByIds(ids);
        //3.删除口味表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(queryWrapper);
    }
}
