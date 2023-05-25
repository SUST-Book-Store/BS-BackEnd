package com.sust.backendadmin.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sust.backendadmin.mapper.UserMapper;
import com.sust.backendadmin.pojo.User;
import com.sust.backendadmin.service.user.UserService;
import com.sust.backendadmin.utils.GetEncryptedStrUtil;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
