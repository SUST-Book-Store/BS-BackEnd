package com.sust.backendadmin.controller.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sust.backendadmin.service.user.UserService;
import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody Map<String, String> data) {
        JSONObject resp = new JSONObject();
        System.out.println(data);
        if (StrUtil.isBlank(data.get("phone")) || StrUtil.isBlank(data.get("password"))) {
            resp.put("status", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userService.getUserLoginResult(data.get("phone"), data.get("password"));
    }

    @PostMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> data) {
        JSONObject resp = new JSONObject();
        if (StrUtil.isBlank(data.get("phone")) || StrUtil.isBlank(data.get("username")) || StrUtil.isBlank(data.get("password")) || StrUtil.isBlank(data.get("sex"))) {
            resp.put("status", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userService.getUserRegisterResult(data.get("phone"), data.get("username"), data.get("password"), data.get("sex"));
    }

    @GetMapping("/isValidToken")
    public JSONObject validateToken(HttpServletRequest request) {
        String userToken = request.getHeader("token");
        JSONObject resp = new JSONObject();
        if (userToken == null || StrUtil.isBlank(userToken)) {
            resp.put("code", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userService.getTokenValidResult(userToken);
    }

    @GetMapping("/getUserInfo")
    public JSONObject getUserInfo(HttpServletRequest request) {
        String userToken = request.getHeader("token");
        JSONObject resp = new JSONObject();
        int user_id = UserTokenUtil.GetUserIdByToken(userToken);
        if (user_id == -1) {
            resp.put("code", 401);
            resp.put("msg", "Token无效");
            return resp;
        }
        return userService.getUserDataById(user_id);
    }
    @PostMapping("/changeUserPassword")
    public JSONObject changeUserPassword(HttpServletRequest request, @RequestBody Map<String, String> data) {
        String userToken = request.getHeader("token");
        JSONObject resp = new JSONObject();
        int user_id = UserTokenUtil.GetUserIdByToken(userToken);
        if (user_id == -1) {
            resp.put("code", 401);
            resp.put("msg", "Token无效");
            return resp;
        }
        return userService.changeUserPassword(user_id, data.get("origpass"), data.get("password"));
    }
    @PostMapping("/changeUserInfo")
    public JSONObject changeUserInfo(HttpServletRequest request, @RequestBody Map<String, String> data) {
        String userToken = request.getHeader("token");
        JSONObject resp = new JSONObject();
        int user_id = UserTokenUtil.GetUserIdByToken(userToken);
        if (user_id == -1) {
            resp.put("code", 401);
            resp.put("msg", "Token无效");
            return resp;
        }
        return userService.changeUserInfo(user_id, data.get("phone"), data.get("username"), data.get("sex"));
    }
}
