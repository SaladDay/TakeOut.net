package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.DishFlavor;
import cn.saladday.rjTakeOut.mapper.DishFlavorMapper;
import cn.saladday.rjTakeOut.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
