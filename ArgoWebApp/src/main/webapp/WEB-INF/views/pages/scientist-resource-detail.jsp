<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<a href="${pageContext.request.contextPath}/profile/${ scientistItemDto.id }/view" title="${ scientistItemDto.name }" target="_blank">
	<strong>${ scientistItemDto.name }</strong>
</a>
<br />
<div class="float-left full-width name_owner font-size-14 padding-top-5">
	<strong>Lĩnh vực tư vấn</strong>:
	<br />
	<div style="display: inline;">
		<input type="hidden" id="majorCount_${ scientistItemDto.id }" name="majorCount_${ scientistItemDto.id }" value="${ scientistItemDto.scientistMajors.size() }">
		<c:forEach items="${ scientistItemDto.scientistMajors }" var="majorDto" varStatus="majorLoop">
			<span class="tag-item small">${ majorDto.name }</span>
			<input type="hidden" id="major_${ scientistItemDto.id }_${ majorLoop.index }" name="major_${ scientistItemDto.id }" value="${ majorDto.name }">
		</c:forEach>
	</div>
</div>