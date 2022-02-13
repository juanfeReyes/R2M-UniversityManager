package com.roadToMaster.UniversityManagerApi.users.infrastructure.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: Enable when basic auth is working
//@Configuration
public class KeycloakClientConfiguration {

  private String serverUrl;
  private String realm;
  private String clientID;
  private String clientSecret;


  @Bean
  public Keycloak keycloak(){
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.GRANT_TYPE)
        .clientId(clientID)
        .clientSecret(clientSecret)
        .build();
  }
}
