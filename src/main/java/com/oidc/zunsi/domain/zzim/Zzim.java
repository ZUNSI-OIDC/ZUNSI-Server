package com.oidc.zunsi.domain.zzim;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oidc.zunsi.domain.common.BaseTime;
import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "zzim")
public class Zzim extends BaseTime {
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

    @Setter
    LocalDateTime visited;

    @Setter
    boolean isReviewed;
}
