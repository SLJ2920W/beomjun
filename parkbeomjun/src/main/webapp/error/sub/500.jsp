<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  	<meta name="robots" content="noindex,nofollow" />
	<link href="${pageContext.request.contextPath}/images/1389273603_134229.ico" type="image/x-icon" rel="shortcut icon" />
  
  		<title>500</title>
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
    <h1>오류(500)</h1>
    <p>서버/어플리케이션에 장애가 발생하였습니다.</p>
    <p>관리자 ☎ <spring:eval expression="@info['admin.cell.phone']"/></p>
  </div>
</body>
</html>