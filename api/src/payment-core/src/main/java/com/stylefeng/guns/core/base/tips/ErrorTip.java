package com.stylefeng.guns.core.base.tips;

/**
 * 返回给前台的错误提示
 *
 * @author fengshuonan
 * @date 2016年11月12日 下午5:05:22
 */
public class ErrorTip extends Tip {

    /** 参数信息异常*/
    private final static Integer INFO_ERROR_CODE = -102;

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    /**
     * 错误信息构造器
     */
    public static class ErrorTipBuilder{

        /** 参数信息异常*/
        public static Tip INFO_ERROR(String message){
            return new ErrorTip(INFO_ERROR_CODE, message);
        }

    }

}
