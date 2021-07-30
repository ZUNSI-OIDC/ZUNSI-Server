package com.oidc.zunsi.dto.zunsi;

import com.oidc.zunsi.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@ToString
public class ZunsiCreateReqDto {
    private User user;
    private String title;
    private String description;
    private String artist;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private String placeName;
    private String webUrl;
    private Long fee;
    private List<String> zunsiTypes;
    private List<String> hashtags;
}
