<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
		<style type="text/css">
			div{
				font-size:10pt;
			}
			ol li{
				list-style:initial !important;
				line-height:150%;
				margin-left: 40px;
			}
			.wrapper_404{
				background-image:url(../../resources/images/common/error.jpg);
				background-repeat:no-repeat;
				height:100%;
				width:100%;
				position:fixed;
				z-index:9999;
			}
			.wrapper_error{
				-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=90)';
				filter: alpha(opacity=90); opacity: .9;position:fixed;
				z-index:9998;
				top:0px;
				left:0px;
				position:fixed;
				background-color:#fff;
			}
		</style> 
		<%-- <script src="${pageContext.request.contextPath}/js/jquery-1.8.1.min.js" type="text/javascript" charset="UTF-8"></script> --%>
		<script type="text/javascript">
			$(function(){
			 	var w = $(window).width();
		        var h = $(window).height();
		        $(".wrapper_error").css({
		        		width : w
		        	,	height : h
		        })
		        $(".wrapper_404").css({
		        		top : "20%"
		        	,	left : "3%"
		        })
			});
		
			function errorView(msg){
				alert(msg);
				return false;
			}
			
		</script>
		
</head>
<body>

<div class="wrapper_error">
	<div class="wrapper_404">
		<h2>현재 페이지 접근에 문제가 발생 하였습니다.</h2>
		<h2>문제는 다음과 같을수 있습니다.</h2>
		<ol>
			<li>자체적인 기술적 오류</li>
			<li>잘못된 접근</li>
			<li>일시적인 서버/어플리케이션 장애</li>
		</ol>
		
		<h3>요청 URI</h3>
		<c:out value="${requestScope['javax.servlet.error.request_uri']}"/>
		<br>
		<h3>발생 일자</h3>
		<%	
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		%>
		<%=sdf.format(d) %>
		<br>
		<h3>에러 타입</h3>
		<c:out value="${requestScope['javax.servlet.error.exception_type']}"/>
		<br>
		<h3>에러 내용</h3>
		<c:out value="${requestScope['javax.servlet.error.exception']}"/>
		<br>
		<h3>에러 메시지</h3>
		<pre>
		<c:out value="${requestScope['javax.servlet.error.message']}"/>
		</pre>		
		<br>
		▶▶&nbsp;<a target="_parent" href="${pageContext.request.contextPath}/">초기 페이지로 이동</a>
		<br /> 
		<!-- ▶▶&nbsp;<a href="#">현재 오류 사항을 메일 보내기</a> -->
		<br>	
	</div>
</div>




</body>
</html>