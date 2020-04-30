<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../layout/taglib.jsp"%>

<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			${formTitle} <small>${formTitle} </small>
		</h1>
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i> Dashboard</a></li>
			<li><a href="#">Category</a></li>
			<li class="active">Device</li>
		</ol>
	</section>

	<!-- Main content -->
	<section class="content">
		<div class="row">
			<div class="col-md-12">
				<!-- Horizontal Form -->
				<div class="box box-info">
					<div class="box-header with-border">
						<h3 class="box-title">Edit Form</h3>
					</div>
					<!-- /.box-header -->
					<!-- form start -->
					<form:form action="save" method="POST" modelAttribute="dto"
						class="form-horizontal">

						<form:hidden path="id" />

						<div class="box-body">
							<div class="form-group">
								<label for="code" class="col-sm-2 control-label">Code</label>
								<div class="col-sm-10">
									<form:input path="code" name="code" id="code" class="form-control" />
									<div class="has-error">
										<form:errors path="code" class="help-inline" />
									</div>
								</div>
							</div>

							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">Name</label>
								<div class="col-sm-10">
									<form:input path="name" name="name" id="name" class="form-control" />
									<div class="has-error">
										<form:errors path="name" class="help-inline" />
									</div>
								</div>
							</div>
						</div>

						<!-- /.box-body -->
						<div class="box-footer">
							<span class="pull-right">
								<a href="${pageContext.request.contextPath}/country" type="button" class="btn btn-default">
									<i class="fa fa-arrow-left"></i> Cancel
								</a>
								<button type="submit" class="btn btn-info">
									<i class="fa fa-save"></i> Save
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