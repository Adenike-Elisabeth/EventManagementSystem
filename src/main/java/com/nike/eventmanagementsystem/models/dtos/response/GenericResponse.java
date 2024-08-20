package com.nike.eventmanagementsystem.models.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse {
    private String message;
    private Object data;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
