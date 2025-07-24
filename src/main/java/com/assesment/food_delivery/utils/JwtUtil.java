package com.assesment.food_delivery.utils;

import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "xCF4lXQePz5F+VNF9rMuJAKkBL5qey4cAlfvA+XhZxI=";


    public static String GenerateToken(String email, UserRole role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *  10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String ExtractUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String ExtractRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean ValidateToken(String token, UserDetails user){
        final String username = ExtractUsername(token);
        return (username.equals(user.getUsername()) & !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private Date extractExpiration(String token){
          return Jwts.parserBuilder()
                  .setSigningKey(SECRET_KEY)
                  .build()
                  .parseClaimsJws(token)
                  .getBody()
                  .getExpiration();
    }
}
