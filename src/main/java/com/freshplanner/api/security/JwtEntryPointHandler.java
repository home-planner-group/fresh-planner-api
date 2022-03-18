package com.freshplanner.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshplanner.api.model.error.ApiError;
import com.freshplanner.api.utility.ApiLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtEntryPointHandler implements AuthenticationEntryPoint {

    /**
     * <h2>Exception Handling for {@link AuthenticationException}</h2>
     * <ul>
     *     <li>Exception Handler for {@link JwtRequestFilter} - handles errors before the 'GlobalExceptionHandler'.</li>
     *     <li>Sends a response with {@link HttpStatus#FORBIDDEN} with body as {@link ApiError}.</li>
     * </ul>
     *
     * @param request       from overwritten method
     * @param response      from overwritten method
     * @param authException from overwritten method
     * @throws IOException      from overwritten method
     * @throws ServletException from overwritten method
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ApiLogger.error("Security Exception: " + authException.getClass().getName());
        ApiLogger.error("Exception Message: " + authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        new ObjectMapper().writeValue(response.getOutputStream(), new ApiError(HttpStatus.FORBIDDEN, request.getRequestURI(), authException));
    }
}
