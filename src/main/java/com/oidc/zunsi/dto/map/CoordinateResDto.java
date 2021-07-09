package com.oidc.zunsi.dto.map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CoordinateResDto {
    private String address;
    private String x;
    private String y;
}
