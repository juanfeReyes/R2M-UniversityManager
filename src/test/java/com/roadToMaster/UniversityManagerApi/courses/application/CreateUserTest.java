package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.users.application.CreateUser;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.keycloak.KeycloakClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

  private final UserEntityMapper userEntityMapper = new UserEntityMapperImpl();

  @Mock
  private UserRepository userRepositoryMock;

  @Mock
  private KeycloakClient keycloakClient;

  @Captor
  private ArgumentCaptor<UserEntity> userEntityCaptor;

  private CreateUser createUser;

  @BeforeEach
  public void init() {
    this.createUser = new CreateUser(userEntityMapper, userRepositoryMock, keycloakClient);
  }

  @Test
  public void shouldSaveCreateUser() {

    var user = UserMother.buildValid();
    when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.empty());
    when(keycloakClient.registerUser(any())).thenReturn("userId");

    createUser.execute(user);

    verify(userRepositoryMock, times(1)).save(userEntityCaptor.capture());
    assertThat(userEntityCaptor.getValue()).usingRecursiveComparison()
        .ignoringFields("role").isEqualTo(user);
  }

  @Test
  public void shouldThrowExceptionWhenUsernameAlreadyExists() {

    var user = UserMother.buildValid();
    when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(userEntityMapper.userToEntity(user)));

    assertThatThrownBy(() -> {
      createUser.execute(user);
    }).isInstanceOf(ResourceAlreadyCreatedException.class)
        .hasMessage(String.format("User with username %s already exists", user.getUsername()));

    verify(userRepositoryMock, never()).save(userEntityCaptor.capture());
  }
}
