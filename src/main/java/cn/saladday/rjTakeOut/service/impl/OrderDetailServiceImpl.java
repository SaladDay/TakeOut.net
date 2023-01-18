package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.OrderDetail;
import cn.saladday.rjTakeOut.domain.Orders;
import cn.saladday.rjTakeOut.mapper.OrderDetailMapper;
import cn.saladday.rjTakeOut.mapper.OrdersMapper;
import cn.saladday.rjTakeOut.service.OrderDetailService;
import cn.saladday.rjTakeOut.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
