package com.sust.backendadmin.service.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.dto.DetailOrderDto;
import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.dto.SearchOrderDto;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.Result;

import java.util.List;

public interface OrderService extends IService<Order> {

    Result getPage(Integer pageNum, Integer pageSize);

    Result add(OrderDto orderDto); //从购物车中下单

    Result addOrder(DetailOrderDto detailOrderDto); //直接下单

    Result getOrderInfoById(Integer orderId);

    Result listOrder(SearchOrderDto searchOrderDto);



    Result send(List<Integer> ids);
}
