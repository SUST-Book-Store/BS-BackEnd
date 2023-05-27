package com.sust.backendadmin.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.dto.PageListDto;
import com.sust.backendadmin.dto.SearchUserDto;
import com.sust.backendadmin.mapper.UserLoginMapper;
import com.sust.backendadmin.pojo.Book;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.pojo.User;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.service.user.UserLoginService;
import com.sust.backendadmin.utils.GetEncryptedStrUtil;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLoginServiceImpl  extends ServiceImpl<UserLoginMapper, User> implements UserLoginService {
    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public JSONObject getUserLoginResult(String username, String password) {
        QueryWrapper<User> loginQueryWrapper =new QueryWrapper<>();
        loginQueryWrapper.eq("username", username).eq("password", GetEncryptedStrUtil.MD5(password));
        List<User> userList = userLoginMapper.selectList(loginQueryWrapper);

        JSONObject resp = new JSONObject();
        if (!userList.isEmpty()) {
            JSONObject userIdentity = new JSONObject();
            String token = UserTokenUtil.GenerateUserToken(userList.get(0).getUserId());
            userIdentity.put("userToken", token);
            resp.put("status", 0);
            resp.put("data", userIdentity);

        } else {
            resp.put("status", -1);
            resp.put("msg", "用户名或密码错误");
        }

        return resp;
    }
    @Override
    public JSONObject getUserRegisterResult(String username, String password, String sex) {
        QueryWrapper<User> loginQueryWrapper =new QueryWrapper<>();
        loginQueryWrapper.eq("username", username);
        List<User> userList = userLoginMapper.selectList(loginQueryWrapper);
        JSONObject resp = new JSONObject();
        if (!userList.isEmpty()) {
            resp.put("status", -100);
            resp.put("msg", "该用户名已存在");
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(GetEncryptedStrUtil.MD5(password));
            newUser.setSex(sex);
            newUser.setRole(0);
            // 执行插入操作，将用户注册信息插入到数据库中
            int insertResult = userLoginMapper.insert(newUser);
            if (insertResult > 0) {
                resp.put("status", 0);
                resp.put("msg", "注册成功");
            } else {
                resp.put("status", -200);
                resp.put("msg", "注册失败");
            }
        }

        return resp;
    }

    @Override
    public Result lists(SearchUserDto userDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userDto.getUsername()))
        {
            wrapper.like(User::getUsername,userDto.getUsername());
        }
        if (StringUtils.isNotBlank(userDto.getSex()))
        {
            wrapper.eq(User::getSex,userDto.getSex());
        }
        if (userDto.getRole()!=-1)
        {
            wrapper.eq(User::getRole,userDto.getRole());
        }
        Page<User> page = new Page<>( userDto.getPageNum(),userDto.getPageSize() );
        page = this.page(page, wrapper);
        PageListDto pageListDto = new PageListDto();
        pageListDto.setUserList(page.getRecords());
        pageListDto.setPages((int) page.getPages());
        pageListDto.setTotal((int) page.getTotal());
        return Result.ok(pageListDto);
    }
    @Autowired
    private OrderService orderService;
    @Override
    public Result deleteUser(List<Integer> ids) {
        List<Order> list = orderService.list(Wrappers.<Order>lambdaQuery().in(Order::getUserId, ids));
        if (list.size()!=0)
            return Result.fail("选择用户有订单信息，不能删除");
        if (CollectionUtils.isEmpty(ids))
            return Result.fail("未选择");
        boolean c = this.removeBatchByIds(ids);
        if (c)
        {
            return Result.ok();
        }else
            return Result.fail("操作失败");
    }

    @Override
    public Result up(List<Integer> ids) {
        List<User> userkList = this.list(Wrappers.<User>lambdaQuery().in(User::getUserId, ids));
        List<User> newUsers = userkList.stream()
                .peek(user -> user.setRole(1))
                .collect(Collectors.toList());
        boolean b = this.updateBatchById(newUsers);
        if (b)
        return Result.ok();
        return Result.fail("操作失败");
    }

    @Override
    public Result down(List<Integer> ids) {
        List<User> userkList = this.list(Wrappers.<User>lambdaQuery().in(User::getUserId, ids));

        boolean b = userkList.stream().anyMatch(user -> user.getRole() == 1);
        if (b)
            return Result.fail("不可以操作其他管理员");
        List<User> newUsers = userkList.stream()
                .peek(user -> user.setRole(0))
                .collect(Collectors.toList());
        boolean c = this.updateBatchById(newUsers);
        if (c)
            return Result.ok();
        return Result.fail("操作失败");

    }
}
