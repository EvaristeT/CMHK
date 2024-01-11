package com.cmhk.ztpayment.responses.schema;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CloseResponseSchema {
    /** 用于标识本次请求 */
    private String uuid = UUID.randomUUID().toString();

    // https://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'Z")
    private Date datetime = new Date();


    /** 返回编码 */
    private String returnCode;
    /** 返回描述 英文*/
    private String returnDesc;
    /** 详细描述 */
    private String suppleDesc;

    private HttpStatusSchema resources;  
}
