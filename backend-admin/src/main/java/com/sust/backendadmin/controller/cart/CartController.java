package com.sust.backendadmin.controller.cart;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.mapper.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public Result update(@RequestBody Cart cart) {
        cart.setUserId(1); // 获取用户id并赋值
        cartService.updateById(cart);
        return Result.ok();
    }

    @PostMapping("/add")
    public Result add(@RequestBody Cart cart) {
        Integer bookId = cart.getBookId();
        Integer userId = 1; //获取userId

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
            cart.setUserId(1);//获取id
        }
        cartService.save(cart);
        return Result.ok();
    }

    @GetMapping("/page")
    public Result getPage(@RequestParam Map<String, String> data) {
        if (StrUtil.isBlank(data.get("pageNum")) || StrUtil.isBlank(data.get("pageSize"))) {
            return Result.fail("传入参数有误");
        }
        String name = data.get("name");
        Integer pageNum =Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return cartService.getPage(pageNum, pageSize, name);
    }

}
