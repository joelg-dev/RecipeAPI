package io.cd21.recipeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidRequestException extends Exception{

    public InvalidRequestException(String message){
        super("Invalid request: " + message);
    }
}
