package com.sust.backendadmin.dto;

import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageListDto {
    private List<Order> orderList;
    //用户列表
    private  List<User> UserList;
    //书籍列表
    private List<Book> bookList;
    //总记录数
    private int total;
    //总页数
    private int pages;

}
