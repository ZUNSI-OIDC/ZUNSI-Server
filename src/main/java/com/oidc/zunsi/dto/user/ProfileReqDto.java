package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@ToString
public class ProfileReqDto {
    private String username;
    private String nickname;
    private List<String> place;
    private List<String> favoriteZunsiList;
    private MultipartFile profileImage;
}
