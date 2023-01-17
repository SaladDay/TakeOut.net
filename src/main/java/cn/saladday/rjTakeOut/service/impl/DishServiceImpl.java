package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.domain.DishFlavor;
import cn.saladday.rjTakeOut.dto.DishDto;
import cn.saladday.rjTakeOut.mapper.DishMapper;
import cn.saladday.rjTakeOut.service.DishFlavorService;
import cn.saladday.rjTakeOut.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional //由于操作了多张表，建议开启事务管理，不然容易一个加数据一个没数据
    public void saveWithFlavor(DishDto dishDto) {
        //将基本信息存储到dish表
        this.save(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //UpdateDish表中的数据
        this.updateById(dishDto);
        //删除DishFlavor表中的数据
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<DishFlavor>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);
        //新增DishFlavor数据
        //dishDto中的flavors数据只有name和value属性，我们需要把dishId摄入
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
