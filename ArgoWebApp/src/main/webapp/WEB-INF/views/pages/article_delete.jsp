<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following" ng-controller="ArticleController">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">
				Danh sách nông sản</h1>
			<div class="col-md-12 col-sm-12 following">
				<form:form id="form-search" action="${pageContext.request.contextPath}/article/search" 
						method="GET" modelAttribute="searchDto">
					
					<div class="card-box">
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<form:input path="name" class="form-control"
										placeholder="Tên bài báo, ..." id="searchName" name="searchName" />
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12">
								<form:input path="scientist" class="form-control"
										placeholder="Tên nhà khoa học, ..." id="searchFarmer" name="searchFarmer" />
							</div>
							<div class="col-md-2 col-sm-2 col-xs-8">
								<form:select path="categoryIds" id="categoryId" name="categoryId" 
									class="form-control selectpicker" multiple="multiple"
									title="Lựa chọn lĩnh vực">
									<option value="">Tất cả lĩnh vực</option>
									<form:options items="${categories}"></form:options>
								</form:select>
							</div>
							<c:choose>
    							<c:when test="${ currentUser != null && currentUser.isFarmer() }">
									<div class="col-md-1 col-sm-1 col-xs-2 text-center">
										<button type="submit" class="btn btn-success">Tìm kiếm</button>
									</div>
									<div class="col-md-1 col-sm-1 col-xs-2 text-center">
										<a class="btn btn-success" href="${pageContext.request.contextPath}/article/create" title="Thêm mới nông sản">
											<i class="fa fa-plus"></i>
										</a>
									</div>
								</c:when>
								<c:otherwise>
									<div class="col-md-2 col-sm-2 col-xs-4 text-center">
										<button type="submit" class="btn btn-success">Tìm kiếm</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="row find-more-container" ng-show="global.advanceSearch">
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="scientistIds" id="scientistId" name="scientistId" 
									class="form-control selectpicker" multiple="multiple"
									title="Lựa chọn nhà khoa học">
									<option value="">Nhà khoa học liên kết</option>
									<form:options items="${scientists}"></form:options>
								</form:select>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="departmentIds" id="departmentId" name="departmentId" 
									class="form-control selectpicker" multiple="multiple"
									title="Lựa chọn quận/huyện">
									<option value="">Tất cả quận/huyện</option>
									<form:options items="${organizations}"></form:options>
								</form:select>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="period" id="period" name="period" 
									class="form-control selectpicker" 
									title="Lựa chọn thời gian">
									<option value="-1">Tất cả thời gian</option>
									<form:options items="${periods}"></form:options>
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="padding-top-10">
								<a data-toggle="0" href="#" class="col-md-12 find-more" 
									ng-click="toggleSearch()">Tìm kiếm nâng cao </a>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		
		<div class="row">
			<c:forEach items="${dtos}" var="dto" varStatus="loop">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="card-box widget-user">
						<div>
							<a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${ dto.name }">
								<c:if test="${ dto.mainImage != null && dto.mainImage != '' }">
									<img src="${pageContext.request.contextPath}/upload/article/${dto.mainImage}" 
										alt="${ dto.name }" class="img-fluid rounded-circle">
								</c:if>
								<div class="wid-u-info">
									<h4 class="m-t-0 m-b-5 font-600" title="${ dto.name }">${ dto.name }</h4>
									<p class="text-muted m-b-5 font-13"></p>
									<%-- <p class="text-muted m-b-5 font-13" title="${ dto.fullAddress }">${ dto.fullAddress }</p> --%>
									<p class="text-muted m-b-5 font-13" title="${ dto.approveName }">Đơn vị cấp phép: ${ dto.approveName }</p>
								</div>
								<hr class="dashed">
								<div class="text-muted font-13">${ dto.title }</div>
							</a>
						</div>
						<c:if test="${ currentUser != null && dto.status.status != 1 }">
							<c:if test="${ dto.owner || dto.canApprove }">
								<hr class="dashed">
								<div class="row">
									<div class="col-sm-6 col-xs-12">
										<c:if test="${ dto.owner }">
											<a class="btn btn-success" href="${pageContext.request.contextPath}/article/${ dto.id }/edit">Chỉnh sửa</a>
										</c:if>
									</div>
									<div class="col-sm-6 col-xs-12">
										<c:if test="${ dto.canApprove }">
											<a class="btn btn-primary pull-right" href="${pageContext.request.contextPath}/article/${ dto.id }/view">Xem và phê duyệt</a>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
		<c:if test="${ pageCount > 0 }">
			<div class="text-center">
				<ul class="pagination">
					<c:choose>
	 					<c:when test="${ currentPage <= 1 }">
	 						<li class="paging-item disabled"><span>&laquo;</span></li>
						</c:when>
						<c:otherwise>
							<li class="paging-item">
								<a href="${pageContext.request.contextPath}/article/page?page=1">&laquo;</a>
							</li>
						</c:otherwise>
					</c:choose>
					
					<c:forEach var="page" begin="1" end="${pageCount}">
						<c:choose>
		 					<c:when test="${ currentPage == page }">
		 						<li class="active"><span>${ page }</span></li>
							</c:when>
							<c:otherwise>
								<li class="paging-item">
									<a href="${pageContext.request.contextPath}/article/page?page=${ page }">${ page }</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					
					<c:choose>
	 					<c:when test="${ currentPage == pageCount }">
	 						<li class="paging-item disabled"><span>&raquo;</span></li>
						</c:when>
						<c:otherwise>
							<li class="paging-item">
								<a href="${pageContext.request.contextPath}/article/page?page=${ pageCount }">&raquo;</a>
							</li>
						</c:otherwise>
					</c:choose>
					
					
				</ul>
			</div>
		</c:if>
	</div>
</section>