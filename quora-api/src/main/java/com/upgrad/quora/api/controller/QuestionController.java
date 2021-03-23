package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    UserHelper userHelper;

    @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final QuestionEntity createdQuestion = questionService.createQuestion(questionRequest.getContent(),authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("Question created successfully");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        List<QuestionEntity> allQuestions = questionService.getAllQuestions(authorization);
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (QuestionEntity q : allQuestions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            questionDetailsResponse.setContent(q.getContent());
            questionDetailsResponse.setId(q.getUuid());
            questionDetailsResponseList.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(@PathVariable("questionId") final String questionId,
                                                                    QuestionEditRequest questionEditRequest,
                                                                    @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        questionService.updateQuestion(questionEditRequest.getContent(),questionId, authorization);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionId).status("Question edited successfully");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{questionId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException {
        questionService.deleteQuestion(questionId,authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") final String userId,
                                                                        @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<>();
        List<QuestionEntity> questionEntityList = questionService.getAllQuestionsByUser(userId,authorization);
        if (questionEntityList != null) {
            for (QuestionEntity q : questionEntityList) {
                QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                questionDetailsResponse.setId(q.getUuid());
                questionDetailsResponse.setContent(q.getContent());
                questionDetailsResponses.add(questionDetailsResponse);
            }
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
    }
}
