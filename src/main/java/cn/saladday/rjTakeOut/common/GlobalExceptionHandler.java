package cn.saladday.rjTakeOut.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * 本质是一个切面(内部是切入于异常代码中)
 * @ControllerAdvice 控制层增强器
 * 应该挂在哪些类上面，
 * 具体要干什么我给了你三个注解
 * 你自己写代码
 *
 */
@ControllerAdvice
@Slf4j
@ResponseBody //如果出异常就会被此方法接手，若要返回必须加
public class GlobalExceptionHandler {
    //异常处理器的返回值类型需要和原方法的返回值类型相同


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R repeatUsernameExceptionHandler(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        if(message.contains("Duplicate entry")){
            log.info("异常是{}",message);
            if(message.contains("category")){
                return R.error("菜品重复");
            }
            if(message.contains("username")){
                return R.error("用户名重复");
            }
            return R.error("未知重复");
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R CouldNotDeleteExceptionHandler(CustomException e){
        String message = e.getMessage();
        return R.error(message);
    }
}
