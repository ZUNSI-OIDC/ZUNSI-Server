package com.oidc.zunsi.dto.zunsi;

import com.oidc.zunsi.domain.enums.ZunsiType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@ToString
public class ZunsiResDto {
    private Long userId;
    private String username;
    private Long zunsiId;
    private String title;
    private String description;
    private String artist;
    private String posterImageUrl;
    private List<String> detailImageUrls;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private Double longitude;
    private Double latitude;
    private String placeName;
    private String webUrl;
    private Long fee;
    private Set<ZunsiType> zunsiTypes;
    private List<String> hashtags;
    private Long likeNum;
}
