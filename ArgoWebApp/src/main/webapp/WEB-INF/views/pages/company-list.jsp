<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">Danh sách doanh nghiệp trên địa bàn</h1>
			<div class="col-md-12 col-sm-12 following">
				<form:form id="form-search" action="${pageContext.request.contextPath}/company/search" 
						method="GET" modelAttribute="searchDto">
					
					<div class="card-box">
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<form:input path="name" class="form-control"
										placeholder="Tên doanh nghiệp, ..." id="searchName" name="searchName" />
							</div>
							<div class="col-md-3 col-sm-3 col-xs-8">
								<form:select path="companyFieldIds" id="companyFieldId" name="companyFieldId" 
									class="form-control selectpicker" multiple="multiple"
									title="Lĩnh vực kinh doanh">
									<option value="">Tất cả lĩnh vực</option>
									<form:options items="${companyFields}"></form:options>
								</form:select>
							</div>
							<div class="col-md-3 col-sm-3 col-xs-8">
								<form:select path="companyTypeIds" id="companyTypeId" name="companyTypeId" 
									class="form-control selectpicker" multiple="multiple"
									title="Loại hình công ty">
									<option value="">Tất cả loại hình</option>
									<form:options items="${companyTypes}"></form:options>
								</form:select>
							</div>
							<div class="col-md-2 col-sm-2 col-xs-4 text-center">
								<button type="submit" class="btn btn-success">Tìm kiếm</button>
							</div>
						</div>
						<%-- <div class="row find-more-container" ng-show="global.advanceSearch">
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="provinceIds" id="provinceId" name="provinceId" 
									class="form-control selectpicker" multiple="multiple"
									title="Tỉnh, thành">
									<option value="">Tất cả</option>
									<form:options items="${provinces}"></form:options>
								</form:select>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="districtIds" id="districtId" name="districtId" 
									class="form-control selectpicker" multiple="multiple"
									title="Quận/huyện">
									<option value="">Tất cả</option>
									<form:options items="${districts}"></form:options>
								</form:select>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12 padding-top-10">
								<form:select path="locationIds" id="locationId" name="locationId" 
									class="form-control selectpicker" multiple="multiple"
									title="Phường/xã">
									<option value="-1">Tất cả</option>
									<form:options items="${locations}"></form:options>
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="padding-top-10">
								<a data-toggle="0" href="#" class="col-md-12 find-more" 
									ng-click="toggleSearch()">Tìm kiếm nâng cao </a>
							</div>
						</div> --%>
					</div>
				</form:form>
			</div>
		</div>
		
		<div class="row">
			<c:forEach items="${dtos}" var="dto" varStatus="loop">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="card-box widget-user">
						<div>
							<a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }">
								<c:if test="${ dto.avatar != null && dto.avatar != '' }">
									<img src="${pageContext.request.contextPath}/upload/avatar/${dto.avatar}" 
										alt="${ dto.name }" class="img-fluid rounded-circle">
								</c:if>
								<div class="wid-u-info">
									<h4 class="m-t-0 m-b-5 font-600" title="${ dto.name }">${ dto.name }</h4>
									<p class="text-muted m-b-5 font-13"></p>
									<p class="text-muted m-b-5 font-13" title="${ dto.fullAddress }">${ dto.fullAddress }</p>
									<p class="text-muted m-b-5 font-13" title="${ dto.approveName }">Đơn vị cấp phép: ${ dto.approveName }</p>
								</div>
								<hr class="dashed">
								<div class="text-muted font-13">${ dto.title }</div>
							</a>
						</div>
						<%-- <c:if test="${ currentUser != null && dto.status.status != 1 }">
							<c:if test="${ dto.owner || dto.canApprove }">
								<hr class="dashed">
								<div class="row">
									<div class="col-sm-6 col-xs-12">
										<c:if test="${ dto.owner }">
											<a class="btn btn-success" href="${pageContext.request.contextPath}/project/${ dto.id }/edit">Chỉnh sửa</a>
										</c:if>
									</div>
									<div class="col-sm-6 col-xs-12">
										<c:if test="${ dto.canApprove }">
											<a class="btn btn-primary pull-right" href="${pageContext.request.contextPath}/project/${ dto.id }/view">Xem và phê duyệt</a>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:if> --%>
					</div>
				</div>
			</c:forEach>
		</div>
		<c:if test="${ pageCount > 1 }">
			<div class="text-center">
				<ul class="pagination">
					<c:choose>
	 					<c:when test="${ currentPage <= 1 }">
	 						<li class="paging-item disabled"><span>&laquo;</span></li>
						</c:when>
						<c:otherwise>
							<li class="paging-item">
								<a href="${pageContext.request.contextPath}/company/page?page=1">&laquo;</a>
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
									<a href="${pageContext.request.contextPath}/company/page?page=${ page }">${ page }</a>
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
								<a href="${pageContext.request.contextPath}/company/page?page=${ pageCount }">&raquo;</a>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</c:if>
	</div>
</section>
<script type="text/javascript">
	$(function() {
		var numberOption = {
		    currencySymbol : '',
		    decimalCharacter : ',',
		    digitGroupSeparator : '.',
		    decimalPlacesRawValue: 3,
		    allowDecimalPadding: false
		};
		
		$('.text-number').each(function() {
			new AutoNumeric(this, numberOption);
	    });
	});
</script>