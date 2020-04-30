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
    
	
    <tiles:importAttribute name="stylesheets" />
	<tiles:importAttribute name="javascripts" />
   
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap-select.min.css">
	
	<!-- Font Awesome -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/carousel.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/control-style.css">
	
	<c:forEach var="css" items="${stylesheets}">
		<link rel="stylesheet" type="text/css" href="<c:url value="${css}"/>">
	</c:forEach>

	<c:set var="titleKey"><tiles:getAsString name="title"/></c:set>
	<title><spring:message code="${titleKey}"></spring:message></title>
	
	<!-- jQuery 3.3.1 -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
	
	<!-- Bootstrap 3.3.7 -->
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
	
	<script type="text/javascript">
		var CKEDITOR_IMAGE_MANAGER_BASE_URL = "${pageContext.request.contextPath}/file-upload/"; 
	</script>
</head>

<body data-custom-background data-off-canvas-nav ng-app="agro">
	<!-- Site wrapper -->
	<div class="wrapper">
		<jsp:include page="main-header.jsp"></jsp:include>
		
		<!-- 
		<div id="loader" stylex="display: none;">
			<div class="loader-container">
				<img src="${pageContext.request.contextPath}/resources/images/load.gif" alt="" class="loader-site spinner">
			</div>
		</div>
		-->
		
		<!-- Content Wrapper. Contains page content -->
		<tiles:insertAttribute name="body" />
	
		<!-- Footer -->
		<!--<tiles:insertAttribute name="footer" />-->
		<jsp:include page="main-footer.jsp"></jsp:include>
	  	
		<!-- Add the sidebar's background. This div must be placed
	       immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>
	</div>
	
    <!-- AngularJS -->
	<script src="${pageContext.request.contextPath}/resources/js/angular.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/angular-animate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/angular-route.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/angular-sanitize.min.js"></script>
	
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

	<!-- UI-Select -->
	<script src="${pageContext.request.contextPath}/resources/js/select.min.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/directive/ng-last-repeat-directive.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/directive/ui-directive.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/select-util.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>

	<!-- 	All JS Files -->
	<c:forEach var="script" items="${javascripts}">
		<script src="<c:url value="${script}"/>"></script>
    </c:forEach>
    
    <script type="text/javascript">
    	function checkImageSource(imgEl) {
    		if ((typeof imgEl.naturalWidth != "undefined" && imgEl.naturalWidth == 0) 
		        	|| imgEl.readyState == 'uninitialized' ) {
		    	var src = $(imgEl).attr('src');
				var errorSrc = $(imgEl).attr('errorSrc');
				
				if (errorSrc != null && errorSrc != undefined && errorSrc != src) {
					$(imgEl).unbind("error").attr("src", errorSrc);
				}
		    }
    	}
    	
		$(function() {
			$('img.with-error-src').each(function() {
				checkImageSource(this);
			});
		});
	</script>
    
</body>
</html>