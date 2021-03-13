package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDeo;
import com.upgrad.quora.service.entity.User_Entity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {

    @Autowired
    private UserDeo userDeo;
    private PasswordCryptographyProvider passwordCryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity signup(User_Entity userEntity) throws SignUpRestrictedException {

        if(userDeo.getUserById(String.valueOf(userEntity.getId()))!=null)
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");

        if (userDeo.getUserByEmail(userEntity.getEmail()) != null)
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");

        //String password = userEntity.getPassword();
        String[] encryptedTxt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedTxt[0]);
        userEntity.setPassword(encryptedTxt[1]);
        return userDeo.createUser(userEntity);

    }
}
