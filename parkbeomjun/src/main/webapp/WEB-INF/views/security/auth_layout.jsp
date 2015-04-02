<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<!DOCTYPE html>
<html lang="ko">
<head>
 	<jsp:include page="include/initSecurity.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
</head>
<body>

<div class="container">
<%// TOP %>
<div style="width:100%;height: 10%;">
	<tiles:insertAttribute name="top"/>
</div>

<%// LEFT %>
<div style="float:left;width:40%;">
	<tiles:insertAttribute name="left"/>
</div>

<%// CENTER %>
<div style="float:left;width:60%;">
	<tiles:insertAttribute name="center"/>
</div>


<%// BOTTOM %>
<div style="clear:both;width:100%;height: 10%;">
	<tiles:insertAttribute name="bottom"/>
</div>

</div>



</body>
</html>