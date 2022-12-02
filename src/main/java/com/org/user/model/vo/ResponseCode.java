package com.org.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(101, "success"),
    FAILED_SIGN_UP(201, "failed to sign up"),
    FAILED_LOGIN(203, "failed to log in, id or password is wrong"),
    FAILED_SMS(202, "failed to notify sms"),
    FAILED_UNKNOWN(300, "unknown error");

    private int code;
    private String message;

}
