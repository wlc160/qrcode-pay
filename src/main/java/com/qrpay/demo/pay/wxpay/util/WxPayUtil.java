package com.qrpay.demo.pay.wxpay.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.Maps;
import com.qrpay.demo.pay.wxpay.config.WxpayConfig;
import com.qrpay.demo.pay.wxpay.pojo.UnifiedOrderResponse;
import com.qrpay.demo.pay.wxpay.pojo.Unifiedorder;
import com.qrpay.demo.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class WxPayUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(WxPayUtil.class);

	private static final BigDecimal N100 = new BigDecimal("100");
	
	private static final String SUCCESS = "SUCCESS";
	
	/**
	 * 简要说明:获取H5支付参数 <br>
	 * 创建者：lcwen
	 * 创建时间:2020年3月13日 下午4:24:15
	 * @param out_trade_no
	 * @param body
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param notify_url
	 * @param attach
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getH5PayParam(String out_trade_no, 
								   	   String body, 
								   	   BigDecimal total_fee,
								   	   String spbill_create_ip ,
								   	   String notify_url, 
								   	   String attach ,
								   	   String openid) throws Exception{
		String order = orderToXml(out_trade_no, body, total_fee, spbill_create_ip, notify_url, attach, openid);
		UnifiedOrderResponse res = unifiedorder(order);
		if (res == null) {
			logger.error("调用微信统一下单接口失败，返回信息：NULL");
			return null;
		}
		if (res.getReturn_code().equals(SUCCESS)) {
			if (res.getResult_code().equals(SUCCESS)) {
				return getSign2Param(res);		
			} else {
				logger.error("调用微信统一下单接口失败，返回信息：errCode=[{}],errMsg=[{}]",
						res.getErr_code(), res.getErr_code_des());
			}			
		}else {
			logger.error("调用微信统一下单接口失败，返回信息：returnCode=[{}],reurnMsg=[{}]",
					res.getReturn_code(), res.getReturn_msg());
		}
		return null;
	}
	
	/**
	 * 二次签名并返回微信预支付订单参数
	 */
	private static Map<String, Object> getSign2Param(UnifiedOrderResponse res) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		SortedMap<String,String> signMap = Maps.newTreeMap();//自然升序map
		String prepayId = "prepay_id="+res.getPrepay_id();
		String timeStamp = getTimeStamp();
		String nonceStr = WXPayUtil.generateNonceStr();
		signMap.put("appId",res.getAppid());
		signMap.put("package",prepayId);
		signMap.put("timeStamp",timeStamp);
		signMap.put("nonceStr",nonceStr);
		signMap.put("signType","MD5");
		result.put("appId",res.getAppid());
		result.put("timeStamp",timeStamp);
		result.put("nonceStr",nonceStr);
		result.put("prepayId",prepayId);
		result.put("paySign",createSign2(signMap));
		return result;
	}
	
	/**
     *  获取当前时间戳
     * @return 当前时间戳字符串
     */
    public static String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }
	
	/**
	 * 简要说明:生成签名订单xml <br>
	 * 创建者：lcwen
	 * 创建时间:2020年3月13日 上午10:18:58
	 * @param out_trade_no  订单号
	 * @param body          商品描述
	 * @param total_fee     金额
	 * @param spbill_create_ip 服务器终端IP地址
	 * @param notify_url 	异步回调地址
	 * @param attach   		附加参数
	 * @return
	 * @throws Exception
	 */
	private static String orderToXml(String out_trade_no, 
								   	 String body, 
								   	 BigDecimal total_fee,
								   	 String spbill_create_ip ,
								   	 String notify_url, 
								   	 String attach ,
								   	 String openid) throws Exception {
		Unifiedorder unifiedorder = new Unifiedorder();
		WXPayConfig config = new WxpayConfig();

		unifiedorder.setAppid(config.getAppID())
				.setMch_id(config.getMchID()) 
				.setNonce_str(WXPayUtil.generateNonceStr())
				.setBody(body) 
				.setOut_trade_no(out_trade_no)
				.setTotal_fee(total_fee.multiply(N100).intValue())//金额需要扩大100倍:1代表支付时是0.01
				.setSpbill_create_ip(spbill_create_ip)
				.setNotify_url(notify_url)
				.setTrade_type("JSAPI")//MWEB-h5支付、 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
				.setAttach(attach)
				.setOpenid(openid)
				.setSign(createSign(unifiedorder));// 签名
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		xStream.alias("xml", Unifiedorder.class);
		return xStream.toXML(unifiedorder);
	}
	
	
	/**
	 * 简要说明:统一下单API <br>
	 * 创建者：lcwen 
	 * 创建时间:2020年3月13日 上午9:21:19 
	 * @param orderInfo
	 * @return
	 */
	private static UnifiedOrderResponse unifiedorder(String orderInfo) {
		String url = WXPayConstants.UNIFIEDORDER_URL;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			// 加入数据
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedOutputStream buffOutStr = new BufferedOutputStream(
					conn.getOutputStream());
			buffOutStr.write(orderInfo.getBytes("UTF-8"));
			buffOutStr.flush();
			buffOutStr.close();

			// 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			XStream xStream = new XStream(new XppDriver(
					new XmlFriendlyNameCoder("_-", "_")));// 说明3(见文末)
			// 将请求返回的内容通过xStream转换为UnifiedOrderRespose对象
			xStream.alias("xml", UnifiedOrderResponse.class);
			logger.info("微信调用统一下单接口，返回数据：[{}]",sb.toString());
			UnifiedOrderResponse uor = JsonUtils.jsonToPojo(
					JsonUtils.objectToJson(xStream.fromXML(sb.toString())),
					UnifiedOrderResponse.class);
			// 根据微信文档return_code 和result_code都为SUCCESS的时候才会返回code_url
			// <span style="color:#ff0000;"><strong>说明4(见文末)</strong></span>
			if (null != uor && "SUCCESS".equals(uor.getReturn_code())
					&& "SUCCESS".equals(uor.getResult_code())) {
				return uor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 简要说明:创建签名 <br>
	 * 创建者：lcwen
	 * 创建时间:2020年3月13日 上午9:42:35
	 * @param unifiedOrder
	 * @return
	 * @throws Exception
	 */
	private static String createSign(Unifiedorder unifiedOrder)
			throws Exception {
		SortedMap<String, String> params = new TreeMap<String, String>();
		params.put("appid", unifiedOrder.getAppid());
		params.put("body", unifiedOrder.getBody());
		params.put("mch_id", unifiedOrder.getMch_id());
		params.put("nonce_str", unifiedOrder.getNonce_str());
		params.put("notify_url", unifiedOrder.getNotify_url());
		params.put("out_trade_no", unifiedOrder.getOut_trade_no());
		params.put("spbill_create_ip", unifiedOrder.getSpbill_create_ip());
		params.put("trade_type", unifiedOrder.getTrade_type());
		params.put("total_fee", unifiedOrder.getTotal_fee() + "");
		params.put("attach", unifiedOrder.getAttach());
		params.put("openid", unifiedOrder.getOpenid());
		String sign = createSign2(params);
		return sign;
	}

	/**
	 * 简要说明:二次签名 <br>
	 * 创建者：lcwen 
	 * 创建时间:2020年3月13日 上午9:39:44
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static String createSign2(SortedMap<String, String> parameters) throws Exception {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		WXPayConfig config = new WxpayConfig();
		sb.append("key=" + config.getKey()); // KEY是商户秘钥
		String sign = WXPayUtil.MD5(sb.toString()).toUpperCase();// MD5加密
		return sign;
	}
}
