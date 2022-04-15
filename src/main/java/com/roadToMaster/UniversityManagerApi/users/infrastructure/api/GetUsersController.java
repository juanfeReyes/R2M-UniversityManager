package com.roadToMaster.UniversityManagerApi.users.infrastructure.api;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import com.roadToMaster.UniversityManagerApi.users.application.IGetUsers;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.api.dto.UserApiMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("user")
@Tag(name = "User")
@SecurityRequirement(name = "basicAuth")
public class GetUsersController {

  private final IGetUsers getUsers;

  public GetUsersController(IGetUsers getUsers, UserApiMapper userApiMapper) {
    this.getUsers = getUsers;
  }

  @Operation(summary = "Get users", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResponse<User> getCourses(@RequestParam @Min(value = 0, message = "page number must be more at least 0") int pageNumber,
                                       @RequestParam @Min(value = 1, message = "page size must be more at least 1") int pageSize) {
    var page = PageRequest.of(pageNumber, pageSize);
    var coursesPage = getUsers.execute(page);
    return new PageResponse(coursesPage.getTotalElements(), coursesPage.getNumber(), coursesPage.getContent());
  }
}
