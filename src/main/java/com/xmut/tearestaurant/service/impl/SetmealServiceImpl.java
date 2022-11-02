package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.common.CustomException;
import com.xmut.tearestaurant.dto.SetmealDto;
import com.xmut.tearestaurant.entity.Dish;
import com.xmut.tearestaurant.entity.Setmeal;
import com.xmut.tearestaurant.entity.SetmealDish;
import com.xmut.tearestaurant.mapper.DishMapper;
import com.xmut.tearestaurant.mapper.SetmealMapper;
import com.xmut.tearestaurant.service.DishService;
import com.xmut.tearestaurant.service.SetmealDishService;
import com.xmut.tearestaurant.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐 同时需要保存套餐和菜品的关联关系
     *
     * @param
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作的是setmeal表
        this.save(setmealDto);
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品关联的表 setmealdish
        setmealDishService.saveBatch(dishes);
    }


    /**
     * 删除套餐和关联表
     *
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //1.查询套餐状态，确定一下是否为停售

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
//        select count(*) from setmeal where id in (1,2,3) and status=1
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        //如果不能删除 抛出业务异常就好了
        if (count > 0)
            throw new CustomException("有正在售卖的套餐，请先停售");
        //2.如果可以删除的话，先删除套餐中的 数据
        this.removeByIds(ids);
        //3.删除关联表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }
}
