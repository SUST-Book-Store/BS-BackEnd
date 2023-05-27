package com.sust.backendadmin.dto;

import lombok.Data;

@Data
public class SearchOrderDto {
    private int pageSize;
    private int pageNum;
    private String no;
    private int status=-1;
}
