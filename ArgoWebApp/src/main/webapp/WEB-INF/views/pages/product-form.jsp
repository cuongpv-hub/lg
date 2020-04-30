<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>

<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">
				<c:choose>
					<c:when test="${ dto.id == null || dto.id == '' }">
						Thêm mới nông sản
					</c:when>
					<c:otherwise>
						Chỉnh sửa nông sản
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
							<c:set var="contactGroupTitle" value="Thông tin của tổ chức, cá nhân có sản phẩm nông sản (hoặc đại diện cho các hộ có nông sản)" scope="request" />
							<c:set var="contactNameLabel" value="Tên tổ chức, cá nhân" scope="request" />
	                        <c:set var="contactAliasLabel" value="Tên viết tắt / thương hiệu" scope="request" />
							<jsp:include page="resource-contact-form.jsp"></jsp:include>
							
							<div class="card-box padding-bottom-5">
                    			<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
                        			<h4 class="col-xs-12 text-bold color-1ab394 padding-left-none margin-top-none margin-bottom-10 text-uppercase text-underline">
                        				Thông tin về hàng hóa nông sản địa phương đang sản xuất
                        			</h4>
                        			
                        			<div class="col-xs-12 padding-none">
										<div class="row">
											<div class="col-md-4 col-sm-4 col-xs-12">
												<div class="form-group">
													<label for="categoryId" class="control-label">Loại nông sản <span class="reqired-input">*</span></label>
													<form:select path="categoryIds" id="categoryId" name="categoryId" 
														class="form-control selectpicker" multiple="multiple"
														title="Chọn loại nông sản" required="required">
														<form:options items="${categories}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="categoryIds" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-8 col-sm-8 col-xs-12">
												<div class="form-group">
													<label for="name" class="control-label">Tên hàng hóa nông sản <span class="reqired-input">*</span></label>
													<form:input path="name" name="name" id="name" class="form-control" required="required" />
													<div class="has-error">
														<form:errors path="name" class="help-inline" />
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
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="square" class="control-label">Diện tích khoảng</label>
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
														<form:errors path="squareUnitId" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="volume" class="control-label">Sản lượng mỗi vụ (năm) <span class="reqired-input">*</span></label>
													<div class="row">
														<div class="col-xs-7 padding-right-none">
															<form:input path="volume" name="volume" id="volume" class="form-control text-number" required="required" />
														</div>
														<div class="col-xs-5 padding-left-none">
															<form:select path="volumeUnitId" id="volumeUnit" name="volumeUnit" 
																required="required" class="selectpicker show-tick">
																<form:options items="${volumeUnits}"></form:options>
															</form:select>
														</div>
													</div>
													<div class="has-error">
														<form:errors path="volume" class="help-inline" />
														<form:errors path="volumeUnitId" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-md-3 col-xs-6">
												<div class="form-group">
													<label for="startDate" class="control-label">Có sản phẩm từ ngày <span class="reqired-input">*</span></label>
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
													<label for="endDate" class="control-label">Đến ngày <span class="reqired-input">*</span></label>
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
										</div>
										
										<div class="row">
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12 padding-right-10">
												<div class="form-group">
													<label for="moneyFrom" class="control-label">Giá bán giao động theo thị trường từ</label>
													<form:input path="moneyFrom" name="moneyFrom" id="moneyFrom" class="form-control text-number" />
													<div class="has-error">
														<form:errors path="moneyFrom" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12 padding-left-10">
												<div class="form-group">
													<label for="moneyTo" class="control-label">Đến</label>
													<div class="row">
														<div class="col-xs-7 padding-right-none">
															<form:input path="moneyTo" name="moneyTo" id="moneyTo" class="form-control text-number" />
														</div>
														<div class="col-xs-5 padding-left-none">
															<form:select path="moneyUnitId" id="moneyUnit" name="moneyUnit" class="selectpicker show-tick">
																<form:options items="${moneyUnits}"></form:options>
															</form:select>
														</div>
													</div>
													<div class="has-error">
														<form:errors path="moneyTo" class="help-inline" />
														<form:errors path="moneyUnitId" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12 padding-right-10">
												<div class="form-group">
													<label for="priceFrom" class="control-label">Giá bán mong muốn đề xuất từ <span class="reqired-input">*</span></label>
													<form:input path="priceFrom" name="priceFrom" id="priceFrom" class="form-control text-number" required="required" />
													<div class="has-error">
														<form:errors path="priceFrom" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-lg-3 col-md-6 col-sm-6 col-xs-12 padding-left-10">
												<div class="form-group">
													<label for="priceTo" class="control-label">Đến <span class="reqired-input">*</span></label>
													<div class="row">
														<div class="col-xs-7 padding-right-none">
															<form:input path="priceTo" name="priceTo" id="priceTo" class="form-control text-number" required="required" />
														</div>
														<div class="col-xs-5 padding-left-none">
															<form:select path="priceUnitId" id="priceUnit" name="priceUnit" 
																required="required" class="selectpicker show-tick">
																<form:options items="${moneyUnits}"></form:options>
															</form:select>
														</div>
													</div>
													<div class="has-error">
														<form:errors path="priceTo" class="help-inline" />
														<form:errors path="priceUnitId" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-md-8 col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="formCooperationId" class="control-label">Mong muốn hợp tác với doanh nghiệp theo hình thức</label>
													<form:select path="formCooperationIds" id="formCooperationId" name="formCooperationId" 
														class="form-control selectpicker" multiple="multiple"
														title="Lựa chọn hình thức hợp tác">
														<form:options items="${formCooperations}"></form:options>
													</form:select>
													<div class="has-error">
														<form:errors path="formCooperationIds" class="help-inline" />
													</div>
												</div>
											</div>
											
											<div class="col-md-4 col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="name" class="control-label">Hình thức khác</label>
													<form:input path="formCooporateDiff" name="formCooporateDiff" id="formCooporateDiff" class="form-control" />
													<div class="has-error">
														<form:errors path="formCooporateDiff" class="help-inline" />
													</div>
												</div>
											</div>	
										</div>
										
										<%-- <div class="row">
											<div class="col-md-6 col-xs-12">
												<div class="form-group">
													<label for="approveName" class="control-label">Đơn vị cấp phép</label>
													<form:input path="approveName" name="approveName" id="approveName" class="form-control" />
													<div class="has-error">
														<form:errors path="approveName" class="help-inline" />
													</div>
												</div>
											</div>
										</div> --%>
										
										<div class="row">
											<div class="col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="description" class="control-label">Mô tả chi tiết về đặc tính của hàng hóa nông sản (Chất lượng sản phẩm, đặc điểm, lợi ích, ...) <span class="reqired-input">*</span></label>
													<form:textarea path="description" name="description" id="description" 
														rows="10" class="form-control" />
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
							
							<!-- Product Image -->
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
											
											<div class="row table-row-xyz">
												<c:forEach items="${imageDtos}" var="image" varStatus="loop">
													<div class="col-lg-2 col-md-3 col-sm-4 col-xs-12">
														<div class="table-cell-content-xyz">
															<div class="row">
																<div class="col-xs-12 picture-container text-center">
																	<img src="${pageContext.servletContext.contextPath}/upload/product/${ image.name }" class="picture-src">
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
								<a href="${pageContext.request.contextPath}/product" type="button" class="btn btn-default">
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
        	<form action="${pageContext.request.contextPath}/product/${ dto.id }/process" method="POST">
	            <div class="modal-header">
					<h2>Gửi yêu cầu phê duyệt nông sản</h2>
	            </div>
	            <div class="modal-body">
	            	<div class="row">
						<div class="col-xs-12">
							<strong>Lưu ý:</strong>
							<ul>
								<li>Bạn cần lưu tất cả thay đổi trước khi gửi yêu cầu phê duyệt nông sản.</li>
								<li>Nếu bạn được cấp quyền phê duyệt, nông sản của bạn sẽ được tự động phê duyệt.</li>
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

<script type="text/javascript">
	var defaultImage = '${pageContext.servletContext.contextPath}/resources/images/no-image-icon.png';
	
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