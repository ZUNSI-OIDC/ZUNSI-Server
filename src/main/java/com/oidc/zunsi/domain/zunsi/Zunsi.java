package com.oidc.zunsi.domain.zunsi;

import com.oidc.zunsi.domain.common.BaseTime;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.hashtag.Hashtag;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zzim.Zzim;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "zunsi")
public class Zunsi extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String description;
    private String artist;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String placeName;
    private String address;
    private Double longitude;
    private Double latitude;
    private String webUrl;
    private Long fee;

    @Setter
    private String posterImageUrl;

    @Setter
    @ElementCollection
    private List<String> detailImageUrls;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<ZunsiType> zunsiTypes = new HashSet<>();

    @Setter
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "zunsi_hashtag",
            joinColumns = @JoinColumn(name = "zunsi_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    @OneToMany(mappedBy = "zunsi", cascade = CascadeType.REMOVE)
    private Set<Zzim> zzims = new HashSet<>();

    @Setter
    private Long zzimCount;
}
