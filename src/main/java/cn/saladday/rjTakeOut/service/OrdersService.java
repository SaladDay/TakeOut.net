package cn.saladday.rjTakeOut.service;

import cn.saladday.rjTakeOut.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
