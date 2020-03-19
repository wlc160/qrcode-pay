<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>、
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <title></title>
</head>
<body>
<script src="/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	aClick();
});
function aClick () { 
    window.location = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${payParam.appId}&redirect_uri=${payParam.url}&response_type=code&scope=snsapi_base&state=${payParam.state}#wechat_redirect";
}  
</script>
</body>
</html>