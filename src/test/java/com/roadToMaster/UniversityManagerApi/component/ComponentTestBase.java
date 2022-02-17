package com.roadToMaster.UniversityManagerApi.component;

import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak.KeycloakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ComponentTestBase {

  @Autowired
  public TestRestTemplate restTemplate;

  @MockBean
  public KeycloakClient keycloakClient;

}
