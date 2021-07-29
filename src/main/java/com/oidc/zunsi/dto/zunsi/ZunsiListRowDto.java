package com.oidc.zunsi.dto.zunsi;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Builder
public class ZunsiListRowDto {
    private String thumbnailUrl;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String placeName;
    private Boolean isZzimed;
}
