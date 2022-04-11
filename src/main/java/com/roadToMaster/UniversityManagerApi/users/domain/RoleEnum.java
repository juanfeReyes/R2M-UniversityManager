package com.roadToMaster.UniversityManagerApi.users.domain;

public enum RoleEnum {
  ADMIN("ADMIN"),
  DIRECTOR("DIRECTOR"),
  PROFESSOR("PROFESSOR");

  public final String value;

  RoleEnum(String value) {
    this.value = value;
  }
}
