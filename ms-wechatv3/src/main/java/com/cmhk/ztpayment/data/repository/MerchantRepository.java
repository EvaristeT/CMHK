package com.cmhk.ztpayment.data.repository;

import com.cmhk.ztpayment.data.entity.Merchant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("MerchantRepository")
public interface MerchantRepository extends CrudRepository<Merchant, Long> {

    Merchant findFirstByMerchantCodeAndIsEnableAndIsDelete(String merchantCode, String isEnable, Integer isDelete);
}
