package com.cmhk.ztpayment.requests.api;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.cmhk.ztpayment.requests.BaseRequest;
import com.cmhk.ztpayment.requests.wechat.WXAppNativeOrderRequest;

import lombok.Data;

@Data
public class WeChatTransactionRequest implements BaseRequest{
    
    private String uuid;
    private Date datetime;

    @NotBlank(message=" `signType` is missed.")
    private String signType;

    @NotBlank(message=" `sign` is missed.")
    private String sign;

    private String merchantMethodId;

    private WXAppNativeOrderRequest resources;


}
