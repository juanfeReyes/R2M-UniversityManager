package com.roadToMaster.UniversityManagerApi.users.application;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetUsers implements IGetUsers {

  private final UserRepository userRepository;

  private final UserEntityMapper userEntityMapper;

  @Autowired
  public GetUsers(UserRepository userRepository, UserEntityMapper userEntityMapper) {
    this.userRepository = userRepository;
    this.userEntityMapper = userEntityMapper;
  }

  @Override
  public Page<User> execute(Pageable pageable) {

    var usersPage = userRepository.findAll(pageable);

    return usersPage.map(userEntityMapper::userToDomain);
  }
}
