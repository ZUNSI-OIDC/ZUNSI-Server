package com.oidc.zunsi.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
@RequestMapping("/social/login/")
public class SocialController {

    @Value("${spring.naver.client-id}")
    private String clientId;

    @Value("${spring.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.naver.callback-url}")
    private String callbackUrl;

//    /**
//     * 네이버 인증 페이지
//     */
//    @GetMapping(value = "/naver")
//    public String socialKakaoLogin(HttpServletRequest request, Model model) {
//        model.addAttribute("clientId", clientId);
//        model.addAttribute("callbackUrl", callbackUrl);
//        return "social/naver/login";
//    }
//
//    /**
//     * 네이버 인증 완료 후 리다이렉트 화면
//     */
//    @GetMapping(value = "/naver/callback")
//    public String redirectKakao(HttpServletRequest request, Model model) throws Exception{
//        model.addAttribute("clientId", clientId);
//        model.addAttribute("clientSecret", clientSecret);
//        model.addAttribute("callbackUrl", callbackUrl);
//        return "social/naver/callback";
//    }
}
