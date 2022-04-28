package com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.cognito;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.IUserProviderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Slf4j
@Component(value = "cognito")
public class CognitoClient implements IUserProviderClient {

  public static final String USER_ACCOUNT_ALREADY_EXISTS_ERROR = "User account already exists";
  @Value("${cognito.poolId}")
  private String userPoolId;

  @Value("${cognito.baseUserPassword}")
  private String basePassword;

  public String registerUser(User user) {
    CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();
    var username = saveUser(cognitoClient, user);
    cognitoClient.close();
    return username;
  }

  private String saveUser(CognitoIdentityProviderClient cognitoClient, User user) {
    AdminCreateUserResponse userResponse;
    try {
      AttributeType userAttrs = AttributeType.builder()
          .name("email")
          .value(user.getEmail())
          .build();

      AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
          .userPoolId(userPoolId)
          .username(user.getUsername())
          .temporaryPassword(basePassword)
          .userAttributes(userAttrs)
          .messageAction(MessageActionType.SUPPRESS)
          .build();

      userResponse = cognitoClient.adminCreateUser(userRequest);

      if (user.getRole().value.equals(RoleEnum.ADMIN.value)) {
        addUserToAdminGroup(cognitoClient, userResponse.user().username());
      }
    } catch (UsernameExistsException usernameExistsException) {
      log.error("Cognito client Error: {}", usernameExistsException.getMessage());
      throw new ResourceConflictException(USER_ACCOUNT_ALREADY_EXISTS_ERROR);
    } catch (InvalidParameterException invalidParameterException) {
      log.error("Cognito client Error: {}", invalidParameterException.getMessage());
      throw new ResourceConflictException(invalidParameterException.awsErrorDetails().errorMessage());
    }
    return userResponse.user().username();
  }

  private void addUserToAdminGroup(CognitoIdentityProviderClient cognitoClient, String username) {
    AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
        .userPoolId(userPoolId)
        .username(username)
        .groupName("ADMIN")
        .build();

    cognitoClient.adminAddUserToGroup(groupRequest);
  }
}
