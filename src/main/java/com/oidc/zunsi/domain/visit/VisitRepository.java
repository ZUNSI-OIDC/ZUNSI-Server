package com.oidc.zunsi.domain.visit;

import com.oidc.zunsi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<List<Visit>> findAllByUser(User user);
    Long countAllByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);
}
