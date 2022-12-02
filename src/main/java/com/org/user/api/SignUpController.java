package com.org.user.api;

import com.org.user.application.SignUpApplication;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import com.org.user.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/sign-up")
@AllArgsConstructor
public class SignUpController extends BaseController {

    private final SignUpApplication signUpApplication;

    @PostMapping("/registration")
    public ResponseVo<UserDto> registration(@RequestBody UserDto userDto) {
        UserDto result = signUpApplication.signUp(userDto);
        return new ResponseVo<>(result,
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage());
    }

    @PostMapping("/reset/password")
    public ResponseVo<UserDto> changePassword(@RequestBody UserDto userDto) {
        UserDto result = signUpApplication.changePassword(userDto);
        return new ResponseVo<>(result,
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage());
    }
}
