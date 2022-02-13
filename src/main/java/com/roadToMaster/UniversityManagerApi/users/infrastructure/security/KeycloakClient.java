package com.roadToMaster.UniversityManagerApi.users.infrastructure.security;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO: enable when basic auth is working
//@Component
public class KeycloakClient {

  private String realm;

  private final Keycloak keycloak;

  @Autowired
  public KeycloakClient(Keycloak keycloak){
    this.keycloak = keycloak;
  }

  //TODO: update return type
  public void registerUser(User user){
    var usersResource = keycloak.realm(realm).users();

    var userRepresentation = buildUserRepresentation(user);
    var passwordCredential = buildPassword();

    var response = usersResource.create(userRepresentation);
    var userId = CreatedResponseUtil.getCreatedId(response);

    var createdUser = usersResource.get(userId);
    createdUser.resetPassword(passwordCredential);

    var realmRole = keycloak.realm(realm).roles().get("").toRepresentation();
    createdUser.roles().realmLevel().add(List.of(realmRole));
    //Test the email functionality :D
    //createdUser.sendVerifyEmail();
  }

  private UserRepresentation buildUserRepresentation(User user){
    var userRepresentation = new UserRepresentation();
    userRepresentation.setEnabled(user.isActive());
    userRepresentation.setUsername(user.getUsername());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName());
    userRepresentation.setEmail(user.getEmail());

    return userRepresentation;
  }

  private CredentialRepresentation buildPassword(){
    var passwordCredential = new CredentialRepresentation();
    passwordCredential.setTemporary(false);
    passwordCredential.setType(CredentialRepresentation.PASSWORD);
    passwordCredential.setValue("temporal");
    return passwordCredential;
  }
}
