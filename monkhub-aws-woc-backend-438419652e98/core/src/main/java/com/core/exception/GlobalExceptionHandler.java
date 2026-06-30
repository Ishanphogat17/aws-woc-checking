package com.core.exception;

import com.core.constant.ExceptionConstant;
import com.core.dto.response.ErrorResponseDto;
import com.core.dto.response.ResponseDto;
import com.core.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(ExceptionConstant.EXCEPTION_OCCURRED, this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());

        Map<String, Object> errors = new LinkedHashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().status(false).message("All required fields are mandatory").data(errors).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> generateException(Exception e, WebRequest request) {
        log.error(ExceptionConstant.EXCEPTION_OCCURRED, this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e.getMessage());

        ErrorResponseDto responseDto = ErrorResponseDto.builder().timestamp(DateUtility.getZonedDateTime().toString()).status(false)
                .message(e.getMessage()).path(request.getDescription(false).replace("uri=", "")).build();

        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> generateResponseStatusException(ResponseStatusException e, WebRequest request) {
        String className = this.getClass().getSimpleName();

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        if (Objects.isNull(e.getCause())) {
            log.error(ExceptionConstant.EXCEPTION_OCCURRED, className, methodName, e.getReason());
        } else {
            log.error(ExceptionConstant.EXCEPTION_OCCURRED_CAUSE, className, methodName, e.getReason(), e.getCause().getMessage());
        }

        ErrorResponseDto responseDto = ErrorResponseDto.builder().timestamp(DateUtility.getZonedDateTime().toString()).status(false)
                .message(e.getReason()).path(request.getDescription(false).replace("uri=", "")).build();

        return ResponseEntity.status(e.getStatusCode()).body(responseDto);
    }
}
