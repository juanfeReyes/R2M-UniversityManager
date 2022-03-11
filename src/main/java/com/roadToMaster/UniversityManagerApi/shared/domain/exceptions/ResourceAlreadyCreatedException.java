package com.roadToMaster.UniversityManagerApi.shared.domain.exceptions;

public class ResourceAlreadyCreatedException extends ResourceConflictException {

  public ResourceAlreadyCreatedException(String message) {
    super(message);
  }
}
