package com.roadToMaster.UniversityManagerApi.users.infrastructure.clients;

import com.roadToMaster.UniversityManagerApi.users.domain.User;

public interface IUserProviderClient {

  String registerUser(User user);
}
