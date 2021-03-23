package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Madhuri
 * Controller class for handling the admin endpoint requests
 */

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * Endpoint method handling the user delete operation
     * @param userUuid
     * @param authorization
     * @throws AuthorizationFailedException, UserNotFoundException
     * @return UserDeleteResponce
     */
    @RequestMapping(method = RequestMethod.DELETE,path ="/admin/user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException
    {
        userBusinessService.deleteUser(userUuid,authorization);
        UserDeleteResponse userDeleteResponce = new UserDeleteResponse().id(userUuid).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponce,HttpStatus.NO_CONTENT);
    }


}