package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * Method for user signup.This method checks for the entry of user in database with first name or email.
     * If there is no entry in database then throws SignUpRestrictedException exception and if user is available in database
     * then this method calls entrypt method of PasswordCreptographyProvider class to encrypt the password and to genarate the salt.
     * Passes this information to the DEO class for persisting the user into database.
     * @param userEntity
     * @return userEntity
     * @throws SignUpRestrictedException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {

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
     * Method takes username and password as input, checks for the user existance if not available throws AuthenticationFailedException exception otherwise
     * creates base 64 password using encrypt method of PasswordCryptographyProvider class.If created password is available in db with the help of JwtTokenProvider class creates access token
     * and stores into user authentication table otherwise throws AuthenticationFailedException exception
     * @param userName
     * @param password
     * @return object of UserAuthetication entity class
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signIn(final String userName, final String password) throws AuthenticationFailedException {
        UserEntity user_Auth_entity = userDao.getUserByUserName(userName);
        if(user_Auth_entity == null)
            throw new AuthenticationFailedException("ATH-001","This username does not exist");

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, user_Auth_entity.getSalt());

        if(encryptedPassword.equals(user_Auth_entity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity = new UserAuthEntity();

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(user_Auth_entity.getUuid(),now,expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);

            userAuthEntity.setUuid(user_Auth_entity.getUuid());
            userAuthEntity.setUser(user_Auth_entity);
            userDao.updateAuthToken(userAuthEntity);

            return userAuthEntity;

        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password Failed");
        }

    }

    /**
     * Method for user signout. This method checks the entry in db with provided access token if entry is available in db user signsout and logoutAt property in user authentication table gets updated
     * otherwise throws SignOutRestrictedException
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
     * method retrives the details about the provides user id with the help of access token. Checks for valid user id if fails throws UserNotFoundException,
     * also checks for valid accesstoken and LogoutAt time if valid access token is not provided or user is already loged out then throws AuthorizationFailedException exception
     * other wise returns detail of user
     * @param userUuid
     * @param authorisation
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(String userUuid, String authorisation) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity user_Auth_entity =userDao.getUserByUserUuid(userUuid);
        if(user_Auth_entity ==null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(authorisation);
        if(userAuthEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt().isBefore(userAuthEntity.getLoginAt())){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }
        return user_Auth_entity;
    }

    /**
     * Method for deleting user with provided user id and access token. Checks for valid user id if fails throws UserNotFoundException,
     *  also checks for valid access token and LogoutAt time if valid access token is not provided or user is already logged out then throws AuthorizationFailedException exception
     *   other wise user gets deleted
     * @param uuid
     * @param authorisation
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(String uuid, String authorisation) throws AuthorizationFailedException,UserNotFoundException {
        UserEntity user_Auth_entity =userDao.getUserByUserUuid(uuid);
        if(user_Auth_entity ==null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }

        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(authorisation);
        if(userAuthEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in.");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.");
        }


        if(user_Auth_entity.getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin.");
        }

        userDao.deleteUser(user_Auth_entity);
        return user_Auth_entity;
    }
}
