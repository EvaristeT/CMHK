package com.cmhk.ztpayment.exceptions;

public class AppIdNotFoundException extends RuntimeException{

    public AppIdNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public AppIdNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
