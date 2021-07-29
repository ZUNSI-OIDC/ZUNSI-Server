package com.oidc.zunsi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Place {
    Seoul("서울", "서울특별시"),
    Gyeonggi("경기", "경기도"),
    Gangwon("강원", "강원도"),
    Sejong("세종", "세종특별자치시"),
    Daejeon("대전", "대전광역시"),
    Daegu("대구", "대구광역시"),
    Ulsan("울산", "울산광역시"),
    Gwangju("광주", "광주광역시"),
    Busan("부산", "부산광역시"),
    Jeju("제주", "제주특별자치도"),
    etc("기타", "기타");
    ;

    private String ko;
    private String key;
}
