package com.cmhk.ztpayment.exceptions;

public class UnexpectedWeChatResponseExcpetion extends RuntimeException{
    
    public UnexpectedWeChatResponseExcpetion(String errorMessage){
        super(errorMessage);
    }

    public UnexpectedWeChatResponseExcpetion(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
