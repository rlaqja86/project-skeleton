package com.org.user.api;

import com.org.user.application.TokenApplication;
import com.org.user.model.dto.TokenDto;
import com.org.user.model.vo.ResponseCode;
import com.org.user.model.vo.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/token")
@AllArgsConstructor
public class TokenController {

    private final TokenApplication tokenApplication;

    @GetMapping("/reissue")
    public ResponseVo<TokenDto> reissue(@RequestParam String refreshToken) {
        return new ResponseVo<>(TokenDto.builder()
                .refreshToken(refreshToken)
                .accessToken(tokenApplication.reissue(refreshToken))
                .build(), ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
