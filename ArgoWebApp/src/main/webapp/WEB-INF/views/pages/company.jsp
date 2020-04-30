<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="posts-2">
    <div class="container">
        <div class="row">
            <div class="col-md-8 ">
                <div class="card-box list-scientist widget-user bgr-white margin-top-20 min-height">
                    <h3 class="text-center text-bold padding-10 text-upc color-pr">
                    	Danh sách dự án tìm đất sản xuất
                    </h3>
                    <div class="row container-land container-scientist padding-10">
                    	<c:forEach items="${projectDtos}" var="dto" varStatus="loop">
	                		<div class="col-md-12 col-xs-12">
	                            <div class="item-project hover">
									<a href="${pageContext.request.contextPath}/project/${ dto.id }/view" title="${ dto.name }">
	                                	<c:if test="${ dto.mainImage != null && dto.mainImage != '' }">
											<img src="${pageContext.request.contextPath}/upload/project/${dto.mainImage}" 
												alt="${ dto.name }" class="img-thumb-responsive">
										</c:if>
									</a>
	                                <div class="name-of-project text-muted font-13 non-midd content-responsive">
	                                    <div class="info-of-project">
	                                        <a href="${pageContext.request.contextPath}/project/${ dto.id }/view" title="${ dto.name }" class="by-name">${ dto.name }</a>
	                                    </div>
	                                    <div class="info-of-project">
	                                    	<a href="${pageContext.request.contextPath}/profile/${ dto.contact.id }/view" title="${ dto.contact.name }">
	                                        	<strong class="text-bold text-black">Doanh nghiệp</strong>:
	                                        	<span class="name_owner">${ dto.contact.name }</span>
	                                        </a>
	                                    </div>
	                                    <div class="info-of-project">
	                                    	<strong class="text-bold text-black">Số điện thoại</strong>: ${ dto.contact.phone }
	                                    </div>
	                                    <div class="info-of-project">
	                                    	<strong class="text-bold text-black">Địa chỉ</strong>: ${ dto.fullAddress }
	                                    </div>
	                                    <div class="info-of-project des-land">${ dto.title }</div>
	                                    <div class="read-more">
	                                        <a href="${pageContext.request.contextPath}/project/${ dto.id }/view" title="Xem thêm >>" class="pull-right">Xem thêm &gt;&gt;</a>
	                                    </div>
	                                </div>
	                            </div>
                        	</div>
	                	</c:forEach>                    
                        <div class="clearfix"></div>
                        <div class="col-md-12 text-center view_more">
                            <a href="${pageContext.request.contextPath}/project/list" title="Xem tất cả">Xem tất cả</a>
                        </div>
                    </div>
                </div>
            </div>

			<div class="col-md-4">
				<c:if test="${ companyDtos != null && !companyDtos.isEmpty() }">
					<section class="margin-top-20">
						<div class="row card-box list-scientist widget-user bgr-white">
							<h4 class="col-xs-12 text-center text-upc color-pr text-bold">Danh sách công ty</h4>
							
							<c:forEach items="${companyDtos}" var="dto" varStatus="loop">
								<div class="col-xs-12 item-enterprise hover">
									<a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }">
										<c:if test="${ dto.avatar != null && dto.avatar != '' }">
											<img src="${pageContext.request.contextPath}/upload/avatar/${dto.avatar}" alt="${ dto.name }">
										</c:if>
									</a>
									<div class="name-of-enterprise text-bold text-muted font-13">	                            	
										<a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }" class="text-bold">
											${ dto.name }
										</a>
										<div class="phone-of-enterprise small">Địa chỉ: ${ dto.fullAddress }</div>
										<div class="phone-of-enterprise small">SĐT: ${ dto.phone }</div>
										<div class="phone-of-enterprise small">Lĩnh vực kinh doanh:
											<br />
											<c:forEach items="${dto.companyFields}" var="field" varStatus="fieldLoop">
												<div style="display: inline">
													<span class="tag-item small">${ field.name }</span>
												</div>
											</c:forEach>
										</div>
									</div>
								</div>                       	
							</c:forEach>
							<div class="clearfix"></div>
							<div class="col-xs-12 text-right view_more">
								<a href="${pageContext.request.contextPath}/company/list" title="Xem tất cả">Xem tất cả</a>
							</div>
						</div>
					</section>
				</c:if>
				
				<c:if test="${ articleDtos != null && !articleDtos.isEmpty() }">
					<section class="margin-top-20">
						<div class="row card-box list-post-exchange widget-user bgr-white">
							<h4 class="col-xs-12 text-center text-upc color-pr text-bold">Mua và bán</h4>
							
							<c:forEach items="${articleDtos}" var="dto" varStatus="loop">
								<div class="col-xs-12 item-post-exchange hover">
									<a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${ dto.name }">
										<c:if test="${ dto.mainImage != null && dto.mainImage != '' }">
											<img src="${pageContext.request.contextPath}/upload/article/${dto.mainImage}" alt="${ dto.name }">
										</c:if>
									</a>
									<div class="name-of-transection text-bold text-muted font-13">	                            	
										<a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${ dto.name }" class="text-bold by-name">
											${ dto.name }
										</a>
										<div class="post-by-enterprise small">
											<a href="${pageContext.request.contextPath}/profile/${ dto.author.id }/view" title="${ dto.name }" class="hv-a">
												Đăng bởi: ${ dto.author.name }
											</a>
										</div>
									</div>
								</div>                       	
							</c:forEach>
							<div class="clearfix"></div>
							<div class="col-xs-12 text-right view_more">
								<a href="${pageContext.request.contextPath}/article/list" title="Xem tất cả">Xem tất cả</a>
							</div>
						</div>
					</section>
				</c:if>
            </div>
        </div>
    </div>
</section>
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