package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.ShoppingCart;
import com.xmut.tearestaurant.mapper.ShoppingCartMapper;
import com.xmut.tearestaurant.service.ShoppingService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 15:14
 */
@Service
public class ShoppingServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingService {
}
