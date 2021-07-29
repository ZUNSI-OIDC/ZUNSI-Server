package com.oidc.zunsi.controller.v1;

import com.oidc.zunsi.domain.response.SingleResult;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.review.ReviewListRowDto;
import com.oidc.zunsi.service.ResponseService;
import com.oidc.zunsi.service.ReviewService;
import com.oidc.zunsi.service.UserService;
import com.oidc.zunsi.service.ZunsiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ResponseService responseService;

    // review create

    // review detail Individual Queries

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "jwt 토큰", required = true, dataType = "String", paramType = "header")
    })
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

    // review not yet by user (jwt)

}
