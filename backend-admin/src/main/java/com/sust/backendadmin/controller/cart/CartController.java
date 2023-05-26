package com.sust.backendadmin.controller.cart;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.cart.CartService;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}")
    public Result deleteById(@PathVariable Integer cartId) {
        if (cartService.getById(cartId) == null) {
            return Result.fail("商品已被删除");
        }
        cartService.removeById(cartId);
        return Result.ok();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Cart cart, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }
        cart.setUserId(userId); // 获取用户id并赋值
        cartService.updateById(cart);
        return Result.ok();
    }

    @PostMapping("/add")
    public Result add(@RequestBody Cart cart, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }

        Integer bookId = cart.getBookId();

        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("book_id", bookId);
        Cart one = cartService.getOne(queryWrapper);
        if (one != null) {
            one.setAmount(one.getAmount() + cart.getAmount());
            cartService.updateById(one);
            return Result.ok();
        }

        System.out.println(cart);
        if(cart.getCartId() == null) {
            Date now = new Date();
            cart.setTime(now);
            cart.setUserId(userId);//获取id
        }
        cartService.save(cart);
        return Result.ok();
    }

    @GetMapping("/page")
    public Result getPage(@RequestParam Map<String, String> data, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        int userId = UserTokenUtil.GetUserIdByToken(userToken);//獲取userId
        if (userId == -1) {
            return Result.fail("登錄過期！請重新登錄！");
        }
        if (StrUtil.isBlank(data.get("pageNum")) || StrUtil.isBlank(data.get("pageSize"))) {
            return Result.fail("传入参数有误");
        }
        String name = data.get("name");
        Integer pageNum =Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return cartService.getPage(pageNum, pageSize, name, userId);
    }

}
