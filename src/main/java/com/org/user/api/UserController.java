package com.org.user.api;

import com.org.user.application.UserApplication;
import com.org.user.model.dto.UserDto;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserApplication userApplication;

    @GetMapping
    public ResponseVo<UserDto> getUser(@RequestParam String email) {
        UserDto dto = userApplication.findByEmail(email);
        return new ResponseVo<>(dto, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
