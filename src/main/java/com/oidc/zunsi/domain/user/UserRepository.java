package com.oidc.zunsi.domain.user;

import com.oidc.zunsi.domain.enums.SnsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySnsIdAndProvider(String snsId, SnsType snsType);
}
