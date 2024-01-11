package com.cmhk.ztpayment.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import com.cmhk.ztpayment.responses.BaseResponse;
import com.cmhk.ztpayment.responses.ResponseEnum;

import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Order(-1)
public class GlobalExceptionHandler {
    
    /**
     * 入参校验异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse paramsError(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        String errorMsg = errorList.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));

        return BaseResponse.ofResponseEnum(ResponseEnum.INVALID_PARAMS, errorMsg);
    }

    /**
     * 参数绑定异常返回
     * @param e 参数绑定异常
     * @return 响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseResponse paramsError(BindException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        String errorMsg = errorList.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        return BaseResponse.ofResponseEnum(ResponseEnum.INVALID_PARAMS, errorMsg);
    }

    /**
     * WeChat 證書錯誤
     * @param e
     * @return
     */
    @ExceptionHandler(WeChatCertificateException.class)
    @ResponseBody
    public BaseResponse paramsError(WeChatCertificateException e) {

        return BaseResponse.ofResponseEnum(ResponseEnum.CERT_ERROR, e.getMessage());
    }

    /**
     * WeChat 證書錯誤
     * @param e
     * @return
     */
    @ExceptionHandler(UnexpectedWeChatResponseExcpetion.class)
    @ResponseBody
    public BaseResponse paramsError(UnexpectedWeChatResponseExcpetion e) {

        return BaseResponse.ofResponseEnum(ResponseEnum.ERROR_ORDER_WECHAT, e.getMessage());
    }

    /**
     * 簽名校验异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidParamException.class)
    @ResponseBody
    public BaseResponse paramError(InvalidParamException e) {
        return BaseResponse.ofResponseEnum(ResponseEnum.INVALID_PARAMS, e.getMessage());
    }
    
    /**
     * 簽名校验异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidSignatureException.class)
    @ResponseBody
    public BaseResponse signatureError(InvalidSignatureException e) {
        return BaseResponse.ofResponseEnum(ResponseEnum.SIGNATURE_INVALID, e.getMessage());
    }

    /**
     * 商户不存在异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(MerchantNotFoundException.class)
    @ResponseBody
    public BaseResponse merchantNotFoundError(MerchantNotFoundException e) {
        return BaseResponse.ofResponseEnum(ResponseEnum.MERCHANTS_NOT_EXIST, e.getMessage());
    }

    /**
     * 商户付款帳戶不存在异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(MerchantMethodNotFoundException.class)
    @ResponseBody
    public BaseResponse merchantMethodNotFoundError(MerchantMethodNotFoundException e) {
        return BaseResponse.ofResponseEnum(ResponseEnum.MERCHANTS_METHOD_NOT_EXIST, e.getMessage());
    }

    /**
     * 付款參數不存在异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(AppIdNotFoundException.class)
    @ResponseBody
    public BaseResponse appIdNotFoundError(AppIdNotFoundException e) {
        return BaseResponse.ofResponseEnum(ResponseEnum.APP_ID_NOT_EXIST, e.getMessage());
    }

    /**
     * 入参校验异常返回
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse serverError(RuntimeException e) {
        log.error("系统抛出异常", e);
        return BaseResponse.ofResponseEnum(ResponseEnum.SERVER_ERROR, e.getMessage());
    }   
    
}
