<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>

<header class="main-header">
	<!-- Logo -->
	<a href="${pageContext.request.contextPath}/home" class="logo">
		<span class="logo-mini"><b>CB</b></span>
		<span class="logo-lg"><b>AgrolinkCB</b></span>
	</a>
	
	<nav class="navbar navbar-static-top">
		<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
			<span class="sr-only">Toggle navigation</span>
		</a>

		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
				<li class="dropdown user user-menu">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						<c:choose>
							<c:when test="${ currentUser.avatar == null || currentUser.avatar == '' }">
								<img src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg"
									class="user-image" alt="User Image">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/upload/avatar/${ currentUser.avatar }"
									class="user-image" alt="User Image">
							</c:otherwise>
						</c:choose>
						<span class="hidden-xs"><c:out value="${currentUser.name}" /></span>
					</a>
					
					<ul class="dropdown-menu">
						<!-- User image -->
						<li class="user-header">
							<c:choose>
								<c:when test="${ currentUser.avatar == null || currentUser.avatar == '' }">
									<img src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg"
										class="img-circle" alt="User Image">
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}/upload/avatar/${ currentUser.avatar }"
										class="img-circle" alt="User Image">
								</c:otherwise>
							</c:choose>
							<p>
								<c:out value="${currentUser.name}" /><br>
								<c:out value="${currentUser.email}" />
							</p>
						</li>

						<li class="user-footer">
							<div class="pull-left">
								<a class="btn btn-default btn-flat" href="${pageContext.servletContext.contextPath}/profile">Thông tin cá nhân</a>
							</div>
							<div class="pull-right">
								<a class="btn btn-default btn-flat" href="${pageContext.servletContext.contextPath}/logout">Đăng xuất</a>
							</div>
						</li>
					</ul>
				</li>

				<!-- Control Sidebar Toggle Button -->
				<!--
				<li><a href="#" data-toggle="control-sidebar">
					<i class="fa fa-gears"></i>
				</a></li>
				-->
			</ul>
		</div>
	</nav>
</header>