package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.Employee;
import cn.saladday.rjTakeOut.mapper.EmployeeMapper;
import cn.saladday.rjTakeOut.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;



    @Override
    public Employee findUser(Employee employee) {
        return employeeMapper.selectAllByUsername(employee.getUsername());
    }
}
