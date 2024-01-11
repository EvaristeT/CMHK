package com.cmhk.ztpayment.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Md5CheckerUtils {
    
    private Md5CheckerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getSecretFormat(String merchantCode, String merchantKey, String uuid){
        return merchantCode + merchantKey + uuid;
    }

    public static boolean check(String merchantCode, String merchantKey, String uuid, String compareValue){
        String value = getSecretFormat(merchantCode, merchantKey, uuid);

        return check(value, compareValue);
    }

    public static boolean check(String value, String compareValue) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            byte[] digiest = md.digest();
            String hashedOutput = DatatypeConverter.printHexBinary(digiest);

            if (hashedOutput.equalsIgnoreCase(compareValue)) {
                return true;
            }
        } catch(NoSuchAlgorithmException e) {
            log.error("I'm sorry, but MD5 is not a valid message digest algorithm", e);

            return false;
        }
        
        return false;
    }
}
