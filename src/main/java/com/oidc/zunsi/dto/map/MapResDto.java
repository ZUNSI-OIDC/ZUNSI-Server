package com.oidc.zunsi.dto.map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class MapResDto {
    private String status;
//    private MetaData meta;
    private Address address;
    private String errorMessage;

//    @Getter
//    @Builder
//    @ToString
//    public class MetaData{
//        private String totalCount;
//        private String page;
//        private String count;
//    }

    @Getter
    @Builder
    @ToString
    public static class Address {
        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
//        private Object addressElements;
        private String x;
        private String y;
    }
}