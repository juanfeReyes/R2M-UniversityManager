package com.roadToMaster.UniversityManagerApi.users.application;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak.KeycloakClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUser implements ICreateUser{

  private final UserRepository userRepository;

  private final KeycloakClient keycloakClient;

  @Autowired
  public CreateUser(UserRepository userRepository, KeycloakClient keycloakClient) {
    this.userRepository = userRepository;
    this.keycloakClient = keycloakClient;
  }

  //TODO: How to handle the id generation and User domain instantiation
  //What should i pass as parameter
  public User execute(User user) {
    //Verify user does not exists
    if(userRepository.existsById(user.getId())){
      throw new ResourceAlreadyCreatedException(String.format("User with id %s already exists", user.getId()));
    }

    //Create user in keycloak
    var userId = keycloakClient.registerUser(user);

    //Save user in BD
    userRepository.save(UserEntity.toEntity(user));

    //return user
    return user;
  }
}
