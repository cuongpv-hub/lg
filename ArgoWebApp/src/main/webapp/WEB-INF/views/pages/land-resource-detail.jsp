<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<a href="${pageContext.request.contextPath}/land/${ landItemDto.id }/view" title="${ landItemDto.name }" target="_blank">
	<strong>${ landItemDto.name }</strong>
</a>
<br />
<div class="float-left full-width name_owner font-size-14 padding-top-5">
	<c:if test="${ landItemDto.volume != null }">
		<div class="summary-content" title="Số lượng lô, mảnh, thửa đất">
			<i aria-hidden="true" class="fa fa-hashtag"></i>
			<span class="content">
				<span id="volume" class="text-number">${ landItemDto.volume }</span>
				thửa, lô, mảnh
			</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.square != null }">
		<div class="summary-content" title="Tổng diện tích">
			<i aria-hidden="true" class="fa fa-square-o"></i>
			<span class="content">
				<span id="square" class="text-number">${ landItemDto.square }</span>
				<c:if test="${ landItemDto.squareUnit != null }">${ landItemDto.squareUnit.name }</c:if>
			</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.tree != null && landItemDto.tree != '' }">
		<div class="summary-content" title="Đất hiện đang trồng">
			<i aria-hidden="true" class="fa fa-tree"></i>
			<span class="content">${ landItemDto.tree }</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.animal != null && landItemDto.animal != '' }">
		<div class="summary-content" title="Chăn nuôi">
			<i aria-hidden="true" class="fa fa-globe"></i>
			<span class="content">${ landItemDto.animal }</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.forest != null && landItemDto.forest != '' }">
		<div class="summary-content" title="Đất hiện đang trồng rừng">
			<i aria-hidden="true" class="fa fa-tree"></i>
			<span class="content">${ landItemDto.forest }</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.startDate != null || landItemDto.endDate != null }">
		<div class="summary-content" title="Thời gian dự kiến">
			<i aria-hidden="true" class="fa fa-calendar"></i>
			<span class="content">${ landItemDto.startDate } - ${ landItemDto.endDate }</span>
		</div>
	</c:if>
	
	<c:if test="${ landItemDto.nears != null && !landItemDto.nears.isEmpty() }">
		<div class="summary-content" title="Vị trí thửa đất">
			<i aria-hidden="true" class="fa fa-truck"></i>
			<span class="content">
				<c:forEach items="${landItemDto.nears}" var="category" varStatus="loop">
					<span class="content">${ category.name }</span><br>
				</c:forEach>
			</span>
		</div>
	</c:if>
	
	<div class="summary-content" title="Loại đất">
		<i aria-hidden="true" class="fa fa-tags"></i>
		<span class="content">
			<input type="hidden" id="categoryCount_${ landItemDto.id }" name="categoryCount_${ landItemDto.id }" value="${ landItemDto.categories.size() }">
			<c:forEach items="${ landItemDto.categories }" var="categoryDto" varStatus="categoryLoop">
				<span class="tag-item small">${ categoryDto.name }</span>
				<input type="hidden" id="category_${ landItemDto.id }_${ categoryLoop.index }" name="category_${ landItemDto.id }" value="${ categoryDto.name }">
			</c:forEach>
		</span>
	</div>
</div>