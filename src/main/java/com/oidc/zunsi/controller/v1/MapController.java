package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.ListResult;
import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.dto.map.CoordinateResDto;
import com.oidc.zunsi.dto.zunsi.ZunsiListRowDto;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.UserService;
import com.oidc.zunsi.service.ZunsiService;
import com.oidc.zunsi.service.naver.MapService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = {"Map"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/map")
public class MapController {
    private final ResponseService responseService;
    private final MapService mapService;
    private final ZunsiService zunsiService;
    private final UserService userService;

    @ApiOperation(value = "주소로 좌표 얻기")
    @GetMapping("/coordinate/{address}")
    public ResponseEntity<SingleResult<CoordinateResDto>> getCoordinate(
            @PathVariable String address
    ) {
        CoordinateResDto resDto = mapService.getCoordinate(address);
        log.info(resDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(resDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "GPS 확인 요청", notes = "근처에 찜한 전시회 존재 여부 확인 요청")
    @GetMapping("/nearby")
    public ResponseEntity<ListResult<ZunsiListRowDto>> getNearbyZunsi(
            @RequestHeader(name = "Authorization") String jwt,
            @ApiParam(value = "latitude") @RequestParam Double latitude,
            @ApiParam(value = "longitude") @RequestParam Double longitude
    ) {
        User user = userService.getUserByJwt(jwt);
        List<ZunsiListRowDto> zunsiList = zunsiService.getNearbyZunsi(user, new Point(latitude, longitude)).stream()
                .map(x -> zunsiService.getZunsiListRowDto(null, x))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getListResult(zunsiList));
    }
}
