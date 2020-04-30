<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>

<div class="content posts-2 ">
	<div class="container">
		<div class="row margin-top-15">
			<div class="col-lg-3 col-md-3">
				<div class="card card-profile widget-info-one">
					<c:choose>
	    				<c:when test="${ profile.avatar == null || profile.avatar == '' }">
							<img src="${pageContext.request.contextPath}/upload/avatar/empty-avatar.jpg" alt="${profile.name}" class="card-image">
						</c:when>
						<c:otherwise>
							<img src="${pageContext.request.contextPath}/upload/avatar/${ profile.avatar }" alt="${profile.name}" class="card-image">
						</c:otherwise>
					</c:choose>
					
					<div class="card-block">
						<h3 class="text-center padding-10">${ profile.name }</h3>
						<ul class="list-unstyled info-profile-farmer">
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-map-marker"></i>
								<span class="text-muted">&nbsp;${ profile.fullAddress }</span>
							</li>
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-phone"></i>
								<span class="text-muted">&nbsp;${ profile.phone }</span>
							</li>
							<li class="color-1ab394">
								<i aria-hidden="true" class="fa fa-envelope-o"></i>
								<span class="text-muted">&nbsp;${ profile.email }</span>
							</li>
							<c:if test="${ profile.company }">
								<li class="color-1ab394">
									<i aria-hidden="true" class="fa fa-gavel"></i>
									<span class="text-muted">&nbsp;Mã số thuế: ${ profile.taxCode }</span>
								</li>
							</c:if>
							<c:if test="${ profile.farmer || profile.scientist }">
								<li class="color-1ab394">
									<i aria-hidden="true" class="fa fa-transgender-alt"></i>
									<c:choose>
	    								<c:when test="${ profile.isMale() }">
											<span class="text-muted">&nbsp;Nam</span>
										</c:when>
										<c:when test="${ profile.isFemale() }">
											<span class="text-muted">&nbsp;Nữ</span>
										</c:when>
										<c:otherwise>
											<span class="text-muted">&nbsp;Khác</span>
										</c:otherwise>
									</c:choose>
								</li>
								<li class="color-1ab394">
									<i aria-hidden="true" class="fa fa-calendar-o"></i>
									<span class="text-muted">&nbsp;${ profile.birthDay }</span>
								</li>
							</c:if>
						</ul>
						<c:if test="${ profile.editable }">
							<a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-success btn-block">Chỉnh sửa thông tin</a>
						</c:if>
					</div>
				</div>
				
				<c:if test="${ profile.company }">
					<div class="card card-profile padding-10">
						<h4 class="text-center bold-title material-green" style="font-size: 15px;">Lĩnh vực kinh doanh</h4>
						<hr class="mint">
						<div class="tag-container">
							<c:forEach items="${profile.companyFields}" var="dto" varStatus="loop">
								<span class="tag-item">${ dto.name }</span>
							</c:forEach>
						</div>
					</div>
					
					<div class="card card-profile padding-10">
						<h4 class="text-center bold-title material-green" style="font-size: 15px;">Nhà khoa học liên kết</h4>
						<hr class="mint">
						<c:choose>
	   						<c:when test="${ profile.linkedScientists == null || profile.linkedScientists.isEmpty() }">
	   							<div class="people-rel-list">
	   								Chưa có nhà khoa học liên kết
	   							</div>
	   						</c:when>
	   						<c:otherwise>
	   							<div class="people-rel-list">
									<ul class="people-rel-list-photos">
										<c:forEach items="${profile.linkedScientists}" var="dto" varStatus="loop">
											<li>
												<a href="${pageContext.request.contextPath}/scientist/${dto.id}/view" title="${ dto.name }">
													<img src="${pageContext.request.contextPath}/upload/avatar/${ dto.avatar }" alt="${ dto.name }">
												</a>
											</li>
										</c:forEach>
									</ul>
								</div>
	   						</c:otherwise>
	   					</c:choose>
					</div>
				</c:if>
				
				<c:if test="${ profile.scientist }">
					<div class="card card-profile padding-10">
						<h4 class="text-center bold-title material-green">Trình độ học vấn</h4>
						<hr class="mint">
						<ul class="list-unstyled">
							<c:forEach items="${profile.literacies}" var="dto" varStatus="loop">
								<li class="color-1ab394">
									<i aria-hidden="true" class="fa fa-graduation-cap"></i>
									<span class="text-muted">&nbsp;${ dto.name }</span>
								</li>
							</c:forEach>
						</ul>
					
						<hr class="dotted">
						<h4 class="text-center bold-title material-green">Lĩnh vực tư vấn</h4>
						<hr class="mint">
						<div class="text-muted tag-container">
							<c:forEach items="${profile.scientistMajors}" var="dto" varStatus="loop">
								<span class="tag-item small">${ dto.name }</span>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</div>
			
			<div class="col-lg-6 col-md-6">
				<h2 class="text-center text-upc text-bold margin-bottom-10">Dự án đang tham gia</h2>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-8.jpg"
											alt="Dự án  Sản Xuất Phân Bón CB-281" class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-phan-bon-cb-281"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Sản Xuất Phân Bón CB-281</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/77"
											class="back-color">Công ty TNHH Âu Bình</a>
									</p>
									<p class="back-color">Địa chỉ: 7378, Ấp Trạch Huỳnh, Phường
										Điền Trực Khanh, Quận Phụng Ánh, Thừa Thiên Huế</p>
									<p>Làng Kawakami, huyện Minamisaku, tỉnh Nagano Nhật Bản,
										vốn là vùng đất cằn cỗi, nhưng nhờ trồng rau...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-13.jpg"
											alt="Dự án  Trồng 1000 ha Dứa Tiêu CB-619"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-trong-1000-ha-dua-tieu-cb-619"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Trồng 1000 ha Dứa Tiêu CB-619</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/3"
											class="back-color">Công ty TNHH Khưu Châu</a>
									</p>
									<p class="back-color">Địa chỉ: 1463 Phố Kha Thảo Quyên,
										Phường 81, Quận Điệp Bạch, Cần Thơ</p>
									<p>Làng Kawakami, huyện Minamisaku, tỉnh Nagano Nhật Bản,
										vốn là vùng đất cằn cỗi, nhưng nhờ trồng rau...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-13.jpg"
											alt="Dự án  Xuất Khẩu Cà Rốt CB-336" class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-xuat-khau-ca-rot-cb-336"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Xuất Khẩu Cà Rốt CB-336</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/51"
											class="back-color">Công ty TNHH Ty Trực</a>
									</p>
									<p class="back-color">Địa chỉ: 90 Phố Đỗ Hội Thúy, Xã Chiêu
										Cái, Quận Vọng Vân, Hưng Yên</p>
									<p>Hiện Công ty C.P đang có 3 trang trại nuôi tôm trong nhà
										ở Thừa Thiên-Huế với diện tích lên đến 215...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-7.jpg"
											alt="Dự án  Trồng Chuối Thần Tiên CB-313"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-trong-chuoi-than-tien-cb-313"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Trồng Chuối Thần Tiên CB-313</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/7"
											class="back-color">Công ty TNHH Ma Nhung</a>
									</p>
									<p class="back-color">Địa chỉ: 795 Phố Vừ Cương Thuần, Xã
										Ngân, Quận Kiện, Hà Nội</p>
									<p>Hiện Công ty C.P đang có 3 trang trại nuôi tôm trong nhà
										ở Thừa Thiên-Huế với diện tích lên đến 215...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-3.jpg"
											alt="Dự án  Trồng Su Su Xuất Khẩu CB-907"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-trong-su-su-xuat-khau-cb-907"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Trồng Su Su Xuất Khẩu CB-907</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/8"
											class="back-color">Công ty TNHH Phan Dân</a>
									</p>
									<p class="back-color">Địa chỉ: 5228, Ấp Khương Trà Khuyên,
										Phường 84, Huyện Trác Siêu Ca, Quảng Bình</p>
									<p>Hiện Công ty C.P đang có 3 trang trại nuôi tôm trong nhà
										ở Thừa Thiên-Huế với diện tích lên đến 215...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-10.jpg"
											alt="Dự án  Sản Xuất Thực Phẩm CB-655" class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án Sản Xuất Thực Phẩm CB-655</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/1"
											class="back-color">1, Ấp 0, Phường Lục Thắm Hợp, Huyện
											Bùi, Hưng Yênn</a>
									</p>
									<p class="back-color">Địa chỉ: 320 Stephon Wall,
										Schinnermouth, NJ 74658-0530</p>
									<p>Làng Kawakami, huyện Minamisaku, tỉnh Nagano Nhật Bản,
										vốn là vùng đất cằn cỗi, nhưng nhờ trồng rau...</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content ">
								<div class="publish-image ">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/uploads/images/btmC5hwcf93t8xaZoOTydk6UVq0iyew7NqLopiNU.jpeg"
											alt="Dự án nuôi trồng hải sản Thu" class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Dự án nuôi trồng hải sản Thu</h4></a>
									<p>
										<a href="http://vicrop.mhsoft.vn/doanh-nghiep/1"
											class="back-color">1, Ấp 0, Phường Lục Thắm Hợp, Huyện
											Bùi, Hưng Yênn</a>
									</p>
									<p class="back-color">Địa chỉ: ha noi</p>
									<p></p>
									<p>Dự án nuôi trồng hải sản</p>
									<p></p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<h2 class="text-center text-upc text-bold margin-bottom-10">Bài
					viết</h2>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content">
								<div class="publish-image">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/samples/project-1.jpg"
											alt="Bưởi Lồng - Tương lai suất khẩu ra thế giới Phần 56"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/buoi-long-tuong-lai-suat-khau-ra-the-gioi-phan-56"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Bưởi Lồng - Tương lai suất khẩu ra thế giới Phần 56</h4></a>
									<p>Người trồng thanh long ở xã Bàu Đồn (huyện Gò Dầu, tỉnh
										Tây Ninh) cho biết đang rất lo lắng trước dịch bệnh đốm trắng
										gây hại cây ăn trái này.</p>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="read-more">
								<div class="feed pull-left ">
									<ul>
										<li><i aria-hidden="true" class="fa fa-calendar"></i> 7
											tháng trước</li>
										<li><i class="fa fa-comments"></i>10</li>&nbsp;
									</ul>
								</div>
								<div class="continue-reading pull-right">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/buoi-long-tuong-lai-suat-khau-ra-the-gioi-phan-56">Đọc
										tiếp<i class="fa fa-angle-right"></i>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content">
								<div class="publish-image">
									<div class="content-image small-image-block">
										<img src="http://vicrop.mhsoft.vn/storage/samples/land-10.jpg"
											alt="Các giống cây trồng cho năng suất cao trong năm nay Phần 174"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/cac-giong-cay-trong-cho-nang-suat-cao-trong-nam-nay-phan-174"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Các giống cây trồng cho năng suất cao trong năm nay Phần 174</h4></a>
									<p>Theo thống kê của Chi cục Trồng trọt - Bảo vệ thực vật
										(BVTV), đến ngày 30/9/2017 diện tích lúa trên địa bàn thành phố
										Hải Phòng bị chuột gây hại lên tới 1472 ha, có thể mất trắng
										khoảng 115 ha. Các ngành chức năng, chính quyền địa phương đang
										sát cánh cùng nông dân diệt chuột, giảm bớt thiệt hại.</p>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="read-more">
								<div class="feed pull-left ">
									<ul>
										<li><i aria-hidden="true" class="fa fa-calendar"></i> 7
											tháng trước</li>
										<li><i class="fa fa-comments"></i>4</li>&nbsp;
									</ul>
								</div>
								<div class="continue-reading pull-right">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/cac-giong-cay-trong-cho-nang-suat-cao-trong-nam-nay-phan-174">Đọc
										tiếp<i class="fa fa-angle-right"></i>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="tr-section feed">
					<div class="tr-post">
						<div class="post-content">
							<div class="news-content">
								<div class="publish-image">
									<div class="content-image small-image-block">
										<img
											src="http://vicrop.mhsoft.vn/storage/uploads/images/uMrHUt7Uymqkwmcqb6kArAcmcMMyKHPoVDkr7Fln.jpeg"
											alt="Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa"
											class="img-responsive">
									</div>
								</div>
								<div class="news-short">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/cao-bang-phat-trien-nong-nghiep-theo-huong-san-xuat-hang-hoa"
										class="entry-title"><h4 class="margin-top-8 line-height-24">
											Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng hóa</h4></a>
									<p>Cao Bằng phát triển nông nghiệp theo hướng sản xuất hàng
										hóa</p>
								</div>
							</div>
							<div class="clearfix"></div>
							<div class="read-more">
								<div class="feed pull-left ">
									<ul>
										<li><i aria-hidden="true" class="fa fa-calendar"></i> 3
											tháng trước</li>
										<li><i class="fa fa-comments"></i>0</li>&nbsp;
									</ul>
								</div>
								<div class="continue-reading pull-right">
									<a
										href="http://vicrop.mhsoft.vn/bai-viet/cao-bang-phat-trien-nong-nghiep-theo-huong-san-xuat-hang-hoa">Đọc
										tiếp<i class="fa fa-angle-right"></i>
									</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3">
				<!-- Thu dat cua nong dan -->
				<div class="card card-profile padding-10">
					<h4 class="text-center bold-title material-green">Thửa đất</h4> <hr class="mint"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/samples/land-4.jpg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/thua-dat/dat-nong-nghiep-tai-so-76-xa-coc-pang" class="entry-title text-muted">Đất Nông Nghiệp tại số 76 Xã Cốc Pàng</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/samples/land-7.jpg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/thua-dat/dat-o-tai-so-92-xa-thanh-long" class="entry-title text-muted">Đất Ở tại số 92 Xã Thanh Long</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/uPp7FN0jwSrrK62wT1dXjtTIhbFBeUleCJfTlcjp.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/thua-dat/dat-nong-nghiep" class="entry-title text-muted">Đất nông nghiệp</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/qgR7UNhAaUXuiaNSVLW1vBC49NnW61ltNL8VwXYB.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/thua-dat/dat-nong-nghiep-archives" class="entry-title text-muted">Đất nông nghiệp Archives</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/FwRR4LrzaDjC0kgIh6qLU0AmJFWR6tZHU7ezWJ03.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/thua-dat/thuo-dat-nong-nghiep" class="entry-title text-muted">Thưở đất Nông nghiệp</a></h5></div></div></div></div></div>
				
				<!-- Nong san cua nong dan -->
				<div class="card card-profile padding-10"><h4 class="text-center bold-title material-green">Nông sản</h4> <hr class="mint"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/tPTkmnUPxy55rklYRjEQ3Ck8uukwlQbfK6681MZH.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/mit-do" class="entry-title text-muted">Mít đỏ</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/g1voisivdd8QmcU5YD2VljkhWd2cuN5WjsjA7nYn.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/mac-cop-cao-bang" class="entry-title text-muted">Mắc Cọp Cao Bằng</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/EQKmFfE9gFMcaI5j6Y4xr0WRVBW41AyMTDnUDfeM.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/vai-thieu-luc-ngan" class="entry-title text-muted">Vải thiều Lục Ngạn</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/OkTyDSfdXcjO4ai8h2GXCgFiIxT2OdZxuhyl0BJ8.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/rau-trau-lang-son" class="entry-title text-muted">Rau Trâu Lạng Sơn</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/WFfOTB0UiHT2E0KVxKO4qNMQrGw0LnbLaemjO7q8.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/nhan-luc-ngan" class="entry-title text-muted">Nhãn Lục Ngạn</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/ZX6lXSGQiIXMIUh0FpRCNFt0Q22M0IDmHf462BTH.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/nho-my" class="entry-title text-muted">Nho my</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/QRdv37HvduG05ywfGbv2GzMwJuwQogktdAQqCGgq.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/mang-cau" class="entry-title text-muted">Mãng cầu</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/75HJFAblNcRY4y9WWEG04LpBPDAYpuy0k13TxuzN.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/sau-rieng-vuon" class="entry-title text-muted">Sầu riêng vườn</a></h5></div></div></div></div> <hr class="dotted"> <div class="tr-post margin-bottom-10"><div class="post-content"><div class="news-content-sm"><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/Nf8uQ42GRTv493qgftwlSwMRmjHNmSvMMtwMdgEl.png" alt="Image" class="img-responsive img-thumbnail"></div></div> <div class="news-short"><h5 class="none-h4"><a href="http://vicrop.mhsoft.vn/cho-nong-san/tao-my" class="entry-title text-muted">Táo mỹ</a></h5></div></div></div></div></div>
				
				<!-- Du an cua cong ty -->
				<div class="card card-profile widget-info-one"><h4 class="text-center bold-title material-green" style="font-size: 15px;">Dự án triển khai</h4> <hr class="mint"> <a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/samples/project-10.jpg" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/du-an-san-xuat-thuc-pham-cb-655" class="entry-title text-muted">Dự án  Sản Xuất Thực Phẩm CB-655</a></h4></div></div></div></div> <hr class="dotted"></div> <a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/samples/project-11.jpg" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/du-an-nong-nghiep-sach-cb-172" class="entry-title text-muted">Dự án  Nông Nghiệp Sạch CB-172</a></h4></div></div></div></div> <hr class="dotted"></div> <a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/acDSyoRrxo79dnGC4Z3FRBWqOsPKuRixNm2IohCo.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san" class="entry-title text-muted">Dự án nuôi trồng hải sản</a></h4></div></div></div></div> <hr class="dotted"></div> <a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/btmC5hwcf93t8xaZoOTydk6UVq0iyew7NqLopiNU.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-trong-hai-san-thu" class="entry-title text-muted">Dự án nuôi trồng hải sản Thu</a></h4></div></div></div></div> <hr class="dotted"></div> <a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/ruDyLmoubRVGbfWplFiptIDlTSXVJS5pHPF0qMvc.png" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/du-an-nuoi-tho" class="entry-title text-muted">Dự án nuôi Thỏ</a></h4></div></div></div></div> <hr class="dotted"></div> <a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><div class="padding-10"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><div class="tr-post margin-bottom-10"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><div class="post-content"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><div class="news-content-sm"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""><div class="publish-image"><div class="content-image"><img src="http://vicrop.mhsoft.vn/storage/uploads/images/lo17DUj7tJHGyEfn5KM2pdvhQQbMLFZRXxnkw5mI.jpeg" alt="Image" class="img-responsive img-thumbnail"></div></div></a><div class="news-short"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><h4 class="none-h4"><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" title=""></a><a href="http://vicrop.mhsoft.vn/du-an/ten-du-an" class="entry-title text-muted">Tên dự án</a></h4></div></div></div></div> <hr class="dotted"></div></div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		var numberOption = {
		    currencySymbol : '',
		    decimalCharacter : ',',
		    digitGroupSeparator : '.',
		    decimalPlacesRawValue: 3,
		    allowDecimalPadding: false
		};
		
		$('.text-number').each(function() {
			new AutoNumeric(this, numberOption);
	    });
	});
</script>