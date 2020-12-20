package edu.myblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenGenerate<algorithm> {
    private static final String SECRET = "youAndI";


    public static String createToken(int minutes, String account) {
        Date expDate = new Date(System.currentTimeMillis() + 1000 * 30 * 60);
        Map map = new HashMap();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = "";
        try {
            token = JWT.create().withHeader(map).withClaim("account", account).withExpiresAt(expDate).sign(Algorithm.HMAC256(SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static boolean checkToken(String token) {
        try {
            JWTVerifier verify = JWT.require(Algorithm.HMAC256(SECRET))
                    .build();
            verify.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        } catch(NullPointerException e){
            return false;
        }
    }
}
