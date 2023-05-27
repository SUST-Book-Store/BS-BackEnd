package com.sust.backendadmin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String sex;
    private String password;
    private Integer role;
    private String phone;
}
