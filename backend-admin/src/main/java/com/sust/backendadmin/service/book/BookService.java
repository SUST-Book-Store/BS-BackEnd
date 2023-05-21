package com.sust.backendadmin.service.book;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.pojo.Book;

import java.util.List;

public interface BookService extends IService<Book> {
    JSONObject getPage(Integer pageNum, Integer pageSize);
}