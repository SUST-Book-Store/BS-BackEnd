package com.sust.backendadmin.controller.book;

import cn.hutool.core.util.StrUtil;
import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/page")
    public Result getList(@RequestParam Map<String, String> data) {
        if (StrUtil.isBlank(data.get("pageNum")) || StrUtil.isBlank(data.get("pageSize"))) {
            return Result.fail("传入参数有误");
        }
        Integer pageNum =Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return Result.ok(bookService.getPage(pageNum, pageSize));
    }

    @GetMapping("/search")
    public Result searchBook(@RequestParam Map<String, String> data) {
        if (StrUtil.isBlank(data.get("keyword")) || StrUtil.isBlank(data.get("page"))) {
            return Result.fail("传入参数有误");
        } else {
            int page = Integer.parseInt(data.get("page"));
            SearchBooksDto searchBooksDto = new SearchBooksDto();
            searchBooksDto.setName("%"+data.get("keyword")+"%");
            searchBooksDto.setPageNum(page);
            searchBooksDto.setPageSize(12);
            return bookService.lists(searchBooksDto);
        }
    }

    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id){
        return Result.ok(bookService.getById(id));
    }
}
