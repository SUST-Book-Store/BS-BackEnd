package com.sust.backendadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sust.backendadmin.pojo.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
