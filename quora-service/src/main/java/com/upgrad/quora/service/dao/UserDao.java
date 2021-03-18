package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.User_Entity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method extracts data from db using emailid from users table.
     * @param email
     * @return
     */
    public User_Entity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", User_Entity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method extracts data from db using first name of user from users table.
     * @param firstName
     * @return
     */
    public User_Entity getUserByName(final String firstName) {
        try {
            return entityManager.createNamedQuery("userByName", User_Entity.class).setParameter("firstName", firstName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method for inserting data into database
     * @param userEntity
     * @return
     */
    public User_Entity createUser(User_Entity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * Method for extracting data from users table with userName
     * @param userName
     * @return
     */
    public User_Entity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUsername", User_Entity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuth updateAuthToken(UserAuth authEntity) {

        entityManager.merge(authEntity);
        return authEntity;
    }

    public UserAuth getUserAuthByToken(String accessToken) {

        try {
            return entityManager.createNamedQuery("userByAuthToken", UserAuth.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }
    public User_Entity getUserByUserUuid(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUseruuid", User_Entity.class).setParameter("uuid", userUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public User_Entity deleteUser(User_Entity userEntity) {
        try {
           entityManager.remove(userEntity);
            return userEntity;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
