package com.roadToMaster.UniversityManagerApi.shared.infrastructure;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: Enable when security configuration is working
@Configuration
public class OpenApiConfiguration {

  @Value("${springdoc.oAuthFlow.authorizationUrl}")
  private String authorizationUrl;

  @Value("${springdoc.oAuthFlow.tokenUrl}")
  private String tokenUrl;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes("OAuthScheme", new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description("OAuth2 authentication")
                    .flows(new OAuthFlows()
                        .authorizationCode(new OAuthFlow()
                            .authorizationUrl(authorizationUrl)
                            .tokenUrl(tokenUrl)
                            .scopes(new Scopes()
                                .addString("read", "read")
                                .addString("write", "read")
                            )
                        )))
        )
        .addSecurityItem(new SecurityRequirement().addList("OAuthScheme"));
  }
}
