package com.iamnbty.traning.backend.repository;

import com.iamnbty.traning.backend.entity.Address;
import com.iamnbty.traning.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, String> {

    List<Address> findByUser(User user);
}
