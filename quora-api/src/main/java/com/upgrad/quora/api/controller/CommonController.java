package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.helper.EndPoints;
import com.upgrad.quora.service.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class CommonController {
    @Autowired
    private UserHelper userHelper;
    /**
     *  This controller class returns the details about the user with uuid and access token provided in the http Request.
     *  It accepts http GET method and throws AuthorizationFailedException when user is signout or not signin.
     *  It throws  UserNotFoundException when user with given uuid is not exist in users entity table.
     */

    @RequestMapping(method = RequestMethod.GET, path="/userprofile/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException , UserNotFoundException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity userEntity = userHelper.getUser(userUuid, bearerToken[1], EndPoints.COMMON);
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