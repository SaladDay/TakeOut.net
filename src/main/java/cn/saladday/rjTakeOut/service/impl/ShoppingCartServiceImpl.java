package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.ShoppingCart;
import cn.saladday.rjTakeOut.mapper.ShoppingCartMapper;
import cn.saladday.rjTakeOut.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {
}
