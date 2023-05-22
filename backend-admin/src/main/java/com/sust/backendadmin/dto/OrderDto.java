package com.sust.backendadmin.dto;

import com.sust.backendadmin.pojo.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private BigDecimal totalPrice;
    private List<Cart> carts;
}
