package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserBusinessService userBusinessService;

    /**
     * Method for user sign up. Http method type is Post, end point is "/user/signup", consumes json file and produces out put in json format.
     * Checks for the existing username and email id if not available throws SignUpRestrictedException otherwise creates user entity in the database.
     * @param signupUserRequest
     * @return SignupUserResponse
     * @throws SignUpRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signup" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> usersignup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setRole("nonadmin");


        final UserEntity createdUserEntity = userBusinessService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

    /**
     * Method for user sign in. Http method type is Post, end point is "/user/signin", consumes json file and produces out put in json format.
     * Creates access token using base 64.
     * Checks for the username and password if not available throws AuthenticationFailedException otherwise creates entry in the user authentication table.
     * @return SigninResponse
     * @throws AuthenticationFailedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signin" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> usersignin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthEntity userAuthEntity = userBusinessService.signIn(decodedArray[0], decodedArray[1]);
        UserEntity user_Auth_entity = userAuthEntity.getUser();
        SigninResponse signinResponse = new SigninResponse().id(user_Auth_entity.getUuid()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthEntity.getAccessToken());

        return new ResponseEntity<SigninResponse>(signinResponse,headers,HttpStatus.OK);
    }

    /**
     * Method for user sign out. Http method is POST, end point is "/user/signout", takes json file as input and produces json file as output.
     * Checks whether user is signedin or not and if not signed in then throws SignOutRestrictedException exception
     * @param authorization
     * @return SignoutResponse
     * @throws SignOutRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signout" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> usersignout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        String[] bearerToken = authorization.split("Bearer ");
        UserEntity user_Auth_entity = userBusinessService.signOut(bearerToken[1]);

        SignoutResponse finalsignoutResponse = new SignoutResponse().id(user_Auth_entity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(finalsignoutResponse, HttpStatus.OK);

    }
}
