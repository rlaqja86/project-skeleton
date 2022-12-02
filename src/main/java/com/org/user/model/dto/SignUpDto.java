package com.org.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String signUpNextUrl;
    private String accessToken;
    private String smsVerificationCode;
    private String command;
}
