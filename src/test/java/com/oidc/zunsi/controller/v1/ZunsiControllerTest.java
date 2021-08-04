package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.user.UserRepository;
import com.oidc.zunsi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class ZunsiControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    private final String baseUrl = "/v1/zunsi";
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
                .place(new HashSet<>(Collections.singletonList(Place.Daejeon)))
                .favoriteZunsiType(new HashSet<>(Arrays.asList(ZunsiType.installation, ZunsiType.design)))
                .profileImageUrl(null)
                .role(User.Role.USER)
                .build());
        jwt = jwtTokenProvider.createToken(user);
    }

    @Test
    void 전시_생성() throws Exception {
        MockMultipartFile posterImageFile
                = new MockMultipartFile(
                "posterImage",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );


        mvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/new")
                .file("poster image", posterImageFile.getBytes())
                .header("Authorization", jwt)
                .param("title", "ZUNSI 타이틀")
                .param("description", "전시회 상세설명입니다.")
                .param("startDate", "1622960458000")
                .param("endDate", "1628230858000")
                .param("placeName", "뚝섬역 전시장")
                .param("address", "경기도 백옥대로 2432번길 18-2")
                .param("webUrl", "https://github.com/bengHak")
                .param("fee", "5000")
                .param("zunsiType", "design", "installation")
                .param("hashtags", "뚝섬", "설치전시")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    // 사용자 (거주 지역, 선호 전시회 종류)
    // 찜한 전시회 종류, 리뷰 쓴 전시회 종류
    // 현재 날짜
    void 전시회_추천() {
    }

    void 전시_리스트_인기순() {
    }

    void 전시_리스트_거리순() {
    }
}
