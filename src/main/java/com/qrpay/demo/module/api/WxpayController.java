package com.qrpay.demo.module.api;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qrpay.demo.pay.wxpay.config.WxpayConfig;
import com.qrpay.demo.pay.wxpay.pojo.NotifyInfo;
import com.qrpay.demo.redis.service.RedisService;
import com.qrpay.demo.util.JsonUtils;
import com.qrpay.demo.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 工程名:meal
 * 包名:com.qrpay.demo.module.api
 * 文件名:WxpayController.java
 * @author lcwen
 * @version $Id: WxpayController.java 2020年3月11日 上午11:35:02 $
 */
@Controller
@RequestMapping(value = "/api/wxpay")
public class WxpayController {
	
	private static final Logger log = LoggerFactory.getLogger(WxpayController.class);
	
	@Autowired(required = false)
	private HttpServletRequest request;
	
	@Autowired(required = false)
	private HttpServletResponse response;
	
	/**
	 * 简要说明:异步通知 <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2020年3月13日 上午10:39:26
	 * 更新者:
	 * 更新时间:
	 */
	@RequestMapping(value = "/notify_url")
	public void notify_url() throws Exception{
		String notityXml = "";
		String inputLine = "";
		while ((inputLine = request.getReader().readLine()) != null) {
			notityXml += inputLine;
		}
		request.getReader().close();
		
		WXPayConfig config = new WxpayConfig();
		XStream xs = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-",
				"_")));
		xs.alias("xml", NotifyInfo.class);
		NotifyInfo ntfInfo = JsonUtils.jsonToPojo(
				JsonUtils.objectToJson(xs.fromXML(notityXml.toString())),
				NotifyInfo.class);
		
		// 验证签名是否正确
		boolean isSign = WXPayUtil.isSignatureValid(notityXml, config.getKey());
		if(!isSign){
			signFail(response);
			return;
		}
		if (!"SUCCESS".equals(ntfInfo.getReturn_code())
				|| !"SUCCESS".equals(ntfInfo.getResult_code())) {
			payFail(response,ntfInfo.getErr_code());
			return;
		}
		
		//订单号、微信交易号、
		String out_trade_no = ntfInfo.getOut_trade_no();
		String trade_no = ntfInfo.getTransaction_id();

		RedisService redisService = SpringUtil.getBean(RedisService.class);
		String status = redisService.getOrderStatus(out_trade_no);
		if(status == null || status.equals("2")){
			return;
		}
		redisService.setOrderStatus(out_trade_no,2);
		redisService.removeOrder(out_trade_no);

		log.info("微信支付成功,订单号：[{}],交易号：[{}]",out_trade_no,trade_no);

		payOk(response);
	}
	
	/**
	 * 支付成功
	 */
	private static void payOk(HttpServletResponse response){
		log.info("================  微信支付异步回调支付成功返回  =================");
		String resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg></xml> ";
		flushResponse(response, resXml);
	}
	
	
	/**
	 * 支付失败
	 */
	private static void payFail(HttpServletResponse response ,String errCode){
		log.info("================  微信支付异步回调支付失败返回  =================");
		String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"
				+ "<return_msg><![CDATA[" + errCode + "]]></return_msg></xml>";
		flushResponse(response, resXml);
	}
	
	/**
	 * 签名失败
	 */
	private static void signFail(HttpServletResponse response){
		log.info("================  微信支付异步回调签名失败  =================");
		String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"  
                + "<return_msg><![CDATA[签名有误]]></return_msg></xml> "; 
		flushResponse(response, resXml);
	}
	
	private static void flushResponse(HttpServletResponse response ,String resXml){
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(response.getOutputStream());      
			out.write(resXml.getBytes("utf-8"));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
