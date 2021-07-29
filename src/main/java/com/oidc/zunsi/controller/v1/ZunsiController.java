package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.PageResult;
import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.zunsi.ZunsiCreateReqDto;
import com.oidc.zunsi.dto.zunsi.ZunsiPageDto;
import com.oidc.zunsi.dto.zunsi.ZunsiResDto;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.UserService;
import com.oidc.zunsi.service.ZunsiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Api(tags = {"Zunsi"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/zunsi")
public class ZunsiController {

    private final ZunsiService zunsiService;
    private final UserService userService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "전시 등록")
    @PostMapping(value = "/new")
    public ResponseEntity<SingleResult<ZunsiResDto>> createNewZunsi(
            @RequestHeader("Authorization") String jwt,
            @ApiParam(value = "title") @RequestParam String title,
            @ApiParam(value = "description") @RequestParam String description,
            @ApiParam(value = "artist") @RequestParam String artist,
            @ApiParam(value = "start date") @RequestParam Long startDate,
            @ApiParam(value = "end date") @RequestParam Long endDate,
            @ApiParam(value = "start time (ex. 1430)") @RequestParam Integer startTime,
            @ApiParam(value = "end time (ex. 1950)") @RequestParam Integer endTime,
            @ApiParam(value = "address") @RequestParam String address,
            @ApiParam(value = "place name") @RequestParam String placeName,
            @ApiParam(value = "web url") @RequestParam String webUrl,
            @ApiParam(value = "fee") @RequestParam Long fee,
            @ApiParam(value = "zunsi type") @RequestParam List<String> zunsiType,
            @ApiParam(value = "hashtags") @RequestParam(required = false) List<String> hashtags,
            @ApiParam(value = "poster image") @RequestParam(value = "posterImage", required = false) MultipartFile posterImage,
            @ApiParam(value = "detail images") @RequestParam(value = "detailImages", required = false) MultipartFile[] detailImages
    ) throws IOException {
        User user = userService.getUserByJwt(jwt);
        if (user == null) throw new IllegalArgumentException("invalid jwt token");
        if (posterImage == null) throw new IllegalArgumentException("no poster image");

        ZunsiCreateReqDto dto = ZunsiCreateReqDto.builder()
                .user(user)
                .title(title)
                .description(description)
                .artist(artist)
                .startDate(Instant.ofEpochMilli(startDate).atZone(ZoneId.of("Asia/Seoul")).toLocalDate())
                .endDate(Instant.ofEpochMilli(endDate).atZone(ZoneId.of("Asia/Seoul")).toLocalDate())
                .startTime(LocalTime.of(startTime / 100, startTime % 100))
                .endTime(LocalTime.of(endTime / 100, endTime % 100))
                .address(address)
                .placeName(placeName)
                .webUrl(webUrl)
                .fee(fee)
                .zunsiTypes(zunsiType)
                .hashtags(hashtags)
                .build();
        Zunsi zunsi = zunsiService.save(dto, posterImage, detailImages);
        ZunsiResDto resDto = zunsiService.getZunsiResDto(zunsi);
        log.info("CREATE ZUNSI");
        log.info(resDto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseService.getSingleResult(resDto));
    }

    @ApiOperation(value = "전시 개별 조회")
    @GetMapping(value = "/{zunsiId}")
    public ResponseEntity<SingleResult<ZunsiResDto>> getZunsiById(
            @ApiParam(value = "zunsi id") @PathVariable Long zunsiId
    ) {
        Zunsi zunsi = zunsiService.getZunsiById(zunsiId);
        ZunsiResDto resDto = zunsiService.getZunsiResDto(zunsi);
        log.info("get zunsi: {}", zunsiId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseService.getSingleResult(resDto));
    }

    @ApiOperation(value = "전시 리스트 조회")
    @PostMapping(value = "/list")
    public ResponseEntity<PageResult<ZunsiResDto>> getZunsiList(
            @RequestHeader(name = "Authorization", required = false) String jwt,
            @ApiParam(value = "filter") @RequestParam(defaultValue = "popular") String filter,
            @ApiParam(value = "limit") @RequestParam(required = false, defaultValue = "10") Integer limit,
            @ApiParam(value = "page") @RequestParam(required = false, defaultValue = "0") Integer page,
            @ApiParam(value = "user latitude") @RequestParam(required = false, defaultValue = "37.574") Double latitude,
            @ApiParam(value = "user longitude") @RequestParam(required = false, defaultValue = "126.976") Double longitude
    ) {
        User user = userService.getUserByJwt(jwt);
        ZunsiPageDto dto = zunsiService.getZunsiList(user, filter, limit, page, new Point(latitude, longitude));
        log.info("filter: {}\tlimit: {}\tpage: {}", filter, limit, page);
        log.info("latitude:{} \tlongitude: {}", latitude, longitude);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getPageListResult(dto.getZunsiListDto(), dto.getHasNext()));
    }
}
