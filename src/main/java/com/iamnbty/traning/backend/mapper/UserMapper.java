package com.iamnbty.traning.backend.mapper;

import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.model.MRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    MRegisterResponse  toRegisterResponse(User user);
}
