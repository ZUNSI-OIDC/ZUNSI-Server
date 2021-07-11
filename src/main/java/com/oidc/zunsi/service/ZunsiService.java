package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.hashtag.Hashtag;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.domain.zunsi.ZunsiRepository;
import com.oidc.zunsi.dto.map.CoordinateResDto;
import com.oidc.zunsi.dto.zunsi.ZunsiCreateReqDto;
import com.oidc.zunsi.dto.zunsi.ZunsiResDto;
import com.oidc.zunsi.service.naver.MapService;
import com.oidc.zunsi.service.naver.NaverObjectStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class ZunsiService {

    private final ZunsiRepository zunsiRepository;
    private final ZzimService zzimService;
    private final NaverObjectStorageService naverObjectStorageService;
    private final HashtagService hashtagService;
    private final MapService mapService;

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

        Zunsi zunsi = Zunsi.builder()
                .title(dto.getTitle())
                .user(dto.getUser())
                .description(dto.getDescription())
                .artist(dto.getArtist())
                .address(dto.getAddress())
                .placeName(dto.getPlaceName())
                .longitude(coordinate.getX())
                .latitude(coordinate.getY())
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
            hashtagSet.add(h == null ? hashtagService.createHashtag(ht) :h);
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

    public ZunsiResDto getZunsiResDto(Zunsi zunsi) {
        User author = zunsi.getUser();

        List<String> hashtags = new ArrayList<>();
        Set<Hashtag> hashtagSet = zunsi.getHashtags();
        log.info(String.valueOf(hashtagSet.size()));
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
                .startDate(zunsi.getStartDate())
                .endDate(zunsi.getEndDate())
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
                .build();
    }
}
