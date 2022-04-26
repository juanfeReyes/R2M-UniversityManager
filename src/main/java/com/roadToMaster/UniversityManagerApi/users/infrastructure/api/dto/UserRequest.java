package com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserRequest {
  @NotEmpty
  private String username;

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  @Email
  private String email;

  @Pattern(regexp = "ADMIN|DIRECTOR|PROFESSOR")
  private String role;

  private boolean active;

}
