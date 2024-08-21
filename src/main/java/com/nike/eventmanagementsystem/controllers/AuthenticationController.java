package com.nike.eventmanagementsystem.controllers;

import com.nike.eventmanagementsystem.exceptions.UserAlreadyExistsException;
import com.nike.eventmanagementsystem.exceptions.UserNotFoundException;
import com.nike.eventmanagementsystem.exceptions.WrongUserTypeException;
import com.nike.eventmanagementsystem.models.dtos.request.LoginRequest;
import com.nike.eventmanagementsystem.models.dtos.request.SignupRequest;
import com.nike.eventmanagementsystem.models.dtos.response.ApiResponse;
import com.nike.eventmanagementsystem.models.dtos.response.LoginResponse;
import com.nike.eventmanagementsystem.models.dtos.response.SignupResponse;
import com.nike.eventmanagementsystem.services.AuthenticationService;
import com.nike.eventmanagementsystem.services.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<SignupResponse>> RegisterUser(@RequestBody @Valid @NonNull SignupRequest request) throws UserNotFoundException, UserAlreadyExistsException {
        log.info("++++++++++++++++++++++++++++++++++++====== got to controller");
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.registerUser(request)));
    }

    @PostMapping("/user-login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) throws UserNotFoundException, WrongUserTypeException {
        log.info("Login being called here");
        return ResponseEntity.ok(new ApiResponse<>(authenticationService.login(request)));
    }

}
