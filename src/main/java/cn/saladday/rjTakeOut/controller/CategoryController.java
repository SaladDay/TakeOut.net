package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Category;
import cn.saladday.rjTakeOut.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R insertCategory(@RequestBody Category category){
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

    @DeleteMapping
    private R delete(Long ids){
        categoryService.remove(ids);
        return R.success("");
    }

    @PutMapping
    private R update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("");
    }
}
