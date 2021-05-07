package com.oidc.zunsi.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping("/social/login/")
public class SocialController {

    /**
     * 네이버 인증 페이지
     */
    @GetMapping(value = "/naver")
    public String socialKakaoLogin(HttpSession session) {
        return "social/naver/login";
    }

    /**
     * 네이버 인증 완료 후 리다이렉트 화면
     */
    @GetMapping(value = "/naver/callback")
    public String redirectKakao(HttpServletRequest request) throws Exception{
        return "social/naver/callback";
    }
}
