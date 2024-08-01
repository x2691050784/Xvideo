package com.imooc.bilbil.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.imooc.bilbil.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    private static final String ISSUER="XZQ";
    //产生令牌
    //直接传输userID可能造成数据不安全
    public static String generateToken(Long userId) throws Exception {
        Algorithm algorithm=Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());//获取公钥私钥.生成算法
        //设置时间
        Calendar calendar=Calendar.getInstance();//日历的类
        calendar.setTime(new Date());//当前时间
        calendar.add(Calendar.HOUR,1);//过期时间
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER) //签发者
                .withExpiresAt(calendar.getTime())//过期时间
                .sign(algorithm); //进行加密算法
    }
    public static Long verifyToken(String token)  {
        Algorithm algorithm= null;//获取公钥私钥.生成算法
        try {
            algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
            JWTVerifier verifier=JWT.require(algorithm).build();//解密类
            DecodedJWT verify = verifier.verify(token);//解密token
            String userId=verify.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e)  {
            throw new ConditionException("555","token过期");
        }catch (Exception e){
            throw new ConditionException("500","非法用户token");
        }

    }

    public static String generateRefreshToken(Long id) throws Exception {
        Algorithm algorithm=Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());//获取公钥私钥.生成算法
        //设置时间
        Calendar calendar=Calendar.getInstance();//日历的类
        calendar.setTime(new Date());//当前时间
        calendar.add(Calendar.DAY_OF_MONTH,7);//过期时间
        return JWT.create().withKeyId(String.valueOf(id))
                .withIssuer(ISSUER) //签发者
                .withExpiresAt(calendar.getTime())//过期时间
                .sign(algorithm); //进行加密算法

    }


}
