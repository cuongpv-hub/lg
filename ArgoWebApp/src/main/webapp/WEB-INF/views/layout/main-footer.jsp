<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>

<div>
    <section id="content-footer" style="background: darkgreen">
        <div class="container-fluid" id="footer">
            <div class="container">
                <div class="row">
                    <div class="col-md-12" id="tp-footer">
                        <img src="${pageContext.request.contextPath}/resources/images/logo-footer.png" class="img-responsive" alt="">
                        <div>
                            <div id="legal-province" class="pull-left">
                                <p>
                                    Giấy phép số: 20/GP-TTĐT ngày 12/3/2018 của Cục Phát thanh, truyền hình và thông tin điện tử
                                </p>
                                <p>
                                    Cơ quan thường trực: Văn phòng Ủy ban nhân dân tỉnh Cao Bằng
                                </p>
                                <p>
                                    Chịu trách nhiệm chính: Chánh Văn phòng Ủy ban nhân dân tỉnh
                                </p>
                                <p>
                                    Trụ sở: Đường Nguyễn Trãi, Thành phố Cao Bằng.
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="col-md-4 col-sm-4 col-xs-12 category">
                            <div class="ft-partial-item">Danh mục</div>
                            <ul class="list-unstyled">
                                <li><a href="#" ng-click="onClickMenu(1)" title="Doanh nghiệp">Doanh nghiệp</a></li>
                                <li><a href="#" ng-click="onClickMenu(2)" title="Nhà nông">Nhà nông</a></li>
                                <li><a href="#" ng-click="onClickMenu(3)" title="Nhà khoa học">Nhà khoa học</a></li>
                                <li><a href="#" ng-click="onClickMenu(4)" title="Nhà nước">Nhà nước</a></li>
                            </ul>
                        </div>
                        <div class="col-md-4 col-sm-4 col-xs-12 category">
                            <div class="ft-partial-item">Thông tin liên hệ</div>
                            <ul class="list-unstyled">
                                <li>Điện thoại  &nbsp; &nbsp; : 01676391060</li>
                                <li>Fax &nbsp;&nbsp; : 02834574583</li>
                                <li>Email &nbsp;: agrolink@caobang.gov.vn</li>
                            </ul>
                        </div>
                        <div class="col-md-4 col-sm-4 col-xs-12 category">
                            <div class="ft-partial-item">Kết nối với chúng tôi</div>
                            <ul class="list-unstyled" id="list-social">
                                <li>
                                	<a href="#">
                                		<img src="${pageContext.request.contextPath}/resources/images/fb-icon.png" class="img-responsive" alt="Facebook">
                                		<span>Facebook</span>
                                	</a>
                                </li>
                                <li>
                                	<a href="#">
                                		<img src="${pageContext.request.contextPath}/resources/images/google-plus-icon.png" class="img-responsive" alt="Google Plus">
                                		<span>Google Plus</span>
                                	</a>
                                </li>
                                <li><a href="#"><img src="${pageContext.request.contextPath}/resources/images/youtube-icon.png" class="img-responsive" alt="Youtube"><span>Youtube</span></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="design-by">
            <div class="container">
                <div class=" col-md-6 col-sm-6 text-left">
                    Copyright © 2018 - Bản quyền thuộc về UBND tỉnh Cao Bằng
                </div>
                <div class="col-md-6 col-sm-6 text-right">Designed by <a href="https://vsdvn.com.vn/" target="_blank">VSD</a></div>
            </div>
        </div>
    </section>
</div>