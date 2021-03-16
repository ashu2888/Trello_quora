package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Class to handle all the exceptions raised by endpoints and map to
 * response entity
 * @author zeelani
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Handler for AuthourizationFailedException
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exception, WebRequest request)  {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                message(exception.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler for InvalidQuestionException
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exception, WebRequest request)  {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                message(exception.getErrorMessage()), HttpStatus.NO_CONTENT);
    }

    /**
     * Handler for UserNotFoundException
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exception, WebRequest request)  {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Method for telling controller class to display custom message when SignUpRestrictedException thrown by service class
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestriction(SignUpRestrictedException exe , WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.CONFLICT);
    }

    /**
     * Method for telling controller class to display custom message when AuthenticationFailedException thrown by service class
     * @param exe
     * @param request
     * @return
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailed(AuthenticationFailedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }

}