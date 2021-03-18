package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User_Entity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.awt.*;

@RestController
@RequestMapping("/")
public class AdminController {
   @Autowired
    UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.DELETE,path ="/admin/user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException
    {
        String[] bearer = authorization.split("Bearer ");
        User_Entity user_entity = userBusinessService.deleteUser(userUuid,bearer[1]);
        UserDeleteResponse userDeleteResponce = new UserDeleteResponse().id(user_entity.getUuid()).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponce,HttpStatus.NO_CONTENT);
    }


}
