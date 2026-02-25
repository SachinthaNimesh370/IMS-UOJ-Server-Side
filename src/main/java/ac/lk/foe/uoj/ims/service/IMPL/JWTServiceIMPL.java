package ac.lk.foe.uoj.ims.service.IMPL;


import ac.lk.foe.uoj.ims.service.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JWTServiceIMPL implements JWTService {
    private final SecretKey secretKey;

    public JWTServiceIMPL() {
        try {
            SecretKey k = KeyGenerator.getInstance("HmacSHA256").generateKey();
            secretKey= Keys.hmacShaKeyFor(k.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String jwtToken(String email, Map<String,String> clams) {
        return Jwts.builder()
                .claims(clams)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String getEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

        }catch (Exception e){
            System.err.println("Token parsing failed: " + e.getMessage());
            return null;
        }
    }
}