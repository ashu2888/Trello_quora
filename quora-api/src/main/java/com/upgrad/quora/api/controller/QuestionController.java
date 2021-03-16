package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.AuthenticateService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zeelani
 * Class implementing the endpoints for the Question
 */

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    AuthenticateService authenticateService;

    @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        String[] bearerToken = authorization.split("Bearer: ");

        UserAuthEntity userAuth = authenticateService.getUser(userUUID,bearerToken[1]);
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> editQuestionContent(@PathVariable("questionId") final String questionId) {
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> deleteQuestion(@PathVariable("questionId") final String questionId) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionResponse>> getAllQuestions(@PathVariable("userId") final String userId) {
        return null;
    }
}
