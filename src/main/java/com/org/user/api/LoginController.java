package com.org.user.api;

import com.org.user.application.UserApplication;
import com.org.user.auth.CustomEmailPasswordToken;
import com.org.user.config.jwt.JwtTokenProvider;
import com.org.user.model.constraint.TimeConstraint;
import com.org.user.model.dto.TokenDto;
import com.org.user.model.dto.UserDto;
import com.org.user.model.vo.LoginRequestVo;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserApplication userApplication;

    @PostMapping("/login")
    public ResponseVo<UserDto> login(@RequestBody LoginRequestVo loginRequestVo) {
        CustomEmailPasswordToken customEmailPasswordToken = new CustomEmailPasswordToken(loginRequestVo.getEmail(), loginRequestVo.getPassword());
        Authentication authentication = authenticationManager.authenticate(customEmailPasswordToken);
        String accessToken = jwtTokenProvider.generate((String) authentication.getPrincipal(), TimeConstraint.ACCESS_TOKEN_DURATION);
        String refreshToken = jwtTokenProvider.generate((String) authentication.getPrincipal(), TimeConstraint.REFRESH_TOKEN_DURATION);
        UserDto userDto = userApplication.findByEmail(loginRequestVo.getEmail(), TokenDto.builder().refreshToken(refreshToken).accessToken(accessToken).build());

        return new ResponseVo<>(userDto, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
