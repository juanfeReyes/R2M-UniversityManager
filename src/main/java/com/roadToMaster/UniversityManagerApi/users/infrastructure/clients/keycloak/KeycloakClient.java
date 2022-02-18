package com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeycloakClient {

  private final Keycloak keycloak;
  @Value("${keycloak.realm}")
  private String realm;

  @Autowired
  public KeycloakClient(Keycloak keycloak) {
    this.keycloak = keycloak;
  }

  public String registerUser(User user) {
    var usersResource = keycloak.realm(realm).users();

    var userRepresentation = buildUserRepresentation(user);
    var passwordCredential = buildPassword();

    var response = usersResource.create(userRepresentation);
    var userId = CreatedResponseUtil.getCreatedId(response);

    var createdUser = usersResource.get(userId);
    createdUser.resetPassword(passwordCredential);

    var realmRole = keycloak.realm(realm).roles().get(user.getRole().value).toRepresentation();
    createdUser.roles().realmLevel().add(List.of(realmRole));


    return userId;
  }

  private UserRepresentation buildUserRepresentation(User user) {
    var userRepresentation = new UserRepresentation();
    userRepresentation.setEnabled(user.isActive());
    userRepresentation.setUsername(user.getUsername());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName());
    userRepresentation.setEmail(user.getEmail());

    return userRepresentation;
  }

  private CredentialRepresentation buildPassword() {
    var passwordCredential = new CredentialRepresentation();
    passwordCredential.setTemporary(false);
    passwordCredential.setType(CredentialRepresentation.PASSWORD);
    passwordCredential.setValue("temporal");
    return passwordCredential;
  }
}
