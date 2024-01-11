package com.cmhk.ztpayment.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="cc_payment_request")
public class PaymentRequest {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="merchant_method_id")
    private String merchantMethodId;

    @Column(name="order_no")
    private String orderNo;

    @Column(name="order_ref")
    private String orderRef;

    @Column(name="amount")
    private Double amount;

    @Column(name="real_amount")
    private Double realAmount;

    @Column(name="lang")
    private String lang;

    @Column(name="status")
    private String status;

    @Column(name="order_desc")
    private String orderDesc;

    @Column(name="currency")
    private String currency;

    @Column(name="create_at")
    private Date createAt;

    @Column(name="update_at")
    private Date updateAt;

    @Column(name="check_code")
    private String checkCode;
}
