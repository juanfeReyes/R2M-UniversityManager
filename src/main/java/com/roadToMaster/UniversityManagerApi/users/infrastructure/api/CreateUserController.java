package com.roadToMaster.UniversityManagerApi.users.infrastructure.api;

import com.roadToMaster.UniversityManagerApi.users.application.interfaces.ICreateUser;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto.UserApiMapper;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@Tag(name = "User")
@SecurityRequirement(name = "basicAuth")
@Validated
public class CreateUserController {

  private final UserApiMapper apiMapper;

  private final ICreateUser createUser;

  @Autowired
  public CreateUserController(UserApiMapper apiMapper, ICreateUser createUser) {
    this.apiMapper = apiMapper;
    this.createUser = createUser;
  }

  @Operation(summary = "Create User", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@Valid @RequestBody UserRequest userRequest) {
    return createUser.execute(apiMapper.userRequestToDomain(userRequest));
  }
}
