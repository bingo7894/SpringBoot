package com.iamnbty.traning.backend.service;

import com.iamnbty.traning.backend.entity.Social;
import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException; // Import Exception
import com.iamnbty.traning.backend.repository.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialService {
    private final SocialRepository repository;

    public SocialService(SocialRepository repository) {
        this.repository = repository;
    }

    public Optional<Social> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Social create(User user, String facebook, String line, String instagram, String tiktok) throws BaseException {
        // Todo: validate

        // create
        Social entity = new Social();

        entity.setUser(user);
        entity.setFacebook(facebook);
        entity.setLine(line);
        entity.setInstagram(instagram);
        entity.setTiktok(tiktok);

        return repository.save(entity);
    }
}