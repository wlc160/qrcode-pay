package com.qrpay.demo.pay.wxpay.pojo;

import java.io.Serializable;

public class Unifiedorder implements Serializable {

	private static final long serialVersionUID = 1L;

	// 必填
	private String appid;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String body;
	private String out_trade_no;
	private Integer total_fee;
	private String spbill_create_ip;
	private String notify_url;
	private String trade_type;

	// 非必填
	private String product_id;
	private String openid;
	private String limit_pay;
	private String time_start;
	private String time_expire;
	private String goods_tag;
	private String fee_type;
	private String device_info;
	private String sign_type;
	private String detail;
	private String attach;

	public String getAppid() {
		return appid;
	}

	public Unifiedorder setAppid(String appid) {
		this.appid = appid;
		return this;
	}

	public String getMch_id() {
		return mch_id;
	}

	public Unifiedorder setMch_id(String mch_id) {
		this.mch_id = mch_id;
		return this;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public Unifiedorder setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
		return this;
	}

	public String getSign() {
		return sign;
	}

	public Unifiedorder setSign(String sign) {
		this.sign = sign;
		return this;
	}

	public String getBody() {
		return body;
	}

	public Unifiedorder setBody(String body) {
		this.body = body;
		return this;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public Unifiedorder setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
		return this;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public Unifiedorder setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
		return this;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public Unifiedorder setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
		return this;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public Unifiedorder setNotify_url(String notify_url) {
		this.notify_url = notify_url;
		return this;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public Unifiedorder setTrade_type(String trade_type) {
		this.trade_type = trade_type;
		return this;
	}

	public String getProduct_id() {
		return product_id;
	}

	public Unifiedorder setProduct_id(String product_id) {
		this.product_id = product_id;
		return this;
	}

	public String getOpenid() {
		return openid;
	}

	public Unifiedorder setOpenid(String openid) {
		this.openid = openid;
		return this;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public Unifiedorder setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
		return this;
	}

	public String getTime_start() {
		return time_start;
	}

	public Unifiedorder setTime_start(String time_start) {
		this.time_start = time_start;
		return this;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public Unifiedorder setTime_expire(String time_expire) {
		this.time_expire = time_expire;
		return this;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public Unifiedorder setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
		return this;
	}

	public String getFee_type() {
		return fee_type;
	}

	public Unifiedorder setFee_type(String fee_type) {
		this.fee_type = fee_type;
		return this;
	}

	public String getDevice_info() {
		return device_info;
	}

	public Unifiedorder setDevice_info(String device_info) {
		this.device_info = device_info;
		return this;
	}

	public String getSign_type() {
		return sign_type;
	}

	public Unifiedorder setSign_type(String sign_type) {
		this.sign_type = sign_type;
		return this;
	}

	public String getDetail() {
		return detail;
	}

	public Unifiedorder setDetail(String detail) {
		this.detail = detail;
		return this;
	}

	public String getAttach() {
		return attach;
	}

	public Unifiedorder setAttach(String attach) {
		this.attach = attach;
		return this;
	}

}
