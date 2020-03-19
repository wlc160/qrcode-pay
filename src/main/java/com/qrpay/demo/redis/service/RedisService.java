package com.qrpay.demo.redis.service;

/**
 * 工程名:qrcode-pay
 * 包名:com.qrpay.demo.redis.service
 * 文件名:RedisService
 * description:
 *
 * @author lcwen
 * @version V1.0: RedisService.java 2020/3/18 15:03
 **/
public interface RedisService {


    void setNewOrder(String orderNum) throws Exception;

    String getCurrOrder() throws  Exception;

    void setOrderStatus(String orderNum ,int status) throws  Exception;

    String getOrderStatus(String orderNum) throws  Exception;

    void setQrCode(String url) throws Exception;

    String getQrCode() throws  Exception;

    void removeOrder(String orderNum) throws  Exception;
}
