package com.qrpay.demo.module.service.impl;

import com.github.wxpay.sdk.WXPayConfig;
import com.qrpay.demo.module.service.PayService;
import com.qrpay.demo.pay.alipay.util.AlipayUtil;
import com.qrpay.demo.pay.wxpay.config.UrlConstants;
import com.qrpay.demo.pay.wxpay.config.WxpayConfig;
import com.qrpay.demo.pay.wxpay.util.WxPayUtil;
import com.qrpay.demo.pay.wxpay.util.WxSignUtil;
import com.qrpay.demo.redis.service.RedisService;
import com.qrpay.demo.util.IPCoverUtil;
import com.qrpay.demo.util.OrderNumUtils;
import com.qrpay.demo.util.QrCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 工程名:qrcode-pay
 * 包名:com.qrpay.demo.module.service
 * 文件名:PayServiceImpl
 * description:
 *
 * @author lcwen
 * @version V1.0: PayServiceImpl.java 2020/3/18 15:24
 **/
@Service
public class PayServiceImpl implements PayService {

    @Value("${files.upload-path}")
    private String imagePath;

    @Value("${files.mapping-path}")
    private String image;

    @Value("${project.first-url}")
    private String firstUrl;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private RedisService redisService;

    BigDecimal money = new BigDecimal("0.01");

    @Override
    public String getQrCode() throws Exception {
        String qrCode = redisService.getQrCode();
        if (qrCode == null){
            String saveUrl = imagePath + "/qr";// 保存路径
            String url = firstUrl + "/pay/payH5";
            String img = QrCodeUtils.encode(url, saveUrl, true);
            qrCode = firstUrl + image + "/qr/" + img;
            redisService.setQrCode(qrCode);
        }
        return qrCode;
    }

    @Override
    public Map<String, String> getPayParam(int payType) throws Exception {
        String orderNum = redisService.getCurrOrder();
        if(orderNum == null){
            orderNum = OrderNumUtils.newOrderNum();
            redisService.setNewOrder(orderNum);
        }

        Map<String, String> result = new HashMap<>();
        if (payType == 1){
            result.put("payParam", AlipayUtil.getPayParam(orderNum, "测试支付",
                    money.toPlainString(), "测试支付", null,
                    null , firstUrl));
        }else{
            WXPayConfig config = new WxpayConfig();
            result.put("appId", config.getAppID());
            result.put("url", firstUrl + "/pay/wxPayH5/");
            result.put("state", orderNum);
        }
        return result;
    }

    @Override
    public Map<String, Object> getWxPayParam(HttpServletRequest request, String code, String orderNum) throws Exception {
        String openid = WxSignUtil.accessWithOpenid(code);
        String ip = IPCoverUtil.getIpAddress(request);
        String notify_url = firstUrl + UrlConstants.VIP_NOTIFY_URL;
        Map<String, Object> param = WxPayUtil.getH5PayParam(orderNum,
                "测试支付", money, ip, notify_url, null, openid);
        if (param == null) {
            throw new Exception("获取微信订单参数失败");
        }
        param.put("payment",money.toPlainString());
        param.put("orderNum",orderNum);
        return param;
    }

    @Override
    public Integer getStatus(String orderNum) throws Exception {
        String status = redisService.getOrderStatus(orderNum);
        if (status == null){
            return 3;//过期
        }
        return Integer.parseInt(status);
    }
}
