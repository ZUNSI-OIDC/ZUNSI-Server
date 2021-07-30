package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.hashtag.Hashtag;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.domain.zunsi.ZunsiRepository;
import com.oidc.zunsi.domain.zzim.Zzim;
import com.oidc.zunsi.dto.map.CoordinateResDto;
import com.oidc.zunsi.dto.map.RectBoxDto;
import com.oidc.zunsi.dto.zunsi.ZunsiCreateReqDto;
import com.oidc.zunsi.dto.zunsi.ZunsiListRowDto;
import com.oidc.zunsi.dto.zunsi.ZunsiPageDto;
import com.oidc.zunsi.dto.zunsi.ZunsiResDto;
import com.oidc.zunsi.service.naver.MapService;
import com.oidc.zunsi.service.naver.NaverObjectStorageService;
import com.oidc.zunsi.util.GeoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ZunsiService {

    private final ZunsiRepository zunsiRepository;
    private final ZzimService zzimService;
    private final NaverObjectStorageService naverObjectStorageService;
    private final HashtagService hashtagService;
    private final MapService mapService;
    private final ReviewService reviewService;

    public boolean checkType(String t) {
        for (ZunsiType zt : ZunsiType.values())
            if (zt.name().equals(t)) return true;
        return false;
    }

    @Transactional
    public Zunsi save(ZunsiCreateReqDto dto, MultipartFile posterImage, MultipartFile[] detailImages) throws IOException {
        Set<ZunsiType> zunsiTypes = new HashSet<>();
        for (String zt : dto.getZunsiTypes()) {
            if (!checkType(zt)) throw new IllegalArgumentException("zunsi type error");
            zunsiTypes.add(ZunsiType.valueOf(zt));
        }

        CoordinateResDto coordinate = mapService.getCoordinate(dto.getAddress());
        Place place = Arrays.stream(Place.values())
                .filter(x -> x.getKey().equals(coordinate.getSido()))
                .findFirst()
                .orElse(Place.etc);

        Zunsi zunsi = Zunsi.builder()
                .title(dto.getTitle())
                .user(dto.getUser())
                .description(dto.getDescription())
                .artist(dto.getArtist())
                .place(place.getKey())
                .placeName(dto.getPlaceName())
                .address(coordinate.getAddress())
                .longitude(coordinate.getPoint().getX())
                .latitude(coordinate.getPoint().getY())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .fee(dto.getFee())
                .webUrl(dto.getWebUrl())
                .zunsiTypes(zunsiTypes)
                .build();
        zunsiRepository.save(zunsi);

        String posterImgUrl = naverObjectStorageService.uploadPosterImage(zunsi, posterImage);
        zunsi.setPosterImageUrl(posterImgUrl);

        if (!detailImages[0].isEmpty()) {
            List<String> detailImgUrls = naverObjectStorageService.uploadDetailImages(zunsi, detailImages);
            zunsi.setDetailImageUrls(detailImgUrls);
        }

        Set<Hashtag> hashtagSet = new HashSet<>();
        for (String ht : dto.getHashtags()) {
            Hashtag h = hashtagService.getHashtagByString(ht);
            hashtagSet.add(h == null ? hashtagService.createHashtag(ht) : h);
        }
        zunsi.setHashtags(hashtagSet);
        zunsiRepository.save(zunsi);
        return zunsi;
    }

    public Zunsi getZunsiById(Long id) {
        Optional<Zunsi> zunsi = zunsiRepository.findById(id);
        if (zunsi.isEmpty()) throw new IllegalArgumentException("invalid zunsi id");
        return zunsi.get();
    }

    public ZunsiListRowDto getZunsiListRowDto(User user, Zunsi zunsi) {
        return ZunsiListRowDto.builder()
                .thumbnailUrl(zunsi.getPosterImageUrl())
                .title(zunsi.getTitle())
                .placeName(zunsi.getPlaceName())
                .startDate(Timestamp.valueOf(zunsi.getStartDate().atStartOfDay()).getTime())
                .endDate(Timestamp.valueOf(zunsi.getEndDate().atStartOfDay()).getTime())
                .isZzimed(user != null ? zzimService.isZzimed(user, zunsi) : false)
                .build();
    }

    public ZunsiResDto getZunsiResDto(Zunsi zunsi) {
        return getZunsiResDto(null, zunsi);
    }

    public ZunsiResDto getZunsiResDto(User user, Zunsi zunsi) {
        User author = zunsi.getUser();

        List<String> hashtags = new ArrayList<>();
        Set<Hashtag> hashtagSet = zunsi.getHashtags();
        for (Hashtag h : hashtagSet) {
            hashtags.add(h.getContent());
        }

        return ZunsiResDto.builder()
                .userId(author.getId())
                .username(author.getUsername())
                .zunsiId(zunsi.getId())
                .title(zunsi.getTitle())
                .description(zunsi.getDescription())
                .artist(zunsi.getArtist())
                .posterImageUrl(zunsi.getPosterImageUrl())
                .detailImageUrls(zunsi.getDetailImageUrls())
                .startDate(Timestamp.valueOf(zunsi.getStartDate().atStartOfDay()).getTime())
                .endDate(Timestamp.valueOf(zunsi.getEndDate().atStartOfDay()).getTime())
                .startTime(zunsi.getStartTime())
                .endTime(zunsi.getEndTime())
                .address(zunsi.getAddress())
                .longitude(zunsi.getLongitude())
                .latitude(zunsi.getLatitude())
                .placeName(zunsi.getPlaceName())
                .webUrl(zunsi.getWebUrl())
                .fee(zunsi.getFee())
                .zunsiTypes(zunsi.getZunsiTypes())
                .hashtags(hashtags)
                .likeNum(zzimService.getZzimCountByZunsi(zunsi))
                .isZzimed(user != null ? zzimService.isZzimed(user, zunsi) : false)
                .reviews(reviewService.getReviewByZunsi(zunsi).stream()
                        .map(reviewService::getListRowDto)
                        .limit(3)
                        .collect(Collectors.toList()))
                .build();
    }

    public ZunsiPageDto getZunsiList(User user, String filter, Integer limit, Integer page, Point point) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        List<ZunsiListRowDto> res = null;
        Page<Zunsi> pageZunsi = null;
        switch (filter) {
            case "zzim":
                res = zzimService.getZzimList(user)
                        .stream().map(Zzim::getZunsi).collect(Collectors.toList())
                        .stream().map(x -> getZunsiListRowDto(user, x)).collect(Collectors.toList());
                break;
            case "recommend":
                String[] placeList = user.getPlace().stream().map(Place::getKey).collect(Collectors.toList()).toArray(String[]::new);
                List<Zunsi> zunsiList = zunsiRepository.findAllByPlaceOrderByEndDateDesc(placeList).orElse(null);
                if (zunsiList == null) break;
                zunsiList = zunsiList.stream()
                        .filter(x -> {
                            HashSet<ZunsiType> types = new HashSet<>(x.getZunsiTypes());
                            types.retainAll(user.getFavoriteZunsiType());
                            return types.size() > 0;
                        }).collect(Collectors.toList());
                int start = (int) pageRequest.getOffset();
                int end = Math.min((start + pageRequest.getPageSize()), zunsiList.size());
                pageZunsi = new PageImpl<>(zunsiList.subList(start, end), pageRequest, zunsiList.size());
                break;
            case "distance":
                RectBoxDto box = GeoUtil.getRectBox(point);
                pageZunsi = zunsiRepository.findAllByLatitudeBetweenAndLongitudeBetweenOrderByEndDate(
                        box.getUpperLeft().getX(),
                        box.getLowerRight().getX(),
                        box.getLowerRight().getY(),
                        box.getUpperLeft().getY(),
                        pageRequest
                ).orElse(null);
                break;
            case "popular":
                pageZunsi = zunsiRepository.findAllByOrderByZzimCountDescEndDateDesc(pageRequest).orElse(null);
                break;
        }
        if (pageZunsi != null)
            res = pageZunsi.getContent().stream().map(x -> getZunsiListRowDto(user, x)).collect(Collectors.toList());

        return ZunsiPageDto.builder()
                .zunsiListDto(res != null ? res : Collections.emptyList())
                .hasNext(pageZunsi != null && pageZunsi.hasNext())
                .build();
    }

    public List<Zunsi> getNearbyZunsi(User user, Point point) {
        RectBoxDto box = GeoUtil.getRectBox(point);
        Optional<List<Zunsi>> zunsi = zunsiRepository.findAllByLatitudeBetweenAndLongitudeBetweenOrderByEndDate(
                box.getUpperLeft().getX(),
                box.getLowerRight().getX(),
                box.getLowerRight().getY(),
                box.getUpperLeft().getY()
        );

        if(zunsi.isEmpty()) return Collections.emptyList();

        return zunsi.get().stream().filter(x -> {
            Zzim zzim = zzimService.getZzim(user, x);
            if(zzim == null) return false;
            else return !zzim.getIsVisited();
        }).collect(Collectors.toList());
    }
}
