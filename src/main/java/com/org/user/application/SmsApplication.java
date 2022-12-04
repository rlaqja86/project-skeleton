package com.org.user.application;

import com.org.user.config.jwt.JwtTokenProvider;
import com.org.user.exception.SmsException;
import com.org.user.model.constraint.TimeConstraint;
import com.org.user.model.dto.SignUpDto;
import com.org.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SmsApplication {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final SmsRandomCodeGenerator smsRandomCodeGenerator;

    public SignUpDto sendSMSAuthCode(String phoneNumber) {
        Assert.notNull(phoneNumber, "phoneNumber should not null");

        String code = smsRandomCodeGenerator.generateRandomCode();
        //caching code for verification - key is phoneNumber
        redisTemplate.opsForValue().set(phoneNumber, code);
        //expiration time is 1 minute
        redisTemplate.expire(phoneNumber, 60, TimeUnit.SECONDS);

        String token = jwtTokenProvider.generate(phoneNumber, TimeConstraint.REGISTRATION_DURATION);

        return SignUpDto.builder().accessToken(token).smsVerificationCode(code).build();
    }

    public SignUpDto updateNextUrl(SignUpDto vo) {
        Assert.notNull(vo.getAccessToken(), "accessToken should not null");

        String token = vo.getAccessToken();
        jwtTokenProvider.validate(token);
        String phoneNumber = jwtTokenProvider.getValue(token);
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        if (storedCode == null || !StringUtils.pathEquals(vo.getSmsVerificationCode(), storedCode)) {
            throw new SmsException("sms code is not matched");
        }
        vo.setSignUpNextUrl(UserCommand.of(vo.getCommand()).getUrl());

        return vo;
    }
}
