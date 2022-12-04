package com.org.user.application

import spock.lang.Specification

class SmsRandomCodeGeneratorTest extends Specification {
    SmsRandomCodeGenerator sut = new SmsRandomCodeGenerator()

    def "length of code is always 4"() {
        when:
        def x = sut.generateRandomCode()
        then:
        x.length() == 4
        x != null
        x.class == String.class
    }
}
