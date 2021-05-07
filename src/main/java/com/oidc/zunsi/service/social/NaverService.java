package com.oidc.zunsi.service.social;

import com.google.gson.Gson;
import com.oidc.zunsi.domain.social.naver.NaverProfile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@AllArgsConstructor
@Service
public class NaverService {

    public NaverProfile getProfile(String accessToken) {
        String res = "";
        String token = accessToken; // 네이버 로그인 접근 토큰; 여기에 복사한 토큰값을 넣어줍니다.
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
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
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            // System.out.println(response.toString());
            res = response.toString();
        } catch (Exception e) {
            System.out.println(e);
        }

        Gson gson = new Gson();
        Map profile = gson.fromJson(res, Map.class);
        String jsonElement = gson.toJson(profile.get("response"));
        NaverProfile naverProfile = gson.fromJson(jsonElement, NaverProfile.class);

        return naverProfile;
    }

    // 유니코드에서 String으로 변환
    public static String convertString(String val) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < val.length(); i++) {
            // 조합이 \\u로 시작하면 6글자를 변환한다. \\uxxxx
            if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {
                // 그 뒤 네글자는 유니코드의 16진수 코드이다. int형으로 바꾸어서 다시 char 타입으로 강제 변환한다.
                Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
                // 변환된 글자를 버퍼에 넣는다.
                sb.append(r);
                // for의 증가 값 1과 5를 합해 6글자를 점프
                i += 5;
            } else {
                // ascii코드면 그대로 버퍼에 넣는다.
                sb.append(val.charAt(i));
            }
        }

        return sb.toString();
    }

    public String getId() {

        return "hello";
    }
}
