package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuth(bearerToken[1]);
        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUser(userAuthEntity.getUser());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        final QuestionEntity createdQuestion = questionService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("Question created successfully");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuth(bearerToken[1]);
        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }
        List<QuestionEntity> allQuestions = questionService.getAllQuestions();
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
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuth(bearerToken[1]);
        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
        }
        QuestionEntity questionEntity = questionService.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        } else if (!questionEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new InvalidQuestionException("ATHR-003","Only the question owner can edit the question");
        } else {
            questionEntity.setContent(questionEditRequest.getContent());
            questionEntity.setDate(ZonedDateTime.now());
            questionService.updateQuestion(questionEntity);
            QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("Question edited successfully");
            return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{questionId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuth(bearerToken[1]);
        if (userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())) {
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
        }
        QuestionEntity questionEntity = questionService.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        } else if (!questionEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new InvalidQuestionException("ATHR-003","Only the question owner or admin can delete the question");
        }
        questionService.deleteQuestion(questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") final String userId,
                                                                        @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<>();
        try {
            UserEntity userEntity = userBusinessService.getUser(userId, bearerToken[1]);
            List<QuestionEntity> questionEntityList = questionService.getAllQuestionsByUser(userEntity);
            if (questionEntityList != null) {
                for (QuestionEntity q : questionEntityList) {
                    QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                    questionDetailsResponse.setId(q.getUuid());
                    questionDetailsResponse.setContent(q.getContent());
                    questionDetailsResponses.add(questionDetailsResponse);
                }
            }
        } catch (AuthorizationFailedException a) {
            if (a.getCode().equals("ATHR-002")) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
            } else {
                throw a;
            }
        } catch (UserNotFoundException u) {
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses, HttpStatus.OK);
    }
}
