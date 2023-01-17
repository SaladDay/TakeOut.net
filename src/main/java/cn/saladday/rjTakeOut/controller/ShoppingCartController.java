package cn.saladday.rjTakeOut.controller;


import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.ShoppingCart;
import cn.saladday.rjTakeOut.mapper.ShoppingCartMapper;
import cn.saladday.rjTakeOut.service.ShoppingCartService;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @RequestMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){

        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        //查询：若有当前用户买的同样的东西，则数量加一
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<ShoppingCart>();
        //如果dishId不为null，则比较dishID&&dishFlavor
        lqw.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
//        lqw.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
        //如果setmealid不为NUll，则比较setMealId
        lqw.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        lqw.eq(ShoppingCart::getUserId,userId);
        ShoppingCart one = shoppingCartService.getOne(lqw);
        if(one!=null){
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            return R.success(one);
        }

        shoppingCart.setUserId(userId);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);

        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> getList(){
        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<ShoppingCart>();
        lqw.eq(ShoppingCart::getUserId,userId);
        lqw.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        //减一，如果是0就删除该数据
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<ShoppingCart>();
        if(dishId!=null){
            lqw.eq(ShoppingCart::getDishId,dishId);
        }else if(setmealId!=null){
            lqw.eq(ShoppingCart::getSetmealId,setmealId);
        }else{
            throw new CustomException("数据异常");
        }
        ShoppingCart one = shoppingCartService.getOne(lqw);
        if(one.getNumber()==1){

            shoppingCartService.removeById(one);
            one.setNumber(one.getNumber()-1);
        }else {
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);

        }
        return R.success(one);


    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<ShoppingCart>();
        lqw.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lqw);
        return R.success("");
    }
}
