package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.enums.SnsType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class SignControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private UserRepository userRepository;

    private final String baseUrl = "/v1/auth";
    private final SnsType testUserSnsType = SnsType.test;
    private final String testUserSnsId = "960318";

    @BeforeEach()
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
                .alwaysDo(print()).build();

        // sign in test 를 위한 회원가입
        userRepository.save(User.builder()
                .snsId(testUserSnsId)
                .provider(testUserSnsType)
                .username("ZUNSI_테스터")
                .role(User.Role.USER)
                .build());
    }

    @Test
    void test_access_token() throws Exception {
        String testToken = "123124123";
        mvc.perform(get(baseUrl + "/" + testToken))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void sign_up() throws Exception {
        String content = "{\n" +
                "    \"provider\": \"test\",\n" +
                "    \"accessToken\": \"1111\",\n" +
                "    \"name\": \"카카로트\",\n" +
                "    \"place\": \"Seoul\",\n" +
                "    \"favoriteZunsiList\": [\n" +
                "        \"installation\",\n" +
                "        \"design\"\n" +
                "    ]\n" +
                "}";

        mvc.perform(post(baseUrl + "/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void sign_in() throws Exception {
        String content = "{\n" +
                "    \"provider\": \"" + testUserSnsType.getType() + "\",\n" +
                "    \"accessToken\": \"" + testUserSnsId + "\"\n" +
                "}";

        mvc.perform(post(baseUrl + "/signin")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
