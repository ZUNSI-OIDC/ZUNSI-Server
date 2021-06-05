package com.oidc.zunsi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ZunsiType {
    painting("회화"),
    sculpture("조각"),
    architecture("건축"),
    picture("사진"),
    craft("공예"),
    engraving("판화"),
    illustration("일러스트"),
    design("디자인"),
    installation("설치"),
    space("공간"),
    media("미디어"),
    other("기타")
    ;

    private String ko;
}