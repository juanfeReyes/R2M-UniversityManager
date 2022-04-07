package com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserApiMapper {

  User userRequestToDomain(UserRequest userRequest);
}
