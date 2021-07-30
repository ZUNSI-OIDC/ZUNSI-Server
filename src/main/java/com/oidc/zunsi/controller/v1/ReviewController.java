package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.review.Review;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.review.CreateReviewReqDto;
import com.oidc.zunsi.dto.review.ReviewDto;
import com.oidc.zunsi.dto.review.ReviewListRowDto;
import com.oidc.zunsi.dto.zunsi.ZunsiListRowDto;
import com.oidc.zunsi.service.*;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = {"Review"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/review")
public class ReviewController {

    private final UserService userService;
    private final ZunsiService zunsiService;
    private final ReviewService reviewService;
    private final VisitService visitService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "리뷰 작성")
    @PostMapping("/new")
    public ResponseEntity<SingleResult<ReviewDto>> createReview(
            @RequestHeader("Authorization") String jwt,
            @ApiParam(value = "zunsi id") @RequestParam Long zunsiId,
            @ApiParam(value = "content") @RequestParam String content,
            @ApiParam(value = "review images") @RequestParam(value = "reviewImages", required = false) MultipartFile[] reviewImages
    ) throws IOException {
        Review review = reviewService.createReview(CreateReviewReqDto.builder()
                .user(userService.getUserByJwt(jwt))
                .zunsi(zunsiService.getZunsiById(zunsiId))
                .content(content)
                .reviewImages(reviewImages)
                .build());
        ReviewDto dto = reviewService.getReviewDto(review);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiOperation(value = "실시간 리뷰 목록")
    @GetMapping("/now")
    public ResponseEntity<SingleResult<List<ReviewListRowDto>>> getCurrentReviewList(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit
    ) {
        List<ReviewListRowDto> dto = reviewService.getCurrentReviews(page, limit).stream()
                .map(reviewService::getListRowDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiOperation(value = "개별 리뷰 조회")
    @GetMapping("")
    public ResponseEntity<SingleResult<ReviewDto>> getCurrentReviewList(
            @RequestParam(value = "id") Long reviewId
    ) {
        ReviewDto dto = reviewService.getReviewDto(reviewService.getReviewById(reviewId));
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiOperation(value = "해당 전시 리뷰 리스트")
    @GetMapping("/zunsi")
    public ResponseEntity<SingleResult<List<ReviewListRowDto>>> getReviewList(@RequestParam(value = "id") Long zunsiId) {
        Zunsi zunsi = zunsiService.getZunsiById(zunsiId);
        List<ReviewListRowDto> dto = reviewService.getReviewByZunsi(zunsi).stream()
                .map(reviewService::getListRowDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "내 리뷰 리스트")
    @GetMapping("/me")
    public ResponseEntity<SingleResult<List<ReviewListRowDto>>> getReviewList(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        if(user == null) throw new IllegalArgumentException("user not exist");
        List<ReviewListRowDto> dto = reviewService.getReviewByUser(user).stream()
                .map(reviewService::getListRowDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "방문 후 리뷰 안한 전시 리스트")
    @GetMapping("/not-yet")
    public ResponseEntity<SingleResult<List<ZunsiListRowDto>>> getNotReviewedList(@RequestHeader("Authorization") String jwt) {
        User user = userService.getUserByJwt(jwt);
        if(user == null) throw new IllegalArgumentException("user not exist");
        List<ZunsiListRowDto> dto = visitService.getVisits(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseService.getSingleResult(dto));
    }
}
