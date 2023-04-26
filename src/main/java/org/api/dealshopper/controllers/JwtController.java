package org.api.dealshopper.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.api.dealshopper.models.TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@RestController
public class JwtController {

    @Value("${jwt.secret}")
    private  String SECRET_KEY;
    @GetMapping("/createtoken")
    public ResponseEntity<?> createToken() {
        try {
            Date now = new Date(System.currentTimeMillis());
            Date expiration = new Date(now.getTime() + 3600 * 1000); // Token-ul expiră într-o oră
            String token = Jwts.builder()
                    .setSubject("user")
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/authorizetoken")
    public ResponseEntity<?> authorizeToken(@RequestBody TokenRequest token) {
        try {
            System.out.println(token);
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token.getToken()).getBody();
            return ResponseEntity.ok().build();
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
