package com.xmut.tearestaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xmut.tearestaurant.common.BaseContext;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.entity.ShoppingCart;
import com.xmut.tearestaurant.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 14:31
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class CartController {
    @Autowired
    private ShoppingService shoppingService;

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list()
    {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());
        //设置当前购物车的用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品或者套餐
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (dishId != null) {
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
//            添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart cart = shoppingService.getOne(queryWrapper);

        //如果已经存在 就在原来数量上加一
        if (cart!=null){
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingService.updateById(cart);
        }else {
            shoppingCart.setNumber(1);
            shoppingService.save(shoppingCart);
            cart=shoppingCart;
        }
        cart.setCreateTime(LocalDateTime.now());
        //如果不存在 就添加到购物车
        return R.success(cart);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingService.remove(queryWrapper);
        return R.success("清空成功");
    }
}
