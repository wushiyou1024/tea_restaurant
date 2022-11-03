package com.xmut.tearestaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.entity.User;
import com.xmut.tearestaurant.service.UserService;
import com.xmut.tearestaurant.utils.SMSUtils;
import com.xmut.tearestaurant.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-01 16:50
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位随机号
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云
//            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            //保存验证码到session
            log.info(code);
//            session.setAttribute(phone, code);
            //验证码不用保存到session中，改为存入到redis中,并且有效期5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("手机验证码发送成功");
        }

        return R.success("手机验证码发送失败");
    }


    /**
     * 移动端用户登录
     *
     * @param
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map + "");
        //获取map中的手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //从session中获取验证码
//        Object codeInSession = session.getAttribute(phone);
        //从redis中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        //验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //比对成功 登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断手机号是否为新用户 如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //
            session.setAttribute("user", user.getId());

            //如果用户登录成功了 就从redis中删除这个验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }

        return R.error("登录失败");
    }
}
