package com.qrpay.demo.pay.wxpay.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager  implements X509TrustManager {

	@Override
	public void checkClientTrusted(
			X509Certificate[] paramArrayOfX509Certificate, String paramString)
			throws CertificateException {
		
	}

	@Override
	public void checkServerTrusted(
			X509Certificate[] paramArrayOfX509Certificate, String paramString)
			throws CertificateException {
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
