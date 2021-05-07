package com.oidc.zunsi.dto.auth;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupReqDto {
    private String provider;
    private String accessToken;
    private String name;
}
