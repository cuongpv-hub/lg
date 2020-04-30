package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.IdSelectDto;
import vn.vsd.agro.dto.LandSearchDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.ProductSearchDto;
import vn.vsd.agro.dto.ProductSimpleDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ProjectSearchDto;
import vn.vsd.agro.dto.ProjectSimpleDto;
import vn.vsd.agro.dto.ScientistSearchDto;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.ScientistRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;

@Controller
@RequestMapping(value = "/modal-search")
public class ModalSearchController extends BaseController {
	
	private static final String SEARCH_SESSION_KEY = "ModalSearch";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	
	@RequestMapping(value = { "/land", "/land/list", "/land/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String searchLand(HttpSession session, Model model,
			@ModelAttribute("searchDto") LandSearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null) {
			searchDto = new LandSearchDto();
		}
		
		Integer searchStatus = searchDto.getStatus();
		if (searchStatus == null) {
			searchStatus = ConstantUtils.SEARCH_STATUS_ALL;
			
			if (currentUser != null && currentUser.isActive() && currentUser.isFarmer()) {
				searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
			}
			
			searchDto.setStatus(searchStatus);
		}
		searchDto.setOnlyAvaiable(true);
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.MODAL_SEARCH_PAGE_LAND_PAGE_SIZE;
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
			boolean canApprove = false;
			
			LandSimpleDto dto = DomainConverter.createLandSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		/************ Search prepare ************/
		Map<String, String> categoryMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		// Common category
		int[] categoryTypes = new int[] {
				CommonCategory.TYPE_LAND_TYPE
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		if (categories != null) {
			for (CommonCategory category : categories) {
				int type = category.getType();
				if (CommonCategory.TYPE_LAND_TYPE == type) {
					categoryMap.put(category.idAsString(), category.getName());
				}
			}
		}
		
		// Location
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				null, searchDto.getProvinceId(), null, null, 
				false, true, true, true);
		
		//searchDto.setCountryId(locationInfo.getCurrentCountryId());
		searchDto.setProvinceId(locationInfo.getCurrentProvinceId());
		//searchDto.setDistrictId(locationInfo.getCurrentDistrictId());
		//searchDto.setLocationId(locationInfo.getCurrentLocationId());
		
		// Status
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive() && currentUser.isCompany()) {
			if (currentUser.isFarmer()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Thửa đất của tôi");
			}
			
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED, "Thửa đất của nhà nông tôi đã liên kết");
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED, "Thửa đất của nhà nông tôi chưa liên kết");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên giảm dần");
		
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		
		if (!searchDto.isWithoutLocation()) {
			model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
			model.addAttribute("allLocations", locationInfo.getAllLocationMap());
			model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
			model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		}
		
		model.addAttribute("categories", categoryMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		IdSelectDto selectDto = new IdSelectDto();
		model.addAttribute("selectDto", selectDto);
		
		return "modal-search-land";
	}
	
	@RequestMapping(value = { "/product", "/product/list", "/product/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String searchProduct(HttpSession session, Model model,
			@ModelAttribute("searchDto") ProductSearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null) {
			searchDto = new ProductSearchDto();
		}
		
		Integer searchStatus = searchDto.getStatus();
		if (searchStatus == null) {
			searchStatus = ConstantUtils.SEARCH_STATUS_ALL;
			
			if (currentUser != null && currentUser.isActive() && currentUser.isFarmer()) {
				searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
			}
			
			searchDto.setStatus(searchStatus);
		}
		searchDto.setOnlyAvaiable(true);
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.MODAL_SEARCH_PAGE_PRODUCT_PAGE_SIZE;
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
			boolean canApprove = false;
			
			ProductSimpleDto dto = DomainConverter.createProductSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		/************ Search prepare ************/
		Map<String, String> categoryMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		// Common category
		int[] categoryTypes = new int[] {
				CommonCategory.TYPE_PRODUCT_TYPE
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		if (categories != null) {
			for (CommonCategory category : categories) {
				int type = category.getType();
				if (CommonCategory.TYPE_PRODUCT_TYPE == type) {
					categoryMap.put(category.idAsString(), category.getName());
				}
			}
		}
		
		// Location
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				null, searchDto.getProvinceId(), null, null, 
				false, true, true, true);
		
		//searchDto.setCountryId(locationInfo.getCurrentCountryId());
		searchDto.setProvinceId(locationInfo.getCurrentProvinceId());
		//searchDto.setDistrictId(locationInfo.getCurrentDistrictId());
		//searchDto.setLocationId(locationInfo.getCurrentLocationId());
		
		// Status
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive()) {
			if (currentUser.isFarmer()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Nông sản của tôi");
			}
			
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED, "Nông sản của nhà nông tôi đã liên kết");
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED, "Nông sản của nhà nông tôi chưa liên kết");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên giảm dần");
		
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		
		if (!searchDto.isWithoutLocation()) {
			model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
			model.addAttribute("allLocations", locationInfo.getAllLocationMap());
			model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
			model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		}
		
		model.addAttribute("categories", categoryMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		IdSelectDto selectDto = new IdSelectDto();
		model.addAttribute("selectDto", selectDto);
		
		return "modal-search-product";
	}
	
	@RequestMapping(value = { "/project", "/project/list", "/project/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String searchProject(HttpSession session, Model model,
			@ModelAttribute("searchDto") ProjectSearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null) {
			searchDto = new ProjectSearchDto();
		}
		
		Integer searchStatus = searchDto.getStatus();
		if (searchStatus == null) {
			searchStatus = ConstantUtils.SEARCH_STATUS_ALL;
			
			if (currentUser != null && currentUser.isActive() && currentUser.isCompany()) {
				searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
			}
			
			searchDto.setStatus(searchStatus);
		}
		searchDto.setOnlyAvaiable(true);
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int pageSize = ConstantUtils.MODAL_SEARCH_PAGE_PROJECT_PAGE_SIZE;
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
			boolean canApprove = false;
			String authorAvatar = null;
			
			ProjectSimpleDto dto = DomainConverter.createProjectSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		/************ Search prepare ************/
		Map<String, String> categoryMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		// Common category
		int[] categoryTypes = new int[] {
				CommonCategory.TYPE_PROJECT_TYPE
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		if (categories != null) {
			for (CommonCategory category : categories) {
				int type = category.getType();
				if (CommonCategory.TYPE_PROJECT_TYPE == type) {
					categoryMap.put(category.idAsString(), category.getName());
				}
			}
		}
		
		// Location
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				null, searchDto.getProvinceId(), null, null, 
				false, true, true, true);
		
		//searchDto.setCountryId(locationInfo.getCurrentCountryId());
		searchDto.setProvinceId(locationInfo.getCurrentProvinceId());
		//searchDto.setDistrictId(locationInfo.getCurrentDistrictId());
		//searchDto.setLocationId(locationInfo.getCurrentLocationId());
		
		// Status
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive()) {
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Dự án của công ty tôi");
			}
			
			if (currentUser.isFarmer() || currentUser.isScientist()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED, "Dự án của công ty tôi đã tham gia");
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED, "Dự án của công ty tôi chưa tham gia");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên giảm dần");
		
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		
		if (!searchDto.isWithoutLocation()) {
			model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
			model.addAttribute("allLocations", locationInfo.getAllLocationMap());
			model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
			model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		}
		
		model.addAttribute("categories", categoryMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		IdSelectDto selectDto = new IdSelectDto();
		model.addAttribute("selectDto", selectDto);
		
		return "modal-search-project";
	}
	
	@RequestMapping(value = { "/scientist", "/scientist/list", "/scientist/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String searchScientist(HttpSession session, Model model,
			@ModelAttribute("searchDto") ScientistSearchDto searchDto) {
		
		if (searchDto == null) {
			searchDto = new ScientistSearchDto();
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		int pageSize = ConstantUtils.MODAL_SEARCH_PAGE_SCIENTIST_PAGE_SIZE;
		
		List<ObjectId> majorIds = dbContext.parseMulti(searchDto.getMajorIds(), true);
		List<ObjectId> literacyIds = dbContext.parseMulti(searchDto.getLiteracyIds(), true);
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		Pageable pageable = new PageRequest(page, pageSize);
		
		List<ProfileDto> dtos = new ArrayList<ProfileDto>();
		long pageCount = 0;
		
		// Search scientist
		List<Scientist> scientists = scientistRepository.getList(dbContext, 
				searchDto.getText(), majorIds, literacyIds, null, null);
		
		if (scientists != null && !scientists.isEmpty()) {
			Map<ObjectId, Scientist> scientistMap = new HashMap<>();
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Scientist scientist : scientists) {
				userIds.add(scientist.getUserId());
				scientistMap.put(scientist.getUserId(), scientist);
			}
			
			ObjectId ignoreProjectId = dbContext.parse(searchDto.getNotLinkedProjectId(), null);
			
			if (currentUser != null && currentUser.isActive() && currentUser.isCompany()) {
				Integer status = searchDto.getStatus();
				
				// Check status filter
				if (status != null && status != ConstantUtils.SEARCH_STATUS_ALL) {
					Collection<ObjectId> companyIds = Arrays.asList(currentUser.getId());
					Collection<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_SCIENTIST);
					
					Collection<ObjectId> ignoreProjectIds = new ArrayList<>();
					if (ignoreProjectId != null) {
						ignoreProjectIds.add(ignoreProjectId);
					}
					
					List<Link> links = linkRepository.getList(dbContext, null, ignoreProjectIds, 
							companyIds, null, null, null, userIds, null, 
							linkTypes, null, null, Link.COLUMNNAME_LINK_OWNER_ID);
					
					if (status == ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED) {
						userIds.clear();
						
						if (links != null) {
							for (Link link : links) {
								userIds.add(link.getLinkOwnerId());
							}
						}
					} else if (status == ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED) {
						if (links != null) {
							for (Link link : links) {
								userIds.remove(link.getLinkOwnerId());
							}
						}
					}
				}
			}
			
			// Search users that linked with scientists
			if (!userIds.isEmpty()) {
				List<ObjectId> countryIds = null;
				List<ObjectId> provinceIds = null;
				List<ObjectId> districtIds = dbContext.parseMulti(searchDto.getDistrictIds(), true);
				List<ObjectId> locationIds = null;
				Boolean isCompany = null;
				Boolean isFarmer = null;
				Boolean isScientist = true;
				
				Sort sort = userRepository.getSort(dbContext);
				Integer sortValue = searchDto.getSort();
				if (sortValue != null)
				{
					if (sortValue == ConstantUtils.SEARCH_SORT_NAME_ASC) {
						sort = new Sort(new Order(Direction.ASC, User.COLUMNNAME_NAME));
					} else if (sortValue == ConstantUtils.SEARCH_SORT_NAME_DESC) {
						sort = new Sort(new Order(Direction.DESC, User.COLUMNNAME_NAME));
					}
				}
				
				List<User> domains = userRepository.getList(dbContext, userIds, 
						countryIds, provinceIds, districtIds, locationIds, 
						isCompany, isFarmer, isScientist, pageable, sort);
				
				long itemCount = userRepository.count(dbContext, userIds, 
						countryIds, provinceIds, districtIds, locationIds, 
						isCompany, isFarmer, isScientist);
				
				pageCount = itemCount / pageSize;
				if (itemCount % pageSize != 0) {
					pageCount++;
				}
				
				for (User domain : domains)
				{
					Scientist scientist = scientistMap.get(domain.getId());
					
					ProfileDto dto = DomainConverter.createProfileDto(domain, 
							null, null, scientist, ConstantUtils.DEFAULT_DATE_FORMAT);
					if (dto != null) {
						dtos.add(dto);
					}
				}
			}
		}
		
		Map<String, String> majorMap = new LinkedHashMap<>();
		Map<String, String> literacyMap = new LinkedHashMap<>();
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		Map<Integer, String> sortMap = new LinkedHashMap<>();
		
		// Common category
		int[] categoryTypes = new int[] {
				CommonCategory.TYPE_SCIENTIST_MAJOR,
				CommonCategory.TYPE_LITERACY
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		if (categories != null) {
			for (CommonCategory category : categories) {
				int type = category.getType();
				if (CommonCategory.TYPE_LITERACY == type) {
					literacyMap.put(category.idAsString(), category.getName());
				} else if (CommonCategory.TYPE_SCIENTIST_MAJOR == type) {
					majorMap.put(category.idAsString(), category.getName());
				}
			}
		}
		
		// Location
		List<Location> allLocations = locationRepository.getAll(dbContext);
		LocationInfoDto locationInfo = this.parseLocations(allLocations, 
				null, searchDto.getProvinceId(), null, null, 
				false, true, true, true);
		
		//searchDto.setCountryId(locationInfo.getCurrentCountryId());
		searchDto.setProvinceId(locationInfo.getCurrentProvinceId());
		//searchDto.setDistrictId(locationInfo.getCurrentDistrictId());
		//searchDto.setLocationId(locationInfo.getCurrentLocationId());
		
		// Status
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive()) {
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED, "Đã tham gia dự án công ty");
				statusMap.put(ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED, "Chưa tham gia dự án công ty");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên giảm dần");
		
		model.addAttribute("locations", locationInfo.getLocationMap());
		model.addAttribute("districts", locationInfo.getDistrictMap());
		model.addAttribute("provinces", locationInfo.getProvinceMap());
		model.addAttribute("countries", locationInfo.getCountryMap());
		
		if (!searchDto.isWithoutLocation()) {
			model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
			model.addAttribute("allLocations", locationInfo.getAllLocationMap());
			model.addAttribute("allDistricts", locationInfo.getAllDistrictMap());
			model.addAttribute("allProvinces", locationInfo.getAllProvinceMap());
		}
		
		model.addAttribute("majors", majorMap);
		model.addAttribute("literacies", literacyMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		IdSelectDto selectDto = new IdSelectDto();
		model.addAttribute("selectDto", selectDto);
		
		return "modal-search-scientist";
	}
}
