package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final String baseUrl = "/v1/user";
    private final SnsType testUserSnsType = SnsType.test;
    private final String testUserSnsId = "960318";
    private String jwt;

    @BeforeEach()
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
                .alwaysDo(print()).build();

        User user = userRepository.save(User.builder()
                .snsId(testUserSnsId)
                .provider(testUserSnsType)
                .username("ZUNSI_테스터")
                .role(User.Role.USER)
                .place(Place.Daejeon)
                .favoriteZunsiType(new HashSet<>(Arrays.asList(ZunsiType.installation, ZunsiType.design)))
                .profileImageUrl(null)
                .role(User.Role.USER)
                .build());
        jwt = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole());
    }

    @Test
    void getProfile() throws Exception {
        mvc.perform(get(baseUrl + "/me")
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username", "ZUNSI_테스터").exists())
                .andExpect(jsonPath("$..place", Place.Daejeon.toString()).exists())
                .andDo(print());
    }

    @Test
    void getAnalytics() throws Exception {
        mvc.perform(get(baseUrl + "/analytics")
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..lastMonth").value(0))
                .andExpect(jsonPath("$..currentMonth").value(0))
                .andExpect(jsonPath("$..currentYear").value(0))
                .andExpect(jsonPath("$..engraving").value(0.))
                .andDo(print());
    }

    // GET 프로필 이미지 수정
    @Test
    void updateProfile() throws Exception {
        mvc.perform(post(baseUrl + "/me")
                .header("Authorization", jwt)
                .param("username", "카카로트")
                .param("place", "Seoul")
                .param("favoriteZunsiList", "picture", "design")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(get(baseUrl + "/me")
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..username").value("카카로트"))
                .andExpect(jsonPath("$..place", Place.Seoul.toString()).exists())
                .andDo(print());
    }

    @Test
    void getZzimZunsi() throws Exception {
        mvc.perform(get(baseUrl + "/me/zzims")
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getMyReview() throws Exception {
        mvc.perform(get(baseUrl + "/me/reviews")
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 알림_설정_테스트() throws Exception {
        String content = "{\"isEnabled\": true}";

        mvc.perform(post(baseUrl + "/me/notification")
                .header("Authorization", jwt)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
