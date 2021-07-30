package com.oidc.zunsi.dto.zunsi;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ZunsiListRowDto {
    private String thumbnailUrl;
    private String title;
    private Long startDate;
    private Long endDate;
    private String placeName;
    private Boolean isZzimed;
}
