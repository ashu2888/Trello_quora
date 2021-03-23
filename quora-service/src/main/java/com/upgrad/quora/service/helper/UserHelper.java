package com.upgrad.quora.service.helper;

import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    @Autowired
    private UserBusinessService userBusinessService;

    public UserAuthEntity getUserAuth(final String authorisation, EndPoints endpoint) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userBusinessService.getUserAuth(authorisation);
        if(userAuthEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthEntity.getLogoutAt() != null && userAuthEntity.getLogoutAt().isAfter(userAuthEntity.getLoginAt())) {
            switch (endpoint) {
                case CREATE_QUESTION :
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
                case ALL_QUESTIONS:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
                case ALL_QUESTIONS_BY_USER:
                    throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
                case DELETE_QUESTION:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
                case EDIT_QUESTION:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
                case COMMON:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
                case DELETE_USER:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out.");
                default:
                    throw new AuthorizationFailedException("ATHR-002","User is signed out");
            }
        }

        return userAuthEntity;
    }

    public UserEntity getUser(final String userUuid, final String authorization, EndPoints endpoint) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity =userBusinessService.getUser(userUuid);
        if (userEntity == null) {
            switch (endpoint) {
                case DELETE_USER:
                    throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
                default:
                    throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
            }
        }
        UserAuthEntity userAuthEntity = getUserAuth(authorization,endpoint);
        if(userEntity.getRole().equals("nonadmin") && endpoint.equals(EndPoints.DELETE_USER)){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin.");
        }
        return userEntity;
    }
}
