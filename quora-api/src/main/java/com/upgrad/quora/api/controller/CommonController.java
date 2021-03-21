package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class CommonController {
    @Autowired
    private UserBusinessService userBusinessService;
    /**
     *  This controller class returns the details about the user with uuid and access token provided in the http Request.
     *  It accepts http GET method and throws AuthorizationFailedException when user is signout or not signin.
     *  It throws  UserNotFoundException when user with given uuid is not exist in users entity table.
     */

    @RequestMapping(method = RequestMethod.GET, path="/userprofile/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

        String[] bearer = authorization.split("Bearer ");
        UserEntity userEntity = userBusinessService.getUser(userUuid, bearer[1]);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .userName(userEntity.getUserName())
                .emailAddress(userEntity.getEmail())
                .country(userEntity.getCountry())
                .aboutMe(userEntity.getAboutMe())
                .dob(userEntity.getDob())
                .contactNumber(userEntity.getContactNumber());

        return new  ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
