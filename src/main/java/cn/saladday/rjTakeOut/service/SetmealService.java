package cn.saladday.rjTakeOut.service;

import cn.saladday.rjTakeOut.domain.Setmeal;
import cn.saladday.rjTakeOut.dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存套餐的同时关联保存dishes
     * @param setmealDto
     */
    public void saveWithDishes(SetmealDto setmealDto);

    void deleteByIds(Long[] ids);
}
