package com.cmhk.ztpayment.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="cc_payment_merchant")
public class Merchant {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="merchant_code")
    private String merchantCode;

    @Column(name="merchant_name")
    private String merchantName;

    @Column(name="merchant_key")
    private String merchantKey;

    @Column(name="is_enable")
    private String isEnable;

    @Column(name="is_delete")
    private Integer isDelete;

    @OneToMany(mappedBy="merchant", fetch = FetchType.LAZY)
    private List<MerchantMethod> methods;
}
