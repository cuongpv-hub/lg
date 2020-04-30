<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">
				Danh sách tin địa phương
			</h1>
			<div class="col-md-12 col-sm-12 following">
				<form:form id="form-search" action="${pageContext.request.contextPath}/news/search" 
						method="GET" modelAttribute="searchDto">
					
					<div class="card-box">
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12">
								<form:input path="name" class="form-control"
										placeholder="Tên tin tức, ..." id="searchName" name="searchName" />
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<form:input path="authorName" class="form-control"
										placeholder="Tên tác giả, ..." id="searchAuthor" name="searchAuthor" />
							</div>
							<div class="col-md-5 col-sm-12 col-xs-12 sm-margin-top-10">
								<div class="input-group">
									<form:select path="categoryIds" id="categoryId" name="categoryId" 
										class="form-control selectpicker" multiple="multiple"
										title="Lựa chọn lĩnh vực" onchange="selectWithAllItem(this, '', true)">
										<option value="">Tất cả lĩnh vực</option>
										<form:options items="${categories}"></form:options>
									</form:select>
									<span class="input-group-btn">
										<button type="submit" class="btn btn-info">
											<i class="fa fa-search"></i> Tìm kiếm
										</button>
										<c:if test="${ currentUser != null && currentUser.isAdmin() }">
											<a class="btn btn-success" href="${pageContext.request.contextPath}/news/create" title="Thêm mới dự án">
												<i class="fa fa-plus"></i> Thêm mới
											</a>
										</c:if>
									</span>
								</div>
							</div>
						</div>
						<div class="row find-more-container" id="advanceSearchPanel" <c:if test="${ false && !searchDto.advanceSearch }">style="display: none;"</c:if>>
							<div class="col-md-3 col-sm-5 col-xs-12 padding-top-10">
								<form:select path="provinceId" id="searchProvince" name="searchProvince" 
									class="form-control selectpicker show-tick" 
									title="Tỉnh/thành" onchange="selectProvince('searchProvince', 'searchDistrict', null, '', 'Tất cả', true)">
									<option value="">Tất cả</option>
									<form:options items="${provinces}"></form:options>
								</form:select>
							</div>
							
							<div class="col-md-4 col-sm-7 col-xs-12 padding-top-10">
								<form:select path="districtIds" id="searchDistrict" name="searchDistrict" 
									class="form-control selectpicker" multiple="multiple" 
									title="Quận/huyện" onchange="selectWithAllItem(this, '', true)">
									<option value="">Tất cả</option>
									<form:options items="${districts}"></form:options>
								</form:select>
							</div>
							
							<div class="col-md-2 col-sm-6 col-xs-12 padding-top-10">
								<form:select path="status" id="status" name="status" 
									class="form-control selectpicker show-tick">
									<form:options items="${statuses}"></form:options>
								</form:select>
							</div>
							
							<div class="col-md-3 col-sm-6 col-xs-12 padding-top-10">
								<form:select path="sort" id="sort" name="sort" 
									class="form-control selectpicker show-tick">
									<form:options items="${sorts}"></form:options>
								</form:select>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		
		<div class="row table-row">
			<c:forEach items="${dtos}" var="dto" varStatus="loop">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="card-box widget-user table-cell-content">
						<div>
							<a href="${pageContext.request.contextPath}/news/${ dto.id }/view" title="${ dto.name }">
								<c:if test="${ dto.mainImage != null && dto.mainImage != '' }">
									<img src="${pageContext.request.contextPath}/upload/news/${dto.mainImage}" 
										alt="${ dto.name }" class="img-fluid rounded-circle">
								</c:if>
								<div class="wid-u-info">
									<h4 class="m-t-0 m-b-5 font-600" title="${ dto.name }">${ dto.name }</h4>
									<p class="text-muted m-b-5 font-13"></p>
									<p class="text-muted m-b-5 font-13" title="${ dto.fullAddress }">
										<i aria-hidden="true" class="fa fa-map-marker"></i>
										${ dto.fullAddress }
									</p>
									<p class="text-muted m-b-5 font-13" title="${ dto.approveName }">
										<i aria-hidden="true" class="fa fa-legal"></i>
										Đơn vị duyệt: ${ dto.approveName }
									</p>
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
											<a class="btn btn-success" href="${pageContext.request.contextPath}/news/${ dto.id }/edit">Chỉnh sửa</a>
										</c:if>
									</div>
									<div class="col-sm-6 col-xs-12">
										<c:if test="${ dto.canApprove }">
											<a class="btn btn-primary pull-right" href="${pageContext.request.contextPath}/news/${ dto.id }/view">Xem và phê duyệt</a>
										</c:if>
									</div>
								</div>
							</c:if>
						</c:if>
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
								<a href="${pageContext.request.contextPath}/news/page?page=1">&laquo;</a>
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
									<a href="${pageContext.request.contextPath}/news/page?page=${ page }">${ page }</a>
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
								<a href="${pageContext.request.contextPath}/news/page?page=${ pageCount }">&raquo;</a>
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