package com.nike.eventmanagementsystem.services.impl;

import com.nike.eventmanagementsystem.exceptions.UserAlreadyExistsException;
import com.nike.eventmanagementsystem.exceptions.UserNotFoundException;
import com.nike.eventmanagementsystem.exceptions.WrongUserTypeException;
import com.nike.eventmanagementsystem.models.dtos.request.LoginRequest;
import com.nike.eventmanagementsystem.models.dtos.request.SignupRequest;
import com.nike.eventmanagementsystem.models.dtos.response.LoginResponse;
import com.nike.eventmanagementsystem.models.dtos.response.SignupResponse;
import com.nike.eventmanagementsystem.models.entities.User;
import com.nike.eventmanagementsystem.models.enums.UserType;
import com.nike.eventmanagementsystem.repositories.UserRepository;
import com.nike.eventmanagementsystem.security.JwtService;
import com.nike.eventmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    @Override
    public SignupResponse registerUser(SignupRequest request) throws UserAlreadyExistsException {
        log.info("register Customer started");
        Optional<User> existingUser = userRepository.findByEmailAddress(request.getEmailAddress());


        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmailAddress() + " already exists");
        } else {

            User user = createUser(request);
            // Optional: Validate user role
            if (!isValidRole(user.getRole())) {
                throw new IllegalArgumentException("Invalid user role provided.");
            }

            User savedUser = userRepository.save(user);
            SignupResponse.UserDto userDto = new SignupResponse.UserDto(savedUser);
            log.info("Register User ended");
            return new SignupResponse("User saved", userDto);

        }
    }


    private boolean isValidRole(UserType role) {
        // Check if the role is one of the defined enums
        return role == UserType.USER || role == UserType.ENTERTAINER;
    }

    private User createUser(SignupRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        //user.setEnabled(false);
        user.setEmailAddress(request.getEmailAddress());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(UserType.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

@Transactional
@Override
    public LoginResponse login(LoginRequest request) throws UserNotFoundException, WrongUserTypeException  {

    log.info("Login request received: email={}, password={}", request.getEmailAddress(), request.getPassword());


    if (request.getEmailAddress() == null || request.getEmailAddress().isEmpty()) {
        throw new UserNotFoundException("Email address cannot be empty.");
    }

    User user = userRepository.findByEmailAddress(request.getEmailAddress())
            .orElseThrow(() -> new UserNotFoundException("User with email: " + request.getEmailAddress() + " not found"));

        validateUser(user);
        userRepository.save(user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailAddress(),
                        request.getPassword()
                )
        );

//            authenticateSpringSecurity(request.getEmail(), request.getPassword());

        String jwtToken = jwtService.generateToken(user);
        String jwtRefToken = jwtService.generateRefreshToken(user);

        return createLoginResponse(user, jwtToken, jwtRefToken);
    }

    private LoginResponse createLoginResponse(User user, String jwtToken, String jwtRefToken) {
        return LoginResponse.builder()
                .userID(Objects.requireNonNull(user.getUserId()).toString())
                .accessToken(jwtToken)
                .refreshToken(jwtRefToken)
                .expiredAt(jwtService.extractExpiration(jwtToken))
                .role(user.getRole().toString())
                .build();
    }

    private void validateUser(User user) throws WrongUserTypeException {
        if (user.getRole() != UserType.USER) {
            throw new WrongUserTypeException("Abegiii, you no suppose dey here");
        }
    }

    }
