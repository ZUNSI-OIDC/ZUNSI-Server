package com.oidc.zunsi.service.naver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oidc.zunsi.dto.map.CoordinateResDto;
import com.oidc.zunsi.dto.map.MapResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapService {
    @Value("${spring.naver.cloud.map.client-id}")
    private String clientId;

    @Value("${spring.naver.cloud.map.client-secret}")
    private String clientSecret;

    @Value("${spring.naver.cloud.map.endpoint}")
    private String endpoint;

    private final RestTemplate restTemplate;

    public CoordinateResDto getCoordinate(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        HttpEntity<MapResDto> entity = new HttpEntity<>(headers);
        String url = endpoint + "?query="+address;

        ResponseEntity res = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(res.getBody().toString());
        log.info(jsonObject.toString());

        JsonArray addresses = (JsonArray) jsonParser.parse(jsonObject.get("addresses").toString());
        JsonObject addressData;
        JsonObject sidoData;
        try {
            addressData = (JsonObject) addresses.get(0);
            JsonArray addressMetadata =(JsonArray) jsonParser.parse(addressData.get("addressElements").toString());
            sidoData = (JsonObject) addressMetadata.get(0);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new IllegalArgumentException("????????? ???????????? ????????? ?????? ???????????????.");
        }

//        MapResDto.builder()
//                .status(jsonObject.get("status").toString().replaceAll("\"", ""))
//                .address(MapResDto.Address.builder()
//                        .roadAddress(addressData.get("roadAddress").toString().replaceAll("\"", ""))
//                        .jibunAddress(addressData.get("jibunAddress").toString().replaceAll("\"", ""))
//                        .englishAddress(addressData.get("englishAddress").toString().replaceAll("\"", ""))
//                        .x(addressData.get("x").toString().replaceAll("\"", ""))
//                        .y(addressData.get("y").toString().replaceAll("\"", ""))
//                        .build())
//                .errorMessage(jsonObject.get("errorMessage").toString().replaceAll("\"", ""))
//                .build();
        return CoordinateResDto.builder()
                .sido(sidoData.get("shortName").toString().replaceAll("\"", ""))
                .address(addressData.get("roadAddress").toString().replaceAll("\"", ""))
                .point(new Point(
                        Double.parseDouble(addressData.get("x").toString().replaceAll("\"", "")),
                        Double.parseDouble(addressData.get("y").toString().replaceAll("\"", ""))))
                .build();
    }
}
