package com.nike.eventmanagementsystem.models.dtos.request;

import com.nike.eventmanagementsystem.models.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    private String firstName;

    private String lastName;

    private String emailAddress;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\+234\\d{8,11}", message = "Phone number must start with '+234' and have 8 to 11 digits")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 12, message = "Password must be 8 to 12 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]).*$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;


}
