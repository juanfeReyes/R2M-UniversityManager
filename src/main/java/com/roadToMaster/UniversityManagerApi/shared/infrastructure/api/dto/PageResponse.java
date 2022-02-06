package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PageResponse<T> {

  private long totalCourses;

  private int pageNumber;

  private List<T> content;
}
