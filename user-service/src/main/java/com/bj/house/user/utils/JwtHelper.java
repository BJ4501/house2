package com.bj.house.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.lang.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * JWT 生成，校验Token
 * Created by BJ on 2018/2/9.
 */
public class JwtHelper {

    private static final String SECRET = "session_secret";

    private static final String ISSUER = "bj_user";

    @SuppressWarnings("RedundantStringToString")
    public static String getToken(Map<String,String> claims){
        try {
            //声明Token算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            //JWT
            JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER) //设置发布者
                    .withExpiresAt(DateUtils.addDays(new Date(),1)); //过期时间

            //将claims存储至token中
            claims.forEach((k,v) -> builder.withClaim(k,v));

            return builder.sign(algorithm).toString();
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    //校验Token
    public static Map<String,String> verifyToken(String token){
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);

        }catch (IllegalArgumentException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        //Claim -> String
        Map<String,String> resultMap = Maps.newHashMap();

        map.forEach((k,v) -> resultMap.put(k,v.asString()));

        return resultMap;
    }
}
