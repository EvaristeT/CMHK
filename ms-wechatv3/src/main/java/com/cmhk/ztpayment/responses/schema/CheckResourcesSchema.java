package com.cmhk.ztpayment.responses.schema;

import lombok.Data;

@Data
public class CheckResourcesSchema {
    private AmountSchema amount;

    private String mchid;
    private String out_trade_no;
    private String trade_state;
    private String appid;
    private String trade_state_desc;

    private PayerSchema payer;
}
