package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.User;
import com.xmut.tearestaurant.mapper.UserMapper;
import com.xmut.tearestaurant.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-01 16:49
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
