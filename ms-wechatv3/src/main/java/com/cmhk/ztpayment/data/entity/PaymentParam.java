package com.cmhk.ztpayment.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="cc_payment_param")
public class PaymentParam {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="account_id")
    private String accountId;

    @Column(name="name")
    private String name;

    @Column(name="value")
    private String value;

    @Column(name="is_delete")
    private Integer isDelete;
}
