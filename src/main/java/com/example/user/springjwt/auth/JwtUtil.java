package com.example.user.springjwt.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey = "yourVeryLongAndComplexSecretKeyThatIsAtLeast256BitsLong123456789";

    public Integer getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public String getSecretKey() {
        return secretKey;
    }

    private Integer accessTokenExpiration = 60 * 60 * 1000; // ms

    public String generateToken(String username) {
        try {
            // Sử dụng phương pháp cơ bản
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                    .compact();

        } catch (Exception e) {
            System.err.println("Token Generation Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(("Authorization"));
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
