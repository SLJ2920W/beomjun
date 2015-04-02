<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  	<meta name="robots" content="noindex,nofollow" />
  		<title>400</title>
		<style>
			body {background: #E8EBFE;margin: 0; padding: 20px; text-align:center; font-family:Arial, Helvetica, sans-serif; font-size:14px; color:#666666;}
			.error_page {width: 600px; padding: 50px; margin: auto;}
			.error_page h1 {margin: 20px 0 0;}
			.error_page p {margin: 10px 0; padding: 0;}		
			a {color: #7570F5; text-decoration:none;}
			a:hover {color: #3B51F5; text-decoration:underline;}
		</style>

</head>

<body class="login">
  <div class="error_page">
    <img src="${rootPath}/resources/images/common/404.gif" />
    <h1>오류(400)</h1>
    <p>잘못된 요청입니다.</p>
    <p>도움이 필요한 경우 도움말을 참조하거나 홈으로 돌아가세요.</p>
    <p><a href="${pageContext.request.contextPath}/">Return to the HOMEPAGE</a></p>    
  </div>
</body>
</html>