package vn.vsd.agro.controller;

import java.beans.PropertyEditor;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.BaseItem;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.IdNameEmbed;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Organization;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.IdSelectDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.LinkLandDto;
import vn.vsd.agro.dto.LinkProductDto;
import vn.vsd.agro.dto.LinkScientistDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.ProductSimpleDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ProjectCreateDto;
import vn.vsd.agro.dto.ProjectDto;
import vn.vsd.agro.dto.ProjectSearchDto;
import vn.vsd.agro.dto.ProjectSimpleDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.LandRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.ScientistRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.ProjectValidator;

@Controller
@RequestMapping(value = "/project")
public class ProjectController extends BaseResourceController {
	
	private static final String PAGE_NAME = "project";
	
	private static final String LIST_PAGE = "project-list";
	private static final String FORM_PAGE = "project-form";
	private static final String VIEW_PAGE = "project-detail";
	
	public static final String SEARCH_SESSION_KEY = "ProjectSearch";
	
	@Autowired
	private ProjectRepository mainRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private LandRepository landRepository;
	
	@Autowired
	private ProjectValidator validator;

	@Autowired
	@Qualifier(value = "numberPropertyEditor")
	private PropertyEditor numberPropertyEditor;
	
	@Override
	protected BasicRepository<? extends BaseItem, ObjectId> getMainRepository() {
		return this.mainRepository;
	}

	@Override
	protected File saveUploadImage(HttpSession session, MultipartFile imageFile) {
		return FileUtils.saveUploadProjectImage(session, imageFile);
	}

	@Override
	protected boolean deleteUploadImage(HttpSession session, String imageName) {
		return FileUtils.deleteUploadProjectFile(session, imageName);
	}

	@Override
	protected String gotoListPage() {
		return "redirect:/project";
	}
	
	@Override
	protected String gotoViewPage(String id) {
		return "redirect:/project/" + id + "/view";
	}

	@Override
	protected String gotoViewPage(String id, String target) {
		return this.gotoViewPage(id) + "#" + target;
	}
	
	@Override
	protected String[] getProcessRoles() {
		return new String[] { RoleUtils.ROLE_COMPANY };
	}

	@Override
	protected int getResourceType() {
		return IdSelectDto.LINK_TYPE_PROJECT;
	}

	@Override
	protected boolean joinWhenLogin(User user, int linkType, String resourceId) {
		if (user.isScientist() && linkType == IdSelectDto.LINK_TYPE_SCIENTIST) {
			return this.joinScientist(user, resourceId);
		}
		
		return true;
	}

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (ProjectCreateDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.registerCustomEditor(BigDecimal.class, numberPropertyEditor);
			dataBinder.registerCustomEditor(Integer.class, numberPropertyEditor);
			dataBinder.setValidator(validator);
		}
	}
	
	@RequestMapping(value = { "", "/list", "/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") ProjectSearchDto searchDto)
	{
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null || searchDto.getStatus() == null) {
			Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
			if (lastSearch != null && lastSearch instanceof ProjectSearchDto) {
				searchDto = (ProjectSearchDto) lastSearch;
			} else {
				searchDto = new ProjectSearchDto();
			}
		}
		
		if (searchDto.isEmptyCategoryId()) {
			searchDto.addCategoryId("");
		}
		
		Integer searchStatus = searchDto.getStatus();
		if (searchStatus == null) {
			searchStatus = ConstantUtils.SEARCH_STATUS_ALL;
			
			if (currentUser != null) {
				if (currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()) {
					searchStatus = ConstantUtils.SEARCH_STATUS_PROGRESS;
				} else if (currentUser.isCompany()) {
					searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
				}
			}
			
			searchDto.setStatus(searchStatus);
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.PROJECT_PAGE_PROJECT_LIST_PAGE_SIZE;
		SearchResult<Project> result = this.searchProjects(dbContext, currentUser, searchDto, pageSize);
		
		List<Project> domains = result.getItems();
		int page = result.getPage();
		long itemCount = result.getCount();
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<ProjectSimpleDto> dtos = new ArrayList<ProjectSimpleDto>(domains.size());
		for (Project domain : domains)
		{
			boolean canApprove = this.canApprove(dbContext, domain, currentUser);
			String authorAvatar = null;
			
			ProjectSimpleDto dto = DomainConverter.createProjectSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, 
				CommonCategory.TYPE_PROJECT_TYPE, null, null);
		for (CommonCategory category : categories) {
			categoryMap.put(category.idAsString(), category.getName());
		}
		
		List<Organization> organizations = organizationRepository.getAll(dbContext);
		for (Organization organization : organizations) {
			organizationMap.put(organization.idAsString(), organization.getName());
		}
		
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				null, searchDto.getProvinceId(), null, null, 
				false, true, true, true);
		
		//searchDto.setCountryId(locationInfo.getCurrentCountryId());
		searchDto.setProvinceId(locationInfo.getCurrentProvinceId());
		//searchDto.setDistrictId(locationInfo.getCurrentDistrictId());
		//searchDto.setLocationId(locationInfo.getCurrentLocationId());
		
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive()) {
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_DRAFT, "Chưa gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đã gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã được phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Bị từ chối phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Dự án của công ty");
			} else if (currentUser.isAdministrator() 
					|| currentUser.isDepartmentAdministrator()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đang chờ phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Đã từ chối phê duyệt");
			} else if (currentUser.isFarmer()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_ME_JOINED, "Dự án tôi đã liên kết");
			} else if (currentUser.isScientist()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_ME_JOINED, "Dự án tôi đã tham gia");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC, "Ngày duyệt giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC, "Ngày duyệt tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_DESC, "Ngày bắt đầu giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_ASC, "Ngày bắt đầu tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_DESC, "Ngày kết thúc giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_ASC, "Ngày kết thúc tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên dự án tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên dự án giảm dần");
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		model.addAttribute("categories", categoryMap);
		model.addAttribute("organizations", organizationMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		model.addAttribute("allLocations", locationInfo.getAllLocationMap());
		model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
		model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PROJECT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return LIST_PAGE;
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		ProjectSearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof ProjectSearchDto) {
			searchDto = (ProjectSearchDto) lastSearch;
		} else {
			searchDto = new ProjectSearchDto();
		}
		
		searchDto.setPage(page);
		return list(session, model, searchDto);
	}
	
	@RequestMapping("/{id}/view")
	@Override
	public String detail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		if (StringUtils.isNullOrEmpty(id)) {
			return this.gotoListPage();
		}
		
		User currentUser = this.getCurrentUser(session, model);
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Project domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		String authorAvatar = "";
		
		if (domain.getContact() != null) {
			User company = userRepository.getById(dbContext, domain.getContact().getId(), User.COLUMNNAME_AVATAR);
			if (company != null) {
				authorAvatar = company.getAvatar();
			}
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		ProjectDto dto = DomainConverter.createProjectDto(domain, currentUser, 
				authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
		
		// Link
		List<Link> links = linkRepository.getList(dbContext, 
				Arrays.asList(domainId), null, null, null, null, null, null, null, null, null, null);
		if (links != null && !links.isEmpty()) {
			Map<ObjectId, Link> landMaps = new HashMap<>();						
			Map<ObjectId, Link> productMaps = new HashMap<>();
			Map<ObjectId, Link> scientistMaps = new HashMap<>();
			
			for (Link link : links) {
				if (link.getLinkType() == Link.LINK_TYPE_LAND) {
					landMaps.put(link.getLinkId(), link);
				} else if (link.getLinkType() == Link.LINK_TYPE_PRODUCT) {
					productMaps.put(link.getLinkId(), link);
				} else if (link.getLinkType() == Link.LINK_TYPE_SCIENTIST) {
					scientistMaps.put(link.getLinkId(), link);
				}
			}
			
			// Link land
			List<Land> lands = landRepository.getListByIds(dbContext, landMaps.keySet());
			if (lands != null) {
				for (Land land : lands) {
					Link link = landMaps.get(land.getId());
					LinkLandDto linkLandDto = DomainConverter.createLinkLandDto(link, 
							land, currentUser, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (linkLandDto != null) {
						dto.addLandDto(linkLandDto);
					}
				}
			}
			
			// Link product
			List<Product> products = productRepository.getListByIds(dbContext, productMaps.keySet());
			if (products != null) {
				for (Product product : products) {
					Link link = productMaps.get(product.getId());
					LinkProductDto linkProductDto = DomainConverter.createLinkProductDto(link,
							product, currentUser, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (linkProductDto != null) {
						dto.addProductDto(linkProductDto);
					}
				}
			}
			
			// Link scientist
			List<Scientist> scientists = scientistRepository.getListByIds(dbContext, scientistMaps.keySet());
			if (scientists != null) {
				List<User> users = this.parseUserExtensions(dbContext, scientists, null);
				
				if (users != null) {
					for (User user : users) {
						Scientist scientist = (Scientist) user.getUserExtension();
						
						if (scientist != null) {
							Link link = scientistMaps.get(scientist.getId());
							LinkScientistDto linkScientistDto = DomainConverter.createLinkScientistDto(link, 
									user, scientist, currentUser, ConstantUtils.DEFAULT_DATE_FORMAT);
							if (linkScientistDto != null) {
								dto.addScientistDto(linkScientistDto);
							}
						}
					}
				}
			}
		}

		model.addAttribute("dto", dto);
		model.addAttribute("allRoles", RoleUtils.REGISTRABLE_ROLES);
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PROJECT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return VIEW_PAGE;
	}
	
	@RequestMapping(value = "/{id}/join", method = { RequestMethod.GET, RequestMethod.PUT })
	public String joinScientist(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser != null && currentUser.isActive() && currentUser.isScientist()) {
			this.joinScientist(currentUser, id);
		}
		
		return this.gotoViewPage(id, "addScientist");
	}
	
	private String editForm(Model model, User currentUser, ProjectCreateDto dto, 
			List<IdNameDto> imageDtos, StatusEmbedDto statusDto) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> squareUnitMap = new LinkedHashMap<>();
		Map<String, String> moneyUnitMap = new LinkedHashMap<>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		
		int[] categoryTypes = new int[] { 
				CommonCategory.TYPE_PROJECT_TYPE, 
				CommonCategory.TYPE_SQUARE_UOM, 
				CommonCategory.TYPE_MONEY_UOM 
			};
		List<CommonCategory> categories = categoryRepository.getList(dbContext, 
				categoryTypes, null, null);
		for (CommonCategory category : categories) {
			if (CommonCategory.TYPE_PROJECT_TYPE == category.getType()) {
				categoryMap.put(category.idAsString(), category.getName());
			} else if (CommonCategory.TYPE_SQUARE_UOM == category.getType()) {
				squareUnitMap.put(category.idAsString(), category.getCode());
				
				if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getSquareUnitId())) {
						dto.setSquareUnitId(category.idAsString());
					}
				}
			} else if (CommonCategory.TYPE_MONEY_UOM == category.getType()) {
				moneyUnitMap.put(category.idAsString(), category.getName());
				
				if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getMoneyUnitId())) {
						dto.setMoneyUnitId(category.idAsString());
					}
				}
			}
		}
		
		List<Organization> organizations = organizationRepository.getAll(dbContext);
		for (Organization organization : organizations) {
			organizationMap.put(organization.idAsString(), organization.getName());
		}
		
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
		
		// Contact Location
		LocationInfoDto contactLocationInfo = this.parseLocations(allLocations, 
				dto.getContactCountryId(), dto.getContactProvinceId(),
				dto.getContactDistrictId(), dto.getContactLocationId(), 
				false, false, false, false);
		
		dto.setContactCountryId(contactLocationInfo.getCurrentCountryId());
		dto.setContactProvinceId(contactLocationInfo.getCurrentProvinceId());
		dto.setContactDistrictId(contactLocationInfo.getCurrentDistrictId());
		dto.setContactLocationId(contactLocationInfo.getCurrentLocationId());
		
		// Image
		if (imageDtos == null || statusDto == null) {
			imageDtos = new ArrayList<>();
			
			ObjectId domainId = dbContext.parse(dto.getId(), null);
			if (domainId != null) {
				Project domain = mainRepository.getById(dbContext, domainId, 
						Project.COLUMNNAME_IMAGES, Project.COLUMNNAME_STATUS);
				if (domain != null) {
					statusDto = DomainConverter.createStatusEmbedDto(domain.getStatus());
					
					List<IdNameEmbed> images = domain.getImages();
					if (images != null) {
						for (IdNameEmbed image : images) {
							IdNameDto imageDto = DomainConverter.createIdNameDto(image);
							if (imageDto != null) {
								imageDtos.add(imageDto);
							}
						}
					}
				}
			}
		}
		
		List<LandSimpleDto> landDtos = new ArrayList<>();
		List<ProductSimpleDto> productDtos = new ArrayList<>();
		List<ProfileDto> scientistDtos = new ArrayList<>();
		
		// Land
		List<ObjectId> landIds = dbContext.parseMulti(dto.getLandIds(), true);
		if (landIds != null && !landIds.isEmpty()) {
			List<Land> lands = landRepository.getListByIds(dbContext, landIds);
			
			if (lands != null && !lands.isEmpty()) {
				for (Land land : lands) {
					String authorAvatar = null;
					LandSimpleDto landDto = DomainConverter.createLandSimpleDto(land, 
							currentUser, authorAvatar, false, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (landDto != null) {
						landDtos.add(landDto);
					}
				}
			}
		}
		
		// Product
		List<ObjectId> productIds = dbContext.parseMulti(dto.getProductIds(), true);
		if (productIds != null && !productIds.isEmpty()) {
			List<Product> products = productRepository.getListByIds(dbContext, productIds);
			
			if (products != null && !products.isEmpty()) {
				for (Product product : products) {
					String authorAvatar = null;
					ProductSimpleDto productDto = DomainConverter.createProductSimpleDto(product, 
							currentUser, authorAvatar, false, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (productDto != null) {
						productDtos.add(productDto);
					}
				}
			}
		}
		
		// Scientist
		List<ObjectId> scientistIds = dbContext.parseMulti(dto.getScientistIds(), true);
		if (scientistIds != null && !scientistIds.isEmpty()) {
			List<Scientist> scientists = scientistRepository.getListByIds(dbContext, scientistIds);
			
			if (scientists != null && !scientists.isEmpty()) {
				List<User> users = this.parseUserExtensions(dbContext, scientists, null);
				
				if (users != null) {
					for (User user : users) {
						Scientist scientist = (Scientist) user.getUserExtension();
						ProfileDto scientistDto = DomainConverter.createProfileDto(user, 
								null, null, scientist, ConstantUtils.DEFAULT_DATE_FORMAT);
						
						if (scientistDto != null) {
							scientistDtos.add(scientistDto);
						}
					}
				}
			}
		}
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PROJECT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		model.addAttribute("categories", categoryMap);
		model.addAttribute("squareUnits", squareUnitMap);
		model.addAttribute("moneyUnits", moneyUnitMap);
		model.addAttribute("organizations", organizationMap);
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		model.addAttribute("contactLocations", contactLocationInfo.getLocationMap());
		model.addAttribute("contactDistricts", contactLocationInfo.getDistrictMap());
		model.addAttribute("contactProvinces", contactLocationInfo.getProvinceMap());
		model.addAttribute("contactCountries", contactLocationInfo.getCountryMap());
		model.addAttribute("allLocations", locationInfo.getAllLocationMap());
		model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
		model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		
		List<MultipartFile> imageFiles = dto.getImageFiles();
		if (imageFiles == null || imageFiles.isEmpty()) {
			imageFiles = new ArrayList<>();
			imageFiles.add(null);
			
			dto.setImageFiles(imageFiles);
		}
		
		model.addAttribute("dto", dto);
		model.addAttribute("imageDtos", imageDtos);
		model.addAttribute("statusDto", statusDto);
		
		model.addAttribute("landDtos", landDtos);
		model.addAttribute("productDtos", productDtos);
		model.addAttribute("scientistDtos", scientistDtos);
		model.addAttribute("pageName", PAGE_NAME);
		
		return FORM_PAGE;
	}

	@RequestMapping("/create")
	public String create(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isCompany()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ProjectCreateDto createDto = new ProjectCreateDto();
		this.copyBaseResourceCreateDto(dbContext, currentUser, createDto);
		createDto.setClosed(false);
		
		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();
		statusDto.setStatus(Project.APPROVE_STATUS_PENDING);
		
		return this.editForm(model, currentUser, createDto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/edit")
	public String edit(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isCompany()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Project domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		if (domain.isApproved()) {
			return this.gotoViewPage(id);
		}
		
		/*if (domain.getContact() != null && !dbContext.equals(domain.getContact().getId(), currentUser.getId())) {
			return this.gotoViewPage(id);
		}*/
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		
		ProjectCreateDto dto = DomainConverter.createProjectCreateDto(domain, currentUser, 
				canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
		if (dto == null) {
			return this.gotoListPage();
		}
		
		StatusEmbedDto statusDto = DomainConverter.createStatusEmbedDto(domain.getStatus());
		List<IdNameDto> imageDtos = new ArrayList<>();
		
		List<IdNameEmbed> images = domain.getImages();
		if (images != null) {
			for (IdNameEmbed image : images) {
				IdNameDto imageDto = DomainConverter.createIdNameDto(image);
				if (imageDto != null) {
					imageDtos.add(imageDto);
				}
			}
		}
		
		List<Link> links = linkRepository.getList(dbContext, 
				Arrays.asList(domainId), null, null, null, null, null, null, null, null, null, null);
		if (links != null && !links.isEmpty()) {
			for (Link link : links) {
				if (link.getLinkType() == Link.LINK_TYPE_LAND) {
					if (domain.isRequireLand()) {
						dto.addLandId(link.getLinkId().toString());
					}								
				} else if (link.getLinkType() == Link.LINK_TYPE_PRODUCT) {
					if (domain.isRequireProduct()) {
						dto.addProductId(link.getLinkId().toString());
					}
				} else if (link.getLinkType() == Link.LINK_TYPE_SCIENTIST) {
					if (domain.isRequireScientist()) {
						dto.addScientistId(link.getLinkId().toString());
					}
				}
			}
		}
		
		return this.editForm(model, currentUser, dto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/delete")
	public String delete(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isCompany()) {
			return this.gotoNoPermission();
		}

		if (id != null) {
			IContext<ObjectId> dbContext = this.getDbContext(currentUser);
			ObjectId domainId = dbContext.parse(id, null);
			
			if (domainId != null) {
				this.mainRepository.delete(dbContext, domainId, currentUser.getId());
			}
		}

		return this.gotoListPage();
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated ProjectCreateDto dto, 
			BindingResult result, final RedirectAttributes redirectAttributes)
	{
		return this.saveEdit(session, model, dto, dto.getId(), result, redirectAttributes);
	}
	
	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String saveEdit(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated ProjectCreateDto dto, 
			@PathVariable(name = "id", required = true) String id,
			BindingResult result, final RedirectAttributes redirectAttributes)
	{
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isCompany()) {
			return this.gotoNoPermission();
		}

		BigDecimal square = dto.getSquare();
		if (square != null && square.signum() < 0) {
			this.addModelError(result, "dto", "square", square, "project.message.invalid.square");
		}
		
		BigDecimal money = dto.getMoney();
		if (money != null && money.signum() < 0) {
			this.addModelError(result, "dto", "money", money, "project.message.invalid.money");
		}
		
		Integer employees = dto.getEmployees();
		if (employees != null && employees.intValue() < 0) {
			this.addModelError(result, "dto", "employees", employees, "project.message.invalid.employees");
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ObjectId> categoryIds = dbContext.parseMulti(dto.getCategoryIds(), true);
		if (categoryIds == null || categoryIds.isEmpty()) {
			this.addModelError(result, "dto", "categoryIds", dto.getCategoryIds(), "project.message.not.empty.category");
		}
		
		ObjectId locationId = dbContext.parse(dto.getLocationId(), null);
		if (locationId == null) {
			this.addModelError(result, "dto", "locationId", null, "project.message.invalid.location");
		}
		
		SimpleDate startDate = null;
		SimpleDate endDate = null;
		
		String startDateValue = dto.getStartDate();
		if (!StringUtils.isNullOrEmpty(startDateValue)) {
			startDate = SimpleDate.parseDate(startDateValue, ConstantUtils.DEFAULT_DATE_FORMAT, null);
		}
		
		String endDateValue = dto.getEndDate();
		if (!StringUtils.isNullOrEmpty(endDateValue)) {
			endDate = SimpleDate.parseDate(endDateValue, ConstantUtils.DEFAULT_DATE_FORMAT, null);
		}
		
		if (startDate == null) {
			this.addModelError(result, "dto", "startDate", startDateValue, "project.message.invalid.date");
		} else if (endDate == null) {
			this.addModelError(result, "dto", "startDate", startDateValue, "project.message.invalid.date");
		} else if (startDate.compareTo(endDate) > 0) {
			this.addModelError(result, "dto", "startDate", startDateValue, "project.message.invalid.date");
		}
		
		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editForm(model, currentUser, dto, null, null);
		}
		
		ObjectId domainId = dbContext.parse(id, null);
		String name = StringUtils.trimText(dto.getName());
		
		if (this.mainRepository.nameExists(dbContext, name, domainId)) {
			this.addModelError(result, "dto", "name", name, "project.message.exists.name");
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		Project domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);
			
			if (domain == null) {
				ObjectError error = new ObjectError("project", "project.message.not.found");
	            result.addError(error);
	            
	            return this.editForm(model, currentUser, dto, null, null);
			}
			
			if (domain.isApproved()) {
				return this.gotoViewPage(id);
			}
			
			/*if (domain.getContact() != null && !dbContext.equals(domain.getContact().getId(), currentUser.getId())) {
				return this.gotoViewPage(id);
			}*/
		} else {
			domain = new Project();
		}
		
		List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, categoryIds);
		if (categories == null || categories.isEmpty()) {
			this.addModelError(result, "dto", "categoryIds", dto.getCategoryIds(), "project.message.not.empty.category");
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		ContactEmbed company = this.createContactDomain(dbContext, currentUser, result, dto);
		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editForm(model, currentUser, dto, null, null);
		}
		
		domain.setContact(company);
		
		domain.setName(name);
		domain.setAddress(dto.getAddress());
		domain.setDescription(dto.getDescription());
		domain.setSquare(square);
		domain.setMoney(money);
		domain.setEmployees(employees);
		domain.setClosed(dto.isClosed());
		domain.setApproveName(dto.getApproveName());
		domain.setRequireLand(dto.isRequireLand());
		domain.setRequireProduct(dto.isRequireProduct());
		domain.setRequireScientist(dto.isRequireScientist());
		domain.setStartDate(startDate);
		domain.setEndDate(endDate);
		
		if (domain.isNew() 
				|| domain.getLocation() == null 
				|| !dbContext.equals(locationId, domain.getLocation().getId())) {
			Location location = locationRepository.getById(dbContext, locationId);
			if (location == null) {
				this.addModelError(result, "dto", "locationId", locationId.toString(), "project.message.invalid.location");
	            
	            return this.editForm(model, currentUser, dto, null, null);
			}
			
			LocationEmbed locationEmbed = new LocationEmbed(location);
			domain.setLocation(locationEmbed);
		}
		
		domain.clearCategories();
		for (CommonCategory category : categories) {
			IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(), category.getName());
			domain.addCategory(categoryEmbed);
		}
		
		ObjectId squareUnitId = dbContext.parse(dto.getSquareUnitId(), null);
		if (squareUnitId == null) {
			domain.setSquareUnit(null);
		} else if (domain.isNew()
				|| domain.getSquareUnit() == null
				|| !dbContext.equals(squareUnitId, domain.getSquareUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, squareUnitId);
			if (category == null) {
				domain.setSquareUnit(null);
			} else {
				IdCodeNameEmbed squareUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setSquareUnit(squareUnit);
			}
		}
		
		ObjectId moneyUnitId = dbContext.parse(dto.getMoneyUnitId(), null);
		if (moneyUnitId == null) {
			domain.setMoneyUnit(null);
		} else if (domain.isNew()
				|| domain.getMoneyUnit() == null
				|| !dbContext.equals(moneyUnitId, domain.getMoneyUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, moneyUnitId);
			if (category == null) {
				domain.setMoneyUnit(null);
			} else {
				IdCodeNameEmbed moneyUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setMoneyUnit(moneyUnit);
			}
		}
		
		this.updateBaseItem(session, dbContext, currentUser, domain, dto);
		
		Project savedDomain = this.mainRepository.save(dbContext, domain);
		
		// Update links
		ObjectId projectId = savedDomain.getId();
		ObjectId companyId = company.getId();
		
		Set<ObjectId> removeLinkIds = new HashSet<>();
		List<Link> newLinks = new ArrayList<>();
		
		Map<ObjectId, Link> currentLinkMap = new HashMap<>();
		if (domainId != null) {
			List<Link> links = linkRepository.getList(dbContext, 
					Arrays.asList(domainId), null, null, null, null, null, null, null, null, null, null);
			if (links != null) {
				for (Link link : links) {
					removeLinkIds.add(link.getId());
					currentLinkMap.put(link.getLinkId(), link);
				}
			}
		}
		
		List<ObjectId> landIds = dbContext.parseMulti(dto.getLandIds(), true);
		List<ObjectId> productIds = dbContext.parseMulti(dto.getProductIds(), true);
		List<ObjectId> scientistIds = dbContext.parseMulti(dto.getScientistIds(), true);
		
		// Link land
		if (domain.isRequireLand() && landIds != null && !landIds.isEmpty()) {
			this.linkLands(dbContext, companyId, projectId, 
					landIds, currentLinkMap, 
					newLinks, removeLinkIds);
		}
		
		// Link product
		if (domain.isRequireProduct() && productIds != null && !productIds.isEmpty()) {
			this.linkProducts(dbContext, companyId, projectId, 
					productIds, currentLinkMap, 
					newLinks, removeLinkIds);
		}
		
		// Link scientist
		if (domain.isRequireScientist() && scientistIds != null && !scientistIds.isEmpty()) {
			this.linkScientists(dbContext, companyId, projectId, 
					scientistIds, currentLinkMap, 
					newLinks, removeLinkIds);
		}
		
		if (!removeLinkIds.isEmpty()) {
			linkRepository.deleteBatch(dbContext, removeLinkIds);
		}
		
		if (!newLinks.isEmpty()) {
			linkRepository.saveBatch(dbContext, newLinks);
		}
		
		return this.gotoListPage();
	}
	
	private boolean joinScientist(User currentUser, String projectId) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(projectId, null);
		
		if (domainId == null) {
			return false;
		}
		
		Project domain = mainRepository.getById(dbContext, domainId, Project.COLUMNNAME_CONTACT);
		if (domain == null) {
			return false;
		}
		
		ObjectId companyId = null;
		if (domain.getContact() != null) {
			companyId = domain.getContact().getId(); 
		}
		
		ObjectId userId = currentUser.getId();
		Scientist scientist = scientistRepository.getByUserId(dbContext, userId);
		
		if (scientist == null) {
			return false;
		}
		
		List<Link> links = linkRepository.getList(dbContext, 
				Arrays.asList(domainId), null, null, null, 
				Arrays.asList(scientist.getId()), null, 
				Arrays.asList(userId), null, 
				Arrays.asList(Link.LINK_TYPE_SCIENTIST), null, null);
		
		if (links == null || links.isEmpty()) {
			StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, Link.STATUS_PENDING);
			
			Link newLink = new Link();
			newLink.setLinkType(Link.LINK_TYPE_SCIENTIST);
			newLink.setProjectId(domainId);
			newLink.setCompanyId(companyId);
			newLink.setLinkId(scientist.getId());
			newLink.setLinkOwnerId(userId);
			newLink.setStatus(newStatus);
			
			linkRepository.save(dbContext, newLink);
		}
		
		return true;
	}
}
