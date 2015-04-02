<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script type="text/javascript">
$(function() {
    $('.form-control').tooltip();
    
});
</script>

<style type="text/css">
	.btn-info{
		opacity: 0.3;
	}
	.btn-info.active{
		opacity: 1;
	}

</style>
  
<form action="${rootPath}/security/auth/update" method="post">

<div class="page-header">
  <h1>${urlView.resource_name}<br><small>${urlView.resource_pattern}</small></h1>
</div>

	<div class="input-group">
	  <span class="input-group-addon">이름</span>
	  <input type="text" name="resource_name" class="form-control" title="값을 입력하지 않으면 수정 되지 않음"  data-placement="left" placeholder="${urlView.resource_name}">
	</div> 
	<div class="input-group">
	  <span class="input-group-addon">URL</span>
	  <input type="text" name="resource_pattern" class="form-control" title="값을 입력하지 않으면 수정 되지 않음"  data-placement="left" placeholder="${urlView.resource_pattern}">
	</div> 
	
	<br>
	<c:if test="${empty urlListView }">
		<div class="alert alert-danger">설정된 권한 없음</div>
	</c:if>
	
		<div class="alert alert-info"> 
			<input type="checkbox" name="authority_children_apply" value="true" data-toggle="toggle" data-on="하위 모든 URL 적용" data-off="하위 모든 URL미 적용" data-onstyle="danger" data-offstyle="default">
			&nbsp;&nbsp;
			<input type="checkbox" name="useYN" value="Y" ${urlView.useYN  == 'Y' ? 'checked=checked' : ''} data-toggle="toggle" data-on="해당 URL 메뉴로 사용" data-off="해당 URL 메뉴로 미사용" data-onstyle="success" data-offstyle="default">
			&nbsp;&nbsp;
			<input type="checkbox" name="menuYN" value="Y" ${urlView.menuYN == 'Y' ? 'checked=checked' : '' } data-toggle="toggle" data-on="해당 URL 활성화" data-off="해당 URL 비 활성화" data-onstyle="warning" data-offstyle="default">
		</div>
		<div class="btn-group alert alert-warning" data-toggle="buttons">
			<c:forEach items="${authList}" var="i">
				<c:set value="false" var="flag"/>
				<c:set value="" var="chk"/>
				<c:set value="" var="chkStyle"/>
				<c:forEach items="${urlListView}" var="z">
					<c:if test="${z.authority == i.authority && flag == 'false'}">
						<c:set value="checked=checked" var="chk"/>
						<c:set value="active" var="chkStyle"/>
						<c:set value="true" var="flag"/>
					</c:if>	
				</c:forEach>
				
					<label class="btn btn-info ${chkStyle}" style="margin:1px 0px">
				    	<input type="checkbox" name="authority_arr" value="${i.authority}" ${chk} > ${i.authority_name}
				  	</label>
			</c:forEach>
		</div>
	
	<input type="hidden" name="sort_parent" value="${urlView.sort_parent == 0 ? urlView.idx : urlView.sort_parent}">
	<input type="hidden" name="resource_id" value="${urlView.resource_id}">
	<input type="hidden" name="idx" value="${urlView.idx}">
	<input type="hidden" name="sort_depth" value="${urlView.sort_depth}"> 
	<input class="btn btn-success btn-lg btn-block" type="submit" value="변경 사항 적용">

</form>

<hr>

<c:if test="${urlView.sort_depth != maxDepth}">
	<div class="panel panel-default">
		<div class="panel-heading">하위 URL 추가</div>
		<div class="panel-body">
			<form class="form-horizontal" role="form" action="${rootPath}/security/auth/insert" method="post">
				<div class="form-group">
					<label for="inputEmail1" class="col-lg-2 control-label">이름</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="inputEmail1" name="resource_name" placeholder="url의 이름">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword1" class="col-lg-2 control-label">URL</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="inputPassword1" name="resource_pattern" placeholder="/security/url...의 형식으로">
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="menuYN" value="Y"> 메뉴로 사용
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input type="checkbox" name="useYN" value="Y"> 비 활성화
							</label>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-offset-2 col-lg-10">
						<button type="submit" class="btn btn-default">등록</button>
					</div>
				</div>
				
				<input type="hidden" name="sort_parent" value="${urlView.sort_parent == 0 ? urlView.idx : urlView.sort_parent}"> 
				<input type="hidden" name="sort_group" value="${urlView.idx}"> 
				<input type="hidden" name="sort_depth" value="${urlView.sort_depth + 1}"> 
				<input type="hidden" name="resource_type" value="url"> 
			</form>
		</div>
	</div>
</c:if>

	 
<!-- ( resource_id, resource_name, resource_pattern, resource_type, sort_order, sort_parent, sort_group, sort_depth, useYN, menuYN ) -->
