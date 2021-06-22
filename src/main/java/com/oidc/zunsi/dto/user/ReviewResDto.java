package com.oidc.zunsi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReviewResDto {
    private Long reviewId;
    private String title;
    private String content;
    private String posterImageUrl;
    private Long visitedDate;
}
