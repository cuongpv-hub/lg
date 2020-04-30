<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp" %>

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
	
	<!-- Bootstrap 3.3.7 -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">

	<!-- Font Awesome -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">

	<!-- Ionicons -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/ionicons.min.css">
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-select.min.css">
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-datepicker.min.css">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-colorpicker.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/control-style.css">
    
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
    
    <tiles:importAttribute name="stylesheets" />
	<tiles:importAttribute name="javascripts" />
	
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

<body class="hold-transition skin-blue sidebar-mini">

	<!-- Site wrapper -->
	<div class="wrapper">
		<jsp:include page="admin-header.jsp"></jsp:include>
		
		<!-- Left side column. contains the sidebar -->
	  	<jsp:include page="admin-menu.jsp"></jsp:include>
	  	
		<!-- Content Wrapper. Contains page content -->
		<tiles:insertAttribute name="body" />
	
		<!-- Footer -->
		<tiles:insertAttribute name="footer" />
	
		<!-- Control Sidebar -->
		<%-- <jsp:include page="admin-control.jsp"></jsp:include> --%>
	  	
		<!-- Add the sidebar's background. This div must be placed
	       immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>
	</div>

	<!-- Select Picker -->
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap-select.min.js"></script>
	
	<!-- Date Picker -->
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.vi.min.js"></script>

	<!-- Color Picker -->
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap-colorpicker.min.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/autoNumeric.min.js"></script>
	
	<!-- SlimScroll -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery.slimscroll.min.js"></script>
	
	<!-- FastClick -->
	<script src="${pageContext.request.contextPath}/resources/js/fastclick.js"></script>

	<!-- AdminLTE App - Added in Template attribute-->
	<!-- AdminLTE for demo purposes - Added in Template attribute -->

	<!-- 	All JS Files -->
	<c:forEach var="script" items="${javascripts}">
		<script src="<c:url value="${script}"/>"></script>
	</c:forEach> 
</body>
</html>
