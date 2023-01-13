package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.domain.Category;
import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.domain.Setmeal;
import cn.saladday.rjTakeOut.mapper.CategoryMapper;
import cn.saladday.rjTakeOut.mapper.DishMapper;
import cn.saladday.rjTakeOut.mapper.SetmealMapper;
import cn.saladday.rjTakeOut.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void remove(Long id) {
        //判断当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> lqw1 = new LambdaQueryWrapper<Setmeal>();
        lqw1.eq(Setmeal::getCategoryId,id);
        Integer count1 = setmealMapper.selectCount(lqw1);
        if(count1>0){
            throw new CustomException("当前分类中关联了套餐，不能删除");
        }

        //判断当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> lqw2 = new LambdaQueryWrapper<Dish>();
        lqw2.eq(Dish::getCategoryId,id);
        Integer count2 = dishMapper.selectCount(lqw2);
        if(count2>0){
            throw new CustomException("当前分类中关联了菜品，不能删除");
        }

        //执行删除
        this.removeById(id);
    }
}
