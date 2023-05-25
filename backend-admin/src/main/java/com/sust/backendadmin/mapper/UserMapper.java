package com.sust.backendadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sust.backendadmin.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
