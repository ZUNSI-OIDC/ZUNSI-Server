package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.user.SnsType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserService {

    private SignService signService;
    private UserRepository userRepository;

    public boolean isUserExist(String provider, String accessToken) {
        String snsId = signService.getSnsId(provider, accessToken);
        SnsType snsType = SnsType.valueOf(provider);
        Optional<User> user = userRepository.findBySnsIdAndProvider(snsId, snsType);
        return user.isPresent();
    }

    public boolean validateUsername(String username) {
        boolean isValidCharacter = Pattern.matches("^[가-힣0-9a-zA-Z]*$", username);
        return 0 < username.length() && username.length() < 11 && isValidCharacter;
    }

    public void save(User newUser) {
        userRepository.save(newUser);
    }
}
