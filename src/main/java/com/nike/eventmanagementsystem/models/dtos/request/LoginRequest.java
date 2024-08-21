package com.nike.eventmanagementsystem.models.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be a minimum of 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]).*$", message =
            "Password must contain at least one uppercase letter and one special character")
    private String password;


    @NotEmpty(message = "Email must not be empty")
    @NonNull
    @Email(message = "Email must be in an email format")
    private String emailAddress;
}
