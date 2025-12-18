package Prasunet.company.JWT;

import Prasunet.company.User.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 secret key (move to application.properties later)
    private static final String SECRET_KEY =
            "my_super_secret_key_which_should_be_long_enough_12345";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // ============================
    // GENERATE TOKEN
    // ============================
    public String generateToken(String userId, Role role) {

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================
    // EXTRACT USER ID
    // ============================
    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    // ============================
    // EXTRACT ROLE
    // ============================
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ============================
    // VALIDATE TOKEN
    // ============================
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ============================
    // INTERNAL: PARSE CLAIMS
    // ============================
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

