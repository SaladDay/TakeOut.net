package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.User;
import cn.saladday.rjTakeOut.service.UserService;
import cn.saladday.rjTakeOut.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.invoke.LambdaConversionException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody Map<String,Object> map){
        String phone = (String)map.get("phone");
        // 获取4位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //加入Redis中并且设置过期时间
        String key = "Validate"+phone;
        redisTemplate.delete(key);
        redisTemplate.opsForValue().set(key,validateCode,5, TimeUnit.MINUTES);
        //log中打印出来
        log.info("验证码为{}",validateCode);
        return R.success("");

    }
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, @RequestBody Map<String,Object> map){
        String phone = (String)map.get("phone");
        String code = (String)map.get("code");
        String key  = "Validate"+phone;
        Object codeObj = redisTemplate.opsForValue().get(key);

        if(codeObj==null){
            throw new CustomException("验证码未获取或失效");
        }

        String codeStr = String.valueOf(codeObj);
        //TODO 测试用例需要修改
        if(!codeStr.equals(code)){
            throw new CustomException("验证码错误");
        }
        redisTemplate.delete(key);
        //查询是否有此用户
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getPhone,phone);
        User user = userService.getOne(lqw);
        //若有，将其id存入session
        HttpSession session = request.getSession();
        if(user!=null){
            session.setAttribute("user",user.getId());
            return R.success("");
        }
        //没有则在数据库中新建用户
        User register = new User();
        register.setPhone(phone);
        register.setStatus(1);
        userService.save(register);
        session.setAttribute("user",register.getId());
        return R.success("");


    }

}
