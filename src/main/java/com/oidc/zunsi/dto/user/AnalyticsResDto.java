package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class AnalyticsResDto {
    // 빈도
    private Long lastMonth;
    private Long currentMonth;
    private Long currentYear;

    // 취향
    private Map<String, Double> favoriteData;
}
