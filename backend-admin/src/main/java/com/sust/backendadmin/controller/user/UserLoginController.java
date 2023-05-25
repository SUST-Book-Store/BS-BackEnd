package com.sust.backendadmin.controller.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sust.backendadmin.service.user.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody Map<String, String> data) {
        JSONObject resp = new JSONObject();
        System.out.println(data);
        if (StrUtil.isBlank(data.get("phone")) || StrUtil.isBlank(data.get("password"))) {
            resp.put("status", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userLoginService.getUserLoginResult(data.get("phone"), data.get("password"));
    }

    @PostMapping("/register")
    public JSONObject register(@RequestBody Map<String, String> data) {
        JSONObject resp = new JSONObject();
        if (StrUtil.isBlank(data.get("phone")) || StrUtil.isBlank(data.get("username")) || StrUtil.isBlank(data.get("password")) || StrUtil.isBlank(data.get("sex"))) {
            resp.put("status", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userLoginService.getUserRegisterResult(data.get("phone"), data.get("username"), data.get("password"), data.get("sex"));
    }

    @PostMapping("/validatetoken")
    public JSONObject validateToken(@RequestBody Map<String, String> data) {
        JSONObject resp = new JSONObject();
        if (StrUtil.isBlank(data.get("token"))) {
            resp.put("code", -100);
            resp.put("msg", "非法请求");
            return resp;
        }
        return userLoginService.getTokenValidResult(data.get("token"));
    }
}