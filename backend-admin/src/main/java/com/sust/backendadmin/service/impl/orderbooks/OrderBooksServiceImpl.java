package com.sust.backendadmin.service.impl.orderbooks;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.mapper.OrderBooksMapper;
import com.sust.backendadmin.pojo.OrderBooks;
import com.sust.backendadmin.service.orderbooks.OrderBooksService;
import org.springframework.stereotype.Service;

@Service
public class OrderBooksServiceImpl extends ServiceImpl<OrderBooksMapper, OrderBooks> implements OrderBooksService {

}
