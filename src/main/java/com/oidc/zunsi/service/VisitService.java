package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.visit.Visit;
import com.oidc.zunsi.domain.visit.VisitRepository;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.dto.user.VisitCountDto;
import com.oidc.zunsi.dto.zunsi.ZunsiListRowDto;
import com.oidc.zunsi.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final ZunsiService zunsiService;

    @Transactional
    public void createVisit(User user, Zunsi zunsi) {
        if (isExist(user, zunsi))
            throw new IllegalArgumentException("user already visit zunsi (id: " + zunsi.getId() + ")");
        Visit visit = Visit.builder()
                .user(user)
                .zunsi(zunsi)
                .isReviewed(false)
                .build();
        visitRepository.save(visit);
    }

    public VisitCountDto getVisitCount(User user) {
        LocalDateTime lastMonthFirstDate = DateUtil.getLastMonthFirstDate();
        LocalDateTime lastMonthLastDate = DateUtil.getLastMonthLastDate();
        LocalDateTime currentMonthFirstDate = DateUtil.getCurrentMonthFirstDate();
        LocalDateTime currentMonthEndDate = DateUtil.getCurrentMonthLastDate();
        LocalDateTime firstDateOfYear = DateUtil.getCurrentYearFirstDate();

        Long lastMonthCnt = visitRepository.countAllByUserAndCreatedAtBetween(user, lastMonthFirstDate, lastMonthLastDate);
        Long currentMonthCnt = visitRepository.countAllByUserAndCreatedAtBetween(user, currentMonthFirstDate, currentMonthEndDate);
        Long currentYearCnt = visitRepository.countAllByUserAndCreatedAtBetween(user, firstDateOfYear, currentMonthEndDate);

        return VisitCountDto.builder()
                .lastMonth(lastMonthCnt)
                .currentMonth(currentMonthCnt)
                .currentYear(currentYearCnt)
                .build();
    }

    public Map<String, Double> getVisitDataByUser(User user) {
        Optional<List<Visit>> visitList = visitRepository.findAllByUser(user);
        boolean isListExist = true;
        if (visitList.isEmpty() || visitList.get().size() == 0) {
            isListExist = false;
        }

        ZunsiType[] zunsiTypes = ZunsiType.values();
        Map<String, Double> favoriteData = new HashMap<>();
        for (ZunsiType zunsi : zunsiTypes) {
            if (!isListExist) {
                favoriteData.put(zunsi.toString(), 0.);
            } else {
                Double ratio = (countVisitByZunsiType(visitList.get(), zunsi) / (double) visitList.get().size()) * 100;
                favoriteData.put(zunsi.toString(), ratio);
            }
        }
        return favoriteData;
    }

    private Long countVisitByZunsiType(List<Visit> visits, ZunsiType zunsiType) {
        long cnt = 0;
        for (Visit visit : visits) {
            if (visit.getZunsi().getZunsiTypes().contains(zunsiType))
                cnt += 1;
        }
        return cnt;
    }

    public List<ZunsiListRowDto> getVisits(User user) {
        List<Visit> visits = visitRepository.findAllByUser(user).orElse(null);
        if (visits == null) return Collections.emptyList();
        return visits.stream()
                .filter(x -> !x.getIsReviewed()).map(Visit::getZunsi)
                .map(x -> zunsiService.getZunsiListRowDto(user, x))
                .collect(Collectors.toList());
    }

    public Boolean isExist(User user, Zunsi zunsi) {
        return visitRepository.findByUserAndZunsi(user, zunsi).isPresent();
    }
}
