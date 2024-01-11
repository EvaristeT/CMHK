package com.cmhk.ztpayment.utils;

/**
* Description: 线程工具类,主要存放各个ThreadLocal，便于后续使用
* date: 2020/12/22 16:10
* @author flanderldk
*/
public class ThreadLocalUtils {

    /** 存放每次请求的messageId以供使用 */
    public static final ThreadLocal<String> MESSAGE_ID_THEADLOCAL = new ThreadLocal<>();
    /**
     * 统一的销毁方法，
     * 在 {@link CheckSignHandlerInterceptor#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)}
     * 拦截器调用这个方法清除线程的Theadlocal
     * 添加的theadlocal要在这里统一remove
     */
    public static void destory(){
        MESSAGE_ID_THEADLOCAL.remove();
    }
}
