package com.oidc.zunsi.dto.auth;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class SignupReqDto {
    private String provider;
    private String accessToken;
    private String name;
    private String place;
    private List<String> favoriteZunsiList;
}
