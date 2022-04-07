package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;

public class UserMother {

  public static User buildValid() {
    var faker = FakerUtil.buildFaker();
    return new User(
        faker.internet().uuid(),
        faker.name().username(),
        faker.name().firstName(),
        faker.name().lastName(),
        faker.internet().emailAddress(),
        RoleEnum.DIRECTOR,
        Boolean.TRUE
    );
  }
}
