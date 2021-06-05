package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.dto.auth.SignupReqDto;
import com.oidc.zunsi.service.social.SocialServiceFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class SignService {

    private final UserService userService;
    private final SocialServiceFactory socialServiceFactory;

    public User getUserFromDto(SignupReqDto dto) {
        String accessToken = dto.getAccessToken();
        String name = dto.getName();
        SnsType provider = SnsType.valueOf(dto.getProvider());
        String place = dto.getPlace();
        Place placeType;
        List<String> zunsiList = dto.getFavoriteZunsiList();
        Set<ZunsiType> zunsiTypeList = new HashSet<>();

        if (userService.isUserExist(provider, accessToken)) throw new IllegalArgumentException("user already exist");
        if (!userService.validateUsername(name)) throw new IllegalArgumentException("illegal username");

        try {
            placeType = Place.valueOf(place);
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal place value");
        }

        if (zunsiList != null) {
            try {
                for (String zunsi : zunsiList) zunsiTypeList.add(ZunsiType.valueOf(zunsi));
            } catch (Exception e) {
                throw new IllegalArgumentException("illegal zunsi type");
            }
        }

        return User.builder()
                .username(name)
                .snsId(socialServiceFactory.getService(provider).getSnsId(accessToken))
                .place(placeType)
                .favoriteZunsiType(zunsiTypeList)
                .provider(provider)
                .profileImageUrl(null)
                .role(User.Role.USER)
                .build();
    }
}
