package com.oidc.zunsi.controller.v1;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class SignController {

    @GetMapping("/")
    public String hello() {
        return "hello";
    }
}
