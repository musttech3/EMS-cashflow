package com.alphasoft.EMS.service;

import com.alphasoft.EMS.enums.Role;
import com.alphasoft.EMS.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JwtService {

    @Value("${jwt.secret.key}")
    public String SECRET_KEY;

    @Value("${jwt.expiration.ms}")
    public long expiration;

    private TokenService tokenService;

    @Autowired
    public JwtService(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    public boolean isTokenValid(String token, UserDetails userDetails, String userAgent) {
        final String username = extractUsername(token);
        final String jti = extractJti(token);
        final String userAgentFromToken = extractUserAgent(token);
        Token tokenForCheck = tokenService.findByJtiAndUsername(jti, username);
        if (tokenForCheck == null) {
            return false;
        } else if (!userAgent.equals(userAgentFromToken)) {
            return false;
        }
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !isRevoked(token);
    }

    public boolean isRevoked(String jwt){
        String jti = extractJti(jwt);
        String username = extractUsername(jwt);
        Token token = tokenService.findByJtiAndUsername(jti, username);
        return token != null && token.isRevoked();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractJti(String token) {
        Claims claims = extractAllClaims(token);
        return String.valueOf(claims.get("jti"));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return String.valueOf(claims.get("role"));
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserAgent(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("user_agent");
    }

    public boolean isAdmin(String token) {
        Claims claims = extractAllClaims(token);

        String role = String.valueOf(claims.get("role"));
        return Role.ADMIN.name().equals(role);

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignInKey(){
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails, Role role, String userAgent) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("jti", UUID.randomUUID().toString());
        claims.put("role", role);
        claims.put("user_agent", userAgent);
        claims.put("iss", "CashFlow - EMS");
        return generateToken(claims, userDetails);
    }



}
