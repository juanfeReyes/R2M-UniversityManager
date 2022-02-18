package com.roadToMaster.UniversityManagerApi.users.application;

import com.roadToMaster.UniversityManagerApi.users.domain.User;

public interface ICreateUser {

  User execute(User user);
}
