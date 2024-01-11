package com.cmhk.ztpayment.requests.wechat;

import lombok.Data;

/**
 * @author flanderldk
 * @date 2020-08-05
 * @desc 微信app下单接口调用入参  参考示例 https://pay.weixin.qq.com/wiki/doc/api/wxpay/ch/fusion_wallet_ch/In-AppPay/chapter6_3.shtml#doc-main
 */
@Data
public class WXAppOrderRequest {
    /** 商户号 */
    private String mchid;
    /** APPID */
    private String appid;
    /** 商品描述 */
    private String description;
    /** 商户数据 */
    private String attach;
    /** 通知地址 */
    private String notify_url;
    /** 商户订单号 */
    private String out_trade_no;
    /** 商品标记 */
    private String goods_tag;
    /** 交易类型 */
    private String trade_type;
    /** 指定支付方式 */
    private String limit_pay;
    /** 交易起始时间 */
    private String time_start;
    /** 交易结束时间 */
    private String time_expire;
    /** MCC码 */
    private String merchant_category_code;
    /** 支付人 */
    private Player player;
    /** 订单金额 */
    private Amount amount;
    /** 订单金额 */
    private SceneInfo scene_info;
    /** 优惠功能 */
    private PromotionDetail promotion_detail;

    @Data
    public static class Player{
        private String openid;
        private String sp_openid;
        private String sub_openid;
    }

    /**
    * Description: 场景信息
    * date: 2021/4/21 10:32
    * @author flanderldk
    */
    @Data
    public static class SceneInfo{
        /** 商户端设备号*/
        private String device_id;
        /** 商户端设备IP*/
        private String device_ip;
        /** 用户终端IP*/
        private String payer_client_ip;
        /** 操作员ID*/
        private String operator_id;
    }

    @Data
    public static class Amount{
        /** 总金额 */
        private int total;
        /** 货币类型 */
        private String currency;

    }

    @Data
    public static class PromotionDetail{
        /** 订单原价 */
        private String cost_price;
        /** 商品小票ID */
        private String receipt_id;
        /** 单品列表 */
        private GoodsDetail goods_detail;
    }

    @Data
    public static class GoodsDetail{
        /** 商品编码 */
        private String goods_id;
        /** 微信侧商品编码 */
        private String wxpay_goods_id;
        /** 商品名称 */
        private String goods_name;
        /** 商品数量 */
        private String quantity;
        /** 商品单价 */
        private String price;
    }

}
