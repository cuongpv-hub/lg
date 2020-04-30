<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>
<div ng-controller="AdministratorController">
<section class="posts-2">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3">
				<div class="card-box list-scientist widget-user bgr-white margin-top-20 min-height"> <!-- card-box list-scientist widget-user bgr-white margin-top-20 min-height -->  <!-- card card-profile widget-info-one -->
					<img src="${pageContext.request.contextPath}/resources/images/org-1.jpg" alt="Tỉnh Cao Bằng" style="width: 100%;"> 
					<div class="card-block">
						<h3 class="text-center">Tỉnh Cao Bằng</h3> 
						<ul class="list-unstyled info-profile-farmer">
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-map-marker"></i> 
								<span class="text-muted">&nbsp;	Tỉnh Cao Bằng</span>
							</li> 
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-phone"></i> 
								<span class="text-muted">&nbsp; 01699888444</span>
							</li> 
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-envelope-o"></i> 
								<span class="text-muted">&nbsp;	admin@admin.admin</span>
							</li>
						</ul>
					</div>
				</div>
			</div> 
			<div class="col-md-8">
				<div class="content card bgr-white">
					<div class="title_news"><h1>Giới thiệu tỉnh Cao Bằng</h1></div> 
					<div id="left_calculator" class="card-block" style="text-align: justify; line-height: 28px;">
						<p>Cao Bằng là tỉnh nằm ở phía Đông Bắc Việt Nam. Hai mặt Bắc và Đông Bắc giáp với tỉnh Quảng Tây (Trung Quốc), với đường biên giới dài 333.403&nbsp;km. Phía Tây giáp tỉnh Tuyên Quang và Hà Giang. Phía nam giáp tỉnh Bắc Kạn và Lạng Sơn. Theo chiều Bắc- Nam là 80&nbsp;km, từ 23°07'12" - 22°21'21" vĩ bắc (tính từ xã Trọng Con huyện Thạch An đến xã Đức Hạnh, huyện Bảo Lâm). Theo chiều đông- tây là 170&nbsp;km, từ 105°16'15" - 106°50'25" kinh đông (tính từ xã Quảng Lâm, huyện Bảo Lâm đến xã Lý Quốc, huyện Hạ Lang).</p> <p>Tỉnh Cao Bằng có diện tích đất tự nhiên 6.690,72&nbsp;km², là cao nguyên đá vôi xen lẫn núi đất, có độ cao trung bình trên 200 m, vùng sát biên có độ cao từ 600- 1.300 m so với mặt nước biển. Núi non trùng điệp. Rừng núi chiếm hơn 90% diện tích toàn tỉnh. Từ đó hình thành nên 3 vùng rõ rệt: Miền đông có nhiều núi đá, miền tây núi đất xen núi đá, miền tây nam phần lớn là núi đất có nhiều rừng rậm.</p> <p>Trên địa bàn tỉnh có hai dòng sông lớn là&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_G%C3%A2m">sông Gâm</a>&nbsp;ở phía tây và&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_B%E1%BA%B1ng_Giang">sông Bằng</a>&nbsp;ở vùng trung tâm và phía đông, ngoài ra còn có một số sông ngòi khác như&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_Qu%C3%A2y_S%C6%A1n">sông Quây Sơn</a>,&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_B%E1%BA%AFc_V%E1%BB%8Dng">sông Bắc Vọng</a>,&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_Nho_Qu%E1%BA%BF">sông Nho Quế</a>,&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_N%C4%83ng">sông Năng</a>,&nbsp;<a href="https://vi.wikipedia.org/wiki/S%C3%B4ng_Neo">sông&nbsp;</a></p> 
						<div class="clear">&nbsp;
						</div> 
						<div class="row">
							<div class="col-md-12 col-sm-12 margin-top-10">
								<div class="card-box">
									<div class="row card-block content-panal-body">
										<h4 class="col-md-12 color-1ab394 margin-top-15 margin-bottom-10" style="text-transform: uppercase;">Bình luận</h4> 
										<div style="min-width: 90%;">
											<ul id="comments-block" class="comments-list"></ul> 
											<div class="text-center"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
</div>