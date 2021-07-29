package com.oidc.zunsi.domain.hashtag;

import com.oidc.zunsi.domain.common.BaseTime;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hashtag")
public class Hashtag extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "hashtags", cascade = CascadeType.ALL)
    private Set<Zunsi> zunsis = new HashSet<>();

    private String content;
}
