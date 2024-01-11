package com.cmhk.ztpayment.exceptions;

public class MerchantMethodNotFoundException extends RuntimeException{
    
    public MerchantMethodNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public MerchantMethodNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
