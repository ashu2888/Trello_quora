package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * DAO class handling the CRUD operations on questions
 * @author zeelani
 */
@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persists the question object in the database
     * @param questionEntity
     * @return : questionEntity
     */
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * Retrieves all the questions from the database
     * @return list of Question objects
     */
    public List<QuestionEntity> getAllQuestions() {
        return entityManager.createNamedQuery("allQuestions",QuestionEntity.class).getResultList();
    }

    /**
     * merges the question object in the databse
     * @param questionEntity
     */
    public void updateQuestion(QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    /**
     * Deletes the question corresponding to the question id
     * @param questionId
     */
    public void deleteQuestion(Integer questionId) {
        QuestionEntity questionEntity = entityManager.find(QuestionEntity.class, questionId);
        entityManager.remove(questionEntity);
    }

    /**
     * Retrieves all the questions from the database of a particular user
     * @param user
     * @return list of Question objects
     */
    public List<QuestionEntity> getAllQuestionsByUser(UserEntity user) {
        return entityManager.createNamedQuery("allQuestions",QuestionEntity.class).setParameter("user",user).getResultList();
    }


}
