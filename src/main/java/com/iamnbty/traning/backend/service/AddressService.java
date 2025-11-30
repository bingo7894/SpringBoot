package com.iamnbty.traning.backend.service;

import com.iamnbty.traning.backend.entity.Address;
import com.iamnbty.traning.backend.entity.Social;
import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.repository.AddressRepository;
import com.iamnbty.traning.backend.repository.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Address create(User user, String line1, String line2, String zipcode) throws BaseException {
        // Todo: validate

        // create
        Address entity = new Address();

        entity.setUser(user);
        entity.setLine1(line1);
        entity.setLine2(line2);
        entity.setZipcode(zipcode);

        return repository.save(entity);
    }
}