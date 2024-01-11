package com.cmhk.ztpayment.services;

import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.config.WeChatMerchantProp;
import com.cmhk.ztpayment.config.WeChatMerchantProp.WeChatCertProp;
import com.cmhk.ztpayment.exceptions.UnexpectedWeChatResponseExcpetion;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;

public interface WeChatV3Service {
    
    public abstract JSONObject transactionNative(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException;
    public abstract JSONObject transactionCheck(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException;
    public abstract JSONObject transactionClose(WeChatTransactionRequest request) throws UnexpectedWeChatResponseExcpetion, InterruptedException;
    
    public abstract void setProperties(WeChatMerchantProp weChatMerchantProp, String appId, String mchId);
    public abstract void setProperties(WeChatMerchantProp weChatMerchantProp);
    public abstract void setMerchant(WeChatCertProp weChatCertProp);
}
