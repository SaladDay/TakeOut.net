package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.domain.DishFlavor;
import cn.saladday.rjTakeOut.domain.SetmealDish;
import cn.saladday.rjTakeOut.dto.DishDto;
import cn.saladday.rjTakeOut.dto.SetmealDto;
import cn.saladday.rjTakeOut.service.CategoryService;
import cn.saladday.rjTakeOut.service.DishFlavorService;
import cn.saladday.rjTakeOut.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;

    @PostMapping
    @CacheEvict(value = "dishes",key = "'dish_'+#dishDto.categoryId")
    public R<String> save(@RequestBody DishDto dishDto){
//        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("");
    }

    @GetMapping("/{id}")
    private R<DishDto> getOne(@PathVariable("id") Long id ){
        DishDto dishDto = new DishDto();
        //通过dishID获取菜品信息
        Dish dish = dishService.getById(id);
        //拷贝入dishDto
        BeanUtils.copyProperties(dish,dishDto);
        //通过dishID获取口味信息list
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<DishFlavor>();
        lqw.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(lqw);
        //拷贝入dishDto
        dishDto.setFlavors(dishFlavors);

        return R.success(dishDto);
    }

    @GetMapping("/list")
    @Cacheable(value = "dishes",key ="'dish_'+#categoryId",unless = "#result==null")
    public R<List<DishDto>> getlist(Long categoryId){
        List<DishDto> DishDtoList = null;
//        String key = "dish_"+categoryId;
//        //查询redis是否存在数据
//        DishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
//        if(DishDtoList!=null){
//            log.info("{}数据从redis中取出",key);
//            return R.success(DishDtoList);
//        }

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<Dish>();
        lqw.eq(categoryId!=null,Dish::getCategoryId,categoryId);
        lqw.eq(Dish::getStatus,1);
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lqw);

        DishDtoList = list.stream().map((dish)->{
            DishDto temp = new DishDto();
            BeanUtils.copyProperties(dish,temp);
            LambdaQueryWrapper<DishFlavor> lqw2 = new LambdaQueryWrapper<DishFlavor>();
            lqw2.eq(DishFlavor::getDishId,dish.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lqw2);
            temp.setFlavors(dishFlavors);
            return temp;
        }).collect(Collectors.toList());
//        redisTemplate.opsForValue().set(key,DishDtoList);
//        log.info("{}数据从mySQL中取出",key);

        return R.success(DishDtoList);
    }

    @GetMapping("/page")
    //一般用于后台，不用进行缓存
    public R<Page<DishDto>> pageQuery(Integer page,Integer pageSize,String name){
        //配置分页构造器
        Page<Dish> ipage = new Page<Dish>(page,pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null,Dish::getName,name);
        lqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(ipage,lqw);
        //ipage中记录中只有categoryid，需要通过category查询出categoryName并且存储在dtoPage中
        //除records中dtoPage比ipage多一个categoryName以外，其他的数据都相同，因此需要使用BeanUtils拷贝属性

        Page<DishDto> dtoPage = new Page<DishDto>();
        BeanUtils.copyProperties(ipage,dtoPage,"records");
        List<Dish> dishRecords = ipage.getRecords();
        List<DishDto> dishDtoRecords = dishRecords.stream().map((item)->{
            DishDto tmp = new DishDto();
            BeanUtils.copyProperties(item,tmp);
            Long categoryId = item.getCategoryId();
            String category = categoryService.getById(categoryId).getName();
            tmp.setCategoryName(category);
            return tmp;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dishDtoRecords);


        return R.success(dtoPage);


    }

    @PutMapping
    @CacheEvict(value = "dishes",key = "'dish_'+#dishDto.categoryId")
    public R<String> edit(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("");

    }

    @PostMapping("/status/{status}")
    @CacheEvict(value = "dishes",allEntries = true)
    public R<String> changeStatus(@PathVariable("status") int status,Long ids){
        LambdaUpdateWrapper<Dish> luw = new LambdaUpdateWrapper<Dish>();
        luw.set(Dish::getStatus,status);
        luw.eq(Dish::getId,ids);



        dishService.update(luw);
        return R.success("");
    }
}
