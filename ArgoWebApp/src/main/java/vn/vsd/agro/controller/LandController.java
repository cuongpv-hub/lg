package vn.vsd.agro.controller;

import java.beans.PropertyEditor;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.IdSelectDto;
import vn.vsd.agro.dto.LandCreateDto;
import vn.vsd.agro.dto.LandDto;
import vn.vsd.agro.dto.LandSearchDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.LinkProjectDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.LandRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.LandValidator;

@Controller
@RequestMapping(value = "/land")
public class LandController extends BaseResourceController {
	private static final String PAGE_NAME = "land";
	
	private static final String LIST_PAGE = "land-list";
	private static final String FORM_PAGE = "land-form";
	private static final String VIEW_PAGE = "land-detail";

	public static final String SEARCH_SESSION_KEY = "landSearch";

	@Autowired
	private LandRepository mainRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LandValidator validator;

	@Autowired
	@Qualifier(value = "numberPropertyEditor")
	private PropertyEditor numberPropertyEditor;
	
	@Override
	protected BasicRepository<? extends BaseItem, ObjectId> getMainRepository() {
		return this.mainRepository;
	}

	@Override
	protected File saveUploadImage(HttpSession session, MultipartFile imageFile) {
		return FileUtils.saveUploadLandImage(session, imageFile);
	}

	@Override
	protected boolean deleteUploadImage(HttpSession session, String imageName) {
		return FileUtils.deleteUploadLandFile(session, imageName);
	}

	@Override
	protected String gotoListPage() {
		return "redirect:/land";
	}

	@Override
	protected String gotoViewPage(String id) {
		return "redirect:/land/" + id + "/view";
	}

	@Override
	protected String gotoViewPage(String id, String target) {
		return this.gotoViewPage(id) + "#" + target;
	}

	@Override
	protected String[] getProcessRoles() {
		return new String[] { RoleUtils.ROLE_FARMER };
	}

	@Override
	protected int getResourceType() {
		return IdSelectDto.LINK_TYPE_LAND;
	}
	
	@Override
	protected boolean joinWhenLogin(User user, int linkType, String resourceId) {
		return true;
	}

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (LandCreateDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.registerCustomEditor(BigDecimal.class, numberPropertyEditor);
			dataBinder.setValidator(validator);
		}
	}

	@RequestMapping(value = { "", "/list", "/search" },
			method = { RequestMethod.GET, RequestMethod.POST })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") LandSearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null || searchDto.getStatus() == null) {
			Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
			if (lastSearch != null && lastSearch instanceof LandSearchDto) {
				searchDto = (LandSearchDto) lastSearch;
			} else {
				searchDto = new LandSearchDto();
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
				} else if (currentUser.isFarmer()) {
					searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
				}
			}
			
			searchDto.setStatus(searchStatus);
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.PROJECT_PAGE_PROJECT_LIST_PAGE_SIZE;
		SearchResult<Land> result = this.searchLands(dbContext, currentUser, searchDto, pageSize);
		
		List<Land> domains = result.getItems();
		int page = result.getPage();
		long itemCount = result.getCount();
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<LandSimpleDto> dtos = new ArrayList<LandSimpleDto>(domains.size());
		for (Land domain : domains)
		{
			String authorAvatar = null;
			boolean canApprove = this.canApprove(dbContext, domain, currentUser);
			
			LandSimpleDto dto = DomainConverter.createLandSimpleDto(domain, currentUser, 
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
				CommonCategory.TYPE_LAND_TYPE, null, null);
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
			if (currentUser.isFarmer()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_DRAFT, "Chưa gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đã gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã được phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Bị từ chối phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Thửa đất của cá nhân");
			} else if (currentUser.isAdministrator() 
					|| currentUser.isDepartmentAdministrator()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đang chờ phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Đã từ chối phê duyệt");
			} else if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_ME_JOINED, "Thửa đất tôi đã liên kết");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC, "Ngày duyệt giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC, "Ngày duyệt tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_DESC, "Ngày bắt đầu giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_ASC, "Ngày bắt đầu tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_DESC, "Ngày kết thúc giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_ASC, "Ngày kết thúc tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên thửa đất tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên thửa đất giảm dần");
		
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
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_LAND);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return LIST_PAGE;
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		LandSearchDto searchDto = null;

		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof LandSearchDto) {
			searchDto = (LandSearchDto) lastSearch;
		} else {
			searchDto = new LandSearchDto();
		}

		searchDto.setPage(page);
		return list(session, model, searchDto);
	}

	@RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
	@Override
	public String detail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);

		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Land domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) {
			return this.gotoListPage();
		}

		String authorAvatar = "";
		if (domain.getContact() != null) {
			User farmer = userRepository.getById(dbContext, domain.getContact().getId(), User.COLUMNNAME_AVATAR);
			if (farmer != null) {
				authorAvatar = farmer.getAvatar();
			}
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		LandDto dto = DomainConverter.createLandDto(domain, currentUser, authorAvatar, canApprove,
				ConstantUtils.DEFAULT_DATE_FORMAT);

		if (dto == null) {
			return this.gotoListPage();
		}
		
		// Link
		List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_LAND);
		List<Link> links = linkRepository.getList(dbContext, 
				null, null, null, null, Arrays.asList(domainId), 
				null, null, null, linkTypes, null, null);
		if (links != null && !links.isEmpty()) {
			Map<ObjectId, Link> linkMaps = new HashMap<>();						
			
			for (Link link : links) {
				linkMaps.put(link.getProjectId(), link);
			}
			
			// Link project
			List<Project> projects = projectRepository.getListByIds(dbContext, linkMaps.keySet());
			if (projects != null) {
				for (Project project : projects) {
					Link link = linkMaps.get(project.getId());
					LinkProjectDto linkLandDto = DomainConverter.createLinkProjectDto(link, 
							project, currentUser, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (linkLandDto != null) {
						dto.addProjectDto(linkLandDto);
					}
				}
			}
		}
		
		model.addAttribute("dto", dto);

		model.addAttribute("allRoles", RoleUtils.REGISTRABLE_ROLES);
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_LAND);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return VIEW_PAGE;
	}

	private String editForm(Model model, User currentUser, LandCreateDto dto, List<IdNameDto> imageDtos,
			StatusEmbedDto statusDto) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);

		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> perposeMap = new LinkedHashMap<String, String>();
		Map<String, String> nearMap = new LinkedHashMap<String, String>();
		Map<String, String> squareUnitMap = new LinkedHashMap<>();
		Map<String, String> moneyUnitMap = new LinkedHashMap<>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		Map<String, String> formCooporateMap = new LinkedHashMap<>();

		int[] categoryTypes = new int[] { 
				CommonCategory.TYPE_LAND_TYPE, 
				CommonCategory.TYPE_SQUARE_UOM,
				CommonCategory.TYPE_MONEY_UOM, 
				CommonCategory.TYPE_LAND_PURPOSE, 
				CommonCategory.TYPE_LAND_NEAR,
				CommonCategory.TYPE_LAND_COOPERATE 
		};
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		
		for (CommonCategory category : categories) {
			if (CommonCategory.TYPE_LAND_TYPE == category.getType()) {
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

				// TODO
				/*if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getMoneyUnitId())) {
						dto.setMoneyUnitId(category.idAsString());
					}
				}*/
			} else if (CommonCategory.TYPE_LAND_PURPOSE == category.getType()) {
				perposeMap.put(category.idAsString(), category.getName());

			}

			else if (CommonCategory.TYPE_LAND_NEAR == category.getType()) {
				nearMap.put(category.idAsString(), category.getName());

			}

			else if (CommonCategory.TYPE_LAND_COOPERATE == category.getType()) {
				formCooporateMap.put(category.idAsString(), category.getName());
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
				Land domain = mainRepository.getById(dbContext, domainId, Land.COLUMNNAME_IMAGES,
						Land.COLUMNNAME_STATUS);
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

		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_LAND);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());

		model.addAttribute("categories", categoryMap);
		model.addAttribute("formCooperations", formCooporateMap);
		model.addAttribute("perposes", perposeMap);
		model.addAttribute("nears", nearMap);
		model.addAttribute("squareUnits", squareUnitMap);
		model.addAttribute("moneyUnits", moneyUnitMap);

		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));

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
		model.addAttribute("pageName", PAGE_NAME);
		
		return FORM_PAGE;
	}

	@RequestMapping("/create")
	public String create(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isFarmer()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		LandCreateDto createDto = new LandCreateDto();
		this.copyBaseResourceCreateDto(dbContext, currentUser, createDto);
		createDto.setClosed(false);

		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();
		statusDto.setStatus(Land.APPROVE_STATUS_PENDING);

		return this.editForm(model, currentUser, createDto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/edit")
	public String edit(HttpSession session, Model model, @PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isFarmer()) {
			return this.gotoNoPermission();
		}

		LandCreateDto dto = null;
		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Land domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) {
			return this.gotoListPage();
		}
		
		if (domain.isApproved()) {
			return this.gotoViewPage(id);
		}

		/*if (domain.getFarmer() != null && !dbContext.equals(domain.getFarmer().getId(), currentUser.getId())) {
			return this.gotoViewPage(id);
		}*/
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);

		dto = DomainConverter.createLandCreateDto(domain, currentUser, canApprove,
				ConstantUtils.DEFAULT_DATE_FORMAT);
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

		return this.editForm(model, currentUser, dto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/delete")
	public String delete(HttpSession session, Model model, @PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isFarmer()) {
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
	public String save(HttpSession session, Model model, @ModelAttribute("dto") @Validated LandCreateDto dto,
			BindingResult result, final RedirectAttributes redirectAttributes) {
		return this.saveEdit(session, model, dto, dto.getId(), result, redirectAttributes);
	}

	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String saveEdit(HttpSession session, Model model, @ModelAttribute("dto") @Validated LandCreateDto dto,
			@PathVariable(name = "id", required = true) String id, BindingResult result,
			final RedirectAttributes redirectAttributes) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isFarmer()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		BigDecimal square = dto.getSquare();
		ObjectId squareUnitId = dbContext.parse(dto.getSquareUnitId(), null);
		if (square == null || square.signum() < 0) {
			this.addModelError(result, "dto", "square", square, "land.message.invalid.square");
		} else if (squareUnitId == null) {
			this.addModelError(result, "dto", "square", square, "land.message.invalid.square");
		}
		
		Integer volume = dto.getVolume();
		if (volume == null || volume.intValue() <= 0) {
			this.addModelError(result, "dto", "volume", volume, "land.message.invalid.volume");
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
			this.addModelError(result, "dto", "startDate", startDateValue, "land.message.invalid.date");
		} else if (endDate == null) {
			this.addModelError(result, "dto", "startDate", startDateValue, "land.message.invalid.date");
		} else if (startDate.compareTo(endDate) > 0) {
			this.addModelError(result, "dto", "startDate", startDateValue, "land.message.invalid.date");
		}
		
		ObjectId locationId = dbContext.parse(dto.getLocationId(), null);
		if (locationId == null) {
			this.addModelError(result, "dto", "locationId", dto.getLocationId(), "land.message.invalid.location");
		}
		
		if (result.hasErrors()) {
			dto.setId(id);

			return this.editForm(model, currentUser, dto, null, null);
		}
		
		ObjectId domainId = dbContext.parse(id, null);
		String name = dto.getName().trim();

		if (this.mainRepository.nameExists(dbContext, name, domainId)) {
			this.addModelError(result, "dto", "name", name, "land.message.exists.name");
			
			return this.editForm(model, currentUser, dto, null, null);
		}

		Land domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);

			if (domain == null) {
				ObjectError error = new ObjectError("error", "land.message.not.found");
				result.addError(error);

				return this.editForm(model, currentUser, dto, null, null);
			}

			if (domain.isApproved()) {
				return this.gotoViewPage(id);
			}
			
			/*if (domain.getFarmer() != null && !dbContext.equals(domain.getFarmer().getId(), currentUser.getId())) {
				return this.gotoViewPage(id);
			}*/
		} else {
			domain = new Land();
		}
		
		ContactEmbed farmer = this.createContactDomain(dbContext, currentUser, result, dto);
		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editForm(model, currentUser, dto, null, null);
		}
		
		domain.setContact(farmer);
		
		domain.setName(name);
		domain.setAddress(dto.getAddress());
		domain.setDescription(dto.getDescription());
		domain.setSquare(square);
		domain.setVolume(volume);
		/*domain.setMoneyFrom(dto.getMoneyFrom());
		domain.setMoneyTo(dto.getMoneyTo());*/
		domain.setClosed(dto.isClosed());
		domain.setTree(dto.getTree());
		domain.setAnimal(dto.getAnimal());
		domain.setForest(dto.getForest());
		domain.setStartDate(startDate);
		domain.setEndDate(endDate);
		
		domain.setFormCooporateDiff(dto.getFormCooporateDiff());
		domain.setApproveName(dto.getApproveName());
		
		domain.clearNears();
		domain.clearPerpose();
		domain.clearFormCooporation();
		domain.clearCategories();
		
		if (domain.isNew() || domain.getLocation() == null
				|| !dbContext.equals(locationId, domain.getLocation().getId())) {
			Location location = locationRepository.getById(dbContext, locationId);
			if (location == null) {
				this.addModelError(result, "dto", "locationId", locationId.toString(), "land.message.invalid.location");

				return this.editForm(model, currentUser, dto, null, null);
			}

			LocationEmbed locationEmbed = new LocationEmbed(location);
			domain.setLocation(locationEmbed);
		}
		
		List<ObjectId> nearIds = dbContext.parseMulti(dto.getNearIds(), true);
		if (nearIds != null) {
			List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, nearIds);

			if (categories != null && !categories.isEmpty()) {
				for (CommonCategory category : categories) {
					IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(),
							category.getName());
					domain.addNear(categoryEmbed);
				}
			}
		}

		List<ObjectId> perposeIds = dbContext.parseMulti(dto.getPerposeIds(), true);
		if (perposeIds != null) {
			List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, perposeIds);

			if (categories != null && !categories.isEmpty()) {
				for (CommonCategory category : categories) {
					IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(),
							category.getName());
					domain.addPerpose(categoryEmbed);
				}
			}
		}

		List<ObjectId> formCooporateIds = dbContext.parseMulti(dto.getFormCooperationIds(), true);
		if (formCooporateIds != null) {
			List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, formCooporateIds);

			if (categories != null && !categories.isEmpty()) {
				for (CommonCategory category : categories) {
					IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(),
							category.getName());
					domain.addFormCooporation(categoryEmbed);
				}
			}
		}

		List<ObjectId> categoryIds = dbContext.parseMulti(dto.getCategoryIds(), true);
		if (categoryIds != null) {
			List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, categoryIds);
			if (categories != null && !categories.isEmpty()) {
				for (CommonCategory category : categories) {
					IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(),
							category.getName());
					domain.addCategory(categoryEmbed);
				}
			}
		}

		if (squareUnitId == null) {
			domain.setSquareUnit(null);
		} else if (domain.isNew() || domain.getSquareUnit() == null
				|| !dbContext.equals(squareUnitId, domain.getSquareUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, squareUnitId);
			if (category == null) {
				domain.setSquareUnit(null);
			} else {
				IdCodeNameEmbed squareUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setSquareUnit(squareUnit);
			}
		}

		/*ObjectId moneyUnitId = dbContext.parse(dto.getMoneyUnitId(), null);
		if (moneyUnitId == null) {
			domain.setMoneyUnit(null);
		} else if (domain.isNew() || domain.getMoneyUnit() == null
				|| !dbContext.equals(moneyUnitId, domain.getMoneyUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, moneyUnitId);
			if (category == null) {
				domain.setMoneyUnit(null);
			} else {
				IdCodeNameEmbed moneyUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setMoneyUnit(moneyUnit);
			}
		}*/

		this.updateBaseItem(session, dbContext, currentUser, domain, dto);

		this.mainRepository.save(dbContext, domain);

		return this.gotoListPage();
	}
}
