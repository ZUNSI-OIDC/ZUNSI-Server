package com.oidc.zunsi.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class CommonControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    private final String baseUrl = "/v1/common";

    @BeforeEach()
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
                .alwaysDo(print()).build();
    }

    @Test
    void getZunsiList() throws Exception {
        mvc.perform(get(baseUrl +"/zunsi"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getPlaceList() throws Exception {
        mvc.perform(get(baseUrl +"/place"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}