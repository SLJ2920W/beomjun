<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script type="text/javascript">
	$(function(){
		var thisObj = $(".input-group");
		$(thisObj).find("button").click(function(){
			var o = $(this).parent().parent();
			var rn = $(o).find("input[name='resource_name']").val();
			var rp = $(o).find("input[name='resource_pattern']").val();
			var i =  $(o).find("input[name='idx']").val();
			//form serialize?...
			$.ajax({
				type : "post",
				dataType : "json",
				url : "auth/urlUpdateAjax",
				data : {
						 resource_name : rn
					,	resource_pattern : rp
					,	idx : i
				},
				error : function(){
					alert("서버와 통신중에 에러가 발생하였습니다.");
				},				
				success : function(d) {
					alert("수정 하였습니다");
				}
			});
		});
	});
</script>


<h1>Spring MVC - View (JSON, XML, PDF, Excel)</h1>
<div style="margin:10px;width:700px;text-align:center">    
    <a href="${rootPath}/security/get.json" class="label label-info">JSON</a>
    <a href="${rootPath}/security/get.xml" class="label label-info">XML</a>
    <a href="${rootPath}/security/get.pdf" class="label label-info">PDF</a>
    <a href="${rootPath}/security/get.xlsx" class="label label-info">Excel</a>
</div>


<c:forEach items="${urlList_1}" var="i">
	<div class="panel panel-info">
		<div class="panel-heading">${i.resource_name}</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-lg-6">
					<div class="input-group">
						<input type="text" class="form-control"  name="resource_name" value="${i.resource_name}">
						<input type="hidden" class="form-control" name="resource_pattern" value="${i.resource_pattern}">
						<input type="hidden" class="form-control" name="idx" value="${i.idx}">
				      	<span class="input-group-btn">
			        		<button class="btn btn-default" type="button">이름 수정</button>
				      	</span>
			    	</div>
			  	</div>
			  	<div class="col-lg-6">
			    	<div class="input-group">
				      	<input type="text" class="form-control" name="resource_pattern"  value="${i.resource_pattern}">
						<input type="hidden" class="form-control" name="resource_name" value="${i.resource_name}">
			      		<input type="hidden" class="form-control" name="idx" value="${i.idx}">
			      		<span class="input-group-btn">
			        		<button class="btn btn-default" type="button">URL 수정</button>
			      		</span>
			    	</div>
				</div>
			</div>
		</div>
	</div>
</c:forEach>



