package com.oidc.zunsi.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SigninReqDto {
    @ApiModelProperty(notes = "naver/apple")
    private String provider;

    @ApiModelProperty(notes = "각 sns에서 제공받은 token")
    private String accessToken;
}
