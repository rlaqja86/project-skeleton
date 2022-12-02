package com.org.user.config.jwt;

import com.org.user.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String SECRET = "token-secret";
    private static final int EXPIRATION_MS = 600000;

    public String generate(String key) {

        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(key)
                .setIssuedAt(new Date())
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public String getValue(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException  | UnsupportedJwtException | IllegalArgumentException | SignatureException ex) {
            log.error("Invalid Jwt Token", ex);
            throw new InvalidTokenException("invalid token : " + ex.getMessage());
        } catch(ExpiredJwtException ex) {
            log.error("Token Expired", ex);
            throw new InvalidTokenException("token expired");
        }
    }
}
