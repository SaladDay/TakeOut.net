package cn.saladday.rjTakeOut.dto;


import cn.saladday.rjTakeOut.domain.Setmeal;
import cn.saladday.rjTakeOut.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    //在Setmeal的基础上增加了SetmealDish的集合
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
