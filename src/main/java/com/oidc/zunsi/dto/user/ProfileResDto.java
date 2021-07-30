package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class ProfileResDto {
    private String username;
    private String nickname;
    private List<String> place;
    private List<String> zunsiTypeList;
    private Long reviewNum;
    private Long zzimNum;
    private String profileImgUrl;
}
