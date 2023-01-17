package cn.saladday.rjTakeOut.dto;

import cn.saladday.rjTakeOut.domain.Dish;
import cn.saladday.rjTakeOut.domain.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {

    public List<DishFlavor> flavors;

    public String categoryName;

}
