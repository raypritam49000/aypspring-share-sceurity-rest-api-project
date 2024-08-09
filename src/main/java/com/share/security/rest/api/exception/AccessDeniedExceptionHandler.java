package com.share.security.rest.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    private final Logger LOGGER = Logger.getLogger(AccessDeniedExceptionHandler.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.info("@@@@@  Inside AccessDeniedExceptionHandler AccessDeniedException : " + accessDeniedException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("timestamp", LocalDateTime.now().toString(), "status", HttpStatus.UNAUTHORIZED.value(), "error", HttpStatus.UNAUTHORIZED.getReasonPhrase(), "message", accessDeniedException.getMessage(), "trace", getStackTrace(accessDeniedException), "path", request.getRequestURI())));
    }

    private String getStackTrace(Throwable throwable) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }
}
