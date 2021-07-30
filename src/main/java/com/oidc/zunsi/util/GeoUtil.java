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

    public static int getDistance (Point p1, Point p2) {
        final int R = 6371; // Radious of the earth
        Double lat1 = p1.getX();
        Double lon1 = p1.getY();
        Double lat2 = p2.getX();
        Double lon2 = p2.getY();
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return distance.intValue();
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
}
