package vn.vsd.agro.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.Company;
import vn.vsd.agro.domain.Farmer;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.ScientistField;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.ScientistMajorEmbed;
import vn.vsd.agro.dto.IdCodeNameDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.ProfileCreateDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ScientistMajorDto;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.CompanyRepository;
import vn.vsd.agro.repository.FarmerRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.ScientistFieldRepository;
import vn.vsd.agro.repository.ScientistRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.ProfileValidator;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController extends BaseController {
	
	private static final String FORM_PAGE = "profile-form";
	private static final String VIEW_PAGE = "profile-detail";
	
	@Autowired
	private ProfileValidator profileValidator;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ScientistFieldRepository scientistFieldRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private FarmerRepository farmerRepository;
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (target.getClass() == ProfileCreateDto.class) {
			dataBinder.setValidator(this.profileValidator);
		}
	}
	
	private String viewProfile(HttpSession session, Model model, 
			IContext<ObjectId> dbContext, User currentUser, User user) {
		ObjectId userId = user.getId();
		
		Company company = null;
		Farmer farmer = null;
		Scientist scientist = null;
		
		if (user.isCompany()) {
			company = companyRepository.getByUserId(dbContext, userId);
		}
		
		if (user.isFarmer()) {
			farmer = farmerRepository.getByUserId(dbContext, userId);
		}
		
		if (user.isScientist()) {
			scientist = scientistRepository.getByUserId(dbContext, userId);
		}
		
		ProfileDto profileDto = DomainConverter.createProfileDto(user, 
				company, farmer, scientist, ConstantUtils.DEFAULT_DATE_FORMAT);
		if (profileDto != null) {
			if (currentUser != null) {
				profileDto.setEditable(dbContext.equals(userId, currentUser.getId()));
			}
			
			model.addAttribute("profile", profileDto);
		}
		
		return VIEW_PAGE;		
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String profile(HttpSession session, Model model) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		return viewProfile(session, model, dbContext, currentUser, currentUser);
	}
	
	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	public String profile(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId userId = dbContext.parse(id, null);
		if (userId == null) {
			return this.gotoHome();
		}
		
		User user = userRepository.getById(dbContext, userId);
		if (user == null || !user.isActive()) {
			return this.gotoHome();
		}
		
		return viewProfile(session, model, dbContext, currentUser, user);
	}
	
	private String formProfile(IContext<ObjectId> dbContext, User currentUser, 
			Model model, ProfileCreateDto dto)
	{
		dto.setCompany(currentUser.isCompany());
		dto.setFarmer(currentUser.isFarmer());
		dto.setScientist(currentUser.isScientist());
		
		Map<String, String> companyTypes = new LinkedHashMap<>();
		Map<String, String> companyFields = new LinkedHashMap<>();
		Map<String, String> literacies = new LinkedHashMap<>();
		Map<Integer, String> genderValues = new LinkedHashMap<>();
		List<ScientistMajorDto> scientistMajors = new ArrayList<>();
		
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
		
		// Scientist Fields
		List<ScientistField> scientistFields = scientistFieldRepository.getAll(dbContext);
		Map<String, ScientistMajorDto> scientistMajorMaps = new HashMap<>();
		if (scientistFields != null) {
			
			for (ScientistField scientistField : scientistFields) {
				IdCodeNameEmbed scientistMajor = scientistField.getMajor();
				
				if (scientistMajor != null) {
					String majorId = scientistMajor.idAsString();
					ScientistMajorDto scientistMajorDto = scientistMajorMaps.get(majorId);
					
					if (!dto.containsFieldOther(majorId)) {
						dto.setFieldOther(majorId, "");
					}
					
					if (!dto.containsFieldDescription(majorId)) {
						dto.setFieldDescription(majorId, "");
					}
					
					if (scientistMajorDto == null) {
						scientistMajorDto = new ScientistMajorDto();
						scientistMajorDto.setId(scientistMajor.idAsString());
						scientistMajorDto.setCode(scientistMajor.getCode());
						scientistMajorDto.setName(scientistMajor.getName());
						
						scientistMajorMaps.put(majorId, scientistMajorDto);
					}
					
					IdCodeNameDto scientistFieldDto = new IdCodeNameDto(scientistField.idAsString(), scientistField.getCode(), scientistField.getName());
					scientistMajorDto.addScientistField(scientistFieldDto);
				}
			}
		}
		
		// Common category
		int[] categoryTypes = new int[] {
				CommonCategory.TYPE_COMPANY_TYPE,
				CommonCategory.TYPE_COMPANY_FIELD,
				CommonCategory.TYPE_SCIENTIST_MAJOR,
				CommonCategory.TYPE_LITERACY
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		if (categories != null) {
			for (CommonCategory category : categories) {
				int type = category.getType();
				if (CommonCategory.TYPE_COMPANY_TYPE == type) {
					companyTypes.put(category.idAsString(), category.getName());
				} else if (CommonCategory.TYPE_COMPANY_FIELD == type) {
					companyFields.put(category.idAsString(), category.getName());
				} else if (CommonCategory.TYPE_LITERACY == type) {
					literacies.put(category.idAsString(), category.getName());
				} else if (CommonCategory.TYPE_SCIENTIST_MAJOR == type) {
					String majorId = category.idAsString();
					ScientistMajorDto scientistMajor = scientistMajorMaps.get(majorId);
					
					if (scientistMajor != null) {
						scientistMajors.add(scientistMajor);
					}
				}
			}
		}
		
		genderValues.put(ConstantUtils.GENDER_MALE, "Nam");
		genderValues.put(ConstantUtils.GENDER_FEMALE, "Nữ");
		genderValues.put(ConstantUtils.GENDER_OTHER, "Khác");
		
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
		
		model.addAttribute("companyTypes", companyTypes);
		model.addAttribute("companyFields", companyFields);
		model.addAttribute("literacies", literacies);
		model.addAttribute("scientistMajors", scientistMajors);
		model.addAttribute("allGenders", genderValues);
		
		model.addAttribute("profileDto", dto);
		
		return FORM_PAGE;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editProfile(HttpSession session, Model model) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId userId = currentUser.getId();
		
		ProfileCreateDto dto = new ProfileCreateDto();
		dto.setName(currentUser.getName());
		dto.setRoles(new ArrayList<>(currentUser.getRoles().keySet()));
		
		dto.setAvatar(currentUser.getAvatar());
		dto.setEmail(currentUser.getEmail());
		dto.setCompanyPhone(currentUser.getCompanyPhone());
		dto.setHomePhone(currentUser.getHomePhone());
		dto.setMobilePhone(currentUser.getMobilePhone());
		dto.setFax(currentUser.getFax());
		//dto.setContactName(currentUser.getContactName());
		
		dto.setAddress(currentUser.getAddress());
		
		LocationEmbed location = currentUser.getLocation();
		if (location != null) {
			dto.setLocationId(location.idAsString());
		}
		
		// Company
		Company company = companyRepository.getByUserId(dbContext, userId);
		if (company != null) {
			dto.setTaxCode(company.getTaxCode());
			dto.setDescription(company.getDescription());
			
			if (company.getType() != null) {
				dto.setTypeId(company.getType().idAsString());
			}
			
			List<IdCodeNameEmbed> companyFields = company.getCompanyFields();
			if (companyFields != null) {
				for (IdCodeNameEmbed companyField : companyFields) {
					if (companyField != null) {
						dto.addCompanyFieldId(companyField.idAsString());
					}
				}
			}
		}
		
		// Farmer
		Farmer farmer = farmerRepository.getByUserId(dbContext, userId);
		if (farmer != null) {
			dto.setGender(farmer.getGender());
			
			if (farmer.getBirthDay() != null) {
	        	dto.setBirthDay(farmer.getBirthDay().format(ConstantUtils.DEFAULT_DATE_FORMAT));
	        }
		} 
		
		// Scientist
		Scientist scientist = scientistRepository.getByUserId(dbContext, userId);
		if (scientist != null) {
			dto.setGender(scientist.getGender());
			
			if (scientist.getBirthDay() != null) {
	        	dto.setBirthDay(scientist.getBirthDay().format(ConstantUtils.DEFAULT_DATE_FORMAT));
	        }
			
			dto.setTitle(scientist.getTitle());
			dto.setPosition(scientist.getPosition());
			dto.setWorkplace(scientist.getWorkplace());
			
			List<ScientistMajorEmbed> scientistMajors = scientist.getScientistMajors();
			if (scientistMajors != null) {
				for (ScientistMajorEmbed scientistMajor : scientistMajors) {
					if (scientistMajor != null) {
						String majorId = scientistMajor.idAsString();
						
						dto.addScientistMajorId(majorId);
						dto.setFieldOther(majorId, scientistMajor.getFieldOther());
						dto.setFieldDescription(majorId, scientistMajor.getFieldDescription());
						
						List<IdCodeNameEmbed> scientistFields = scientistMajor.getScientistFields();
						if (scientistFields != null) {
							for (IdCodeNameEmbed scientistField : scientistFields) {
								if (scientistField != null) {
									dto.addScientistFieldId(scientistField.idAsString());
								}
							}
						}
					}
				}
			}
			
			List<IdCodeNameEmbed> literacies = scientist.getLiteracies();
			if (literacies != null) {
				for (IdCodeNameEmbed literacy : literacies) {
					if (literacy != null) {
						dto.addLiteracyId(literacy.idAsString());
					}
				}
			}
		}
		
		return this.formProfile(dbContext, currentUser, model, dto);
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProfile(HttpSession session, Model model,
			@ModelAttribute("profileDto") @Validated ProfileCreateDto profileDto, 
			BindingResult result, final RedirectAttributes redirectAttributes) {

		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (result.hasErrors()) {
			return this.formProfile(dbContext, currentUser, model, profileDto);
		}
		
		ObjectId userId = currentUser.getId();
		
		String email = profileDto.getEmail().trim();
		String mobilePhone = profileDto.getMobilePhone().trim();
		String companyPhone = profileDto.getCompanyPhone().trim();
		String homePhone = profileDto.getHomePhone().trim();
		
		if (userRepository.existEmail(dbContext, email, userId)) {
			ObjectError error = new ObjectError("email", "profile.message.exists.email");
            result.addError(error);
		}
		
		boolean validPhone = false;
		if (!StringUtils.isNullOrEmpty(mobilePhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(dbContext, mobilePhone, userId)) {
				ObjectError error = new ObjectError("mobilePhone", "profile.message.exists.phone");
	            result.addError(error);
			}
		}
		
		if (!StringUtils.isNullOrEmpty(companyPhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(dbContext, companyPhone, userId)) {
				ObjectError error = new ObjectError("companyPhone", "profile.message.exists.phone");
	            result.addError(error);
			}
		}
		
		if (!StringUtils.isNullOrEmpty(homePhone)) {
			validPhone = true;
			
			if (userRepository.existPhone(dbContext, homePhone, userId)) {
				ObjectError error = new ObjectError("homePhone", "profile.message.exists.phone");
	            result.addError(error);
			}
		}
		
		if (!validPhone) {
			ObjectError error = new ObjectError("mobilePhone", "profile.message.exists.phone");
            result.addError(error);
		}
		
		ObjectId locationId = dbContext.parse(profileDto.getLocationId(), null);
		if (locationId == null) {
			ObjectError error = new ObjectError("locationId", "profile.message.invalid.location");
            result.addError(error);
		}
		
		Location location = locationRepository.getById(dbContext, locationId);
		if (location == null) {
			ObjectError error = new ObjectError("locationId", "profile.message.invalid.location");
            result.addError(error);
		}
		
		if (result.hasErrors()) {
			return this.formProfile(dbContext, currentUser, model, profileDto);
		}
		
		
		currentUser.setName(profileDto.getName());
		currentUser.setEmail(email);
		currentUser.setMobilePhone(mobilePhone);
		currentUser.setCompanyPhone(companyPhone);
		currentUser.setHomePhone(homePhone);
		currentUser.setFax(profileDto.getFax());
		//currentUser.setContactName(profileDto.getContactName());
		
		currentUser.setAddress(profileDto.getAddress());
		currentUser.setLocation(new LocationEmbed(location));
		
		Map<String, Boolean> currentRoles = currentUser.getRoles();
		
		currentUser.clearRegistrableRoles();
		currentUser.addRole(RoleUtils.ROLE_USER, false);
		
		for (String role : profileDto.getRoles()) {
			Boolean canApprove = currentRoles.get(role);
			currentUser.addRole(role, canApprove == null ? false : canApprove);
		}
		
		Company company = null;
		Scientist scientist = null;
		Farmer farmer = null;
		
		if (currentUser.isCompany()) {
			company = companyRepository.getByUserId(dbContext, userId);
			if (company == null) {
				company = new Company();
			}
			
			company.setTaxCode(profileDto.getTaxCode());
			company.setDescription(profileDto.getDescription());
			company.setActive(true);
			
			company.clearCompanyFields();
			ObjectId typeId = dbContext.parse(profileDto.getTypeId(), null);
			if (typeId != null) {
				CommonCategory companyType = categoryRepository.getById(dbContext, typeId);
				
				if (companyType != null && companyType.getType() == CommonCategory.TYPE_COMPANY_TYPE) {
					company.setType(new IdCodeNameEmbed(companyType, companyType.getCode(), companyType.getName()));
				}
			}
			
			List<ObjectId> companyFieldIds = dbContext.parseMulti(profileDto.getCompanyFieldIds(), true);
			if (companyFieldIds != null && !companyFieldIds.isEmpty()) {
				List<CommonCategory> companyFields = categoryRepository.getListByIds(dbContext, companyFieldIds);
				
				if (companyFields != null) {
					for (CommonCategory companyField : companyFields) {
						company.addCompanyField(new IdCodeNameEmbed(companyField, companyField.getCode(), companyField.getName()));
					}
				}
			}
		} else if (currentRoles.containsKey(RoleUtils.ROLE_COMPANY)) {
			companyRepository.setActiveByUserId(dbContext, userId, false);
		}
		
		SimpleDate birthDay = null;
		if (!StringUtils.isNullOrEmpty(profileDto.getBirthDay())) {
			birthDay = SimpleDate.parseDate(profileDto.getBirthDay(), ConstantUtils.DEFAULT_DATE_FORMAT, null);
		}
		
		if (currentUser.isFarmer()) {
			farmer = farmerRepository.getByUserId(dbContext, userId);
			if (farmer == null) {
				farmer = new Farmer();
			}
			
			farmer.setGender(profileDto.getGender());
			farmer.setBirthDay(birthDay);
			farmer.setActive(true);
		} else if (currentRoles.containsKey(RoleUtils.ROLE_FARMER)) {
			farmerRepository.setActiveByUserId(dbContext, userId, false);
		}
		
		if (currentUser.isScientist()) {
			scientist = scientistRepository.getByUserId(dbContext, userId);
			if (scientist == null) {
				scientist = new Scientist();
			}
			
			scientist.setGender(profileDto.getGender());
			scientist.setBirthDay(birthDay);
			scientist.setTitle(profileDto.getTitle());
			scientist.setPosition(profileDto.getPosition());
			scientist.setWorkplace(profileDto.getWorkplace());
			scientist.setActive(true);
			
			scientist.clearScientistMajors();
			scientist.clearLiteracies();
			
			// 
			List<ObjectId> scientistMajorIds = dbContext.parseMulti(profileDto.getScientistMajorIds(), true);
			if (scientistMajorIds != null && !scientistMajorIds.isEmpty()) {
				List<CommonCategory> scientistMajors = categoryRepository.getListByIds(dbContext, scientistMajorIds);
				
				if (scientistMajors != null && !scientistMajors.isEmpty()) {
					for (CommonCategory scientistMajor : scientistMajors) {
						ScientistMajorEmbed majorEmbed = new ScientistMajorEmbed(scientistMajor);
						scientist.addScientistMajor(majorEmbed);
					}
					
					List<ObjectId> scientistFieldIds = dbContext.parseMulti(profileDto.getScientistFieldIds(), true);
					
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
					
					Map<String, String> fieldOthers = profileDto.getFieldOthers();
					if (fieldOthers != null) {
						for (String majorId : fieldOthers.keySet()) {
							ObjectId scientistMajorId = dbContext.parse(majorId, null);
							String scientistFieldName = fieldOthers.get(majorId);
							
							if (scientistMajorId != null && !StringUtils.isNullOrEmpty(scientistFieldName)) {
								scientist.addOtherScientistField(scientistMajorId, scientistFieldName);
							}
						}
					}
					
					Map<String, String> fieldDescriptions = profileDto.getFieldDescriptions();
					if (fieldDescriptions != null) {
						for (String majorId : fieldOthers.keySet()) {
							ObjectId scientistMajorId = dbContext.parse(majorId, null);
							String scientistFieldDescription = fieldDescriptions.get(majorId);
							
							if (scientistMajorId != null && !StringUtils.isNullOrEmpty(scientistFieldDescription)) {
								scientist.addScientistFieldDescription(scientistMajorId, scientistFieldDescription);
							}
						}
					}
				}
			}
			
			List<ObjectId> literacyIds = dbContext.parseMulti(profileDto.getLiteracyIds(), true);
			if (literacyIds != null && !literacyIds.isEmpty()) {
				List<CommonCategory> literacies = categoryRepository.getListByIds(dbContext, literacyIds);
				if (literacies != null) {
					for (CommonCategory literacy : literacies) {
						IdCodeNameEmbed literacyEmbed = new IdCodeNameEmbed(literacy, literacy.getCode(), literacy.getName());
						scientist.addLiteracy(literacyEmbed);
					}
				}
			}
		} else if (currentRoles.containsKey(RoleUtils.ROLE_SCIENTIST)) {
			scientistRepository.setActiveByUserId(dbContext, userId, false);
		}
		
		MultipartFile imageFile = profileDto.getImageFile();
		if (imageFile != null && !imageFile.isEmpty()) {
			// Remove current avatar 
			String currentAvatar = currentUser.getAvatar();
			if (!StringUtils.isNullOrEmpty(currentAvatar)) {
				FileUtils.deleteUploadAvatarFile(session, currentAvatar);
				currentUser.setAvatar(null);
			}
			
			File saveFile = FileUtils.saveUploadAvatarImage(session, imageFile);
			if (saveFile != null) {
				currentUser.setAvatar(saveFile.getName());
			}
		}
		
		User registedUser = userRepository.save(dbContext, currentUser);
		if (registedUser == null) {
			return this.formProfile(dbContext, currentUser, model, profileDto);
		}
		
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
		}
		
		return this.viewProfile(session, model, dbContext, currentUser, currentUser);
	}
}
