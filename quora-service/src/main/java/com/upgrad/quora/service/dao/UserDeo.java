package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.User_Entity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDeo {
    @PersistenceContext
    private EntityManager entityManager;

    public User_Entity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", User_Entity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public User_Entity getUserById(final String id) {
        try {
            return entityManager.createNamedQuery("userById", User_Entity.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public User_Entity createUser(User_Entity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

}
