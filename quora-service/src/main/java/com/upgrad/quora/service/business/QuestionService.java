package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.helper.EndPoints;
import com.upgrad.quora.service.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class handling the business logic for Questions endpoint
 * @author zeelani
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserHelper userHelper;

    /**
     * Service method handling the creation question business logic
     * @param content
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     */
    @Transactional
     public QuestionEntity createQuestion(final String content,final String authorization) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.CREATE_QUESTION);
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(content);
        questionEntity.setUser(userAuthEntity.getUser());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionDao.createQuestion(questionEntity);
        return questionEntity;
     }

    /**
     * Service method handling the get all questions business logic
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     */
    @Transactional
    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.ALL_QUESTIONS);
        return  questionDao.getAllQuestions();
    }

    /**
     * Service method handling the update question business logic
     * @param content
     * @param questionId
     * @param authorization
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional
    public void updateQuestion(final String content, final String questionId, final String authorization) throws AuthorizationFailedException,InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1],EndPoints.EDIT_QUESTION);
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        } else if (!questionEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new InvalidQuestionException("ATHR-003","Only the question owner can edit the question");
        } else {
            questionEntity.setContent(content);
            questionEntity.setDate(ZonedDateTime.now());
            questionDao.updateQuestion(questionEntity);
        }
    }

    /**
     * Service method handling the delete question business logic
     * @param questionId
     * @param authorization
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional
    public void deleteQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1],EndPoints.DELETE_QUESTION);
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        } else if (!questionEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid())) {
            throw new InvalidQuestionException("ATHR-003","Only the question owner or admin can delete the question");
        }
        questionDao.deleteQuestion(questionId);
    }

    /**
     * Service method handling the get all questions by user business logic
     * @param userId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @Transactional
    public List<QuestionEntity> getAllQuestionsByUser(final String userId,final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity userEntity = userHelper.getUser(userId,bearerToken[1],EndPoints.ALL_QUESTIONS_BY_USER);
        return  questionDao.getAllQuestionsByUser(userEntity);
    }

}
