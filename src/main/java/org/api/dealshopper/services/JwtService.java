package org.api.dealshopper.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY; //the secret key can be found in the application.properties

    private final UserRepository userRepository;
    //private final UserDetailsService userDetailsService;

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    /**
     * extracts the user id from a token, it may throw exception if the token is broken
     * so use it with try and catch
     */
    public Integer extractUserId(String token) throws Exception
    {
        Claims claims = extractAllClaims(token);

        return (Integer) claims.get("userId");
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());

        return generateToken(claims,userDetails);
    }

    /**
     * @return the new created token
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    )
    {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+86400000))//24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * validates the token
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username=extractUsername(token);
        //System.out.println(userDetails.getUsername());

        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @return the encoded signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * validates if a token is valid and not expired
     */
    public boolean authorizeToken(String token)
    {
        // throws exception if the secret ket is not ok
        //Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();

        return isTokenValid(token, userRepository.findByUsername(extractUsername(token)).orElseThrow());
    }
}
