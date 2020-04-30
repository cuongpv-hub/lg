<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>

<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">
				<c:choose>
					<c:when test="${ dto.id == null || dto.id == '' }">
						Thêm mới dự án
					</c:when>
					<c:otherwise>
						Chỉnh sửa dự án
					</c:otherwise>
				</c:choose>
			</h1>
			
			<c:if test="${ dto.id != null && currentUser != null }">
				<c:if test="${ dto.owner || dto.canApprove }">
					<span class="col-sm-12 text-center">
						<strong>Trạng thái:</strong>
						<c:choose>
							<c:when test="${ statusDto.status == 0 }">
								<span class="text-italic"><spring:message code="status.label.draft"></spring:message></span>
							</c:when>
							<c:when test="${ statusDto.status == 2 }">
								<span><spring:message code="status.label.processing"></spring:message></span>
							</c:when>
							<c:when test="${ statusDto.status == 1 }">
								<span class="text-bold"><spring:message code="status.label.approved"></spring:message></span>
								<c:if test="${ statusDto.comment != null && statusDto.comment != '' }">
									<i class="fa fa-info-circle" title="${ statusDto.comment }"></i>
								</c:if>
							</c:when>
							<c:when test="${ statusDto.status == -1 }">
								<span class="text-strike text-danger"><spring:message code="status.label.rejected"></spring:message></span>
								<c:if test="${ statusDto.comment != null && statusDto.comment != '' }">
									<i class="fa fa-info-circle" title="${ statusDto.comment }"></i>
								</c:if>
							</c:when>
						</c:choose> 
					</span>
				</c:if>
			</c:if>
			
			<div class="col-md-12 col-sm-12 following">
				<!-- Horizontal Form -->
				<div class="box box-info">
					<!-- form start -->
					<form:form name="form" action="save" method="POST" 
						modelAttribute="dto" enctype="multipart/form-data">

						<form:hidden path="id" />

						<div class="box-body">
							<c:set var="contactGroupTitle" value="Thông tin về doanh nghiệp / HTX" scope="request" />
							<c:set var="contactNameLabel" value="Tên doanh nghiệp / HTX" scope="request" />
	                        <c:set var="contactAliasLabel" value="Tên viết tắt / thương hiệu" scope="request" />
							<jsp:include page="resource-contact-form.jsp"></jsp:include>
							
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                        			<h4 class="col-xs-12 text-bold color-1ab394 padding-left-none margin-top-none margin-bottom-10 text-uppercase text-underline">
                        				Thông tin về dự án, chương trình
                        			</h4>
                        			
                        			<div class="col-xs-12 padding-none">
										<div class="row">
											<div class="col-md-4 col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="categoryId" class="control-label">Loại dự án <span class="reqired-input">*</span></label>
													<form:select path="categoryIds" id="categoryId" name="categoryId" 
														class="form-control selectpicker" multiple="multiple"
														title="Lựa chọn loại dự án" required="required">
														<form:options items="${categories}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="categoryIds" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-8 col-sm-8 col-xs-12">
												<div class="form-group">
													<label for="name" class="control-label">Tên dự án <span class="reqired-input">*</span></label>
													<form:input path="name" name="name" id="name" class="form-control" required="required" />
													<div class="has-error">
														<form:errors path="name" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-md-3 col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="money" class="control-label">Tổng mức đầu tư dự án</label>
													<div class="row">
														<div class="col-xs-7 padding-right-none">
															<form:input path="money" name="money" id="money" class="form-control text-number" />
														</div>
														<div class="col-xs-5 padding-left-none">
															<form:select path="moneyUnitId" id="moneyUnit" name="moneyUnit" class="selectpicker show-tick">
																<form:options items="${moneyUnits}"></form:options>
															</form:select>
														</div>
													</div>
													<div class="has-error">
														<form:errors path="money" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-3 col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="square" class="control-label">Diện tích dự án (nếu có)</label>
													<div class="row">
														<div class="col-xs-7 padding-right-none">
															<form:input path="square" name="square" id="square" class="form-control text-number" />
														</div>
														<div class="col-xs-5 padding-left-none">
															<form:select path="squareUnitId" id="squareUnit" name="squareUnit" class="selectpicker show-tick">
																<form:options items="${squareUnits}"></form:options>
															</form:select>
														</div>
													</div>
													<div class="has-error">
														<form:errors path="square" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-3 col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="employees" class="control-label">Tổng số lao động</label>
													<form:input path="employees" name="employees" id="employees" class="form-control text-number" />
													<div class="has-error">
														<form:errors path="employees" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-md-3 col-xs-6">
												<div class="form-group">
													<label for="startDate" class="control-label">Thời gian bắt đầu <span class="reqired-input">*</span></label>
													<div class="input-group date" data-provide="datepicker" 
															data-date-format="${ dateFormatLower }" data-date-language="vi">
														<form:input path="startDate" name="startDate" id="startDate" class="form-control" required="required" />
														<div class="input-group-addon">
													        <span class="glyphicon glyphicon-th"></span>
													    </div>
													</div>
													<div class="has-error">
														<form:errors path="startDate" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-3 col-xs-6">
												<div class="form-group">
													<label for="endDate" class="control-label">Thời gian kết thúc <span class="reqired-input">*</span></label>
													<div class="input-group date" data-provide="datepicker" 
															data-date-format="${ dateFormatLower }" data-date-language="vi">
														<form:input path="endDate" name="endDate" id="endDate" class="form-control" required="required" />
														<div class="input-group-addon">
													        <span class="glyphicon glyphicon-th"></span>
													    </div>
													</div>
													<div class="has-error">
														<form:errors path="endDate" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-6 col-xs-12">
												<div class="form-group">
													<label for="approveName" class="control-label">Đơn vị cấp phép <span class="reqired-input">*</span></label>
													<form:input path="approveName" name="approveName" id="approveName" class="form-control" required="required" />
													<div class="has-error">
														<form:errors path="approveName" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="address" class="control-label">Địa chỉ (đường, tổ, thôn, xóm, khu phố, ...) <span class="reqired-input">*</span></label>
													<form:input path="address" name="address" id="address" class="form-control" 
														placeholder="Số nhà, đường, tổ, thôn, xóm, làng, khu phố, ..." required="required" />
													<div class="has-error">
														<form:errors path="address" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-sm-2 col-xs-12">
												<div class="form-group">
													<label for="province" class="control-label">Tỉnh / thành <span class="reqired-input">*</span></label>
													<form:select path="provinceId" id="province" name="province" class="selectpicker" 
														data-live-search="true" required="required" onchange="selectProvince()">
														<form:options items="${provinces}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="provinceId" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-sm-3 col-xs-12">
												<div class="form-group">
													<label for="district" class="control-label">Quận / huyện <span class="reqired-input">*</span></label>
													<form:select path="districtId" id="district" name="district" class="selectpicker" 
														data-live-search="true" required="required" onchange="selectDistrict()">
														<form:options items="${districts}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="districtId" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-sm-3 col-xs-12">
												<div class="form-group">
													<label for="location" class="control-label">Xã / phường / thị trấn <span class="reqired-input">*</span></label>
													<form:select path="locationId" id="location" name="location" class="selectpicker" 
														data-live-search="true" required="required" onchange="selectLocation()">
														<form:options items="${locations}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="locationId" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="description" class="control-label">Mô tả chi tiết về dự án (Mục tiêu, nội dung đầu tư, sản phẩm, quy mô, thị trường, khả năng mở rộng) <span class="reqired-input">*</span></label>
													<form:textarea path="description" name="description" id="description" 
														rows="10" class="form-control" required="required" />
													<ckeditor:replace replace="description" basePath="${pageContext.servletContext.contextPath}/resources/ckeditor/"/>
													<div class="has-error">
														<form:errors path="description" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<!-- Link land -->
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                    				<div class="col-xs-12 padding-none">
	                        			<div class="btn-touch btn-group btn-group-vertical" data-toggle="buttons">
											<label class="btn <c:if test="${ dto.requireLand }">active</c:if>" for="requireLand">
												<input type="checkbox" id="requireLand" name="requireLand" <c:if test="${ dto.requireLand }">checked</c:if> 
													value="true" style="display: none;" />
												<i class="fa fa-square-o fa-2x"></i>
												<i class="fa fa-check-square-o fa-2x"></i>
												<span class="control-label">Có nhu cầu thuê đất</span>
											</label>
										</div>
										
										<a id="addLand" href="javascript:;" class="btn btn-sm btn-info" 
											title="Tìm và thêm thuê đất" onclick="openLandModal()">
											<i class="fa fa-square"></i> Tìm và thêm thuê đất
										</a>
									</div>
									
									<div class="col-xs-12 padding-none">
										<div class="row table-row" id="landPanel">
											<c:forEach items="${ landDtos }" var="land" varStatus="loop">
												<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
													<div class="card-box table-cell-content">
														<div class="btn-touch btn-group btn-group-vertical padding-left-none col-md-12" data-toggle="buttons">
															<label class="btn active padding-left-none" for="land_${ land.id }" id="btnLand_${ land.id }">
																<input type="checkbox" id="land_${ land.id }" name="landIds" value="${ land.id }" checked style="display: none;" />
																<i class="fa fa-square-o fa-2x"></i>
																<i class="fa fa-check-square-o fa-2x"></i>
																<span class="data-toggle-text-label">Chọn thửa đất</span>
															</label>
														</div>
														
														<br />
														<div class="item-farmer hover padding-none margin-none">
															<a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }" target="_blank">
																<c:choose>
											 						<c:when test="${ land.mainImage == null || land.mainImage == '' }">
											 							<img src="${pageContext.request.contextPath}/upload/land/empty-image.png" 
																			alt="${ land.name }" class="img-fluid rounded-circle-xyz">
											 						</c:when>
											 						<c:otherwise>
											 							<img src="${pageContext.request.contextPath}/upload/land/${ land.mainImage }" 
																			alt="${ land.name }" class="img-fluid rounded-circle-xyz with-error-src"
																			errorSrc="${pageContext.request.contextPath}/upload/land/empty-image.png">
											 						</c:otherwise>
											 					</c:choose>
															</a>
															<div class="name-of-farmer text-muted font-13">
																<c:set var="landItemDto" value="${land}" scope="request" />
																<jsp:include page="land-resource-detail.jsp"></jsp:include>
																<%-- <a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }" target="_blank">
																	${ land.name }
																</a>
																<br />
																<div class="float-left full-width name_owner font-size-14 padding-top-5">
																	<strong>Loại đất</strong>:
																	<br />
																	<div style="display: inline;">
																		<c:forEach items="${ land.categories }" var="categoryDto" varStatus="categoryLoop">
																			<span class="tag-item small">${ categoryDto.name }</span>
																		</c:forEach>
																	</div>
																</div> --%>
															</div>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							
							<!-- Link product -->
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                    				<div class="col-xs-12 padding-none">
										<div class="btn-touch btn-group btn-group-vertical" data-toggle="buttons">
											<label class="btn <c:if test="${ dto.requireProduct }">active</c:if>" for="requireProduct">
												<input type="checkbox" id="requireProduct" name="requireProduct" <c:if test="${ dto.requireProduct }">checked</c:if> 
													value="true" style="display: none;" />
												<i class="fa fa-square-o fa-2x"></i>
												<i class="fa fa-check-square-o fa-2x"></i>
												<span class="control-label">Có nhu cầu mua nông sản</span>
											</label>
										</div>
										
										<a id="addProduct" href="javascript:;" class="btn btn-sm btn-info" 
											title="Tìm và thêm nông sản" onclick="openProductModal()">
											<i class="fa fa-paw"></i> Tìm và thêm nông sản
										</a>
									</div>
								
									<div class="col-xs-12 padding-none">
										<div class="row table-row" id="productPanel">
											<c:forEach items="${ productDtos }" var="product" varStatus="loop">
												<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
													<div class="card-box table-cell-content">
														<div class="btn-touch btn-group btn-group-vertical padding-left-none col-md-12" data-toggle="buttons">
															<label class="btn active padding-left-none" for="product_${ product.id }" id="btnProduct_${ product.id }">
																<input type="checkbox" id="product_${ product.id }" name="productIds" value="${ product.id }" checked style="display: none;" />
																<i class="fa fa-square-o fa-2x"></i>
																<i class="fa fa-check-square-o fa-2x"></i>
																<span class="data-toggle-text-label">Chọn nông sản</span>
															</label>
														</div>
														
														<br />
														<div class="item-farmer hover padding-none margin-none">
															<a href="${pageContext.request.contextPath}/product/${ product.id }/view" title="${ product.name }" target="_blank">
																<c:choose>
											 						<c:when test="${ product.mainImage == null || product.mainImage == '' }">
											 							<img src="${pageContext.request.contextPath}/upload/product/empty-image.png" 
																			alt="${ product.name }" class="img-fluid rounded-circle-xyz">
											 						</c:when>
											 						<c:otherwise>
											 							<img src="${pageContext.request.contextPath}/upload/product/${ product.mainImage }" 
																			alt="${ product.name }" class="img-fluid rounded-circle-xyz with-error-src"
																			errorSrc="${pageContext.request.contextPath}/upload/product/empty-image.png">
											 						</c:otherwise>
											 					</c:choose>
															</a>
															<div class="name-of-farmer text-muted font-13">
																<c:set var="productItemDto" value="${product}" scope="request" />
																<jsp:include page="product-resource-detail.jsp"></jsp:include>
																<%-- <a href="${pageContext.request.contextPath}/product/${ product.id }/view" title="${ product.name }" target="_blank">
																	${ product.name }
																</a>
																<br />
																<div class="float-left full-width name_owner font-size-14 padding-top-5">
																	<strong>Loại nông sản</strong>:
																	<br />
																	<div style="display: inline;">
																		<c:forEach items="${ product.categories }" var="categoryDto" varStatus="categoryLoop">
																			<span class="tag-item small">${ categoryDto.name }</span>
																		</c:forEach>
																	</div>
																</div> --%>
															</div>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							
							<!-- Link scientist -->
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                    				<div class="col-xs-12 padding-none">
										<div class="btn-touch btn-group btn-group-vertical" data-toggle="buttons">
											<label class="btn <c:if test="${ dto.requireScientist }">active</c:if>" for="requireScientist">
												<input type="checkbox" id="requireScientist" name="requireScientist" <c:if test="${ dto.requireScientist }">checked</c:if> 
													value="true" style="display: none;" />
												<i class="fa fa-square-o fa-2x"></i>
												<i class="fa fa-check-square-o fa-2x"></i>
												<span class="control-label">Có nhu cầu mời nhà khoa học tham gia</span>
											</label>
										</div>
										
										<a id="addScientist" href="javascript:;" class="btn btn-sm btn-info" 
											title="Tìm và mời nhà khoa học" onclick="openScientistModal()">
											<i class="fa fa-mortar-board"></i> Tìm và mời nhà khoa học
										</a>
									</div>
								
									<div class="col-xs-12 padding-none">
										<div class="row table-row" id="scientistPanel">
											<c:forEach items="${ scientistDtos }" var="scientist" varStatus="loop">
												<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
													<div class="card-box table-cell-content">
														<div class="btn-touch btn-group btn-group-vertical padding-left-none col-md-12" data-toggle="buttons">
															<label class="btn active padding-left-none" for="scientist_${ scientist.id }" id="btnScientist_${ scientist.id }">
																<input type="checkbox" id="scientist_${ scientist.id }" name="scientistIds" value="${ scientist.id }" checked style="display: none;" />
																<i class="fa fa-square-o fa-2x"></i>
																<i class="fa fa-check-square-o fa-2x"></i>
																<span class="data-toggle-text-label">Chọn nhà khoa học</span>
															</label>
														</div>
														
														<br />
														<div class="item-farmer hover padding-none margin-none">
															<a href="${pageContext.request.contextPath}/profile/${ scientist.id }/view" title="${ scientist.name }" target="_blank">
																<c:choose>
											 						<c:when test="${ scientist.avatar == null || scientist.avatar == '' }">
											 							<img src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg" 
																			alt="${ scientist.name }" class="img-fluid rounded-circle-xyz">
											 						</c:when>
											 						<c:otherwise>
											 							<img src="${pageContext.request.contextPath}/upload/avatar/${ scientist.avatar }" 
																			alt="${ scientist.name }" class="img-fluid rounded-circle-xyz with-error-src"
																			errorSrc="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg">
											 						</c:otherwise>
											 					</c:choose>
															</a>
															<div class="name-of-farmer text-muted font-13">
																<c:set var="scientistItemDto" value="${scientist}" scope="request" />
																<jsp:include page="scientist-resource-detail.jsp"></jsp:include>
																
																<%-- <a href="${pageContext.request.contextPath}/profile/${ scientist.id }/view" title="${ scientist.name }" target="_blank">
																	${ scientist.name }
																</a>
																<br />
																<div class="float-left full-width name_owner font-size-14 padding-top-5">
																	<strong>Lĩnh vực tư vấn</strong>:
																	<br />
																	<div style="display: inline;">
																		<c:forEach items="${ scientist.scientistMajors }" var="majorDto" varStatus="majorLoop">
																			<span class="tag-item small">${ majorDto.name }</span>
																		</c:forEach>
																	</div>
																</div> --%>
															</div>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
							
							<!-- Project image -->
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                    				<div class="col-xs-12 padding-none picture-container-group">
										<div class="form-group">
											<div class="row padding-bottom-10">
												<div class="col-xs-12">
													<label for="imageFiles" class="control-label">Hình ảnh&nbsp;&nbsp;&nbsp;&nbsp;</label>
													<a id="addFile" href="javascript:;" class="btn btn-sm btn-info" title="Thêm hình ảnh">
														<i class="fa fa-image"></i> Thêm hình ảnh
													</a>
												</div>
											</div>
											
											<div class="row">
												<c:forEach items="${imageDtos}" var="image" varStatus="loop">
													<div class="col-lg-2 col-md-3 col-sm-4 col-xs-12">
														<div class="row">
															<div class="col-xs-12 picture-container text-center">
																<img src="${pageContext.servletContext.contextPath}/upload/project/${ image.name }" class="picture-src">
															</div>
														</div>
														<div class="row">
															<div class="col-xs-12">
																<form:radiobutton path="mainImage" id="existMainImage_${loop.index}" value="${ image.name }"/>
																<label for="existMainImage_${loop.index}">Ảnh chính</label>
															</div>
														</div>
														<div class="row">
															<div class="col-xs-12">
																<form:radiobutton path="posterImage" id="existPosterImage_${loop.index}" value="${ image.name }"/>
																<label for="existPosterImage_${loop.index}">Ảnh toàn cảnh</label>
															</div>
														</div>
														<div class="row">
															<div class="col-xs-12">
																<input type="checkbox" name="deleteImages[${loop.index}]" id="deleteExistImage_${loop.index}" value="${ image.id }" />
																<label for="deleteExistImage_${loop.index}">Xóa ảnh</label>
															</div>
														</div>
													</div>
												</c:forEach>
												
												<div id="filePanel">
													<div class="col-lg-2 col-md-3 col-sm-4 col-xs-12">
														<div class="margin-bottom-10">
															<div class="row">
																<div class="col-xs-12 picture-container">
																	<div class="picture">
																		<input type="file" name="imageFiles[0]" id="imageFile_0" onchange="previewImage(this, 0)">
																		<img id="imagePreview_0" class="picture-src"
																			src="${pageContext.servletContext.contextPath}/resources/images/no-image-icon.png">
																	</div>
																</div>
															</div>
															
															<div class="row">
																<div class="col-xs-12 text-inline text-center">
																	<input type="radio" name="mainImage" id="mainImage_0" value="" />
																	<label for="mainImage_0">Ảnh chính</label>
																</div>
															</div>
															
															<div class="row">
																<div class="col-xs-12 text-inline text-center">
																	<input type="radio" name="posterImage" id="posterImage_0" value="" />
																	<label for="posterImage_0">Ảnh toàn cảnh</label>
																</div>
															</div>
															
															<div class="row">
																<div class="col-xs-12 text-inline text-center">
																	<a class="btn btn-warning btn-sm" href="javascript:;" onclick="removeSelectImage(0)">
																		<i class="fa fa-times"></i> Xóa ảnh
																	</a>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- /.box-body -->
						<div class="box-footer">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/project" type="button" class="btn btn-default">
									<i class="fa fa-arrow-left"></i> Hủy bỏ
								</a>
								<button type="submit" class="btn btn-info">
									<i class="fa fa-save"></i> Lưu thay đổi
								</button>
								
								<c:if test="${ dto.id != null && dto.owner && statusDto.status != 1 && statusDto.status != 2 }">
									<button type="button" class="btn btn-success" data-toggle="modal" data-target="#confirm-process">
										<i class="fa fa-paper-plane-o"></i> Gửi phê duyệt
									</button>
								</c:if>
							</span>
						</div>
						<!-- /.box-footer -->
					</form:form>
				</div>
			</div>
		</div>
	</div>
</section>

<div class="modal fade" id="confirm-process" tabindex="-1" role="dialog" 
	aria-labelledby="processModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/project/${ dto.id }/process" method="POST">
	            <div class="modal-header">
					<h2>Gửi yêu cầu phê duyệt dự án</h2>
	            </div>
	            <div class="modal-body">
	            	<div class="row">
						<div class="col-xs-12">
							<strong>Lưu ý:</strong>
							<ul>
								<li>Bạn cần lưu tất cả thay đổi trước khi gửi yêu cầu phê duyệt dự án.</li>
								<li>Nếu bạn được cấp quyền phê duyệt, dự án của bạn sẽ được tự động phê duyệt.</li>
							</ul>
						</div>
					</div>
	            	<div class="row">
						<div class="col-xs-12">
							<div class="form-group">
								<label for="comment" class="control-label">Ghi chú</label>
	                			<textarea name="comment" id="comment" rows="3" class="form-control"></textarea>
	                		</div>
	                	</div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-success">Gửi phê duyệt</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="searchResource" tabindex="-1" role="dialog" 
	aria-labelledby="searchResourceLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
        	<input type="hidden" name="resourceType" id="resourceType">
        	
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Tìm và mời nhà khoa học</h4>
            </div>
            <div class="modal-body modal-body-content" style="min-height:350px">
            </div>
            <div class="modal-footer">
            	<button type="button" class="btn btn-primary" onclick="selectResources()">Đồng ý</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
	var defaultImage = '${pageContext.servletContext.contextPath}/resources/images/no-image-icon.png';
	
	var numberOption = {
		    currencySymbol : '',
		    decimalCharacter : ',',
		    digitGroupSeparator : '.',
		    decimalPlacesRawValue: 3,
		    allowDecimalPadding: false
		};
	
	var provinces_${rndValue} = {
			<c:forEach items="${allProvinces}" var="item" varStatus="itemLoop">
				<c:if test="${ itemLoop.index > 0 }">, </c:if>
				'${ item.getKey() }': [
					<c:forEach items="${item.getValue()}" var="province" varStatus="loop">
						<c:if test="${ loop.index > 0 }">, </c:if>
						{ 'value': '${ province.id }', 'name': '${ province.name }' }
					</c:forEach>
				]
			</c:forEach>
	};
	
	var districts_${rndValue} = {
			<c:forEach items="${allDistricts}" var="item" varStatus="itemLoop">
				<c:if test="${ itemLoop.index > 0 }">, </c:if>
				'${ item.getKey() }': [
					<c:forEach items="${item.getValue()}" var="district" varStatus="loop">
						<c:if test="${ loop.index > 0 }">, </c:if>
						{ 'value': '${ district.id }', 'name': '${ district.name }' }
					</c:forEach>
				]
			</c:forEach>
	};
	
	var locations_${rndValue} = {
			<c:forEach items="${allLocations}" var="item" varStatus="itemLoop">
				<c:if test="${ itemLoop.index > 0 }">, </c:if>
				'${ item.getKey() }': [
					<c:forEach items="${item.getValue()}" var="location" varStatus="loop">
						<c:if test="${ loop.index > 0 }">, </c:if>
						{ 'value': '${ location.id }', 'name': '${ location.name }' }
					</c:forEach>
				]
			</c:forEach>
	};
	
	function previewImage(input, index) {
		if (input.files && input.files[0]) 
		{
			var fileName = input.files[0].name;
			
			var reader = new FileReader();
			reader.onload = function (e) {
				$('#imagePreview_' + index).attr('src', e.target.result).fadeIn('slow');
				$('#mainImage_' + index).val(fileName);
				$('#posterImage_' + index).val(fileName);
			}
			
			reader.readAsDataURL(input.files[0]);
		} else {
			$('#imagePreview_' + index).attr('src,', defaultImage).fadeIn('slow');
			$('#mainImage_' + index).val('');
			$('#posterImage_' + index).val('');
		}
	}
	
	function removeSelectImage(index) {
		$('#imageFile_' + index).val('');
		$('#imagePreview_' + index).attr('src', defaultImage);
		$('#mainImage_' + index).val('');
		$('#posterImage_' + index).val('');
	}
	
	function initModalForm() {
		$('#searchResource .selectpicker').selectpicker();
        $('#searchResource .button-search').on('click', searchResources);
        $('#searchResource #form-search').on('submit', searchResources);
        
        $('#searchResource .text-number').each(function() {
    		new AutoNumeric(this, numberOption);
        });
        
        $('#searchResource img.with-error-src').each(function() {
			checkImageSource(this);
		});
	}
	
	function openProductModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/product';
		
		$('#searchResource #resourceType').val(0);
        $('#searchResource .modal-title').text('Tìm và thêm nông sản vào dự án');
        
		$('#searchResource .modal-body-content').load(url, function(){
	        $('#searchResource').modal({ show:true });
	        
	        initModalForm();
	    });
	}
	
	function openLandModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/land';
		
		$('#searchResource #resourceType').val(1);
        $('#searchResource .modal-title').text('Tìm và thêm thửa đất vào dự án');
        
		$('#searchResource .modal-body-content').load(url, function(){
	        $('#searchResource').modal({ show: true });
	        
	        initModalForm();
	    });
	}
	
	function openScientistModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/scientist?withoutLocation=true';
		
		$('#searchResource #resourceType').val(2);
        $('#searchResource .modal-title').text('Tìm và mời nhà khoa học');
        
		$('#searchResource .modal-body-content').load(url, function(){
	        $('#searchResource').modal({ show:true });
	        
	        initModalForm();
	    });
	}
	
	function searchResources() {
		var resourceType = $('#searchResource #resourceType').val();
		
		var url = '';
		if (resourceType == 0) {
			url = '${pageContext.servletContext.contextPath}/modal-search/product';
		} else if (resourceType == 1) {
			url = '${pageContext.servletContext.contextPath}/modal-search/land';
		} else if (resourceType == 2) {
			url = '${pageContext.servletContext.contextPath}/modal-search/scientist';
		} else {
			return false;
		}
		
		var searchDto = $('#searchResource .form-search').serialize();
		
		$.ajax({
			type: 'post',
			url: url,
			data: searchDto,
			success: function ( html ) {  
				$('#searchResource .modal-body-content').html( html );
				
				initModalForm();
			}
		});
		
		return false;
	}
	
	function selectProduct(itemId) {
		var $existItem = $('#productPanel #product_' + itemId);
		
		if ($existItem.length > 0) {
			$existItem.prop('checked', true);
			
			var $itemButton = $('#productPanel #btnProduct_' + itemId);
			if (!$itemButton.hasClass('active')) {
				$itemButton.addClass('active');
			}
		} else {
			var itemName = $('#searchResource #name_' + itemId).val();
			var itemImage = $('#searchResource #image_' + itemId).val();
			var html = $('#searchResource #resource_' + itemId + '_content').html();
			
			/*var itemCategories = [];
			
			var categoryCount = $('#searchResource #categoryCount_' + itemId).val();
			for (var i = 0; i < categoryCount; i++) {
				var category = $('#searchResource #category_' + itemId + '_' + i).val();
				if (category != null && category != undefined) {
					itemCategories.push(category);
				}
			}*/
			
			if (itemImage == null || itemImage == '') {
				itemImage = 'empty-image.png';
			}
			
			var newItemHtml = '<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">'
				+ '<div class="card-box table-cell-content">'
					+ '<div class="btn-touch btn-group btn-group-vertical col-md-12 padding-left-none" data-toggle="buttons">'
						+ '<label class="btn active padding-left-none" for="product_' + itemId + '" id="btnProduct_' + itemId + '">'
							+ '<input type="checkbox" id="product_' + itemId + '" name="productIds" value="' + itemId + '" checked style="display: none;" />'
							+ '<i class="fa fa-square-o fa-2x"></i>\n'
							+ '<i class="fa fa-check-square-o fa-2x"></i>\n'
							+ '<span class="data-toggle-text-label">Chọn nông sản</span>'
						+ '</label>'
					+ '</div>'
					+ '<div class="item-farmer hover padding-none margin-none">'
						+ '<a href="${pageContext.request.contextPath}/product/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							+ '<img src="${pageContext.request.contextPath}/upload/product/' + itemImage + '" alt="' + itemName + '" class="img-fluid rounded-circle-xyz">'
						+ '</a>'
						+ '<div class="name-of-farmer text-muted font-13">'
							+ html
							//+ '<a href="${pageContext.request.contextPath}/product/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							//	+ itemName
							//+ '</a><br />'
							//+ '<div class="float-left full-width name_owner font-size-14 padding-top-5">'
							//	+ '<strong>Loại nông sản</strong>:'
							//	+ '<div style="display: inline;">\n';
							//for (var i = 0; i < itemCategories.length; i++) {
							//	newItemHtml = newItemHtml 
							//		+ '<span class="tag-item small">' + itemCategories[i] + '</span>\n';
							//}
			
			newItemHtml = newItemHtml
							//	+ '</div>'
							//+ '</div>'
						+ '</div>'
					+ '</div>'
				+ '</div>'
			+ '</div>';
	
			var html = $.parseHTML(newItemHtml);
			$('#productPanel').append(html);
		}
	}
	
	function selectLand(itemId) {
		var $existItem = $('#productPanel #land_' + itemId);
		
		if ($existItem.length > 0) {
			$existItem.prop('checked', true);
			
			var $itemButton = $('#landPanel #btnLand_' + itemId);
			if (!$itemButton.hasClass('active')) {
				$itemButton.addClass('active');
			}
		} else {
			var itemName = $('#searchResource #name_' + itemId).val();
			var itemImage = $('#searchResource #image_' + itemId).val();
			var html = $('#searchResource #resource_' + itemId + '_content').html();
			
			/*var itemCategories = [];
			
			var categoryCount = $('#searchResource #categoryCount_' + itemId).val();
			for (var i = 0; i < categoryCount; i++) {
				var category = $('#searchResource #category_' + itemId + '_' + i).val();
				if (category != null && category != undefined) {
					itemCategories.push(category);
				}
			}*/
			
			if (itemImage == null || itemImage == '') {
				itemImage = 'empty-image.png';
			}
			
			var newItemHtml = '<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">'
				+ '<div class="card-box table-cell-content">'
					+ '<div class="btn-touch btn-group btn-group-vertical col-md-12 padding-left-none" data-toggle="buttons">'
						+ '<label class="btn active padding-left-none" for="land_' + itemId + '" id="btnLand_' + itemId + '">'
							+ '<input type="checkbox" id="land_' + itemId + '" name="landIds" value="' + itemId + '" checked style="display: none;" />'
							+ '<i class="fa fa-square-o fa-2x"></i>\n'
							+ '<i class="fa fa-check-square-o fa-2x"></i>\n'
							+ '<span class="data-toggle-text-label">Chọn thửa đất</span>'
						+ '</label>'
					+ '</div>'
					+ '<div class="item-farmer hover padding-none margin-none">'
						+ '<a href="${pageContext.request.contextPath}/land/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							+ '<img src="${pageContext.request.contextPath}/upload/land/' + itemImage + '" alt="' + itemName + '" class="img-fluid rounded-circle-xyz">'
						+ '</a>'
						+ '<div class="name-of-farmer text-muted font-13">'
							+ html
							//+ '<a href="${pageContext.request.contextPath}/land/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							//	+ itemName
							//+ '</a><br />'
							//+ '<div class="float-left full-width name_owner font-size-14 padding-top-5">'
							//	+ '<strong>Loại đất</strong>:'
							//	+ '<div style="display: inline;">\n';
							//for (var i = 0; i < itemCategories.length; i++) {
							//	newItemHtml = newItemHtml 
							//		+ '<span class="tag-item small">' + itemCategories[i] + '</span>\n';
							//}
			
			newItemHtml = newItemHtml
							//	+ '</div>'
							//+ '</div>'
						+ '</div>'
					+ '</div>'
				+ '</div>'
			+ '</div>';
	
			var html = $.parseHTML(newItemHtml);
			$('#landPanel').append(html);
		}
	}
	
	function selectScientist(itemId) {
		var $existItem = $('#scientistPanel #scientist_' + itemId);
		
		if ($existItem.length > 0) {
			$existItem.prop('checked', true);
			
			var $itemButton = $('#scientistPanel #btnScientist_' + itemId);
			if (!$itemButton.hasClass('active')) {
				$itemButton.addClass('active');
			}
		} else {
			var itemName = $('#searchResource #name_' + itemId).val();
			var itemAvatar = $('#searchResource #avatar_' + itemId).val();
			var html = $('#searchResource #resource_' + itemId + '_content').html();
			
			/*var itemMajors = [];
			
			var majorCount = $('#searchResource #majorCount_' + itemId).val();
			for (var i = 0; i < majorCount; i++) {
				var major = $('#searchResource #major_' + itemId + '_' + i).val();
				if (major != null && major != undefined) {
					itemMajors.push(major);
				}
			}*/
			
			if (itemAvatar == null || itemAvatar == '') {
				itemAvatar = 'empty-avatar.jpg';
			}
			
			var newScientist = '<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">'
				+ '<div class="card-box table-cell-content">'
					+ '<div class="btn-touch btn-group btn-group-vertical col-md-12 padding-left-none" data-toggle="buttons">'
						+ '<label class="btn active padding-left-none" for="scientist_' + itemId + '" id="btnScientist_' + itemId + '">'
							+ '<input type="checkbox" id="scientist_' + itemId + '" name="scientistIds" value="' + itemId + '" checked style="display: none;" />'
							+ '<i class="fa fa-square-o fa-2x"></i>\n'
							+ '<i class="fa fa-check-square-o fa-2x"></i>\n'
							+ '<span class="data-toggle-text-label">Chọn nhà khoa học</span>'
						+ '</label>'
					+ '</div>'
					+ '<div class="item-farmer hover padding-none margin-none">'
						+ '<a href="${pageContext.request.contextPath}/profile/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							+ '<img src="${pageContext.request.contextPath}/upload/avatar/' + itemAvatar + '" alt="' + itemName + '" class="img-fluid rounded-circle-xyz">'
						+ '</a>'
						+ '<div class="name-of-farmer text-muted font-13">'
							+ html
							//+ '<a href="${pageContext.request.contextPath}/profile/' + itemId + '/view" title="' + itemName + '" target="_blank">'
							//	+ itemName
							//+ '</a><br />'
							//+ '<div class="float-left full-width name_owner font-size-14 padding-top-5">'
							//	+ '<strong>Lĩnh vực tư vấn</strong>:<br />'
							//	+ '<div style="display: inline;">\n';
							//	for (var i = 0; i < itemMajors.length; i++) {
							//		newScientist = newScientist 
							//			+ '<span class="tag-item small">' + itemMajors[i] + '</span>\n';
							//	}
			
			newScientist = newScientist
							//	+ '</div>'
							//+ '</div>'
						+ '</div>'
					+ '</div>'
				+ '</div>'
			+ '</div>';
	
			var html = $.parseHTML(newScientist);
			$('#scientistPanel').append(html);
		}
	}
	
	function selectProject(itemId) {
		
	}
	
	function selectResources() {
		var resourceType = $('#searchResource #resourceType').val();
		var $selectedItems = $('#searchResource input[name=ids]:checked');
		
		$selectedItems.each(function(index, el) {
			var itemId = el.value;
			
			if (resourceType == 0) {
				selectProduct(itemId);
			} else if (resourceType == 1) {
				selectLand(itemId);
			} else if (resourceType == 2) {
				selectScientist(itemId);
			} else if (resourceType == 3) {
				selectProject(itemId);
			}
		});
		
		$('#searchResource').modal('hide');
	}
	
	$(function() {
		setLocationControls('country', 'province', 'district', 'location', true);
		setLocationValues(provinces_${rndValue}, districts_${rndValue}, locations_${rndValue});
		
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
		
		new AutoNumeric('#square', numberOption);
		new AutoNumeric('#money', numberOption);
		
		$('#addFile').click(function() {
			var fileIndex = $('#filePanel div.picture-container').children().length;
			
			var newFileItem = $('<div>').attr('class', 'col-lg-2 col-md-3 col-sm-4 col-xs-12')
				.append(
						$('<div>').attr('class', 'margin-bottom-10')
						.append(
								$('<div>').attr('class', 'row')
								.append(
										$('<div>').attr('class', 'col-xs-12 picture-container')
										.append(
												$('<div>').attr('class', 'picture')
												.append(
														$('<input>').attr('type', 'file')
															.attr('id', 'imageFile_' + fileIndex)
															.attr('name', 'imageFiles[' + fileIndex + ']')
															.attr('onchange', 'previewImage(this, ' + fileIndex + ')')
												)
												.append(
														$('<img>')
															.attr('id', 'imagePreview_' + fileIndex)
															.attr('class', 'picture-src')
															.attr('src', defaultImage)
												)
										)
								)
						)
						.append(
								$('<div>').attr('class', 'row')
								.append(
										$('<div>').attr('class', 'col-xs-12 text-inline text-center')
										.append(
												$('<input>').attr('type', 'radio')
													.attr('id', 'mainImage_' + fileIndex)
													.attr('name', 'mainImage')
													.attr('value', '')
										)
										.append(
												$('<label>').attr('for', 'mainImage_' + fileIndex)
													.html('&nbsp;Ảnh chính')
										)
								)
						)
						.append(
								$('<div>').attr('class', 'row')
								.append(
										$('<div>').attr('class', 'col-xs-12 text-inline text-center')
										.append(
												$('<input>').attr('type', 'radio')
													.attr('id', 'posterImage_' + fileIndex)
													.attr('name', 'posterImage')
													.attr('value', '')
										)
										.append(
												$('<label>').attr('for', 'posterImage_' + fileIndex)
													.html('&nbsp;Ảnh toàn cảnh')
										)
								)
						)
						.append(
								$('<div>').attr('class', 'row')
								.append(
										$('<div>').attr('class', 'col-xs-12 text-inline text-center')
										.append(
												$('<a>').attr('class', 'btn btn-warning btn-sm')
													.attr('href', 'javascript:;')
													.attr('onclick', 'removeSelectImage(' + fileIndex + ')')
													.html('<i class=\'fa fa-times\'></i>&nbsp;Xóa ảnh')
										)
								)
						)
				);
			
			$('#filePanel').append(newFileItem);
		});
	});
</script>