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
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // ?????? ??????
                .alwaysDo(print()).build();

        User user = userRepository.save(User.builder()
                .snsId(testUserSnsId)
                .provider(testUserSnsType)
                .username("ZUNSI_?????????")
                .role(User.Role.USER)
                .place(new HashSet<>(Collections.singletonList(Place.Daejeon)))
                .favoriteZunsiType(new HashSet<>(Arrays.asList(ZunsiType.installation, ZunsiType.design)))
                .profileImageUrl(null)
                .role(User.Role.USER)
                .build());
        jwt = jwtTokenProvider.createToken(user);
    }

    @Test
    void ??????_??????() throws Exception {
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
                .param("title", "ZUNSI ?????????")
                .param("description", "????????? ?????????????????????.")
                .param("startDate", "1622960458000")
                .param("endDate", "1628230858000")
                .param("placeName", "????????? ?????????")
                .param("address", "????????? ???????????? 2432?????? 18-2")
                .param("webUrl", "https://github.com/bengHak")
                .param("fee", "5000")
                .param("zunsiType", "design", "installation")
                .param("hashtags", "??????", "????????????")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    // ????????? (?????? ??????, ?????? ????????? ??????)
    // ?????? ????????? ??????, ?????? ??? ????????? ??????
    // ?????? ??????
    void ?????????_??????() {
    }

    void ??????_?????????_?????????() {
    }

    void ??????_?????????_?????????() {
    }
}
