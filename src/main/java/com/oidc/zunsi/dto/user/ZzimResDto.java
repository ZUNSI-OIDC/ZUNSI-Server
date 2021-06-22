package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ZzimResDto {
    private Long zunsiId;
    private String title;
    private String placeName;
    private Long startDate;
    private Long endDate;
    private String posterImageUrl;
}
