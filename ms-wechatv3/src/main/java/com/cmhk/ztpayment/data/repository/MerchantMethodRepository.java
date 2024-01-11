package com.cmhk.ztpayment.data.repository;

import java.util.List;

import com.cmhk.ztpayment.data.entity.MerchantMethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("MerchantMethodRepository")
public interface MerchantMethodRepository extends JpaRepository<MerchantMethod, Long> {
    

    List<MerchantMethod> findByMerchantId(String merchantId);

    MerchantMethod findFirstByMerchantIdAndIsEnableAndIsDelete(String merchantId, String isEnable, Integer isDelete);

    @Query(value = "SELECT * FROM cc_payment_merchant_method WHERE merchant_id = ?1 AND method_id IN (SELECT id FROM cc_payment_method WHERE method_code = ?2) AND is_enable = 'ON' AND is_delete = 0 LIMIT 1", nativeQuery = true)
    MerchantMethod findWithMerchantIdAndMethodCode(String merchantId, String methodCode);

    MerchantMethod findById(String id);
}
