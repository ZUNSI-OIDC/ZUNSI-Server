package com.oidc.zunsi.domain.zzim;

import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ZzimRepository extends JpaRepository<Zzim, Long> {
    Optional<List<Zzim>> findAllByUser(User user);
    Optional<List<Zzim>> findAllByZunsi(Zunsi zunsi);
}
