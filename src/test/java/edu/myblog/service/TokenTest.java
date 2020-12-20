package edu.myblog.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TokenTest {
    Algorithm algorithm = Algorithm.HMAC256("11224");

    public TokenTest() throws UnsupportedEncodingException {
    }

    @Test
    public void testToken(){
        Date date = new Date(System.currentTimeMillis() + 1000*30*60);
            String sign = JWT.create()
                    .withClaim("username", "呵呵")
                    .withClaim("id", 111)
                    .withExpiresAt(date)

                    .sign(algorithm);
            System.out.println(sign);


    }
    @Test
    public void checkToken(){

        String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTExLCJleHAiOjE2MDY0NDkwNTMsInVzZXJuYW1lIjoi5ZG15ZG1In0.iU8SJOq1dgursPO8l1mtD1insaPI2bpFt_hu7-TIW4U";
        JWTVerifier verify = JWT.require(algorithm)
                .build();
        verify.verify(token);
    }
}
