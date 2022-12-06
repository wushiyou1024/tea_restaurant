package com.xmut.tearestaurant;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bless_Wu
 * @Description
 * 在SpringBootApplication上使用@ServletComponentScan注解后，
 * Servlet（控制器）、Filter（过滤器）、Listener（监听器）
 * 可以直接通过@WebServlet、@WebFilter、@WebListener注解自动注册到Spring容器中，无需其他代码。
 * @create 2022-10-20 21:21
 */

@Slf4j
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class TeaResApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaResApplication.class, args);
        log.info("项目启动成");
    }
}
