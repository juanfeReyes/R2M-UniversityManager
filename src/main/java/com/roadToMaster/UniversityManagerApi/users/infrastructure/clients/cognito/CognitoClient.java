package com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.cognito;

import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.IUserProviderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Component(value = "cognito")
public class CognitoClient implements IUserProviderClient {

  @Value("${cognito.poolId}")
  private String userPoolId;

  public String registerUser(User user) {
    CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
        .region(Region.US_EAST_1)
        .build();
    var username = saveUser(cognitoClient, user);
    cognitoClient.close();
    return username;
  }

  private String saveUser(CognitoIdentityProviderClient cognitoClient, User user) {

    AttributeType userAttrs = AttributeType.builder()
        .name("email")
        .value(user.getEmail())
        .build();

    AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
        .userPoolId(userPoolId)
        .username(user.getUsername())
        .temporaryPassword("Temporal1*")
        .userAttributes(userAttrs)
        .messageAction(MessageActionType.SUPPRESS)
        .build();

    AdminCreateUserResponse userResponse = cognitoClient.adminCreateUser(userRequest);

    if (user.getRole().value.equals(RoleEnum.ADMIN.value)) {
      AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
          .userPoolId(userPoolId)
          .username(userResponse.user().username())
          .groupName("ADMIN")
          .build();

      cognitoClient.adminAddUserToGroup(groupRequest);
    }

    return userResponse.user().username();
  }
}
