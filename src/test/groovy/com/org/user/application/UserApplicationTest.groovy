package com.org.user.application

import com.org.user.entity.User
import com.org.user.model.dto.UserDto
import com.org.user.service.UserService
import com.org.user.util.UserConverter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserApplicationTest extends Specification {

    UserService service = Mock()
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
    UserConverter converter = new UserConverter(encoder)
    UserApplication application = new UserApplication(converter, service)


    def "verify find by email normal case" () {
        given:
        User user = User.builder().email("k1b219@naver.com").build()
        service.findByEmail("k1b219@naver.com") >> user
        when:
        UserDto userDto = application.findByEmail("k1b219@naver.com","token")
        then:
        userDto.getEmail() == "k1b219@naver.com"
        userDto.getAccessToken() == "token"
    }


}
