package com.nike.eventmanagementsystem.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String jwtToken)  {
        return extractClaims(jwtToken, Claims::getSubject);
    }
    public String extractUserType(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userType", String.class);
    }


    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateTokenForUser(UserDetails userDetails, String emailAddress, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", emailAddress);
        claims.put("userType", userType);
        return generateToken(userDetails);
    }

    public String generateRefreshTokenForUser(UserDetails userDetails, String emailAddress) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", emailAddress);
        return generateRefreshToken(userDetails);
    }

    public String generateTokenForUser(UserDetails userDetails) {
        Map<String, Object> claims = Collections.emptyMap();
        return buildToken(claims, userDetails, jwtExpiration);
    }

    private String buildCustomToken(Map<String, Object> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    public String generateToken(
//            Map<String, Object> extraClaims,
//            UserDetails userDetails
//    ) {
//        return buildToken(extraClaims, userDetails, jwtExpiration);
//    }




    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration){
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.putAll(extraClaims);
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + expiration));
        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String userName) {
        final String username = extractUsername(token);
        return (username.equals(userName)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    private Key getSignInKey() {
        // Example code to strip ENC(...) and decode the key inside
        String trimmedKey = secretKey.substring(4, secretKey.length() - 1);
        byte[] keyBytes = Base64.getDecoder().decode(trimmedKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
