package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.review.Review;
import com.oidc.zunsi.domain.review.ReviewRepository;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.review.ReviewListRowDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

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

    public List<Review> getReviewByZunsi(Zunsi zunsi) {
        Optional<List<Review>> reviews = reviewRepository.findAllByZunsiOrderByCreatedAt(zunsi);
        if (reviews.isEmpty())
            return Collections.emptyList();
        return reviews.get();
    }
}
