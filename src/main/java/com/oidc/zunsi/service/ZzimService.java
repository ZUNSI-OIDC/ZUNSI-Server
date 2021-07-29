package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.domain.zzim.Zzim;
import com.oidc.zunsi.domain.zzim.ZzimRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ZzimService {
    private final ZzimRepository zzimRepository;

    public Boolean isZzimed(User user, Zunsi zunsi) {
        List<Zzim> zzims = zzimRepository.findAllByUserAndZunsi(user, zunsi).orElse(null);
        if(zzims == null) return false;
        return zzims.size() > 0;
    }

    public Long getZzimCountByZunsi(Zunsi zunsi){
        Optional<List<Zzim>> zzims = zzimRepository.findAllByZunsi(zunsi);
        if(zzims.isEmpty())
            return 0L;
        return (long) zzims.get().size();
    }

    public Long getZzimCountByUser(User user) {
        Optional<List<Zzim>> zzims = zzimRepository.findAllByUser(user);
        if(zzims.isEmpty())
            return 0L;
        return (long) zzims.get().size();
    }

    public List<Zzim> getZzimList(User user) {
        Optional<List<Zzim>> zzims = zzimRepository.findAllByUser(user);
        if(zzims.isEmpty())
            return Collections.emptyList();
        return zzims.get();
    }
}
