package com.sust.backendadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sust.backendadmin.dto.CartDto;
import com.sust.backendadmin.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    List<CartDto> unionPage(String name, Integer userId, Integer pageNum, Integer pageSize);
}
