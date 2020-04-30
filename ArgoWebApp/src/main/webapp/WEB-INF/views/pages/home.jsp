<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>


<div ng-controller="HomeController" class="home-page">
	<div id="introduceCarousel" class="carousel slide" data-ride="carousel">
		<!-- Carousel indicators -->
		<ol class="carousel-indicators">
			<li data-target="#introduceCarousel" data-slide-to="0" class="active"></li>
			<li data-target="#introduceCarousel" data-slide-to="1"></li>
			<li data-target="#introduceCarousel" data-slide-to="2"></li>
		</ol>

		<!-- Wrapper for carousel items -->
		<div class="carousel-inner">
			<div class="item active">
				<img src="${pageContext.request.contextPath}/resources/images/banner-01.png" alt="AGRO Link CB">
			</div>
			<div class="item">
				<img src="${pageContext.request.contextPath}/resources/images/banner-02.jpg" alt="AGRO Link CB">
			</div>
			<div class="item">
				<img src="${pageContext.request.contextPath}/resources/images/banner-03.jpg" alt="AGRO Link CB">
			</div>
		</div>

		<!-- Carousel controls -->
		<a class="carousel-control left" href="#introduceCarousel" data-slide="prev">
			<span class="glyphicon glyphicon-chevron-left"></span>
		</a>
		<a class="carousel-control right" href="#introduceCarousel" data-slide="next">
			<span class="glyphicon glyphicon-chevron-right"></span>
		</a>
	</div>

	<div class="search-form">
		<div class="appoform-wrapper noborder col-md-8 col-md-offset-2">
			<div class="form-bgr">
				<form:form class="row-fluid"  action="${pageContext.request.contextPath}/home/search-redirect" 
					method="GET" modelAttribute="searchDto">
					<div class="row">
						<div class="col-md-5 col-sm-5">
							<label class="sr-only" for="keyword">Từ khóa</label>
							<form:input path="name" class="form-control" 
								placeholder="Từ khóa, ..." id="keyword" name="keyword" />
						</div>
						<div class="col-md-3 col-sm-3">
							<form:select path="categoryId" id="type" name="type" 
								class="selectpicker bs-select-hidden" data-style="btn-white"
								title="Lựa chọ thông tin">
								<form:options items="${categories}"></form:options>
							</form:select>
						</div>
						<div class="col-md-3 col-sm-3">
							<form:select path="districtIds" id="districtId" name="districtId" 
								class="selectpicker bs-select-hidden"  data-style="btn-white"
								title="Lựa chọn quận/huyện">
								<option class="text-placeholder" value="">Tất cả quận/huyện</option>
								<form:options items="${districts}"></form:options>
							</form:select>
						</div>
						<div class="col-md-1 col-sm-1 form-action">
							<button type="submit" class="btn btn-primary btn-search-submit pull-left">
								<i class="fa fa-search" aria-hidden="true"></i>
							</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	
	<div>
		<section class="section">
			<div class="container-fluid" id="introduce">
				<div class="container">
					<div class="general-title text-center" id="slogan">
						<h4 class="top-title">Tỉnh Cao Bằng</h4>
						<p class="lead small-title">Hệ thống liên kết sản xuất nông nghiệp theo chuỗi giá trị</p>
						<hr>
					</div>
					<!-- end general title -->
					
					<div class="row">
						<div class="col-md-5 col-xs-12">
							<div class="row" id="list-district" class="list-district">
								<div class="padding-left-60 padding-right-60">
									<c:forEach items="${districts}" var="district" varStatus="loop">
										<c:choose>
											<c:when test="${ loop.index == 0 }">
												<div class="commune-item pointer col-xs-12 text-bold">
											</c:when>
											<c:otherwise>
												<div class="commune-item pointer col-xs-6 pull-left">
											</c:otherwise>
										</c:choose>
											<a href="">
												- ${district.value.name}
											</a>
										</div>
									</c:forEach>										
								</div>
							</div>
							
							<div class="row" id="list-hightlight-product" class="list-hightlight-product">
								<div class="padding-left-5 padding-right-5">
									<div class="col-xs-12">
										<div id="agricultural-special" class="text-center">
											<div class="text-center " id="title-agricultural-special">
												Các nông sản tiêu biểu
											</div>
										</div>
									</div>
									
									<div ng-repeat="product in hightlightProducts" class="col-md-6 col-xs-12 padding-2">
										<div class="hightlight-product-item">
											<a href="${pageContext.request.contextPath}/home/product/detail" title="{{product.name}}">
												<img src='{{product.img}}' alt="{{product.name}}">
											</a>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="col-md-7 col-xs-12">
							<img src="${pageContext.request.contextPath}/resources/images/main-image.png"
								style="width: 100%; height: 100%;">
						</div>
					</div>
				</div>
			</div>
		</section>
		<!-- end section -->
	</div>
	
	<!--du an-->
	<section class="section background-white" id="project">
		<div class="container" id="project-container">
			<div class="general-title text-center">
				<h4 class="top-title">Dự án chờ hợp tác</h4>
				<hr>
				<p class="lead">Dự án kêu gọi nhà nông, nhà khoa học cùng hợp tác phát triển</p>
			</div>
			<!-- end general title -->
			<div class="row module-wrapper" id="content-project">
				<div class="col-md-12">
					<div class="carousel slide multi-item-carousel" id="projectCarousel">
						<div class="carousel-inner padding-left-60 padding-right-60">
							<c:forEach items="${projects}" var="project" varStatus="loop" >
								<!-- <div class="item item-project">
									
								</div>
									 -->
										
										<div class="col-xs-6 col-md-4">
											<div class="item item-project">
												<div class="entry">
													<a href="${pageContext.request.contextPath}/project/${ project.id }/view" title='${project.name}'>
													<img src='${pageContext.request.contextPath}/upload/project/${project.mainImage}' class="img-responsive" alt="${project.name}">
												</a>
												<h4 class="project-name">
													<a href="${pageContext.request.contextPath}/project/${ project.id }/view" title='${project.name}'>
														<span>${project.name}</span>
													</a>
												</h4>
												</div>
												
											</div>
										</div> 
										
										
									
								</c:forEach>
							 <%-- <div ng-repeat="project in projects" class="item item-project" ng-class="{'active': $index == 0}"
								ng-last-repeat="HomePage.Project">
								<div class="col-xs-6 col-md-4">
									<div>
										<a href="${pageContext.request.contextPath}/home/project/detail" title='{{project.name}}'>
											<img src='{{project.img}}' class="img-responsive" alt="{{project.name}}">
										</a>
										<h4 class="project-name">
											<a href="${pageContext.request.contextPath}/home/project/detail" title='{{project.name}}'>
												{{project.name}}
											</a>
										</h4>
									</div>
								</div>
          					</div> --%>
						</div>
						<a class="left carousel-control" href="#projectCarousel" data-slide="prev">
							<i class="glyphicon glyphicon-chevron-left"></i>
						</a>
        				<a class="right carousel-control" href="#projectCarousel" data-slide="next">
        					<i class="glyphicon glyphicon-chevron-right"></i>
        				</a>
					</div>
				</div>
			</div>
			
			
			<!-- row -->
		</div>
		
		
		<!-- end container -->
	</section>
	<!-- end section -->
	
	
	
	
	
	<!--dat cho hop tac-->
	<section class="section" id="farmer-land">
		<div class="container box-shadown">
			<div class="general-title text-center">
				<h4 class="top-title">Đất chờ doanh nghiệp hợp tác</h4>
				<hr id="hr-farmer-land">
				<p class="lead">Hệ thống liên kết sản xuất nông nghiệp theo chuỗi giá trị</p>
			</div>
			<!-- end general title -->
			<div class="row module-wrapper" id="farmer-land-list">
				<%-- <div class="col-lg-3 col-md-4 col-sm-6 col-12">
					
					<div class="product-item">
						<a href="${pageContext.request.contextPath}/home/land/detail" title="{{item.name}}">
							<img class="img-responsive" src='{{item.img}}' alt="{{item.name}}">
						</a>
						
						<div class="info-product">
							<a href="${pageContext.request.contextPath}/home/land/detail" title="{{item.name}}">
								<h3 class="text-left name-product">{{item.name}}</h3>
							</a>
						
							<p><b>Diện tích</b> : {{ item.area }} m<sup>2</sup></p>
							<p><b>Vị trí</b> : {{ item.location }}</p>
							<p><b>Liên hệ</b> : {{ item.contact }}</p>
						</div>
					</div>
					
					
				</div> --%>
				<c:forEach items="${lands}" var="land" varStatus="loop" >
						<div class="col-md-3 col-sm-6 col-xs-12">
							<div class="land-item">
								<a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }">
									<img src="${pageContext.request.contextPath}/upload/land/${land.mainImage}" class="img-responsive" alt="${ land.name }">
								</a>
								<a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }" title="${ land.name }">
									<h3 class="name-land">${ land.name }</h3>
								</a>
								<div class="info-land">
									<div class="info-name">
										<span>Diện tích</span> :
										<span id="square" class="text-number">${ land.square }</span>
										<c:if test="${ land.squareUnit != null }">${ land.squareUnit.name }</c:if>
									</div>
								</div>
								<div class="info-land">
									<div class="pull-left location-of-land">
										<span>Vị trí</span> : 
										<span>${ land.fullAddress }</span>
									</div>
								</div>
								<div class="info-land">
									<div class="pull-left">
										<span>Liên hệ</span> : 
										<span>${ land.contact.name }</span>
									</div>
								</div>
							</div>
						</div>
				</c:forEach>
			</div>
		</div>
	</section>
	<!-- end section -->
	
	<section class="section" id="how-download">
		<div class="col-md-5 col-md-offset-7" id="content-download">
			<div>
				<span>Hệ thống kết nối bốn nhà Agrolink</span> cung cấp phiên bản Website chạy trên máy tính và App chạy trên hệ điều hành Android, iOS.
			</div>
			<div>
				Linh tải cho hệ điều hành Android : <a href="#" title="Link tải Android">Click vào đây</a>
			</div>
			<div>
				Linh tải cho hệ điều hành iOs : <a href="#" title="Link tải iOs">Click vào đây</a>
			</div>
		</div>
	</section>

	<!--nong san-->
	<section class="section" id="products">
		<div class="container box-shadown">
			<div class="general-title text-center title-agricultural-special">
				<h4 class="top-title">Sản phẩm</h4>
				<hr>
				<p class="small-title">Hệ thống liên kết sản xuất nông nghiệp theo chuỗi giá trị</p>
			</div>
			<div class="row module-wrapper" id="farmer-land-list">
				<c:forEach items="${products}" var="product" varStatus="loop" >
					<div class="col-md-3 col-sm-6 col-xs-12">
							<div class="land-item">
								<a href="${pageContext.request.contextPath}/product/${ product.id }/view" title="${ product.name }">
									<img src="${pageContext.request.contextPath}/upload/product/${product.mainImage}" class="img-responsive" alt="${ product.name }">
								</a>
								<a href="${pageContext.request.contextPath}/product/${ product.id }/view" title="${ product.name }" title="${ product.name }">
									<h3 class="name-land">${ product.name }</h3>
								</a>
								<div class="info-land">
									<div class="info-name">
										<span>Sản lượng</span> :
										<span id="square" class="text-number">${ product.volume }</span>
										<c:if test="${ product.volumeUnit != null }">${ product.volumeUnit.name }</c:if>
									</div>
								</div>
								<div class="info-land">
									<div class="pull-left location-of-land">
										<span>Vị trí</span> : 
										<span>${ product.fullAddress }</span>
									</div>
								</div>
								<div class="info-land">
									<div class="pull-left">
										<span>Liên hệ</span> : 
										<span>${ product.contact.name }</span>
									</div>
								</div>
							</div>
						</div>					
				</c:forEach>
			
				<%-- <div ng-repeat="item in products" class="col-lg-3 col-md-4 col-sm-6 col-12">
					<div class="product-item">
						<a href="${pageContext.request.contextPath}/home/product/detail" title="{{ item.name }}">
							<img class="img-responsive" src='{{ item.img }}' alt="{{ item.name }}">
						</a>
						
						<div class="info-product">
							<a href="${pageContext.request.contextPath}/home/product/detail" title="{{ item.name }}">
								<h3 class="text-center name-product">{{ item.name }}</h3>
							</a>
						
							<p><b>Nơi sản xuất</b>: {{ item.nsx }}</p>
						
							<p><b>Quy cách đóng gói</b>: {{ item.package }}</p>
							
							<p><b>Liên hệ</b>: {{ item.contact }}</p>
						</div>
					</div>
				</div> --%>
			</div>
		</div>
	</section>
	
	<!-- thu vien anh du an -->
	<!-- 
	<section class="section white">
		<div class="general-title text-center">
			<h4 class="top-title">Thư viện ảnh dự án</h4>
			<hr>
			<p class="lead">Ảnh về các dự án đang được thực hiện</p>
		</div>
		<div class="row module-wrapper grid-layout text-center">
			<div class="col-md-12 col-md-12">
				<div class="shop-item item-image" id="gallery">
				</div>
			</div>
		</div>
	</section>
	-->
	
	<!-- bai viet -->
	<section class="section">
		<div class="container box-shadown">
			<div class="general-title text-center">
				<h4 class="top-title">Bài viết của nhà khoa học</h4>
				<hr id="hr-post-scientist">
				<p class="lead">Bài viết chia sẻ kinh nghiệm canh tác, chăn
					nuôi... của các nhà khoa học</p>
				<a href="" title="Xem tất cả bài viết của nhà khoa học">
					<p id="view-post-more">Xem thêm &gt;&gt;</p>
				</a>
			</div>
			
			<!-- end general title -->
			<div class="row module-wrapper blog-widget">
				<div class="col-md-5" id="first-post-scientist">
					<a href="${pageContext.request.contextPath}/article/${ article.id }/view" title="${ article.name }">
						<img src="${pageContext.request.contextPath}/upload/article/${article.mainImage}" class="img-responsive" alt="${ article.name }">
					</a>
					<div class="first-post">
						<div class="first-post-title">
							<a href="${pageContext.request.contextPath}/article/${ article.id }/view" title="${ article.name }">
								${ article.name }
							</a>
						</div>
						<div class="first-post-info">
						<span>${ article.author.name }</span> - <span>${ article.createDate}</span>
						</div>
						<div class="first-post-content">
							<span>
								${ article.title}
							</span>
						</div>
					</div>
				</div>
				
				<div class="col-md-7 list-post">
					<c:forEach items="${articles}" var="article" varStatus="loop" >
						<div class="row post-item">
							<div class="col-md-3 col-sm-3 col-xs-12 publish-image">
								<a href="${pageContext.request.contextPath}/article/${ article.id }/view" title="${ article.name }">
									<img src='${pageContext.request.contextPath}/upload/article/${article.mainImage}' class="img-responsive" alt="${ article.name }">
								</a>
							</div>
							<div class="col-md-9 col-sm-9 col-xs-12">
							<div class="post-title">
								<a href="${pageContext.request.contextPath}/article/${ article.id }/view" title="${ article.name }" style="color: black;">
									${ article.name }
								</a>
							</div>
							<div class="post-info"><span>${ article.author.name }</span> - <span></span></div>
								<div class="post-content">
									<span>${ article.title}</span>
								</div>
							</div>
						</div>
					</c:forEach>
					<%-- <div ng-repeat="item in scientists" class="row post-item">
						<div class="col-md-3 col-sm-3 col-xs-12 publish-image">
							<a href="${pageContext.request.contextPath}/home/paper/detail" title="{{ item.name }}">
								<img src='{{ item.img }}' class="img-responsive" alt="{{ item.name }}">
							</a>
						</div>
						<div class="col-md-9 col-sm-9 col-xs-12">
							<div class="post-title">
								<a href="${pageContext.request.contextPath}/home/paper/detail" title="{{ item.name }}">
									{{ item.name }}
								</a>
							</div>
							<div class="post-info">{{ item.author }} - {{ item.dop }}</div>
							<div class="post-content">Theo báo cáo của Chi cục Trồng trọt - BVTV Nghệ
								An, từ cuối tháng 6 đến nửa đầu tháng 8/2017, diện tích lúa
								bị bệnh lùn sọc đen tăng từ 54 ha lên 675,5 ha.</div>
						</div>
					</div> --%>
				</div>
			</div>
		</div>
	</section>
	<!-- end section -->
	
	<!-- section utility -->
	<section class="section white" id="utility">
		<div class="container box" id="utility-container">
			<div class="general-title text-center">
				<h4 class="top-title">Tiện ích nhà nông</h4>
				<hr id="hr-utility">
				<p class="lead">Dự báo thời tiết, lượng mưa, độ ẩm, thông báo của chính quyền địa phương</p>
			</div>
			<div class="row" id="content-utility">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="row">
						<div class="col-md-5" id="weather-city">
							<div class="utility">
								<div class="utility-title-right text-center">Thành Phố Cao Bằng</div>
								<div class="weather-today">
									<img src="http://l.yimg.com/a/i/us/we/52/47.gif" alt="Weather image">
									<div>Thứ năm</div>
									<div>Ngày 19/04/2018</div>
									<div>25 ℃</div>
									<div>77 ℉</div>
								</div>
								<div class="wind-speed">17.70 KM/H SW</div>
							</div>
						</div>
						<div class="col-md-7">
							<div class="utility">
								<div class="utility-title-right text-center ">Thông tin khác</div>
								<div class="text-left padding-top-5 padding-bottom-5">
									<span class="text-left padding-top-10 padding-bottom-10">Độ</span>
									<span class="pull-right text-right">73</span>
								</div>
								<div class="text-left padding-top-5 padding-bottom-5">
									<span class="text-left ">Tốc độ gió</span>
									<span class="pull-right text-right">17.70 KM/H</span>
								</div>
								<div class="text-left padding-top-5 padding-bottom-5">
									<span class="text-left">Hướng gió</span>
									<span class="pull-right text-right">190 SW</span>
								</div>
								<div class="text-left padding-top-5 padding-bottom-13">
									<span class="text-left">Nhiệt độ(℃)</span>
									<span class="pull-right text-right">25 ℃</span>
								</div>
							</div>
						</div>
					</div>
					<div class="row utility padding-10">
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day border-rightx">
								<div class="text-center margin-top-5">Thứ sáu</div>
								<img src="http://l.yimg.com/a/i/us/we/52/47.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">26
									℃</div>
								<div class="text-center margin-top-5">78.8 ℉</div>
							</div>
						</div>
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day border-rightx">
								<div class="text-center margin-top-5">Thứ bảy</div>
								<img src="http://l.yimg.com/a/i/us/we/52/4.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">27
									℃</div>
								<div class="text-center margin-top-5">80.6 ℉</div>
							</div>
						</div>
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day border-rightx">
								<div class="text-center margin-top-5">Chủ nhật</div>
								<img src="http://l.yimg.com/a/i/us/we/52/47.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">31
									℃</div>
								<div class="text-center margin-top-5">87.8 ℉</div>
							</div>
						</div>
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day border-rightx">
								<div class="text-center margin-top-5">Thứ hai</div>
								<img src="http://l.yimg.com/a/i/us/we/52/4.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">29
									℃</div>
								<div class="text-center margin-top-5">84.2 ℉</div>
							</div>
						</div>
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day border-rightx">
								<div class="text-center margin-top-5">Thứ ba</div>
								<img src="http://l.yimg.com/a/i/us/we/52/4.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">26
									℃</div>
								<div class="text-center margin-top-5">78.8 ℉</div>
							</div>
						</div>
						<div class="col-md-2 non-padding text-center">
							<div class="weather-by-day ">
								<div class="text-center margin-top-5">Thứ tư</div>
								<img src="http://l.yimg.com/a/i/us/we/52/47.gif"
									alt="Weather image">
								<div class="text-center margin-top-5 margin-bottom-10">24
									℃</div>
								<div class="text-center margin-top-5">75.2 ℉</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div id="utility-right" class="pull-left utility">
						<div class="utility-title-right">Thông báo của chính quyền
							địa phương</div>
							<c:forEach items="${news}" var="dto" varStatus="loop" >
								<a	href="${pageContext.request.contextPath}/news/${ dto.id }/view"
									title="${ dto.name }">
									<div class="pull-left utility-item">${ dto.name }</div>
								</a> 
							</c:forEach>
						<!-- <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/gioi-thieu-dia-phuong"
							title="Giới thiệu xã Khánh Xuân">
							<div class="pull-left utility-item">Giới thiệu xã Khánh
								Xuân</div>
						</a> 
						<a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/dien-bien-con-bao-so-3-20"
							title="Diễn biến cơn bão số 3">
							<div class="pull-left utility-item">Diễn biến cơn bão số 3
							</div>
						</a> <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/dien-bien-con-bao-so-12-14"
							title="Diễn biến cơn bão số 12">
							<div class="pull-left utility-item">Diễn biến cơn bão số 12
							</div>
						</a> <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/dien-bien-con-bao-so-9-16"
							title="Diễn biến cơn bão số 9">
							<div class="pull-left utility-item">Diễn biến cơn bão số 9
							</div>
						</a> <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/dien-bien-con-bao-so-10-27"
							title="Diễn biến cơn bão số 10">
							<div class="pull-left utility-item">Diễn biến cơn bão số 10
							</div>
						</a> <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/dien-bien-con-bao-so-3-21"
							title="Diễn biến cơn bão số 3">
							<div class="pull-left utility-item">Diễn biến cơn bão số 3
							</div>
						</a> <a
							href="http://vicrop.mhsoft.vn/tin-nha-nong/sau-duc-qua-sau-duc-trai-conopomorpha-cramerella-27"
							title="Sâu đục quả, sâu đục trái  (Conopomorpha cramerella)">
							<div class="pull-left utility-item">Sâu đục quả, sâu đục
								trái (Conopomorpha cramerella)</div>
						</a> -->
					</div>
				</div>
			</div>
		</div>
	</section>
	
	<!-- section contact -->
	<section class="section" id="contact_us">
		<div class="container">
			<div class="general-title text-center">
				<h4 class="top-title">Liên hệ với Agrolink</h4>
				<hr>
				<p class="small-title">Để lại thông tin để chúng tôi tư vấn và giải đáp thắc mắc</p>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="col-md-5">
						<div class="text-contact">
							Agrolink là hệ thống liên kết kết nối 4 nhà: doanh nghiệp, nhà nông, nhà khoa học, nhà nước.Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec odio. Quisque volutpat mattis eros. Nullam malesuada erat ut turpis. Suspendisse urna nibh, viverra non, semper suscipit, posuere a, pede.
							Donec nec justo eget felis facilisis fermentum. Aliquam porttitor mauris sit amet orci. Aenean dignissim pellentesque felis.
							Morbi in sem quis dui placerat ornare. Pellentesque odio nisi, euismod in, pharetra a, ultricies in, diam. Sed arcu. Cras consequat.
						</div>
						<h3 class="text-center register-tp">Gửi thông tin liên hệ</h3>
						<form action="http://vicrop.mhsoft.vn/register-contact" method="POST">
							<input type="hidden" name="_token" value="LnrvPKOFlXMtkPjEIefqd3UtygAlSyVEebVyK8Jv">
							
							<div class="form-group">
								<input type="text" value="" placeholder="Họ và tên" class="form-control" required="" name="name">
							</div>
							
							<div class="form-group">
								<input type="email" placeholder="Email" value="" class="form-control" required="" name="email">
							</div>
							
							<div class="form-group">
								<input type="text" placeholder="Số điện thoại" value="" class="form-control" required="" name="phone_number">
							</div>
							
							<div class="form-group">
								<textarea name="issue_interested" class="form-control" required="" placeholder="Vấn đề quan tâm"></textarea>
							</div>
							
							<div class="text-center">
								<button type="submit" id="btn-contact" class="btn btn-success">Gửi đi</button>
							</div>
						</form>
					</div>
					
					<div class="col-md-7">
						<img src="http://vicrop.mhsoft.vn/assets/home/images/contact_us.png" class="img-responsive" alt="">
					</div>
				</div>
			</div>
		</div>
	</section>

</div>
<script type="text/javascript">
	$(function() {
		
	});
</script>
