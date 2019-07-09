package com.qf.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2917:47
 * @description: TODO  JWT
 */
public class JwtTest {

    /**
     *  JWT发布令牌
     */
    @Test
    public void createTokenTest() {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666")
                .setSubject("fang")//账号
                .setIssuedAt(new Date())  //发布时间
                .setExpiration(new Date(new Date().getTime() + 60 * 1000))  //开始生效时间
                .signWith(SignatureAlgorithm.HS256, "fang"); //设置签名
        //.claim()  //添加自定义属性
        String jwtToken = jwtBuilder.compact();
        System.out.println(jwtToken);//eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiJmYW5nIiwiaWF0IjoxNTYxOTQ2NjQzLCJleHAiOjE1NjE5NDY3MDN9.ooQrSFIxbBu8PleL5gcYY83r-f48Mcuqx7seIaTCFmM
    }

    /**
     * 解析jwtToken  验证token正确性
     */
    @Test
    public void parseTokenTest(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiJmYW5nIiwiaWF0IjoxNTYxOTUwNjA0LCJleHAiOjE1NjE5NTA2NjR9.0FuyRxgUaZmo1L-cMls_f7UTvT7rq9YvjbbLFEICsmw";
        //验证令牌的合法性
        Claims claims = Jwts.parser().setSigningKey("fang").parseClaimsJws(token).getBody();
        //
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());
    }

}