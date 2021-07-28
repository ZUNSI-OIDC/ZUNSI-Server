package com.oidc.zunsi.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class SignupReqDto {
    private String provider;
    private String accessToken;
    private String name;
    private List<String> placeList;
    private List<String> favoriteZunsiList;
}
