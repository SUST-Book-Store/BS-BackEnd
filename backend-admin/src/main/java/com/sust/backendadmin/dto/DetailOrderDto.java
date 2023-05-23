package com.sust.backendadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailOrderDto {
    private Integer bookId;
    private Integer amount;
    private BigDecimal price;
}
