package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.visit.Visit;
import com.oidc.zunsi.domain.visit.VisitRepository;
import com.oidc.zunsi.dto.user.VisitCountDto;
import com.oidc.zunsi.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class VisitService {
    private final VisitRepository visitRepository;

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
        if(visitList.isEmpty() || visitList.get().size() == 0) {
            isListExist = false;
        }

        ZunsiType[] zunsiTypes = ZunsiType.values();
        Map<String, Double> favoriteData = new HashMap<>();
        for (ZunsiType zunsi : zunsiTypes) {
            if(!isListExist) {
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
        for(Visit visit: visits) {
            if(visit.getZunsi().getZunsiTypes().contains(zunsiType))
                cnt += 1;
        }
        return cnt;
    }
}
