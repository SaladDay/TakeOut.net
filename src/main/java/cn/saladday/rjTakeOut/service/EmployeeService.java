package cn.saladday.rjTakeOut.service;

import cn.saladday.rjTakeOut.domain.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {

    public Employee findUser(Employee employee);

}
