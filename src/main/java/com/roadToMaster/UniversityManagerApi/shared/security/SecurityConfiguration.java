package com.roadToMaster.UniversityManagerApi.shared.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Profile("!test")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${springdoc.oAuthFlow.resourceServerIdentifier}")
  private String resourceServerIdentifier;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .exceptionHandling()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/swagger-ui.html/**", "/swagger-ui/**", "/api-docs/**")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/course").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.course_write.getItem()))
        .antMatchers(HttpMethod.PUT, "/course/**").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.course_write.getItem()))
        .antMatchers(HttpMethod.DELETE, "/course/**").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.course_write.getItem()))
        .antMatchers(HttpMethod.GET, "/course").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.course_read.getItem()))
        .antMatchers(HttpMethod.POST, "/subject").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.subject_write.getItem()))
        .antMatchers(HttpMethod.PUT, "/subject/**").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.subject_write.getItem()))
        .antMatchers(HttpMethod.DELETE, "/subject/**").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.subject_write.getItem()))
        .antMatchers(HttpMethod.GET, "/subject").hasAuthority(String.format("%s/%s", resourceServerIdentifier, SecurityAuthority.subject_read.getItem()))
        .antMatchers(HttpMethod.POST, "/user/**").hasAuthority("ADMIN")
        .antMatchers(HttpMethod.GET, "/user").hasAuthority("ADMIN")
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(getJwtAuthToken()));
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**");
  }

  JwtAuthenticationConverter getJwtAuthToken() {
    var converter = new JwtAuthenticationConverter();

    var groupAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    groupAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");
    groupAuthoritiesConverter.setAuthorityPrefix("");

    var scopeAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    scopeAuthoritiesConverter.setAuthorityPrefix("");

    var compositeAuthorities = new DelegatingJwtGrantedAuthoritiesConverter(groupAuthoritiesConverter, scopeAuthoritiesConverter);
    converter.setJwtGrantedAuthoritiesConverter(compositeAuthorities);
    return converter;
  }
}
