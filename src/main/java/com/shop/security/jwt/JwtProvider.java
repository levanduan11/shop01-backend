package com.shop.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.security.userprincal.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class JwtProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    public static final String JWT_SECRET = "duan777@gmail.com";
    public static final long accessTokenValidityInMilliseconds = 1800l * 1000;//30 p
    private static final long refreshTokenValidityInMilliseconds = 30 * 60 * 100000;

    private static final long tokenValidityInMillisecondsForRememberMe = 2592000l * 1000;//30d;


    public String accessToken(Authentication authentication, boolean rememberMe) {

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date validity;
        long now = (new Date()).getTime();
        if (rememberMe) {
            validity = new Date(now + tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + accessTokenValidityInMilliseconds);
        }
        UserDetailsImpl userPrinciple = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("create token vs userdetail {}", userPrinciple);
        return getToken(authorities, validity, userPrinciple.getUsername());
    }

    public String getToken(String authorities, Date validity, String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .claim(JWT_SECRET, authorities)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String refreshToken(String username) {
        Date validity;
        long now = (new Date()).getTime();
        validity = new Date(now + refreshTokenValidityInMilliseconds);
        return Jwts
                .builder()
                .setSubject(username)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public boolean validateToken(String token, HttpServletResponse response) throws IOException {
        String mess = "";
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {}", e.getMessage());
            mess = e.getMessage();
        } catch (MalformedJwtException e) {
            log.error("Invalid format Token -> Message: {}", e.getMessage());
            mess = e.getMessage();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e.getMessage());
            mess = e.getMessage();
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty --> Message {}", e.getMessage());
            mess = e.getMessage();
        }
        if (!mess.equals("")) {
            response.setHeader("error", mess);
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", mess);
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);

        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts
                .parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(JWT_SECRET).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


}
