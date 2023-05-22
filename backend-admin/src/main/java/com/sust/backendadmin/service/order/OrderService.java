package com.sust.backendadmin.service.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.Result;

public interface OrderService extends IService<Order> {
    Result add(OrderDto orderDto);
}
