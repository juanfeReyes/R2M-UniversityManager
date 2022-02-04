package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {

  private String message;

  private Date timestamp;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String> fieldsError;

  public ErrorResponse(String message) {
    this.message = message;
    this.timestamp = new Date();
  }

  public ErrorResponse(String message, List<String> violations) {
    this.message = message;
    this.timestamp = new Date();
    this.fieldsError = violations;
  }
}
