package com.qrpay.demo.module.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 工程名:qrcode-pay
 * 包名:com.qrpay.demo.module.service
 * 文件名:PayService
 * description:
 *
 * @author lcwen
 * @version V1.0: PayService.java 2020/3/18 15:23
 **/
public interface PayService {


    String getQrCode() throws Exception;

    Map<String,String> getPayParam(int payType) throws Exception;

    Map<String,Object> getWxPayParam(HttpServletRequest request ,String code ,String orderNum) throws Exception;

    Integer getStatus(String orderNum) throws Exception;
}
