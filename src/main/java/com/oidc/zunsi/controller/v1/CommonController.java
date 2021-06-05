package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.response.ListResult;
import com.oidc.zunsi.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Api(tags = {"Common"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/common")
public class CommonController {
    private final ResponseService responseService;

    @ApiOperation(value = "전시 타입 리스트 조회")
    @GetMapping("/zunsi")
    public ResponseEntity<ListResult<HashMap<String, String>>> getZunsiTypes() {
        ZunsiType[] zunsiTypes = ZunsiType.values();
        List<HashMap<String, String>> zunsiTypeList = new ArrayList<>();
        for (ZunsiType zunsi : zunsiTypes) {
            HashMap<String, String> zunsiMap = new HashMap<>();
            zunsiMap.put("id", zunsi.toString());
            zunsiMap.put("valueKr", zunsi.getKo());
            zunsiTypeList.add(zunsiMap);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getListResult(zunsiTypeList));
    }

    @ApiOperation(value = "장소 리스트 조회")
    @GetMapping("/place")
    public ResponseEntity<ListResult<HashMap<String, String>>> getPlaces() {
        Place[] places = Place.values();
        List<HashMap<String, String>> placeMapList = new ArrayList<>();
        for (Place place : places) {
            HashMap<String, String> placeMap = new HashMap<>();
            placeMap.put("id", place.toString());
            placeMap.put("valueKr", place.getKo());
            placeMapList.add(placeMap);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getListResult(placeMapList));
    }
}
