package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Madhuri
 * Class implementing the endpoint for the admin user
 */


@RestController
@RequestMapping("/")
public class AdminController {
    @Autowired
    UserBusinessService userBusinessService;

    /**
     * This controller class takes user uuid and access token as request parameters  and returns UserDeleteResponce. The http request method is DELETE.
     * It throws AuthorizationFailed exception if given access token is not valid or user is signed out or not signin.
     * It also throws UserNotFound exception if there is no user found in users class with given uuid.
     * @param userUuid
     * @param authorization
     * @return UserDeleteResponse
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE,
            path ="/admin/user/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException
    {
        String[] bearer = authorization.split("Bearer ");
        UserEntity userEntity = userBusinessService.deleteUser(userUuid,bearer[1]);
        UserDeleteResponse userDeleteResponce = new UserDeleteResponse().id(userEntity.getUuid()).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponce,HttpStatus.OK);
    }


}
