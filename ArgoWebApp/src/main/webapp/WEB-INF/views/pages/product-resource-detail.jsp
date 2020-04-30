<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<a href="${pageContext.request.contextPath}/product/${ productItemDto.id }/view" title="${ productItemDto.name }" target="_blank">
	<strong>${ productItemDto.name }</strong>
</a>
<br />
<div class="float-left full-width name_owner font-size-14 padding-top-5">
	<c:if test="${ productItemDto.square != null }">
		<div class="summary-content" title="Diện tích">
			<i aria-hidden="true" class="fa fa-globe"></i>
			<span class="content">
				<span id="square" class="text-number">${ productItemDto.square }</span>
				<c:if test="${ productItemDto.squareUnit != null }">${ productItemDto.squareUnit.name }</c:if>
			</span>
		</div>
	</c:if>
	
	<c:if test="${ productItemDto.volume != null }">
		<div class="summary-content" title="Sản lượng mỗi vụ (năm)">
			<i aria-hidden="true" class="fa fa-balance-scale"></i>
			<span class="content">
				<span id="volume" class="text-number">${ productItemDto.volume }</span>
				<c:if test="${ productItemDto.volumeUnit != null }">${ productItemDto.volumeUnit.name }</c:if>
			</span>
		</div>
	</c:if>
	
	<c:if test="${ productItemDto.moneyFrom != null || productItemDto.moneyTo != null }">
		<div class="summary-content" title="Giá thị trường">
			<i aria-hidden="true" class="fa fa-money"></i>
			<span class="content">
				<span id="moneyFrom" class="text-number">${ productItemDto.moneyFrom }</span>
			</span>
			<span class="content-label">-</span>
			<span class="content">
				<span id="moneyTo" class="text-number">${ productItemDto.moneyTo }</span>
				<c:if test="${ productItemDto.moneyUnit != null }">${ productItemDto.moneyUnit.name }</c:if>
			</span>
		</div>
	</c:if>
	
	<c:if test="${ productItemDto.startDate != null || productItemDto.endDate != null }">
		<div class="summary-content" title="Thời gian dự kiến">
			<i aria-hidden="true" class="fa fa-calendar"></i>
			<span class="content">${ productItemDto.startDate } - ${ productItemDto.endDate }</span>
		</div>
	</c:if>
	
	<div class="summary-content" title="Loại nông sản">
		<i aria-hidden="true" class="fa fa-tags"></i>
		<span class="content">
			<input type="hidden" id="categoryCount_${ productItemDto.id }" name="categoryCount_${ productItemDto.id }" value="${ productItemDto.categories.size() }">
			<c:forEach items="${ productItemDto.categories }" var="categoryDto" varStatus="categoryLoop">
				<span class="tag-item small">${ categoryDto.name }</span>
				<input type="hidden" id="category_${ productItemDto.id }_${ categoryLoop.index }" name="category_${ productItemDto.id }" value="${ categoryDto.name }">
			</c:forEach>
		</span>
	</div>
</div>