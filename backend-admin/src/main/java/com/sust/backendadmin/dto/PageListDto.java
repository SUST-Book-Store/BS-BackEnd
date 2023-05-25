package com.sust.backendadmin.dto;

import com.sust.backendadmin.pojo.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageListDto {
    //书籍列表
    private List<Book> bookList;
    //总记录数
    private int total;
    //总页数
    private int pages;

}
