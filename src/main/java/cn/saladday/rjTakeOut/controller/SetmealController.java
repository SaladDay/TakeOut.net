package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.domain.Setmeal;
import cn.saladday.rjTakeOut.domain.SetmealDish;
import cn.saladday.rjTakeOut.dto.SetmealDto;
import cn.saladday.rjTakeOut.service.CategoryService;
import cn.saladday.rjTakeOut.service.SetmealDishService;
import cn.saladday.rjTakeOut.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status,@RequestParam("ids") Long[] ids){
        log.info("更新参数{},更新单位{}",status,ids);

        for (Long id : ids) {
            LambdaUpdateWrapper<Setmeal> luw = new LambdaUpdateWrapper<Setmeal>();
            luw.set(Setmeal::getStatus,status);
            luw.eq(Setmeal::getId,id);
            setmealService.update(luw);
        }
        return R.success("");

    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDishes(setmealDto);
        return R.success("");

    }

    @GetMapping("/{id}")
    public R<SetmealDto> getOne(@PathVariable("id") Long id){
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        //已经将setmeal中的信息放入setmealDto中了，还需要加入SetmealDish[] 信息
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<SetmealDish>();
        lqw.eq(SetmealDish::getSetmealId,id);
        lqw.orderByAsc(SetmealDish::getSort);
        List<SetmealDish> setmealDishes = setmealDishService.list(lqw);

        setmealDto.setSetmealDishes(setmealDishes);
        return R.success(setmealDto);


    }

    @GetMapping("/list")
    public R<List<SetmealDto>> getlist(Long categoryId){
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<Setmeal>();
        lqw.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        lqw.eq(Setmeal::getStatus,1);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lqw);
        //list查出的是setmeal，需要的是setmealDto

        List<SetmealDto> setmealDtoList = list.stream().map((setmeal)->{
            SetmealDto temp = new SetmealDto();
            BeanUtils.copyProperties(setmeal,temp);
            LambdaQueryWrapper<SetmealDish> lqw2 = new LambdaQueryWrapper<SetmealDish>();
            lqw2.eq(SetmealDish::getSetmealId,setmeal.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(lqw2);
            temp.setSetmealDishes(setmealDishes);
            return temp;
        }).collect(Collectors.toList());

        return R.success(setmealDtoList);


    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> pageQuery(int page,int pageSize,String name){
        Page<Setmeal> iPage = new Page<Setmeal>(page,pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<Setmeal>();
        lqw.like(name!=null,Setmeal::getName,name);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(iPage,lqw);
        //原始数据
        Page<SetmealDto> dtoPage = new Page<SetmealDto>();
        //复制页面信息
        BeanUtils.copyProperties(iPage,dtoPage,"records");

        List<Setmeal> records = iPage.getRecords();
        List<SetmealDto> dtoRecords = records.stream().map((item)->{
            SetmealDto temp = new SetmealDto();
            BeanUtils.copyProperties(item,temp);
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            temp.setCategoryName(categoryName);
            return temp;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoRecords);

        return R.success(dtoPage);

    }

    @DeleteMapping

    public R<String> deleteByIds(@RequestParam Long[] ids){
        setmealService.deleteByIds(ids);
        return R.success("");
    }
}
