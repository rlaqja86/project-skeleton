package com.org.user.util;

import com.org.user.entity.User;
import com.org.user.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverter {

    private PasswordEncoder passwordEncoder;

    public User convert(UserDto vo) {
       return User.builder()
                .email(vo.getEmail())
                .name(vo.getName())
                .nickName(vo.getNickName())
                .password(passwordEncoder.encode(vo.getPassword()))
                .phoneNumber(vo.getPhoneNumber()).build();
    }

    public UserDto convert(User user) {
        return UserDto.builder().email(user.getEmail())
                .name(user.getName())
                .nickName(user.getNickName())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber()).build();
    }

    public UserDto convert(User user, String accessToken) {
        return UserDto.builder().email(user.getEmail())
                .name(user.getName())
                .nickName(user.getNickName())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .accessToken(accessToken)
                .build();
    }
}
