package com.qrpay.demo.pay.wxpay.config;

import java.io.InputStream;
import java.util.ResourceBundle;

import com.github.wxpay.sdk.WXPayConfig;

public class WxpayConfig implements WXPayConfig{

	@Override
	public String getAppID() {
		return ResourceBundle.getBundle("application").getString("wechat.appID");
	}
	
	public String getSecret(){
		return ResourceBundle.getBundle("application").getString("wechat.appSecret");
	}

	@Override
	public String getMchID() {
		return ResourceBundle.getBundle("application").getString("wechat.mchId");
	}

	@Override
	public String getKey() {
		return ResourceBundle.getBundle("application").getString("wechat.key");
	}

	@Override
	public InputStream getCertStream() {
		return null;
	}

	@Override
	public int getHttpConnectTimeoutMs() {
		return 0;
	}

	@Override
	public int getHttpReadTimeoutMs() {
		return 0;
	}

	
}
