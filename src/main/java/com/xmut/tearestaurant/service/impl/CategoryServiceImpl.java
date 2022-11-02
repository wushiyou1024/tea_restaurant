package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.common.CustomException;
import com.xmut.tearestaurant.entity.Category;
import com.xmut.tearestaurant.entity.Dish;
import com.xmut.tearestaurant.entity.Setmeal;
import com.xmut.tearestaurant.mapper.CategoryMapper;
import com.xmut.tearestaurant.service.CategoryService;
import com.xmut.tearestaurant.service.DishService;
import com.xmut.tearestaurant.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

   @Autowired
   private DishService dishService;
   @Autowired
   private SetmealService setmealService;


    /**
     * 根据id删除分类 删除之前判断是否有关联菜品和套餐
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);

        //如果已经关联了菜品 抛出业务异常
        if (count>0){
            throw  new CustomException("当前分类关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        //如果已经关联了套餐 抛出业务异常
        if (count1>0){
            throw  new CustomException("当前分类关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
