<%@page import="com.peoplewiki.security.springSecurity.domain.MemberInfo"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<style type="text/css">
.tree{
	background-color: #FFFFFF ;
	border: 0px !important;
	margin-bottom:30px;
}
.tree li {
    list-style-type:none;
    margin:0;
    padding:10px 5px 0 5px;
    position:relative
}
.tree ul:first{padding-left:0px !important;}
.tree li::before, .tree li::after {
    content:'';
    left:-20px;
    position:absolute;
    right:auto
}
.tree li::before {
    border-left:1px solid #999;
    bottom:50px;
    height:100%;
    top:0;
    width:1px
}
.tree li::after {
    border-top:1px solid #999;
    height:20px;
    top:25px;
    width:25px
}
.tree li span {
    -moz-border-radius:5px;
    -webkit-border-radius:5px;
    border:1px solid #999;
    border-radius:5px;
    display:inline-block;
    padding:3px 8px;
    text-decoration:none
}

.tree li > span {
    cursor:pointer
}
.tree>ul>li::before, .tree>ul>li::after {
    border:0
}
.tree li:last-child::before {
    height:30px
}
.tree li > span:hover, .tree li > span:hover+ul li span {
    background:#eee;
    border:1px solid #94a0b4;
    color:#000
}
.active{
	background:#eee;
    border:1px solid #94a0b4;
    color:#000
}

</style>


<script type="text/javascript">
$(function () {
    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', '접기');
    $('.tree li.parent_li > span').find("i").on('click', function (e) {
    	var t = $(this).parent();
        var children = $(t).parent('li.parent_li').find(' > ul > li');
        if (children.is(":visible")) {
            children.hide('fast');
            $(t).attr('title', '펼치기').find(' > i').addClass('glyphicon-plus-sign').removeClass('glyphicon-minus-sign');
        } else {
            children.show('fast');
            $(t).attr('title', '접기').find(' > i').addClass('glyphicon-minus-sign').removeClass('glyphicon-plus-sign');
        }
        e.stopPropagation();
    });
});
</script>

<i class="glyphicon glyphicon-certificate"></i>
<i class="glyphicon glyphicon-folder-open"></i>
<i class="glyphicon glyphicon-minus-sign"></i>
<i class="glyphicon glyphicon-leaf"></i>
아이콘을 클릭하면 펼치고 닫기 실행

<div class="tree well">
	<ul class="parent_shape"> 
		<li>
			<span><i class="glyphicon glyphicon-certificate"></i> <a href="${rootPath}/security/auth">메뉴 관리</a></span> 
			<ul>
				<c:forEach items="${urlList_1}" var="i">
		        <li>
		            <span class="${resource_id == i.resource_id ? 'active' : '' }"><i class="glyphicon glyphicon-folder-open"></i> <a href="${rootPath}/security/auth/${i.resource_id}">${i.resource_name}</a></span>
					<ul>
		            <c:forEach items="${urlList_2}" var="k">	 
		            	<c:if test="${i.idx == k.sort_group}">
		                <li>
		                	<span  class="${resource_id == k.resource_id ? 'active' : '' }"><i class="glyphicon glyphicon-minus-sign"></i> <a href="${rootPath}/security/auth/${k.resource_id}">${k.resource_name }</a></span>
		                	<ul>
				            <c:forEach items="${urlList_3}" var="z">	 
				            	<c:if test="${k.idx == z.sort_group}">			                	
		                        <li>
			                        <span  class="${resource_id == z.resource_id ? 'active' : '' }"><i class="glyphicon glyphicon-minus-sign"></i> <a href="${rootPath}/security/auth/${z.resource_id}">${z.resource_name }</a></span>
				                	<ul>
						            <c:forEach items="${urlList_4}" var="y">	 
						            	<c:if test="${z.idx == y.sort_group}">			                	
				                        <li>
					                        <span  class="${resource_id == y.resource_id ? 'active' : '' }"><i class="glyphicon glyphicon-leaf"></i> <a href="${rootPath}/security/auth/${y.resource_id}">${y.resource_name }</a></span>
				                        </li> 
										</c:if>
									</c:forEach> 
									</ul>
		                        </li> 
								</c:if>
							</c:forEach> 
							</ul>
				        </li>
					    </c:if>
				    </c:forEach>
					</ul>
		        </li>
		        </c:forEach>
		    </ul>
		</li> 
	</ul>	    
</div>


<h1>ContentEditable Autocorrection Test</h1>
<div id="editor" accesskey="s" contextmenu="">
	alt+s accesskey단축키
</div>
