package com.xmut.tearestaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.tearestaurant.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:50
 */
@Mapper
public interface DishMapper  extends BaseMapper<Dish> {
}
