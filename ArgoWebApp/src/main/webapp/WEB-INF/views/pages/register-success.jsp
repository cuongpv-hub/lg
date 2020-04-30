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
                    	<spring:message code="register.page.title"></spring:message>
                    </h2>
                    	
                    <br>
                    <label>Bạn đã đăng ký thành công, hãy <span class="text-bold">kiểm tra email</span> để kích hoạt tài khoản của bạn.</label>
                    
                    <br>
                    <br>
                    <div class="form-group">
						<a href="${pageContext.request.contextPath}/login" style="color: #1ab394; text-decoration: underline;">
							<spring:message code="register.message.login"></spring:message>
						</a>
					</div>
					
					<div class="form-group">
						<a href="${pageContext.request.contextPath}/forget-password" style="color: #1ab394; text-decoration: underline;">
							<spring:message code="login.message.forgot.password"></spring:message>
						</a>
					</div>
                </div>
            </div>
		</div>
    </div>
</section>