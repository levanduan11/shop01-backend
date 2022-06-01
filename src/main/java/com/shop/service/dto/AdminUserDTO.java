package com.shop.service.dto;

import com.shop.model.Authority;
import com.shop.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminUserDTO {

    private Long id;

    @NotBlank
    @Size(min = 1,max = 50)
    private String username;
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;
    @Email
    @Size(min = 5,max = 254)
    private String email;
    private String imageUrl;

    private boolean activated=false;

    private Instant createdDate;

    private Set<String>authorities;

    public AdminUserDTO() {
    }

    public AdminUserDTO(User user) {
       this.id= user.getId();
       this.username=user.getUsername();
       this.firstName=user.getFirstName();
       this.lastName=user.getLastName();
       this.email=user.getEmail();
       this.activated= user.isActivated();
       this.imageUrl=user.getImageUrl();
       this.createdDate=user.getCreatedDate();
       this.authorities=user.getAuthorities()
               .stream()
               .map(Authority::getName)
               .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "AdminUserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activated=" + activated +
                ", createdDate=" + createdDate +
                ", authorities=" + authorities +
                '}';
    }
}
