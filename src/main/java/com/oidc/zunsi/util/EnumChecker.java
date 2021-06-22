package com.oidc.zunsi.util;

import com.google.common.base.Enums;
import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.ZunsiType;

public class EnumChecker {
    public static boolean checkValidZunsiType(String zunsiTypeString) {
        return Enums.getIfPresent(ZunsiType.class, zunsiTypeString).isPresent();
    }

    public static boolean checkValidPlace(String placeString) {
        return Enums.getIfPresent(Place.class, placeString).isPresent();
    }
}
