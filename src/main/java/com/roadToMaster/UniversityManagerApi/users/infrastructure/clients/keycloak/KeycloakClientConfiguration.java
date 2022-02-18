package com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfiguration {

  @Value("${keycloak.clientId}")
  private String clientID;

  @Value("${keycloak.clientSecret}")
  private String clientSecret;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.serverUrl}")
  private String serverUrl;


  @Bean
  public Keycloak keycloak(){
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(clientID)
        .clientSecret(clientSecret)
        .build();
  }
}
