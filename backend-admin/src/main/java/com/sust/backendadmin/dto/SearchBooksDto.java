package com.sust.backendadmin.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBooksDto implements Serializable {
    private int pageSize;
    private int pageNum;
    private String name;
    private List<Data> data;
    private String author;
    private int status=-1;
    private String type;
}
