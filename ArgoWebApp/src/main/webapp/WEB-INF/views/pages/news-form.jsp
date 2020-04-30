<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>

<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following" ng-controller="NewsController">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center" style="font-weight: bold;">
				<c:choose>
					<c:when test="${ dto.id == null}">
						Thêm mới bài viết
					</c:when>
					<c:otherwise>
						Chỉnh sửa bài viết
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
							<div class="row">
								<div class="col-md-4 col-sm-4 col-xs-12">
									<div class="form-group">
										<label for="categoryId" class="control-label">Loại bài viết</label>
										<form:select path="categoryIds" id="categoryId" name="categoryId" 
											class="form-control selectpicker" multiple="multiple"
											title="Lựa chọn loại bài viết">
											<form:options items="${categories}"></form:options>
										</form:select>
										<div class="has-error">
											<form:errors path="categoryIds" class="help-inline" />
										</div>
									</div>
								</div>
								<div class="col-md-8 col-sm-8 col-xs-12">
									<div class="form-group">
										<label for="name" class="control-label">Tên bài viết</label>
										<form:input path="name" name="name" id="name" class="form-control" />
										<div class="has-error">
											<form:errors path="name" class="help-inline" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="row">
								
								<div class="col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="approveName" class="control-label">Đơn vị cấp phép</label>
										<form:input path="approveName" name="approveName" id="approveName" class="form-control" />
										<div class="has-error">
											<form:errors path="approveName" class="help-inline" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="description" class="control-label">Mô tả</label>
										<form:textarea path="description" name="description" id="description" 
											rows="10" class="form-control" />
										<ckeditor:replace replace="description" basePath="${pageContext.servletContext.contextPath}/resources/ckeditor/"/>
										<div class="has-error">
											<form:errors path="description" class="help-inline" />
										</div>
									</div>
								</div>
							</div>
							
							<%-- <div class="row">
								<div class="col-md-6 col-xs-12">
									<div class="form-group">
										<label for="startDate" class="control-label">Thời gian dự kiến</label>
										<div class="row">
											<div class="col-sm-6 col-xs-12">
												<div class="input-group date" data-provide="datepicker" data-date-format="${ dateFormatLower }">
													<form:input path="startDate" name="startDate" id="startDate" class="form-control" />
													<div class="input-group-addon">
												        <span class="glyphicon glyphicon-th"></span>
												    </div>
												</div>
												<div class="has-error">
													<form:errors path="startDate" class="help-inline" />
												</div>
											</div>
											<div class="col-sm-6 col-xs-12">
												<div class="input-group date" data-provide="datepicker" data-date-format="${ dateFormatLower }">
													<form:input path="endDate" name="endDate" id="endDate" class="form-control" />
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
								</div>
								<div class="col-md-3 col-xs-12">
									<div class="form-group">
										<label for="volume" class="control-label">Khối lượng</label>
										<div class="row">
											<div class="col-xs-6 padding-right-none">
												<form:input path="volume" name="volume" id="volume" class="form-control" />
											</div>
											<div class="col-xs-6 padding-left-none">
												<form:select path="volumeUnitId" id="volumeUnit" name="volumeUnit" class="selectpicker">
													<form:options items="${volumeUnits}"></form:options>
												</form:select>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-3 col-xs-12">
									<div class="form-group">
										<label for="money" class="control-label">Giá</label>
										<div class="row">
											<div class="col-xs-6 padding-right-none">
												<form:input path="money" name="money" id="money" class="form-control" />
											</div>
											<div class="col-xs-6 padding-left-none">
												<form:select path="moneyUnitId" id="moneyUnit" name="moneyUnit" class="selectpicker">
													<form:options items="${moneyUnits}"></form:options>
												</form:select>
											</div>
										</div>
									</div>
								</div>
							</div> --%>
							
							<div class="row">
								<div class="col-sm-12 col-xs-12 picture-container-group">
									<div class="form-group">
										<label for="imageFiles" class="control-label">Hình ảnh&nbsp;&nbsp;&nbsp;&nbsp;</label>
										<a id="addFile" href="javascript:;" classx="btn btn-primary" title="Thêm hình ảnh">
											<i class="fa fa-image"></i> Thêm hình ảnh
										</a>
										
										<div class="row">
											<c:forEach items="${imageDtos}" var="image" varStatus="loop">
												<div class="col-lg-2 col-md-3 col-sm-4 col-xs-12">
													<div class="row">
														<div class="col-xs-12 picture-container text-center">
															<img src="${pageContext.servletContext.contextPath}/upload/news/${ image.name }" class="picture-src">
														</div>
													</div>
													<div class="row">
														<div class="col-xs-12">
															<form:radiobutton path="mainImage" id="existMainImage_${loop.index}" value="${ image.name }"/>
															<span for="existMainImage_${loop.index}">Ảnh chính</span>
														</div>
													</div>
													<div class="row">
														<div class="col-xs-12">
															<form:radiobutton path="posterImage" id="existPosterImage_${loop.index}" value="${ image.name }"/>
															<span for="existPosterImage_${loop.index}">Ảnh toàn cảnh</span>
														</div>
													</div>
													<div class="row">
														<div class="col-xs-12">
															<input type="checkbox" name="deleteImages[${loop.index}]" id="deleteExistImage_${loop.index}" value="${ image.id }" />
															<span for="deleteExistImage_${loop.index}">Xóa ảnh</span>
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
																<span for="mainImage_0">Ảnh chính</span>
															</div>
														</div>
														
														<div class="row">
															<div class="col-xs-12 text-inline text-center">
																<input type="radio" name="posterImage" id="posterImage_0" value="" />
																<span for="posterImage_0">Ảnh toàn cảnh</span>
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

						<!-- /.box-body -->
						<div class="box-footer">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/news" type="button" class="btn btn-default">
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
        	<form action="${pageContext.request.contextPath}/news/${ dto.id }/process" method="POST">
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