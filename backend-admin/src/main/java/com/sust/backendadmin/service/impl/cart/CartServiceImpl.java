package com.sust.backendadmin.service.impl.cart;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.CartDto;
import com.sust.backendadmin.mapper.CartMapper;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    public Result getPage(Integer pageNum, Integer pageSize, String name) {
        return Result.ok(cartMapper.unionPage(name, 1, pageNum, pageSize), cartMapper.selectCount(null));
    }
}
