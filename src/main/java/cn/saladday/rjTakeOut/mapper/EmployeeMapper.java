package cn.saladday.rjTakeOut.mapper;

import cn.saladday.rjTakeOut.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Value;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select * from employee where username = #{username}")
    public Employee selectAllByUsername(String username);


}
