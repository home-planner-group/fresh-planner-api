package com.freshplanner.api.controller.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class ApiError {
    private final HttpStatus status;
    private final String requestPath;
    private final String timestamp;
    private final String reason;
    private final String message;
    private final List<String> errors;


    public ApiError(HttpStatus status, String requestPath, Exception exception, List<ObjectError> errorObjects) {
        super();
        this.status = status;
        this.requestPath = requestPath;
        this.timestamp = getCurrentTimeStamp();
        this.reason = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.errors = new ArrayList<>();
        errorObjects.forEach(objectError -> errors.add(objectError.toString()));
    }

    public ApiError(HttpStatus status, String requestPath, Exception exception) {
        super();
        this.status = status;
        this.requestPath = requestPath;
        this.timestamp = getCurrentTimeStamp();
        this.reason = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
        this.errors = Collections.singletonList("error occurred");
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
        Date now = new Date();
        return sdf.format(now);
    }
}
