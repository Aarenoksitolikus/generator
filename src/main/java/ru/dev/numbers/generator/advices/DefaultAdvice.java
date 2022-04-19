package ru.dev.numbers.generator.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.dev.numbers.generator.exceptions.ExceptionDto;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleUserNotFoundException(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(ExceptionDto.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(exception.getMessage())
                        .build());
    }
}
