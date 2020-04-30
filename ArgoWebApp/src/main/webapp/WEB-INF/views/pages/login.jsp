<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<section class="login-form posts-2 table-center" style="background: none;">
    <div class="container-fluid cell-center">
        <div class="row">
            <div class="offset-md-3 col-md-6">
                <div class="login-content">
                    <div class="logo-container text-center">
                        <img class="img img-responsive" src="${pageContext.request.contextPath}/resources/images/logo.png">
                    </div>
                    <h2 class="text-center text-bold">
                    	<spring:message code="login.page.title"></spring:message>
                    </h2>
                    
                    <spring:message code="login.placeholder.account" var="accountPlaceholder"></spring:message>
                    <spring:message code="login.placeholder.password" var="passwordPlaceholder"></spring:message>
                    <spring:message code="login.placeholder.remember.me" var="rememberMePlaceholder"></spring:message>
                    
                    <form:form name="form" action="login" method="POST" enctype="multipart/form-data"
							modelAttribute="loginDto" class="form-horizontal padding-10">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        
                        <div class="form-group with-icon relative" title="${accountPlaceholder}">
                        	<span class="left-icon fa fa-user-circle-o"></span>
                        	<form:input path="account" name="account" id="account" type="text" cssClass="form-control" 
                        		placeholder="${accountPlaceholder}" required="required" />
                        	<span class="right-icon reqired-input">*</span>
							<form:errors path="account" cssClass="has_error" element="div" />
                        </div>
                        
                        <div class="form-group with-icon relative" title="${passwordPlaceholder}">
                        	<span class="left-icon fa fa-lock"></span>
                        	<form:password path="password" name="password" id="password" cssClass="form-control" 
                        		placeholder="${passwordPlaceholder}" required="required" />
                            <span class="right-icon reqired-input">*</span>
                            <form:errors path="password" cssClass="has_error" element="div" />
                        </div>
                        
                        <div class="form-group with-left-iconx relative" title="<spring:message code="register.placeholder.role"></spring:message>">
                        	<!-- <span class="left-icon fa fa-users"></span> -->
                        	<span class="form-controlx">
                        		<span class="form-control-list">
									<form:checkbox path="remember" value="true" label="${rememberMePlaceholder}" />
								</span>
							</span>
                        </div>
                        
                        <div class="form-group">
                        	<button type="submit" class="btn btn-success">
                            	<spring:message code="login.action.login"></spring:message>
                            </button>
                            <div class="pull-right" style="margin-top: 15px;">
                            	<spring:message code="login.message.have.not.account"></spring:message>
                            	<a href="${pageContext.request.contextPath}/register" style="color: red;">
                                	<spring:message code="login.action.goto.register"></spring:message>
                                </a>
                            </div>
                        </div>
                        <div class="form-group pull-right">
							<a href="${pageContext.request.contextPath}/forget-password" style="color: #1ab394; text-decoration: underline;">
								<spring:message code="login.message.forgot.password"></spring:message>
							</a>
                        </div>
                    </form:form>
                </div>
            </div>
		</div>
        <!-- /.row -->
       	
       	<%-- <div class="row">
            <div class="offset-md-3 col-md-6">
				<c:set var="lang">${pageContext.response.locale.language}</c:set>
		        <div class="language-container text-center">
		        	<a href="?lang=vi">
		        		<img src="<c:url value='/resources/images/language_vietnamese_inactive.png' />" 
		        				alt="Tiếng Việt" class="flag <c:if test="${lang == 'vi'}">active</c:if>" />
		        	</a>
		            <a href="?lang=en">
		            	<img src="<c:url value='/resources/images/language_english_inactive.png' />" 
		            			alt="English" class="flag <c:if test="${lang == 'en'}">active</c:if>" />
		            </a>
		        </div>
	        </div>
        </div> --%>
    </div>
    <!-- /.container -->
</section>