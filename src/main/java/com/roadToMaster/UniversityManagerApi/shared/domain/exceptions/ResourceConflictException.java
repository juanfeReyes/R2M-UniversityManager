package com.roadToMaster.UniversityManagerApi.shared.domain.exceptions;

public class ResourceConflictException extends RuntimeException {

  public ResourceConflictException(String message) {
    super(message);
  }
}
