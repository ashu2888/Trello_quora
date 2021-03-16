package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
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
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method extracts data from db using first name of user from users table.
     * @param firstName
     * @return
     */
    public UserEntity getUserByName(final String firstName) {
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("firstName", firstName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method for inserting data into database
     * @param userEntity
     * @return
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * Method for extracting data from users table with userName
     * @param userName
     * @return
     */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity updateAuthToken(UserAuthEntity authEntity) {

        entityManager.merge(authEntity);
        return authEntity;
    }

    public UserAuthEntity getUserAuthByToken(String accessToken) {

        try {
            return entityManager.createNamedQuery("userByAuthToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }
    public UserEntity getUserByUserUuid(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUseruuid", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
