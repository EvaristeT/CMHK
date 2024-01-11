package com.cmhk.ztpayment.exceptions;

public class InvalidParamException extends RuntimeException{
    
    public InvalidParamException(String errorMessage){
        super(errorMessage);
    }

    public InvalidParamException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
