package com.cmhk.ztpayment.utils;

import static com.cmhk.ztpayment.utils.ThreadLocalUtils.MESSAGE_ID_THEADLOCAL;

/**
 * 请求信息工具类
 *
 * @author LSY
 * @since 2020/7/13
 */
public class RequestUtils {

    private RequestUtils(){
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * 存放当前请求的messageId
     *
     * @param messageId 请求标识
     */
    public static void setMessageId(String messageId){
        MESSAGE_ID_THEADLOCAL.set(messageId);
    }

    /**
     * 获取当前请求的messageId
     *
     * @return messageId，即请求标识
     */
    public static String getMessageId(){
        return MESSAGE_ID_THEADLOCAL.get();
    }

    /** 移除当前请求的messageId */
    public static void removeMessageId(){
        MESSAGE_ID_THEADLOCAL.remove();
    }


}
