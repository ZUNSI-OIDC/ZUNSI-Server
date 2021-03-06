package com.oidc.zunsi.domain.review;

import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Page<Review>> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Optional<List<Review>> findAllByUser(User user);
    Optional<List<Review>> findAllByZunsiOrderByCreatedAt(Zunsi zunsi);
}
