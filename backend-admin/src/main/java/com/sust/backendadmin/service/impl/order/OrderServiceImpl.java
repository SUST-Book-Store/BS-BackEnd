package com.sust.backendadmin.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.*;
import com.sust.backendadmin.mapper.CartMapper;
import com.sust.backendadmin.mapper.OrderBooksMapper;
import com.sust.backendadmin.mapper.OrderMapper;
import com.sust.backendadmin.mapper.UserMapper;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.pojo.*;
import com.sust.backendadmin.service.book.BookService;
import com.sust.backendadmin.service.orderbooks.OrderBooksService;
import com.sust.backendadmin.service.user.UserService;
import com.sust.backendadmin.utils.OrderIdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderIdCreate orderIdCreate;

    @Autowired
    private OrderBooksMapper orderBooksMapper;

    @Override
    public Result getPage(Integer pageNum, Integer pageSize, Integer userId) {
        IPage<Order> orderIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> bookQueryWrapper =new QueryWrapper<>();
        bookQueryWrapper.orderByDesc("order_id").eq("user_id", userId);
        List<Order> orders = orderMapper.selectPage(orderIPage, bookQueryWrapper).getRecords();
        return Result.ok(orders, orderMapper.selectCount(new QueryWrapper<Order>().eq("user_id", userId)));
    }

    @Transactional
    @Override
    public Result add(OrderDto orderDto, Integer userId) {
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
        QueryWrapper<User> addrQueryWrapper =new QueryWrapper<>();
        addrQueryWrapper.eq("user_id", userId.intValue());
        List<User> userList = userMapper.selectList(addrQueryWrapper);
        String address = "";
        if (userList.isEmpty()) {
            return Result.fail("该用户不存在");
        } else {
            User user = userList.get(0);
            address = user.getAddress();
        }
        if (address == null || address.toString() == "") {
            return Result.fail("invalidaddr");
        }
        Order order = new Order();
        order.setAddress(address.toString());
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
    public Result addOrder(DetailOrderDto detailOrderDto, Integer userId) {
        int bookId = detailOrderDto.getBookId();
        int amount = detailOrderDto.getAmount();
        //判断库存
        boolean success = bookService.update().setSql("stock = stock - " + amount)
                .eq("book_id", bookId).ge("stock", amount).update();
        if (!success) {
            return Result.fail("库存不足");
        }
        QueryWrapper<User> addrQueryWrapper =new QueryWrapper<>();
        addrQueryWrapper.eq("user_id", userId.intValue());
        List<User> userList = userMapper.selectList(addrQueryWrapper);
        String address = "";
        if (userList.isEmpty()) {
            return Result.fail("该用户不存在");
        } else {
            User user = userList.get(0);
            address = user.getAddress();
        }
        if (address == null || address.toString() == "") {
            return Result.fail("invalidaddr");
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setNo(orderIdCreate.nextId());
        order.setStatus(0);
        order.setAddress(address.toString());
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

    @Override
    public Result listOrder(SearchOrderDto searchOrderDto) {

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(searchOrderDto.getNo()))
        {
            String no = searchOrderDto.getNo();
            Long longid = Long.valueOf(no);
            wrapper.like(Order::getNo,longid);
        }
        if (searchOrderDto.getStatus()!=-1)
        {
            wrapper.eq(Order::getStatus,searchOrderDto.getStatus());
        }
        Page<Order> page = new Page<>( searchOrderDto.getPageNum(),searchOrderDto.getPageSize() );
        page = this.page(page, wrapper);
        PageListDto pageListDto = new PageListDto();
        pageListDto.setOrderList(page.getRecords());
        pageListDto.setPages((int) page.getPages());
        pageListDto.setTotal((int) page.getTotal());
        return Result.ok(pageListDto);
    }

    @Override
    public Result send(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Result.fail("未选择");
        List<Order> orderList = this.list(Wrappers.<Order>lambdaQuery().in(Order::getOrderId, ids));
        if (orderList.size()!=ids.size())
            return Result.fail("存在订单缺失");
        boolean b = orderList.stream().anyMatch(order -> order.getStatus() == 0);
        if (b)
        return Result.fail("存在订单未付款");
        boolean c = orderList.stream().anyMatch(order -> order.getStatus() == 2);
        if (c)
            return Result.fail("存在订单已经取消");
        boolean d = orderList.stream().anyMatch(order -> order.getStatus() == 3);
        if (d)
            return Result.fail("存在订单已经发货");
        List<Order> newOrders = orderList.stream()
                .peek(book -> book.setStatus(3))
                .collect(Collectors.toList());
        boolean h = this.updateBatchById(newOrders);
        if (h)
        {
            return Result.ok();
        }else
            return Result.fail("操作失败");

    }
    @Autowired
    private OrderBooksService orderBooksService;
    @Override
    public Result deleteByIds(List<Integer> ids) {
        List<Order> orderList = this.listByIds(ids);
        if (orderList.size()!=ids.size())
            return Result.fail("存在订单缺失");
        //判断订单是否存在未付款
        boolean b = orderList.stream().anyMatch(order -> order.getStatus() == 0);
        if (b)
            return Result.fail("存在订单未付款,不可删除");
        //删除购物车
        boolean c = orderBooksService.remove(Wrappers.<OrderBooks>lambdaQuery().in(OrderBooks::getOrderId, ids));
        if (c)
        {
            boolean byIds = this.removeByIds(ids);
            if (byIds)
            return Result.ok();
            else return Result.fail("删除失败");
        }
        else return Result.fail("删除失败");

    }
}
