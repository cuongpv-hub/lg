package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.Country;
import vn.vsd.agro.domain.District;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Organization;
import vn.vsd.agro.domain.Province;
import vn.vsd.agro.domain.ScientistField;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;
import vn.vsd.agro.dto.Envelope;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.CountryRepository;
import vn.vsd.agro.repository.DistrictRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.ProvinceRepository;
import vn.vsd.agro.repository.ScientistFieldRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.PasswordUtils;
import vn.vsd.agro.util.RoleUtils;

@Controller
public class InitDbController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private ScientistFieldRepository scientistFieldRepository;
	
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> initDatabase(HttpServletRequest request) {
		String requestUrl = request.getRemoteHost();

		IContext<ObjectId> dbContext = MongoContext.ROOT_CONTEXT;

		// Admin User
		String email = "atoz.net@gmail.com";
		User adminUser = userRepository.findByEmailOrPhone(email);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setName("Administrator");
			adminUser.setEmail(email);
			adminUser.setMobilePhone("0123456789");
			adminUser.setAddress("Lang Ha");
			adminUser.setCurrentOrgId(dbContext.getRootId());
			adminUser.setAccessOrgs(Arrays.asList(dbContext.getRootId()));
			adminUser.setRoles(Collections.singletonMap(RoleUtils.ROLE_ADMIN, true));
			adminUser.setActive(true);

			String password = PasswordUtils.getDefaultPassword();
			adminUser.setPassword(password);

			userRepository.save(dbContext, adminUser);
		}
		
		email = "cuongpv@gmail.com";
		adminUser = userRepository.findByEmailOrPhone(email);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setName("Phạm Văn Cường");
			adminUser.setEmail(email);
			adminUser.setMobilePhone("0972820725");
			adminUser.setAddress("Lang Ha");
			adminUser.setCurrentOrgId(dbContext.getRootId());
			adminUser.setAccessOrgs(Arrays.asList(dbContext.getRootId()));
			adminUser.setRoles(Collections.singletonMap(RoleUtils.ROLE_COMPANY, true));
			
			adminUser.addApproveOrg(new ObjectId("5b08cace0d28f126e4d5b1aa"));
			adminUser.addApproveOrg(new ObjectId("5b08cace0d28f126e4d5b1ab"));
			
			adminUser.setActive(true);
			

			String password = PasswordUtils.getDefaultPassword();
			adminUser.setPassword(password);

			userRepository.save(dbContext, adminUser);
		}
		
		
		email = "vanpt@gmail.com";
		adminUser = userRepository.findByEmailOrPhone(email);
		if (adminUser == null) {
			adminUser = new User();
			adminUser.setName("Phạm Thị Vân");
			adminUser.setEmail(email);
			adminUser.setMobilePhone("0972820726");
			adminUser.setAddress("Lang Ha");
			adminUser.setCurrentOrgId(dbContext.getRootId());
			adminUser.setAccessOrgs(Arrays.asList(dbContext.getRootId()));
			adminUser.setRoles(Collections.singletonMap(RoleUtils.ROLE_FARMER, true));
			adminUser.setActive(true);

			String password = PasswordUtils.getDefaultPassword();
			adminUser.setPassword(password);

			userRepository.save(dbContext, adminUser);
		}

		// Project Type
		List<CommonCategory> projectTypes = categoryRepository.getList(dbContext, CommonCategory.TYPE_PROJECT_TYPE,
				null, null);
		if (projectTypes == null || projectTypes.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d1.setCode("01");
			d1.setName("Trồng rừng");
			d1.setMain(true);
			d1.setIndex(1);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d2.setCode("02");
			d2.setName("Xây dựng");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d3.setCode("03");
			d3.setName("Chăn nuôi");
			d3.setIndex(3);
			
			CommonCategory d4 = new CommonCategory();
			d4.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d4.setCode("04");
			d4.setName("Môi trường");
			d4.setIndex(4);
			
			CommonCategory d5 = new CommonCategory();
			d5.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d5.setCode("05");
			d5.setName("Thời tiết");
			d5.setIndex(5);
			
			CommonCategory d6 = new CommonCategory();
			d6.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d6.setCode("06");
			d6.setName("Trắc địa");
			d6.setIndex(6);
			
			CommonCategory d7 = new CommonCategory();
			d7.setType(CommonCategory.TYPE_PROJECT_TYPE);
			d7.setCode("07");
			d7.setName("Phong thủy");
			d7.setIndex(7);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3, d4, d5, d6, d7);
			categoryRepository.saveBatch(dbContext, categories);
		}

		// Product Type
		List<CommonCategory> productTypes = categoryRepository.getList(dbContext, CommonCategory.TYPE_PRODUCT_TYPE,
				null, null);
		if (productTypes == null || productTypes.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_PRODUCT_TYPE);
			d1.setCode("01");
			d1.setName("Nông sản");
			d1.setMain(true);
			d1.setIndex(1);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_PRODUCT_TYPE);
			d2.setCode("02");
			d2.setName("Thủy sản");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_PRODUCT_TYPE);
			d3.setCode("03");
			d3.setName("Gia cầm");
			d3.setIndex(3);
			
			CommonCategory d4 = new CommonCategory();
			d4.setType(CommonCategory.TYPE_PRODUCT_TYPE);
			d4.setCode("04");
			d4.setName("Gia súc");
			d4.setIndex(4);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3, d4);
			categoryRepository.saveBatch(dbContext, categories);
		}


		// Square Uoms
		List<CommonCategory> squareUoms = categoryRepository.getList(dbContext, CommonCategory.TYPE_SQUARE_UOM,
				null, null);
		if (squareUoms == null || squareUoms.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_SQUARE_UOM);
			d1.setCode("m2");
			d1.setName("m<sup>2</sup>");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_SQUARE_UOM);
			d2.setCode("ha");
			d2.setName("ha");
			d2.setIndex(2);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2);
			categoryRepository.saveBatch(dbContext, categories);
		}

		// Money Uoms
		List<CommonCategory> moneyUoms = categoryRepository.getList(dbContext, CommonCategory.TYPE_MONEY_UOM,
				null, null);
		if (moneyUoms == null || moneyUoms.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_MONEY_UOM);
			d1.setCode("₫");
			d1.setName("VNĐ");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_MONEY_UOM);
			d2.setCode("$");
			d2.setName("Dollars");
			d2.setIndex(2);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Volumn UOM
		List<CommonCategory> volumeUnit = categoryRepository.getList(dbContext, CommonCategory.TYPE_VOLUME_UOM,
				null, null);
		if (volumeUnit == null || volumeUnit.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_VOLUME_UOM);
			d1.setCode("kg");
			d1.setName("Kilogram");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_VOLUME_UOM);
			d2.setCode("tạ");
			d2.setName("tạ");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_VOLUME_UOM);
			d2.setCode("tấn");
			d2.setName("tấn");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Land type
		List<CommonCategory> landTypes = categoryRepository.getList(dbContext, CommonCategory.TYPE_LAND_TYPE,
				null, null);
		if (landTypes == null || landTypes.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_LAND_TYPE);
			d1.setCode("NN");
			d1.setName("Nông nghiệp");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_LAND_TYPE);
			d2.setCode("TS");
			d2.setName("Thủy sản");
			d2.setIndex(2);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Land purpose
		List<CommonCategory> landPerpose = categoryRepository.getList(dbContext, CommonCategory.TYPE_LAND_PURPOSE,
				null, null);
		if (landPerpose == null || landPerpose.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_LAND_PURPOSE);
			d1.setCode("CNN");
			d1.setName("Cây ngắn ngày");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_LAND_PURPOSE);
			d2.setCode("NTS");
			d2.setName("Nuôi trồng thủy sản");
			d2.setIndex(2);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Land near
		List<CommonCategory> landNear = categoryRepository.getList(dbContext, CommonCategory.TYPE_LAND_NEAR,
				null, null);
		if (landNear == null || landNear.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_LAND_NEAR);
			d1.setCode("001");
			d1.setName("Gần quốc lộ");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_LAND_NEAR);
			d2.setCode("002");
			d2.setName("Gần nguồn nước");
			d2.setIndex(2);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Company Type
		List<CommonCategory> companyTypes = categoryRepository.getList(dbContext, CommonCategory.TYPE_COMPANY_TYPE,
				null, null);
		if (companyTypes == null || companyTypes.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_COMPANY_TYPE);
			d1.setCode("NN");
			d1.setName("Nhà nước");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_COMPANY_TYPE);
			d2.setCode("TN");
			d2.setName("Tư nhân");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_COMPANY_TYPE);
			d3.setCode("TC");
			d3.setName("Tổ chức khác");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Company Field
		List<CommonCategory> companyFields = categoryRepository.getList(dbContext, CommonCategory.TYPE_COMPANY_FIELD,
				null, null);
		if (companyFields == null || companyFields.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d1.setCode("TR");
			d1.setName("Trồng rừng");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d2.setCode("XD");
			d2.setName("Xây dựng");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d3.setCode("CN");
			d3.setName("Chăn nuôi");
			d3.setIndex(3);
			
			CommonCategory d4 = new CommonCategory();
			d4.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d4.setCode("TĐ");
			d4.setName("Trắc địa");
			d4.setIndex(4);
			
			CommonCategory d5 = new CommonCategory();
			d5.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d5.setCode("TT");
			d5.setName("Thời tiết");
			d5.setIndex(5);
			
			CommonCategory d6 = new CommonCategory();
			d6.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d6.setCode("MT");
			d6.setName("Môi trường");
			d6.setIndex(6);
			
			CommonCategory d7 = new CommonCategory();
			d7.setType(CommonCategory.TYPE_COMPANY_FIELD);
			d7.setCode("PT");
			d7.setName("Phong thuỷ");
			d7.setIndex(7);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3, d4, d5, d6, d7);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Company Type
		List<CommonCategory> literacies = categoryRepository.getList(dbContext, CommonCategory.TYPE_LITERACY,
				null, null);
		if (literacies == null || literacies.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_LITERACY);
			d1.setCode("CN");
			d1.setName("Cử nhân");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_LITERACY);
			d2.setCode("KS");
			d2.setName("Kĩ sư");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_LITERACY);
			d3.setCode("TS");
			d3.setName("Tiến sĩ");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Scientist Major
		List<CommonCategory> majors = categoryRepository.getList(dbContext, CommonCategory.TYPE_SCIENTIST_MAJOR,
				null, null);
		if (majors == null || majors.isEmpty()) {
			Set<String> majorCodes = new HashSet<>();
			if (majors != null) {
				for (CommonCategory d : majors) {
					majorCodes.add(d.getCode());
				}
			}
			
			List<CommonCategory> categories = new ArrayList<>();
			
			if (!majorCodes.contains("01")) {
				CommonCategory d1 = new CommonCategory();
				d1.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d1.setCode("01");
				d1.setName("Trồng trọt");
				d1.setIndex(1);
				d1.setMain(true);
				
				categories.add(d1);
			}
			
			if (!majorCodes.contains("02")) {
				CommonCategory d2 = new CommonCategory();
				d2.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d2.setCode("02");
				d2.setName("Chăn nuôi");
				d2.setIndex(2);
				
				categories.add(d2);
			}
			
			if (!majorCodes.contains("03")) {
				CommonCategory d3 = new CommonCategory();
				d3.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d3.setCode("03");
				d3.setName("Thú y");
				d3.setIndex(3);
				
				categories.add(d3);
			}
			
			if (!majorCodes.contains("04")) {
				CommonCategory d4 = new CommonCategory();
				d4.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d4.setCode("04");
				d4.setName("Lâm nghiệp");
				d4.setIndex(4);
				
				categories.add(d4);
			}
			
			if (!majorCodes.contains("05")) {
				CommonCategory d5 = new CommonCategory();
				d5.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d5.setCode("05");
				d5.setName("Thủy sản");
				d5.setIndex(5);
				
				categories.add(d5);
			}
			
			if (!majorCodes.contains("06")) {
				CommonCategory d6 = new CommonCategory();
				d6.setType(CommonCategory.TYPE_SCIENTIST_MAJOR);
				d6.setCode("06");
				d6.setName("Công nghệ sinh học nông nghiệp");
				d6.setIndex(6);
				
				categories.add(d6);
			}
			
			if (!categories.isEmpty()) {
				categoryRepository.saveBatch(dbContext, categories);
			}
		}
		
		// Scientist Fields
		List<ScientistField> scientistFields = scientistFieldRepository.getAll(dbContext);
		if (scientistFields == null || scientistFields.isEmpty()) {
			Map<String, List<?>> fields = new HashMap<>();
			// 01 - Trong trot
			fields.put("01", Arrays.asList(
					new String[] { "01", "Nông hoá" },
					new String[] { "02", "Thổ nhưỡng học" },
					new String[] { "03", "Cây lương thực và cây thực phẩm" },
					new String[] { "04", "Cây rau, cây hoa và cây ăn quả" },
					new String[] { "05", "Cây công nghiệp và cây thuốc" },
					new String[] { "06", "Bảo vệ thực vật" },
					new String[] { "07", "Bảo quản và chế biến nông sản" },
					new String[] { "08", "Khoa học công nghệ trồng trọt khác" }
				));
			
			// 02 - Chan nuoi
			fields.put("02", Arrays.asList(
					new String[] { "01", "Sinh lý và hoá sinh động vật nuôi" },
					new String[] { "02", "Di truyền và nhân giống động vật nuôi" },
					new String[] { "03", "Nuôi dưỡng động vật nuôi" },
					new String[] { "04", "Thức ăn và dinh dưỡng cho động vật nuôi" },
					new String[] { "05", "Bảo vệ động vật nuôi" },
					new String[] { "06", "Sinh trưởng và phát triển của động vật nuôi" },
					new String[] { "07", "Khoa học công nghệ chăn nuôi khác..." }
				));
			
			// 03 - Thu y
			fields.put("03", Arrays.asList(
					new String[] { "01", "Y học thú y" },
					new String[] { "02", "Gây mê và điều trị tích cực thú y" },
					new String[] { "03", "Dịch tễ học thú y" },
					new String[] { "04", "Miễn dịch học thú y" },
					new String[] { "05", "Giải phẫu học và sinh lý học thú y" },
					new String[] { "06", "Bệnh học thú y" },
					new String[] { "07", "Vi sinh vật học thú y (trừ vi rút)" },
					new String[] { "08", "Ký sinh trùng học thú y" },
					new String[] { "09", "Sinh học phóng xạ và chụp ảnh" },
					new String[] { "10", "Vi rút học thú y" },
					new String[] { "11", "Phẫu thuật thú y" },
					new String[] { "12", "Dược học thú ý" }
				));
			
			// 04 - Lam nghiep
			fields.put("04", Arrays.asList(
					new String[] { "01", "Lâm sinh" },
					new String[] { "02", "Quản lý và bảo vệ rừng" },
					new String[] { "03", "Tài nguyên rừng" },
					new String[] { "04", "Sinh thái và môi trường rừng" },
					new String[] { "05", "Giống cây rừng" },
					new String[] { "06", "Nông lâm kết hợp" },
					new String[] { "07", "Bảo quản và chế biến lâm sản" },
					new String[] { "08", "Khoa học công nghệ lâm nghiệp khác" }
				));
			
			// 05 - Thuy san
			fields.put("05", Arrays.asList(
					new String[] { "01", "Sinh lý và dinh dưỡng thuỷ sản" },
					new String[] { "02", "Di truyền học và nhân giống thuỷ sản" },
					new String[] { "03", "Bệnh học thuỷ sản" },
					new String[] { "04", "Nuôi trồng thuỷ sản" },
					new String[] { "05", "Giống cây rừng" },
					new String[] { "06", "Hệ sinh thái & ĐG nguồn lợi thuỷ sản" },
					new String[] { "07", "Quản lý và khai thác thuỷ sản" },
					new String[] { "08", "Bảo quản và chế biến thuỷ sản" },
					new String[] { "09", "KHCN thuỷ sản khác" }
				));
			
			// 06 - Cong nghe sinh hoc ngong nghiep
			fields.put("06", Arrays.asList(
					new String[] { "01", "Công nghệ gen (cây trồng và động vật nuôi); nhân dòng vật nuôi" },
					new String[] { "02", "Các công nghệ tế bào trong nông nghiệp" },
					new String[] { "03", "Các công nghệ enzym và protein trong nông nghiệp" },
					new String[] { "04", "Các công nghệ vi sinh vật trong nông nghiệp" },
					new String[] { "05", "Đạo đức học trong công nghệ sinh học nông nghiệp" },
					new String[] { "06", "Công nghệ sinh học trong nông nghiệp khác" }
				));
			
			majors = categoryRepository.getList(dbContext, CommonCategory.TYPE_SCIENTIST_MAJOR, null, null);
			
			List<ScientistField> newScientistFields = new ArrayList<>();
			
			for (CommonCategory major : majors) {
				@SuppressWarnings("unchecked")
				List<String[]> fieldValues = (List<String[]>) fields.get(major.getCode());
				IdCodeNameEmbed majorEmbed = new IdCodeNameEmbed(major, major.getCode(), major.getName());
				
				if (fieldValues != null) {
					for (String[] fieldValue : fieldValues) {
						ScientistField scientistField = new ScientistField();
						scientistField.setMajor(majorEmbed);
						scientistField.setCode(fieldValue[0]);
						scientistField.setName(fieldValue[1]);
						
						newScientistFields.add(scientistField);
					}
				}
			}
			
			if (!newScientistFields.isEmpty()) {
				scientistFieldRepository.saveBatch(dbContext, newScientistFields);
			}
		}
		
		// land cooperates		
		List<CommonCategory> landCooporate= categoryRepository.getList(dbContext, CommonCategory.TYPE_LAND_COOPERATE,
				null, null);
		if (landCooporate == null || landCooporate.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_LAND_COOPERATE);
			d1.setCode("001");
			d1.setName("Cho doanh nghiệp thuê đất");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_LAND_COOPERATE);
			d2.setCode("002");
			d2.setName("Hợp tác sản xuất doanh nghiệp bao tiêu sản phẩm");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_LAND_COOPERATE);
			d3.setCode("003");
			d3.setName("Sản xuất theo đặt hàng của doanh nghiệp");
			d3.setIndex(3);
			
			/*CommonCategory d4 = new CommonCategory();
			d4.setType(CommonCategory.LAND_COOPERATE_TYPE);
			d4.setCode("004");
			d4.setName("Hình thức khác");
			d4.setIndex(4);*/
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
				
		// product cooperates
		List<CommonCategory> productCooperate = categoryRepository.getList(dbContext, 
				CommonCategory.TYPE_PRODUCT_COOPERATE, null, null);
		if (productCooperate == null || productCooperate.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_PRODUCT_COOPERATE);
			d1.setCode("001");
			d1.setName("Ký hợp đồng sản xuất theo yêu cầu của doanh nghiệp");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_PRODUCT_COOPERATE);
			d2.setCode("002");
			d2.setName("DN ký hợp đồng bao tiêu sản phẩm");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_PRODUCT_COOPERATE);
			d3.setCode("003");
			d3.setName("Có sản phẩm mời doanh nghiệp đến thu mua");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// company cooperates
		List<CommonCategory> companyCooperates = categoryRepository.getList(dbContext, 
				CommonCategory.TYPE_COMPANY_COOPERATE, null, null);
		if (companyCooperates == null || companyCooperates.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_COMPANY_COOPERATE);
			d1.setCode("001");
			d1.setName("Ký hợp đồng sản xuất theo yêu cầu của doanh nghiệp");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_COMPANY_COOPERATE);
			d2.setCode("002");
			d2.setName("DN ký hợp đồng bao tiêu sản phẩm");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_COMPANY_COOPERATE);
			d3.setCode("003");
			d3.setName("Thuê đất tự sản xuất");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// farmer cooperates
		List<CommonCategory> farmerCooperates = categoryRepository.getList(dbContext, 
				CommonCategory.TYPE_FARMER_COOPERATE, null, null);
		if (farmerCooperates == null || farmerCooperates.isEmpty()) {
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_FARMER_COOPERATE);
			d1.setCode("001");
			d1.setName("Ký hợp đồng sản xuất theo yêu cầu của doanh nghiệp");
			d1.setIndex(1);
			d1.setMain(true);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_FARMER_COOPERATE);
			d2.setCode("002");
			d2.setName("DN ký hợp đồng bao tiêu sản phẩm");
			d2.setIndex(2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_FARMER_COOPERATE);
			d3.setCode("003");
			d3.setName("Có sản phẩm mời doanh nghiệp đến thu mua");
			d3.setIndex(3);
			
			List<CommonCategory> categories = Arrays.asList(d1, d2, d3);
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// scientist cooperates
		
		// Article type
		List<CommonCategory> articleTypes = categoryRepository.getList(dbContext, CommonCategory.TYPE_ARTICLE_TYPE,
				null, null);
		if (articleTypes == null || articleTypes.isEmpty()) {
			List<CommonCategory> categories = new ArrayList<>();
			
			CommonCategory d1 = new CommonCategory();
			d1.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d1.setCode("01");
			d1.setName("Trồng trọt");
			d1.setIndex(1);
			d1.setMain(true);
			categories.add(d1);
			
			CommonCategory d2 = new CommonCategory();
			d2.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d2.setCode("02");
			d2.setName("Chăn nuôi");
			d2.setIndex(2);
			categories.add(d2);
			
			CommonCategory d3 = new CommonCategory();
			d3.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d3.setCode("03");
			d3.setName("Thú y");
			d3.setIndex(3);
			categories.add(d3);
			
			CommonCategory d4 = new CommonCategory();
			d4.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d4.setCode("04");
			d4.setName("Lâm nghiệp");
			d4.setIndex(4);
			categories.add(d4);
			
			CommonCategory d5 = new CommonCategory();
			d5.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d5.setCode("05");
			d5.setName("Thủy sản");
			d5.setIndex(5);
			categories.add(d5);
			
			CommonCategory d6 = new CommonCategory();
			d6.setType(CommonCategory.TYPE_ARTICLE_TYPE);
			d6.setCode("06");
			d6.setName("Công nghệ sinh học nông nghiệp");
			d6.setIndex(6);
			categories.add(d6);
			
			categoryRepository.saveBatch(dbContext, categories);
		}
		
		// Country
		Country country = null;
		
		List<Country> countries = countryRepository.getAll(dbContext);
		if (countries != null) {
			for (Country c : countries) {
				if ("VN".equals(c.getCode())) {
					country = c;
					break;
				}
			}
		}
		
		if (country == null) {
			country = new Country();
			country.setCode("VN");
			country.setName("Việt Nam");
			
			country = countryRepository.save(dbContext, country);
		}
		
		// Province
		Province province = null;
		
		List<Province> provinces = provinceRepository.getListByCountry(dbContext, 
				country.getId(), null, null, null);
		if (provinces != null) {
			for (Province p : provinces) {
				if ("06".equals(p.getCode())) {
					province = p;
					break;
				}
			}
		}
		
		if (province == null) {
			province = new Province();
			province.setCode("06");
			province.setName("Cao Bằng");
			province.setCountry(new CountryEmbed(country));
			
			province = provinceRepository.save(dbContext, province);
		}
		
		// Districts
		List<District> districts = districtRepository.getListByProvince(dbContext, 
				province.getId(), null, null, null);
		if (districts == null || districts.isEmpty()) {
			ProvinceEmbed provinceEmbed = new ProvinceEmbed(province);
			
			District d1 = new District();
			d1.setProvince(provinceEmbed);
			d1.setCode("01");
			d1.setName("Thành Phố Cao Bằng");
			
			District d2 = new District();
			d2.setProvince(provinceEmbed);
			d2.setCode("02");
			d2.setName("Huyện Bảo Lạc");
			
			District d3 = new District();
			d3.setProvince(provinceEmbed);
			d3.setCode("03");
			d3.setName("Huyện Thông Nông");
			
			District d4 = new District();
			d4.setProvince(provinceEmbed);
			d4.setCode("04");
			d4.setName("Huyện Hà Quảng");
			
			District d5 = new District();
			d5.setProvince(provinceEmbed);
			d5.setCode("05");
			d5.setName("Huyện Trà Lĩnh");
			
			District d6 = new District();
			d6.setProvince(provinceEmbed);
			d6.setCode("06");
			d6.setName("Huyện Trùng Khánh");
			
			District d7 = new District();
			d7.setProvince(provinceEmbed);
			d7.setCode("07");
			d7.setName("Huyện Nguyên Bình");
			
			District d8 = new District();
			d8.setProvince(provinceEmbed);
			d8.setCode("08");
			d8.setName("Huyện Hoà An");
			
			District d9 = new District();
			d9.setProvince(provinceEmbed);
			d9.setCode("09");
			d9.setName("Huyện Quảng Uyên");
			
			District d10 = new District();
			d10.setProvince(provinceEmbed);
			d10.setCode("10");
			d10.setName("Huyện Thạch An");
			
			District d11 = new District();
			d11.setProvince(provinceEmbed);
			d11.setCode("11");
			d11.setName("Huyện Hạ Lang");
			
			District d12 = new District();
			d12.setProvince(provinceEmbed);
			d12.setCode("12");
			d12.setName("Huyện Bảo Lâm");
			
			District d13 = new District();
			d13.setProvince(provinceEmbed);
			d13.setCode("13");
			d13.setName("Huyện Phục Hoà");
			
			List<District> domains = Arrays.asList(d1, d2, d3, d4, d5, d6, 
					d7, d8, d9, d10, d11, d12, d13);
			districtRepository.saveBatch(dbContext, domains);
			
			districts = districtRepository.getListByProvince(dbContext, 
					province.getId(), null, null, null);
		}
		
		// Wards
		Map<String, List<?>> wards = new HashMap<>();
		
		wards.put("01", Arrays.asList(
				new String[] { "01", "Phường Đề Thám" },
				new String[] { "02", "Phường Duyệt Trung" },
				new String[] { "03", "Phường Hòa Chung" },
				new String[] { "04", "Phường Hợp Giang" },
				new String[] { "05", "Phường Ngọc Xuân" },
				new String[] { "06", "Phường Sông Bằng" },
				new String[] { "07", "Phường Sông Hiến" },
				new String[] { "08", "Phường Tân Giang" },
				new String[] { "09", "Xã Chu Trinh" },
				new String[] { "10", "Xã Hưng Đạo" },
				new String[] { "11", "Xã Vĩnh Quang" }
			));
		
		// Bảo Lạc
		wards.put("02", Arrays.asList(
				new String[] { "01", "Thị trấn Bảo Lạc" },
				new String[] { "02", "Xã Bảo Toàn" },
				new String[] { "03", "Xã Cô Ba" },
				new String[] { "04", "Xã Cốc Pàng" },
				new String[] { "05", "Xã Đình Phùng" },
				new String[] { "06", "Xã Hồng An" },
				new String[] { "07", "Xã Hồng Trị" },
				new String[] { "08", "Xã Hưng Đạo" },
				new String[] { "09", "Xã Hưng Thịnh" },
				new String[] { "10", "Xã Huy Giáp" },
				new String[] { "11", "Xã Khánh Xuân" },
				new String[] { "12", "Xã Kim Cúc" },
				new String[] { "13", "Xã Phan Thanh" },
				new String[] { "14", "Xã Sơn Lập" },
				new String[] { "15", "Xã Sơn Lộ" },
				new String[] { "16", "Xã Thượng Hà" },
				new String[] { "17", "Xã Xuân Trường" }
			));
		
		// Thông Nông
		wards.put("03", Arrays.asList(
				new String[] { "01", "Thị trấn Thông Nông" },
				new String[] { "02", "Xã Bình Lãng" },
				new String[] { "03", "Xã Cần Nông" },
				new String[] { "04", "Xã Cần Yên" },
				new String[] { "05", "Xã Đa Thông" },
				new String[] { "06", "Xã Lương Can" },
				new String[] { "07", "Xã Lương Thông" },
				new String[] { "08", "Xã Ngọc Động" },
				new String[] { "09", "Xã Thanh Long" },
				new String[] { "10", "Xã Vị Quang" },
				new String[] { "11", "Xã Yên Sơn" }
			));
		
		// Hà Quảng
		wards.put("04", Arrays.asList(
				new String[] { "01", "Thị trấn Xuân Hòa" },
				new String[] { "02", "Xã Cải Viên" },
				new String[] { "03", "Xã Đào Ngạn" },
				new String[] { "04", "Xã Hạ Thôn" },
				new String[] { "05", "Xã Hồng Sỹ" },
				new String[] { "06", "Xã Kéo Yên" },
				new String[] { "07", "Xã Lũng Nặm" },
				new String[] { "08", "Xã Mã Ba" },
				new String[] { "09", "Xã Nà Sác" },
				new String[] { "10", "Xã Nội Thôn" },
				new String[] { "11", "Xã Phù Ngọc" },
				new String[] { "12", "Xã Quý Quân" },
				new String[] { "13", "Xã Sóc Hà" },
				new String[] { "14", "Xã Sỹ Hai" },
				new String[] { "15", "Xã Thượng Thôn" },
				new String[] { "16", "Xã Tổng Cọt" },
				new String[] { "17", "Xã Trường Hà" },
				new String[] { "18", "Xã Vân An" },
				new String[] { "19", "Xã Vần Dính" }
			));
		
		// Trà Lĩnh
		wards.put("05", Arrays.asList(
				new String[] { "01", "Thị trấn Hùng Quốc" },
				new String[] { "02", "Xã Cao Chương" },
				new String[] { "03", "Xã Cô Mười" },
				new String[] { "04", "Xã Lưu Ngọc" },
				new String[] { "05", "Xã Quang Hán" },
				new String[] { "06", "Xã Quang Trung" },
				new String[] { "07", "Xã Quang Vinh" },
				new String[] { "08", "Xã Quốc Toản" },
				new String[] { "09", "Xã Tri Phương" },
				new String[] { "10", "Xã Xuân Nội" }
			));
		
		// Trùng Khánh
		wards.put("06", Arrays.asList(
				new String[] { "01", "Thị trấn Trùng Khánh" },
				new String[] { "02", "Xã Cảnh Tiên" },
				new String[] { "03", "Xã Cao Thăng" },
				new String[] { "04", "Xã Chí Viễn" },
				new String[] { "05", "Xã Đàm Thủy" },
				new String[] { "06", "Xã Đình Minh" },
				new String[] { "07", "Xã Đình Phong" },
				new String[] { "08", "Xã Đoài Côn" },
				new String[] { "09", "Xã Đức Hồng" },
				new String[] { "10", "Xã Khâm Thành" },
				new String[] { "11", "Xã Lăng Hiếu" },
				new String[] { "12", "Xã Lăng Yên" },
				new String[] { "13", "Xã Ngọc Chung" },
				new String[] { "14", "Xã Ngọc Côn" },
				new String[] { "15", "Xã Ngọc Khê" },
				new String[] { "16", "Xã Phong Châu" },
				new String[] { "17", "Xã Phong Nặm" },
				new String[] { "18", "Xã Thân Giáp" },
				new String[] { "19", "Xã Thông Huề" },
				new String[] { "20", "Xã Trung Phúc" }
			));
		
		// Nguyên Bình
		wards.put("07", Arrays.asList(
				new String[] { "01", "Thị trấn Nguyên Bình" },
				new String[] { "02", "Thị trấn Tĩnh Túc" },
				new String[] { "03", "Xã Bắc Hợp" },
				new String[] { "04", "Xã Ca Thành" },
				new String[] { "05", "Xã Hoa Thám" },
				new String[] { "06", "Xã Hưng Đạo" },
				new String[] { "07", "Xã Lang Môn" },
				new String[] { "08", "Xã Mai Long" },
				new String[] { "09", "Xã Minh Tâm" },
				new String[] { "10", "Xã Minh Thanh" },
				new String[] { "11", "Xã Phan Thanh" },
				new String[] { "12", "Xã Quang Thành" },
				new String[] { "13", "Xã Tam Kim" },
				new String[] { "14", "Xã Thịnh Vượng" },
				new String[] { "15", "Xã Thái Học" },
				new String[] { "16", "Xã Thành Công" },
				new String[] { "17", "Xã Thể Dục" },
				new String[] { "18", "Xã Triệu Nguyên" },
				new String[] { "19", "Xã Vũ Nông" },
				new String[] { "20", "Xã Yên Lạc" }
			));
		
		// Hoà An
		wards.put("08", Arrays.asList(
				new String[] { "01", "Thị trấn Nước Hai" },
				new String[] { "02", "Xã Bạch Đằng" },
				new String[] { "03", "Xã Bế Triều" },
				new String[] { "04", "Xã Bình Dương" },
				new String[] { "05", "Xã Bình Long" },
				new String[] { "06", "Xã Công Trừng" },
				new String[] { "07", "Xã Đại Tiến" },
				new String[] { "08", "Xã Dân Chủ" },
				new String[] { "09", "Xã Đức Long" },
				new String[] { "10", "Xã Đức Xuân" },
				new String[] { "11", "Xã Hà Trì" },
				new String[] { "12", "Xã Hoàng Tung" },
				new String[] { "13", "Xã Hòa Chung" },
				new String[] { "14", "Xã Hồng Nam" },
				new String[] { "15", "Xã Hồng Việt" },
				new String[] { "16", "Xã Hưng Đạo Lê Chung" },
				new String[] { "17", "Xã Nam Tuấn" },
				new String[] { "18", "Xã Ngũ Lão" },
				new String[] { "19", "Xã Nguyễn Huệ" },
				new String[] { "20", "Xã Quang Trung" },
				new String[] { "21", "Xã Trưng Vương" },
				new String[] { "22", "Xã Trương Lương" }
			));
		
		// Quảng Uyên
		wards.put("09", Arrays.asList(
				new String[] { "01", "Thị trấn Quảng Uyên" },
				new String[] { "02", "Xã Bình Lăng" },
				new String[] { "03", "Xã Cai Bộ" },
				new String[] { "04", "Xã Chí Thảo" },
				new String[] { "05", "Xã Đoài Khôn" },
				new String[] { "06", "Xã Độc Lập" },
				new String[] { "07", "Xã Hạnh Phúc" },
				new String[] { "08", "Xã Hoàng Hải" },
				new String[] { "09", "Xã Hồng Định" },
				new String[] { "10", "Xã Hồng Quang" },
				new String[] { "11", "Xã Ngọc Động" },
				new String[] { "12", "Xã Phi Hải" },
				new String[] { "13", "Xã Phúc Sen" },
				new String[] { "14", "Xã Quảng Hưng" },
				new String[] { "15", "Xã Quốc Dân" },
				new String[] { "16", "Xã Quốc Phong" },
				new String[] { "17", "Xã Tự Do" }
			));
		
		// Thạch An
		wards.put("10", Arrays.asList(
				new String[] { "01", "Thị trấn Đông Khê" },
				new String[] { "02", "Xã Canh Tân" },
				new String[] { "03", "Xã Danh Sỹ" },
				new String[] { "04", "Xã Đức Long" },
				new String[] { "05", "Xã Đức Thông" },
				new String[] { "06", "Xã Đức Xuân" },
				new String[] { "07", "Xã Kim Đồng" },
				new String[] { "08", "Xã Lê Lai" },
				new String[] { "09", "Xã Lê Lợi" },
				new String[] { "10", "Xã Minh Khai" },
				new String[] { "11", "Xã Quang Trọng" },
				new String[] { "12", "Xã Thái Cường" },
				new String[] { "13", "Xã Thị Ngân" },
				new String[] { "14", "Xã Thụy Hùng" },
				new String[] { "15", "Xã Trọng Con" },
				new String[] { "16", "Xã Vân Trình" }
			));
		
		// Hạ Lang
		wards.put("11", Arrays.asList(
				new String[] { "01", "Thị trấn Thanh Nhật" },
				new String[] { "02", "Xã An Lạc" },
				new String[] { "03", "Xã Cô Ngân" },
				new String[] { "04", "Xã Đức Quang" },
				new String[] { "05", "Xã Đồng Loan" },
				new String[] { "06", "Xã Kim Loan" },
				new String[] { "07", "Xã Lý Quốc" },
				new String[] { "08", "Xã Minh Long" },
				new String[] { "09", "Xã Quang Long" },
				new String[] { "10", "Xã Thái Đức" },
				new String[] { "11", "Xã Thắng Lợi" },
				new String[] { "12", "Xã Thị Hoa" },
				new String[] { "13", "Xã Việt Chu" },
				new String[] { "14", "Xã Vinh Quý" }
			));
		
		// Bảo Lâm
		wards.put("12", Arrays.asList(
				new String[] { "01", "Thị trấn Pác Miầu" },
				new String[] { "02", "Xã Đức Hạnh" },
				new String[] { "03", "Xã Lý Bôn" },
				new String[] { "04", "Xã Mông Ân" },
				new String[] { "05", "Xã Nam Cao" },
				new String[] { "06", "Xã Nam Quang" },
				new String[] { "07", "Xã Quảng Lâm" },
				new String[] { "08", "Xã Tân Việt" },
				new String[] { "09", "Xã Thạch Lâm" },
				new String[] { "10", "Xã Thái Học" },
				new String[] { "11", "Xã Thái Sơn" },
				new String[] { "12", "Xã Vĩnh Phong" },
				new String[] { "13", "Xã Vĩnh Quang" },
				new String[] { "14", "Xã Yên Thổ" }
			));
		
		// Phục Hoà
		wards.put("13", Arrays.asList(
				new String[] { "01", "Thị trấn Tà Lùng" },
				new String[] { "02", "Thị trấn Hòa Thuận" },
				new String[] { "03", "Xã Cách Linh" },
				new String[] { "04", "Xã Đại Sơn" },
				new String[] { "05", "Xã Hồng Đại" },
				new String[] { "06", "Xã Lương Thiện" },
				new String[] { "07", "Xã Mỹ Hưng" },
				new String[] { "08", "Xã Tiên Thành" },
				new String[] { "09", "Xã Triệu Ẩu" }
			));
		
		Map<String, Location> locationMaps = new HashMap<>();
		
		List<Location> locations = locationRepository.getAll(dbContext);
		for (Location location : locations) {
			locationMaps.put(location.getCode(), location);
		}
		
		List<Location> newLocations = new ArrayList<>();
		
		for (District district : districts) {
			String districtCode = district.getCode();
			
			@SuppressWarnings("unchecked")
			List<String[]> wardValues = (List<String[]>) wards.get(districtCode);
			
			if (wardValues != null) {
				DistrictEmbed districtEmbed = new DistrictEmbed(district);
				
				for (String[] values : wardValues) {
					String wardCode = districtCode + values[0];
					Location location = locationMaps.get(wardCode);
					
					if (location == null) {
						Location l = new Location();
						l.setCode(wardCode);
						l.setName(values[1]);
						l.setDistrict(districtEmbed);
						
						newLocations.add(l);
					}
				}
			}
		}
		
		if (!newLocations.isEmpty()) {
			locationRepository.saveBatch(dbContext, newLocations);
		}
		
		// Organization
		List<Organization> organizations = organizationRepository.getAll(dbContext);
		if (organizations == null || organizations.isEmpty()) {
			Organization d1 = new Organization();
			d1.setCode("01");
			d1.setName("Thành Phố Cao Bằng");
			d1.setIndex(1);
			
			Organization d2 = new Organization();
			d2.setCode("02");
			d2.setName("Huyện Bảo Lạc");
			d2.setIndex(2);
			
			Organization d3 = new Organization();
			d3.setCode("03");
			d3.setName("Huyện Thông Nông");
			d3.setIndex(3);
			
			Organization d4 = new Organization();
			d4.setCode("04");
			d4.setName("Huyện Hà Quảng");
			d4.setIndex(4);
			
			Organization d5 = new Organization();
			d5.setCode("05");
			d5.setName("Huyện Trà Lĩnh");
			d5.setIndex(5);
			
			Organization d6 = new Organization();
			d6.setCode("06");
			d6.setName("Huyện Trùng Khánh");
			d6.setIndex(6);
			
			Organization d7 = new Organization();
			d7.setCode("07");
			d7.setName("Huyện Nguyên Bình");
			d7.setIndex(7);
			
			Organization d8 = new Organization();
			d8.setCode("08");
			d8.setName("Huyện Hoà An");
			d8.setIndex(8);
			
			Organization d9 = new Organization();
			d9.setCode("09");
			d9.setName("Huyện Quảng Uyên");
			d9.setIndex(9);
			
			Organization d10 = new Organization();
			d10.setCode("10");
			d10.setName("Huyện Thạch An");
			d10.setIndex(10);
			
			Organization d11 = new Organization();
			d11.setCode("11");
			d11.setName("Huyện Hạ Lang");
			d11.setIndex(11);
			
			Organization d12 = new Organization();
			d12.setCode("12");
			d12.setName("Huyện Bảo Lâm");
			d12.setIndex(12);
			
			Organization d13 = new Organization();
			d13.setCode("13");
			d13.setName("Huyện Phục Hoà");
			d13.setIndex(13);
			
			List<Organization> domains = Arrays.asList(d1, d2, d3, d4, d5, d6, 
					d7, d8, d9, d10, d11, d12, d13);
			organizationRepository.saveBatch(dbContext, domains);
		}
		
		Map<String, String> x = Collections.singletonMap(requestUrl, adminUser.idAsString());
		return new Envelope(x).toResponseEntity(HttpStatus.OK);
	}

}
