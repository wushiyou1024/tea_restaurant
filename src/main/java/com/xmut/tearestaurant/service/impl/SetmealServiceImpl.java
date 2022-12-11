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

    @Override
    public SetmealDto getSetmealAndSetmealDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        //2.查询当前套餐下的所有菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        //1.更新setmeal表
        this.updateById(setmealDto);
        //2.删除setmeal_dish中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //3.将dishs中的id设置成当前sto的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //4.保存
        setmealDishService.saveBatch(setmealDishes);

    }
}
