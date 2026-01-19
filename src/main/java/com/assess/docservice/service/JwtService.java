package com.assess.docservice.service;

import com.assess.docservice.component.TenantContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET =
            "mySuperSecretKeyForDemoPurpose123!mySuperSecretKey";

    private static final long EXPIRATION = 60 * 60 * 1000;

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String username,
                                List<String> roles,
                                String tenantId) {

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("tenantId", tenantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {

        @SuppressWarnings("unchecked")
        List<String> roles = extractAllClaims(token).get("roles", List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();

        List<SimpleGrantedAuthority> authorities =
                extractRoles(token).stream()
                        .map(a -> (SimpleGrantedAuthority) a)
                        .toList();

        String tenantId = claims.get("tenantId", String.class);
        TenantContext.setTenantId(tenantId);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}




