package com.oidc.zunsi.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@ToString
public class ReviewDto {
    private Long id;
    private String username;
    private String content;
    private List<String> detailImageUrls;
    private LocalDate createdAt;
}
