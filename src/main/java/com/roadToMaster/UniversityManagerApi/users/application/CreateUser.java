package com.roadToMaster.UniversityManagerApi.users.application;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.IUserProviderClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser {

  private final UserEntityMapper entityMapper;

  private final UserRepository userRepository;

  private final IUserProviderClient userProviderClient;

  @Autowired
  public CreateUser(UserEntityMapper entityMapper, UserRepository userRepository,
                    @Qualifier("cognito") IUserProviderClient userProviderClient) {
    this.entityMapper = entityMapper;
    this.userRepository = userRepository;
    this.userProviderClient = userProviderClient;
  }

  public User execute(User user) {

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new ResourceAlreadyCreatedException(String.format("User with username %s already exists", user.getUsername()));
    }

    userProviderClient.registerUser(user);

    //Save user in BD
    userRepository.save(entityMapper.userToEntity(user));

    //return user
    return user;
  }
}
