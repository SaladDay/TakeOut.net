package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.domain.*;
import cn.saladday.rjTakeOut.mapper.OrderDetailMapper;
import cn.saladday.rjTakeOut.mapper.OrdersMapper;
import cn.saladday.rjTakeOut.service.*;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private ShoppingCartService shoppingCartService;


    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void submit(Orders orders) {

        //查询用户id
        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        //查询用户信息
        LambdaQueryWrapper<User> lqw3 = new LambdaQueryWrapper<User>();
        lqw3.eq(User::getId,userId);
        User user = userService.getOne(lqw3);
        //查询地址数据
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<AddressBook>();
        lqw.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(lqw);
        BeanUtils.copyProperties(addressBook,orders,"id");
        orders.setAddressBookId(addressBook.getId());
        //完成orders信息
        orders.setUserId(userId);
        long id = IdWorker.getId();
        orders.setNumber(id);
        orders.setStatus(2);
        orders.setOrderTime(LocalDateTime.now());

        AtomicInteger amount = new AtomicInteger(0);
        orders.setUserName(user.getName());
        orders.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName());

        //查询购物车
        LambdaQueryWrapper<ShoppingCart> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(lqw2);
        if(list==null || list.size()==0){
            throw new CustomException("购物车空，请添加菜品");
        }
        List<OrderDetail> orderDetails = list.stream().map((item)->{
            OrderDetail temp = new OrderDetail();

            BeanUtils.copyProperties(item,temp);
            temp.setOrderId(id);
            temp.setAmount(temp.getAmount().add(BigDecimal.valueOf(temp.getNumber())));
            amount.getAndAdd(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return temp;
        }).collect(Collectors.toList());

        orders.setAmount(BigDecimal.valueOf(amount.get()));

        //向orders表存入一条数据
        orders.setCheckoutTime(LocalDateTime.now());
        this.save(orders);

        //向orderDetial表存入多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车
        shoppingCartService.remove(lqw2);
    }
}
