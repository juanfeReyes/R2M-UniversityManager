package com.roadToMaster.UniversityManagerApi.shared.domain.exceptions;

public class ResourceAlreadyCreatedException extends RuntimeException {

  public ResourceAlreadyCreatedException(String message) {
    super(message);
  }
}
