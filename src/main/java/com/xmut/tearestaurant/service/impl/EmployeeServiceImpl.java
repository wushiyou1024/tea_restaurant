package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.Employee;
import com.xmut.tearestaurant.mapper.EmployeeMapper;
import com.xmut.tearestaurant.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-21 20:25
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
