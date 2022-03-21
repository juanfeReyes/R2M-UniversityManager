package com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String role;
  private boolean active;

}
