package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        return entityManager.createNamedQuery("question.fetchAll",QuestionEntity.class).getResultList();
    }

    /**
     * merges the question object in the databse
     * @param questionEntity
     */
    public void updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
    }

    /**
     * Deletes the question corresponding to the question id
     * @param questionUUID
     */
    public void deleteQuestion(final String questionUUID) {
        QuestionEntity questionEntity = null;
        try {
            questionEntity = getQuestion(questionUUID);
        } catch (NoResultException n) {
            return;
        }
        entityManager.remove(questionEntity);
    }

    /**
     * Get the question corresponding to the question id
     * @param questionUUID
     */
    public QuestionEntity getQuestion(final String questionUUID) {
        try {
            return entityManager.createNamedQuery("question.fetchByUuid", QuestionEntity.class).
                    setParameter("uuid", questionUUID).getSingleResult();
        } catch (NoResultException n) {
            return null;
        }
    }

    /**
     * Retrieves all the questions from the database of a particular user
     * @param user
     * @return list of Question objects
     */
    public List<QuestionEntity> getAllQuestionsByUser(UserEntity user) {
        return entityManager.createNamedQuery("question.fetchByUser",QuestionEntity.class).setParameter("user",user).getResultList();
    }


}
