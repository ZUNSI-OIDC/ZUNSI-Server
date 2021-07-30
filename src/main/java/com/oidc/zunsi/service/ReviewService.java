package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.review.Review;
import com.oidc.zunsi.domain.review.ReviewRepository;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.review.CreateReviewReqDto;
import com.oidc.zunsi.dto.review.ReviewDto;
import com.oidc.zunsi.dto.review.ReviewListRowDto;
import com.oidc.zunsi.service.naver.NaverObjectStorageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final NaverObjectStorageService naverObjectStorageService;

    @Transactional
    public Review createReview(CreateReviewReqDto dto) throws IOException {
        UUID uuid = UUID.randomUUID();
        List<String> urls = naverObjectStorageService.uploadReviewImages(uuid.toString(), dto.getReviewImages());
        Review review = Review.builder()
                .user(dto.getUser())
                .zunsi(dto.getZunsi())
                .content(dto.getContent())
                .reviewDetailImageUrls(urls)
                .build();
        reviewRepository.save(review);
        return review;
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("review not exist"));
    }

    public Long getReviewCountByUser(User user) {
        Optional<List<Review>> reviews = reviewRepository.findAllByUser(user);
        if (reviews.isEmpty())
            return 0L;
        return (long) reviews.get().size();
    }

    public List<Review> getReviewByUser(User user) {
        Optional<List<Review>> reviews = reviewRepository.findAllByUser(user);
        if (reviews.isEmpty())
            return Collections.emptyList();
        return reviews.get();
    }

    public List<Review> getCurrentReviews(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Optional<Page<Review>> reviews = reviewRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        if (reviews.isEmpty()) return Collections.emptyList();
        return reviews.get().getContent();
    }

    public List<Review> getReviewByZunsi(Zunsi zunsi) {
        Optional<List<Review>> reviews = reviewRepository.findAllByZunsiOrderByCreatedAt(zunsi);
        if (reviews.isEmpty())
            return Collections.emptyList();
        return reviews.get();
    }

    public ReviewDto getReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .username(review.getUser().getUsername())
                .content(review.getContent())
                .detailImageUrls(review.getReviewDetailImageUrls())
                .createdAt(Timestamp.valueOf(review.getCreatedAt()).getTime())
                .build();
    }

    public ReviewListRowDto getListRowDto(Review review) {
        return ReviewListRowDto.builder()
                .id(review.getId())
                .username(review.getUser().getUsername())
                .content(review.getContent())
                .posterImgUrl(review.getZunsi().getPosterImageUrl())
                .thumbnailUrl(new ArrayList<>(review.getReviewDetailImageUrls()).get(0))
                .visitDate(LocalDate.from(review.getCreatedAt()))
                .build();
    }
}
