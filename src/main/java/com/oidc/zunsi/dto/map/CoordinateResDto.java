package com.oidc.zunsi.dto.map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.geo.Point;

@Getter
@Builder
@ToString
public class CoordinateResDto {
    private String sido;
    private String address;
    private Point point;
}
