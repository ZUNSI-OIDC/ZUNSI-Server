package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.user.User;
import com.oidc.zunsi.domain.zunsi.Zunsi;
import com.oidc.zunsi.domain.zzim.Zzim;
import com.oidc.zunsi.domain.zzim.ZzimRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ZzimService {
    private final ZzimRepository zzimRepository;
    private final VisitService visitService;

    @Transactional
    public Zzim createZzim(User user, Zunsi zunsi) {
        if(isZzimed(user, zunsi) && visitService.isExist(user, zunsi))
            throw new IllegalArgumentException("user already visit zunsi (id: " + zunsi.getId() + ")");

        Zzim zzim = Zzim.builder()
                .user(user)
                .zunsi(zunsi)
                .isVisited(false)
                .build();
        zzimRepository.save(zzim);
        return zzim;
    }

    public Boolean isZzimed(User user, Zunsi zunsi) {
        return zzimRepository.findByUserAndZunsi(user, zunsi).isPresent();
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

    public void deleteZzim(User user, Zunsi zunsi) {
        Optional<Zzim> zzim = zzimRepository.findByUserAndZunsi(user, zunsi);
        if(zzim.isEmpty()) return;
        zzimRepository.delete(zzim.get());
    }
}
