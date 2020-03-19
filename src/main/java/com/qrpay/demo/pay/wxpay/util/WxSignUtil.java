package com.qrpay.demo.pay.wxpay.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.github.wxpay.sdk.WXPayConfig;
import com.qrpay.demo.pay.wxpay.config.WXPayConstants;
import com.qrpay.demo.pay.wxpay.config.WxpayConfig;
import com.qrpay.demo.util.JsonUtils;

/**
 * ************ 微信授权
 * 工程名:meal
 * 包名:com.meal.pay.wxpay.util
 * 文件名:WxSignUtil.java
 * @author lcwen
 * @version $Id: WxSignUtil.java 2020年3月16日 下午4:57:30 $
 */
public class WxSignUtil {
	
	/**
	 * 简要说明:授权并获取openid <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2020年3月16日 下午5:16:22
	 * 更新者:
	 * 更新时间:
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public static String accessWithOpenid(String code) throws Exception{
		WxpayConfig config = new WxpayConfig();
		String req_url = WXPayConstants.ACCESS_TOKEN_SUFFIX + "?appid="
				+ config.getAppID() + "&secret=" + config.getSecret()
				+ "&code=" + code + "&grant_type=authorization_code";
		String res = httpsRequest(req_url, "GET", null);
		if (res != null) {
			Map map = JsonUtils.jsonToPojo(res, Map.class);
			try {				
				return map.get("openid").toString();
			} catch (Exception e) {
				throw new Exception("授权获取openid出错：CODE:" + map.get("errcode")
						+ ",ERRMSG:" + map.get("errmsg") + "");
			}
		}
		throw new Exception("微信授权失败：" + res);
	}
	
	/**
	 * 简要说明:发起请求 <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2020年3月16日 下午5:17:52
	 * 更新者:
	 * 更新时间:
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = null;
		try {
			// 创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			TrustManager[] tm = { new MyX509TrustManager() };
			// 初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 获取SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			// 设置当前实例使用的SSLSocketFactory
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			// 往服务器端写内容
			if (null != outputStr) {
				OutputStream os = conn.getOutputStream();
				os.write(outputStr.getBytes("utf-8"));
				os.close();
			}

			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);

			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
	
}
