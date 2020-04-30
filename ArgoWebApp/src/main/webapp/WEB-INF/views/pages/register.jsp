<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<style type="text/css">
	@media (min-width: 992px) {
		.lg-padding-left-none {
			padding-left: 0px;
		}
		
		.lg-padding-right-none {
			padding-right: 0px;
		}
	}
	
	@media (min-width: 768px) {
		.md-padding-left-none {
			padding-left: 0px;
		}
		
		.md-padding-right-none {
			padding-right: 0px;
		}
	}
</style>
<section class="login-form posts-2 table-center" style="background: none;">
    <div class="container-fluid cell-center">
        <div class="row">
            <div class="offset-lg-1 col-lg-10 col-xs-12">
                <div class="login-content">
                	<div class="row">
						<div class="col-xs-12">
							<div class="logo-container text-center">
								<img class="img" src="${pageContext.request.contextPath}/resources/images/logo-2.png">
							</div>
							<h2 class="text-center text-bold">
								<br>
								<spring:message code="register.page.title"></spring:message>
							</h2>
						</div>
					</div>
                    <form:form name="form" action="register" method="POST" enctype="multipart/form-data"
							modelAttribute="registerDto" class="form-horizontal-x">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						
						<div class="row">
							<div class="col-lg-2 col-md-4 col-sm-12 col-xs-12">
								<div class="form-group">
									<label for="imageFile" class="control-label">Ảnh đại diện</label>
									<div class="picture-container text-left">
										<div class="picture margin-none">
											<input type="file" name="imageFile" id="imageFile" onchange="previewImage(this, 0)">
											<img id="imagePreview_0" src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg" class="picture-src">
										</div>
									</div>
									<div class="has-error">
										<form:errors path="imageFile" class="help-inline" />
									</div>
								</div>
							</div>
							<div class="col-lg-10 col-md-8 col-sm-12 col-xs-12">
								<div class="row">
									<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="email" class="control-label">Địa chỉ email (dùng để đăng nhập hệ thống) <span class="reqired-input">*</span></label>
											<form:input path="email" name="email" id="email" class="form-control" 
												placeholder="Địa chỉ email" required="required" />
											<div class="has-error">
												<form:errors path="email" class="help-inline" />
											</div>
										</div>
									</div>
									<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="name" class="control-label">Tên hiển thị <span class="reqired-input">*</span></label>
											<form:input path="name" name="name" id="name" class="form-control" required="required" />
											<div class="has-error">
												<form:errors path="name" class="help-inline" />
											</div>
										</div>
									</div>
									<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="password" class="control-label">Mật khẩu đăng nhập <span class="reqired-input">*</span></label>
											<form:password path="password" name="password" id="password" class="form-control" required="required" />
											<div class="has-error">
												<form:errors path="password" class="help-inline" />
											</div>
										</div>
									</div>
									<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="confirmPassword" class="control-label">Nhập lại mật khẩu <span class="reqired-input">*</span></label>
											<form:password path="confirmPassword" name="confirmPassword" id="confirmPassword" 
												class="form-control" required="required" />
											<div class="has-error">
												<form:errors path="confirmPassword" class="help-inline" />
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-4 col-xs-12">
								<div class="form-group">
									<label for="address" class="control-label">Địa chỉ (tổ, thôn, xóm, khu phố, ...) <span class="reqired-input">*</span></label>
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
							<div class="col-lg-3 col-md-3 col-xs-6">
								<div class="form-group">
									<label for="mobilePhone" class="control-label">Điện thoại di động <span class="reqired-input">*</span></label>
									<form:input path="mobilePhone" name="mobilePhone" id="mobilePhone" class="form-control" 
										placeholder="Điện thoại di động" required="required" />
									<div class="has-error">
										<form:errors path="mobilePhone" class="help-inline" />
									</div>
								</div>
							</div>
							<div class="col-lg-3 col-md-3 col-xs-6">
								<div class="form-group">
									<label for="companyPhone" class="control-label">Điện thoại công ty</label>
									<form:input path="companyPhone" name="companyPhone" id="companyPhone" class="form-control" 
										placeholder="Điện thoại công ty" />
									<div class="has-error">
										<form:errors path="companyPhone" class="help-inline" />
									</div>
								</div>
							</div>
							<div class="col-lg-3 col-md-3 col-xs-6">
								<div class="form-group">
									<label for="homePhone" class="control-label">Điện thoại nhà riêng</label>
									<form:input path="homePhone" name="homePhone" id="homePhone" class="form-control" 
										placeholder="Điện thoại nhà" />
									<div class="has-error">
										<form:errors path="homePhone" class="help-inline" />
									</div>
								</div>
							</div>
							<div class="col-lg-3 col-md-3 col-xs-6">
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
						
						<%--
                        <div class="form-group with-icon relative" title="${namePlaceholder}">
                        	<span class="left-icon fa fa-user-circle-o"></span>
                        	<form:input path="name" name="name" id="name" type="text" cssClass="form-control" 
                        		placeholder="${namePlaceholder}" required="required" />
                        	<span class="right-icon reqired-input">*</span>
							<form:errors path="name" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-icon relative" title="${phonePlaceholder}">
                        	<span class="left-icon fa fa-phone"></span>
                        	<form:input path="phone" name="phone" id="phone" type="text" cssClass="form-control" 
                        		placeholder="${phonePlaceholder}" required="required" />
                            <span class="right-icon reqired-input">*</span>
                            <form:errors path="phone" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-icon relative" title="${emailPlaceholder}">
                        	<span class="left-icon fa fa-envelope-o"></span>
                        	<form:input path="email" name="email" id="email" type="email" cssClass="form-control" 
                        		placeholder="${emailPlaceholder}" required="required" />
                            <span class="right-icon reqired-input">*</span>
                            <form:errors path="email" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-icon relative" title="${passwordPlaceholder}">
                        	<span class="left-icon fa fa-lock"></span>
                        	<form:password path="password" name="password" id="password" cssClass="form-control" 
                        		placeholder="${passwordPlaceholder}" required="required" />
                            <span class="right-icon reqired-input">*</span>
                            <form:errors path="password" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-icon relative" title="${confirmPasswordPlaceholder}">
                        	<span class="left-icon fa fa-unlock-alt"></span>
                        	<form:password path="confirmPassword" name="confirmPassword" id="confirmPassword" cssClass="form-control" 
                        		placeholder="${confirmPasswordPlaceholder}" required="required" />
                            <span class="right-icon reqired-input">*</span>
                            <form:errors path="confirmPassword" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-icon relative" title="${addressPlaceholder}">
                        	<span class="left-icon fa fa-map-marker"></span>
                        	<form:input path="address" name="address" id="address" type="text" cssClass="form-control" 
                        		placeholder="${addressPlaceholder}" required="required" />
                        	<span class="right-icon reqired-input">*</span>
							<form:errors path="address" cssClass="has_error" element="div" />
                        </div>
                        <div class="form-group with-left-icon relative" title="<spring:message code="register.placeholder.role"></spring:message>">
                        	<span class="left-icon fa fa-users"></span>
                        	<span id="role" class="form-control">
	                        	<c:forEach items="${allRoles}" var="role">
	                        		<span class="form-control-list">
	                        			<spring:message code="register.role.${role.toLowerCase()}" var="roleLabel"></spring:message>
										<form:checkbox path="roles" value="${role}" label="${roleLabel}" />
									</span>
								</c:forEach>
							</span>
							<span class="right-icon reqired-input">*</span>
							<form:errors path="roles" cssClass="has_error" element="div" />
                        </div>
                        --%>
                        <div class="clearfix">
                            &nbsp;
                        </div>
                        <div class="form-group">
                           	<a href="${pageContext.request.contextPath}/home" class="btn btn-warning" style="color: red;">
                           		<spring:message code="register.action.goto.home"></spring:message>
                           	</a>
                            <button type="submit" class="btn btn-success">
                            	<spring:message code="register.action.register"></spring:message>
                            </button>
                            <div class="pull-right" style="margin-top: 15px;">
                            	<spring:message code="register.message.have.account"></spring:message>
                                <a href="${pageContext.request.contextPath}/login" style="color: red;">
                                	<spring:message code="register.action.goto.login"></spring:message>
                                </a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        
		<%-- <div class="row">
            <div class="offset-md-3 col-md-6 col-xs-12">
				<c:set var="lang">${pageContext.response.locale.language}</c:set>
		        <div class="language-container text-center">
		        	<a href="?lang=vi">
		        		<img src="<c:url value='/resources/images/language_vietnamese_inactive.png' />" 
		        				alt="Tiếng Việt" class="flag <c:if test="${lang == 'vi'}">active</c:if>" />
		        	</a>
		            <a href="?lang=en">
		            	<img src="<c:url value='/resources/images/language_english_inactive.png' />" 
		            			alt="English" class="flag <c:if test="${lang == 'en'}">active</c:if>" />
		            </a>
		        </div>
        	</div>
        </div> --%>
    </div>
</section>

<!-- Select Picker -->
<script src="${pageContext.request.contextPath}/resources/js/bootstrap-select.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/select-util.js"></script>

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
		/* var selected = el.checked;
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
		} */
	}
	
	$(function() {
		setLocationControls('country', 'province', 'district', 'location', true);
		setLocationValues(provinces_${rndValue}, districts_${rndValue}, locations_${rndValue});
	});
</script>