package com.oidc.zunsi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Place {
    Seoul("서울"),
    Gyeonggi("경기"),
    Gangwon("강원"),
    Sejong("세종"),
    Daejeon("대전"),
    Daegu("대구"),
    Ulsan("울산"),
    Gwangju("광주"),
    Busan("부산"),
    Jeju("제주"),
    etc("기타");
    ;

    private String ko;
}
