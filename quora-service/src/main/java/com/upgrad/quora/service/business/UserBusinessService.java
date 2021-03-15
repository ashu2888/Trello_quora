package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.User_Entity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method for user signup.This method checks for the entry of user in database with first name or email. If there is entry in table then throws proper exception
     * @param userEntity
     * @return
     * @throws SignUpRestrictedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity signup(User_Entity userEntity) throws SignUpRestrictedException {

        if(userDao.getUserByName(userEntity.getFirstName())!=null) {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }

        if (userDao.getUserByEmail(userEntity.getEmail()) != null)
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");

        String[] encryptedTxt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedTxt[0]);
        userEntity.setPassword(encryptedTxt[1]);
        return userDao.createUser(userEntity);

    }
}
