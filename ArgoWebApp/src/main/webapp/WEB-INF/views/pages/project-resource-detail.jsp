<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<a href="${pageContext.request.contextPath}/project/${ projectItemDto.id }/view" title="${ projectItemDto.name }" target="_blank">
	<strong>${ projectItemDto.name }</strong>
</a>
<br />
<div class="float-left full-width name_owner font-size-14 padding-top-5">
	<c:if test="${ projectItemDto.startDate != null || projectItemDto.endDate != null }">
		<div class="summary-content" title="Thời gian dự kiến">
			<i aria-hidden="true" class="fa fa-calendar"></i>
			<span class="content">${ projectItemDto.startDate } - ${ projectItemDto.endDate }</span>
		</div>
	</c:if>
	
	<div class="summary-content" title="Loại dự án">
		<i aria-hidden="true" class="fa fa-tags"></i>
		<span class="content">
			<input type="hidden" id="categoryCount_${ projectItemDto.id }" name="categoryCount_${ projectItemDto.id }" value="${ projectItemDto.categories.size() }">
			<c:forEach items="${ projectItemDto.categories }" var="categoryDto" varStatus="categoryLoop">
				<span class="tag-item small">${ categoryDto.name }</span>
				<input type="hidden" id="category_${ projectItemDto.id }_${ categoryLoop.index }" name="category_${ projectItemDto.id }" value="${ categoryDto.name }">
			</c:forEach>
		</span>
	</div>
</div>