package com.org.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String nickName;
    private String password;
    private String name;
    private String phoneNumber;
    private TokenDto tokenDto;
}
