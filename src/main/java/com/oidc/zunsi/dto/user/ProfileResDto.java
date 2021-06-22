package com.oidc.zunsi.dto.user;

import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.ZunsiType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Builder
@Getter
@ToString
public class ProfileResDto {
    private String username;
    private String nickname;
    private Place place;
    private Set<ZunsiType> zunsiTypeList;
    private Long reviewNum;
    private Long zzimNum;
    private String profileImgUrl;
}
