package com.org.user.application

import com.org.user.config.jwt.JwtTokenProvider
import com.org.user.entity.User
import com.org.user.exception.SignUpException
import com.org.user.model.dto.UserDto
import com.org.user.service.UserService
import com.org.user.util.UserConverter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder;
import spock.lang.Specification

class SignUpApplicationTest extends Specification {
    UserService userService = Mock()
    JwtTokenProvider jwtTokenProvider = new JwtTokenProvider()
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    UserConverter converter = new UserConverter(passwordEncoder)
    SignUpApplication sut = new SignUpApplication(userService, converter, jwtTokenProvider, passwordEncoder)


    def "verify sign up normal case"() {
        given:
        String token = jwtTokenProvider.generate("k1b219@naver.com")
        UserDto dto = UserDto.builder().email("k1b219@naver.com").password("1234").accessToken(token).build()
        when:
        UserDto result = sut.signUp(dto)

        then:
        dto != null
        result.getEmail() == "k1b219@naver.com"
    }

    def "verify sign up Access Token is invalid case"() {
        given:
        String token = jwtTokenProvider.generate("k1b219@naver.com") + "someInvalidTokenCharacter"
        UserDto dto = UserDto.builder().email("k1b219@naver.com").password("1234").accessToken(token).build()
        when:
        UserDto result = sut.signUp(dto)

        then:
        dto != null
        SignUpException ex = thrown()
        ex.getMessage() == "invalid token : JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."
    }

    def "verify sign up duplicated email exist case"() {
        given:
        String token = jwtTokenProvider.generate("k1b219@naver.com")
        UserDto dto = UserDto.builder().email("k1b219@naver.com").password("1234").accessToken(token).build()
        User user = User.builder().name("k1b219@naver.com").password("1234").build()
        userService.findByEmail("k1b219@naver.com") >> user
        when:
        UserDto result = sut.signUp(dto)

        then:
        dto != null
        SignUpException ex = thrown()
        ex.getMessage() == "email is duplicated"
    }

    def "verify change password norma case"() {
        given:
        String token = jwtTokenProvider.generate("k1b219@naver.com")
        UserDto dto = UserDto.builder().email("k1b219@naver.com").phoneNumber("01049249971").password("after").accessToken(token).build()
        User user = User.builder().name("k1b219@naver.com").password("before").build()
        userService.findByPhoneNumber("01049249971") >> user
        when:
        UserDto result = sut.changePassword(dto)
        then:
        dto != null
        passwordEncoder.matches("after", result.getPassword())
    }

    def "verify change password access token invalid case"() {
        given:
        String token = jwtTokenProvider.generate("k1b219@naver.com") + "someInvalidTokenCharacter"
        UserDto dto = UserDto.builder().email("k1b219@naver.com").phoneNumber("01049249971").password("after").accessToken(token).build()
        User user = User.builder().name("k1b219@naver.com").password("before").build()
        userService.findByPhoneNumber("01049249971") >> user
        when:
        UserDto result = sut.changePassword(dto)
        then:
        dto != null
        SignUpException ex = thrown()
        ex.getMessage() == "invalid token : JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."    }
}
