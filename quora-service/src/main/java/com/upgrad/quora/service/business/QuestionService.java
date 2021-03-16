package com.upgrad.quora.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

}
