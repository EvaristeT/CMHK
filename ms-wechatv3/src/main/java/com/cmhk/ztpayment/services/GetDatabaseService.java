package com.cmhk.ztpayment.services;

import com.cmhk.ztpayment.data.entity.Merchant;
import com.cmhk.ztpayment.data.entity.MerchantMethod;
import com.cmhk.ztpayment.data.entity.PaymentParam;
import com.cmhk.ztpayment.data.entity.PaymentRequest;

public interface GetDatabaseService {
    public abstract Merchant getMerchant(String merchantCode);
    public abstract MerchantMethod getMerchantMethod(String merchantId, String merchantMethodId);
    public abstract PaymentParam getPaymentParam(String accountId, String code);
    public abstract PaymentRequest getPaymentRequest(String methodId, String outTradeNo);
    
    public abstract boolean checkPaymentExists(String methodId, String outTradeNo);
}
