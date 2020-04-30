package vn.vsd.agro.controller;

import java.io.File;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.BaseItem;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.IdNameEmbed;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.News;
import vn.vsd.agro.domain.Organization;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.NewsCreateDto;
import vn.vsd.agro.dto.NewsDto;
import vn.vsd.agro.dto.NewsSearchDto;
import vn.vsd.agro.dto.NewsSimpleDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.NewsRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.NewsValidator;

@Controller
@RequestMapping(value = "/news")
public class NewsController extends BaseItemController {
	
	private static final String PAGE_NAME = "news";
	
	private static final String LIST_PAGE = "news-list";
	private static final String FORM_PAGE = "news-form";
	private static final String VIEW_PAGE = "news-detail";
	
	public static final String SEARCH_SESSION_KEY = "newsSearch";
	
	@Autowired
	private NewsRepository mainRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NewsValidator validator;

	@Override
	protected BasicRepository<? extends BaseItem, ObjectId> getMainRepository() {
		return this.mainRepository;
	}

	@Override
	protected File saveUploadImage(HttpSession session, MultipartFile imageFile) {
		return FileUtils.saveUploadNewsImage(session, imageFile);
	}

	@Override
	protected boolean deleteUploadImage(HttpSession session, String imageName) {
		return FileUtils.deleteUploadNewsFile(session, imageName);
	}

	@Override
	protected String gotoListPage() {
		return "redirect:/news";
	}
	
	@Override
	protected String gotoViewPage(String id) {
		return "redirect:/news/" + id + "/view";
	}

	@Override
	protected String[] getProcessRoles() {
		return new String[] { RoleUtils.ROLE_ADMIN, RoleUtils.ROLE_DEPARTMENT_ADMIN };
	}

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (NewsCreateDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.setValidator(validator);
		}
	}
	
	@RequestMapping({ "", "/list", "/search" })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") NewsSearchDto searchDto)
	{
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null) {
			Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
			if (lastSearch != null && lastSearch instanceof NewsSearchDto) {
				searchDto = (NewsSearchDto) lastSearch;
			} else {
				searchDto = new NewsSearchDto();
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
					searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
				}
			}
			
			searchDto.setStatus(searchStatus);
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.NEWS_PAGE_NEWS_LIST_PAGE_SIZE;
		SearchResult<News> result = this.searchNews(dbContext, currentUser, searchDto, pageSize);
		
		List<News> domains = result.getItems();
		int page = result.getPage();
		long itemCount = result.getCount();
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<NewsSimpleDto> dtos = new ArrayList<NewsSimpleDto>(domains.size());
		for (News domain : domains)
		{
			boolean canApprove = this.canApprove(dbContext, domain, currentUser);
			NewsSimpleDto dto = DomainConverter.createNewsSimpleDto(domain, currentUser, 
					canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, 
				CommonCategory.TYPE_NEWS_TYPE, null, null);
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
			if (currentUser.isAdministrator() 
					|| currentUser.isDepartmentAdministrator()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_DRAFT, "Chưa gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đang chờ phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đã gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã được phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Bị từ chối phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Tin tức của tôi");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC, "Ngày duyệt giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC, "Ngày duyệt tăng dần");
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
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_NEWS);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return LIST_PAGE;
	}

	@RequestMapping("/page")
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		NewsSearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof NewsSearchDto) {
			searchDto = (NewsSearchDto) lastSearch;
		} else {
			searchDto = new NewsSearchDto();
		}
		
		searchDto.setPage(page);
		return list(session, model, searchDto);
	}
	
	@RequestMapping("/{id}/view")
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
		
		News domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		String authorAvatar = "";
		
		if (domain.getAuthor() != null) {
			User author = userRepository.getById(dbContext, domain.getAuthor().getId(), User.COLUMNNAME_AVATAR);
			if (author != null) {
				authorAvatar = author.getAvatar();
			}
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		NewsDto dto = DomainConverter.createNewsDto(domain, currentUser, 
				authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
		
		model.addAttribute("dto", dto);
		model.addAttribute("allRoles", RoleUtils.REGISTRABLE_ROLES);
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_NEWS);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return VIEW_PAGE;
	}
	
	private String editForm(Model model, User currentUser, NewsCreateDto dto, 
			List<IdNameDto> imageDtos, StatusEmbedDto statusDto) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> organizationMap = new LinkedHashMap<>();
		
		int[] categoryTypes = new int[] { 
				CommonCategory.TYPE_NEWS_TYPE 
		};
		List<CommonCategory> categories = categoryRepository.getList(dbContext, 
				categoryTypes, null, null);
		for (CommonCategory category : categories) {
			if (CommonCategory.TYPE_NEWS_TYPE == category.getType()) {
				categoryMap.put(category.idAsString(), category.getName());
			}
		}
		
		List<Organization> organizations = organizationRepository.getAll(dbContext);
		for (Organization organization : organizations) {
			organizationMap.put(organization.idAsString(), organization.getName());
		}
		
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				dto.getCountryId(), dto.getProvinceId(), 
				dto.getDistrictId(), dto.getLocationId(), 
				false, false, false, false);
		
		dto.setCountryId(locationInfo.getCurrentCountryId());
		dto.setProvinceId(locationInfo.getCurrentProvinceId());
		dto.setDistrictId(locationInfo.getCurrentDistrictId());
		dto.setLocationId(locationInfo.getCurrentLocationId());
		
		// Image
		if (imageDtos == null || statusDto == null) {
			imageDtos = new ArrayList<>();
			
			ObjectId domainId = dbContext.parse(dto.getId(), null);
			if (domainId != null) {
				News domain = mainRepository.getById(dbContext, domainId, 
						News.COLUMNNAME_IMAGES, News.COLUMNNAME_STATUS);
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
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_NEWS);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		model.addAttribute("categories", categoryMap);
		model.addAttribute("organizations", organizationMap);
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
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

		if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
			return this.gotoNoPermission();
		}

		NewsCreateDto createDto = new NewsCreateDto();
		createDto.setClosed(false);
		
		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();
		statusDto.setStatus(News.APPROVE_STATUS_PENDING);
		
		return this.editForm(model, currentUser, createDto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/edit")
	public String edit(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		News domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		if (domain.isApproved()) {
			return this.gotoViewPage(id);
		}
		
		if (domain.getAuthor() != null && !dbContext.equals(domain.getAuthor().getId(), currentUser.getId())) {
			return this.gotoViewPage(id);
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		
		NewsCreateDto dto = DomainConverter.createNewsCreateDto(domain, currentUser, 
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

		return this.editForm(model, currentUser, dto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/delete")
	public String delete(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
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
			@ModelAttribute("dto") @Validated NewsCreateDto dto, 
			BindingResult result, final RedirectAttributes redirectAttributes)
	{
		return this.saveEdit(session, model, dto, dto.getId(), result, redirectAttributes);
	}
	
	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String saveEdit(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated NewsCreateDto dto, 
			@PathVariable(name = "id", required = true) String id,
			BindingResult result, final RedirectAttributes redirectAttributes)
	{
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
			return this.gotoNoPermission();
		}
		
		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editForm(model, currentUser, dto, null, null);
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		List<ObjectId> categoryIds = dbContext.parseMulti(dto.getCategoryIds(), true);
		if (categoryIds == null || categoryIds.isEmpty()) {
			ObjectError error = new ObjectError("categoryIds", "news.message.not.empty.category");
            result.addError(error);
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		ObjectId locationId = dbContext.parse(dto.getLocationId(), null);
		if (locationId == null) {
			ObjectError error = new ObjectError("locationId", "news.message.invalid.location");
            result.addError(error);
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		String name = dto.getName().trim();
		if (this.mainRepository.nameExists(dbContext, name, domainId)) {
			ObjectError error = new ObjectError("name", "news.message.exists.name");
            result.addError(error);
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		News domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);
			
			if (domain == null) {
				ObjectError error = new ObjectError("error", "news.message.not.found");
	            result.addError(error);
	            
	            return this.editForm(model, currentUser, dto, null, null);
			}
			
			if (domain.isApproved()) {
				return this.gotoViewPage(id);
			}
			
			if (domain.getAuthor() != null && !dbContext.equals(domain.getAuthor().getId(), currentUser.getId())) {
				return this.gotoViewPage(id);
			}
		} else {
			domain = new News();
		}
		
		List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, categoryIds);
		if (categories == null || categories.isEmpty()) {
			ObjectError error = new ObjectError("categoryIds", "news.message.not.empty.category");
            result.addError(error);
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		domain.setName(name);		
		domain.setDescription(dto.getDescription());		
		domain.setAddress(dto.getAddress());
		domain.setLocation(currentUser.getLocation());
		
		domain.setApproveName(dto.getApproveName());
		domain.setClosed(dto.isClosed());
		
		IdNameEmbed author = new IdNameEmbed(currentUser.getId(), currentUser.getName());
		domain.setAuthor(author);
		
		if (domain.isNew() 
				|| domain.getLocation() == null 
				|| !dbContext.equals(locationId, domain.getLocation().getId())) {
			Location location = locationRepository.getById(dbContext, locationId);
			if (location == null) {
				ObjectError error = new ObjectError("locationId", "project.message.invalid.location");
	            result.addError(error);
	            
	            return this.editForm(model, currentUser, dto, null, null);
			}
			
			LocationEmbed locationEmbed = new LocationEmbed(location);
			domain.setLocation(locationEmbed);
		}
		
		domain.clearCategories();
		
		domain.clearCategories();
		for (CommonCategory category : categories) {
			IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(), category.getName());
			domain.addCategory(categoryEmbed);
		}
		
		this.updateBaseItem(session, dbContext, currentUser, domain, dto);
		
		this.mainRepository.save(dbContext, domain);
		
		return this.gotoListPage();
	}
}
