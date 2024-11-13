package com.hikingplanner.hikingplanner.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${secret-key}")
    private String secretKey;

     // 무효화된 토큰을 저장할 Set (ConcurrentHashMap을 이용한 Thread-safe한 Set)
     private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();
    
    public String create(String userId){ //userid를 받아와서 jwt생성

        Date expiredDate = Date.from(Instant.now().plus(1,ChronoUnit.HOURS));//만료기간
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
            .signWith(key,SignatureAlgorithm.HS256)
            .setSubject(userId).setIssuedAt(new Date()).setExpiration(expiredDate)
            .compact();

        return jwt;
        

    }

    public String validate (String jwt) {
        String subject = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 토큰이 무효화된 경우 null 반환
        if (invalidatedTokens.contains(jwt)) {
            System.out.println("무효화된 토큰입니다.");
            return null;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
            subject = claims.getSubject();
            
            
        } catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }
        
        return subject;
    }

    public void invalidateToken(String jwt) {
        invalidatedTokens.add(jwt);
    }

    
}
