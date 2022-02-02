package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponse {

  private String message;

  private Date timestamp;

  public ErrorResponse(String message){
    this.message = message;
    this.timestamp = new Date();
  }
}
