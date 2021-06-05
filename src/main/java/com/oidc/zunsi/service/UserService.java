package com.oidc.zunsi.service;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.user.UserRepository;
import com.oidc.zunsi.service.social.SocialServiceFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserService {

    private final SocialServiceFactory socialServiceFactory;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean isUserExist(SnsType provider, String accessToken) {
        String snsId = getSnsId(provider, accessToken);
        Optional<User> user = userRepository.findBySnsIdAndProvider(snsId, provider);
        return user.isPresent();
    }

    public User getUserByProviderAndToken(String provider, String accessToken) {
        SnsType snsType;
        try {
            snsType = SnsType.valueOf(provider);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid provider");
        }
        String snsId = getSnsId(snsType, accessToken);
        return userRepository.findBySnsIdAndProvider(snsId, snsType).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    public boolean validateUsername(String username) {
        boolean isValidCharacter = Pattern.matches("^[가-힣0-9a-zA-Z]*$", username);
        return 0 < username.length() && username.length() < 11 && isValidCharacter;
    }

    public String getSnsId(SnsType provider, String accessToken) {
        return socialServiceFactory.getService(provider).getSnsId(accessToken);
    }

    public void save(User newUser) {
        userRepository.save(newUser);
    }

    public User getUserByJwt(String jwt) {
        String userId = jwtTokenProvider.getUserPk(jwt);
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }
}
