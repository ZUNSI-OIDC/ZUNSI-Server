package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.dto.map.CoordinateResDto;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.naver.MapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"Map"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/map")
public class MapController {
    private final ResponseService responseService;
    private final MapService mapService;

    @ApiOperation(value = "주소로 좌표 얻기")
    @GetMapping("/coordinate/{address}")
    public ResponseEntity<SingleResult<CoordinateResDto>> getCoordinate(
            @PathVariable String address
            ) {
        CoordinateResDto resDto = mapService.getCoordinate(address);
        log.info(resDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(resDto));
    }
}
