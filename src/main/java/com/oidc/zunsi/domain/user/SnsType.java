package com.oidc.zunsi.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsType {

    kakao("kakao"),
    google("google"),
    facebook("facebook"),
    apple("apple"),
    test("test");

    private final String type;
}

