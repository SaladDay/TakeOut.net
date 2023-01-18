package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.Orders;
import cn.saladday.rjTakeOut.service.OrdersService;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;



    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("");

    }

}
