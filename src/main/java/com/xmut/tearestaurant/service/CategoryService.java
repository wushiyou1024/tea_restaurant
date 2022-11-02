package com.xmut.tearestaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.tearestaurant.entity.Category;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-24 20:19
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
