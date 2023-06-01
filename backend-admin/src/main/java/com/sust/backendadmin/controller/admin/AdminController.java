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

import javax.servlet.http.HttpServletRequest;
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
    public Result lists(@RequestBody SearchBooksDto searchBooksDto, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.lists(searchBooksDto);
        } else {
            return Result.fail("你没有权限");
        }
    }

    @PostMapping("/userList")
    public Result userList(@RequestBody SearchUserDto userDto, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return userService.lists(userDto);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/books/delete")
    public Result delete(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.delete(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/add")
    public Result add(@RequestBody Book book, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.add(book);
        } else {
            return Result.fail("你没有权限");
        }

    }

    //
    @PostMapping("/books/up")
    public Result up(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.up(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/books/down")
    public Result down(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.down(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/books/savebook")
    public Result saveBook(@RequestBody BookDto book, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.saveBook(book);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/getBooks")
    public Result down(@RequestParam("id") int bookid, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            Book book = bookService.getById(bookid);
            String detail = book.getDetail();
            List<String> detailList = Arrays.asList(detail.split(";"));
            BookDto bookDto = new BookDto();
            BeanUtils.copyProperties(book, bookDto);
            bookDto.setDetail(detailList);
            return Result.ok(bookDto);
        } else {
            return Result.fail("你没有权限");
        }

    }

    //    @PostMapping("/books/img")
//    public Result uploadImg(@RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request)
//    {
//        String userToken = request.getHeader("token");
//        boolean is_admin = userService.checkIfisAdminByToken(userToken);
//        if (is_admin) {
//            return bookService.upload(file);
//        } else {
//            return Result.fail("你没有权限");
//        }
//    }
    @PostMapping("/books/img")
    public Result uploadImg(@RequestParam(value = "file", required = false) MultipartFile file) {
        return bookService.upload(file);
    }

    @PostMapping("/books/save")
    public Result down(@RequestBody BookDto book, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return bookService.updateByBookId(book);

        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/user/delete")
    public Result deleteUser(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return userService.deleteUser(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    //提高用户权限
    @PostMapping("/user/up")
    public Result userUp(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return userService.up(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/user/down")
    public Result userDown(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return userService.down(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/listOrder")
    public Result listOrder(@RequestBody SearchOrderDto searchOrderDto, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return orderService.listOrder(searchOrderDto);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/orders/send")
    public Result sendOrder(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return orderService.send(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }

    @PostMapping("/orders/delete")
    public Result deleteOrder(@RequestBody List<Integer> ids, HttpServletRequest request) {
        String userToken = request.getHeader("token");
        boolean is_admin = userService.checkIfisAdminByToken(userToken);
        if (is_admin) {
            return orderService.deleteByIds(ids);
        } else {
            return Result.fail("你没有权限");
        }

    }
}
