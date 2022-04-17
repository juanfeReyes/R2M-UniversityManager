package com.roadToMaster.UniversityManagerApi.component.users;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.UserRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.IUserProviderClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

public class CreateUserTest extends ComponentTestBase {
  private final static String BASE_URL = "/user";


  @Autowired
  private UserRepository userRepository;

  @MockBean
  @Qualifier("cognito")
  private IUserProviderClient userProviderClient;

  @AfterEach
  public void teardown(){
    userRepository.deleteAll();
  }

  Comparator roleEnumComparator = (e, v) -> ((RoleEnum) e).value.compareTo((String) v);

  @Test
  public void ShouldCreateUser() {
    var userRequest = UserRequestMother.buildRandom();

    var response = restTemplate.exchange(BASE_URL, HttpMethod.POST, new HttpEntity<>(userRequest),
        User.class);

    var storedUser = userRepository.findByUsername(userRequest.getUsername());

    verify(userProviderClient, atMostOnce()).registerUser(any());
    assertThat(response.getBody()).usingRecursiveComparison()
        .withComparatorForType(roleEnumComparator, RoleEnum.class)
        .ignoringFields("id")
        .isEqualTo(userRequest);
    assertThat(storedUser).isPresent();
  }
}
