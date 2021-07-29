package com.oidc.zunsi.domain.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oidc.zunsi.domain.common.BaseTime;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review")
public class Review extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zunsi_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Zunsi zunsi;

    private String content;

    @OneToMany(mappedBy = "review")
    private Set<Like> likes = new HashSet<>();

    @Setter
    @ElementCollection
    private Set<String> reviewDetailImageUrls = new HashSet<>();
}
