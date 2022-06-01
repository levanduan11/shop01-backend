package com.shop.web.rest;

import com.shop.share.Constants;
import com.shop.share.HeaderUtil;
import com.shop.share.PaginationUtil;
import com.shop.share.ResponseUtil;
import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.security.AuthoritiesConstants;
import com.shop.service.Impl.EmailAlreadyUsedException;
import com.shop.service.Impl.UserServiceImpl;
import com.shop.service.dto.AdminUserDTO;
import com.shop.web.rest.errors.BadRequestAlertException;
import com.shop.web.rest.errors.LoginAlreadyUsedException;
import com.shop.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class UserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
            Arrays.asList(
                    "id",
                    "username",
                    "firstName",
                    "lastName",
                    "email",
                    "activated",
                    "createdDate"
            )
    );

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final String applicationName = "e-comerShop";
    private final UserServiceImpl userService;

    private final UserRepository userRepository;

    public UserResource(UserServiceImpl userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody ManagedUserVM userDTO) throws URISyntaxException {

        log.debug("REST request to save User {} ", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID");
        } else if (userRepository.findOneByUsername(userDTO.getUsername().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            return ResponseEntity
                    .created(new URI("/api/admin/users/" + newUser.getUsername()))
                    .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getUsername()))
                    .body(newUser);
        }

    }

    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AdminUserDTO> updateUser(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to update user {} ", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        boolean check = checkIdForUpdate(existingUser, userDTO);
        if (existingUser.isPresent() && (!check)) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByUsername(userDTO.getUsername());
        if (existingUser.isPresent() && (!checkIdForUpdate(existingUser, userDTO))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<AdminUserDTO> updatedUser = userService.updateUser(userDTO);
        return ResponseUtil.wrapOrNotFound(
                updatedUser,
                HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getUsername())
        );
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<AdminUserDTO>> getAllUsersByPage(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {

        log.debug("request to gel all by page user for an admin");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<AdminUserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

    }

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {

        log.debug("request to gel all user for an admin");
        List<AdminUserDTO> adminUserDTOS = userRepository.findAll()
                .stream()
                .map(AdminUserDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(adminUserDTOS, HttpStatus.OK);

    }

    @GetMapping("/users1")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")

    public ResponseEntity<Page<User>> all(Pageable pageable) {
        return ResponseEntity
                .ok(userRepository.findAll(pageable));
    }


    @GetMapping("/users/{username}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AdminUserDTO> getUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String username) {
        log.debug("request to get user {} ", username);
        return ResponseUtil
                .wrapOrNotFound(userService.getUerWithAuthoritiesByUsername(username).map(AdminUserDTO::new));

    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String username) {
        log.debug("request to delete user {} ", username);
        userService.deleteUser(username);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", username))
                .build();
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable
                .getSort()
                .stream()
                .map(Sort.Order::getProperty)
                .allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }


    private boolean checkIdForUpdate(Optional<User> user, AdminUserDTO userDTO) {
        return user.get().getId().equals(userDTO.getId());
    }


}
