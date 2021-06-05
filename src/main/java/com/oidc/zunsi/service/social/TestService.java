package com.oidc.zunsi.service.social;

import com.oidc.zunsi.domain.enums.SnsType;
import org.springframework.stereotype.Service;

@Service
public class TestService implements SocialService {
    @Override
    public String getSnsId(String token) {
        return token;
    }

    @Override
    public String getProfileImageUrl(String token) {
        return null;
    }

    @Override
    public String getEmail(String token) {
        return null;
    }

    @Override
    public SnsType getSnsType() {
        return SnsType.test;
    }
}
