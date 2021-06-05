package com.oidc.zunsi.service.social;

import com.oidc.zunsi.domain.enums.SnsType;

public interface SocialService {
    public String getSnsId(String token);
    public String getProfileImageUrl(String token);
    public String getEmail(String token);
    public SnsType getSnsType();
}
