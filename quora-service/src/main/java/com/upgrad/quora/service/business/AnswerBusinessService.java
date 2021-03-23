package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.helper.EndPoints;
import com.upgrad.quora.service.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vipin, Ashish
 * Service class to handling all the business logic of answer endpoint request
 */

@Service
public class AnswerBusinessService {

    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Service method for handling the request to create an answers for a question
     * @param answer
     * @param questionId
     * @param authorization
     * @return
     * @throws InvalidQuestionException
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String answer, final String questionId, final String authorization)
            throws InvalidQuestionException, AuthorizationFailedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.CREATE_ANSWER);
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.createAnswer(answerEntity);
    }

    /**
     * Service method for handling the request to edit an answer for a question
     * @param answer
     * @param uuid
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String answer, final String uuid, final String authorization)
            throws AuthorizationFailedException, AnswerNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.EDIT_ANSWER);
        AnswerEntity answerEntity = answerDao.getAnswerByUuId(uuid);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        UserEntity currentUser = userAuthEntity.getUser();
        if (currentUser.getUuid() != answerEntity.getUser().getUuid()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setAns(answer);
        return answerDao.editAnswer(answerEntity);
    }


    /**
     * Service method for handling the request to delete a answer
     * @param answerId
     * @param authorization
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAnswer(final String answerId, final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.DELETE_ANSWER);
        AnswerEntity answerEntity = answerDao.getAnswerByUuId(answerId);
        // Validate if requested answer exist or not
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        // Validate if current user is the owner of requested answer or the role of user is not nonadmin
        if (!userAuthEntity.getUser().getUuid().equals(answerEntity.getUser().getUuid())) {
            if (userAuthEntity.getUser().getRole().equals("nonadmin")) {
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
            }
        }
        answerDao.userAnswerDelete(answerId);
    }


    /**
     * Service method for handling the request to get all answers for a question
     * @param questionId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        String[] bearerToken = authorization.split("Bearer ");
        UserAuthEntity userAuthEntity = userHelper.getUserAuth(bearerToken[1], EndPoints.ALL_ANSWERS_FOR_QUESTION);
        QuestionEntity questionEntity = questionDao.getQuestion(questionId);
        // Validate if requested question exist or not
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAllAnswersToQuestion(questionId);
    }
}
