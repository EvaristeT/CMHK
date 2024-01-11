package com.cmhk.ztpayment.constant;

public enum WeChatPayStateEnum {
    SUCCESS("PAYMENT"),
    REFUND("REFUND"),
    NOTPAY("PENDING"),
    CLOSED("FAILURE"),
    REVOKED("FAILURE"),
    USERPAYING("PENDING"),
    PAYERROR("FAILURE"),
    ACCEPT("PENDING");

    private String text;

    WeChatPayStateEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static String parseStatus (String state) {
        for (WeChatPayStateEnum rs: WeChatPayStateEnum.values()) {
            if (rs.toString().equalsIgnoreCase(state)) {
                return rs.text;
            }
        }

        return null;
    }
}
