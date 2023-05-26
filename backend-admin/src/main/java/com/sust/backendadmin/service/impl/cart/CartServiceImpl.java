package com.sust.backendadmin.service.impl.cart;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.mapper.CartMapper;
import com.sust.backendadmin.service.cart.CartService;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    public Result getPage(Integer pageNum, Integer pageSize, String name, Integer userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Integer pageBegin = (pageNum - 1) * pageSize;
        return Result.ok(cartMapper.unionPage(name, userId, pageBegin, pageSize), cartMapper.selectCount(queryWrapper));
    }
}
