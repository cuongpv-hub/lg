<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<c:if test="${ dto.posterImage != null && dto.posterImage != '' }">
	<style type="text/css">
		.profile-bg-picture {
			background-image: url('${pageContext.request.contextPath}/upload/article/${ dto.posterImage }') !important;
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
					<c:if test="${ dto.authorAvatar != null && dto.authorAvatar != '' }">
						<span class="pull-left m-r-15">
							<img src="${pageContext.request.contextPath}/upload/avatar/${ dto.authorAvatar }" 
								alt="${ dto.author.name }" class="thumb-lg img-circle">
						</span>
					</c:if> 
					<div class="media-body">
						<h4 class="m-t-5 m-b-5 ellipsis">${ dto.name }</h4> 
						<p class="font-13">${ dto.author.name }</p> 
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
						<c:if test="${ dto.owner && dto.status.status != 1 }">
							<div class="row padding-top-10">
								<div class="col-xs-12">
									<a class="btn btn-primary" href="${pageContext.request.contextPath}/article/${ dto.id }/edit">
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
                        <h4 class="col-md-12 text-bold color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">Bài viết</h4>
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
                                            <span class="content-label">&nbsp;<strong>Tác giả:</strong></span>
                                            <span class="content">${ dto.author.name }</span>
                                        </div>
                                    </div>
                                    <div class="row summary-item">
                                        <div class="col-xs-12 summary-content">
                                        	<i aria-hidden="true" class="fa fa-legal"></i>
                                        	<span class="content-label">&nbsp;<strong>Đơn vị duyệt:</strong></span>
                                            <span class="content">${ dto.approveName }</span>
                                        </div>
                                    </div>
                                    <div class="row summary-item">
                                        <div class="col-xs-12 summary-content">
                                        	<i aria-hidden="true" class="fa fa-briefcase"></i>
                                            <span class="content-label">&nbsp;<strong>Lĩnh vực:</strong></span>
                                            <span class="content">
                                            	<c:forEach items="${dto.categories}" var="category" varStatus="loop">
                                           			<a class="tag-item small" href="${pageContext.request.contextPath}/article?categoryId=${ category.id }" title="${ category.name }">
                                           				${ category.name }
                                           			</a>
                                            	</c:forEach>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Images -->
            <div class="col-md-12 col-sm-12 margin-top-10">
                <div class="card-box">
                    <div class="row content-panal-body table-row">
                        <h4 class="col-md-12 text-bold color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">
                        	Hình ảnh bài viết
                        </h4>
                        
                        <c:forEach items="${dto.images}" var="image" varStatus="loop">
	                        <div class="col-sm-3">
	                            <div class="blog-row blog-compact blog-compact-sm table-cell-content">
	                                <div class="blog-col">
	                                    <div class="blog-relative blog-hover blog-hover-scale">
	                                        <div class="blog-image blog-vgradient-black-lg">
	                                            <div class="overlay">
	                                            	<img src="${pageContext.request.contextPath}/upload/article/${ image }" 
	                                            		alt="Ảnh bài viết" width="100%" height="500" class="img-fluid blog-img">
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
			
			<!-- Comment -->
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