package com.cmhk.ztpayment.data.repository;

import com.cmhk.ztpayment.data.entity.PaymentRequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("PaymentRequestRepository")
public interface PaymentRequestRepository extends CrudRepository<PaymentRequest, Long> {
    boolean existsPaymentRequestByMerchantMethodIdAndOrderRef(String methodId, String outTradeNo);

    PaymentRequest findFirstByMerchantMethodIdAndOrderRef(String methodId, String outTradeNo);
}
