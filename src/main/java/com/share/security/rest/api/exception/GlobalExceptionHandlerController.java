package com.share.security.rest.api.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException ex, HttpServletRequest request) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now(), "status", HttpStatus.CONFLICT.value(), "error", HttpStatus.CONFLICT.getReasonPhrase(), "message", ex.getMessage(), "trace", getStackTrace(ex), "path", request.getRequestURI()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now(), "status", HttpStatus.NOT_FOUND.value(), "error", HttpStatus.NOT_FOUND.getReasonPhrase(), "message", ex.getMessage(), "trace", getStackTrace(ex), "path", request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return new ResponseEntity<>(Map.of("timestamp", LocalDateTime.now(), "status", HttpStatus.FORBIDDEN.value(), "error", HttpStatus.FORBIDDEN.getReasonPhrase(), "message", ex.getMessage(), "trace", getStackTrace(ex), "path", request.getRequestURI()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException iex = (InvalidFormatException) cause;
            String fieldName = getFieldName(iex);
            String enumValues = getEnumValues(iex.getTargetType());
            String message = String.format("Invalid value for field '%s'. Expected one of: %s", fieldName, enumValues);
            return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.BAD_REQUEST, "statusCode", HttpStatus.BAD_REQUEST.value(), "errorMessage", message, "success", false, "timestamp", LocalDateTime.now().toString(), "path", getFullURL(request)), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("status", HttpStatus.BAD_REQUEST, "statusCode", HttpStatus.BAD_REQUEST.value(), "errorMessage", "Malformed JSON request", "success", false, "timestamp", LocalDateTime.now().toString(), "path", getFullURL(request)), HttpStatus.BAD_REQUEST);
    }

    private String getStackTrace(Throwable throwable) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }

    private String getFullURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        if (queryString != null) {
            return String.format("%s://%s:%d%s?%s", scheme, serverName, serverPort, requestURI, queryString);
        } else {
            return String.format("%s://%s:%d%s", scheme, serverName, serverPort, requestURI);
        }
    }

    private String getFieldName(InvalidFormatException ex) {
        return ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).findFirst().orElse("unknown");
    }

    private String getEnumValues(Class<?> targetType) {
        if (targetType.isEnum()) {
            Class<Enum<?>> enumClass = (Class<Enum<?>>) targetType;
            return Stream.of(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.joining(", "));
        }
        return "";
    }


}