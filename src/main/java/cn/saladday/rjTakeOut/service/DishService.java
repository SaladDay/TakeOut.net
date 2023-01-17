package cn.saladday.rjTakeOut.service;

import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    /**
     * 保存菜品的同时存储口味信息
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 更新菜品的同时修改储存的口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);
}
