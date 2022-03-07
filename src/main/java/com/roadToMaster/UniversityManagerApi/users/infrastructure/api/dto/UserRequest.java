package com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto;

import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRequest {
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String role;
  private boolean active;

  public static User toDomain(UserRequest userRequest) {
    return new User(
        UUID.randomUUID().toString(),
        userRequest.getUsername(),
        userRequest.getFirstName(),
        userRequest.getLastName(),
        userRequest.getEmail(),
        RoleEnum.valueOf(userRequest.getRole()),
        userRequest.isActive());
  }
}
