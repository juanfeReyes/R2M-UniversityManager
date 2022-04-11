package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.users.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
//@NoArgsConstructor
@AllArgsConstructor
public class Subject {

  @NotNull
  private String id;

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;

  private List<Schedule> schedules;

  private User professor;

}
