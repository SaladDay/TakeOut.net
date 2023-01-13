package cn.saladday.rjTakeOut.filter;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Employee;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "checkLoginFilter",urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    private static final AntPathMatcher PATH_MATCH =new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求URL
        String url = String.valueOf(request.getRequestURI());
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**"
        };

        //判断是否需要放行
        if(check(urls,url)){
            //需要->放行
            filterChain.doFilter(request,response);
            return;
        }
        //是否登入
        if(request.getSession().getAttribute("employee")!=null){
            //登入->放行
            ThreadLocalEmpIdDataUtil.setEmpId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        //未登入->跳转到登入界面
        response.getWriter().write(new ObjectMapper().writeValueAsString(R.error("NOTLOGIN")));



    }
    private static boolean check(String[] urls,String url){
        for (String s : urls) {
            if(PATH_MATCH.match(s,url)){
                return true;
            }
        }
        return false;

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
