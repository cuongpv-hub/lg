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
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Organization;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.IdSelectDto;
import vn.vsd.agro.dto.LinkProjectDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.ProductCreateDto;
import vn.vsd.agro.dto.ProductDto;
import vn.vsd.agro.dto.ProductSearchDto;
import vn.vsd.agro.dto.ProductSimpleDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.ProductValidator;

@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseResourceController {
	private static final String PAGE_NAME = "product";
	
	private static final String LIST_PAGE = "product-list";
	private static final String FORM_PAGE = "product-form";
	private static final String VIEW_PAGE = "product-detail";

	public static final String SEARCH_SESSION_KEY = "productSearch";

	@Autowired
	private ProductRepository mainRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProductValidator validator;

	@Autowired
	@Qualifier(value = "numberPropertyEditor")
	private PropertyEditor numberPropertyEditor;
	
	@Override
	protected BasicRepository<? extends BaseItem, ObjectId> getMainRepository() {
		return this.mainRepository;
	}

	@Override
	protected File saveUploadImage(HttpSession session, MultipartFile imageFile) {
		return FileUtils.saveUploadProductImage(session, imageFile);
	}

	@Override
	protected boolean deleteUploadImage(HttpSession session, String imageName) {
		return FileUtils.deleteUploadProductFile(session, imageName);
	}

	@Override
	protected String gotoListPage() {
		return "redirect:/product";
	}

	@Override
	protected String gotoViewPage(String id) {
		return "redirect:/product/" + id + "/view";
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
		return IdSelectDto.LINK_TYPE_PRODUCT;
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

		if (ProductCreateDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.registerCustomEditor(BigDecimal.class, numberPropertyEditor);
			dataBinder.setValidator(validator);
		}
	}

	@RequestMapping(value = { "", "/list", "/search" },
			method = { RequestMethod.GET, RequestMethod.POST })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") ProductSearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null || searchDto.getStatus() == null) {
			Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
			if (lastSearch != null && lastSearch instanceof ProductSearchDto) {
				searchDto = (ProductSearchDto) lastSearch;
			} else {
				searchDto = new ProductSearchDto();
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
		SearchResult<Product> result = this.searchProducts(dbContext, currentUser, searchDto, pageSize);
		
		List<Product> domains = result.getItems();
		int page = result.getPage();
		long itemCount = result.getCount();
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<ProductSimpleDto> dtos = new ArrayList<ProductSimpleDto>(domains.size());
		for (Product domain : domains)
		{
			String authorAvatar = null;
			boolean canApprove = this.canApprove(dbContext, domain, currentUser);
			
			ProductSimpleDto dto = DomainConverter.createProductSimpleDto(domain, currentUser, 
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
				CommonCategory.TYPE_PRODUCT_TYPE, null, null);
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
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Nông sản của cá nhân");
			} else if (currentUser.isAdministrator() 
					|| currentUser.isDepartmentAdministrator()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đang chờ phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Đã từ chối phê duyệt");
			} else if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_ME_JOINED, "Nông sản tôi đã liên kết");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC, "Ngày duyệt giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC, "Ngày duyệt tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_DESC, "Ngày bắt đầu giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_START_DATE_ASC, "Ngày bắt đầu tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_DESC, "Ngày kết thúc giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_END_DATE_ASC, "Ngày kết thúc tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên nông sản tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên nông sản giảm dần");
		
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
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PRODUCT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return LIST_PAGE;
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		ProductSearchDto searchDto = null;

		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof ProductSearchDto) {
			searchDto = (ProductSearchDto) lastSearch;
		} else {
			searchDto = new ProductSearchDto();
		}

		searchDto.setPage(page);
		return list(session, model, searchDto);
	}

	@RequestMapping("/{id}/view")
	@Override
	public String detail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);

		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Product domain = this.mainRepository.getById(dbContext, domainId);
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
		ProductDto dto = DomainConverter.createProductDto(domain, currentUser, authorAvatar, canApprove,
				ConstantUtils.DEFAULT_DATE_FORMAT);

		if (dto == null) {
			return this.gotoListPage();
		}

		// Link
		List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
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
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PRODUCT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return VIEW_PAGE;
	}
	
	private String editForm(Model model, User currentUser, ProductCreateDto dto, List<IdNameDto> imageDtos,
			StatusEmbedDto statusDto) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);

		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> volumeUnitMap = new LinkedHashMap<>();
		Map<String, String> squareUnitMap = new LinkedHashMap<>();
		Map<String, String> moneyUnitMap = new LinkedHashMap<>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		Map<String, String> formCooporateMap = new LinkedHashMap<>();

		int[] categoryTypes = new int[] { 
				CommonCategory.TYPE_PRODUCT_TYPE, 
				CommonCategory.TYPE_VOLUME_UOM,
				CommonCategory.TYPE_MONEY_UOM, 
				CommonCategory.TYPE_SQUARE_UOM,
				CommonCategory.TYPE_PRODUCT_COOPERATE 
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		for (CommonCategory category : categories) {
			if (CommonCategory.TYPE_PRODUCT_TYPE == category.getType()) {
				categoryMap.put(category.idAsString(), category.getName());
			} else if (CommonCategory.TYPE_VOLUME_UOM == category.getType()) {
				volumeUnitMap.put(category.idAsString(), category.getCode());

				if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getVolumeUnitId())) {
						dto.setVolumeUnitId(category.idAsString());
					}
				}
			} else if (CommonCategory.TYPE_MONEY_UOM == category.getType()) {
				moneyUnitMap.put(category.idAsString(), category.getName());

				if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getMoneyUnitId())) {
						dto.setMoneyUnitId(category.idAsString());
					}
				}
			} else if (CommonCategory.TYPE_SQUARE_UOM == category.getType()) {
				squareUnitMap.put(category.idAsString(), category.getCode());

				if (category.isMain()) {
					if (StringUtils.isNullOrEmpty(dto.getMoneyUnitId())) {
						dto.setMoneyUnitId(category.idAsString());
					}
				}

			} else if (CommonCategory.TYPE_PRODUCT_COOPERATE == category.getType()) {
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
				Product domain = mainRepository.getById(dbContext, domainId, 
						Product.COLUMNNAME_IMAGES, Product.COLUMNNAME_STATUS);
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

		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_PRODUCT);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());

		model.addAttribute("formCooperations", formCooporateMap);
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		model.addAttribute("categories", categoryMap);
		model.addAttribute("volumeUnits", volumeUnitMap);
		model.addAttribute("moneyUnits", moneyUnitMap);
		model.addAttribute("squareUnits", squareUnitMap);
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
		
		ProductCreateDto createDto = new ProductCreateDto();
		this.copyBaseResourceCreateDto(dbContext, currentUser, createDto);
		createDto.setClosed(false);

		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();
		statusDto.setStatus(Product.APPROVE_STATUS_PENDING);

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

		ProductCreateDto dto = null;
		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);

		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Product domain = this.mainRepository.getById(dbContext, domainId);
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
		dto = DomainConverter.createProductCreateDto(domain, currentUser, canApprove,
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
	public String save(HttpSession session, Model model, @ModelAttribute("dto") @Validated ProductCreateDto dto,
			BindingResult result, final RedirectAttributes redirectAttributes) {
		return this.saveEdit(session, model, dto, dto.getId(), result, redirectAttributes);
	}

	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String saveEdit(HttpSession session, Model model, @ModelAttribute("dto") @Validated ProductCreateDto dto,
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
		if (square != null && square.signum() < 0) {
			this.addModelError(result, "dto", "square", square, "product.message.invalid.square");
		}
		
		BigDecimal volume = dto.getVolume();
		if (volume == null || volume.signum() < 0) {
			this.addModelError(result, "dto", "volume", volume, "product.message.invalid.volume");
		}
		
		BigDecimal moneyFrom = dto.getMoneyFrom();
		BigDecimal moneyTo = dto.getMoneyTo();
		if (moneyFrom != null && moneyFrom.signum() < 0) {
			this.addModelError(result, "dto", "moneyFrom", moneyFrom, "product.message.invalid.money");
		} else if (moneyTo != null && moneyTo.signum() < 0) {
			this.addModelError(result, "dto", "moneyFrom", moneyFrom, "product.message.invalid.money");
		} else if (moneyTo != null && moneyFrom != null && moneyFrom.compareTo(moneyTo) > 0) {
			this.addModelError(result, "dto", "moneyFrom", moneyFrom, "product.message.invalid.money");
		}
		
		BigDecimal priceFrom = dto.getPriceFrom();
		BigDecimal priceTo = dto.getPriceTo();
		ObjectId priceUnitId = dbContext.parse(dto.getPriceUnitId(), null);
		
		if (priceFrom == null || priceFrom.signum() < 0) {
			this.addModelError(result, "dto", "priceFrom", priceFrom, "product.message.invalid.price");
		} else if (priceTo == null || priceTo.signum() < 0) {
			this.addModelError(result, "dto", "priceFrom", priceFrom, "product.message.invalid.price");
		} else if (priceFrom.compareTo(priceTo) > 0) {
			this.addModelError(result, "dto", "priceFrom", priceFrom, "product.message.invalid.price");
		} else if (priceUnitId == null) {
			this.addModelError(result, "dto", "priceFrom", priceFrom, "product.message.invalid.price");
		}
		
		// Date
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
			this.addModelError(result, "dto", "startDate", startDateValue, "product.message.invalid.date");
		} else if (endDate == null) {
			this.addModelError(result, "dto", "startDate", startDateValue, "product.message.invalid.date");
		} else if (startDate.compareTo(endDate) > 0) {
			this.addModelError(result, "dto", "startDate", startDateValue, "product.message.invalid.date");
		}
		
		ObjectId locationId = dbContext.parse(dto.getLocationId(), null);
		if (locationId == null) {
			this.addModelError(result, "dto", "locationId", null, "product.message.invalid.location");
		}
		
		if (result.hasErrors()) {
			dto.setId(id);

			return this.editForm(model, currentUser, dto, null, null);
		}
		
		ObjectId domainId = dbContext.parse(id, null);
		String name = dto.getName().trim();

		if (this.mainRepository.nameExists(dbContext, name, domainId)) {
			this.addModelError(result, "dto", "name", name, "product.message.exists.name");

			return this.editForm(model, currentUser, dto, null, null);
		}

		Product domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);

			if (domain == null) {
				ObjectError error = new ObjectError("error", "product.message.not.found");
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
			domain = new Product();
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
		domain.setClosed(dto.isClosed());
		
		domain.setVolume(volume);
		domain.setSquare(square);
		domain.setMoneyFrom(moneyFrom);
		domain.setMoneyTo(moneyTo);
		domain.setPriceFrom(priceFrom);
		domain.setPriceTo(priceTo);
		domain.setStartDate(startDate);
		domain.setEndDate(endDate);
		
		domain.setFormCooporateDiff(dto.getFormCooporateDiff());
		domain.setApproveName(dto.getApproveName());

		if (domain.isNew() || domain.getLocation() == null
				|| !dbContext.equals(locationId, domain.getLocation().getId())) {
			Location location = locationRepository.getById(dbContext, locationId);
			if (location == null) {
				this.addModelError(result, "dto", "locationId", locationId.toString(), "product.message.invalid.location");

				return this.editForm(model, currentUser, dto, null, null);
			}

			LocationEmbed locationEmbed = new LocationEmbed(location);
			domain.setLocation(locationEmbed);
		}

		domain.clearCategories();

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

		List<ObjectId> formCooporateIds = dbContext.parseMulti(dto.getFormCooperationIds(), true);
		domain.clearFormCooporation();
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

		ObjectId volumeUnitId = dbContext.parse(dto.getVolumeUnitId(), null);
		if (volumeUnitId == null) {
			domain.setVolumeUnit(null);
		} else if (domain.isNew() || domain.getVolumeUnit() == null
				|| !dbContext.equals(volumeUnitId, domain.getVolumeUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, volumeUnitId);
			if (category == null) {
				domain.setVolumeUnit(null);
			} else {
				IdCodeNameEmbed squareUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setVolumeUnit(squareUnit);
			}
		}

		ObjectId moneyUnitId = dbContext.parse(dto.getMoneyUnitId(), null);
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
		}

		if (priceUnitId == null) {
			domain.setPriceUnit(null);
		} else if (domain.isNew() || domain.getPriceUnit() == null
				|| !dbContext.equals(priceUnitId, domain.getPriceUnit().getId())) {
			CommonCategory category = categoryRepository.getById(dbContext, priceUnitId);
			if (category == null) {
				domain.setPriceUnit(null);
			} else {
				IdCodeNameEmbed priceUnit = new IdCodeNameEmbed(category, category.getCode(), category.getName());
				domain.setPriceUnit(priceUnit);
			}
		}
		
		ObjectId squareUnitId = dbContext.parse(dto.getSquareUnitId(), null);
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

		this.updateBaseItem(session, dbContext, currentUser, domain, dto);

		this.mainRepository.save(dbContext, domain);

		return this.gotoListPage();
	}
}
