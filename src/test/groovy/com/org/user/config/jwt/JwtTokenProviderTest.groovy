package com.org.user.config.jwt

import com.org.user.model.constraint.TimeConstraint
import spock.lang.Specification

class JwtTokenProviderTest extends Specification {
    JwtTokenProvider sut = new JwtTokenProvider()

    def "verify JwtTokenProvider generate accessToken" () {
        given:
        String targetKey = "test"
        when:
        String accessToken = sut.generate(targetKey, TimeConstraint.ACCESS_TOKEN_DURATION)
        then:
        accessToken != null
        accessToken.size() == 129
    }

    def "verify JwtTokenProvider return value of accessToken" () {
        given:
        String targetKey = "test"
        String accessToken = sut.generate(targetKey, TimeConstraint.ACCESS_TOKEN_DURATION)
        when:
        String value = sut.getValue(accessToken)
        then:
        value != null
        value == targetKey
    }

    def "verify JwtTokenProvider validate accessToken" () {
        given:
        String targetKey = "test"
        String accessToken = sut.generate(targetKey, TimeConstraint.ACCESS_TOKEN_DURATION)
        when:
        Boolean value = sut.validate(accessToken)
        then:
        value
    }


}
