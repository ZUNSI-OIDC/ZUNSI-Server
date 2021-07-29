package com.oidc.zunsi.service;

import com.oidc.zunsi.config.security.JwtTokenProvider;
import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.review.Review;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.user.UserRepository;
import com.oidc.zunsi.domain.zzim.Zzim;
import com.oidc.zunsi.dto.user.*;
import com.oidc.zunsi.service.naver.NaverObjectStorageService;
import com.oidc.zunsi.service.social.SocialServiceFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class UserService {

    private final SocialServiceFactory socialServiceFactory;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewService reviewService;
    private final ZzimService zzimService;
    private final VisitService visitService;
    private final NaverObjectStorageService naverObjectStorageService;

    public boolean isUserExist(SnsType provider, String accessToken) {
        String snsId = getSnsId(provider, accessToken);
        Optional<User> user = userRepository.findBySnsIdAndProvider(snsId, provider);
        return user.isPresent();
    }

    public User getUserByProviderAndToken(String provider, String accessToken) {
        SnsType snsType;
        try {
            snsType = SnsType.valueOf(provider);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid provider");
        }
        String snsId = getSnsId(snsType, accessToken);
        return userRepository.findBySnsIdAndProvider(snsId, snsType).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean validateUsername(String username) {
        boolean isValidCharacter = Pattern.matches("^[가-힣0-9a-zA-Z]*$", username);
        return 0 < username.length() && username.length() < 11 && isValidCharacter;
    }

    public String getSnsId(SnsType provider, String accessToken) {
        return socialServiceFactory.getService(provider).getSnsId(accessToken);
    }

    @Transactional
    public void save(User newUser, MultipartFile profileImage) throws IOException {
        User user = userRepository.save(newUser);
        if(profileImage != null) updateProfileImage(user, profileImage);
    }

    public User getUserByJwt(String jwt) {
        if (jwt == null) return null;
        String userId = jwtTokenProvider.getUserPk(jwt);
        return userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public ProfileResDto getProfileResDto(User user) {
        return ProfileResDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .place(user.getPlace())
                .zunsiTypeList(user.getFavoriteZunsiType())
                .reviewNum(reviewService.getReviewCountByUser(user))
                .zzimNum(zzimService.getZzimCountByUser(user))
                .profileImgUrl(user.getProfileImageUrl())
                .build();
    }

    public AnalyticsResDto getAnalytics(User user) {
        VisitCountDto visitCountDto = visitService.getVisitCount(user);

        return AnalyticsResDto.builder()
                .lastMonth(visitCountDto.getLastMonth())
                .currentMonth(visitCountDto.getCurrentMonth())
                .currentYear(visitCountDto.getCurrentYear())
                .favoriteData(visitService.getVisitDataByUser(user))
                .build();
    }

    public void updateUser(User user, ProfileReqDto dto) throws IOException {
        Set<Place> placeTypeSet = new HashSet<>();
        for (String p : dto.getPlace()) {
            placeTypeSet.add(Place.valueOf(p));
        }
        Set<ZunsiType> zunsiTypeSet = new HashSet<>();
        for (String zunsiType : dto.getFavoriteZunsiList()) {
            zunsiTypeSet.add(ZunsiType.valueOf(zunsiType));
        }
        String imageUrl = dto.getProfileImage() != null ?
                naverObjectStorageService.uploadProfileImage(user, dto.getProfileImage()) : null;
        user.setProfileImageUrl(imageUrl);
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setPlace(placeTypeSet);
        user.setFavoriteZunsiType(zunsiTypeSet);

        userRepository.save(user);
    }

    public void updateProfileImage(User user, MultipartFile profileImage) throws IOException {
        String imageUrl = naverObjectStorageService.uploadProfileImage(user, profileImage);
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    public List<ZzimResDto> getZzimList(User user) {
        List<ZzimResDto> zzimResDtoList = new ArrayList<>();
        List<Zzim> zzimList = zzimService.getZzimList(user);
        for (Zzim zzim : zzimList) {
            zzimResDtoList.add(ZzimResDto.builder()
                    .zunsiId(zzim.getZunsi().getId())
                    .title(zzim.getZunsi().getTitle())
                    .startDate(zzim.getZunsi().getStartDate().toEpochDay())
                    .endDate(zzim.getZunsi().getEndDate().toEpochDay())
                    .placeName(zzim.getZunsi().getPlaceName())
                    .posterImageUrl(zzim.getZunsi().getPosterImageUrl())
                    .build());
        }
        return zzimResDtoList;
    }

    public List<ReviewResDto> getReviewList(User user) {
        List<ReviewResDto> reviewResDtoList = new ArrayList<>();
        List<Review> reviewList = reviewService.getReviewByUser(user);
        for (Review review : reviewList) {
            reviewResDtoList.add(ReviewResDto.builder()
                    .reviewId(review.getId())
                    .title(review.getZunsi().getTitle())
                    .content(review.getContent())
                    .posterImageUrl(review.getZunsi().getPosterImageUrl())
                    .visitedDate(review.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                    .build());
        }
        return reviewResDtoList;
    }

    public void changeNotificationOption(User user, Boolean isEnabled) {
        user.setNotification(isEnabled);
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
