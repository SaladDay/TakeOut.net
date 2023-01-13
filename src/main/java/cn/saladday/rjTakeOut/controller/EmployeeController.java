package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Employee;
import cn.saladday.rjTakeOut.service.EmployeeService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.time.LocalDateTime;
import java.util.Objects;

@ResponseBody
@Controller
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //校验参数
        if(employee.getPassword()==null || employee.getUsername()==null){
            return R.error("请输入完整的用户名和密码");
        }

        //密码加密MD5
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        //查询用户名
        Employee selectUsernameEmployee = employeeService.findUser(employee);
        if(selectUsernameEmployee==null){
            return R.error("用户名不存在");
        }
        //比对密码
        if(!Objects.equals(selectUsernameEmployee.getPassword(), password)){
            return R.error("密码输入错误");
        }
        //查看员工状态
        if(selectUsernameEmployee.getStatus() == 0){
            return R.error("账户已禁用");
        }
        //登入成功将id存入Session，返回
        HttpSession session = request.getSession();
        session.setAttribute("employee",selectUsernameEmployee.getId());
        selectUsernameEmployee.setPassword(null);
        return R.success(selectUsernameEmployee);
    }

    @PostMapping("/logout")
    public R<Object> logout(HttpServletRequest request){
        HttpSession session = request.getSession();

        session.removeAttribute("employee");
        return R.success(null);
    }

    @PostMapping
    public R<Object> save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        Long managerId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateUser(managerId);
//        employee.setUpdateUser(managerId);
        employee.setStatus(1);
        boolean save;
        save = employeeService.save(employee);
        if(save){
            return R.success("");
        }else {
            return R.error("服务器异常，添加失败");
        }
    }

    @GetMapping("/page")
    private R<Page<Employee>> pageQuery(int page,int pageSize,String name){
        //创建分页构造器,数据包裹器
        Page<Employee> pageInfo = new Page<Employee>(page,pageSize);

        //创建条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<Employee>();
        lqw.like(!StringUtils.isEmpty(name),Employee::getName,name);
        lqw.orderByDesc(Employee::getCreateTime);

        employeeService.page(pageInfo, lqw);
        return R.success(pageInfo);






    }

    @PutMapping("/adjust")
    private R adjust(HttpSession session,@RequestBody Employee employee){
        Long empId = (Long)session.getAttribute("employee");

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
//        log.info("{}信息更改",employee);
        employeeService.updateById(employee);

        return R.success("");

    }

    @GetMapping("/{id}")
    private R<Employee> queryAnEmployeeById(@PathVariable("id") Long id){
        Employee byId = employeeService.getById(id);
        return R.success(byId);
    }

    @PutMapping
    private R<Object> updateAnEmployee(HttpSession session,@RequestBody Employee employee){
        Long empId = (Long)session.getAttribute("employee");
        employeeService.updateById(employee);
        return R.success("");
    }


}
