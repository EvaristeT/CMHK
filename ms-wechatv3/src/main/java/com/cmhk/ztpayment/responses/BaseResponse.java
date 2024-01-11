package com.cmhk.ztpayment.responses;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 基础出参类
 *
 * @author LSY
 * @since 2020/7/13
 */
@Data
@JsonInclude(Include.NON_NULL)
public class BaseResponse {

    /** 用于标识本次请求 */
    private String uuid = UUID.randomUUID().toString();

    // https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'Z")
    private Date datetime = new Date();


    /** 返回编码 */
    private String returnCode;
    /** 返回描述 英文*/
    private String returnDesc;
    /** 详细描述 */
    private String suppleDesc;

    private JSONObject resources;    

    /**
     * 返回成功
     *
     * @return 成功
     */
    public static BaseResponse ofSuccess(){
        return ofResponseEnum(null, ResponseEnum.SUCCESS);
    }

    /**
     * 根据服务器返回枚举及补充描述设置返回编码及返回描述
     * @param responseEnum 服务器返回枚举
     * @param suppleDesc 补充描述,单纯的补充描述，不做任何处理
     */
    public static BaseResponse ofResponseEnum(ResponseEnum responseEnum, String suppleDesc){
        return ofResponseEnum(null, responseEnum, suppleDesc);
    }

    /**
     * 根据服务器返回枚举及补充描述设置返回编码及返回描述
     * @param jsonObject 业务返回数据
     * @param responseEnum 服务器返回枚举
     * @param suppleDesc 补充描述,单纯的补充描述，不做任何处理
     */
    public static BaseResponse ofResponseEnum(JSONObject jsonObject, ResponseEnum responseEnum){
        return ofResponseEnum(jsonObject, responseEnum, null);
    }

    /**
     * 根据服务器返回枚举及补充描述设置返回编码及返回描述
     * @param jsonObject 业务返回数据
     * @param responseEnum 服务器返回枚举
     * @param suppleDesc 补充描述,单纯的补充描述，不做任何处理
     */
    public static BaseResponse ofResponseEnum(JSONObject jsonObject, ResponseEnum responseEnum, String suppleDesc){
        if (Objects.isNull(responseEnum)) {
            throw new IllegalArgumentException("param responseEnum of method ofResponseEnum can not be null");
        }
        
        BaseResponse baseResponse = new BaseResponse();
        
        baseResponse.returnCode = responseEnum.getReturnCode();
        baseResponse.returnDesc =  responseEnum.getReturnDescEn();
        baseResponse.suppleDesc = ( suppleDesc != null && !suppleDesc.isEmpty() ) ? suppleDesc : responseEnum.getReturnDescEn();

        if( jsonObject != null ){
            baseResponse.setResources(jsonObject);
        }

        return baseResponse;
    }
}
