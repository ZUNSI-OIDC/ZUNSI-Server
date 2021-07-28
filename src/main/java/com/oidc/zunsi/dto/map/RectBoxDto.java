package com.oidc.zunsi.dto.map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.geo.Point;

@Getter
@ToString
@Builder
public class RectBoxDto {
    private Point upperLeft;
    private Point lowerRight;
}
