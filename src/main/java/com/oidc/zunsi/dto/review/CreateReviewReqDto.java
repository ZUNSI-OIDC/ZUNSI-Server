package com.oidc.zunsi.dto.review;

import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@ToString
public class CreateReviewReqDto {
    private Zunsi zunsi;
    private User user;
    private String content;
    private MultipartFile[] reviewImages;
}
