package com.xmut.tearestaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.entity.Employee;
import com.xmut.tearestaurant.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-21 20:26
 */

@Slf4j
@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 員工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.加密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.查询数据库 根据用户名查询
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        //将条件构造器作为参数传入service中进行查询
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到返回登录失败结果
        if (emp == null) {
            return R.error("登录失败");
        }
        //4.密码比对 不一致返回登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //5.查看员工状态，如果为禁用状态 则返回员工已经禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //6.最后一步 将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中保存的员工
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * 这里记录一下，下面91到95行为公告数据 也就是在更新或者新增的时候都需要填写的公共代码
     * 每次都写很麻烦 所以使用mp来统一管理自动填充
     * 1.首先需要在employee实体中 在需要自动填充的字段上添加注解
     * @TableField(fill = FieldFill.INSERT) 括号内指明需要在什么操作下进行自动填充
     * 然后在common包下面新建MyMetaObjectHandle 剩余注解在那个包在记录
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("{}", employee);
        //设置初始密码 使用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //1.构造分页构造器
        //当前在第几页 查几条
        Page pageInfo = new Page(page, pageSize);
        //2.条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //3.执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
//        //        log.info(employee.toString());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long emp = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(emp);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null)
            return R.success(employee);
       return R.error("没有该员工");
    }
}
