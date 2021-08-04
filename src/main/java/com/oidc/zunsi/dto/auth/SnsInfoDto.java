package com.oidc.zunsi.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SnsInfoDto {
    private String name;
    private String id;
}
