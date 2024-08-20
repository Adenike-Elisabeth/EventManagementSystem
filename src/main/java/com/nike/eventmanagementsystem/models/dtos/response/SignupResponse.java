package com.nike.eventmanagementsystem.models.dtos.response;

import com.nike.eventmanagementsystem.models.entities.User;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private String status;

    private String message;

    private UserDto userDto;

    private TokenResponse data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {

        private String firstName;

        private String lastName;

        private String emailAddress;

        private String phoneNumber;

        public UserDto(User user) {
            this.phoneNumber = user.getPhoneNumber();
            this.emailAddress = user.getEmailAddress();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();

        }


    }
}
