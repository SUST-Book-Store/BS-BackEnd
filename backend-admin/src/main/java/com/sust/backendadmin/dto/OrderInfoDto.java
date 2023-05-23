package com.sust.backendadmin.dto;

import com.sust.backendadmin.pojo.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDto {
    private Book book;
    private Integer amount;
}
