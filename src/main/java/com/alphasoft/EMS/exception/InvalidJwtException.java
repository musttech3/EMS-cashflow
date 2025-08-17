package com.alphasoft.EMS.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String message) {
        super (message);
    }

}
