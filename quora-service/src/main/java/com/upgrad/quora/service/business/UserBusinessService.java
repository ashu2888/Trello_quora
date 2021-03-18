package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuth;
import com.upgrad.quora.service.entity.User_Entity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method for user signup.This method checks for the entry of user in database with first name or email.
     * If there is entry in table then throws proper exception
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

    /**
     * Method for the validation of authentication
     * @param userName
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuth signIn(final String userName, final String password) throws AuthenticationFailedException {
        User_Entity user_entity = userDao.getUserByUserName(userName);
        if(user_entity == null)
            throw new AuthenticationFailedException("ATH-001","This username does not exist");

        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, user_entity.getSalt());

        if(encryptedPassword.equals(user_entity.getPassword())){
           JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
           UserAuth userAuth = new UserAuth();

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

           userAuth.setAccessToken(jwtTokenProvider.generateToken(user_entity.getUuid(),now,expiresAt));
           userAuth.setLoginAt(now);
           userAuth.setExpiresAt(expiresAt);

           userAuth.setUuid(user_entity.getUuid());
           userAuth.setUser(user_entity);
           userDao.updateAuthToken(userAuth);

            return userAuth;

        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password Failed");
        }

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity signOut(String accessToken) throws SignOutRestrictedException {

        UserAuth userAuth = userDao.getUserAuthByToken(accessToken);
        if (userAuth == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        User_Entity userEntity = userAuth.getUser();

        userAuth.setLogoutAt(ZonedDateTime.now());
        userDao.updateAuthToken(userAuth);

        return userEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity getUser( String userUuid,String authorisation) throws AuthorizationFailedException, UserNotFoundException {

        User_Entity user_entity =userDao.getUserByUserUuid(userUuid);
        if(user_entity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        UserAuth userAuth = userDao.getUserAuthByToken(authorisation);
        if(userAuth==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuth.getLogoutAt().isBefore(userAuth.getLoginAt())){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        return  user_entity;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User_Entity deleteUser(String uuid, String authorisation) throws AuthorizationFailedException,UserNotFoundException {
        User_Entity user_entity =userDao.getUserByUserUuid(uuid);
        if(user_entity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }

        UserAuth userAuth = userDao.getUserAuthByToken(authorisation);
        if(userAuth==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in.");
        }

        if(userAuth.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.");
        }


        if(user_entity.getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin.");
        }

        userDao.deleteUser(user_entity);
        return  user_entity;
    }
}
