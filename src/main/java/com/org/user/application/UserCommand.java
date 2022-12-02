package com.org.user.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public enum UserCommand {
    REGISTRATION("/api/v1/auth/sign-up/registration"),
    CHANGE_PASSWORD("/api/v1/auth/sign-up/reset/password"),
    NONE("");

    private String url;

    public static UserCommand of(String inputCommand) {
        for (UserCommand command : UserCommand.values()) {
            if (StringUtils.pathEquals(inputCommand, command.name())) {
                return command;
            }
        }
        return NONE;
    }
}
