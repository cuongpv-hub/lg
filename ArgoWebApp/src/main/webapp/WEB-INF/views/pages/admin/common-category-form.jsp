<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../layout/taglib.jsp"%>

<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			${formTitle} <small>${formTitle}</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="${pageContext.request.contextPath}/home">
				<i class="fa fa-dashboard"></i>
				Trang chủ
			</a></li>
			<li><a href="${pageContext.request.contextPath}/manager">
				Danh mục
			</a></li>
			<li class="active">
				Danh mục dùng chung
			</li>
		</ol>
	</section>

	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<!-- Horizontal Form -->
				<div class="box box-info">
					<div class="box-header with-border">
						<h3 class="box-title">
							<c:choose>
								<c:when test="${ dto.id == null || dto.id == '' }">
									Thêm mới ${ dto.typeName }
								</c:when>
								<c:otherwise>
									Chỉnh sửa ${ dto.typeName }
								</c:otherwise>
							</c:choose>
						</h3>
					</div>
					<!-- /.box-header -->
					
					<!-- form start -->
					<form:form action="save" method="POST" modelAttribute="dto" class="form-horizontal">

						<form:hidden path="id" />
						<form:hidden path="type" />
						
						<div class="box-body">
							<div class="form-group">
								<label for="code" class="col-sm-2 control-label">Mã danh mục</label>
								<div class="col-sm-10">
									<form:input path="code" name="code" id="code" class="form-control" />
									<div class="has-error">
										<form:errors path="code" class="help-inline" />
									</div>
								</div>
							</div>

							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">Tên danh mục</label>
								<div class="col-sm-10">
									<form:input path="name" name="name" id="name" class="form-control" />
									<div class="has-error">
										<form:errors path="name" class="help-inline" />
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">Số thứ tự</label>
								<div class="col-sm-4">
									<form:input path="index" name="index" id="index" class="form-control" />
									<div class="has-error">
										<form:errors path="index" class="help-inline" />
									</div>
								</div>
								
								<div class="col-sm-6 btn-touch btn-group btn-group-vertical" data-toggle="buttons">
									<label class="btn <c:if test="${ dto.main }">active</c:if>" for="requireProduct">
										<input type="checkbox" id="main" name="main" <c:if test="${ dto.main }">checked</c:if> 
											value="true" style="display: none;" />
										<i class="fa fa-square-o fa-2x"></i>
										<i class="fa fa-check-square-o fa-2x"></i>
										<span class="control-label">Danh mục ngầm định trong nhóm</span>
									</label>
								</div>
							</div>
							
							<c:if test="${not empty error}">
								<div class="help-inline">$error}</div>
							</c:if>
						</div>

						<!-- /.box-body -->
						<div class="box-footer">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/manager/common-category-page?page=${ currentPage }" type="button" class="btn btn-default">
									<i class="fa fa-arrow-left"></i> Hủy bỏ
								</a>
								<button type="submit" class="btn btn-info">
									<i class="fa fa-save"></i> Lưu thay đổi
								</button>
							</span>
						</div>
						<!-- /.box-footer -->
					</form:form>
				</div>
			</div>
		</div>
		<!-- /.row -->
	</section>
	<!-- /.content -->
</div>

<script type="text/javascript">
	$(function() {
		var numberOption = {
			    currencySymbol : '',
			    decimalCharacter : ',',
			    digitGroupSeparator : '.',
			    decimalPlacesRawValue: 3,
			    allowDecimalPadding: false
			};
		
		new AutoNumeric('#index', numberOption);
	});
</script>