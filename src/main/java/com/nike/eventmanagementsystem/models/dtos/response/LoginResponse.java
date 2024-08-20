package com.nike.eventmanagementsystem.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String userID;
    private String accessToken;
    private String refreshToken;
    private String role;
    private Date expiredAt;
}
