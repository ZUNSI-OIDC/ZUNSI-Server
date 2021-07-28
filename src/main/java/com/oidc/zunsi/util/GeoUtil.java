package com.oidc.zunsi.util;

import com.oidc.zunsi.dto.map.RectBoxDto;
import org.springframework.data.geo.Point;

public class GeoUtil {
    public static RectBoxDto getRectBox (Point point) {
        return RectBoxDto.builder()
                .upperLeft(new Point(point.getX() - 0.1, point.getY() + 0.1))
                .lowerRight(new Point(point.getX() + 0.1, point.getY() - 0.1))
                .build();
    }
}
