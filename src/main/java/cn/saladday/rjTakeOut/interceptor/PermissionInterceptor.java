package cn.saladday.rjTakeOut.interceptor;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Employee;
import cn.saladday.rjTakeOut.mapper.EmployeeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Long employeeId = (Long)session.getAttribute("employee");
        if(employeeId!=null) {
            Employee employee = employeeMapper.selectById(employeeId);
            log.info("权限拦截器开始运行,当前用户名为{}",employee.getUsername());
            if("admin".equals(employee.getUsername())){
                return true;
            }else {

                response.setContentType("application/json;charset=utf-8");
                String s = new ObjectMapper().writeValueAsString(R.error("无权限，请勿操作"));
                response.getWriter().write(s);
                return false;
            }

        }else{
            response.setContentType("application/json;charset=utf-8");
            String s = new ObjectMapper().writeValueAsString(R.error("无权限，请勿操作"));
            response.getWriter().write(s);
            return false;
        }


    }
}
