<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>

<aside class="main-sidebar">
	<section class="sidebar">
		<div class="user-panel">
			<div class="pull-left image">
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
			</div>
			<div class="pull-left info">
				<p>
					<c:out value="${currentUser.name}" />
				</p>
				<a href="javascript:;"><i class="fa fa-circle text-success"></i>Quản trị hệ thống</a>
			</div>
		</div>
		
		<!-- search form -->
		<!-- 
		<form action="#" method="get" class="sidebar-form">
			<div class="input-group">
				<input type="text" name="q" class="form-control" placeholder="Search...">
				<span class="input-group-btn">
					<button type="submit" name="search" id="search-btn" class="btn btn-flat">
						<i class="fa fa-search"></i>
					</button>
				</span>
			</div>
		</form>
		-->
		<!-- /.search form -->
		
		<ul class="sidebar-menu">
			<li class="header">CHỨC NĂNG CHÍNH</li>
			<li>
				<a href="${pageContext.request.contextPath}/manager/common-category">
					<i class="fa fa-dashboard"></i>
					<span>Danh mục dùng chung</span>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/manager/user">
					<i class="fa fa-th"></i>
					<span>Người sử dụng</span>
				</a>
			</li>
		</ul>
	</section>
</aside>