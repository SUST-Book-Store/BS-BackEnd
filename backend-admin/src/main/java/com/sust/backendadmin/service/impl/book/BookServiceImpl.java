package com.sust.backendadmin.service.impl.book;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.PageListDto;
import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.mapper.BookMapper;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import com.sust.backendadmin.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
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

    @Override
    public Result lists(SearchBooksDto searchBooksDto) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(searchBooksDto.getName()))
        {
            wrapper.like(Book::getName,searchBooksDto.getName());
        }
        if (StringUtils.isNotBlank(searchBooksDto.getAuthor()))
        {
            wrapper.like(Book::getAuthor,searchBooksDto.getAuthor());
        }
        if (StringUtils.isNotBlank(searchBooksDto.getType()))
        {
            wrapper.like(Book::getCategory,searchBooksDto.getType());
        }
        if (searchBooksDto.getStatus()!=-1)
        {
            wrapper.eq(Book::getAvailable,searchBooksDto.getStatus());
        }
        if (CollectionUtils.isNotEmpty(searchBooksDto.getDates()))
        {
            wrapper.between(Book::getPublisher,searchBooksDto.getDates().get(0),searchBooksDto.getDates().get(1));
        }
        Page<Book> page = new Page<>( searchBooksDto.getPageNum(),searchBooksDto.getPageSize() );
        page = this.page(page, wrapper);
        PageListDto pageListDto = new PageListDto();
        pageListDto.setBookList(page.getRecords());
        pageListDto.setPages((int) page.getPages());
        pageListDto.setTotal((int) page.getTotal());
        return Result.ok(pageListDto);

    }

    @Override
    public Result add(Book book) {

        return null;
    }

}
