package com.oidc.zunsi.domain.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.oidc.zunsi.domain.common.BaseTime;
import com.oidc.zunsi.domain.enums.Place;
import com.oidc.zunsi.domain.enums.SnsType;
import com.oidc.zunsi.domain.enums.ZunsiType;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.domain.zzim.Zzim;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseTime implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String snsId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SnsType provider;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    private String nickname;

    @Column(nullable = false)
    private Role role;

    @Setter
    private String email; // 현재 db 스키마에 없음

    @Setter
    private String bio;

    @Setter
    private String profileImageUrl;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Place> place = new HashSet<>();

    @Setter
    private Boolean notification;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<ZunsiType> favoriteZunsiType = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Zzim> zzims = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Zunsi> zunsi = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getKey()));
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return null;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return false;
    }


    @Getter
    @RequiredArgsConstructor
    public enum Role {

        ADMIN("ROLE_ADMIN", "admin"),
        USER("ROLE_USER", "user");

        private final String key;
        private final String title;
    }

}
