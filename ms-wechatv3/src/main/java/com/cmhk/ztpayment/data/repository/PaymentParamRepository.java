package com.cmhk.ztpayment.data.repository;

import com.cmhk.ztpayment.data.entity.PaymentParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("PaymentParamRepository")
public interface PaymentParamRepository extends CrudRepository<PaymentParam, Long> {
    PaymentParam findFirstByAccountIdAndNameAndIsDelete(String accountId, String name, Integer isDelete);
}
