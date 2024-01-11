package com.cmhk.ztpayment.controllers;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.cmhk.ztpayment.aspects.CheckResources;
import com.cmhk.ztpayment.aspects.VerifySignature;
import com.cmhk.ztpayment.config.WeChatMerchantProp;
import com.cmhk.ztpayment.data.entity.Merchant;
import com.cmhk.ztpayment.data.entity.MerchantMethod;
import com.cmhk.ztpayment.data.entity.PaymentParam;
import com.cmhk.ztpayment.exceptions.UnexpectedWeChatResponseExcpetion;
import com.cmhk.ztpayment.requests.api.WeChatTransactionRequest;
import com.cmhk.ztpayment.responses.BaseResponse;
import com.cmhk.ztpayment.responses.ResponseEnum;
import com.cmhk.ztpayment.responses.schema.CheckResponseSchema;
import com.cmhk.ztpayment.responses.schema.CloseResponseSchema;
import com.cmhk.ztpayment.responses.schema.NativeResponseSchema;
import com.cmhk.ztpayment.services.GetDatabaseService;
import com.cmhk.ztpayment.services.UpdatePaymentService;
import com.cmhk.ztpayment.services.WeChatV3Service;

import com.cmhk.ztpayment.data.repository.MerchantMethodRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping(value = "/api/wechatv3/{merchantCode}/transaction")
@Tag(name = "訂單類接口", description = "")
// @NacosPropertySource(dataId = "wechat-v3", groupId = "zt-payment", autoRefreshed = true)
public class ApiController {

    @Autowired
    WeChatV3Service weChatV3Service;

    @Autowired
    WeChatMerchantProp weChatMerchantProp;

    @Autowired
    GetDatabaseService getDatabaseService;

    @Autowired
    UpdatePaymentService updatePaymentService;

    @Autowired
    MerchantMethodRepository merchantMethodRepository;


    @Operation(
        summary="查詢訂單",description="請參考： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml", tags = {"WeChat API"}
        , externalDocs=@ExternalDocumentation(url="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckResponseSchema.class)))
    })
    @PostMapping(value = "check")
    @ResponseBody
    @CheckResources
    @VerifySignature
    public BaseResponse transactionCheck( 
        @PathVariable("merchantCode") String merchantCode,
        @Valid @RequestBody WeChatTransactionRequest request
     ) throws UnexpectedWeChatResponseExcpetion, InterruptedException{
        //===== Call common process =====
        commonProcess(merchantCode, request.getMerchantMethodId());

        //===== Call transactionCheck() service =====
        JSONObject result = weChatV3Service.transactionCheck(request);

        //===== Call updatePayment() service after no error from transactionCheck() =====
        updatePaymentService.updatePayment(request, result);

        //===== Call transactionCheck() service =====
        return BaseResponse.ofResponseEnum(result, ResponseEnum.SUCCESS);
    }


    @Operation(summary="關閉訂單", description="請參考： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_3.shtml", tags = {"WeChat API"}
        , externalDocs=@ExternalDocumentation(url="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_3.shtml")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CloseResponseSchema.class)) })
    })
    @PostMapping(value = "close")
    @ResponseBody
    @CheckResources
    @VerifySignature
    public BaseResponse transactionClose( 
        @PathVariable("merchantCode") String merchantCode,
        @Valid @RequestBody WeChatTransactionRequest request
     ) throws UnexpectedWeChatResponseExcpetion, InterruptedException{
        //===== Call common process =====
        commonProcess(merchantCode, request.getMerchantMethodId());

        //===== Call transactionClose() service =====
        JSONObject result = weChatV3Service.transactionClose(request);

        //===== Call cancelPayment() service after no error from transactionClose() =====
        updatePaymentService.cancelPayment(request);

        //===== Return result =====
        return BaseResponse.ofResponseEnum(result, ResponseEnum.SUCCESS);
    }


    @Operation(summary="Native下單", description="用於生成 QR Code. 請參考： https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_1.shtml", tags = {"WeChat API"}
    , externalDocs=@ExternalDocumentation(url="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_1.shtml")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = NativeResponseSchema.class)) })
    })
    @PostMapping(value = "native")
    @ResponseBody
    @CheckResources
    @VerifySignature
    public BaseResponse transactionNative( 
        @PathVariable("merchantCode") String merchantCode,
        @Valid @RequestBody WeChatTransactionRequest request
    ) throws UnexpectedWeChatResponseExcpetion, InterruptedException {
        //===== Call common process =====
        commonProcess(merchantCode, request.getMerchantMethodId());

        //===== Add default values =====
        if( StringUtils.isEmpty(request.getResources().getMerchant_category_code()) ){
            request.getResources().setMerchant_category_code("4815");
        }
        if( StringUtils.isEmpty(request.getResources().getTrade_type()) ){
            request.getResources().setTrade_type("NATIVE");
        }

        //===== Call transactionNative() service =====
        JSONObject result = weChatV3Service.transactionNative(request);

        //===== Call addPayment() service after no error from transactionNative() =====
        updatePaymentService.addPayment(request);

        //===== Return result =====
        return BaseResponse.ofResponseEnum(result, ResponseEnum.SUCCESS);
    }


    private void commonProcess(String merchantCode, String merchantMethodId) {
        //===== Find we chat pay cert by app id and mch id =====
        Merchant merchant = getDatabaseService.getMerchant(merchantCode);
        String merchantId = merchant.getId();

        MerchantMethod merchantMethod = getDatabaseService.getMerchantMethod(merchantId, merchantMethodId);
        String accountId = merchantMethod.getAccountId();
        String methodId = merchantMethod.getId();

        PaymentParam paymentParam = getDatabaseService.getPaymentParam(accountId, "appid");
        String appId = paymentParam.getValue();

        paymentParam = getDatabaseService.getPaymentParam(accountId, "mchid");
        String mchId = paymentParam.getValue();

        weChatV3Service.setProperties(weChatMerchantProp, appId, mchId);

        updatePaymentService.setMethodId(methodId);
    }
}