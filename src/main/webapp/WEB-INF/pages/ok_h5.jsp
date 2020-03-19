<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>支付成功页面</title>
<link rel="stylesheet" href="/css/h5/reset.css">
<link rel="stylesheet" href="/css/h5/border.css">
<style>
html, body {
	width: 100%;
	height: 100%;
	font-family: '微软雅黑';
}

* {
	box-sizing: border-box;
}

.payment-fail-body {
	overflow: hidden;
	width: 100%;
	height: inherit;
	text-align: center;
}

.order-fail-img {
	display: block;
	width: 2.9rem;
	margin: 2.85rem auto 0 auto;
}

.order-succ-img {
	margin-top: 1.6rem;
}

.order-desc {
	padding-top: .7rem;
	font-size: .36rem;
	color: #303030;
}

.order-finished {
	display: -webkit-flex;
	display: -ms-flex;
	display: -moz-flex;
	display: -o-flex;
	display: flex;
	justify-content: space-between;
	width: 100%;
	padding: .4rem;
	background-color: #eaf8f8;
	font-size: .3rem;
	margin-top: .7rem;
}

.payment-money {
	color: #30bdbb;
}
</style>
</head>
<body>
<div class="payment-fail-body">
	<img class="order-fail-img order-succ-img"
		src="/images/icon_smile.png" />
	<p class="order-desc">您已完成支付，请关闭页面</p>
	<div class="order-finished">
		支付金额：<span class="payment-money">￥${money}</span>
	</div>
</div>
<script src="/js/jquery-3.3.1.min.js"></script>
</body>
</html>
