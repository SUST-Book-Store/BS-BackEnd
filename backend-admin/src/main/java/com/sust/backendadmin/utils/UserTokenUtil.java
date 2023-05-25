package com.sust.backendadmin.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class UserTokenUtil {
    private static final String  secretKey = "SUST";
    public static String GenerateUserToken(int userid) {
        JwtBuilder builder = Jwts.builder();
        return builder.setSubject("user-token")
                .setIssuedAt(new Date())
                .claim("userid", userid)
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 3600 * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

//  获取UserId，若过期则返回-1
    public static int GetUserIdByToken(String userToken){
        int userid;
        if (ValidateUserToken(userToken)){
            Claims claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(userToken)
                    .getBody();
            userid = Integer.parseInt(claims.get("userid").toString());
        } else {
            userid = -1;
        }
        return userid;
    }

    public static boolean ValidateUserToken(String token) {
        boolean isValid = false;
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            long expiration = claims.getExpiration().getTime();
            if (expiration >= System.currentTimeMillis()) {
                isValid = true;
            }
        } catch (Exception e) {
            System.out.println("Expired Session Token");
        }
        return isValid;
    }
}
