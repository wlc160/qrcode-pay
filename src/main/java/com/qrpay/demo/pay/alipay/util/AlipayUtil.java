package com.qrpay.demo.pay.alipay.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.qrpay.demo.pay.alipay.config.AlipayConfig;

/**
 * 工程名:meal
 * 包名:com.meal.pay.alipay.util
 * 文件名:AlipayUtil.java
 * @author lcwen
 * @version $Id: AlipayUtil.java 2020年3月11日 下午3:05:38 $
 */
public class AlipayUtil {

	
	/**
	 * 简要说明:获取支付宝支付参数 <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2020年3月11日 下午3:24:44
	 * 更新者:
	 * 更新时间:
	 * @param out_trade_no      订单号
	 * @param subject		          商品名称
	 * @param total_amount      金额
	 * @param description	          商品介绍
	 * @param timeout_express   超时时间(可为null)
	 * @param passbackParams    附近参数
	 * @return
	 */
	public static String getPayParam(String out_trade_no ,
								     String subject ,
								     String total_amount ,
								     String description ,
								     String timeout_express,
								     String passbackParams ,
								     String firstUrl){
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL,
				AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY,
				AlipayConfig.FORMAT, AlipayConfig.CHARSET,
				AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
		
		AlipayTradeWapPayRequest request=new AlipayTradeWapPayRequest();
	    
		// 封装请求支付信息
	    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
	    model.setSubject(subject);
	    model.setTotalAmount(total_amount);
	    model.setBody(description);
	    model.setTimeoutExpress(timeout_express);
	    model.setProductCode(AlipayConfig.PRODUCT_CODE);
		model.setPassbackParams(passbackParams);
		
		request.setBizModel(model);
		request.setNotifyUrl(firstUrl + AlipayConfig.notify_url);//异步地址
	    request.setReturnUrl(firstUrl + AlipayConfig.return_url);//同步地址
	    
	    //form表单生产
	    String form = "";
	   
	 	try {
	 		 // 调用SDK生成表单
	 		form = client.pageExecute(request).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	    return form;
	}
	
	
}
