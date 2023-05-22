package com.sust.backendadmin.controller.order;

import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public Result add(@RequestBody OrderDto orderDto) {
        return orderService.add(orderDto);
    }
}
