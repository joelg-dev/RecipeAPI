package io.cd21.recipeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParameterException extends Exception{

    public InvalidParameterException(String message, String parameter){
        super(message.formatted(parameter));
    }
}
