package com.roadToMaster.UniversityManagerApi.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {

  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private RoleEnum role;
  private boolean active;
}
