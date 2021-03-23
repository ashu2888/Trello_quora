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

/**
 * @author Madhuri
 * Controller class for handling the user profile endpoint requests
 */

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserHelper userHelper;

    /**
     * Get method to fetch the user profile based in user ID provided
     * @param userUuid
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path="/userprofile/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization)
                             throws AuthorizationFailedException , UserNotFoundException {
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