package com.sust.backendadmin.service.user;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sust.backendadmin.dto.SearchUserDto;
import com.sust.backendadmin.pojo.Result;
import com.sust.backendadmin.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {
    JSONObject getUserLoginResult(String phone, String password);
    JSONObject getUserRegisterResult(String phone, String username, String password, String sex);
    JSONObject getTokenValidResult(String token);
    JSONObject getUserDataById(int user_id);
    JSONObject changeUserPassword(int user_id, String orig_password, String new_password);
    JSONObject changeUserInfo(int user_id, String phone, String username, String sex, String address);
    boolean checkIfisAdminByToken(String token);

    Result lists(SearchUserDto userDto);

    Result deleteUser(List<Integer> ids,int id);

    Result up(List<Integer> ids,int id);

    Result down(List<Integer> ids,int id);
}
