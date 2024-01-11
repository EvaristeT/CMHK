package com.cmhk.ztpayment.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.constant.WeChatPayStateEnum;
import com.cmhk.ztpayment.data.entity.PaymentRequest;
import com.cmhk.ztpayment.data.repository.PaymentRequestRepository;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;
import com.cmhk.ztpayment.services.GetDatabaseService;
import com.cmhk.ztpayment.services.UpdatePaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class UpdatePaymentServiceImpl implements UpdatePaymentService{
    private String methodId;

    @Autowired
    GetDatabaseService getDatabaseService;

    @Autowired
    PaymentRequestRepository paymentRequestRepository;

    @Override
    public void addPayment(WeChatTransactionRequest request) {
        //===== Get input variable =====
        String uuid = UUID.randomUUID().toString();
        String outTradeNo = request.getResources().getOut_trade_no();
        Double amount = Double.valueOf(request.getResources().getAmount().getTotal());
        String currency = request.getResources().getAmount().getCurrency();
        Date createAt = new Date();
        Date updateAt = new Date();

        //===== Check record exists or not =====
        boolean exists = getDatabaseService.checkPaymentExists(methodId, outTradeNo);

        log.info("status = PENDING, uuid = {}, methodId = {}, outTradeNo = {}, exists = {}", uuid, methodId, outTradeNo, exists);

        //===== Update duplicated record if exists, else set new id =====
        PaymentRequest paymentRequest = new PaymentRequest();

        if (exists) {
            paymentRequest = getDatabaseService.getPaymentRequest(methodId, outTradeNo);
        } else {
            paymentRequest.setId(uuid);
            paymentRequest.setCreateAt(createAt);
        }

        //===== Save record =====
        paymentRequest.setMerchantMethodId(methodId);
        paymentRequest.setOrderNo(outTradeNo);
        paymentRequest.setOrderRef(outTradeNo);
        paymentRequest.setAmount(amount);
        paymentRequest.setRealAmount(amount);
        paymentRequest.setLang("zh_hk");
        paymentRequest.setStatus("PENDING");
        paymentRequest.setOrderDesc("");
        paymentRequest.setCurrency(currency);
        paymentRequest.setUpdateAt(updateAt);
        paymentRequest.setCheckCode("");

        paymentRequestRepository.save(paymentRequest);
    }

    @Override
    public void updatePayment(WeChatTransactionRequest request, JSONObject result) {
        //===== Get input variable =====
        String outTradeNo = request.getResources().getOut_trade_no();
        Date updateAt = new Date();

        String tradeState = result.getString("trade_state");
        String status = WeChatPayStateEnum.parseStatus(tradeState);

        //===== Check record exists or not =====
        boolean exists = getDatabaseService.checkPaymentExists(methodId, outTradeNo);

        log.info("tradeState = {}, status = {}, methodId = {}, outTradeNo = {}, exists = {}", tradeState, status, methodId, outTradeNo, exists);

        //===== Update record status if exists =====
        if (exists) {
            PaymentRequest paymentRequest = getDatabaseService.getPaymentRequest(methodId, outTradeNo);

            String currentStatus = paymentRequest.getStatus();

            log.info("currentStatus = {}, status = {}", currentStatus, status);

            if (!currentStatus.equals(status)) {
                paymentRequest.setStatus(status);
                paymentRequest.setUpdateAt(updateAt);

                paymentRequestRepository.save(paymentRequest);
            }
        }
    }

    @Override
    public void cancelPayment(WeChatTransactionRequest request) {
        //===== Get input variable =====
        String outTradeNo = request.getResources().getOut_trade_no();
        Date updateAt = new Date();

        //===== Check record exists or not =====
        boolean exists = getDatabaseService.checkPaymentExists(methodId, outTradeNo);

        log.info("status = FAILURE, methodId = {}, outTradeNo = {}, exists = {}", methodId, outTradeNo, exists);

        //===== Update record status if exists =====
        if (exists) {
            PaymentRequest paymentRequest = getDatabaseService.getPaymentRequest(methodId, outTradeNo);

            paymentRequest.setStatus("FAILURE");
            paymentRequest.setUpdateAt(updateAt);

            paymentRequestRepository.save(paymentRequest);
        }
    }

    @Override
    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }
}
