package com.qrpay.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生成
 * 工程名:meal
 * 包名:com.meal.util
 * 文件名:OrderNumUtils.java
 * @author lcwen
 * @version $Id: OrderNumUtils.java 2020年3月11日 上午9:28:34 $
 */
public class OrderNumUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 简要说明:创建订单号 <br>
	 * 详细说明:格式：日期+四位随机数
	 * 创建者：lcwen
	 * 创建时间:2020年3月11日 上午9:38:19
	 * 更新者:
	 * 更新时间:
	 * @return
	 */
	public static String newOrderNum() {
		return sdf.format(new Date()) + Math.round((Math.random() + 1) * 1000);
	}
	
}
