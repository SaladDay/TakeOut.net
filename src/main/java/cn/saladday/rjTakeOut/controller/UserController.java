package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.User;
import cn.saladday.rjTakeOut.service.UserService;
import cn.saladday.rjTakeOut.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.invoke.LambdaConversionException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpServletRequest request){
        // 获取4位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //加入session中并且设置过期时间

        HttpSession session = request.getSession();
        session.removeAttribute("code");
        session.setAttribute("code",validateCode);
        //log中打印出来
        log.info("验证码为{}",validateCode);
        return R.success("");

    }
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request, @RequestBody Map<String,Object> map){
        String phone = (String)map.get("phone");
        String code = (String)map.get("code");

        HttpSession session = request.getSession();
        Object codeObj = session.getAttribute("code");
        if(codeObj==null){
            throw new CustomException("验证码未获取或失效");
        }
        session.removeAttribute("code");
        String codeStr = String.valueOf(codeObj);
        //TODO 测试用例需要修改
        if(!"1234".equals(code)){
            throw new CustomException("验证码错误");
        }
        //查询是否有此用户
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(User::getPhone,phone);
        User user = userService.getOne(lqw);
        //若有，将其id存入session
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
