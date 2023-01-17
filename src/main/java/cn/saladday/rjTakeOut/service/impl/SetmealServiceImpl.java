package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Setmeal;
import cn.saladday.rjTakeOut.domain.SetmealDish;
import cn.saladday.rjTakeOut.dto.SetmealDto;
import cn.saladday.rjTakeOut.mapper.SetmealMapper;
import cn.saladday.rjTakeOut.service.SetmealDishService;
import cn.saladday.rjTakeOut.service.SetmealService;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional
    public void saveWithDishes(SetmealDto setmealDto) {
        //存储套餐信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //给list中的每一项添加setmealId
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteByIds(Long[] ids) {
        for (Long id : ids) {
            //如果在售，则不能删除
            Integer status = this.getById(id).getStatus();
            if(status==1){
                String name = this.getById(id).getName();
                throw new CustomException(name+" 在售，不可删除");
            }
            //删除setmeal表中的数据
            this.removeById(id);
            //删除setmealDish表中关联的数据
            LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<SetmealDish>();
            lqw.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(lqw);
        }
    }
}
