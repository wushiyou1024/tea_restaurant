package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.DishFlavor;
import com.xmut.tearestaurant.mapper.DishFlavorMapper;
import com.xmut.tearestaurant.mapper.DishMapper;
import com.xmut.tearestaurant.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-29 21:09
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
