package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class VisitCountDto {
    Long lastMonth;
    Long currentMonth;
    Long currentYear;
}
