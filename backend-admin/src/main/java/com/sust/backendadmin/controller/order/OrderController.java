package com.sust.backendadmin.controller.order;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sust.backendadmin.dto.DetailOrderDto;
import com.sust.backendadmin.dto.OrderDto;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/cancel/{id}")
    public Result cancelOrder(@PathVariable Integer id) {
        if (id == null) {
            return Result.fail("订单已取消");
        }
        boolean success = orderService.update().eq("order_id", id).set("status", 2).update();
        if (!success) {
            return Result.fail("取消失败，请稍后再试");
        }
        return Result.ok();
    }

    @GetMapping("/pay/{id}")
    public Result payOrder(@PathVariable Integer id) {
        if (id == null) {
            return Result.fail("非法请求");
        }
        boolean success = orderService.update().eq("order_id", id).set("status", 1).set("pay_time",new Date()).update();
        if (!success) {
            return Result.fail("付款失败，请稍后再试");
        }
        return Result.ok();
    }

    @GetMapping("/get/{id}")
    public Result getOrderInfoById(@PathVariable Integer id) {
        if (id == null) {
            return Result.fail("订单已过期");
        }
        return orderService.getOrderInfoById(id);
    }

    @GetMapping("/page")
    public Result add(@RequestParam Map<String, String> data, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }
        if (StrUtil.isBlank(data.get("pageNum")) || StrUtil.isBlank(data.get("pageSize"))) {
            return Result.fail("传入参数有误");
        }
        Integer pageNum =Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return orderService.getPage(pageNum, pageSize, userId);
    }
    //直接加入
    @PostMapping("/detail/add")
    public Result addOrder(@RequestBody DetailOrderDto detailOrderDto, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }
        return orderService.addOrder(detailOrderDto, userId);
    }

    //从购物车中加入
    @PostMapping("/cart/add")
    public Result add(@RequestBody OrderDto orderDto, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }
        return orderService.add(orderDto, userId);
    }
}
