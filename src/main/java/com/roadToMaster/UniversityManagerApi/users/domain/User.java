package com.roadToMaster.UniversityManagerApi.users.domain;

import lombok.Getter;

@Getter
public class User {

  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String Role; //Only few options are available, how to force them?

  private boolean active;
}
