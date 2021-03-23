package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author zeelani, Madhuri, Ashish, Vipin
 * Class handling all the exceptions raised by all the endpoints.
 */

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Method handling the SignUpRestrictedException
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestriction(SignUpRestrictedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.CONFLICT);
    }

    /**
     * Method handling AuthenticationFailedException
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailed(AuthenticationFailedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    /**
     * Method handling SignOutRestrictedException
     * @param exe
     * @param request
     * @return ErrorResponse
     */

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    /**
     * Method handling UserNotFoundException
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }

    /**
     * Method handling AuthorizationFailedException
     * @param exe
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.FORBIDDEN);
    }

    /**
     * Handler for InvalidQuestionException
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exception, WebRequest request)  {
        if (exception.getCode().equals("QUES-001")) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                    message(exception.getErrorMessage()), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).
                    message(exception.getErrorMessage()), HttpStatus.FORBIDDEN);
        }
    }
    /**
     * Handler for AnswerNotFoundException
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exception , WebRequest request){
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()),HttpStatus.NOT_FOUND);
    }
}
