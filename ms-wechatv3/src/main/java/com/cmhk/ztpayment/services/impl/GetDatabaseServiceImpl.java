package com.cmhk.ztpayment.services.impl;

import com.cmhk.ztpayment.constant.PayMethodEnum;
import com.cmhk.ztpayment.data.entity.Merchant;
import com.cmhk.ztpayment.data.entity.MerchantMethod;
import com.cmhk.ztpayment.data.entity.PaymentParam;
import com.cmhk.ztpayment.data.entity.PaymentRequest;
import com.cmhk.ztpayment.data.repository.MerchantMethodRepository;
import com.cmhk.ztpayment.data.repository.MerchantRepository;
import com.cmhk.ztpayment.data.repository.PaymentParamRepository;
import com.cmhk.ztpayment.data.repository.PaymentRequestRepository;
import com.cmhk.ztpayment.services.GetDatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GetDatabaseServiceImpl implements GetDatabaseService{
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMethodRepository merchantMethodRepository;

    @Autowired
    private PaymentParamRepository paymentParamRepository;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;
    
    private Merchant merchant;
    private MerchantMethod merchantMethod;
    private PaymentParam paymentParam;
    private PaymentRequest paymentRequest;

    public Merchant getMerchant(String merchantCode) {
        try {
            merchant = merchantRepository.findFirstByMerchantCodeAndIsEnableAndIsDelete(merchantCode, "ON", 0);
        } catch (Exception e) {
            log.info(e.toString());
        }

        return merchant;
    }

    public MerchantMethod getMerchantMethod(String merchantId, String merchantMethodId) {
        String methodCode = PayMethodEnum.WechatPay.toString();

        log.info("merchantId = {}, merchantMethodId = {}, methodCode = {}", merchantId, merchantMethodId, methodCode);

        try {
            if (merchantMethodId == null || merchantMethodId.isEmpty()) {
                //===== Use merchant_id and payment method code to find cc_payment_merchant_method record =====
                log.info("cc_payment_merchant_method >>> Use merchant_id and payment method code to find record");
                merchantMethod = merchantMethodRepository.findWithMerchantIdAndMethodCode(merchantId, methodCode);
            } else {
                //===== Use unique id to find cc_payment_merchant_method record =====
                log.info("cc_payment_merchant_method >>> Use unique id to find record");
                merchantMethod = merchantMethodRepository.findById(merchantMethodId);
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        return merchantMethod;
    }

    public PaymentParam getPaymentParam(String accountId, String code) {
        try {
            paymentParam = paymentParamRepository.findFirstByAccountIdAndNameAndIsDelete(accountId, code, 0);
        } catch (Exception e) {
            log.info(e.toString());
        }

        return paymentParam;
    }

    public PaymentRequest getPaymentRequest(String methodId, String outTradeNo) {
        try {
            paymentRequest = paymentRequestRepository.findFirstByMerchantMethodIdAndOrderRef(methodId, outTradeNo);
        } catch (Exception e) {
            log.info(e.toString());
        }

        return paymentRequest;
    }

    public boolean checkPaymentExists(String methodId, String outTradeNo) {
        return paymentRequestRepository.existsPaymentRequestByMerchantMethodIdAndOrderRef(methodId, outTradeNo);
    }
}
