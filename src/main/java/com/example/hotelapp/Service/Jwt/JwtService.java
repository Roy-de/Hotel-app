package com.example.hotelapp.Service.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {
    private static final String SECRET_KEY ="b65005aac265181a0a2a7759366f1cceac6c584adc21dff12470a2623cbcc123";
    //Method to create token
    public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private boolean isTokenExpired(String token){
        log.info("Checking if token is expired");
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, UserDetails userDetails){
        log.info("Validating toke for user: {}",userDetails.getUsername());
        final String username = extractUsername(token);
        log.info("Check if token is expired");
        boolean expiry = isTokenExpired(token);
        log.info("Token expiry is: {}",expiry);
        log.info("Check if username matches.");
        boolean b = username.equals(userDetails.getUsername());
        log.info("Using username: {}",username);
        log.info("Validating against: {}",userDetails.getUsername());
        log.info("Result is {}",b);
        log.info("Successfully validated token and now returning boolean: {}",b);
        return b;
    }
    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username);
    }
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
