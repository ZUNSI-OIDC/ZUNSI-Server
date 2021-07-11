package com.oidc.zunsi.service;

import com.oidc.zunsi.domain.hashtag.Hashtag;
import com.oidc.zunsi.domain.hashtag.HashtagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public Hashtag getHashtagByString(String keyword) {
        Optional<Hashtag> hashtag = hashtagRepository.findByContent(keyword);
        if (hashtag.isEmpty()) return null;
        return hashtag.get();
    }

    public Hashtag createHashtag(String keyword) {
        Hashtag hashtag = Hashtag.builder().content(keyword).build();
        hashtagRepository.save(hashtag);
        return hashtag;
    }
}
