package com.sust.backendadmin.controller.book;

import cn.hutool.core.util.StrUtil;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books/page")
    public Result getList(@RequestParam Map<String, String> data) {
        if (StrUtil.isBlank(data.get("pageNum")) || StrUtil.isBlank(data.get("pageSize"))) {
            return Result.fail("传入参数有误");
        }
        Integer pageNum =Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));
        return Result.ok(bookService.getPage(pageNum, pageSize));
    }

    @GetMapping("/books/detail/{id}")
    public Result getById(@PathVariable Integer id){
        return Result.ok(bookService.getById(id));
    }
}
