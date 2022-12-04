package com.org.user.application;


import com.org.user.model.dto.TokenDto;
import com.org.user.model.dto.UserDto;
import com.org.user.service.UserService;
import com.org.user.util.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserApplication {

    private final UserConverter userConverter;
    private final UserService userService;

    public UserDto findByEmail(String email, TokenDto token) {
        return userConverter.convert(userService.findByEmail(email), token);
    }

    public UserDto findByEmail(String email) {
        return userConverter.convert(userService.findByEmail(email));
    }
}
