package com.cmhk.ztpayment.requests.wechat;

import lombok.Data;

/**
 * @author evariste
 * @date 2021-07-05
 * @desc 微信app下单接口调用入参  参考示例 https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_1.shtml
 */
@Data
public class WXAppNativeOrderRequest {
    /** APPID */
    private String appid;
    /** 商户号 */
    private String mchid;
    /** 商品描述 */
    private String description;
    /** 商户订单号 */
    private String out_trade_no;
    /** 交易起始时间 */
    private String time_start;
    /** 交易结束时间 */
    private String time_expire;
    /** 商户数据 */
    private String attach;
    /** 通知地址 */
    private String notify_url;
    /** 商品标记 */
    private String goods_tag;
    /** 交易类型 */
    private String trade_type;
    /** MCC码 */
    private String merchant_category_code;
    /** 订单金额 */
    private Amount amount;
    /** 优惠功能 */
    private PromotionDetail detail;
    /** 场景信息 */
    private SceneInfo scene_info;
    /** 结算信息 */
    private SettleInfo settle_info;

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
        private String invoice_id;
        /** 单品列表 */
        private GoodsDetail goods_detail;
    }

    @Data
    public static class GoodsDetail{
        /** 商户侧商品编码 */
        private String merchant_goods_id;
        /** 微信侧商品编码 */
        private String wechatpay_goods_id;
        /** 商品名称 */
        private String goods_name;
        /** 商品数量 */
        private String quantity;
        /** 商品单价 */
        private String unit_price;
    }

    @Data
    public static class SceneInfo{
        /** 用户终端IP*/
        private String payer_client_ip;
        /** 商户端设备号*/
        private String device_id;
        /** -商户门店信息*/
        private StoreInfo store_info;
    }

    @Data
    public static class StoreInfo {
        /** 门店编号*/
        private String id;
        /** 门店名称*/
        private String name;
        /** 地区编码*/
        private String area_code;
        /** 详细地址*/
        private String address;
    }

    @Data
    public static class SettleInfo {
        /** 是否指定分账*/
        private Boolean profit_sharing; 
    }
}
