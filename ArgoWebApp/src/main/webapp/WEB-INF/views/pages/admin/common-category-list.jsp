<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../layout/taglib.jsp"%>

<style type="text/css">
	.row-disactive span {
		color: #ff0000;
		text-decoration: line-through;
	}
	
	.row-disactive span i.fa {
		color: #ff0000;
	}
</style>

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
				<div class="box">
					<div class="row box-header with-border">
						<form:form name="searchForm" action="${pageContext.request.contextPath}/manager/common-category/search" method="GET"
								modelAttribute="searchDto" class="form-horizontal">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="input-group">
									<span class="input-group-addon text-left" id="type-addon" style="text-align: left;">Danh mục</span>
									<form:select path="type" id="typeValue" class="selectpicker" aria-describedby="type-addon"
										onchange="$(this).closest('form').trigger('submit');">
										<form:options items="${typeDtos}"></form:options>
									</form:select>
								</div>
							</div>
							<div class="col-md-8 col-sm-8 col-xs-12">
								<div class="input-group">
									<form:input path="text" class="form-control"
										data-placeholder="Mã, tên danh mục" id="search" name="search" />
									<span class="input-group-btn">
										<button type="submit" class="btn btn-info btn-flat">
											<i class="fa fa-search"></i> Tìm kiếm
										</button>
										
										<a href="${pageContext.request.contextPath}/manager/common-category/create" 
											type="button" class="btn btn-success btn-flat">
											<i class="fa fa-plus"></i> Thêm mới
										</a>
									</span>
									
								</div>
							</div>
						</form:form>
					</div>

					<!-- /.box-header -->
					<div class="box-body">
						<table class="table table-bordered" id="dataTable">
							<tr>
								<th style="width: 20px" class="text-center">#</th>
								<th width="20%">Mã danh mục</th>
								<th width="45%">Tên hiển thị</th>
								<th width="15%" class="text-center">Thứ tự</th>
								<th width="15%" class="text-center">Ngầm định</th>
								<th width="1%" class="text-center">Chức năng</th>
							</tr>

							<c:forEach items="${dtos}" var="dto" varStatus="loop">
								<tr <c:if test="${ !dto.active }">class="row-disactive"</c:if>>
									<td class="text-center"><span>${itemStartIndex + loop.index + 1}</span></td>
									<td><span>${dto.code}</span></td>
									<td><span>${dto.name}</span></td>
									<td class="text-center"><span>${dto.index}</span></td>
									<td class="text-center">
										<span>
											<c:choose>
												<c:when test="${ dto.main }">
													<i class="fa fa-check-square-o"></i>
												</c:when>
												<c:otherwise>
													<i class="fa fa-square-o"></i>
												</c:otherwise>
											</c:choose>
										</span>
									</td>
									<td nowrap>
										<a href="${pageContext.request.contextPath}/manager/common-category/${dto.id}/edit"
											type="button" class="btn btn-info btn-flat" title="Chỉnh sửa dữ liệu">
											<i class="fa fa-edit"></i>
										</a>
										
										<c:choose>
											<c:when test="${ dto.active }">
												<a href="javascript:;" type="button" class="btn btn-danger btn-flat" 
													onclick="openDeleteModal('${dto.id}')" title="Xóa dữ liệu">
													<i class="fa fa-remove"></i>
												</a>
											</c:when>
											<c:otherwise>
												<a href="javascript:;" type="button" class="btn btn-primary btn-flat" 
													onclick="openActiveModal('${dto.id}')" title="Khôi phục dữ liệu">
													<i class="fa fa-check"></i>
												</a>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>

					<!-- /.box-body -->
					<div class="box-footer clearfix">
						<ul class="pagination pagination-sm no-margin pull-right" id="paginationItem">
							<li class="paging-item">
								<a href="${pageContext.request.contextPath}/manager/common-category-page?page=1">&laquo;</a>
							</li>
							
							<c:forEach var = "i" begin = "1" end = "${pageCount}">
								<li class="paging-item">
									<a href="${pageContext.request.contextPath}/manager/common-category-page?page=${i}">${i}</a>
								</li>
							</c:forEach
							>
							<li class="paging-item">
								<a href="${pageContext.request.contextPath}/manager/common-category-page?page=${pageCount}">&raquo;</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>

<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" 
	aria-labelledby="deleteModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/manager/common-category/delete" method="POST">
	            <div class="modal-header">
					<h2>Xác nhận xóa dữ liệu</h2>
	            </div>
	            <div class="modal-body">
	            	<strong>Bạn có thực sự muốn xóa dữ liệu này?</strong>
	            	
	            	<br/><br/>
	            	<strong>Lưu ý:</strong> Dữ liệu bạn xóa sẽ chuyển sang trạng thái không kích hoạt và không được sử dụng nữa.
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-danger">Đồng ý xóa dữ liệu</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="confirm-active" tabindex="-1" role="dialog" 
	aria-labelledby="activeModalLabel" aria-hidden="true" data-backdrop="false">
    <div class="modal-dialog">
        <div class="modal-content">
        	<form action="${pageContext.request.contextPath}/manager/common-category/active" method="POST">
	            <div class="modal-header">
					<h2>Xác nhận kích hoạt dữ liệu</h2>
	            </div>
	            <div class="modal-body">
	            	<strong>Bạn có thực sự muốn kích hoạt dữ liệu này?</strong>
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">Hủy bỏ</button>
	                <button type="submit" class="btn btn-primary">Đồng ý kích hoạt dữ liệu</button>
	            </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
	function openActiveModal(id) {
		var url = '${pageContext.request.contextPath}/manager/common-category/' + id + '/active';
		
		$('#confirm-active form').attr('action', url);
		$('#confirm-active').modal({ show: true });
	}

	function openDeleteModal(id) {
		var url = '${pageContext.request.contextPath}/manager/common-category/' + id + '/delete';
		
		$('#confirm-delete form').attr('action', url);
		$('#confirm-delete').modal({ show: true });
	}
</script>