package com.shop.service.Impl;

import com.shop.model.Authority;
import com.shop.model.User;
import com.shop.repository.AuthorityRepository;
import com.shop.repository.UserRepository;
import com.shop.security.AuthoritiesConstants;
import com.shop.security.SecurityUtils;
import com.shop.service.IUserService;
import com.shop.service.dto.AdminUserDTO;
import com.shop.service.dto.UserDTO;
import com.shop.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements IUserService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        String hashPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        return userRepository.save(user);
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("activating uer for activation key {} ", key);

        return userRepository
                .findOneByActivationKey(key)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationKey(null);
                    return user;
                });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("reset user password for reset key {} ", key);

        return userRepository
                .findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                });
    }

    public Optional<User> requestPasswordReset(String mail) {

        return userRepository
                .findOneByEmailIgnoreCase(mail)
                .filter(User::isActivated)
                .map(user -> {
                    user.setResetKey(generateKey(33));
                    user.setResetDate(Instant.now());
                    return user;
                });
    }

    public User registerUer(AdminUserDTO userDTO, String password) {
        userRepository
                .findOneByUsername(userDTO.getUsername().toLowerCase())
                .ifPresent(user -> {
                    boolean removed = removeNonActivatedUser(user);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                });
        userRepository
                .findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(user -> {
                    boolean removed = removeNonActivatedUser(user);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                });

        User newUser = new User();
        String hashPassword = passwordEncoder.encode(password);
        newUser.setUsername(userDTO.getUsername().toLowerCase());
        newUser.setPassword(hashPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setActivated(true);
        newUser.setActivationKey(null);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.info("created information for user {} ", newUser);
        return newUser;
    }

    public User createUser(ManagedUserVM userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());

        String hashPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(hashPassword);
        user.setResetKey(generateKey(19));
        user.setResetDate(Instant.now());
        user.setActivated(userDTO.isActivated());
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("created information for user {} ", user);
        return user;

    }

    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setUsername(userDTO.getUsername().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    Set<String> authoritiesAsString = userDTO.getAuthorities();
                    if (authoritiesAsString != null) {
                        managedAuthorities.clear();
                        authoritiesAsString
                                .stream()
                                .map(authorityRepository::findByName)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .forEach(managedAuthorities::add);
                    }
                    log.debug("change information  user {} ", user);
                    userRepository.save(user);
                    return user;

                }).map(AdminUserDTO::new);

    }

    public void deleteUser(String username) {
        userRepository
                .findOneByUsername(username)
                .ifPresent(
                        user -> {
                            userRepository.delete(user);
                            log.debug("delete user {} ", user);
                        }
                );
    }

    public void updateUser(String firstName, String lastName, String email, String imageUrl) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findByUsername)
                .ifPresent(user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email);
                    }
                    user.setImageUrl(imageUrl);
                    userRepository.save(user);
                    log.debug("changed information for user {}", user);
                });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUerWithAuthoritiesByUsername(String username) {

        return userRepository
                .findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }


    @Transactional
    public void changePassword(String currentPass, String newPass) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findByUsername)
                .ifPresent(user -> {
                    String hashPass = user.getPassword();
                    if (!passwordEncoder.matches(currentPass, hashPass)) {
                        throw new InvalidPasswordException();
                    }
                    String newHashPass = passwordEncoder.encode(newPass);
                    user.setPassword(newHashPass);
                    log.debug("Chaged password for user: {}", user);

                });
    }

    @Scheduled(cron = "0 0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("deleting not activated user {} ", user);
                    userRepository.delete(user);
                });
    }

    @Transactional(readOnly = true)
    public List<String> getRoleNames() {

        return authorityRepository
                .findAll()
                .stream()
                .map(Authority::getName)
                .collect(Collectors.toList());
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    private String generateKey(int n) {
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        while (i < 26) {
            if (j < 10) {
                sb.append((char) (j + '0'));
                j++;
            }
            sb.append((char) (i + 'a'));
            sb.append((char) (i + 'A'));
            i++;
        }
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        for (int k = 0; k < n; k++) {
            int index = random.nextInt(sb.length());
            res.append(sb.charAt(index));
        }
        return res.toString();
    }
}
