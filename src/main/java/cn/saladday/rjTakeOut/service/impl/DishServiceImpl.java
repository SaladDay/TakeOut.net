package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.mapper.DishMapper;
import cn.saladday.rjTakeOut.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
