package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.social.naver.NaverProfile;
import com.oidc.zunsi.domain.user.SnsType;
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

    @ApiOperation(value = "소셜 로그인")
    @PostMapping(value = "/signin")
    public ResponseEntity<SingleResult<String>> signinByProvider(
            @ApiParam(value = "json") @RequestBody SigninReqDto signinReqDto
    ) {
        User user = userService.getUserByProviderAndToken(signinReqDto.getProvider(), signinReqDto.getAccessToken());
        // userService.registerAccessToken(user, fcmMessageService.getAccessToken());
        String jwt = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(jwt));
    }

    @ApiOperation(value = "소셜 계정 가입", notes = "성공시 jwt 토큰을 반환합니다")
    @PostMapping(value = "/signup")
    public ResponseEntity<SingleResult<String>> signupProvider(
            @ApiParam(value = "json") @RequestBody SignupReqDto signupReqDto
    ) {
        String provider = signupReqDto.getProvider();
        String accessToken = signupReqDto.getAccessToken();
        String name = signupReqDto.getName();
        SnsType snsType = SnsType.valueOf(provider);
        // TODO: SNS ID 를 각 프로필에서 가져오도록 로직 변경
        String snsId = signService.getSnsId(provider, accessToken);

        if (userService.isUserExist(provider, accessToken)) throw new IllegalArgumentException("already registered");
        if (!userService.validateUsername(name)) throw new IllegalArgumentException("illegal name");

        User newUser = User.builder()
                .snsId(snsId)
                .provider(snsType)
                .username(name)
                .role(User.Role.USER)
                .build();
        userService.save(newUser);

        String jwt = jwtTokenProvider.createToken(String.valueOf(newUser.getId()), newUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseService.getSingleResult(jwt));
    }
}
