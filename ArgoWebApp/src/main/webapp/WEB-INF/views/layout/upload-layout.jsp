<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>

<%
   response.setHeader( "Pragma", "no-cache" );
   response.setHeader( "Cache-Control", "no-cache" );
   response.setDateHeader( "Expires", 0 );
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="description" content="By VSD Ltd." />
    <meta name="author" content="VSD Ltd." />
	
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
 
    <link href="favicon.ico" rel="shortcut icon">

    <!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resources/js/ie/es5-shim-4.5.9.min.js"></script>
        <script>
            document.createElement('ui-select');
            document.createElement('ui-select-match');
            document.createElement('ui-select-choices');
        </script>
    <![endif]-->
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
        <script src="${pageContext.request.contextPath}/resources/js/ie/html5shiv-3.7.3.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/ie/respond-1.4.2.min.js"></script>
    <![endif]-->
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">	
	
	<!-- Font Awesome -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">

	<tilesx:useAttribute id="stylesheets" name="stylesheets" classname="java.util.List" />
	<tilesx:useAttribute id="javascripts" name="javascripts" classname="java.util.List" />
	<c:forEach var="css" items="${stylesheets}">
    	<link rel="stylesheet" type="text/css" href="<c:url value="${css}"/>">
    </c:forEach>
	
	<c:set var="titleKey"><tiles:getAsString name="title"/></c:set>
	<title><spring:message code="${titleKey}"></spring:message></title>
	
	<!-- jQuery 3.3.1 -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	
	<!-- Bootstrap 3.3.7 -->
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
</head>
<body>
	<tiles:insertAttribute name="body" />
	
	<!-- 	All JS Files -->
	<c:forEach var="script" items="${javascripts}">
    	<script src="<c:url value="${script}"/>"></script>
	</c:forEach>
</body>
</html>