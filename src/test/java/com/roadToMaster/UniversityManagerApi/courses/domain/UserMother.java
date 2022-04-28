package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;

import java.util.UUID;

public class UserMother {

  public static User buildValid() {
    var faker = FakerUtil.buildFaker();
    return new User(
        UUID.randomUUID().toString(),
        faker.name().username(),
        faker.name().firstName(),
        faker.name().lastName(),
        faker.internet().emailAddress(),
        RoleEnum.PROFESSOR,
        Boolean.TRUE
    );
  }

  public static User buildValidWithRole(RoleEnum role) {
    var faker = FakerUtil.buildFaker();
    return new User(
        UUID.randomUUID().toString(),
        faker.name().firstName(),
        faker.name().firstName(),
        faker.name().lastName(),
        faker.internet().emailAddress(),
        role,
        Boolean.TRUE
    );
  }
}
