package com.roadToMaster.UniversityManagerApi.shared.infrastructure.api;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                       HttpHeaders headers,
                                                                       HttpStatus status, WebRequest request){
    var errors = new ArrayList<String>();
    for (var error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getDefaultMessage());
    }
    var errorMessage = new ErrorResponse("Bad request the following fields have errors", errors);
    log.error("Exception: {}: {}",errorMessage.getMessage(), String.join(",", errorMessage.getFieldsError()));
    return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler(value = {ResourceConflictException.class})
  public ResponseEntity<ErrorResponse> handleResourceConflictException(ResourceConflictException exception) {
    var errorMessage = new ErrorResponse(exception.getMessage());
    log.error(errorMessage.getMessage());
    return new ResponseEntity(errorMessage, HttpStatus.CONFLICT);
  }

  @ResponseBody
  @ExceptionHandler(value = {ResourceNotFoundException.class})
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
    var errorMessage = new ErrorResponse(exception.getMessage());
    log.error(errorMessage.getMessage());
    return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
  }

  @ResponseBody
  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> handleConstraintDeclarationException(ConstraintViolationException exception) {
    var errors = new ArrayList<String>();
    for (var error : exception.getConstraintViolations()) {
      errors.add(error.getMessage());
    }
    var errorMessage = new ErrorResponse("Bad request the following fields have errors", errors);
    log.error("Exception: {}: {}",errorMessage.getMessage(), String.join(",", errorMessage.getFieldsError()));
    return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
  }

  @ResponseBody
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception exception) {
    log.error("Unhandled exception: {}", exception.getMessage());
    var errorMessage = new ErrorResponse("Internal server error. Request was not processed");
    return new ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
