package cn.saladday.rjTakeOut.service;

import cn.saladday.rjTakeOut.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {


    /**
     * 根据id删除category
     * 但是在删除之前需要判断该category是否有下属
     * @param id
     */
    public void remove(Long id);


}
