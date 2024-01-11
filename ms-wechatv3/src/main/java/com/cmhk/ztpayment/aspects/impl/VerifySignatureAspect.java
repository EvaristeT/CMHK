package com.cmhk.ztpayment.aspects.impl;

import java.util.HashMap;
import java.util.Map;

import com.cmhk.ztpayment.data.entity.Merchant;
import com.cmhk.ztpayment.data.entity.MerchantMethod;
import com.cmhk.ztpayment.data.entity.PaymentParam;
import com.cmhk.ztpayment.exceptions.AppIdNotFoundException;
import com.cmhk.ztpayment.exceptions.InvalidParamException;
import com.cmhk.ztpayment.exceptions.InvalidSignatureException;
import com.cmhk.ztpayment.exceptions.MerchantMethodNotFoundException;
import com.cmhk.ztpayment.exceptions.MerchantNotFoundException;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;
import com.cmhk.ztpayment.services.GetDatabaseService;
import com.cmhk.ztpayment.utils.Md5CheckerUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class VerifySignatureAspect {

    @Autowired
    private GetDatabaseService getDatabaseService; 

    private String merchantCode;
    private String uuid;

    private String signType;
    private String sign;

    private String merchantMethodId;

    @Before(value = "@annotation(com.cmhk.ztpayment.aspects.VerifySignature)")
    public void preHandler(JoinPoint joinPoint) throws InvalidParamException, InvalidSignatureException, MerchantNotFoundException, MerchantMethodNotFoundException, AppIdNotFoundException{

        getValues(joinPoint);

        log.info("Merchant Code: {}, Base Request: uuid->{} signType->{} sign->{}", merchantCode, uuid, signType, sign);

        //===== Check uuid or sign value =====
        if (uuid == null || uuid.isEmpty() || sign == null || sign.isEmpty()) {
            throw new InvalidParamException("uuid or sign are invalid");
        }


        //===== Check merchant key =====
        Merchant merchant = getDatabaseService.getMerchant(merchantCode);

        if (merchant == null) {
            throw new MerchantNotFoundException("Cannot find merchant");
        }

        String merchantKey = merchant.getMerchantKey();
        String merchantId = merchant.getId();

        if (merchantKey == null || merchantKey.isEmpty()) {
            throw new MerchantNotFoundException("Cannot find merchant key");
        }

        if (merchantId == null || merchantId.isEmpty()) {
            throw new MerchantNotFoundException("Cannot find merchant id");
        }

        log.info("merchantKey = {} ", merchantKey);


        //===== Find account id =====
        MerchantMethod merchantMethod = getDatabaseService.getMerchantMethod(merchantId, merchantMethodId);

        if (merchantMethod == null) {
            throw new MerchantMethodNotFoundException("Cannot find merchant method");
        }

        String accountId = merchantMethod.getAccountId();

        if (accountId == null || accountId.isEmpty()) {
            throw new MerchantMethodNotFoundException("Cannot find payment account");
        }

        log.info("accountId = {} ", accountId);


        //===== Check Md5 value =====
        if ( Md5CheckerUtils.check(merchantCode, merchantKey, uuid, sign) ) {
            log.info("MD5 Check true");
        } else {
            log.info("MD5 Check false");

            throw new InvalidSignatureException("MD5 are not matched");
        }

        
        //===== Check app id and mch id =====
        PaymentParam paymentParam = getDatabaseService.getPaymentParam(accountId, "appid");

        if (paymentParam == null) {
            throw new AppIdNotFoundException("Cannot find payment parameters");
        }

        String appId = paymentParam.getValue();

        paymentParam = getDatabaseService.getPaymentParam(accountId, "mchid");
        String mchId = paymentParam.getValue();
        
        if (appId == null || appId.isEmpty() || mchId == null || mchId.isEmpty()) {
            throw new AppIdNotFoundException("Cannot find the certificate of wechat pay");
        }

        log.info("appId = {} >>> mchId = {} ", appId, mchId);
    }

    /**
     * Get the parameter Map collection
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
 
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
 
        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
            log.info("Input: {}->{}", paramNames[i], paramValues[i]);
        }
 
        return param;
    }

    private void getValues(JoinPoint joinPoint){
        Map<String, Object> map = getNameAndValue(joinPoint);
        
        map.entrySet().forEach(entry -> {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();

            if( paramName.equals("merchantCode") ){
                merchantCode = (String) paramValue;
            }

            if( paramValue instanceof WeChatTransactionRequest ){
                WeChatTransactionRequest baseRequest = (WeChatTransactionRequest) paramValue;
                
                uuid = baseRequest.getUuid();
                signType = baseRequest.getSignType();
                sign = baseRequest.getSign();
                merchantMethodId = baseRequest.getMerchantMethodId();
            }
        });
    }

}
