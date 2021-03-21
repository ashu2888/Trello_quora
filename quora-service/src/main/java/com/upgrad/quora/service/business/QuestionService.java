package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class handling the business logic for Questions endpoint
 * @author zeelani
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
     public QuestionEntity createQuestion(QuestionEntity questionEntity) {
         questionDao.createQuestion(questionEntity);
         return questionEntity;
     }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions() {
        return  questionDao.getAllQuestions();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQuestion(final QuestionEntity questionEntity) {
        questionDao.updateQuestion(questionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity getQuestion(final String questionUUID) {
        return questionDao.getQuestion(questionUUID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteQuestion(final String questionUUID) {
        questionDao.deleteQuestion(questionUUID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(final UserEntity userEntity) {
        return  questionDao.getAllQuestionsByUser(userEntity);
    }

}
