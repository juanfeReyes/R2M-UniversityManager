package com.roadToMaster.UniversityManagerApi.users.domain;

public enum RoleEnum {
  ADMIN ("ROLE_ADMIN"),
  DIRECTOR ("ROLE_DIRECTOR"),
  PROFESSOR ("ROLE_PROFESSOR");

  public final String value;

  RoleEnum(String value) {
    this.value = value;
  }
}
