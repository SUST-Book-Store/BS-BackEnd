package com.sust.backendadmin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @TableId(type = IdType.AUTO)
    private Integer bookId;
    private String isbn;
    private String name;
    private String description;
    private String photo;
    private Double price;
    private String category;
    private String author;
    private String publisher;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
    private Integer stock;
    private List<String> detail;
    private Integer available;
}
