package com.sust.backendadmin.service.impl.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.mapper.CartMapper;
import com.sust.backendadmin.mapper.OrderBooksMapper;
import com.sust.backendadmin.mapper.OrderMapper;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.OrderBooks;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.utils.OrderIdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderIdCreate orderIdCreate;

    @Autowired
    private OrderBooksMapper orderBooksMapper;

    @Transactional
    @Override
    public Result add(OrderDto orderDto) {
        Order order = new Order();
        Integer userId = 1;
        order.setUserId(userId);
        order.setNo(orderIdCreate.nextId());
        order.setStatus(0);
        order.setTotalPrice(orderDto.getTotalPrice());
        Date now = new Date();
        order.setTime(now);
        //创建订单
        save(order);

        List<Cart> carts = orderDto.getCarts();
        for(Cart cart : carts) {
            OrderBooks orderBooks = new OrderBooks();
            orderBooks.setBookId(cart.getBookId());
            orderBooks.setAmount(cart.getAmount());
            orderBooks.setOrderId(order.getOrderId());
            orderBooksMapper.insert(orderBooks);

            //删除购物车数据
            cartMapper.deleteById(cart.getCartId());
        }
        return Result.ok();
    }
}
