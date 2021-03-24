package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vipin mohan, Ashish
 * Controller class for handling the Answer endpoint requests for the questions asked
 */

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    /**
     * Post method for creating an answer for the previoulsy asked question
     * @param answerRequest
     * @param questionId
     * @param authorization
     * @return
     * @throws InvalidQuestionException
     * @throws AuthenticationFailedException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.POST,path = "/question/{questionId}/answer/create",
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId,
                             @RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthenticationFailedException,AuthorizationFailedException {
        final AnswerEntity createdAnswerEntity =  answerBusinessService.createAnswer(answerRequest.getAnswer(), questionId, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);

    }


    /**
     * Put method for  handling the edit operation for a previously answered question
     * @param answerRequest
     * @param answerId
     * @param authorization
     * @return
     * @throws AnswerNotFoundException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}",
            produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> editAnswer(final AnswerRequest answerRequest, @PathVariable("answerId") final String answerId,
                            @RequestHeader("authorization") final String authorization) throws AnswerNotFoundException, AuthorizationFailedException {
        AnswerEntity answerEntity = answerBusinessService.editAnswer(answerRequest.getAnswer(),answerId,authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<>(answerResponse, HttpStatus.OK);
    }


    /**
     * Delete method for handling the delete operation for an answer
     * @param answerId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, AnswerNotFoundException {
        answerBusinessService.deleteAnswer(answerId, authorization);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    /**
     * Get method for getting all answers for a question
     * @param questionId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion (@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {
        List<AnswerEntity> allAnswers = answerBusinessService.getAllAnswersToQuestion(questionId, authorization);
        List<AnswerDetailsResponse> allAnswersResponse = new ArrayList<AnswerDetailsResponse>();
        for (AnswerEntity ans : allAnswers) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse()
                    .answerContent(ans.getAns())
                    .questionContent(ans.getQuestion().getContent())
                    .id(ans.getUuid());
            allAnswersResponse.add(answerDetailsResponse);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(allAnswersResponse, HttpStatus.OK);
    }
}
