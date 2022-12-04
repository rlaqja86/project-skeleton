package com.org.user.application;

import com.org.user.config.jwt.JwtTokenProvider;
import com.org.user.entity.User;
import com.org.user.exception.SignUpException;
import com.org.user.service.UserService;
import com.org.user.util.UserConverter;
import com.org.user.model.dto.UserDto;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SignUpApplication {
    private final UserService userService;
    private final UserConverter converter;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserDto signUp(UserDto userDto) {
        try {
            Assert.notNull(userDto.getTokenDto());
            jwtTokenProvider.validate(userDto.getTokenDto().getAccessToken());
            checkEmailIsNotDuplicated(userDto.getEmail());
            userService.save(converter.convert(userDto));
            return userDto;
        } catch (Exception exception) {
            log.error("[SignUpApplication.signUp] failed to save user", exception);
            throw new SignUpException(exception.getMessage());
        }
    }

    public UserDto changePassword(UserDto userDto) {
        try {
            Assert.notNull(userDto.getTokenDto());
            jwtTokenProvider.validate(userDto.getTokenDto().getAccessToken());
            User user = userService.findByPhoneNumber(userDto.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            return converter.convert(user);
        } catch (Exception exception) {
            log.error("[SignUpApplication.changePassword] failed to change password", exception);
            throw new SignUpException(exception.getMessage());
        }
    }

    private void checkEmailIsNotDuplicated(String email) {
       if(userService.findByEmail(email) != null) {
           throw new SignUpException("email is duplicated");
       }
    }
}
