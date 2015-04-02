<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ page session="true" %>
     
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css" />

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
 

 
<!-- <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
 -->
<%
    /*Logger logger = LoggerFactory.getLogger(this.getClass());*/
    // response.setHeader("Cache-Control","no-store");   
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");   
    response.setDateHeader("Expires",0);   
    if (request.getProtocol().equals("HTTP/1.1")) 
		response.setHeader("Cache-Control", "no-cache");
    
    pageContext.setAttribute("cr", "\r");
    pageContext.setAttribute("lf", "\n");
    pageContext.setAttribute("crlf", "\r\n");
%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>