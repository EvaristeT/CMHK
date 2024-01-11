package com.cmhk.ztpayment.exceptions;

public class MerchantNotFoundException extends RuntimeException{
    
    public MerchantNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public MerchantNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
