package com.xmut.tearestaurant.filter;

import com.alibaba.fastjson.JSON;
import com.xmut.tearestaurant.common.BaseContext;
import com.xmut.tearestaurant.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Line;
import java.io.IOException;

/**
 * @author Bless_Wu
 * @Description 检查用户是否已经登陆过
 * @create 2022-10-22 19:35
 */
@Slf4j

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到请求");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的url
        String uri = request.getRequestURI();
        log.info("拦截到请求{}", uri);
        //定义不需要请求的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        //2.本次请求是否需要检查登录状态
        boolean check = check(urls, uri);
        //3.如果不要处理 放行
        if (check) {
            log.info("不需要处理{}", uri);
            filterChain.doFilter(request, response);
            return;
        }
        //4.判断登录状态，如果已经登录 则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        //4-2.判断移动用户登录状态，如果已经登录 则直接放行
        if (request.getSession().getAttribute("user") != null) {
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("拦截未登录{}", uri);
        //5.如果未登录 则返回未登录结果,通过输出流的方式向客户端响应
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配 检查本次请求是否需要放行
     *
     * @param requestUrI
     * @return
     */
    public boolean check(String urls[], String requestUrI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrI);
            if (match == true)
                return true;
        }
        return false;
    }
}
