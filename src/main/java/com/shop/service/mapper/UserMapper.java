package com.shop.service.mapper;

import com.shop.model.Authority;
import com.shop.model.User;
import com.shop.service.dto.AdminUserDTO;
import com.shop.service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public List<UserDTO>usersToUserDTOs(List<User>users){
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user){
        return new UserDTO(user);
    }
    public List<AdminUserDTO>usersToAdminUserDTOs(List<User>users){
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToAdminUserDTO)
                .collect(Collectors.toList());
    }
    public AdminUserDTO userToAdminUserDTO(User user){
        return new AdminUserDTO(user);
    }

    public User userDTOToUser(AdminUserDTO userDTO){
        if (userDTO == null){
            return null;
        }else {
            User user=new User();
            user.setId(userDTO.getId());
            user.setUsername(userDTO.getUsername());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            Set<Authority>authorities=this.authoritiesFromString(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority>authoritiesFromString(Set<String>authoritiesAsString){
        Set<Authority>authorities=new HashSet<>();
        if (authoritiesAsString != null){
            authorities=authoritiesAsString
                    .stream()
                    .map(s -> {
                        Authority authority=new Authority();
                        authority.setName(s);
                        return authority;
                    })
                    .collect(Collectors.toSet());
        }
        return authorities;
    }

    public User userFromId(Long id){
        if (id==null){
            return null;
        }
        User user=new User();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",source = "id")
    public UserDTO toDtoId(User user){
        if (user == null){
            return null;
        }
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        return userDTO;
    }

    public Set<UserDTO>toDtoIdSet(Set<User>users){
        if (users ==null){
            return Collections.emptySet();
        }
        Set<UserDTO>userSet=new HashSet<>();
        for(User user:users){
            userSet.add(this.toDtoId(user));
        }
        return userSet;
    }

    @Named("username")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",source = "id")
    @Mapping(target = "username",source = "username")
    public UserDTO toDtoLogin(User user){
        if (user == null){
            return null;
        }
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        return userDTO;

    }

    @Named("usernameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id",source = "id")
    @Mapping(target = "username",source = "username")
    public Set<UserDTO>toDtoLoginSet(Set<User>users){
        if (users ==null){
            return Collections.emptySet();
        }
        Set<UserDTO>userDTOS=new HashSet<>();
        for(User user:users){
            userDTOS.add(this.toDtoLogin(user));
        }
        return userDTOS;
    }

}
