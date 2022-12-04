package com.org.user.application

import com.org.user.config.jwt.JwtTokenProvider
import com.org.user.exception.SmsException
import com.org.user.model.dto.SignUpDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import spock.lang.Specification

class SmsApplicationTest extends Specification {
    JwtTokenProvider jwtTokenProvider = Mock()
    RedisTemplate<String, String> redisTemplate = Mock()
    SmsRandomCodeGenerator smsRandomCodeGenerator = Mock()
    ValueOperations valueOperations = Mock()

    SmsApplication sut =
            new SmsApplication(jwtTokenProvider, redisTemplate, smsRandomCodeGenerator)

    def "test sendSMSAuthCode verify normal case result"() {
        given:
        redisTemplate.opsForValue() >> valueOperations
        jwtTokenProvider.generate("01049249971") >> "token"
        smsRandomCodeGenerator.generateRandomCode() >> "1111"
        when:
        def result = sut.sendSMSAuthCode("01049249971")
        then:
        result != null
        result.getAccessToken() == "token"
        result.getSmsVerificationCode() == "1111"
    }

    def "test sendSMSAuthCode verify when phoneNumber is Null then IllegalArgumentException thrown "() {
        when:
        sut.sendSMSAuthCode(null)
        then:
        IllegalArgumentException ex = thrown()
    }

    def "test updateNextUrl verify registration case"() {
        given:
        redisTemplate.opsForValue() >> valueOperations
        jwtTokenProvider.getValue("token") >> "01049249971"
        valueOperations.get("01049249971") >> "1111"
        when:
        SignUpDto dto = SignUpDto.builder()
                .command(UserCommand.REGISTRATION.name())
                .accessToken("token")
                .smsVerificationCode("1111")
                .build()
        def result = sut.updateNextUrl(dto)
        then:
        result != null
        result.getSignUpNextUrl() == UserCommand.REGISTRATION.getUrl()
    }

    def "test updateNextUrl verify change password case"() {
        given:
        redisTemplate.opsForValue() >> valueOperations
        jwtTokenProvider.getValue("token") >> "01049249971"
        valueOperations.get("01049249971") >> "1111"
        when:
        SignUpDto dto = SignUpDto.builder()
                .command(UserCommand.CHANGE_PASSWORD.name())
                .accessToken("token")
                .smsVerificationCode("1111")
                .build()
        def result = sut.updateNextUrl(dto)
        then:
        result != null
        result.getSignUpNextUrl() == UserCommand.CHANGE_PASSWORD.getUrl()
    }

    def "test updateNextUrl verify code is not matched"() {
        given:
        redisTemplate.opsForValue() >> valueOperations
        jwtTokenProvider.getValue("token") >> "invalid_code"
        valueOperations.get("01049249971") >> "1111"
        when:
        SignUpDto dto = SignUpDto.builder()
                .command(UserCommand.REGISTRATION.name())
                .accessToken("token")
                .smsVerificationCode("1111")
                .build()
        def result = sut.updateNextUrl(dto)
        then:
        SmsException ex = thrown()
    }

    def "test updateNextUrl accessToken is null"() {
        given:
        redisTemplate.opsForValue() >> valueOperations
        jwtTokenProvider.getValue("token") >> "invalid_code"
        valueOperations.get("01049249971") >> "1111"
        when:
        SignUpDto dto = SignUpDto.builder()
                .command(UserCommand.REGISTRATION.name())
                .accessToken(null)
                .smsVerificationCode("1111")
                .build()
        def result = sut.updateNextUrl(dto)
        then:
        IllegalArgumentException ex = thrown()
    }

}
