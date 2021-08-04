package com.oidc.zunsi.service.social;

import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.dto.auth.SnsInfoDto;

public interface SocialService {
    public SnsInfoDto getSnsInfo(String token);
    public String getProfileImageUrl(String token);
    public String getEmail(String token);
    public SnsType getSnsType();
}
