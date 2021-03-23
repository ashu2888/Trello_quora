package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import com.upgrad.quora.service.helper.EndPoints;
import com.upgrad.quora.service.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

/**
 * @author Madhuri
 * Service class handling the user endpoints business logic
 */

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private UserHelper userHelper;

    /**
     * Service method for handling the business logic for user signup request
     * @param userEntity
     * @return
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

        if(userDao.getUserByName(userEntity.getUserName())!=null) {
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
     * Service method for handling the business logic for user sign in request
     * @param userName
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signIn(final String userName, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUserName(userName);
        if(userEntity == null)
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expiresAt = now.plusHours(8);
            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setUuid(userEntity.getUuid());
            userAuthEntity.setUser(userEntity);
            userDao.updateAuthToken(userAuthEntity);
            return userAuthEntity;
        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password Failed");
        }

    }

    /**
     * Service method for handling the business logic for user signout request
     * @param accessToken
     * @return
     * @throws SignOutRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signOut(String accessToken) throws SignOutRestrictedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }

        UserEntity userEntity = userAuthEntity.getUser();
        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        userDao.updateAuthToken(userAuthEntity);
        return userEntity;
    }

    /**
     * Service method for handling the business logic for user entity
     * @param userUuid
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(String userUuid) {
        UserEntity userEntity =userDao.getUserByUserUuid(userUuid);
        return userEntity;
    }

    /**
     * Service method for handling the business logic for user auth entity
     * @param authorisation
     * @return
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity getUserAuth(final String authorisation) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(authorisation);
        return userAuthEntity;
    }


    /**
     * Service method for handling the business logic for delete user request
     * @param uuid
     * @param authorization
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(String uuid, String authorization) throws AuthorizationFailedException,UserNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity userEntity =userHelper.getUser(uuid,bearerToken[1], EndPoints.DELETE_USER);
        userDao.deleteUser(userEntity);
    }
}
