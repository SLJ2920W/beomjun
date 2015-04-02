<%@page import="com.peoplewiki.security.springSecurity.domain.MemberInfo"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String name = "";
try{
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

Object principal = auth.getPrincipal();	// 로그인 하지 않았을 경우 anonymousUser 란 String 객체가 리턴된다(로그인 했으면 MemberInfo 객체)
if(principal != null && principal instanceof MemberInfo){
	name = ((MemberInfo)principal).getName();
}
}catch(Exception e){
	e.printStackTrace();
}


%>


	<sec:authorize access="isAnonymous()">
		<form name="" action="${rootPath}/security/user/login" method="post">
			ID : <input type="text" name="loginid" value="test"><br>
			PW : <input type="password" name="loginpwd" value="test1password">
		<input type="submit" value="로그인">	
		</form>
	</sec:authorize>
	
	<sec:authorize access="isAuthenticated()">
	<%=name%>님 반갑습니다<br/>
	
	<a href="${rootPath}/j_spring_security_logout">로그아웃</a>
	</sec:authorize>
	<ul>
		<!-- test - ROLE_ADMIN -->
    	<sec:authorize access="hasRole('ROLE_ADMIN')">
        <li>관리자 화면</li>
        </sec:authorize>
        <sec:authorize access="permitAll">
        <li>비회원 게시판</li>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
        <li>준회원 게시판</li>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_MEMBER2', 'ROLE_ADMIN')">
        <li>정회원 게시판</li>
        </sec:authorize>
    </ul>  
	<c:if test="${not empty securityexceptionmsg}">
		<div style="color:red;">
			<div>Your login attempt was not successful, try again.</div>
			<div>${securityexceptionmsg}</div>
		</div>
	</c:if>

<p>메시지 소스 테스트</p>
<spring:message code="AclEntryAfterInvocationProvider.noPermission" arguments="메롱,ㅋㅋ"  text="null.."/>
<spring:message code="DigestAuthenticationFilter.incorrectRealm" arguments="홍길동,이순신"  text="null.."/>
<spring:message code="DigestAuthenticationFilter.incorrectRealm424" arguments="메롱,ㅋㅋ"  text="null.."/>
...
${DigestAuthenticationFilter.usernameNotFound}
<spring:eval expression="@info['admin.cell.phone']"/>

...
