<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../layout/taglib.jsp"%>

<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			${formTitle} <small>${formTitle}</small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i>Quản trị hệ thống</a></li>
			<li><a href="#">Danh mục</a></li>
			<li class="active">Quốc gia</li>
		</ol>
	</section>

	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<div class="box">
					<div class="row box-header with-border">
						<div class="col-md-8 col-xs-12">
							<form:form action="country" method="GET"
								modelAttribute="searchDto" class="form-horizontal">
								<div class="input-group input-group-sm">
									<form:input path="text" class="form-control" data-placeholder="Tìm kiếm quốc gia" />
									<span class="input-group-btn">
										<button type="submit" class="btn btn-info btn-flat">
											<i class="fa fa-search"></i>
										</button>
									</span>
								</div>
							</form:form>
						</div>
						<div class="col-md-4 col-xs-12">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/country/create" type="button" class="btn btn-info btn-flat">
									<i class="fa fa-plus"></i> Thêm mới
								</a>
							</span>
						</div>
					</div>

					<!-- /.box-header -->
					<div class="box-body">
						<table class="table table-bordered">
							<tr>
								<th style="width: 10px">STT</th>
								<th width="35%">Mã quốc gia</th>
								<th width="64%">Tên quốc gia</th>
								<th width="1%">#</th>
							</tr>

							<c:forEach items="${dtos}" var="dto" varStatus="loop">
								<tr>
									<td>${ (loop.index + 1) }</td>
									<td>${dto.code}</td>
									<td>${dto.name}</td>
									<td nowrap>
										<a href="${pageContext.request.contextPath}/country/edit?id=${dto.id}" type="button" class="btn btn-info btn-flat">
											<i class="fa fa-edit"></i>
										</a>
										<a href="${pageContext.request.contextPath}/country/delete?id=${dto.id}" type="button" 
											class="btn btn-danger btn-flat delete-confirmation">
											<i class="fa fa-remove"></i>
										</a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>

					<!-- /.box-body -->
					<div class="box-footer clearfix">
						<ul class="pagination pagination-sm no-margin pull-right">
							<li><a href="#">&laquo;</a></li>
							<li><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">&raquo;</a></li>
						</ul>
					</div>

					<!--<c:if test="${not empty message}">
						<div class="form-message">${message}</div>
					</c:if>-->
				</div>
			</div>
		</div>
	</section>
</div>