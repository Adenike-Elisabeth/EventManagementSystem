package com.nike.eventmanagementsystem.services.impl;

import com.nike.eventmanagementsystem.exceptions.UserAlreadyExistsException;
import com.nike.eventmanagementsystem.models.dtos.request.SignupRequest;
import com.nike.eventmanagementsystem.models.dtos.response.SignupResponse;
import com.nike.eventmanagementsystem.models.entities.User;
import com.nike.eventmanagementsystem.models.enums.UserType;
import com.nike.eventmanagementsystem.repositories.UserRepository;
import com.nike.eventmanagementsystem.security.JwtService;
import com.nike.eventmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public SignupResponse registerUser(SignupRequest request) throws UserAlreadyExistsException {
        log.info("register Customer started");
        Optional<User> existingUser = userRepository.findByEmailAddress(request.getEmailAddress());


        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmailAddress() + " already exists");
        } else {

            User user = createUser(request);
            User savedUser = userRepository.save(user);
            SignupResponse.UserDto userDto = new SignupResponse.UserDto(savedUser);
            log.info("Register User ended");
            return new SignupResponse("User saved", userDto);
        }
    }

    private User createUser(SignupRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setEnabled(false);
        user.setEmailAddress(request.getEmailAddress());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(UserType.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }
}
