package com.iamnbty.traning.backend.repository;

import com.iamnbty.traning.backend.entity.Social;
import com.iamnbty.traning.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SocialRepository extends CrudRepository<Social, String> {

    Optional<Social> findByUser(User user);
}
