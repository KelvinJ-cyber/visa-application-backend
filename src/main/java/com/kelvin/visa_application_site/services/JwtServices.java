package com.kelvin.visa_application_site.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServices {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpirationInMs;


    // ? Extracts all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return io.jsonwebtoken.Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        // ? Compose a JWT: custom claims + standard claims (sub/iat/exp) and sign it with HS256.
        return Jwts
                .builder()
                .setClaims(extraClaims)                              // your custom payload fields
                .setSubject(userDetails.getUsername())               // 'sub' claim: who the token is about
                .setIssuedAt(new Date(System.currentTimeMillis()))   // 'iat' claim: when issued
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 'exp' claim: when it expires
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // sign to prevent tampering
                .compact();                                          // serialize to 'header.payload.signature'
    }


    // ! Extracts a specific claim from the JWT token using the provided claims resolver function
    public  <T> T extractClaims(
            String token,
            Function<Claims, T>
            claimsResolver // function to extract specific claim
    ){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return generateToken(claims, userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails,jwtExpirationInMs);
    }


    public long getExpirationTime() {
        // Exposes configured TTL. Make sure this is in MILLISECONDS if you add to currentTimeMillis().
        return jwtExpirationInMs;
    }

    // ? Validates the JWT token against the provided user details
    public boolean isTokenValid(String token, UserDetails userDetails){
          try{
              final String username = extractUsername(token);
              return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
          } catch (JwtException | IllegalArgumentException e){
              return false;
          }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Decode secret key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
