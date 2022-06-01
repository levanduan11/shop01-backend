package com.shop.web.rest;

import com.shop.security.jwt.JwtProvider;
import com.shop.web.rest.vm.LoginVM;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserJWTController {

    private final JwtProvider provider;
    private final AuthenticationManagerBuilder managerBuilder;

    public UserJWTController(JwtProvider provider, AuthenticationManagerBuilder managerBuilder) {
        this.provider = provider;
        this.managerBuilder = managerBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM vm) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(vm.getUsername(), vm.getPassword());

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = provider.accessToken(authentication, vm.isRememberMe());
        String refreshToken = provider.refreshToken(vm.getUsername());

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        JWTToken jwtToken = new JWTToken(accessToken, refreshToken);
        jwtToken.setUsername(vm.getUsername());

        return new ResponseEntity<>(jwtToken, httpHeaders, HttpStatus.OK);
    }


    static class JWTToken {
        private String accessToken;
        private String refreshToken;

        private String username;

        public JWTToken(String accToken, String refreshToken) {
            this.accessToken = accToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }


}
