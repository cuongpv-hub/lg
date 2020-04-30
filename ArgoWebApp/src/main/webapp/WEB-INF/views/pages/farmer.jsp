<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>


<div ng-controller="FarmerController">
	<section class="posts-2">
    <div class="container">
        <div class="row">
            <div class="col-md-8 ">
            <!-- card-box list-scientist widget-user bgr-white margin-top-20 min-height -->
                <div class="card-box list-scientist widget-user bgr-white margin-top-20 min-height">
                    <h2 class="text-center text-bold padding-10 text-upc color-pr">Đất chờ liên kết</h2>
                    <div class="row container-land container-scientist padding-10"> 
                    	<c:forEach items="${landDtos}" var="land" varStatus="loop">
                    		<div class="col-md-12 col-xs-12">
	                            <div class="item-project hover">
	                                <a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }">
	                                    <img src="${pageContext.request.contextPath}/upload/land/${land.mainImage}" alt="${ land.name }" class="img-thumb-responsive">
	                                </a>
	                                <div class="name-of-project text-muted font-13 non-midd content-responsive">
	                                    <div class="info-of-project">
	                                        <a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }" class="by-name">${ land.name }</a>
	                                    </div>
	                                    <div class="info-of-project">
	                                        <a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="${ land.name }"><strong>Cá nhân/tổ chức</strong>: <span class="name_owner">{{land.contact.name}}</span></a>
	                                    </div>
	                                    <div class="info-of-land"><strong>Trạng thái</strong>: <span class="unconnected">${ land.status.comment }</span></div>
	                                   
	                                    <div class="info-of-project des-land">
	                                        ${ land.title }
	                                    </div>
	                                    <div class="read-more">
	                                        <a href="${pageContext.request.contextPath}/land/${ land.id }/view" title="Xem thêm >>" class="pull-right">Xem thêm &gt;&gt;</a>
	                                    </div>
	                                </div>
	                            </div>
                        	</div>
                    	</c:forEach>   
                    	<div class="clearfix"></div>
                    	<div class="col-md-12 text-center view_more">
                    		<a href="${pageContext.request.contextPath}/land" title="Xem tất cả">Xem tất cả</a>
                    	</div>
                    </div>
                    </div>
                    
                    <!-- NONG SAN -->
                   <div class="card-box list-scientist widget-user bgr-white margin-top-20 container-agr">
	                   <h2 class="text-center text-bold padding-10 text-upc color-pr">Danh sách nông sản cần bao tiêu</h2>
	                    <div class="row container-land container-scientist padding-10">    
	                    	<c:forEach items="${productDtos}" var="dto" varStatus="loop">
	                    		<div class="col-md-12 col-xs-12">
		                            <div class="item-project hover">
		                                <a href="${pageContext.request.contextPath}/product/${ dto.id }/view" title="${ dto.name }">
		                                    <img src="${pageContext.request.contextPath}/upload/product/${dto.mainImage}" alt="${ dto.name }" class="img-thumb-responsive"></a>
		                                <div class="name-of-project text-muted font-13 non-midd content-responsive">
		                                    <div class="info-of-project">
		                                        <a href="${pageContext.request.contextPath}/product/${ dto.id }/view" title="${ dto.name }" class="by-name">${ dto.name } </a>
		                                    </div>
		                                    <div class="info-of-project">
		                                        <a href="${pageContext.request.contextPath}/product/${ dto.id }/view" title="${ dto.name }"><strong>Cá nhân/tổ chức</strong>: <span class="name_owner">${ dto.contact.name }</span></a>
		                                    </div>
		                                    
		                                    <div class="info-of-land"><strong>Trạng thái</strong>: <span class="unconnected">${ dto.status.comment }</span></div>
		                                    <div class="info-of-project des-land">
		                                         ${ dto.title }
		                                    </div>
		                                    <div class="read-more">
		                                        <a href="${pageContext.request.contextPath}/product/${ dto.id }/view" title="Xem thêm >>" class="pull-right">Xem thêm &gt;&gt;</a>
		                                    </div>
		                                </div>
		                            </div>
	                        	</div>
	                    	</c:forEach>
	                        <div class="clearfix"></div>
	                        <div class="col-md-12 text-center view_more"><a href="${pageContext.request.contextPath}/product" title="Xem tất cả">Xem tất cả</a></div>
	                    </div>
                   </div>
            </div>

            <div class="col-md-4">
                <section class="margin-top-20">
                    <div class="card-box list-scientist widget-user bgr-white">
                        <h4 class="text-center text-upc color-pr text-bold">Danh sách cá nhân/tổ chức</h4>
                        <c:forEach items="${farmerDtos}" var="dto" varStatus="loop">
                        	<div class="item-enterprise hover">
	                            	<a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }">
										<c:if test="${ dto.avatar != null && dto.avatar != '' }">
											<img src="${pageContext.request.contextPath}/upload/avatar/${dto.avatar}" alt="${ dto.name }">
										</c:if>
									</a>
	                            
	                            <div class="name-of-enterprise text-muted font-13">	                            	
	                                <a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }" class="text-bold">${ dto.name }</a>
	                                <div class="phone-of-enterprise small">Địa chỉ: ${ dto.fullAddress }</div>
	                                <div class="phone-of-enterprise small">SĐT: ${ dto.phone }</div>
	                            </div>
                        	</div> 	
                        </c:forEach>
                        
                        
                       
                        <div class="clearfix"></div>
                        <div class="col-md-12 text-right view_more">
                            <a href="${pageContext.request.contextPath}/farmer/list" title="Xem tất cả">Xem tất cả</a>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</section>
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
		
		$('.text-number').each(function() {
			new AutoNumeric(this, numberOption);
	    });
	});
</script>