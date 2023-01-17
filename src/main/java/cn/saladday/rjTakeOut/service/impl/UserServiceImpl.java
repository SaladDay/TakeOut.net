package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.User;
import cn.saladday.rjTakeOut.mapper.UserMapper;
import cn.saladday.rjTakeOut.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
