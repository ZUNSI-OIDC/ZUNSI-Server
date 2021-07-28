package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.social.naver.NaverProfile;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.dto.auth.SigninReqDto;
import com.oidc.zunsi.dto.auth.SignupReqDto;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.SignService;
import com.oidc.zunsi.service.UserService;
import com.oidc.zunsi.service.social.NaverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Api(tags = {"Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class SignController {

    private final ResponseService responseService;
    private final UserService userService;
    private final SignService signService;
    private final NaverService naverService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "토큰 테스트")
    @GetMapping("/{accessToken}")
    public ResponseEntity<SingleResult<NaverProfile>> hello(
            @PathVariable String accessToken
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(naverService.getProfile(accessToken)));
    }

    @ApiOperation(value = "유저 삭제 (네이버 OAuth 토큰)")
    @DeleteMapping("/sns/{token}")
    public ResponseEntity<SingleResult<String>> deleteUserBySnsToken(
            @PathVariable String token
    ) {
        User user = userService.getUserByProviderAndToken("naver", token);
        userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult("removed"));
    }

    @ApiOperation(value = "유저 삭제")
    @DeleteMapping("/jwt/{jwt}")
    public ResponseEntity<SingleResult<String>> deleteUser(
            @PathVariable String jwt
    ) {

        userService.deleteUser(userService.getUserByJwt(jwt));
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult("removed"));
    }

    @ApiOperation(value = "소셜 로그인")
    @PostMapping(value = "/signin")
    public ResponseEntity<SingleResult<String>> signinByProvider(
            @ApiParam(value = "json") @RequestBody SigninReqDto signinReqDto
    ) {
        User user = userService.getUserByProviderAndToken(signinReqDto.getProvider(), signinReqDto.getAccessToken());
        String jwt = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(jwt));
    }

    @ApiOperation(value = "소셜 계정 가입", notes = "성공시 jwt 토큰을 반환합니다")
    @PostMapping(value = "/signup")
    public ResponseEntity<SingleResult<String>> signupProvider(
            @RequestBody SignupReqDto dto
    ) throws IOException {
        log.info(dto.toString());
        User newUser = signService.getUserFromDto(dto);
        userService.save(newUser, null);
        String jwt = jwtTokenProvider.createToken(String.valueOf(newUser.getId()), newUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseService.getSingleResult(jwt));
    }
}
