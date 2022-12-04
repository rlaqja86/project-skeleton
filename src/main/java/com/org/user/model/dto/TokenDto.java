package com.org.user.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
