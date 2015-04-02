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
<script type="text/javascript" src="http://markusslima.github.io/bootstrap-filestyle/js/bootstrap-filestyle.min.js"></script>

<script type="text/javascript">
	$(function(){
		$(":file").filestyle({buttonText: "Find file"});
		
		
		$("button[type='submit']").click(function(){
			$("#hiddenDivLoading").show();
		 	var w = $(window).width();
	        var h = $(window).height();
	        $("#hiddenDivLoading").css({
	        		width : w
	        	,	height : h
	        })
		})
	});
</script>
<style type="text/css">
.input-group {
	width: 300px;
}
.area{
	float: left;
	margin: 50px;
}

</style>

</head>
<body>

<div class="container">

<div class="area" >
<form action="${rootPath}/excel/home/up1" method="post" enctype="multipart/form-data" >
<!-- 	서식1 : <input type="file" class="filestyle" name="files" data-buttonText="Find file"> -->
	서식1 : <input type="file"  name="files" id="exampleInputFile">
	<br>
	
	<input type="radio" name="flag" value="1" id="delT" checked="checked"><label for="delT">데이터 삭제</label> 
	<input type="radio" name="flag" value="0" id="delF"><label for="delF">데이터  유지</label>
	
	<button type="submit" class="btn btn-primary">Submit</button>
</form> 
<br><br>
<a href="${rootPath}/excel/home/1" class="btn btn-primary btn-lg " role="button">서식1 리스트</a>
</div>

<div class="area" >
<form action="${rootPath}/excel/home/up2" method="post" enctype="multipart/form-data">
	서식2 : <input type="file" class="filestyle" name="files" data-buttonText="Find file">
	<br>
	<input type="radio" name="flag" value="1" id="delTT" checked="checked"><label for="delTT">데이터 삭제</label> 
	<input type="radio" name="flag" value="0" id="delFF"><label for="delFF">데이터  유지</label>
	<button type="submit" class="btn btn-primary">Submit</button>
</form>
<br><br>
<a href="${rootPath}/excel/home/2" class="btn btn-primary btn-lg " role="button">서식2 리스트</a>
</div>


</div>

	<div id="hiddenDivLoading" style="background-color:#000;-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=70)';filter: alpha(opacity=70); opacity: .7;position:fixed;z-index:1000;top:0px;left:0px;display:none;">
	</div>


</body>
</html>
