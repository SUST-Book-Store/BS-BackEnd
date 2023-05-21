package com.sust.backendadmin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sust.backendadmin.mapper.BookMapper;
import com.sust.backendadmin.pojo.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendAdminApplicationTests {
	@Autowired
	private BookMapper bookMapper;

	@Test
	void contextLoads() {
		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		List<Book> books = bookMapper.selectList(queryWrapper);
		System.out.println(books);
	}

}
