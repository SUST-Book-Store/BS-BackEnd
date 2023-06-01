package com.sust.backendadmin.service.book;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.dto.BookDto;
import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends IService<Book> {
    JSONObject getPage(Integer pageNum, Integer pageSize);


    Result lists(SearchBooksDto searchBooksDto);

    Result add(Book book);

    Result delete(List<Integer> ids);

    Result up(List<Integer> ids);

    Result down(List<Integer> ids);

    Result upload(MultipartFile file);

    Result saveBook(BookDto book);

    Result updateByBookId(BookDto book);
}
