package com.nike.eventmanagementsystem.services;

import com.nike.eventmanagementsystem.exceptions.UserAlreadyExistsException;
import com.nike.eventmanagementsystem.exceptions.UserNotFoundException;
import com.nike.eventmanagementsystem.exceptions.WrongUserTypeException;
import com.nike.eventmanagementsystem.models.dtos.request.LoginRequest;
import com.nike.eventmanagementsystem.models.dtos.request.SignupRequest;
import com.nike.eventmanagementsystem.models.dtos.response.LoginResponse;
import com.nike.eventmanagementsystem.models.dtos.response.SignupResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {
    SignupResponse registerUser(SignupRequest request) throws UserNotFoundException, UserAlreadyExistsException;

    @Transactional
    LoginResponse login(LoginRequest request) throws UserNotFoundException, WrongUserTypeException;
}
