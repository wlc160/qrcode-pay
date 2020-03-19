package com.qrpay.demo.module.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qrpay.demo.pay.alipay.config.AlipayConfig;
import com.qrpay.demo.redis.service.RedisService;
import com.qrpay.demo.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * 工程名:meal
 * 包名:com.qrpay.demo.module.api;
 * 文件名:AlipayController.java
 * @author lcwen
 * @version $Id: AlipayController.java 2020年3月11日 上午11:34:45 $
 */
@Controller
@RequestMapping(value = "/api/alipay")
public class AlipayController {
	
	private static final Logger log = LoggerFactory.getLogger(AlipayController.class);
	
	@Autowired(required = false)
	private HttpServletRequest request;
	
	@RequestMapping(value = "/notify_url")
	public void notify_url(){
		try {
			boolean signVerified = signVerify(request);
			if (!signVerified) {// 是否验证不成功
				log.error("支付宝异步通知验证签名失败");
				return;
			}	
			
			String trade_status = new String(request.getParameter(
					"trade_status").getBytes("ISO-8859-1"), "UTF-8");
			if (!trade_status.equals("TRADE_FINISHED") && !trade_status.equals("TRADE_SUCCESS")) {
				log.info("支付宝异步通知回调结果：失败");
				return;
			}
			
			String out_trade_no = request.getParameter("out_trade_no");
			String trade_no = request.getParameter("trade_no");

			RedisService redisService = SpringUtil.getBean(RedisService.class);
			String status = redisService.getOrderStatus(out_trade_no);
			if(status == null || status.equals("2")){
				return;
			}
			redisService.setOrderStatus(out_trade_no,2);
			redisService.removeOrder(out_trade_no);

			log.info("支付宝支付成功,订单号：[{}],交易号：[{}]",out_trade_no,trade_no);
			
		} catch (Exception e) {
			log.error("支付宝异步通知出错，错误信息：",e.getMessage(),e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 简要说明:同步路径 <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2020年3月12日 下午5:47:17
	 * 更新者:
	 * 更新时间:
	 * @return
	 */
	@RequestMapping(value = "/return_url")
	public String return_url(){
		try {
			boolean signVerified = signVerify(request);
			if (!signVerified) {
				return "fail_h5";
			}
			request.setAttribute("money", request.getParameter("total_amount"));
			return "ok_h5";
		} catch (Exception e) {
			log.error("支付宝同步通知出错，错误信息：",e.getMessage(),e);
			e.printStackTrace();
		}
		return "fail_h5";
	}
	
	
	
	/**
	 * 简要说明:支付宝签名验证 <br>
     *************************页面功能说明*************************
     * 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
     * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
     * 如果没有收到该页面返回的 success 
     * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
	 * 创建者：lcwen
	 * 创建时间:2020年3月12日 下午5:00:28
	 * @param request
	 * @return
	 * @throws Exception
	 */
    private boolean signVerify(HttpServletRequest request) throws Exception{
		boolean signVerified = false;
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
			params.put(name, valueStr);
		}
		signVerified = AlipaySignature.rsaCheckV1(params,
				AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET,
				AlipayConfig.SIGNTYPE); // 调用SDK验证签名
		return signVerified;
	}

}
