package com.sust.backendadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sust.backendadmin.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginMapper extends BaseMapper<User> {
}
