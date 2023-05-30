package com.sust.backendadmin.service.impl.book;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.PageListDto;
import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.mapper.BookMapper;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private OSSUtils ossUtils;
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
        wrapper.orderByDesc(Book::getCreateTime);
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

    @Override
    public Result delete(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids))
            return Result.fail("未选择");
        List<Book> bookList = this.list(Wrappers.<Book>lambdaQuery().in(Book::getBookId, ids));
        if (bookList.size()!=ids.size())
            return Result.fail("存在图书不存在");
        boolean b = bookList.stream().anyMatch(book -> book.getAvailable() == 1);
        if (b)
            return Result.fail("选择图书中存在上架图书");
        boolean c = this.removeBatchByIds(ids);
        if (c)
        {
            return Result.ok();
        }else
            return Result.fail("操作失败");

    }

    @Override
    public Result up(List<Integer> ids) {
        List<Book> bookList = this.list(Wrappers.<Book>lambdaQuery().in(Book::getBookId, ids));
        if (bookList.size()!=ids.size())
            return Result.fail("存在图书不存在");
        boolean b = bookList.stream().anyMatch(book -> book.getAvailable() == 1);
        if(b)
            return Result.fail("存在书籍已经上架，不可重复上架");
        List<Book> newBooks = bookList.stream()
                .peek(book -> book.setAvailable(1))
                .collect(Collectors.toList());
        this.updateBatchById(newBooks);
        return Result.ok();
    }

    @Override
    public Result down(List<Integer> ids) {
        List<Book> bookList = this.list(Wrappers.<Book>lambdaQuery().in(Book::getBookId, ids));
        if (bookList.size()!=ids.size())
            return Result.fail("存在图书不存在");
        boolean b = bookList.stream().anyMatch(book -> book.getAvailable() == 0);
        if(b)
            return Result.fail("存在书籍已经下架，不可重复上架");
        List<Book> newBooks = bookList.stream()
                .peek(book -> book.setAvailable(0))
                .collect(Collectors.toList());
        this.updateBatchById(newBooks);
        return Result.ok();

    }

    @Override
    public Result upload(MultipartFile file) {
        String s = null;
        try { s = ossUtils.upload(file); }
        catch (IOException e) { e.printStackTrace(); }
        return Result.ok(s);
    }

    @Override
    public Result saveBook(Book book) {
        if (StringUtils.isBlank(book.getName())||StringUtils.isBlank(book.getIsbn())||StringUtils.isBlank(book.getAuthor()))
            return Result.fail("请补全信息");
        book.setCreateTime(new Date());
        boolean save = this.save(book);
        if (save)
            return Result.ok();
        return Result.fail("请补全信息");
    }

}
