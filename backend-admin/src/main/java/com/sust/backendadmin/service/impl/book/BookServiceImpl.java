package com.sust.backendadmin.service.impl.book;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.mapper.BookMapper;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public JSONObject getPage(Integer pageNum, Integer pageSize) {
        IPage<Book> bookIPage = new Page<>(pageNum, pageSize);
        QueryWrapper<Book> bookQueryWrapper =new QueryWrapper<>();
        bookQueryWrapper.orderByDesc("book_id");
        List<Book> books = bookMapper.selectPage(bookIPage, bookQueryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        resp.put("records", books);
        resp.put("total", bookMapper.selectCount(null));
        return resp;
    }
}
