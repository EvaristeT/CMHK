package com.cmhk.ztpayment.aspects.impl;

import java.util.HashMap;
import java.util.Map;

import com.cmhk.ztpayment.exceptions.InvalidParamException;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CheckResourcesAspect {
    private WeChatTransactionRequest request;

    @Before(value = "@annotation(com.cmhk.ztpayment.aspects.CheckResources)")
    public void preHandler(JoinPoint joinPoint) throws InvalidParamException {
        getValues(joinPoint);

        String appId = request.getResources().getAppid();
        String mchId = request.getResources().getMchid();
        String notifyUrl = request.getResources().getNotify_url();

        log.info("request appId: {}, mchId: {}, notify url: {}", appId, mchId, notifyUrl);

        //===== Check app id value =====
        if (appId != null && !appId.isEmpty()) {
            throw new InvalidParamException("Not allow to input app id");
        }

        //===== Check mch id value =====
        if (mchId != null && !mchId.isEmpty()) {
            throw new InvalidParamException("Not allow to input merchant id");
        }

        //===== Check url value =====
        if (notifyUrl != null && !notifyUrl.isEmpty()) {
            throw new InvalidParamException("Not allow to input notify url");
        }

        log.info("CheckResourcesAspect success");
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

            if ( paramName.equals("request") ) {
                request = (WeChatTransactionRequest) paramValue;
            }
        });
    }
}
