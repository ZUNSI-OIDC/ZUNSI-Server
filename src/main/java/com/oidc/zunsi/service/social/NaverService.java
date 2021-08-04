package com.oidc.zunsi.service.social;

import com.google.gson.Gson;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.social.naver.NaverProfile;
import com.oidc.zunsi.dto.auth.SnsInfoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class NaverService implements SocialService {

    @Override
    public SnsInfoDto getSnsInfo(String token) {
        NaverProfile naverProfile = getProfile(token);
        return SnsInfoDto.builder()
                .id(naverProfile.getId())
                .name(naverProfile.getName())
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
        return SnsType.naver;
    }

    public NaverProfile getProfile(String accessToken) {
        String res = "";
        String header = "Bearer " + accessToken; // Bearer 다음에 공백 추가
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                throw new IllegalArgumentException("invalid token");
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            res = response.toString();
        } catch (Exception e) {
            System.out.println(e);
            if (e.getClass() == IllegalArgumentException.class)
                throw new IllegalArgumentException(e.getMessage());
        }

        Gson gson = new Gson();
        Map profile = gson.fromJson(res, Map.class);
        String jsonElement = gson.toJson(profile.get("response"));
        log.info(jsonElement);

        NaverProfile naverProfile = gson.fromJson(jsonElement, NaverProfile.class);
        if (naverProfile == null) {
            throw new IllegalArgumentException("네이버 프로필을 가져오지 못했습니다");
        }
        return naverProfile;
    }
}
