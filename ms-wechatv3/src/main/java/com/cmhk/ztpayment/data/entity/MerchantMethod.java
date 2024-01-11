package com.cmhk.ztpayment.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="cc_payment_merchant_method")
public class MerchantMethod {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="merchant_id")
    private String merchantId;

    @Column(name="method_id")
    private String methodId;

    @Column(name="account_id")
    private String accountId;

    @Column(name="is_enable")
    private String isEnable;

    @Column(name="is_delete")
    private Integer isDelete;

    @ManyToOne
    @JoinColumn(name="merchant_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Merchant merchant;
}
