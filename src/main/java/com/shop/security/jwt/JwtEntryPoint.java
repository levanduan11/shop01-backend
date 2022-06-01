package com.shop.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log= LoggerFactory.getLogger(JwtEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error Message {}",authException.getMessage());
        Map<String, String>errors=new HashMap<>();
        errors.put("Login_error","please check password or username !!!");
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        new ObjectMapper().writeValue(response.getOutputStream(),errors);
        //response.sendError(HttpServletResponse.SC_ACCEPTED,"Error -> Unauthorized");

    }
}
