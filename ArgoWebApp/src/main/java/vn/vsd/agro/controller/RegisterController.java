package vn.vsd.agro.controller;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.RegisterDto;
import vn.vsd.agro.mail.EmailSender;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.PasswordUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.RegisterValidator;

@Controller
public class RegisterController extends BaseController {
	
	@Autowired
	private RegisterValidator validator;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	/*@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ScientistFieldRepository scientistFieldRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private FarmerRepository farmerRepository;*/
	
	@Autowired
	private EmailSender emailSender;
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (RegisterDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.setValidator(this.validator);
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, RegisterDto dto) {
		IContext<ObjectId> dbContext = MongoContext.ROOT_CONTEXT;
		
		// Location
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				dto.getCountryId(), dto.getProvinceId(), 
				dto.getDistrictId(), dto.getLocationId(), 
				false, false, false, false);
		
		dto.setCountryId(locationInfo.getCurrentCountryId());
		dto.setProvinceId(locationInfo.getCurrentProvinceId());
		dto.setDistrictId(locationInfo.getCurrentDistrictId());
		dto.setLocationId(locationInfo.getCurrentLocationId());
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PROJECT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		
		model.addAttribute("allRoles", RoleUtils.REGISTRABLE_ROLES);
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		model.addAttribute("allLocations", locationInfo.getAllLocationMap());
		model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
		model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		
		model.addAttribute("registerDto", dto);

		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveRegister(HttpSession session, HttpServletRequest httpRequest, Model model,
			@ModelAttribute("registerDto") @Validated RegisterDto registerDto, 
			BindingResult result, final RedirectAttributes redirectAttributes) {

		IContext<ObjectId> dbContext = MongoContext.ROOT_CONTEXT;
		
		String email = registerDto.getEmail().trim();
		String mobilePhone = registerDto.getMobilePhone().trim();
		String companyPhone = registerDto.getCompanyPhone().trim();
		String homePhone = registerDto.getHomePhone().trim();
		
		if (userRepository.existEmail(email)) {
			this.addModelError(result, "registerDto", "email", email, "register.message.exists.email");
		}
		
		boolean validPhone = false;
		if (!StringUtils.isNullOrEmpty(mobilePhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(mobilePhone)) {
				this.addModelError(result, "registerDto", "mobilePhone", mobilePhone, "register.message.exists.phone");
			}
		}
		
		if (!StringUtils.isNullOrEmpty(companyPhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(companyPhone)) {
				this.addModelError(result, "registerDto", "companyPhone", companyPhone, "register.message.exists.phone");
			}
		}
		
		if (!StringUtils.isNullOrEmpty(homePhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(homePhone)) {
				this.addModelError(result, "registerDto", "homePhone", homePhone, "register.message.exists.phone");
			}
		}
		
		if (!validPhone) {
			this.addModelError(result, "registerDto", "mobilePhone", mobilePhone, "register.message.not.empty.phone");
		}
		
		ObjectId locationId = dbContext.parse(registerDto.getLocationId(), null);
		if (locationId == null) {
			this.addModelError(result, "registerDto", "locationId", registerDto.getLocationId(), "register.message.invalid.location");
		}
		
		Location location = locationRepository.getById(dbContext, locationId);
		if (location == null) {
			this.addModelError(result, "registerDto", "locationId", registerDto.getLocationId(), "register.message.invalid.location");
		}
		
		if (result.hasErrors()) {
			return this.register(model, registerDto);
		}
		
		
		ObjectId rootId = dbContext.getRootId();
		String validToken = UUID.randomUUID().toString();
		
		String password = PasswordUtils.encode(registerDto.getPassword());
		
		User newUser = new User();
		newUser.setName(registerDto.getName());
		newUser.setPassword(password);
		
		newUser.setEmail(email);
		newUser.setMobilePhone(mobilePhone);
		newUser.setCompanyPhone(companyPhone);
		newUser.setHomePhone(homePhone);
		newUser.setFax(registerDto.getFax());
		//newUser.setContactName(registerDto.getContactName());
		
		newUser.setAddress(registerDto.getAddress());
		newUser.setLocation(new LocationEmbed(location));
		
		newUser.addRole(RoleUtils.ROLE_USER, false);
		for (String role : registerDto.getRoles()) {
			newUser.addRole(role, false);
		}
		
		newUser.addAccessOrg(rootId);
		newUser.setCurrentOrgId(rootId);
		newUser.setValidToken(validToken);
		newUser.setActive(true);
		
		/*Company company = null;
		Scientist scientist = null;
		Farmer farmer = null;
		
		if (newUser.isCompany()) {
			company = new Company();
			company.setTaxCode(registerDto.getTaxCode());
			company.setDescription(registerDto.getDescription());
			company.setActive(false);
			
			ObjectId typeId = dbContext.parse(registerDto.getTypeId(), null);
			if (typeId != null) {
				CommonCategory companyType = categoryRepository.getById(dbContext, typeId);
				
				if (companyType != null && companyType.getType() == CommonCategory.TYPE_COMPANY_TYPE) {
					company.setType(new IdCodeNameEmbed(companyType, companyType.getCode(), companyType.getName()));
				}
			}
			
			List<ObjectId> companyFieldIds = dbContext.parseMulti(registerDto.getCompanyFieldIds(), true);
			if (companyFieldIds != null && !companyFieldIds.isEmpty()) {
				List<CommonCategory> companyFields = categoryRepository.getListByIds(dbContext, companyFieldIds);
				
				if (companyFields != null) {
					for (CommonCategory companyField : companyFields) {
						company.addCompanyField(new IdCodeNameEmbed(companyField, companyField.getCode(), companyField.getName()));
					}
				}
			}
		}
		
		SimpleDate birthDay = null;
		if (!StringUtils.isNullOrEmpty(registerDto.getBirthDay())) {
			birthDay = SimpleDate.parseDate(registerDto.getBirthDay(), ConstantUtils.DEFAULT_DATE_FORMAT, null);
		}
		
		if (newUser.isFarmer()) {
			farmer = new Farmer();
			farmer.setGender(registerDto.getGender());
			farmer.setBirthDay(birthDay);
			farmer.setActive(false);
		}
		
		if (newUser.isScientist()) {
			scientist = new Scientist();
			scientist.setGender(registerDto.getGender());
			scientist.setBirthDay(birthDay);
			scientist.setTitle(registerDto.getTitle());
			scientist.setPosition(registerDto.getPosition());
			scientist.setWorkplace(registerDto.getWorkplace());
			scientist.setActive(false);
			
			// 
			List<ObjectId> scientistMajorIds = dbContext.parseMulti(registerDto.getScientistMajorIds(), true);
			if (scientistMajorIds != null && !scientistMajorIds.isEmpty()) {
				List<CommonCategory> scientistMajors = categoryRepository.getListByIds(dbContext, scientistMajorIds);
				
				if (scientistMajors != null && !scientistMajors.isEmpty()) {
					for (CommonCategory scientistMajor : scientistMajors) {
						ScientistMajorEmbed majorEmbed = new ScientistMajorEmbed(scientistMajor);
						scientist.addScientistMajor(majorEmbed);
					}
					
					List<ObjectId> scientistFieldIds = dbContext.parseMulti(registerDto.getScientistFieldIds(), true);
					
					if (scientistFieldIds != null && !scientistFieldIds.isEmpty()) {
						List<ScientistField> scientistFields = scientistFieldRepository.getListByIds(dbContext, 
								scientistMajorIds, scientistFieldIds);
						
						if (scientistFields != null) {
							
							for (ScientistField scientistField : scientistFields) {
								IdCodeNameEmbed fieldEmbed = new IdCodeNameEmbed(scientistField, 
										scientistField.getCode(), scientistField.getName());
								scientist.addScientistField(scientistField.getMajor().getId(), fieldEmbed);
							}
						}
					}
				}
			}
			
			List<ObjectId> literacyIds = dbContext.parseMulti(registerDto.getLiteracyIds(), true);
			if (literacyIds != null && !literacyIds.isEmpty()) {
				List<CommonCategory> literacies = categoryRepository.getListByIds(dbContext, literacyIds);
				if (literacies != null) {
					for (CommonCategory literacy : literacies) {
						IdCodeNameEmbed literacyEmbed = new IdCodeNameEmbed(literacy, literacy.getCode(), literacy.getName());
						scientist.addLiteracy(literacyEmbed);
					}
				}
			}
		}*/
		
		MultipartFile imageFile = registerDto.getImageFile();
		if (imageFile != null && !imageFile.isEmpty()) {
			File saveFile = FileUtils.saveUploadAvatarImage(session, imageFile);
			
			if (saveFile != null) {
				newUser.setAvatar(saveFile.getName());
			}
		}
		
		User registedUser = userRepository.save(dbContext, newUser);
		if (registedUser == null) {
			return this.register(model, registerDto);
		}
		
		/*ObjectId userId = registedUser.getId();
		String[] searchUsers = registedUser.getSearchValues();
		
		if (company != null) {
			company.setId(userId);
			company.setUserId(userId);
			company.setSearchUsers(searchUsers);
			
			companyRepository.save(dbContext, company);
		}
		
		if (farmer != null) {
			farmer.setId(userId);
			farmer.setUserId(userId);
			farmer.setSearchUsers(searchUsers);
			
			farmerRepository.save(dbContext, farmer);
		}
		
		if (scientist != null) {
			scientist.setId(userId);
			scientist.setUserId(userId);
			scientist.setSearchUsers(searchUsers);
			
			scientistRepository.save(dbContext, scientist);
		}*/
		
		// Send email
		String activeUserUrl = httpRequest.getScheme() + "://" 
				+ httpRequest.getServerName() + ":"
				+ httpRequest.getServerPort() 
				+ httpRequest.getContextPath();
		
		if (!activeUserUrl.endsWith("/")) {
			activeUserUrl += "/";
		}
		
		activeUserUrl += "active?token=" + validToken;
		
		emailSender.sendForActiveUser(activeUserUrl, registedUser);
		
		return "register-success";
	}
	
	@RequestMapping("/active")
	public String activeUser(Model model, @RequestParam("token") String validToken) {
		User user = userRepository.findByValidToken(validToken);
		if (user == null || user.isActive()) {
			return "active-fail";
		}
		
		boolean result = userRepository.activeUser(user.getId());
		if (!result) {
			return "active-fail";
		}
		
		return "active-success";
	}
}
