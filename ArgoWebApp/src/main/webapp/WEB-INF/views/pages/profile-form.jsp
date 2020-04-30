<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>

<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2 following">
	<div class="container">
		<div class="row">
			<h1 class="col-sm-12 text-center text-bold">Chỉnh sửa thông tin cá nhân</h1>
			
			<div class="col-md-12 col-sm-12 following">
				<div class="box box-info">
					<form:form name="form" action="save" method="POST" 
						modelAttribute="profileDto" enctype="multipart/form-data">
						
						<div class="box-body">
							<div class="row">
								<div class="col-lg-2 col-md-4 col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="imageFile" class="control-label">Ảnh đại diện</label>
										<div class="picture-container text-left">
											<div class="picture margin-none">
												<input type="file" name="imageFile" id="imageFile" onchange="previewImage(this, 0)">
												<c:choose>
								    				<c:when test="${ profileDto.avatar == null || profileDto.avatar == '' }">
														<img id="imagePreview_0" src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg" class="picture-src">
													</c:when>
													<c:otherwise>
														<img id="imagePreview_0" src="${pageContext.request.contextPath}/upload/avatar/${ profileDto.avatar }" class="picture-src">
													</c:otherwise>
												</c:choose>
											</div>
										</div>
										<div class="has-error">
											<form:errors path="imageFile" class="help-inline" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-5 col-md-8 col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="email" class="control-label">Địa chỉ email (dùng để đăng nhập hệ thống) <span class="reqired-input">*</span></label>
										<form:input path="email" name="email" id="email" class="form-control" 
											placeholder="Địa chỉ email" required="required" />
										<div class="has-error">
											<form:errors path="email" class="help-inline" />
										</div>
									</div>
								</div>
								
								<div class="col-lg-5 col-md-8 col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="name" class="control-label">Tên hiển thị (Cá nhân/Tổ chức, Doanh nghiệp/CQNN)<span class="reqired-input">*</span></label>
										<form:input path="name" name="name" id="name" class="form-control" required="required" />
										<div class="has-error">
											<form:errors path="name" class="help-inline" />
										</div>
									</div>
								</div>
								
								<div class="col-md-4 col-sm-4 col-xs-12">
									<div class="form-group">
										<label for="address" class="control-label">Địa chỉ (tổ, thôn, xóm, khu phố, ...) <span class="reqired-input">*</span></label>
										<form:input path="address" name="address" id="address" class="form-control" 
											placeholder="Số nhà, đường, tổ, thôn, xóm, làng, khu phố, ..." required="required" />
										<div class="has-error">
											<form:errors path="address" class="help-inline" />
										</div>
									</div>
								</div>
								<div class="col-md-2 col-sm-2 col-xs-12">
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
								
								<div class="col-md-2 col-sm-3 col-xs-12">
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
								
								<div class="col-md-2 col-sm-3 col-xs-12">
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
								<div class="col-md-3 col-sm-6 col-xs-12">
									<div class="form-group">
										<label for="mobilePhone" class="control-label">Điện thoại di động <span class="reqired-input">*</span></label>
										<form:input path="mobilePhone" name="mobilePhone" id="mobilePhone" class="form-control" placeholder="Điện thoại di động" />
										<div class="has-error">
											<form:errors path="mobilePhone" class="help-inline" />
										</div>
									</div>
								</div>
								<div class="col-md-3 col-sm-6 col-xs-12">
									<div class="form-group">
										<label for="companyPhone" class="control-label">Điện thoại công ty</label>
										<form:input path="companyPhone" name="companyPhone" id="companyPhone" class="form-control" placeholder="Điện thoại công ty" />
										<div class="has-error">
											<form:errors path="companyPhone" class="help-inline" />
										</div>
									</div>
								</div>
								<div class="col-md-3 col-sm-6 col-xs-12">
									<div class="form-group">
										<label for="homePhone" class="control-label">Điện thoại nhà riêng</label>
										<form:input path="homePhone" name="homePhone" id="homePhone" class="form-control" placeholder="Điện thoại nhà" />
										<div class="has-error">
											<form:errors path="homePhone" class="help-inline" />
										</div>
									</div>
								</div>
								<div class="col-md-3 col-sm-6 col-xs-12">
									<div class="form-group">
										<label for="fax" class="control-label">Số fax</label>
										<form:input path="fax" name="fax" id="fax" class="form-control" placeholder="Số fax" />
										<div class="has-error">
											<form:errors path="fax" class="help-inline" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-lg-12 col-sm-12 col-xs-12">
									<div class="form-group">
										<label for="role" class="control-label">Vai trò <span class="reqired-input">*</span></label>
										<span id="role" class="form-control">
				                        	<c:forEach items="${allRoles}" var="role" varStatus="loop">
				                        		<span class="form-control-list">
				                        			<spring:message code="register.role.${role.toLowerCase()}" var="roleLabel"></spring:message>
													<form:checkbox path="roles" value="${role}" label="${roleLabel}" 
														id="role_${ loop.index }" name="role_${ loop.index }" onclick="changeRole(this, ${ allRoles.size() })" />
												</span>
											</c:forEach>
										</span>
										<div class="has-error">
											<form:errors path="roles" class="help-inline" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="row" id="companyInfo" <c:if test="${ !profileDto.company }">style="display: none;"</c:if>>
								<div class="col-xs-12">
									<div class="card card-rectangle margin-left-none margin-right-none padding-15">
										<div class="row">
											<div class="col-xs-12">
												<h4 class="text-center bold-title material-green" style="font-size: 15px;">Thông tin về doanh nghiệp/tổ chức/HTX</h4>
												<hr class="mint">
											</div>
										</div>
										<div class="row">
											<div class="col-md-2 col-sm-5 col-xs-12">
												<div class="form-group">
													<label for="brandName" class="control-label">Tên viết tắt hoặc thương hiệu</label>
													<form:input path="brandName" name="brandName" id="brandName" class="form-control" placeholder="Tên viết tắt hoặc thương hiệu" />
													<div class="has-error">
														<form:errors path="brandName" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-3 col-sm-7 col-xs-12">
												<div class="form-group">
													<label for="typeId" class="control-label">Loại hình</label>
													<form:select path="typeId" id="typeId" name="typeId" 
														class="form-control selectpicker" title="Loại hình công ty">
														<form:options items="${companyTypes}"></form:options>
													</form:select>
													<form:errors path="typeId" class="help-inline" />
												</div>
											</div>
											<div class="col-md-2 col-sm-5 col-xs-12">
												<div class="form-group">
													<label for="taxCode" class="control-label">Mã số thuế</label>
													<form:input path="taxCode" name="taxCode" id="taxCode" class="form-control" placeholder="Mã số thuế" />
													<div class="has-error">
														<form:errors path="taxCode" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-7 col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="companyFieldId" class="control-label">Lĩnh vực kinh doanh</label>
													<form:select path="companyFieldIds" id="companyFieldId" name="companyFieldId" multiple="multiple"
														class="form-control selectpicker" title="Lĩnh vực kinh doanh">
														<form:options items="${companyFields}"></form:options>
													</form:select>
													<form:errors path="companyFieldIds" class="help-inline" />
												</div>
											</div>
										</div>
										
										<div class="row">
											<div class="col-xs-12">
												<div class="form-group">
													<label for="description" class="control-label">Thông tin mô tả</label>
													<form:textarea path="description" name="description" id="description" class="form-control" rows="3"></form:textarea>
													<div class="has-error">
														<form:errors path="description" class="help-inline" />
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row" id="personInfo" <c:if test="${ !profileDto.farmer && !profileDto.scientist }">style="display: none;"</c:if>>
								<div class="col-xs-12">
									<div class="card card-rectangle margin-left-none margin-right-none padding-15">
										<div class="row">
											<div class="col-xs-12">
												<h4 class="text-center bold-title material-green" style="font-size: 15px;">Thông tin cá nhân</h4>
												<hr class="mint">
											</div>
										</div>
										<div class="row">
											<div class="col-md-3 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="birthDay" class="control-label">Ngày sinh</label>
													<div class="input-group date" data-provide="datepicker" data-date-format="${ dateFormatLower }">
														<form:input path="birthDay" name="birthDay" id="birthDay" class="form-control" />
														<div class="input-group-addon">
													        <span class="glyphicon glyphicon-th"></span>
													    </div>
													</div>
													<div class="has-error">
														<form:errors path="birthDay" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-md-3 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="gender" class="control-label">Giới tính</label>
													<form:select path="gender" id="gender" name="gender" 
														class="form-control selectpicker" title="Giới tính">
														<form:options items="${allGenders}"></form:options>
													</form:select>
													<form:errors path="gender" class="help-inline" />
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row" id="scientistInfo" <c:if test="${ !profileDto.scientist }">style="display: none;"</c:if>>
								<div class="col-xs-12">
									<div class="card card-rectangle margin-left-none margin-right-none padding-15">
										<div class="row">
											<div class="col-xs-12">
												<h4 class="text-center bold-title material-green" style="font-size: 15px;">Thông tin nhà khoa học</h4>
												<hr class="mint">
											</div>
										</div>
										
										<div class="row">
											<div class="col-lg-2 col-md-3 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="title" class="control-label">Chức danh khoa học</label>
													<form:input path="title" name="title" id="title" class="form-control" />
													<div class="has-error">
														<form:errors path="title" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-lg-2 col-md-3 col-sm-6 col-xs-12">
												<div class="form-group">
													<label for="position" class="control-label">Chức vụ</label>
													<form:input path="position" name="position" id="position" class="form-control" />
													<div class="has-error">
														<form:errors path="position" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-lg-3 col-md-6 col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="workplace" class="control-label">Đơn vị công tác</label>
													<form:input path="workplace" name="workplace" id="workplace" class="form-control" />
													<div class="has-error">
														<form:errors path="workplace" class="help-inline" />
													</div>
												</div>
											</div>
											<div class="col-lg-5 col-md-12 col-sm-12 col-xs-12">
												<div class="form-group">
													<label for="literacyId" class="control-label">Trình độ học vấn</label>
													<form:select path="literacyIds" id="literacyId" name="literacyId" multiple="multiple"
														class="form-control selectpicker" title="Học vấn">
														<form:options items="${literacies}"></form:options>
													</form:select>
													<form:errors path="literacyIds" class="help-inline" />
												</div>
											</div>
										</div>
										
										<c:forEach items="${scientistMajors}" var="scientistMajor">
											<div class="row">
												<div class="col-xs-12">
													<fieldset>
														<legend class="form-control-list margin-top-20 margin-bottom-5">
															<form:checkbox path="scientistMajorIds" value="${scientistMajor.id}" label="${scientistMajor.name}" />
														</legend>
														
														<div class="row padding-left-30 padding-right-30">
															<c:forEach items="${scientistMajor.scientistFields}" var="scientistField">
								                        		<div class="col-md-6 col-xs-12 form-control-list">
																	<form:checkbox path="scientistFieldIds" value="${scientistField.id}" label="${scientistField.name}" />
																</div>
															</c:forEach>
														</div>
														
														<div class="row padding-left-30 padding-right-30">
															<div class="col-xs-12">
																<div class="input-group">
																	<div class="input-group-addon">Lĩnh vực khác: </div>
																	<form:input path="fieldOthers['${scientistMajor.id}']" class="form-control" />
																</div>
															</div>
														</div>
														
														<div class="row padding-left-30 padding-right-30">
															<div class="col-xs-12">
																<div class="form-group">
																	<label class="control-label">Mô tả thêm về lĩnh vực hoạt động</label>
																	<form:textarea path="fieldDescriptions['${scientistMajor.id}']" class="form-control" rows="3"></form:textarea>
																</div>
															</div>
														</div>
													</fieldset>
												</div>
											</div>
										</c:forEach>
  									</div>
								</div>
							</div>
						</div>
						
						<div class="box-footer">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/profile" type="button" class="btn btn-default">
									<i class="fa fa-arrow-left"></i> Hủy bỏ
								</a>
								<button type="submit" class="btn btn-info">
									<i class="fa fa-save"></i> Lưu thay đổi
								</button>
							</span>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</section>

<script type="text/javascript">
	var defaultImage = '${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg';
	
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
			}
			
			reader.readAsDataURL(input.files[0]);
		} else {
			$('#imagePreview_' + index).attr('src,', defaultImage).fadeIn('slow');
		}
	}
	
	function changeRole(el, maxIndex) {
		var selected = el.checked;
		var value = el.value;
		
		if (selected) {
			if ('COM' == value) {
				$('#companyInfo').show();
			} else if ('SCI' == value) {
				$('#scientistInfo').show();
			}
			
			if ('SCI' == value || 'FAR' == value) {
				$('#personInfo').show();
			}
		} else {
			if ('COM' == value) {
				$('#companyInfo').hide();
			} else if ('SCI' == value) {
				$('#scientistInfo').hide();
			}
			
			if ('SCI' == value || 'FAR' == value) {
				var havePersonInfor = false;
				for (var i = 0; i < maxIndex; i++) {
					var roleEl = $('#role_' + i);
					var roleValue = roleEl.val();
					
					if ('SCI' == roleValue || 'FAR' == roleValue) {
						if (roleEl.is(':checked')) {
							havePersonInfor = true;
							break;
						}
					}
				}
				
				if (havePersonInfor) {
					$('#personInfo').show();
				} else {
					$('#personInfo').hide();
				}
			}
		}
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
	
		setLocationControls('country', 'province', 'district', 'location', true);
		setLocationValues(provinces_${rndValue}, districts_${rndValue}, locations_${rndValue});
	});
</script>