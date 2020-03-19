package com.qrpay.demo.module.controller;

import com.qrpay.demo.module.service.PayService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 工程名:qrcode-pay
 * 包名:com.qrpay.demo.controller
 * 文件名:PayController
 * description:
 *
 * @author lcwen
 * @version V1.0: PayController.java 2020/3/18 15:01
 **/
@Controller
@RequestMapping(value = "/pay")
public class PayController {

    private static  final Logger log = LoggerFactory.getLogger(PayController.class);

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private PayService payService;

    /**
     * 跳转到首页 - 二维码
     * @return
     */
    @RequestMapping(value = "/index")
    public String index() throws  Exception{
        String imgCode = payService.getQrCode();
        request.setAttribute("imgCode",imgCode);
        return "index";
    }

    /**
     * 扫码进入
     * @return
     */
    @RequestMapping(value =  "/payH5")
    public String payH5() throws  Exception{
        String userAgent = request.getHeader("user-agent");
        if (userAgent == null || !(userAgent.contains("AlipayClient") || userAgent.contains("MicroMessenger"))) {
            log.info("未知来源扫码进入付费模块，返回无页面...");
            return "expire_h5";
        }

        if(userAgent.contains("AlipayClient")){
            Map<String, String> result = payService.getPayParam(1);
            request.setAttribute("payParam",result.get("payParam"));
            return "alipay_h5";
        }else{
            request.setAttribute("payParam",payService.getPayParam(2));
            return "middle_direct";
        }
    }


    /**
     * 微信支付页
     * @param state
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/wxPayH5")
    public String wxPayH5(String state ,String code) throws  Exception{
        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(code)) {
            return "expire_h5";
        }
        try {
            request.setAttribute("payParam",payService.getWxPayParam(request,code,state));
            return "wxpay_h5";
        } catch (Exception e) {
            log.error("微信获取支付参数出错，错误信息：", e.getMessage(), e);
            e.printStackTrace();
        }
        return "expire_h5";
    }

    /**
     * 获取支付状态
     * @param orderNum
     * @return
     */
    @RequestMapping(value = "/getPayStatus/{orderNum}")
    @ResponseBody
    public Integer getStatus(@PathVariable  String orderNum){
        Integer status = null;
        try {
            status = payService.getStatus(orderNum);
        }catch (Exception e){
            status = 3;
            e.printStackTrace();
        }
        return status;
    }

}
