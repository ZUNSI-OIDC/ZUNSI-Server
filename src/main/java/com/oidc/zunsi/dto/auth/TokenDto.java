package com.oidc.zunsi.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
