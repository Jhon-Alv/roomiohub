package com.roomiohub.backend.security.jwt;

import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration-time}")
    private Long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractTenantId(String token) {
        String tenantId = extractClaim(token, claims -> claims.get("tenantId", String.class));
        return tenantId != null ? UUID.fromString(tenantId) : null;
    }

    public UUID extractSedeId(String token) {
        String sedeId = extractClaim(token, claims -> claims.get("sedeId", String.class));
        return sedeId != null ? UUID.fromString(sedeId) : null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UsuarioEntity usuario) {
        return generateToken(new HashMap<>(), usuario);
    }

    public String generateToken(Map<String, Object> extraClaims, UsuarioEntity usuario) {
        extraClaims.put("tenantId", usuario.getTenantId().toString());
        if (usuario.getSedeId() != null) {
            extraClaims.put("sedeId", usuario.getSedeId().toString());
        }
        extraClaims.put("rol", "ROLE_" + usuario.getRol().name());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
