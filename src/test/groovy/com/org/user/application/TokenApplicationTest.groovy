package com.org.user.application

import com.org.user.config.jwt.JwtTokenProvider
import com.org.user.model.constraint.TimeConstraint
import spock.lang.Specification

class TokenApplicationTest extends Specification {
    JwtTokenProvider tokenProvider = new JwtTokenProvider()
    TokenApplication sut = new TokenApplication(tokenProvider)

    def "verify generate accessToken by refreshToken"() {
        given:
        String id = "k1b219"
        String refreshToken = tokenProvider.generate(id, TimeConstraint.REFRESH_TOKEN_DURATION)
        when:
        String accessToken = sut.reissue(refreshToken)
        then:
        tokenProvider.getValue(accessToken) == "k1b219"
        tokenProvider.validate(accessToken)
    }
}
