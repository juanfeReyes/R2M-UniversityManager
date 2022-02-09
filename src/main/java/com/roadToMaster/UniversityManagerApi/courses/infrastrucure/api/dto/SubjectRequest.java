package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class SubjectRequest{

  @NotEmpty
  private String id;

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;
}
