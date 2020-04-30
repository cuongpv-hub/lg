<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<c:if test="${ dto.posterImage != null && dto.posterImage != '' }">
	<style type="text/css">
		.profile-bg-picture {
			background-image: url('${pageContext.request.contextPath}/upload/product/${ dto.posterImage }') !important;
			/*height: 65vh !important;*/
			height: 45vh !important;
		}
	</style>
</c:if>

<header class="profile-bg-picture"></header>
<section class="user-section">
	<div class="container">
		<div class="profile-user-box">
			<div class="row">
				<div class="col-sm-9">
					<c:choose>
	 					<c:when test="${ dto.mainImage != null && dto.mainImage != '' }">
	 						<span class="pull-left m-r-15">
								<img src="${pageContext.request.contextPath}/upload/land/${ dto.mainImage }" 
									alt="${ dto.contact.name }" class="thumb-lg img-circle with-error-src" 
									<c:if test="${ dto.authorAvatar != null && dto.authorAvatar != '' }">
										errorSrc="${pageContext.request.contextPath}/upload/avatar/${ dto.authorAvatar }"
									</c:if>
								>
							</span>
	 					</c:when>
	 					<c:when test="${ dto.authorAvatar != null && dto.authorAvatar != '' }">
	 						<span class="pull-left m-r-15">
								<img src="${pageContext.request.contextPath}/upload/avatar/${ dto.authorAvatar }" 
									alt="${ dto.contact.name }" class="thumb-lg img-circle">
							</span>
	 					</c:when>
	 				</c:choose>
					<div class="media-body">
						<h4 class="m-t-5 m-b-5 ellipsis">${ dto.name }</h4> 
						<p class="font-13">${ dto.contact.name }</p> 
						<p class="text-muted m-b-0"><strong>Địa chỉ:</strong> ${ dto.fullAddress }</p>
						<c:if test="${ currentUser != null }">
							<c:if test="${ dto.owner || dto.canApprove }">
								<p class="text-muted m-b-0">
									<strong>Trạng thái:</strong> 
									<c:choose>
	 									<c:when test="${ dto.status.status == 0 }">
	 										<span class="text-italic"><spring:message code="status.label.draft"></spring:message></span>
	 									</c:when>
	 									<c:when test="${ dto.status.status == 2 }">
	 										<span><spring:message code="status.label.processing"></spring:message></span>
	 									</c:when>
	 									<c:when test="${ dto.status.status == 1 }">
	 										<span class="text-bold"><spring:message code="status.label.approved"></spring:message></span>
	 										<c:if test="${ dto.status.comment != null && dto.status.comment != '' }">
	 											<i class="fa fa-info-circle" title="${ dto.status.comment }"></i>
	 										</c:if>
	 									</c:when>
	 									<c:when test="${ dto.status.status == -1 }">
	 										<span class="text-strike text-danger"><spring:message code="status.label.rejected"></spring:message></span>
	 										<c:if test="${ dto.status.comment != null && dto.status.comment != '' }">
	 											<i class="fa fa-info-circle" title="${ dto.status.comment }"></i>
	 										</c:if>
	 									</c:when>
	 								</c:choose>
								</p>
							</c:if>
						</c:if>
					</div>
				</div>
				<div class="col-sm-3 text-right">
					<c:if test="${ currentUser != null }">
						<!-- 
						<div class="row">
							<div class="col-xs-12">
								<a class="btn btn-success waves-effect waves-light" href="${pageContext.request.contextPath}/product/${ dto.id }/link">
									<i class="fa fa-user m-r-5"></i> Liên kết
								</a>
							</div>
						</div>
						-->
						
						<c:if test="${ dto.owner && dto.status.status != 1 }">
							<div class="row padding-top-10">
								<div class="col-xs-12">
									<a class="btn btn-primary" href="${pageContext.request.contextPath}/product/${ dto.id }/edit">
										<i class="fa fa-edit m-r-5"></i> Chỉnh sửa
									</a>
								</div>
							</div>
						</c:if>
						
						<c:if test="${ dto.canApprove }">
							<div class="row padding-top-10">
								<div class="col-xs-12">
									<button class="btn btn-success" data-toggle="modal" data-target="#confirm-approve">
										<i class="fa fa-check m-r-5"></i>
										<c:choose>
											<c:when test="${ dto.owner }">
												Phê duyệt
											</c:when>
											<c:otherwise>
												Đồng ý phê duyệt
											</c:otherwise>
										</c:choose>
									</button>
								</div>
							</div>
							
							<c:if test="${ !dto.owner }">
								<div class="row padding-top-10">
									<div class="col-xs-12">
										<button class="btn btn-danger" data-toggle="modal" data-target="#confirm-reject">
											<i class="fa fa-times m-r-5"></i> Không đồng ý phê duyệt
										</button>
									</div>
								</div>
							</c:if>
						</c:if>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</section>

<section class="posts-2">
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-sm-12 ">
				<div class="card-box">
					<div class="row content-panal-body card-block">
						<h4 class="col-md-12 text-bold color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">
							Mô tả nông sản
						</h4>
						<div class="col-md-7 col-sm-7 col-xs-12 text-justify l-height-text content-text">
							${ dto.html }
							<div class="text-center"></div>
						</div>
						
						<div class="offset-md-1 offset-sm-1 col-md-4 col-sm-4 col-xs-12 tab-sidebar text-inline">
							<div class="mobile-box">
								<div class="box-summary link-list">
									<div class="row summary-item">
										<div class="col-xs-12 summary-content">
											<i aria-hidden="true" class="fa fa-user"></i>
											<span class="content-label">&nbsp;<strong>Nhà nông:</strong></span>
											<span class="content">${ dto.contact.name }</span>
										</div>
									</div>
									<div class="row summary-item">
										<div class="col-xs-12 summary-content">
											<i aria-hidden="true" class="fa fa-legal"></i>
											<span class="content-label">&nbsp;<strong>Đơn vị cấp phép:</strong></span>
											<span class="content">${ dto.approveName }</span>
										</div>
									</div>
									
									<c:if test="${ dto.square != null }">
										<div class="row summary-item">
											<div class="col-xs-12 summary-content">
												<i aria-hidden="true" class="fa fa-globe"></i>
												<span class="content-label">&nbsp;<strong>Diện tích khoảng:</strong></span>
												<span class="content">
													<span id="square" class="text-number">${ dto.square }</span>
													<c:if test="${ dto.squareUnit != null }">${ dto.squareUnit.name }</c:if>
												</span>
											</div>
										</div>
									</c:if>
									
									<c:if test="${ dto.volume != null }">
										<div class="row summary-item">
											<div class="col-xs-12 summary-content">
												<i aria-hidden="true" class="fa fa-balance-scale"></i>
												<span class="content-label">&nbsp;<strong>Sản lượng mỗi vụ (năm):</strong></span>
												<span class="content">
													<span id="volume" class="text-number">${ dto.volume }</span>
													<c:if test="${ dto.volumeUnit != null }">${ dto.volumeUnit.name }</c:if>
												</span>
											</div>
										</div>
									</c:if>
									
									<c:if test="${ dto.moneyFrom != null || dto.moneyTo != null }">
										<div class="row summary-item">
											<div class="col-xs-12 summary-content">
												<i aria-hidden="true" class="fa fa-money"></i>
												<span class="content-label">&nbsp;<strong>Giá giao động từ:</strong></span>
												<span class="content">
													<span id="moneyFrom" class="text-number">${ dto.moneyFrom }</span>
												</span>
												<span class="content-label"><strong>đến:</strong></span>
												<span class="content">
													<span id="moneyTo" class="text-number">${ dto.moneyTo }</span>
													<c:if test="${ dto.moneyUnit != null }">${ dto.moneyUnit.name }</c:if>
												</span>
											</div>
										</div>
									</c:if>
									
									<c:if test="${ dto.startDate != null || dto.endDate != null }">
										<div class="row summary-item">
											<div class="col-xs-12 summary-content">
												<i aria-hidden="true" class="fa fa-calendar"></i>
												<span class="content-label">&nbsp;<strong>Thời gian dự kiến:</strong></span>
												<span class="content">${ dto.startDate } - ${ dto.endDate }</span>
											</div>
										</div>
									</c:if>
									
									<div class="row summary-item">
										<div class="col-xs-12 summary-content">
											<i aria-hidden="true" class="fa fa-tags"></i>
											<!-- <span class="content-label">&nbsp;<strong>Loại nông sản:</strong></span> -->
											<span class="content">
												<c:forEach items="${dto.categories}" var="category" varStatus="loop">
													<a class="tag-item small" href="${pageContext.request.contextPath}/product?categoryId=${ category.id }" title="${ category.name }">
														${ category.name }
													</a>
												</c:forEach>
											</span>
										</div>
									</div>
									
									<c:choose>
										<c:when test="${ dto.formCooperations != null && !dto.formCooperations.isEmpty() }">
											<div class="row summary-item">
												<div class="col-xs-12 summary-content">
													<i aria-hidden="true" class="fa fa-handshake-o"></i>
													<span class="content-label">&nbsp;<strong>Hình thức hợp tác:</strong></span>
													<span class="content">
														<c:forEach items="${dto.formCooperations}" var="category" varStatus="loop">
															<a class="tag-item small" href="javascript:;" title="${ category.name }">
																${ category.name }
															</a>
														</c:forEach>
														<c:if test="${ dto.formCooporateDiff != null && dto.formCooporateDiff != '' }">
															<a class="tag-item small" href="javascript:;" title="${ dto.formCooporateDiff }">
																${ dto.formCooporateDiff }
															</a>
														</c:if>
													</span>
												</div>
											</div>
										</c:when>
										<c:when test="${ dto.formCooporateDiff != null && dto.formCooporateDiff != '' }">
											<div class="row summary-item">
												<div class="col-xs-12 summary-content">
													<i aria-hidden="true" class="fa fa-handshake-o"></i>
													<span class="content-label">&nbsp;<strong>Hình thức hợp tác:</strong></span>
													<span class="content">
														<c:if test="${ dto.formCooporateDiff != null && dto.formCooporateDiff != '' }">
															<a class="tag-item small" href="javascript:;" title="${ dto.formCooporateDiff }">
																${ dto.formCooporateDiff }
															</a>
														</c:if>
													</span>
												</div>
											</div>
										</c:when>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- Linked projects -->
            <div class="col-md-12 col-sm-12 margin-top-10">
                <div class="card-box">
                    <div class="row content-panal-body card-block following table-row">
                        <div class="col-md-12 farmer-register">
                            <h4 class="color-1ab394 text-bold margin-top-15 margin-bottom-10" style="text-transform: uppercase;">
                            	Danh sách các dự án tham gia bao tiêu&nbsp;&nbsp;&nbsp;&nbsp;
                            	
                            	<c:choose>
								 	<c:when test="${ dto.owner }">
								 		<a id="addProject" href="javascript:;" class="btn btn-sm btn-success" 
											title="Tìm và thêm dự án liên kết" onclick="openProjectModal()">
											<i class="fa fa-anchor"></i> Thêm dự án liên kết
										</a>
									</c:when>
									<c:when test="${ currentUser != null && currentUser.isCompany() }">
										<a id="addProject" href="javascript:;" class="btn btn-sm btn-success" 
											title="Đăng ký bao tiêu sản phẩm" onclick="openProjectModal()">
											<i class="fa fa-handshake-o"></i> Đăng ký bao tiêu sản phẩm
										</a>
									</c:when>
									<c:when test="${ currentUser == null }">
										<a id="addProject" href="javascript:;" class="btn btn-sm btn-success" 
											title="Đăng ký bao tiêu sản phẩm" onclick="loginAndLinkProjectModal()">
											<i class="fa fa-handshake-o"></i> Đăng ký bao tiêu sản phẩm
										</a>
									</c:when>
								</c:choose>
                            </h4>
                        </div>
                        <c:choose>
		 					<c:when test="${ dto.projectDtos == null || dto.projectDtos.isEmpty() }">
		 						<div class="col-md-12">
		 							<div class="text-center">Chưa có cá nhân hoặc tổ chức đăng ký tham gia bao tiêu</div>
		 						</div>
		 					</c:when>
		 					<c:otherwise>
		 						<c:forEach items="${ dto.projectDtos }" var="project" varStatus="loop">
									<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12" id="project_${ project.id }">
										<div class="card-box table-cell-content">
											<div class="item-farmer hover padding-none margin-none">
												<a href="${pageContext.request.contextPath}/project/${ project.id }/view" title="${ project.name }" target="_blank">
													<c:choose>
								 						<c:when test="${ project.mainImage == null || project.mainImage == '' }">
								 							<img src="${pageContext.request.contextPath}/upload/project/empty-image.png" 
																alt="${ project.name }" class="img-fluid rounded-circle-xyz">
								 						</c:when>
								 						<c:otherwise>
								 							<img src="${pageContext.request.contextPath}/upload/project/${ project.mainImage }" 
																alt="${ project.name }" class="img-fluid rounded-circle-xyz with-error-src"
																errorSrc="${pageContext.request.contextPath}/upload/project/empty-image.png">
								 						</c:otherwise>
								 					</c:choose>
												</a>
												<div class="name-of-farmer text-muted font-13">
													<c:set var="projectItemDto" value="${project}" scope="request" />
													<jsp:include page="project-resource-detail.jsp"></jsp:include>
													
													<%-- <a href="${pageContext.request.contextPath}/project/${ project.id }/view" title="${ project.name }" target="_blank">
														${ project.name }
													</a>
													<br />
													<div class="float-left full-width name_owner font-size-14 padding-top-5">
														<div class="summary-content">
				                                        	<i aria-hidden="true" class="fa fa-tags"></i>
				                                            <span class="content">
				                                            	<c:forEach items="${ project.categories }" var="categoryDto" varStatus="categoryLoop">
																	<span class="tag-item small">${ categoryDto.name }</span>
																</c:forEach>
				                                            </span>
				                                        </div>
													</div> --%>
												</div>
											</div>
											<div class="card-box-footer">
												<div class="inline" title="Trạng thái liên kết">
													<i class="fa fa-handshake-o"></i>
													<c:choose>
									 					<c:when test="${ project.status == null || project.status.status == 0 }">
									 						<i class="text-italic">Chưa xác nhận liên kết</i>
									 					</c:when>
									 					<c:when test="${ project.status.status == 1 }">
									 						<strong class="text-bold">Đã xác nhận liên kết</strong>
									 					</c:when>
									 					<c:when test="${ project.status.status == -1 }">
									 						<i class="text-italic text-strike text-danger">Đã từ chối liên kết</i>
									 					</c:when>
									 					<c:when test="${ project.status.status == 2 }">
									 						<i class="text-italic text-strike">Đã liên kết với dự án khác</i>
									 					</c:when>
									 				</c:choose>
												</div>
												
									 			<c:if test="${ currentUser != null }">
									 				<c:choose>
									 					<c:when test="${ dto.owner }">
									 						<c:if test="${ project.status == null || project.status.status == 0 }">
																<button type="button" class="btn btn-success btn-block" onclick="approveLink('${ project.linkId }', 0, 0)">
																	<i class="fa fa-check"></i> Dự án xác nhận liên kết
																</button>
															</c:if>
															
															<button type="button" class="btn btn-danger btn-block" onclick="removeLink('${ project.linkId }', 0, 0)">
																<i class="fa fa-times"></i> Bỏ liên kết dự án
															</button>
														</c:when>
														<c:when test="${ project.owner }">
															<c:if test="${ project.status == null || project.status.status == 0 }">
																<button type="button" class="btn btn-success btn-block" onclick="approveLink('${ project.linkId }', 0, 1)">
																	<i class="fa fa-check"></i> Xác nhận liên kết nông sản
																</button>
															</c:if>
															
															<button type="button" class="btn btn-danger btn-block" onclick="removeLink('${ project.linkId }', 0, 1)">
																<i class="fa fa-times"></i> Từ chối liên kết nông sản
															</button>
														</c:when>
													</c:choose>
												</c:if>
											</div>
										</div>
									</div>
								</c:forEach>
		 					</c:otherwise>
		 				</c:choose>
                    </div>
                </div>
            </div>
            
			<!-- Product images -->
			<div class="col-md-12 col-sm-12 margin-top-10">
				<div class="card-box">
					<div class="row content-panal-body table-row">
						<h4 class="col-md-12 text-bold color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">
							Hình ảnh nông sản
						</h4>
						
						<c:forEach items="${dto.images}" var="image" varStatus="loop">
							<div class="col-sm-3">
								<div class="blog-row blog-compact blog-compact-sm table-cell-content">
									<div class="blog-col" style="height: 100%">
										<div class="blog-relative blog-hover blog-hover-scale" style="height: 100%">
											<div class="blog-image blog-vgradient-black-lg" style="height: 100%">
												<div class="overlay" style="height: 100%">
													<img src="${pageContext.request.contextPath}/upload/product/${ image }" 
														alt="Ảnh dự án" width="100%" height="500" class="img-fluid blog-img">
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			
			<!-- Product comment -->
			<div class="col-md-12 col-sm-12 margin-top-10">
                <div class="card-box">
                    <div class="row card-block content-panal-body">
                        <h4 class="col-md-12 text-bold color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">Bình luận</h4>
                        <div style="min-width: 90%;">
                        	<!--
                            <ul id="comments-block" class="comments-list">
                                <li>
                                    <div id="comment-99" class="comment-main-level">
                                        <div class="comment-avatar"><img src="${pageContext.request.contextPath}/resources/images/1.jpg" alt=""></div>
                                        <div class="comment-box">
                                            <div class="comment-head">
                                                <h6 class="comment-name color-03658c">Tạ Thanh Bảo</h6> <span>9 tháng trước</span>
                                            </div>
                                            <div class="comment-content">
                                                Nhà nước nên làm và điều chỉnh nhanh kịp thời không thì không phải con số này mà còn nhiều con số này nữa
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                            <div class="text-center">
                                <ul data-v-82963a40="" class="pagination">
                                    <li data-v-82963a40="" class="prev-item disabled"><a data-v-82963a40="" tabindex="0" class="prev-link-item">Trước</a></li>
                                    <li data-v-82963a40="" class="page-item active"><a data-v-82963a40="" tabindex="0" class="page-link-item">1</a></li>
                                    <li data-v-82963a40="" class="next-item disabled"><a data-v-82963a40="" tabindex="0" class="next-link-item">Sau</a></li>
                                </ul>
                            </div>
                            -->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="link-resource-detail.jsp"></jsp:include>

<script type="text/javascript">
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
	});
</script>