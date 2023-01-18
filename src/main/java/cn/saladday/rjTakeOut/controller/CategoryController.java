package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Category;
import cn.saladday.rjTakeOut.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R insertCategory(@RequestBody Category category){
        redisTemplate.delete("category");
        categoryService.save(category);
        return R.success("");
    }

    @GetMapping("/page")
    public R<Page<Category>> pageQuery(Integer page,Integer pageSize){
        Page<Category> ipage = new Page<Category>();
        ipage.setCurrent(page);
        ipage.setSize(pageSize);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);

        categoryService.page(ipage,lqw);
        return R.success(ipage);

    }

    @GetMapping("list")
    public R<List<Category>> listQuery(Integer type){
        List<Category> list = null;
        String key = "category";
        //查询缓存中是否有数据
        list = (List<Category>) redisTemplate.opsForValue().get(key);
        if(list!=null){
            log.info("从redis中取出的category数据");
            return R.success(list);
        }
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<Category>();
        lqw.eq(type!=null,Category::getType,type);
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        list = categoryService.list(lqw);
        //存入缓存
        redisTemplate.opsForValue().set(key,list);
        log.info("从mysql中取出的category数据");
        return R.success(list);
    }

    @DeleteMapping
    public R delete(Long ids){
        categoryService.remove(ids);
        redisTemplate.delete("category");
        return R.success("");
    }

    @PutMapping
    public R update(@RequestBody Category category){
        redisTemplate.delete("category");
        categoryService.updateById(category);
        return R.success("");
    }


}
