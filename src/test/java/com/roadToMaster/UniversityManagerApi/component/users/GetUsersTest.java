package com.roadToMaster.UniversityManagerApi.component.users;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.UserRequestMother;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.clients.IUserProviderClient;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

public class GetUsersTest extends ComponentTestBase {

  private final static String BASE_URL = "/user";

  private static final ParameterizedTypeReference<PageResponse<User>> userResponseReference = new ParameterizedTypeReference<>() {
  };

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserEntityMapper userEntityMapper;

  Comparator roleEnumComparator = (e, v) -> ((RoleEnum) e).value.compareTo((String) v);

  @AfterEach
  public void teardown(){
    userRepository.deleteAll();
  }
  @Test
  public void ShouldGetUsers() {

    userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));

    var url = UriComponentsBuilder.fromUriString(BASE_URL)
        .queryParam("pageNumber", 0)
        .queryParam("pageSize", 2)
        .toUriString();
    var response = restTemplate.exchange(url, HttpMethod.GET, null, userResponseReference);

    assertThat(response.getBody().getContent()).hasSize(1);
  }
}
