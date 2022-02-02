package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler(value = {ResourceAlreadyCreatedException.class})
  public ResponseEntity<ErrorResponse> handleResourceAlreadyCreatedException(ResourceAlreadyCreatedException exception){
    var errorMessage = new ErrorResponse(exception.getMessage());
    return new ResponseEntity<ErrorResponse>(errorMessage, HttpStatus.CONFLICT);
  }
}
