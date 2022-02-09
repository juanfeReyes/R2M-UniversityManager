package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler(value = {ResourceAlreadyCreatedException.class})
  public ResponseEntity<ErrorResponse> handleResourceAlreadyCreatedException(ResourceAlreadyCreatedException exception) {
    var errorMessage = new ErrorResponse(exception.getMessage());
    return new ResponseEntity<ErrorResponse>(errorMessage, HttpStatus.CONFLICT);
  }

  @ResponseBody
  @ExceptionHandler(value = {ResourceNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptionException(ResourceNotFoundException exception) {
    var errorMessage = new ErrorResponse(exception.getMessage());
    return new ResponseEntity<ErrorResponse>(errorMessage, HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> handleConstraintDeclarationException(ConstraintViolationException exception) {
    var errors = new ArrayList<String>();
    for (var error : exception.getConstraintViolations()) {
      errors.add(error.getMessage());
    }
    var errorMessage = new ErrorResponse("Bad request the following fields have errors", errors);
    return new ResponseEntity<ErrorResponse>(errorMessage, HttpStatus.BAD_REQUEST);
  }
}
