package com.sust.backendadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sust.backendadmin.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
