<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<div class="card-box padding-bottom-5" id="contactInfo">
	<div class="row content-panal-body card-block padding-top-none padding-bottom-none">
		<h4 class="col-xs-12 color-1ab394 padding-left-none padding-right-none margin-top-none margin-bottom-10">
			<span class="text-bold text-uppercase text-underline">${ contactGroupTitle }</span>
			
			<span class="pull-right">
			<a id="getContact" href="javascript:;" class="btn btn-sm btn-info" 
				title="Lấy thông tin từ tài khoản đăng ký" onclick="loadContactInfo()">
				<i class="fa fa-refresh"></i> Lấy thông tin đăng ký
			</a>
			</span>
		</h4>
		
		<div class="col-xs-12 padding-none">
			<div class="row">
				<div class="col-md-8 col-sm-8 col-xs-12">
					<div class="form-group">
						<label for="contactName" class="control-label">${ contactNameLabel } <span class="reqired-input">*</span></label>
						<form:input path="contactName" name="contactName" id="contactName" class="form-control" required="required" />
						<!-- oninvalid="this.setCustomValidity('Hãy nhập thông tin cho trường này')" oninput="setCustomValidity('')" -->
						<div class="has-error">
							<form:errors path="contactName" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-md-4 col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactAlias" class="control-label">${ contactAliasLabel }</label>
						<form:input path="contactAlias" name="contactAlias" id="contactAlias" class="form-control" />
						<div class="has-error">
							<form:errors path="contactAlias" class="help-inline" />
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactAddress" class="control-label">Địa chỉ (đường, tổ, thôn, xóm, khu phố, ...) <span class="reqired-input">*</span></label>
						<form:input path="contactAddress" name="contactAddress" id="contactAddress" class="form-control" 
							placeholder="Số nhà, đường, tổ, thôn, xóm, làng, khu phố, ..." required="required" />
						<div class="has-error">
							<form:errors path="contactAddress" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-sm-2 col-xs-12">
					<div class="form-group">
						<label for="contactProvince" class="control-label">Tỉnh / thành <span class="reqired-input">*</span></label>
						<form:select path="contactProvinceId" id="contactProvince" name="contactProvince" class="selectpicker" 
							data-live-search="true" required="required" 
							onchange="selectProvince('contactProvince', 'contactDistrict', 'contactLocation', null, null, true)">
							<form:options items="${ contactProvinces }"></form:options>
						</form:select>
						<div class="has-error">
							<form:errors path="contactProvinceId" class="help-inline" />
						</div>
					</div>
				</div>
				
				<div class="col-sm-3 col-xs-12">
					<div class="form-group">
						<label for="contactDistrict" class="control-label">Quận / huyện <span class="reqired-input">*</span></label>
						<form:select path="contactDistrictId" id="contactDistrict" name="contactDistrict" class="selectpicker" 
							data-live-search="true" required="required" 
							onchange="selectDistrict('contactDistrict', 'contactLocation', null, null, true)">
							<form:options items="${ contactDistricts }"></form:options>
						</form:select>
						<div class="has-error">
							<form:errors path="contactDistrictId" class="help-inline" />
						</div>
					</div>
				</div>
				
				<div class="col-sm-3 col-xs-12">
					<div class="form-group">
						<label for="contactLocation" class="control-label">Xã / phường / thị trấn <span class="reqired-input">*</span></label>
						<form:select path="contactLocationId" id="contactLocation" name="contactLocation" class="selectpicker" 
							data-live-search="true" required="required" 
							onchange="selectLocation('contactLocation', true)">
							<form:options items="${ contactLocations }"></form:options>
						</form:select>
						<div class="has-error">
							<form:errors path="contactLocationId" class="help-inline" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactPhone" class="control-label">Điện thoại</label>
						<form:input path="contactPhone" name="contactPhone" id="contactPhone" class="form-control" placeholder="Điện thoại công ty, tổ chức" />
						<div class="has-error">
							<form:errors path="contactPhone" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-lg-2 col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactMobile" class="control-label">Di động <span class="reqired-input">*</span></label>
						<form:input path="contactMobile" name="contactMobile" id="contactMobile" class="form-control" 
							placeholder="Điện thoại di động" required="required" />
						<div class="has-error">
							<form:errors path="contactMobile" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-lg-2 col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactHomePhone" class="control-label">Điện thoại nhà</label>
						<form:input path="contactHomePhone" name="contactHomePhone" id="contactHomePhone" class="form-control" placeholder="Điện thoại nhà" />
						<div class="has-error">
							<form:errors path="contactHomePhone" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-lg-2 col-sm-4 col-xs-12">
					<div class="form-group">
						<label for="contactFax" class="control-label">Số fax</label>
						<form:input path="contactFax" name="contactFax" id="contactFax" class="form-control" placeholder="Số fax" />
						<div class="has-error">
							<form:errors path="contactFax" class="help-inline" />
						</div>
					</div>
				</div>
				<div class="col-lg-4 col-sm-8 col-xs-12">
					<div class="form-group">
						<label for="contactEmail" class="control-label">Địa chỉ email <span class="reqired-input">*</span></label>
						<form:input path="contactEmail" name="contactEmail" id="contactEmail" class="form-control" 
							placeholder="Địa chỉ email" required="required" />
						<div class="has-error">
							<form:errors path="contactEmail" class="help-inline" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	function loadContactInfo() {
		var url = '${pageContext.servletContext.contextPath}/contact';
		
		$.ajax({
			type: 'get',
			url: url,
			success: function (data) {  
				if (data != null && data != undefined) {
					$('#contactInfo #contactName').val(data.name);
					$('#contactInfo #contactAlias').val(data.alias);
					$('#contactInfo #contactPhone').val(data.companyPhone);
					$('#contactInfo #contactMobile').val(data.mobilePhone);
					$('#contactInfo #contactHomePhone').val(data.homePhone);
					$('#contactInfo #contactFax').val(data.fax);
					$('#contactInfo #contactEmail').val(data.email);
					
					$('#contactInfo #contactAddress').val(data.address);
					
					var location = data.location;
					if (location) {
						var district = location.district;
						if (district) {
							var province = district.province;
							if (province) {
								selectProvince('contactProvince', 'contactDistrict', 'contactLocation', 
										null, null, true, 
										province.id, district.id, location.id);
							}
						}
					}
				}
			}
		});
	}
</script>