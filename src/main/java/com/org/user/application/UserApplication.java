package com.org.user.application;


import com.org.user.model.dto.UserDto;
import com.org.user.service.UserService;
import com.org.user.util.UserConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserApplication {

    private final UserConverter userConverter;
    private final UserService userService;

    @Transactional
    public UserDto findByEmail(String email, String token) {
        return userConverter.convert(userService.findByEmail(email), token);
    }
}
