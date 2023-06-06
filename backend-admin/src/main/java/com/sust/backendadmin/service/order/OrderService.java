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

    Result getPage(Integer pageNum, Integer pageSize, Integer userId);

    Result add(OrderDto orderDto, Integer userId); //从购物车中下单

    Result addOrder(DetailOrderDto detailOrderDto, Integer userId); //直接下单

    Result getOrderInfoById(Integer orderId);

    Result deleteById(Integer orderId, Integer userId);

    Result listOrder(SearchOrderDto searchOrderDto);

    Result send(List<Integer> ids);

    Result deleteByIds(List<Integer> ids);

    Result getByNo(Long no);
}
