package com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  UserEntity userToEntity(User user);

  User userToDomain(UserEntity userEntity);
}
