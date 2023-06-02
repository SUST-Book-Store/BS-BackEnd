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
import com.sust.backendadmin.mapper.UserMapper;
import com.sust.backendadmin.pojo.Order;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.pojo.User;
import com.sust.backendadmin.service.order.OrderService;
import com.sust.backendadmin.service.user.UserService;
import com.sust.backendadmin.utils.GetEncryptedStrUtil;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getUserLoginResult(String phone, String password) {
        QueryWrapper<User> loginQueryWrapper =new QueryWrapper<>();
        loginQueryWrapper.eq("phone", phone).eq("password", GetEncryptedStrUtil.MD5(password));
        List<User> userList = userMapper.selectList(loginQueryWrapper);

        JSONObject resp = new JSONObject();

        if (!userList.isEmpty()) {
            String token = UserTokenUtil.GenerateUserToken(userList.get(0).getUserId());
            resp.put("status", 0);
            resp.put("accessToken", token);

        } else {
            resp.put("status", -1);
            resp.put("msg", "用户名或密码错误");
        }
        return resp;
    }
    @Override
    public JSONObject getUserRegisterResult(String phone, String username, String password, String sex) {
        QueryWrapper<User> loginQueryWrapper =new QueryWrapper<>();
        loginQueryWrapper.eq("phone", phone);
        List<User> userList = userMapper.selectList(loginQueryWrapper);
        JSONObject resp = new JSONObject();
        if (!userList.isEmpty()) {
            resp.put("status", -100);
            resp.put("msg", "该用户已存在");
        } else {
            User newUser = new User();
            newUser.setPhone(phone);
            newUser.setUsername(username);
            newUser.setPassword(GetEncryptedStrUtil.MD5(password));
            newUser.setSex(sex);
            newUser.setRole(0);
            // 执行插入操作，将用户注册信息插入到数据库中
            int insertResult = userMapper.insert(newUser);
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
    public JSONObject changeUserPassword(int user_id, String orig_password, String new_password) {
        JSONObject resp = new JSONObject();
        QueryWrapper<User> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("user_id", user_id);
        String oldPass = userMapper.selectList(updateWrapper).get(0).getPassword();
        if (!oldPass.equals(GetEncryptedStrUtil.MD5(orig_password))){
            resp.put("code", -100);
            resp.put("msg", "原密码输入错误");
        } else {
            User updatedUser = new User();
            updatedUser.setPassword(GetEncryptedStrUtil.MD5(new_password));
            int updateResult = userMapper.update(updatedUser, updateWrapper);
            if (updateResult > 0) {
                // 更新成功
                resp.put("code", 0);
                resp.put("msg", "密码修改成功");
            } else {
                // 更新失败
                resp.put("code", -300);
                resp.put("msg", "密码修改失败");
            }
        }
        return resp;

    }

    @Override
    public JSONObject changeUserInfo(int user_id, String phone, String username, String sex, String address) {
        JSONObject resp = new JSONObject();
        QueryWrapper<User> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("user_id", user_id);
        User updatedUser = new User();
        updatedUser.setPhone(phone);
        updatedUser.setUsername(username);
        updatedUser.setSex(sex);
        updatedUser.setAddress(address);
        int updateResult = userMapper.update(updatedUser, updateWrapper);
        if (updateResult > 0) {
            // 更新成功
            resp.put("code", 0);
            resp.put("msg", "修改成功");
        } else {
            // 更新失败
            resp.put("code", -300);
            resp.put("msg", "修改失败");
        }
        return resp;
    }

    @Override
    public boolean checkIfisAdminByToken(String token) {
        int user_id = UserTokenUtil.GetUserIdByToken(token);
        if (user_id == -1) {
            return false;
        }
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
        System.out.println(users.get(0));
        System.out.println(users.get(0).getRole());

        if (users.get(0).getRole().equals(1)||users.get(0).getRole().equals(2))
        {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JSONObject getUserDataById(int user_id) {
        QueryWrapper<User> loginQueryWrapper =new QueryWrapper<>();
        loginQueryWrapper.eq("user_id", user_id);
        List<User> userList = userMapper.selectList(loginQueryWrapper);
        JSONObject resp = new JSONObject();
        if (userList.isEmpty()) {
            resp.put("code", -100);
            resp.put("msg", "该用户不存在");
        } else {
            User user = userList.get(0);
            resp.put("code", 0);
            JSONObject data = new JSONObject();
            data.put("user_id", user_id);
            data.put("username", user.getUsername());
            data.put("phone", user.getPhone());
            data.put("sex", user.getSex());
            data.put("address", user.getAddress());
            if (user.getRole().intValue() == 1) {
                data.put("is_admin", true);
            } else {
                data.put("is_admin", false);
            }
            resp.put("data", data);
        }
        return resp;
    }

    @Override
    public JSONObject getTokenValidResult(String token) {
        boolean isValid = UserTokenUtil.ValidateUserToken(token);
        JSONObject resp = new JSONObject();
        if (isValid) {
            resp.put("code", 200);
            resp.put("isvalid", true);

        } else {
            resp.put("code", 401);
            resp.put("isvalid", false);
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
        List<User> userkList = this.listByIds(ids);
        boolean b = userkList.stream().anyMatch(user -> user.getRole() == 1);
        if (b)
        {
            return Result.fail("选择中存在管理员，无权限删除其他管理员");
        }
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
        boolean a = userkList.stream().anyMatch(user -> user.getRole() == 1);
        if (a)
            return Result.fail("存在用户已是管理员，无需提升权限");
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
        boolean bd = userkList.stream().anyMatch(user -> user.getRole() == 0);
        if (bd)
            return Result.fail("存在用户已降低至最低权限，无需降低");
        List<User> newUsers = userkList.stream()
                .peek(user -> user.setRole(0))
                .collect(Collectors.toList());
        boolean c = this.updateBatchById(newUsers);
        if (c)
            return Result.ok();
        return Result.fail("操作失败");

    }
}
