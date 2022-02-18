package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;

import java.util.UUID;

public class UserMother {

  public static User buildValid(){
    return new User(
        UUID.randomUUID().toString(),
        "test username",
        "test first name",
        "test last name",
        "test@gmail.com",
        RoleEnum.DIRECTOR,
        Boolean.TRUE
    );
  }
}
