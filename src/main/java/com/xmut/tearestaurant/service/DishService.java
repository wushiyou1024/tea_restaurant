package com.xmut.tearestaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.tearestaurant.dto.DishDto;
import com.xmut.tearestaurant.entity.Dish;

import java.util.List;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:51
 */

public interface DishService extends IService<Dish> {
  //新增菜品，同时插入菜品对应的口味数据 操作两张表 dish和dishflavor
     void saveWithFlavor(DishDto dishDto);
     //更新菜品
     void updateWithFlavor(DishDto dishDto);
     //根据id查询菜品信息和口味信息
    DishDto getByIdWithFlavor(Long id);

    void deleteWithFlavor(List<Long> ids);
}
