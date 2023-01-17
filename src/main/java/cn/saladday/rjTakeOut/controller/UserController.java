package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public R<String> login(HttpServletRequest request){
        request.getSession().setAttribute("user",1L);
        return R.success("1");
    }

}
