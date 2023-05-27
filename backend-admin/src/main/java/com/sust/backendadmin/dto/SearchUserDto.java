package com.sust.backendadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserDto {
    private int pageSize;
    private int pageNum;
    private String username;
    private String sex;
    private int role=-1;
}
