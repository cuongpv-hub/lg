<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<div class="modal fade" id="confirm-approve" tabindex="-1" role="dialog" 
	aria-labelledby="approveModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/${pageName}/${ dto.id }/approve" method="POST">
	            <div class="modal-header">
					<h2><spring:message code="page.${pageName}.approve.title"></spring:message></h2>
	            </div>
	            <div class="modal-body">
	            	<div class="row">
						<div class="col-xs-12">
							<div class="form-group">
								<label for="comment" class="control-label">Ghi chú</label>
	                			<textarea name="comment" rows="3" class="form-control"></textarea>
	                		</div>
	                	</div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-success">Đồng ý phê duyệt</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="confirm-reject" tabindex="-1" role="dialog" 
	aria-labelledby="rejectModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/${pageName}/${ dto.id }/reject" method="POST">
	            <div class="modal-header">
					<h2><spring:message code="page.${pageName}.reject.title"></spring:message></h2>
	            </div>
	            <div class="modal-body">
	            	<div class="row">
						<div class="col-xs-12">
							<div class="form-group">
								<label for="comment" class="control-label">Ghi chú</label>
	                			<textarea name="comment" rows="3" required="required" class="form-control"></textarea>
	                		</div>
	                	</div>
	                </div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-danger">Không đồng ý phê duyệt</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="loginAndLink" tabindex="-1" role="dialog" 
	aria-labelledby="processModalLabel" aria-hidden="true" data-backdrop="false">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<form action="${pageContext.request.contextPath}/${pageName}/${ dto.id }/view" method="GET">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">
						<spring:message code="page.${pageName}.link.title"></spring:message>
					</h3>
				</div>
				<div class="modal-body modal-body-content">
					<div>
						<input type="hidden" id="linkOption" name="linkOption" value="0">
						<input type="hidden" id="linkType" name="linkType" value="0">
						
						<ul class="nav nav-tabs">
							<li class="active" onclick="selectLinkWithProjectOption(0)">
								<a href="#login-tab" data-toggle="tab">Đăng nhập</a>
							</li>
							<li onclick="selectLinkWithProjectOption(1)">
								<a href="#register-tab" data-toggle="tab">Đăng ký thành viên</a>
							</li>
						</ul>
						
						<div class="tab-content">
							<div class="tab-pane active" id="login-tab">
								<br />
								<div class="form-group with-icon relative" title="Địa chỉ email hoặc số điện thoại">
		                        	<span class="left-icon fa fa-user-circle-o"></span>
		                        	<input name="loginAccount" id="loginAccount" type="text" class="form-control" 
		                        		placeholder="Địa chỉ email hoặc số điện thoại" required="required" />
		                        	<span class="right-icon reqired-input">*</span>
		                        </div>
		                        
		                        <div class="form-group with-icon relative" title="Mật khẩu">
		                        	<span class="left-icon fa fa-lock"></span>
		                        	<input name="loginPassword" id="loginPassword" type="password" class="form-control" 
		                        		placeholder="Mật khẩu" required="required" />
		                            <span class="right-icon reqired-input">*</span>
		                        </div>
		                        
		                        <div class="has-error login-error">
									<div class="help-inline">Lỗi đăng nhập hệ thống, bạn hãy kiểm tra lại thông tin đăng nhập!</div>
								</div>
							</div>
							<div class="tab-pane" id="register-tab">
								<br />
								<div class="row">
									<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="registerName" class="control-label">Tên liên hệ <span class="reqired-input">*</span></label>
											<input name="registerName" id="registerName" type="text" class="form-control" required="required" />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="registerPassword" class="control-label">Mật khẩu <span class="reqired-input">*</span></label>
											<input name="registerPassword" id="registerPassword" type="password" class="form-control" required="required" />
										</div>
									</div>
									<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label for="registerConfirmPassword" class="control-label">Nhập lại mật khẩu <span class="reqired-input">*</span></label>
											<input name="registerConfirmPassword" id="registerConfirmPassword" type="password" class="form-control" required="required" />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-9 col-md-8 col-xs-12">
										<div class="form-group">
											<label for="registerEmail" class="control-label">Địa chỉ email <span class="reqired-input">*</span></label>
											<input name="registerEmail" id="registerEmail" type="email" class="form-control" placeholder="Địa chỉ email" required="required" />
										</div>
									</div>
									<div class="col-lg-3 col-md-4 col-xs-12">
										<div class="form-group">
											<label for="registerPhone" class="control-label">ĐT di động <span class="reqired-input">*</span></label>
											<input name="registerPhone" id="registerPhone" type="text" class="form-control" placeholder="Điện thoại di động" required="required" />
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
														<input type="checkbox" value="${role}" id="registerRole_${ loop.index }" name="registerRole" />
														<label for="registerRole_${ loop.index }">${roleLabel}</label>
													</span>
												</c:forEach>
											</span>
										</div>
									</div>
								</div>
								
								<div class="has-error register-password-error">
									<div class="help-inline">Xác nhận mật khẩu không đúng, bạn hãy kiểm tra lại thông tin đăng ký!</div>
								</div>
								
								<div class="has-error register-email-error">
									<div class="help-inline">Địa chỉ email đã được đăng ký là thành viên của hệ thống, bạn hãy kiểm tra lại thông tin đăng ký!</div>
								</div>
								
								<div class="has-error register-phone-error">
									<div class="help-inline">Số điện thoại đã được đăng ký là thành viên của hệ thống, bạn hãy kiểm tra lại thông tin đăng ký!</div>
								</div>
								
								<div class="has-error register-error">
									<div class="help-inline">Lỗi đăng ký thành viên mới, bạn hãy kiểm tra lại thông tin đăng ký!</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="linkWithProject()" id="accept-button">Đăng nhập</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
				</div>
			</form>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmApproveLink" tabindex="-1" role="dialog" 
	aria-labelledby="approveLinkModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/${pageName}/${ dto.id }/link/linkId/approve" method="POST">
	            <div class="modal-header">
					<h2>Xác nhận tham gia liên kết</h2>
	            </div>
	            <div class="modal-body">
	            	<div style="text-bold">Bạn có đồng ý tham gia liên kết?</div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-success">Đồng ý tham gia</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="confirmDeleteLink" tabindex="-1" role="dialog" 
	aria-labelledby="deleteLinkModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/${pageName}/${ dto.id }/link/linkId/delete" method="POST">
	            <div class="modal-header">
					<h2>Xác nhận hủy bỏ liên kết</h2>
	            </div>
	            <div class="modal-body">
	            	<div style="text-bold">Bạn có đồng ý hủy bỏ liên kết?</div>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-danger">Đồng ý hủy bỏ</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="searchResource" tabindex="-1" role="dialog" 
	aria-labelledby="processModalLabel" aria-hidden="true" data-backdrop="false">
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
            	<button type="button" class="btn btn-primary" onclick="linkResources()">Đồng ý</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
	var numberOption = {
	    currencySymbol : '',
	    decimalCharacter : ',',
	    digitGroupSeparator : '.',
	    decimalPlacesRawValue: 3,
	    allowDecimalPadding: false
	};
	
	function loginAndLinkProductModal() {
		$('#loginAndLink .has-error').hide();
		$('#loginAndLink #linkType').val(0);
		
		$('#loginAndLink').modal({ show:true });
	}
	
	function loginAndLinkLandModal() {
		$('#loginAndLink .has-error').hide();
		$('#loginAndLink #linkType').val(1);
		
		$('#loginAndLink').modal({ show:true });
	}
	
	function loginAndLinkScientistModal() {
		$('#loginAndLink .has-error').hide();
		$('#loginAndLink #linkType').val(2);
		
		$('#loginAndLink').modal({ show:true });
	}
	
	function loginAndLinkProjectModal() {
		$('#loginAndLink .has-error').hide();
		$('#loginAndLink #linkType').val(3);
		
		$('#loginAndLink').modal({ show:true });
	}
	
	function selectLinkWithProjectOption(option) {
		$('#loginAndLink #linkOption').val(option);
		$('#loginAndLink #accept-button').text(option == 0 ? "Đăng nhập" : "Đăng ký");
	}
	
	function linkWithProject() {
		$('#loginAndLink .has-error').hide();
		
		var linkOption = $('#loginAndLink #linkOption').val();
		var linkType = $('#loginAndLink #linkType').val();
		
		var url;
		var data = {
			linkType: linkType
		};
		
		if (linkOption == 0) {
			// Login
			url = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/login';
			
			data.account = $('#loginAndLink #loginAccount').val();
			data.password = $('#loginAndLink #loginPassword').val();
		} else if (linkOption == 1) {
			// Register
			url = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/register';
			
			data.name = $('#loginAndLink #registerName').val();
			data.password = $('#loginAndLink #registerPassword').val();
			data.confirmPassword = $('#loginAndLink #registerConfirmPassword').val();
			data.email = $('#loginAndLink #registerEmail').val();
			data.mobilePhone = $('#loginAndLink #registerPhone').val();
			data.roles = [];
			
			var $selectedRoles = $('#loginAndLink input[name=registerRole]:checked');
			$selectedRoles.each(function(index, el) {
				data.roles.push(el.value);
			});
		}
		
		$.ajax({
			type: 'post',
			contentType: "application/json",
			url: url,
			data: JSON.stringify(data),
			dataType: "json",
			success: function ( html ) {
				$('#loginAndLink').modal('hide');
				
				if (linkType == 0) {
					// open search product form
					openProductModal();
				} else if (linkType == 1) {
					// open search land form
					openLandModal();
				} else if (linkType == 2) {
					window.location.href = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/view#addScientist';
					window.location.reload();
				} else if (linkType == 3) {
					// open search project form
					openProjectModal();
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				if (linkOption == 0) {
					$('#loginAndLink .login-error').show();
				} else if (linkOption == 1) {
					$('#loginAndLink .register-error').show();
					
					var error = XMLHttpRequest.responseJSON;
					if (error != null && error != undefined) {
						if (error.code == 'password') {
							$('#loginAndLink .register-password-error').show();
						} else if (error.code == 'email') {
							$('#loginAndLink .register-email-error').show();
						} else if (error.code == 'phone') {
							$('#loginAndLink .register-phone-error').show();
						}
					}
				}
		    }
		});
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
	
	// Search and link product
	function openProductModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/product?withoutLocation=false&notLinkedProjectId=${ dto.id }';
		
		$('#searchResource #resourceType').val(0);
        $('#searchResource .modal-title').text('Tìm và liên kết nông sản vào dự án');
        
		$('#searchResource .modal-body-content').load(url, function(){
	        $('#searchResource').modal({ show:true });
	        
	        initModalForm();
	    });
	}
	
	// Search and link land
	function openLandModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/land?withoutLocation=false&notLinkedProjectId=${ dto.id }';
		
		$('#searchResource #resourceType').val(1);
        $('#searchResource .modal-title').text('Tìm và liên kết thửa đất vào dự án');
        
		$('#searchResource .modal-body-content').load(url, function(){
			$('#searchResource').modal({ show:true });
	        
			initModalForm();
	    });
	}
	
	// Search and link scientist
	function openScientistModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/scientist?withoutLocation=false&notLinkedProjectId=${ dto.id }';
		
		$('#searchResource #resourceType').val(2);
        $('#searchResource .modal-title').text('Tìm và mời nhà khoa học');
        
		$('#searchResource .modal-body-content').load(url, function(){
	        $('#searchResource').modal({ show:true });
	        
	        initModalForm();
	    });
	}
	
	// Search and link project
	function openProjectModal() {
		var url = '${pageContext.servletContext.contextPath}/modal-search/project?withoutLocation=false';
		
		if ('${pageName}' == 'product') {
			url += '&requireProduct=Y';
		} else if ('${pageName}' == 'land') {
			url += '&requireLand=Y';
		} else if ('${pageName}' == 'scientist') {
			url += '&requireScientist=Y';
		}
		
		$('#searchResource #resourceType').val(3);
        $('#searchResource .modal-title').text('Tìm và liên kết với dự án');
        
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
		} else if (resourceType == 3) {
			url = '${pageContext.servletContext.contextPath}/modal-search/project';
		} else {
			return false;
		}
		
		var data = $('#searchResource .form-search').serialize();
		$.ajax({
			type: 'post',
			url: url,
			data: data,
			success: function ( html ) {  
				$('#searchResource .modal-body-content').html( html );
				
				initModalForm();
			}
		});
		
		return false;
	}
	
	function linkResources() {
		var resourceType = $('#searchResource #resourceType').val();
		var $selectedItems = $('#searchResource input[name=ids]:checked');
		
		var itemPrefix = '';
		if (resourceType == 0) {
			itemPrefix = '#productPanel #product_';
		} else if (resourceType == 1) {
			itemPrefix = '#landPanel #land_';
		} else if (resourceType == 2) {
			itemPrefix = '#scientistPanel #scientist_';
		} else if (resourceType == 3) {
			itemPrefix = '#projectPanel #project_';
		}
		
		var selectValid = false;
		
		var data = {
			type: resourceType,
			ids: []
		}
		
		$selectedItems.each(function(index, el) {
			selectValid = true;
			
			var itemId = el.value;
			var $existItem = $(itemPrefix + itemId);
			
			if ($existItem.length <= 0) {
				data.ids.push(itemId);
			}
		});
		
		if (data.ids.length > 0) {
			$('#searchResource').modal('hide');
			
			var url = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/link';
			
			$.ajax({
				type: 'post',
				contentType: 'application/json',
				url: url,
				data: JSON.stringify(data),
				dataType: 'json',
				success: function ( html ) {  
					var target = '';
					
					if (resourceType == 0) {
						target = 'addProduct';
					} else if (resourceType == 1) {
						target = 'addLand';
					} else if (resourceType == 2) {
						target = 'addScientist';
					} else if (resourceType == 3) {
						target = 'addProject';
					}
					
					window.location.href = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/view#' + target;
					window.location.reload();
				}
			});
		} else if (selectValid) {
			$('#searchResource').modal('hide');
		}
	}
	
	function approveLink(linkId, linkType, roleType) {
		// linkType: 0 = product, 1: land, 2: scientist
		// roleType: 0 = company (project owner), 1: resource owner (product, land, scientist)
		var approveUrl = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/link/' + linkId + '/approve';
		
		$('#confirmApproveLink form').attr('action', approveUrl);
		$('#confirmApproveLink').modal({ show:true });
	}
	
	function removeLink(linkId, linkType, roleType) {
		// linkType: 0 = product, 1: land, 2: scientist
		// roleType: 0 = company (project owner), 1: resource owner (product, land, scientist)
		var deleteUrl = '${pageContext.request.contextPath}/${pageName}/${ dto.id }/link/' + linkId + '/delete';
		
		$('#confirmDeleteLink form').attr('action', deleteUrl);
		$('#confirmDeleteLink').modal({ show:true });
	}
</script>