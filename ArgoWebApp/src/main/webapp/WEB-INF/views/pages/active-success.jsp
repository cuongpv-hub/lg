<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="login-form posts-2 table-center" style="background: none;">
    <div class="container-fluid cell-center">
        <div class="row">
            <div class="offset-md-3 col-md-6">
                <div class="login-content">
                    <div class="logo-container text-center">
                        <img class="img" src="${pageContext.request.contextPath}/resources/images/logo-2.png">
                    </div>
                    <h2 class="text-center text-bold">
                    	<spring:message code="active.page.title"></spring:message>
                    </h2>
                    	
                    <br>
                    <label>Bạn đã kích hoạt tài khoản thành công, hãy <a href="${pageContext.request.contextPath}/login" style="color: #1ab394; text-decoration: underline;">đăng nhập</a> để sử dụng các chức năng của hệ thống.</label>
                    
                    <br>
                </div>
            </div>
		</div>
    </div>
</section>