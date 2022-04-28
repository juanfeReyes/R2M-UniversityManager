package com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserRequest {
  @NotEmpty(message = "username cannot be empty")
  private String username;

  @NotEmpty(message = "firstname cannot be empty")
  private String firstName;

  @NotEmpty(message = "lastname cannot be empty")
  private String lastName;

  @Email
  private String email;

  @Pattern(regexp = "ADMIN|DIRECTOR|PROFESSOR|STUDENT", message = "Invalid role")
  private String role;

  private boolean active;

}
