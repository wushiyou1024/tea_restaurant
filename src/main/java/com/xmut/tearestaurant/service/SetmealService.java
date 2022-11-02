package com.xmut.tearestaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.tearestaurant.dto.SetmealDto;
import com.xmut.tearestaurant.entity.Setmeal;

import java.util.List;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:51
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐 同时需要保存套餐和菜品的关联关系
     * @param
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和关联表
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
