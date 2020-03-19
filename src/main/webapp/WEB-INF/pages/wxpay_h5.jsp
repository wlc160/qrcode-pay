<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>微信支付页面</title>
<link rel="stylesheet" href="/css/reset.css">
<link rel="stylesheet" href="/css/border.css">
<style>
html, body {
	width: 100%;
	height: 100%;
}

* {
	box-sizing: border-box;
}

.payment-body {
	position: relative;
	height: 100%;
	font-size: .32rem;
	color: #333;
	padding: 0 .4rem;
}

.payment-detail {
	text-align: center;
	padding: .3rem 0 .5rem 0;
}

.vip-item {
	font-size: .32rem;
	padding-bottom: .2rem;
}

.platform {
	font-size: .26rem;
	color: #666;
}

.payment-money {
	font-size: .4rem;
	padding-top: .45rem;
}

.payment-money span {
	font-size: .76rem;
}

.paument-list li {
	display: -webkit-flex;
	display: -moz-flex;
	display: -ms-flex;
	display: -o-flex;
	display: flex;
	justify-content: space-between;
	font-size: .3rem;
	color: #999;
	padding-top: .35rem;
}

.paument-list li span {
	color: #333;
}

.payment-tip {
	position: absolute;
	bottom: 1.7rem;
	left: 0;
	right: 0;
	text-align: center;
	font-size: .32rem;
}

.payment-tip span {
	color: #30bdbb;
}

.payment-btn {
	position: absolute;
	bottom: .4rem;
	left: .4rem;
	right: .4rem;
	line-height: .88rem;
	border-radius: .1rem;
	background-color: #30bdbb;
	color: #fff;
	text-align: center;
	font-size: .36rem;
}
</style>
</head>
<body>
	<div class="payment-body">
        <p class="payment-tip">请在<span id="payment_tip">10分钟</span>内完成支付</p>
        <div class="payment-btn" onclick="payBtn()">支付</div>
    </div>

    <input type="hidden" id="payment" value="${payParam.payment}">
    <input type="hidden" id="orderNum" value="${payParam.orderNum}">
    <input type="hidden" id="appId" value="${payParam.appId}">
    <input type="hidden" id="nonceStr" value="${payParam.nonceStr}">
    <input type="hidden" id="prepayId" value="${payParam.prepayId}">
	<input type="hidden" id="paySign" value="${payParam.paySign}">
	<input type="hidden" id="timeStamp" value="${payParam.timeStamp}">
	
	<script src="/js/jquery-3.3.1.min.js"></script>
    <script>
	var isFirst = true;
	var statusTimer = null;
	var isExpire = false;
    var orderNum = $('#orderNum').val();

    $(document).ready(function() {
		initBase();
	})


	function initBase() {
        getStatus();
		statusTimer = setInterval(function() {
            getStatus();
		}, 2000);
	}

	function getStatus(){
        $.get('/pay/getPayStatus/' + orderNum ,{},function(res){
            if (res == 2) {
                window.location.href = "/pay/okh5?money=" + $('#payment').val();
                return;
            }
            if (res == 3){
                window.location.href = "/pay/expireh5";
                return;
            }
            if (isFirst) {
                isFirst = false;
                payBtn();
            }
        },'json');
    }

	function payBtn() {
		if (isExpire) {
			window.location.href = "/api/expireh5";
			return;
		}
		if (typeof WeixinJSBridge == "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady',
						onBridgeReady, false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady',
						onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady',
						onBridgeReady);
			}
		} else {
			onBridgeReady();
		}
	}
	
	function onBridgeReady() {
		WeixinJSBridge.invoke('getBrandWCPayRequest', {
			"appId" : $("#appId").val(), //公众号名称，由商户传入     
			"timeStamp" : $("#timeStamp").val(), //时间戳，自1970年以来的秒数     
			"nonceStr" : $("#nonceStr").val(), //随机串     
			"package" : $("#prepayId").val(),
			"signType" : "MD5", //微信签名方式：     
			"paySign" : $("#paySign").val()
		//微信签名 
		}, function(res) {
			if (res.err_msg == "get_brand_wcpay_request:ok") {// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
                window.location.href = "/pay/okh5?money=" + $('#payment').val();
			}
		});
	}
</script>
</body>
</html>
