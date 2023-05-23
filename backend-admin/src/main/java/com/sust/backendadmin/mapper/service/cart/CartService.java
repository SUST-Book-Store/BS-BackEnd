package com.sust.backendadmin.mapper.service.cart;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.dto.CartDto;
import com.sust.backendadmin.mapper.BookMapper;
import com.sust.backendadmin.pojo.Cart;
import com.sust.backendadmin.pojo.Result;

import java.util.List;

public interface CartService extends IService<Cart> {
    Result getPage(Integer pageNum, Integer pageSize, String name);
}
