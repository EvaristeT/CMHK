package com.cmhk.ztpayment.exceptions;

public class WeChatCertificateException extends RuntimeException{
    
    public WeChatCertificateException(String errorMessage){
        super(errorMessage);
    }

    public WeChatCertificateException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
