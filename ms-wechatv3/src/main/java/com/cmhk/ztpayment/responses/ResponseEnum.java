package com.cmhk.ztpayment.responses;

public enum ResponseEnum {
    
    /** 成功返回 */
    SUCCESS("0", "成功", "成功", "SUCCESS"),

    /* ------------------------通用返回码------------------------ */
    /** 入参错误 */
    INVALID_PARAMS("-101", "参数异常", "參數異常", "INVALID_PARAMS"),

    /** 数据不存在 */
    DATA_NOT_FOUND("-102","数据不存在", "數據不存在", "DATA_NOT_FOUND"),

    /** MD5錯誤 */
    SIGNATURE_INVALID("-103","签名错误", "簽名錯誤", "Signature Invalid"),

    /** 證書錯誤 */
    CERT_ERROR("-104","证书错误", "證書錯誤", "CERTIFICATE ERROR"),

    /** 服务器异常 */
    SERVER_ERROR("-500","服务器异常", "服務器異常", "SERVER ERROR"),

    /* ------------------------业务返回码-2xx ------------------------ */

    /** 微信下单失败 */
    ERROR_ORDER_WECHAT("-216", "微信下单失败", "微信下單失敗", "Failed to order wechat"),

    /* ------------------------商户返回定义-3xx ------------------------ */

    /** 商户被冻结*/
    MERCHANTS_FROZEN("-300", "商户已被冻结", "商戶已被凍結", "Merchants have been frozen"),

    /** 商户不存在*/
    MERCHANTS_NOT_EXIST("-301", "商户不存在", "商戶不存在", "Merchant does not exist"),

    /** 商户付款方式不存在*/
    MERCHANTS_METHOD_NOT_EXIST("-302", "商户付款方式不存在", "商户付款方式不存在", "Merchant method does not exist"),

    /** 付款參數不存在*/
    APP_ID_NOT_EXIST("-303", "付款参数不存在", "付款參數不存在", "Payment parameter does not exist");

    ResponseEnum(String returnCode, String returnDescSc, String returnDescTc, String returnDescEn){
        this.returnCode = returnCode;
        this.returnDescSc = returnDescSc;
        this.returnDescTc = returnDescTc;
        this.returnDescEn = returnDescEn;
    }

    private String returnCode;

    private String returnDescSc;

    private String returnDescTc;

    private String returnDescEn;

    public String getReturnCode(){
        return this.returnCode;
    }

    public String getReturnDescSc() {
        return returnDescSc;
    }

    public String getReturnDescTc() {
        return returnDescTc;
    }

    public String getReturnDescEn() {
        return returnDescEn;
    }
    
}
