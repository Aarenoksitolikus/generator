package ru.dev.numbers.generator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberException extends RuntimeException {
    public NumberException(String message) {
        super(message);
    }
}
