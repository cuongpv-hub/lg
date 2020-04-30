<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>
<div ng-controller="ScientistController">
	<section class="posts-2">
    <div class="container">
        <div class="row">
            <div class="col-md-8 ">
                <div class="card-box list-scientist widget-user bgr-white margin-top-20 min-height">
                    <h2 class="text-center text-bold padding-10 text-upc color-pr">Danh sách bài báo</h2>
                    <div class="row container-land container-scientist padding-10">
                    	<c:forEach items="${articleDtos}" var="dto" varStatus="loop">
                    		<div class="col-md-12 col-xs-12">
	                            <div class="item-project hover">
	                                <a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${dto.name}">
	                                    <img src="${pageContext.request.contextPath}/upload/article/${dto.mainImage}" alt="${dto.name}" class="img-thumb-responsive">
	                                </a>
	                                <div class="name-of-project text-muted font-13 non-midd content-responsive">
	                                    <div class="info-of-project">
	                                        <a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${dto.name}" class="by-name">${dto.name} </a>
	                                    </div>
	                                    <div class="info-of-project">
	                                        <a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="${dto.name}"><strong>Tác giả</strong>: <span class="name_owner">${dto.author.name}</span></a>
	                                    </div>
	                                    
	                                    	<strong>Lĩnh vực tư vấn: </strong><br>
	                                    	
											<span >
                                            	<c:forEach items="${dto.categories}" var="category" varStatus="loop">
                                           			<a href="${pageContext.request.contextPath}/article?categoryId=${ category.id }" title="${ category.name }">
                                           				<span class="tag-item small">${ category.name }</span>
                                           			</a>
                                            	</c:forEach>
                                            </span>
                                        
	                                    
	                                   
                                    
                                            <div class="info-of-project des-land">
	                                        	${dto.title}
	                                    	</div>
	                                    	<div class="read-more">
	                                        	<a href="${pageContext.request.contextPath}/article/${ dto.id }/view" title="Xem thêm >>" class="pull-right">Xem thêm &gt;&gt;</a>
	                                    	</div>
										</div>	
	                                    
	                                    
	                                    
	                                </div>
	                            </div>
                       
                    	</c:forEach>
                    	</div>
                    	                  
	                	
                        <!--  -->
                        <div class="col-md-12 text-center view_more"><a href="${pageContext.request.contextPath}/scientist" title="Xem tất cả">Xem tất cả</a></div>
                        <div class="clearfix"></div>
                        
                    </div>
                </div>
                <div class="col-md-4">
                <section class="margin-top-20">
                    <div class="card-box list-scientist widget-user bgr-white">
                        <h4 class="text-center text-upc color-pr text-bold">Danh sách nhà khoa học</h4>
                        <c:forEach items="${scientistDtos}" var="dto" varStatus="loop">
                        	<div class="item-enterprise hover">
	                            <a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }">
	                                <img src="${pageContext.request.contextPath}/upload/avatar/${dto.avatar}" alt="${ dto.name }">
	                            </a>
	                            
	                            <div class="name-of-enterprise text-muted font-13">	                            	
	                                <a href="${pageContext.request.contextPath}/profile/${ dto.id }/view" title="${ dto.name }" class="text-bold">${ dto.name }</a>
	                                <div class="phone-of-enterprise small">Địa chỉ: ${ dto.fullAddress }</div>
	                                <div class="phone-of-enterprise small">SĐT: ${ dto.phone }</div>
	                                <strong>Lĩnh vực tư vấn: </strong> 
	                                	<div style="display: inline;">
		                                	<c:forEach items="${dto.scientistMajors}" var="category" varStatus="loop">
		                                		<span class="tag-item small">${category.name}</span>		                                	
		                                	</c:forEach>
	                                	</div>
										<!-- <div ng-repeat="f in c.scientistFields" style="display: inline;">
											
										</div>	 -->
	                            </div>
                        	</div> 
                        
                        </c:forEach>
                        
							<div class="col-md-12 text-right view_more">
                           	 <a href="${pageContext.request.contextPath}/scientist/list" title="Xem tất cả">Xem tất cả</a>
                        	</div>
                        


                        <div class="clearfix">
							
                        </div>
                        
                    </div>
                </section>
            </div>
                
            </div>
        </div>
    </div>
</section>
</div>