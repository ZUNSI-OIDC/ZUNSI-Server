package com.oidc.zunsi.service.social;

import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.dto.auth.SnsInfoDto;
import org.springframework.stereotype.Service;

@Service
public class TestService implements SocialService {
    @Override
    public SnsInfoDto getSnsInfo(String token) {
        return SnsInfoDto.builder()
                .id(token)
                .name("익명의 유저")
                .build();
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
