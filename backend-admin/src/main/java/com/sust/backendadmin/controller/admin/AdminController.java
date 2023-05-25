package com.sust.backendadmin.controller.admin;

import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;

    @PostMapping("/lists")
    public Result lists(@RequestBody SearchBooksDto searchBooksDto)
    {
        SearchBooksDto booksDto = new SearchBooksDto();
        return bookService.lists(searchBooksDto);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Book book)
    {
        return bookService.add(book);
    }
//    //
//    @PostMapping("/up")
//    public Result upOrDown(@RequestBody )
//    {
//        return bookService.up();
//    }
}
