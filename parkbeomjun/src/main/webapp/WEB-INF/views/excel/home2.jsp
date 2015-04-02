<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0-wip/css/bootstrap.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0-wip/js/bootstrap.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">


</head>
<body>

<div class="container">

<a href="${rootPath}/excel/home" class="btn btn-primary btn-lg " role="button">Back</a>
<br><br><br>

	<table class="table table-striped">
		<c:forEach items="${list}" var="i">
			<tr>
				<td>${i.a}</td>
				<td>${i.b}</td>
				<td>${i.c}</td>
			</tr>
		</c:forEach>
	</table>

</div>
</body>
</html>