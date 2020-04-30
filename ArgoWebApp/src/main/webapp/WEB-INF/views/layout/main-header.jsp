<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>

<c:set var="activeMenu"><tiles:getAsString name="menu"/></c:set>

<div class="topbar clearfix" id="topbar">
    <div class="container">
        <div class="row-fluid">
            <div class="col-md-6 text-left">
                <div class="social">
                    <a href="#" data-tooltip="tooltip" data-placement="bottom" title="Facebook"><i class="fa fa-facebook"></i></a>
                    <a href="#" data-tooltip="tooltip" data-placement="bottom" title="Youtube"><i class="fa fa-youtube"></i></a>
                    <div>
                        <strong><i class="fa fa-phone"></i></strong>
                        <a href="tel:01676391060">01676391060</a>
                    </div>
                    <div>
                        <strong><i class="fa fa-envelope"></i></strong>
                        <a href="mailto:agrolink@caobang.gov.vn"><span>agrolink@caobang.gov.vn</span></a>
                    </div>
                </div><!-- end social -->
            </div><!-- end left -->
            <div class="col-md-6 text-right right-menu">
                <ul class="list-inline act-auth">
                	<c:choose>
    					<c:when test="${ currentUser == null }">
		                    <li class="ac-item">
		                    	<a href="${pageContext.request.contextPath}/login" title="Đăng nhập hệ thống">Đăng nhập</a>
		                    </li>
		                    <li class="ac-item">
		                    	<a href="${pageContext.request.contextPath}/register" title="Đăng ký tài khoản mới">Đăng ký</a>
		                    </li>
	                    </c:when>
	                    <c:otherwise>
	                    	<li class="ac-item">
		                    	<a href="${pageContext.request.contextPath}/profile" title="Thông tin tài khoản">${ currentUser.name }</a>
		                    </li>
		                    
		                    <c:if test="${ currentUser.isAdministrator() || currentUser.isDepartmentAdministrator() || currentUser.isCompany() }">
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/project" title="Quản lý dự án">Quản lý dự án</a>
			                    </li>
		                    </c:if>
		                    
		                    <c:if test="${ currentUser.isAdministrator() || currentUser.isDepartmentAdministrator() || currentUser.isFarmer() }">
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/product" title="Quản lý nông sản">Quản lý nông sản</a>
			                    </li>
			                    
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/land" title="Quản lý thửa đất">Quản lý thửa đất</a>
			                    </li>
		                    </c:if>
		                    
		                    <c:if test="${ currentUser.isAdministrator() || currentUser.isDepartmentAdministrator() || currentUser.isScientist() }">
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/article" title="Quản lý bài viết">Quản lý bài viết</a>
			                    </li>
		                    </c:if>
		                    
		                    <c:if test="${ currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()}">
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/news" title="Quản lý trang tin nội bộ">Quản lý trang tin địa phương</a>
			                    </li>
		                    </c:if>
		                    
		                    <c:if test="${ currentUser.isAdministrator() || currentUser.isDepartmentAdministrator() }">
			                    <li class="ac-item">
			                    	<a href="${pageContext.request.contextPath}/manager" title="Quản lý trị hệ thống">Quản trị hệ thống</a>
			                    </li>
		                    </c:if>
		                    
		                    <li class="ac-item">
		                    	<a href="${pageContext.request.contextPath}/logout" title="Đăng xuất">Đăng xuất</a>
		                    </li>
	                    </c:otherwise>
					</c:choose>
				</ul>
            </div><!-- end left -->
        </div><!-- end row -->
    </div><!-- end container -->
</div>

<header class="header">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-12">
            	<nav class="navbar navbar-default">
					<div class="container-fluid">
						<div class="navbar-cell">
							<div class="navbar-header">
								<a class="navbar-brand" href="${pageContext.request.contextPath}">
									<img src="${pageContext.request.contextPath}/resources/images/logo.png" alt="Agrolink">
								</a>
								<div>
									<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#header-navbar-collapse" aria-expanded="false">
										<span class="sr-only">Toggle navigation</span>
										<span class="fa fa-bars"></span>
									</button>
								</div>
							</div>
						</div>
						
						<div class="navbar-cell stretch">
							<div class="collapse navbar-collapse" id="header-navbar-collapse">
								<div class="navbar-cell">
									<ul class="nav navbar-nav navbar-right">
										<li <c:if test="${ activeMenu == '' || activeMenu == 'home' }">class="active"</c:if>>
											<a href="${pageContext.request.contextPath}/">Trang chủ</a>
										</li>
										<li <c:if test="${ activeMenu == 'company' }">class="active"</c:if>>
											<a href="${pageContext.request.contextPath}/company" >Doanh nghiệp</a>
										</li>
										<li <c:if test="${ activeMenu == 'farmer' }">class="active"</c:if>>
											<a href="${pageContext.request.contextPath}/farmer" >Nhà nông</a>
										</li>
										<li <c:if test="${ activeMenu == 'scientist' }">class="active"</c:if>>
											<a href="${pageContext.request.contextPath}/scientist" >Nhà khoa học</a>
										</li>
										<li <c:if test="${ activeMenu == 'administrative' }">class="active"</c:if>>
											<a href="${pageContext.request.contextPath}/administrative" >Nhà nước</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
            		</div>
            	</nav>
            </div><!-- end col -->
        </div><!-- end row -->
    </div><!-- end container -->
</header>

<script type="text/javascript">
	$(function() {
		$('.header .navbar-default .nav li').hover(function () {
		    $(this).addClass('hover');
		}, function () {
		    $(this).removeClass('hover');
		});
	});
</script>