package com.sust.backendadmin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CartDto {
    private Integer cartId;
    private Integer bookId;
    private Integer amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date time;
    private String name;
    private String photo;
    private BigDecimal price;
    private String username;
}
