package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * DAO class handling the CRUD operations on answers
 * @author Vipin mohan, Ashish
 */

@Repository
public class AnswerDao {

    @PersistenceContext
    private  EntityManager entityManager;

    /**
     * Merge the answer object in the database
     * @param answerEntity
     * @return : answerEntity
     */
    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    /**
     * Persists the answer object in the database
     * @param answerEntity
     * @return : answerEntity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Fetch the answer object from database using UUID
     * @param uuid
     * @return : questionEntity
     */
    public AnswerEntity getAnswerByUuId(String  uuid) {
        try {
            return entityManager.createNamedQuery("answer.fetchByUuid", AnswerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Delete the answer
     * @param answerId
     */
    public void userAnswerDelete(final String answerId) {
        AnswerEntity answerEntity = getAnswerByUuId(answerId);
        entityManager.remove(answerEntity);
    }

    /**
     * Fetch all answers for a question
     * @param questionId
     * @return
     */
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        try {
            return entityManager.createNamedQuery("answer.fetchByQuestionId", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Merge the answer content
     * @param answerEntity
     * @return
     */
    public AnswerEntity editAnswerContent(final AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }
}
