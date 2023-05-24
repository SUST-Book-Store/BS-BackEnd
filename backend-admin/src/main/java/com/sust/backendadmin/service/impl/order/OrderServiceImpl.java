package com.sust.backendadmin.service.impl.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.DetailOrderDto;
import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.dto.OrderInfoDto;
import com.sust.backendadmin.mapper.CartMapper;
import com.sust.backendadmin.mapper.OrderBooksMapper;
import com.sust.backendadmin.mapper.OrderMapper;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.pojo.*;
import com.sust.backendadmin.service.book.BookService;
import com.sust.backendadmin.utils.OrderIdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private BookService bookService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderIdCreate orderIdCreate;

    @Autowired
    private OrderBooksMapper orderBooksMapper;

    @Override
    public Result getPage(Integer pageNum, Integer pageSize) {
        IPage<Order> orderIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> bookQueryWrapper =new QueryWrapper<>();
        bookQueryWrapper.orderByDesc("order_id");
        List<Order> orders = orderMapper.selectPage(orderIPage, bookQueryWrapper).getRecords();
        return Result.ok(orders, orderMapper.selectCount(null));
    }

    @Transactional
    @Override
    public Result add(OrderDto orderDto) {
        List<Cart> carts = orderDto.getCarts();
        //扣减库存
        for(Cart cart : carts) {
            int bookId = cart.getBookId();
            Book book = bookService.getById(bookId);
            int amount = cart.getAmount();
            boolean success = bookService.update().setSql("stock = stock - " + amount)
                    .eq("book_id", bookId).ge("stock", amount).update();
            if(!success) {
                return Result.fail(book.getName()+ "库存不足");
            }
        }
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

    @Transactional
    @Override
    public Result addOrder(DetailOrderDto detailOrderDto) {
        int bookId = detailOrderDto.getBookId();
        int amount = detailOrderDto.getAmount();
        //判断库存
        boolean success = bookService.update().setSql("stock = stock - " + amount)
                .eq("book_id", bookId).ge("stock", amount).update();
        if (!success) {
            return Result.fail("库存不足");
        }
        Order order = new Order();
        int userId = 1;
        order.setUserId(userId);
        order.setNo(orderIdCreate.nextId());
        order.setStatus(0);

        order.setTotalPrice(detailOrderDto.getPrice().multiply(BigDecimal.valueOf(amount)));
        Date time = new Date();
        order.setTime(time);
        //创建订单
        save(order);
        OrderBooks orderBooks = new OrderBooks();
        orderBooks.setBookId(bookId);
        orderBooks.setOrderId(order.getOrderId());
        orderBooks.setAmount(amount);
        orderBooksMapper.insert(orderBooks);
        return Result.ok();
    }

    @Override
    public Result getOrderInfoById(Integer orderId) {
        QueryWrapper<OrderBooks> orderBooksQueryWrapper = new QueryWrapper<>();
        orderBooksQueryWrapper.eq("order_id", orderId);
        List<OrderBooks> orderBooks = orderBooksMapper.selectList(orderBooksQueryWrapper);
        List<OrderInfoDto> res = new LinkedList<>();
        for (OrderBooks o : orderBooks) {
            Integer bookId = o.getBookId();
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setBook(bookService.getById(bookId));
            orderInfoDto.setAmount(o.getAmount());
            res.add(orderInfoDto);
        }
        return Result.ok(res);
    }
}
