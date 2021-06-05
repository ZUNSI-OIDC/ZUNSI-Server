package com.oidc.zunsi.domain.enums;

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

