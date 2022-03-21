package com.roadToMaster.UniversityManagerApi.users.application;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak.KeycloakClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CreateUser implements ICreateUser {

  private final UserEntityMapper entityMapper;

  private final UserRepository userRepository;

  private final KeycloakClient keycloakClient;

  @Autowired
  public CreateUser(UserEntityMapper entityMapper, UserRepository userRepository, KeycloakClient keycloakClient) {
    this.entityMapper = entityMapper;
    this.userRepository = userRepository;
    this.keycloakClient = keycloakClient;
  }

  public User execute(User user) {

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new ResourceAlreadyCreatedException(String.format("User with username %s already exists", user.getUsername()));
    }

    keycloakClient.registerUser(user);

    //Save user in BD
    userRepository.save(entityMapper.userToEntity(user));

    //return user
    return user;
  }
}
