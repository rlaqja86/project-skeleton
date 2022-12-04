package com.org.user.util;

import com.org.user.entity.User;
import com.org.user.model.dto.TokenDto;
import com.org.user.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverter {

    private PasswordEncoder passwordEncoder;

    public User convert(UserDto dto) {
       return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickName(dto.getNickName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber()).build();
    }

    public UserDto convert(User user) {
        return UserDto.builder().email(user.getEmail())
                .name(user.getName())
                .nickName(user.getNickName())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber()).build();
    }

    public UserDto convert(User user, TokenDto tokenDto) {
        return UserDto.builder().email(user.getEmail())
                .name(user.getName())
                .nickName(user.getNickName())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .tokenDto(TokenDto.builder()
                        .accessToken(tokenDto.getAccessToken())
                        .refreshToken(tokenDto.getRefreshToken())
                        .build())
                .build();
    }
}
