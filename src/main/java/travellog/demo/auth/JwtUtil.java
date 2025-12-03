package travellog.demo.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:3600000}")
    private int jwtExpirationMs;


      public String generateToken(String userId) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + jwtExpirationMs);
    return Jwts.builder()
      .setSubject(userId)
      .setIssuedAt(now)
      .setExpiration(exp)
      .signWith(SignatureAlgorithm.HS256, jwtSecret)
      .compact();
  }

  public String getUserIdFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret)
      .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

    
}
