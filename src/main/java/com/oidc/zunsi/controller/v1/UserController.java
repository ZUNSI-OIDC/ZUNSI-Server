package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.dto.user.*;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.UserService;
import com.oidc.zunsi.util.EnumChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Api(tags = {"User"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내 프로필 조회")
    @GetMapping("/me")
    public ResponseEntity<SingleResult<ProfileResDto>> getProfile(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        ProfileResDto dto = userService.getProfileResDto(user);
        log.info(dto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내 프로필 수정")
    @PostMapping("/me")
    public ResponseEntity<SingleResult<String>> updateProfile(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) List<String> placeList,
            @RequestParam(required = false) List<String> favoriteZunsiList,
            @RequestParam(required = false) MultipartFile profileImage
    ) throws IOException {
        User user = userService.getUserByJwt(jwt);

        for (String place : placeList) {
            if (!EnumChecker.checkValidPlace(place))
                throw new IllegalArgumentException("invalid place");
        }
        for (String zunsi : favoriteZunsiList) {
            if (!EnumChecker.checkValidZunsiType(zunsi))
                throw new IllegalArgumentException("invalid zunsi type");
        }
        ProfileReqDto dto = ProfileReqDto.builder()
                .username(username != null ? username : user.getUsername())
                .nickname(nickname != null ? nickname : user.getNickname())
                .place(placeList)
                .favoriteZunsiList(favoriteZunsiList)
                .profileImage(profileImage)
                .build();
        userService.updateUser(user, dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult("success"));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "프로필 이미지 수정")
    @PostMapping("/me/profile-image")
    public ResponseEntity<SingleResult<String>> updateProfileImage(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) MultipartFile profileImage
    ) throws IOException {
        User user = userService.getUserByJwt(jwt);
        if (user == null) throw new IllegalArgumentException("invalid jwt token");
        if (profileImage.isEmpty()) throw new IllegalArgumentException("no image");
        userService.updateProfileImage(user, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult("success"));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내가 방문한 전시 선호도 분석")
    @GetMapping("/analytics")
    public ResponseEntity<SingleResult<AnalyticsResDto>> getAnalytics(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        AnalyticsResDto dto = userService.getAnalytics(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내 찜 리스트")
    @GetMapping("/me/zzims")
    public ResponseEntity<SingleResult<List<ZzimResDto>>> getZzimList(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        List<ZzimResDto> dto = userService.getZzimList(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "알림 설정")
    @PostMapping("/me/notification")
    public ResponseEntity<SingleResult<NotificationResDto>> changeNotificationSetting(
            @RequestHeader("Authorization") String jwt,
            @RequestBody NotificationReqDto dto
    ) {
        User user = userService.getUserByJwt(jwt);
        userService.changeNotificationOption(user, dto.getIsEnabled());
        NotificationResDto resDto = NotificationResDto.builder()
                .msg(dto.getIsEnabled() ? "notification enabled" : "notification disabled")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(resDto));
    }
}
