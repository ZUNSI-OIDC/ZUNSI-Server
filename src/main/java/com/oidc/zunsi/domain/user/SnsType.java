package com.oidc.zunsi.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsType {

    naver("naver"),
    apple("apple"),
    test("test");

    private final String type;
}

