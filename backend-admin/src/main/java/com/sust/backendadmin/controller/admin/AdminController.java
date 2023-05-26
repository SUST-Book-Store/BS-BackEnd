package com.sust.backendadmin.controller.admin;

import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;

    @PostMapping("/lists")
    public Result lists(@RequestBody SearchBooksDto searchBooksDto)
    {

        return bookService.lists(searchBooksDto);
    }
    @PostMapping("/books/delete")
    public Result delete(@RequestBody List<Integer> ids)
    {
        return bookService.delete(ids);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Book book)
    {
        return bookService.add(book);
    }
    //
    @PostMapping("/books/up")
    public Result up(@RequestBody List<Integer> ids)
    {
        return bookService.up(ids);
    }
    @PostMapping("/books/down")
    public Result down(@RequestBody List<Integer> ids)
    {
        return bookService.down(ids);
    }
}
