package com.cmhk.ztpayment.exceptions;

public class InvalidSignatureException extends RuntimeException{
    
    public InvalidSignatureException(String errorMessage){
        super(errorMessage);
    }

    public InvalidSignatureException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
