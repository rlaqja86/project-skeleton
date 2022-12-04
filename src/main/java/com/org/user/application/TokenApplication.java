package com.org.user.application;

import com.org.user.config.jwt.JwtTokenProvider;
import com.org.user.model.constraint.TimeConstraint;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenApplication {

    private final JwtTokenProvider tokenProvider;

    public String reissue(String refreshToken) {
        tokenProvider.validate(refreshToken);
        String id = tokenProvider.getValue(refreshToken);
        return tokenProvider.generate(id, TimeConstraint.ACCESS_TOKEN_DURATION);
    }

}
