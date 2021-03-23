package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 * @author Madhuri
 * DAO class to perform CRUD operations on user entity
 */

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method  extracts data from db using userByEmail named query with email as parameter from users table.
     * Method calls createNamedQuery of entityManager class.
     * @param email
     * @return instance of user
     */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("user.fetchByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method  extracts data from db using userByName named query with firstName as parameter from users table.
     * Method calls createNamedQuery of entityManager class.
     * @param userName
     * @return instance of user
     */
    public UserEntity getUserByName(final String userName) {
        try {
            return entityManager.createNamedQuery("user.fetchByUsername", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method for persisting data into database.Method calls createNamedQuery of entityManager class.
     * @param userEntity
     * @return instance of user
     */
    public UserEntity createUser(UserEntity userEntity) {
        try {
            entityManager.persist(userEntity);
            return userEntity;
        }catch (ConstraintViolationException exe){
            return null;
        }

    }

    /**
     * Method for extracting data from users table using userByUsername named query with userName as parameter. Method calls createNamedQuery of entityManager class.
     * @param userName
     * @return instance of user
     */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("user.fetchByUsername", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     *  Method for updating the user authentication table. Method calls merge method of entityManager class.
     * @param authEntity
     * @return instance of user authentication
     */
    public UserAuthEntity updateAuthToken(UserAuthEntity authEntity) {

        entityManager.merge(authEntity);
        return authEntity;
    }

    /**
     * Method for extracting data from user authentication table with access token using userByAuthToken named query. Method calls createNamedQuery of entityManager class.
     * @param accessToken
     * @return instance of user authentication
     */
    public UserAuthEntity getUserAuthByToken(String accessToken) {

        try {
            return entityManager.createNamedQuery("user.fetchByAuthToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    /**
     * Method for extracting data from users table using userByUseruuid named query with uuid as parameter. Method calls createNamedQuery of entityManager class.
     * @param userUuid
     * @return instance of user entity
     */
    public UserEntity getUserByUserUuid(final String userUuid) {
        try {
            return entityManager.createNamedQuery("user.fetchByUseruuid", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method deletes the entry for user and user authentication with the help of entity manager's remove method.
     * @param userEntity
     * @return instance of user entity
     */
    public UserEntity deleteUser(UserEntity userEntity) {
        try {
            entityManager.remove(userEntity);
            return userEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
