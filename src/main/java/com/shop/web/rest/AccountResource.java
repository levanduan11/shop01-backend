package com.shop.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.model.Authority;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.security.SecurityUtils;
import com.shop.security.jwt.JwtProvider;


import com.shop.security.userprincal.UserDetailServiceImpl;
import com.shop.service.Impl.*;
import com.shop.service.dto.AdminUserDTO;
import com.shop.service.dto.CategoryDTO;
import com.shop.web.rest.vm.ChangeImageUrlVM;
import com.shop.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        public AccountResourceException(String message) {
            super(message);
        }
    }

    private final static Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final UserServiceImpl userService;

    private final AuthorityServiceImpl authorityService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final UserDetailServiceImpl userDetailServices;

    public static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/shop-1-d2325.appspot.com/o/le.van.duan?alt=media&token=c644783d-849d-4e73-b01f-367ffd593247";

    public AccountResource(UserServiceImpl userService, AuthorityServiceImpl authorityService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserRepository userRepository, UserDetailServiceImpl userDetailServices) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.userDetailServices = userDetailServices;
    }

    @PostMapping("/test")
    public String test(@RequestBody CategoryDTO categoryDTO){
        System.out.println(99);
        return categoryDTO.toString();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM userVM, HttpServletResponse response) throws IOException {
        if (isPasswordLengthInvalid(userVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        String usernameDuplicate = "";
        String emailDuplicate = "";
        try {
            if (userVM.getImageUrl() == null) {
                userVM.setImageUrl(DEFAULT_IMAGE_URL);
            }
            userService.registerUer(userVM, userVM.getPassword());
        } catch (UsernameAlreadyUsedException e) {
            usernameDuplicate = e.getMessage();

        } catch (EmailAlreadyUsedException e) {
            emailDuplicate = e.getMessage();
        }
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("username", usernameDuplicate);
        responseMessage.put("email", emailDuplicate);
        new ObjectMapper().writeValue(response.getOutputStream(), responseMessage);


    }

    @GetMapping("/login")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("rest request to check if the current user is authenticated");
        String remote = request.getRemoteUser();
        return remote;
    }

    @GetMapping("/activate")
    public void activatedAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
                .getUserWithAuthorities()
                .map(AdminUserDTO::new)
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String username = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());

        if (existingUser.isPresent() && (!existingUser.get().getUsername().equals(username))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByUsername(username);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getImageUrl()
        );
    }

    @PutMapping("/change-image")
    public void changeImageUrl(@RequestBody ChangeImageUrlVM changeImageUrlVM) {
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (!currentUser.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        User user = currentUser.get();
        user.setImageUrl(changeImageUrlVM.getImageUrl());
        userRepository.save(user);

    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String bearer = "Bearer ";
        refreshTokenHelper(response, authorizationHeader, bearer);

    }

    private void refreshTokenHelper(HttpServletResponse response, final String authorizationHeader, final String bearer) throws IOException {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(bearer)) {
            String refreshToken = authorizationHeader.substring(bearer.length());
            if (jwtProvider.validateToken(refreshToken, response)) {
                String username = jwtProvider.getUsernameFromToken(refreshToken);
                String accessToken = getByUsername(username);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
        }
    }

    private String getByUsername(final String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found with username: " + username));
        String authority = user.getAuthorities()
                .stream()
                .filter(Objects::nonNull)
                .map(Authority::getName)
                .collect(Collectors.joining(","));
        long now = (new Date().getTime());
        Date validity = new Date(now + JwtProvider.accessTokenValidityInMilliseconds);
        String accessToken = jwtProvider.getToken(authority, validity, username);
        return accessToken;
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
                StringUtils.isEmpty(password) ||
                        password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
                        password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }



}
