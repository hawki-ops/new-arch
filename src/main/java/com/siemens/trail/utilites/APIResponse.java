package com.siemens.trail.utilites;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse<T> {
    boolean success;
    T data;
    String message;
    int statusCode;

    public APIResponse(T data) {
        success = true;
        statusCode = 200;
        this.data = data;
    }

    public APIResponse(String message, int statusCode) {
        success = false;
        this.message = message;
        this.statusCode = statusCode;
    }
}
