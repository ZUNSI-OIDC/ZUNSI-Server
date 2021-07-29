package com.oidc.zunsi.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Builder
public class ReviewListRowDto {
    private Long id;
    private String username;
    private String content;
    private String posterImgUrl;
    private String thumbnailUrl;
    private LocalDate visitDate;
}
