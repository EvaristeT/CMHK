package com.cmhk.ztpayment.services;

import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;

public interface UpdatePaymentService {
    public abstract void addPayment(WeChatTransactionRequest request);
    public abstract void updatePayment(WeChatTransactionRequest request, JSONObject result);
    public abstract void cancelPayment(WeChatTransactionRequest request);

    public abstract void setMethodId(String methodId);
}
