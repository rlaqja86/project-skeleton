package com.org.user.util

import com.org.user.entity.User
import com.org.user.model.dto.TokenDto
import com.org.user.model.dto.UserDto
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserConverterTest extends Specification {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    UserConverter sut = new UserConverter(encoder)

    def "verify UserConverter convert userDto to user"() {
        given :
        TokenDto tokenDto = TokenDto.builder().accessToken("access").refreshToken("refresh").build()
        UserDto dto = UserDto.builder()
                .name("kimbeom")
                .tokenDto(tokenDto)
                .email("k1b219@naver.com")
                .phoneNumber("01049249971")
                .nickName("nick")
                .password("1234") .build()
        when:
        User user = sut.convert(dto)

        then:
        user.getName() == "kimbeom"
        user.getEmail() == "k1b219@naver.com"
        user.getPhoneNumber() == "01049249971"
        user.getNickName() == "nick"
        encoder.matches("1234", user.getPassword())
    }

    def "verify UserConverter convert user to userDto"() {
        given :
        User user = User.builder()
                .name("kimbeom")
                .email("k1b219@naver.com")
                .phoneNumber("01049249971")
                .nickName("nick")
                .password(encoder.encode("1234")) .build()
        when:
        UserDto userDto = sut.convert(user)

        then:
        userDto.getName() == "kimbeom"
        userDto.getEmail() == "k1b219@naver.com"
        userDto.getPhoneNumber() == "01049249971"
        userDto.getNickName() == "nick"
        userDto.getPassword() == user.getPassword()
    }

    def "verify UserConverter convert user to userDto with accessToekn"() {
        given :
        User user = User.builder()
                .name("kimbeom")
                .email("k1b219@naver.com")
                .phoneNumber("01049249971")
                .nickName("nick")
                .password(encoder.encode("1234")) .build()
        TokenDto tokenDto = TokenDto.builder().accessToken("access").refreshToken("refresh").build()
        when:
        UserDto userDto = sut.convert(user, tokenDto)

        then:
        userDto.getName() == "kimbeom"
        userDto.getEmail() == "k1b219@naver.com"
        userDto.getPhoneNumber() == "01049249971"
        userDto.getNickName() == "nick"
        userDto.getPassword() == user.getPassword()
        userDto.getTokenDto().getAccessToken() == "access"
        userDto.getTokenDto().getRefreshToken() == "refresh"
    }
}
