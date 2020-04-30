<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<div class="container-modal-search">
	<div class="row">
		<div class="col-md-12 col-sm-12 following">
			<form:form id="form-search" action="${pageContext.request.contextPath}/modal-search/scientist/search" 
					method="POST" modelAttribute="searchDto" class="form-search">
				<form:hidden path="withoutLocation" />
				<form:hidden path="notLinkedProjectId" />
				
				<div class="card-box">
					<div class="row">
						<div class="col-md-4 col-sm-6 col-xs-12">
							<form:input path="text" class="form-control"
									placeholder="Tên nhà khoa học, địa chỉ, chức vụ, ..." id="searchText" name="searchText" />
						</div>
						<div class="col-md-3 col-sm-6 col-xs-12">
							<form:select path="literacyIds" id="literacyId" name="literacyId" 
								class="form-control selectpicker" multiple="multiple"
								title="Trình độ học vấn" onchange="selectWithAllItem(this, '', true)">
								<option value="">Tất cả trình độ học vấn</option>
								<form:options items="${literacies}"></form:options>
							</form:select>
						</div>
						<div class="col-md-5 col-sm-12 col-xs-12 sm-margin-top-10">
							<div class="input-group">
								<form:select path="majorIds" id="majorId" name="majorId" 
									class="form-control selectpicker" multiple="multiple"
									title="Lĩnh vực tư vấn" onchange="selectWithAllItem(this, '', true)">
									<option value="">Tất cả lĩnh vực tư vấn</option>
									<form:options items="${majors}"></form:options>
								</form:select>
								<span class="input-group-btn">
									<button type="button" class="btn btn-info button-search">
										<i class="fa fa-search"></i> Tìm kiếm
									</button>
								</span>
							</div>
						</div>
					</div>
					<div class="row find-more-container" id="advanceSearchPanel" <c:if test="${ false && !searchDto.advanceSearch }">style="display: none;"</c:if>>
						<div class="col-md-3 col-sm-5 col-xs-12 padding-top-10">
							<form:select path="provinceId" id="searchProvince" name="searchProvince" 
								class="form-control selectpicker show-tick" 
								title="Tỉnh/thành" onchange="selectProvince('searchProvince', 'searchDistrict', null, '', 'Tất cả', true)">
								<option value="">Tất cả tỉnh/thành</option>
								<form:options items="${provinces}"></form:options>
							</form:select>
						</div>
						
						<div class="col-md-4 col-sm-7 col-xs-12 padding-top-10">
							<form:select path="districtIds" id="searchDistrict" name="searchDistrict" 
								class="form-control selectpicker" multiple="multiple" 
								title="Quận/huyện" onchange="selectWithAllItem(this, '', true)">
								<option value="">Tất cả quận/huyện</option>
								<form:options items="${districts}"></form:options>
							</form:select>
						</div>
						
						<div class="col-md-2 col-sm-6 col-xs-12 padding-top-10">
							<form:select path="status" id="status" name="status" 
								class="form-control selectpicker show-tick"
								title="Trạng thái">
								<form:options items="${statuses}"></form:options>
							</form:select>
						</div>
						
						<div class="col-md-3 col-sm-6 col-xs-12 padding-top-10">
							<form:select path="sort" id="sort" name="sort" 
								class="form-control selectpicker show-tick"
								title="Sắp xếp">
								<form:options items="${sorts}"></form:options>
							</form:select>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	
	<div class="row table-row table-result following">
		<form:form id="form-select" action="${pageContext.request.contextPath}/modal-search/scientist/select" 
					method="POST" modelAttribute="selectDto" class="form-select">
			<c:forEach items="${dtos}" var="dto" varStatus="loop">
				<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
					<div class="card-box table-cell-content padding-bottom-none">
						<div class="item-farmer hover padding-none margin-none" id="resource_${ dto.id }">
							<a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }" 
								target="_blank" id="resource_${ dto.id }_image">
								<c:choose>
			 						<c:when test="${ dto.avatar == null || dto.avatar == '' }">
			 							<img src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg" 
											alt="${ dto.name }" class="img-fluid rounded-circle-xyz">
			 						</c:when>
			 						<c:otherwise>
			 							<img src="${pageContext.request.contextPath}/upload/avatar/${dto.avatar}" 
											alt="${ dto.name }" class="img-fluid rounded-circle-xyz with-error-src"
											errorSrc="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg">
			 						</c:otherwise>
			 					</c:choose>
							</a>
							<div class="name-of-farmer text-muted font-13" id="resource_${ dto.id }_content">
								<c:set var="scientistItemDto" value="${dto}" scope="request" />
								<jsp:include page="scientist-resource-detail.jsp"></jsp:include>
							</div>
							<div class="btn-touch btn-group btn-group-vertical col-md-12 padding-left-none" data-toggle="buttons">
								<label class="btn padding-left-none" for="select_${ dto.id }">
									<input type="checkbox" id="select_${ dto.id }" name="ids" value="${ dto.id }" style="display: none;" />
									<i class="fa fa-square-o fa-2x"></i>
									<i class="fa fa-check-square-o fa-2x"></i>
									<span class="data-toggle-text-label">Chọn nhà khoa học</span>
								</label>
							</div>
							
							<input type="hidden" id="name_${ dto.id }" name="name_${ dto.id }" value="${ dto.name }">
							<input type="hidden" id="avatar_${ dto.id }" name="avatar_${ dto.id }" value="${ dto.avatar }">
						</div>
					</div>
				</div>
			</c:forEach>
		</form:form>
	</div>
</div>
