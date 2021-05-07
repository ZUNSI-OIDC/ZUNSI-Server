package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.user.SnsType;
import com.oidc.zunsi.service.social.NaverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SignService {

    private final NaverService naverService;
    public String getSnsId(String provider, String accessToken) {
        String snsId;
        if (provider.equals(SnsType.naver.getType())) {
            snsId = naverService.getId();
        } else if (provider.equals(SnsType.apple.getType())) {
            throw new IllegalArgumentException("not supported sns");
        }else {
            throw new IllegalArgumentException("not supported sns");
        }

        return snsId;
    }
}
