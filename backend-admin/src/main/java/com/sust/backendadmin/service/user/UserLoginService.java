package com.sust.backendadmin.service.user;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.pojo.User;


public interface UserLoginService extends IService<User> {
    JSONObject getUserLoginResult(String phone, String password);
    JSONObject getUserRegisterResult(String phone, String username, String password, String sex);
}
