package com.qrpay.demo.pay.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 工程名:meal
 * 包名:com.meal.pay.alipay.config
 * 文件名:AlipayConfig.java
 * @author lcwen
 * @version $Id: AlipayConfig.java 2020年3月11日 上午10:48:40 $
 */
public class AlipayConfig {
	
	/**商户appid*/
	public static String APPID = "";
	
	/**商户私钥 pkcs8格式的*/
	public static String RSA_PRIVATE_KEY = "";

	/**支付宝公钥*/
	public static String ALIPAY_PUBLIC_KEY = "";

	/**服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问*/
	public static String notify_url = "/api/alipay/notify_url";
	
	/**页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址*/
	public static String return_url = "/api/alipay/return_url";
	
	/**请求网关地址*/
	public static String URL = "https://openapi.alipay.com/gateway.do";
	
	/**编码*/
	public static String CHARSET = "UTF-8";
	
	/**返回格式*/
	public static String FORMAT = "json";
	
	
	/**日志记录目录*/
	public static String log_path = "C:\\";
	
	/**RSA2*/
	public static String SIGNTYPE = "RSA2";
	
	/** 销售产品码：QUICK_WAP_PAY  */
	public static String PRODUCT_CODE = "QUICK_WAP_PAY";
	
	/** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
