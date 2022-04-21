package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto.UserRequest;

public class UserRequestMother {

  public static UserRequest buildRandom() {
    var request = new UserRequest();
    request.setUsername("auto-user");
    request.setEmail("auto@email.com");
    request.setActive(Boolean.TRUE);
    request.setFirstName("auto");
    request.setLastName("mation");
    request.setRole(RoleEnum.DIRECTOR.value);
    return request;
  }
}
