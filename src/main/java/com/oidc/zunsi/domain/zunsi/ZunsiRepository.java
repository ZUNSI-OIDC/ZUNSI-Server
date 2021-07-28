package com.oidc.zunsi.domain.zunsi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZunsiRepository extends JpaRepository<Zunsi, Long> {
    // 반경 내 전시 목록 (거리순)
    Optional<Page<Zunsi>> findAllByLatitudeBetweenAndLongitudeBetweenOrderByEndDate(Double latBottom, Double latTop, Double longBottom, Double longTop, Pageable pageRequest);

    // 인기순
    Optional<Page<Zunsi>> findAllByOrderByZzimCountDescEndDateDesc(Pageable pageRequest);
}