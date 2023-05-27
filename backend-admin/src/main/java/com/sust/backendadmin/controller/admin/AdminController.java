package com.sust.backendadmin.controller.admin;

import com.sust.backendadmin.dto.BookDto;
import com.sust.backendadmin.dto.SearchBooksDto;
import com.sust.backendadmin.dto.SearchOrderDto;
import com.sust.backendadmin.dto.SearchUserDto;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.service.book.BookService;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/lists")
    public Result lists(@RequestBody SearchBooksDto searchBooksDto)
    {

        return bookService.lists(searchBooksDto);
    }
    @PostMapping("/userList")
    public Result userList(@RequestBody SearchUserDto userDto )
    {


        return  userService.lists(userDto);
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
    @PostMapping("/books/savebook")
    public Result saveBook(@RequestBody Book book)
    {
        return bookService.saveBook(book);
    }
    @PostMapping("/getBooks")
    public Result down(@RequestParam("id") int bookid)
    {
        Book book = bookService.getById(bookid);
        String detail = book.getDetail();
        List<String> detailList = Arrays.asList(detail.split(";"));
        BookDto bookDto = new BookDto();
        BeanUtils.copyProperties(book,bookDto);
        bookDto.setDetail(detailList);
        return Result.ok(bookDto);
    }
    @PostMapping("/books/img")
    public Result uploadImg(@RequestParam(value = "file",required = false) MultipartFile file)
    { return  bookService.upload(file); }
    @PostMapping("/books/save")
    public Result down(@RequestBody BookDto book)
    {
        String detailString = StringUtils.collectionToDelimitedString(book.getDetail(), ";");
        Book book1 = new Book();
        BeanUtils.copyProperties(book,book1);
        book1.setDetail(detailString);
        boolean b = bookService.updateById(book1);
        if (b)
        {
            return Result.ok();
        }
        return Result.fail("编辑失败");
    }
    @PostMapping("/user/delete")
    public Result deleteUser(@RequestBody List<Integer> ids)
    {
        return userService.deleteUser(ids);
    }
    //提高用户权限
    @PostMapping("/user/up")
    public Result userUp(@RequestBody List<Integer> ids)
    {

        return userService.up(ids);
    }
    @PostMapping("/user/down")
    public Result userDown(@RequestBody List<Integer> ids)
    {

        return userService.down(ids);
    }
    @PostMapping("/listOrder")
    public Result listOrder(@RequestBody SearchOrderDto searchOrderDto)
    {
        return orderService.listOrder(searchOrderDto);
    }
    @PostMapping("/orders/send")
    public Result deleteOrder(@RequestBody List<Integer> ids)
    {
        return orderService.send(ids);
    }
}
