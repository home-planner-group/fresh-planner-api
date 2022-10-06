package com.freshplanner.api.controller;

import com.freshplanner.api.controller.model.ApiError;
import com.freshplanner.api.exception.ElementNotFoundException;
import com.freshplanner.api.utility.ApiLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleArgumentException(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadableMessage(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessException(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthException(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ApiError> handleElementException(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        ApiError apiErrorResponse = new ApiError(
                HttpStatus.BAD_REQUEST, request.getRequestURI(),
                exception,
                exception.getBindingResult().getAllErrors());
        return buildResponse(apiErrorResponse, exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUncaught(HttpServletRequest request, Exception exception) {
        return handleDefaultException(request, exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //==================================================================================================================

    private ResponseEntity<ApiError> handleDefaultException(HttpServletRequest request, Exception exception, HttpStatus status) {
        ApiError apiErrorResponse = new ApiError(
                status,
                request.getRequestURI(),
                exception);
        return buildResponse(apiErrorResponse, exception);
    }

    private ResponseEntity<ApiError> buildResponse(ApiError apiError, Exception exception) {
        String message = "Exception '" + apiError.getReason()
                + "' with status '" + apiError.getStatus().toString()
                + "' on path '" + apiError.getRequestPath() + "': "
                + apiError.getMessage();
        if (apiError.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            ApiLogger.error(message);
            exception.printStackTrace();
        } else {
            ApiLogger.warning(message);
        }
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
