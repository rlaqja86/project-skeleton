package com.org.user.application;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SmsRandomCodeGenerator {
    public String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i ++) {
            code.append(random.nextInt(9));
        }
        return code.toString();
    }
}
