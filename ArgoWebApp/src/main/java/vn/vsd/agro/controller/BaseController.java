package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.context.UserInfo;
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.INeedApprove;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.News;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.UserExtension;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;
import vn.vsd.agro.dto.ArticleSearchDto;
import vn.vsd.agro.dto.Envelope;
import vn.vsd.agro.dto.ErrorDto;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.LandSearchDto;
import vn.vsd.agro.dto.LocationInfoDto;
import vn.vsd.agro.dto.Meta;
import vn.vsd.agro.dto.NewsSearchDto;
import vn.vsd.agro.dto.ProductSearchDto;
import vn.vsd.agro.dto.ProjectSearchDto;
import vn.vsd.agro.repository.ArticleRepository;
import vn.vsd.agro.repository.LandRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.NewsRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;

public abstract class BaseController {

	public static final String SESSION_KEY_ACCESS_TOKEN = "AccessToken";
	public static final String HTTP_HEADER_KEY_ACCESS_TOKEN = "AccessToken";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private LandRepository landRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private NewsRepository newsRepository;
	
	public String gotoHome() {
		return "redirect:/home";
	}

	public String gotoNoPermission() {
		return "redirect:/notPermission";
	}

	public String gotoLogin() {
		return "redirect:/login";
	}

	public IContext<ObjectId> getDbContext(User currentUser) {
		if (currentUser != null) {
			List<String> userRoles = new ArrayList<>(currentUser.getRoles().keySet());

			UserInfo<ObjectId> userInfo = new UserInfo<ObjectId>(currentUser.getEmail(), currentUser.getName(),
					Arrays.asList(MongoContext.CONTEXT_ROOT_ID), userRoles);

			IContext<ObjectId> dbContext = new MongoContext(MongoContext.CONTEXT_ROOT_ID, ConstantUtils.CLIENT_NAME,
					MongoContext.CONTEXT_ROOT_ID, currentUser.getId(), userInfo);

			return dbContext;
		}

		return MongoContext.ROOT_CONTEXT;
	}

	public User getCurrentUser(HttpSession session, Model model) {
		Object accessToken = session.getAttribute(SESSION_KEY_ACCESS_TOKEN);

		if (accessToken != null) {
			User currentUser = userRepository.findByAccessToken(accessToken.toString());
			if (currentUser != null) {
				model.addAttribute("currentUser", currentUser);
			}

			return currentUser;
		}

		return null;
	}

	public User getCurrentUser(HttpHeaders httpHeader) {
		String accessToken = httpHeader.getFirst(HTTP_HEADER_KEY_ACCESS_TOKEN);

		if (accessToken != null && !accessToken.isEmpty()) {
			return userRepository.findByAccessToken(accessToken);
		}

		return null;
	}

	protected ResponseEntity<?> responseErrorEntity(Exception ex) {
		if (ex != null) {
			ex.printStackTrace();

			ErrorDto dto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
			return new Envelope(dto).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new Envelope(Meta.ERROR).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected void addModelError(BindingResult result, String fieldName, Object value, String messageKey) {
		this.addModelError(result, "dto", fieldName, value, messageKey);
	}
	
	protected void addModelError(BindingResult result, String dtoName, String fieldName, Object value, String messageKey) {
		FieldError error = new FieldError(dtoName, fieldName, value, true, new String[] { messageKey }, null, null);
		result.addError(error);
	}
	
	protected List<User> parseUserExtensions(IContext<ObjectId> dbContext, List<? extends UserExtension> domains,
			Sort userSort) {
		if (domains == null || domains.isEmpty()) {
			return Collections.<User>emptyList();
		}

		if (userSort == null) {
			userSort = userRepository.getSort(dbContext);
		}

		Set<ObjectId> userIds = new HashSet<>();
		Map<ObjectId, UserExtension> domainMaps = new HashMap<>();

		for (UserExtension domain : domains) {
			userIds.add(domain.getUserId());
			domainMaps.put(domain.getUserId(), domain);
		}

		List<User> users = userRepository.getListByIds(dbContext, userIds, userSort);
		if (users == null) {
			return Collections.<User>emptyList();
		}

		List<User> retValues = new LinkedList<>();
		for (User user : users) {
			user.setUserExtension(domainMaps.get(user.getId()));
			retValues.add(user);
		}

		return retValues;
	}

	protected LocationInfoDto parseLocations(List<Location> allLocations, String currentCountryId,
			String currentProvinceId, String currentDistrictId, String currentLocationId, boolean withAllCountry,
			boolean withAllProvince, boolean withAllDistrict, boolean withAllLocation) {

		LocationInfoDto dto = new LocationInfoDto();
		dto.setCurrentCountryId(currentCountryId);
		dto.setCurrentProvinceId(currentProvinceId);
		dto.setCurrentDistrictId(currentDistrictId);
		dto.setCurrentLocationId(currentLocationId);

		for (Location location : allLocations) {
			String locationId = location.idAsString();

			if (!withAllLocation && StringUtils.isNullOrEmpty(dto.getCurrentLocationId())) {
				dto.setCurrentLocationId(locationId);
			}

			//
			DistrictEmbed district = location.getDistrict();
			if (district != null) {
				String districtId = district.idAsString();

				List<IdNameDto> locations = dto.getLocations(districtId);
				if (locations == null) {
					if (!withAllDistrict && StringUtils.isNullOrEmpty(dto.getCurrentDistrictId())) {
						dto.setCurrentDistrictId(districtId);
					}

					locations = new ArrayList<>();

					//
					ProvinceEmbed province = district.getProvince();
					if (province != null) {
						String provinceId = province.idAsString();

						List<IdNameDto> districts = dto.getDistricts(provinceId);
						if (districts == null) {
							if (!withAllProvince && StringUtils.isNullOrEmpty(dto.getCurrentProvinceId())) {
								dto.setCurrentProvinceId(provinceId);
							}

							districts = new ArrayList<>();

							//
							CountryEmbed country = province.getCountry();
							if (country != null) {
								String countryId = country.idAsString();

								if (!withAllCountry && StringUtils.isNullOrEmpty(dto.getCurrentCountryId())) {
									dto.setCurrentCountryId(countryId);
								}

								List<IdNameDto> provinces = dto.getProvinces(countryId);
								if (provinces == null) {
									provinces = new ArrayList<>();

									if (!dto.containsCountry(countryId)) {
										dto.addCountry(countryId, country.getName());
									}
								}

								provinces.add(new IdNameDto(provinceId, province.getName()));
								dto.setProvinces(countryId, provinces);
							}
						}

						districts.add(new IdNameDto(districtId, district.getName()));
						dto.setDistricts(provinceId, districts);
					}
				}

				locations.add(new IdNameDto(locationId, location.getName()));
				dto.setLocations(districtId, locations);
			}
		}

		// Prepare current location
		String countryId = dto.getCurrentCountryId();
		String provinceId = dto.getCurrentProvinceId();
		String districtId = dto.getCurrentDistrictId();

		if (!StringUtils.isNullOrEmpty(countryId)) {
			List<IdNameDto> provinces = dto.getProvinces(countryId);
			if (provinces != null) {
				for (IdNameDto province : provinces) {
					dto.addProvince(province.getId(), province.getName());
				}
			}
		}

		if (!StringUtils.isNullOrEmpty(provinceId)) {
			List<IdNameDto> districts = dto.getDistricts(provinceId);
			if (districts != null) {
				for (IdNameDto district : districts) {
					dto.addDistrict(district.getId(), district.getName());
				}
			}
		}

		if (!StringUtils.isNullOrEmpty(districtId)) {
			List<IdNameDto> locations = dto.getLocations(districtId);
			if (locations != null) {
				for (IdNameDto location : locations) {
					dto.addLocation(location.getId(), location.getName());
				}
			}
		}

		return dto;
	}

	protected SearchResult<Project> searchProjects(IContext<ObjectId> dbContext,
			User currentUser, ProjectSearchDto searchDto, int pageSize) {
		
		List<ObjectId> ownerIds = new ArrayList<>();
		List<Integer> statuses = new ArrayList<>();
		
		String text = searchDto.getText();
		String projectName = searchDto.getName();
		String companyName = searchDto.getCompany();
		int searchStatus = searchDto.getStatus();
		
		boolean valid = this.processSearchStatus(searchStatus, 
				currentUser, RoleUtils.ROLE_COMPANY, ownerIds, statuses);
		if (!valid) {
			return new SearchResult<Project>();
		}
		
		List<ObjectId> projectIds = new ArrayList<>();
		List<ObjectId> categoryIds = dbContext.parseMulti(searchDto.getCategoryIds(), true);
		
		List<ObjectId> countryIds = new ArrayList<>();
		List<ObjectId> provinceIds = new ArrayList<>();
		List<ObjectId> districtIds = dbContext.parseMulti(searchDto.getDistrictIds(), true);
		List<ObjectId> locationIds = new ArrayList<>();
		
		ObjectId provinceId = dbContext.parse(searchDto.getProvinceId(), null);
		if (provinceId != null) {
			provinceIds.add(provinceId);
		}
		
		if (!ownerIds.isEmpty()) {
			companyName = null;
		}
		
		List<Project> domains = null;
		long itemCount = 0;
		
		if (searchStatus == ConstantUtils.SEARCH_STATUS_ME_JOINED) {
			// Check if user joined projects
			if (currentUser == null || !currentUser.isActive()) {
				domains = new ArrayList<>();
			} else {
				List<ObjectId> linkOwnerIds = Arrays.asList(currentUser.getId());
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						null, null, null, null, linkOwnerIds, null, 
						null, null, null, Link.COLUMNNAME_PROJECT_ID);
				if (links == null || links.isEmpty()) {
					domains = new ArrayList<>();	
				} else {
					for (Link link : links) {
						projectIds.add(link.getProjectId());
					}
				}
			}
		}
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		
		if (domains == null) {
			List<ObjectId> withMySelfIds = new ArrayList<>();
			boolean withoutExpired = true;
			Boolean active = null;
			
			Boolean requireProduct = this.changeBooleanValue(searchDto.getRequireProduct());
			Boolean requireLand = this.changeBooleanValue(searchDto.getRequireLand());
			Boolean requireScientist = this.changeBooleanValue(searchDto.getRequireScientist());
			
			if (currentUser != null && currentUser.isActive() 
					&& (currentUser.isAdministrator() 
							|| currentUser.isDepartmentAdministrator() 
							|| currentUser.isCompany())) {
				
				withoutExpired = !StringUtils.isNullOrEmpty(searchDto.getWithoutClosed()) 
						&& ConstantUtils.YES_VALUE.equalsIgnoreCase(searchDto.getWithoutClosed());
				
				if (currentUser.isCompany() && searchStatus == ConstantUtils.SEARCH_STATUS_ALL) {
					withMySelfIds.add(currentUser.getId());
				}
			} else {
				active = true;
			}
			
			if (projectIds.isEmpty() && searchDto.isOnlyAvaiable()) {
				statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
			}
			
			Pageable pageable = new PageRequest(page, pageSize);
			Integer searchSort = searchDto.getSort();
			
			Sort sort = projectRepository.getSort(dbContext);
			if (searchSort == null || searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Project.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Project.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Project.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Project.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Project.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Project.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Project.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Project.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Project.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Project.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Project.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Project.COLUMNNAME_NAME)
				);
			}
			
			domains = projectRepository.getList(dbContext, 
					text, projectName, companyName, projectIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds, 
					statuses, withMySelfIds, requireProduct, requireLand, requireScientist, 
					withoutExpired, active, pageable, sort);
			
			itemCount = projectRepository.count(dbContext, 
					text, projectName, companyName, projectIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds,
					statuses, withMySelfIds, requireProduct, requireLand, requireScientist, 
					withoutExpired, active);
		}
		
		return new SearchResult<Project>(domains, page, itemCount);
	}

	protected SearchResult<Product> searchProducts(IContext<ObjectId> dbContext,
			User currentUser, ProductSearchDto searchDto, int pageSize) {
		List<ObjectId> ownerIds = new ArrayList<>();
		List<Integer> statuses = new ArrayList<>();
		
		String text = searchDto.getText();
		String productName = searchDto.getName();
		String farmerName = searchDto.getFarmer();
		int searchStatus = searchDto.getStatus();
		
		boolean valid = this.processSearchStatus(searchStatus, 
				currentUser, RoleUtils.ROLE_FARMER, ownerIds, statuses);
		if (!valid) {
			return new SearchResult<Product>();
		}
		
		List<Product> domains = null;
		long itemCount = 0;
		
		List<ObjectId> productIds = new ArrayList<>();
		List<ObjectId> ignoreProductIds = new ArrayList<>();
		List<ObjectId> categoryIds = dbContext.parseMulti(searchDto.getCategoryIds(), true);
		
		List<ObjectId> countryIds = new ArrayList<>();
		List<ObjectId> provinceIds = new ArrayList<>();
		List<ObjectId> districtIds = dbContext.parseMulti(searchDto.getDistrictIds(), true);
		List<ObjectId> locationIds = new ArrayList<>();
		
		ObjectId provinceId = dbContext.parse(searchDto.getProvinceId(), null);
		if (provinceId != null) {
			provinceIds.add(provinceId);
		}
		
		if (!ownerIds.isEmpty()) {
			farmerName = null;
		}
		
		if (currentUser != null && currentUser.isActive() && currentUser.isCompany()) {
			if (searchStatus == ConstantUtils.SEARCH_STATUS_ME_JOINED
					|| searchStatus == ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED) {
				// Search products that linked with own projects
				List<ObjectId> linkCompanyIds = Arrays.asList(currentUser.getId());
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						linkCompanyIds, null, null, null, null, null, 
						linkTypes, null, null, Link.COLUMNNAME_LINK_ID);
				if (links == null || links.isEmpty()) {
					domains = new ArrayList<>();	
				} else {
					for (Link link : links) {
						productIds.add(link.getLinkId());
					}
				}
			} else if (searchStatus == ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED) {
				// Search products that not linked with own projects
				List<ObjectId> linkCompanyIds = Arrays.asList(currentUser.getId());
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						linkCompanyIds, null, null, null, null, null, 
						linkTypes, null, null, Link.COLUMNNAME_LINK_ID);
				if (links != null) {
					for (Link link : links) {
						ignoreProductIds.add(link.getLinkId());
					}
				}
			}
		}
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		
		if (domains == null) {
			List<ObjectId> withMySelfIds = new ArrayList<>();
			boolean withoutExpired = true;
			Boolean active = null;
			
			if (currentUser != null && currentUser.isActive() 
					&& (currentUser.isAdministrator() 
							|| currentUser.isDepartmentAdministrator() 
							|| currentUser.isFarmer())) {
				withoutExpired = !StringUtils.isNullOrEmpty(searchDto.getWithoutClosed()) 
						&& ConstantUtils.YES_VALUE.equalsIgnoreCase(searchDto.getWithoutClosed());
				
				if (currentUser.isFarmer() && searchStatus == ConstantUtils.SEARCH_STATUS_ALL) {
					withMySelfIds.add(currentUser.getId());
				}
			} else {
				active = true;
			}
			
			if (productIds.isEmpty() && searchDto.isOnlyAvaiable()) {
				statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				
				// Search only don't have link with projects
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				List<Integer> linkStatus = Arrays.asList(Link.STATUS_APPROVED, Link.STATUS_APPROVED_OTHER_PROJECT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						null, null, null, null, null, null, 
						linkTypes, linkStatus, null, Link.COLUMNNAME_LINK_ID);
				if (links != null) {
					for (Link link : links) {
						ignoreProductIds.add(link.getLinkId());
					}
				}
			}
			
			Pageable pageable = new PageRequest(page, pageSize);
			Integer searchSort = searchDto.getSort();
			
			Sort sort = projectRepository.getSort(dbContext);
			if (searchSort == null || searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Product.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Product.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Product.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Product.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Product.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Product.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Product.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Product.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Product.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Product.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Product.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Product.COLUMNNAME_NAME)
				);
			}
			
			domains = productRepository.getList(dbContext, 
					text, productName, farmerName, productIds, ignoreProductIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds, 
					statuses, withMySelfIds, withoutExpired, active, pageable, sort);
			
			itemCount = productRepository.count(dbContext, 
					text, productName, farmerName, productIds, ignoreProductIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds, 
					statuses, withMySelfIds, withoutExpired, active);
		}
		
		return new SearchResult<Product>(domains, page, itemCount);
	}

	protected SearchResult<Land> searchLands(IContext<ObjectId> dbContext,
			User currentUser, LandSearchDto searchDto, int pageSize) {
		List<ObjectId> ownerIds = new ArrayList<>();
		List<Integer> statuses = new ArrayList<>();
		
		String text = searchDto.getText();
		String productName = searchDto.getName();
		String farmerName = searchDto.getFarmer();
		int searchStatus = searchDto.getStatus();
		
		boolean valid = this.processSearchStatus(searchStatus, 
				currentUser, RoleUtils.ROLE_FARMER, ownerIds, statuses);
		if (!valid) {
			return new SearchResult<Land>();
		}
		
		List<Land> domains = null;
		long itemCount = 0;
		
		List<ObjectId> landIds = new ArrayList<>();
		List<ObjectId> ignoreLandIds = new ArrayList<>();
		List<ObjectId> categoryIds = dbContext.parseMulti(searchDto.getCategoryIds(), true);
		
		List<ObjectId> countryIds = new ArrayList<>();
		List<ObjectId> provinceIds = new ArrayList<>();
		List<ObjectId> districtIds = dbContext.parseMulti(searchDto.getDistrictIds(), true);
		List<ObjectId> locationIds = new ArrayList<>();
		
		ObjectId provinceId = dbContext.parse(searchDto.getProvinceId(), null);
		if (provinceId != null) {
			provinceIds.add(provinceId);
		}
		
		if (!ownerIds.isEmpty()) {
			farmerName = null;
		}
		
		if (currentUser != null && currentUser.isActive() && currentUser.isCompany()) {
			if (searchStatus == ConstantUtils.SEARCH_STATUS_ME_JOINED
					|| searchStatus == ConstantUtils.SEARCH_STATUS_RESOURCE_JOINED) {
				// Search products that linked with own projects
				List<ObjectId> linkCompanyIds = Arrays.asList(currentUser.getId());
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						linkCompanyIds, null, null, null, null, null, 
						linkTypes, null, null, Link.COLUMNNAME_LINK_ID);
				if (links == null || links.isEmpty()) {
					domains = new ArrayList<>();	
				} else {
					for (Link link : links) {
						landIds.add(link.getLinkId());
					}
				}
			} else if (searchStatus == ConstantUtils.SEARCH_STATUS_RESOURCE_NOT_JOINED) {
				// Search products that not linked with own projects
				List<ObjectId> linkCompanyIds = Arrays.asList(currentUser.getId());
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						linkCompanyIds, null, null, null, null, null, 
						linkTypes, null, null, Link.COLUMNNAME_LINK_ID);
				if (links != null) {
					for (Link link : links) {
						ignoreLandIds.add(link.getLinkId());
					}
				}
			}
		}
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		
		if (domains == null) {
			List<ObjectId> withMySelfIds = new ArrayList<>();
			boolean withoutExpired = true;
			Boolean active = null;
			
			if (currentUser != null && currentUser.isActive() 
					&& (currentUser.isAdministrator() 
							|| currentUser.isDepartmentAdministrator() 
							|| currentUser.isFarmer())) {
				withoutExpired = !StringUtils.isNullOrEmpty(searchDto.getWithoutClosed()) 
						&& ConstantUtils.YES_VALUE.equalsIgnoreCase(searchDto.getWithoutClosed());
				
				if (currentUser.isFarmer() && searchStatus == ConstantUtils.SEARCH_STATUS_ALL) {
					withMySelfIds.add(currentUser.getId());
				}
			} else {
				active = true;
			}
			
			if (landIds.isEmpty() && searchDto.isOnlyAvaiable()) {
				statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				
				// Search only don't have link with projects
				List<Integer> linkTypes = Arrays.asList(Link.LINK_TYPE_PRODUCT);
				List<Integer> linkStatus = Arrays.asList(Link.STATUS_APPROVED, Link.STATUS_APPROVED_OTHER_PROJECT);
				
				List<Link> links = linkRepository.getList(dbContext, null, null, 
						null, null, null, null, null, null, 
						linkTypes, linkStatus, null, Link.COLUMNNAME_LINK_ID);
				if (links != null) {
					for (Link link : links) {
						ignoreLandIds.add(link.getLinkId());
					}
				}
			}
			
			Pageable pageable = new PageRequest(page, pageSize);
			Integer searchSort = searchDto.getSort();
			
			Sort sort = landRepository.getSort(dbContext);
			if (searchSort == null || searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Land.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Land.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Land.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Land.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Land.COLUMNNAME_STATUS_VALUE),
						new Order(Direction.DESC, Land.COLUMNNAME_STATUS_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Land.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_START_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Land.COLUMNNAME_START_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Land.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_END_DATE_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Land.COLUMNNAME_END_DATE_VALUE),
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_ASC) {
				sort = new Sort(
						new Order(Direction.ASC, Land.COLUMNNAME_NAME)
				);
			} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_DESC) {
				sort = new Sort(
						new Order(Direction.DESC, Land.COLUMNNAME_NAME)
				);
			}
			
			domains = landRepository.getList(dbContext, 
					text, productName, farmerName, landIds, ignoreLandIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds, 
					statuses, withMySelfIds, withoutExpired, active, pageable, sort);
			
			itemCount = landRepository.count(dbContext, 
					text, productName, farmerName, landIds, ignoreLandIds, ownerIds, categoryIds,  
					countryIds, provinceIds, districtIds, locationIds, 
					statuses, withMySelfIds, withoutExpired, active);
		}
		
		return new SearchResult<Land>(domains, page, itemCount);
	}
	
	protected SearchResult<Article> searchArticles(IContext<ObjectId> dbContext,
			User currentUser, ArticleSearchDto searchDto, int pageSize) {
		List<ObjectId> ownerIds = new ArrayList<>();
		List<Integer> statuses = new ArrayList<>();
		
		String text = searchDto.getText();
		String articleName = searchDto.getName();
		String authorName = searchDto.getAuthorName();
		int searchStatus = searchDto.getStatus();
		
		boolean valid = this.processSearchStatus(searchStatus, 
				currentUser, RoleUtils.ROLE_SCIENTIST, ownerIds, statuses);
		if (!valid) {
			return new SearchResult<Article>();
		}
		
		List<ObjectId> articleIds = new ArrayList<>();
		List<ObjectId> ignoreArticleIds = new ArrayList<>();
		List<ObjectId> categoryIds = dbContext.parseMulti(searchDto.getCategoryIds(), true);
		List<ObjectId> authorIds = dbContext.parseMulti(searchDto.getAuthorIds(), true);
		
		if (!ownerIds.isEmpty()) {
			authorName = null;
		}
		
		if (authorIds != null && !authorIds.isEmpty()) {
			ownerIds.addAll(authorIds);
		}
		
		List<ObjectId> withMySelfIds = new ArrayList<>();
		boolean withoutExpired = true;
		Boolean active = null;
		
		if (currentUser != null && currentUser.isActive() 
				&& (currentUser.isAdministrator() 
						|| currentUser.isDepartmentAdministrator() 
						|| currentUser.isScientist())) {
			withoutExpired = !StringUtils.isNullOrEmpty(searchDto.getWithoutClosed()) 
					&& ConstantUtils.YES_VALUE.equalsIgnoreCase(searchDto.getWithoutClosed());
			
			if (currentUser.isScientist() && searchStatus == ConstantUtils.SEARCH_STATUS_ALL) {
				withMySelfIds.add(currentUser.getId());
			}
		} else {
			active = true;
		}
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		
		Pageable pageable = new PageRequest(page, pageSize);
		Integer searchSort = searchDto.getSort();
		
		Sort sort = projectRepository.getSort(dbContext);
		if (searchSort == null || searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, Article.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, Article.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, Article.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, Article.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, Article.COLUMNNAME_STATUS_VALUE),
					new Order(Direction.DESC, Article.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, Article.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, Article.COLUMNNAME_STATUS_VALUE),
					new Order(Direction.DESC, Article.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, Article.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, Article.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, Article.COLUMNNAME_NAME)
			);
		}
		
		List<Article> domains = articleRepository.getList(dbContext, 
				text, articleName, authorName, articleIds, 
				ignoreArticleIds, ownerIds, categoryIds,  
				statuses, withMySelfIds, withoutExpired, active, pageable, sort);
		
		long itemCount = articleRepository.count(dbContext, 
				text, articleName, authorName, articleIds, 
				ignoreArticleIds, ownerIds, categoryIds,  
				statuses, withMySelfIds, withoutExpired, active);
		
		return new SearchResult<Article>(domains, page, itemCount);
	}
	
	protected SearchResult<News> searchNews(IContext<ObjectId> dbContext,
			User currentUser, NewsSearchDto searchDto, int pageSize) {
		
		List<ObjectId> ownerIds = new ArrayList<>();
		List<Integer> statuses = new ArrayList<>();
		
		String text = searchDto.getText();
		String newsName = searchDto.getName();
		String authorName = searchDto.getAuthorName();
		int searchStatus = searchDto.getStatus();
		
		boolean valid = this.processSearchStatus(searchStatus, 
				currentUser, RoleUtils.ROLE_ADMIN, ownerIds, statuses);
		if (!valid) {
			return new SearchResult<News>();
		}
		
		List<ObjectId> newsIds = new ArrayList<>();
		List<ObjectId> ignoreNewsIds = new ArrayList<>();
		List<ObjectId> categoryIds = dbContext.parseMulti(searchDto.getCategoryIds(), true);
		
		List<ObjectId> countryIds = new ArrayList<>();
		List<ObjectId> provinceIds = new ArrayList<>();
		List<ObjectId> districtIds = dbContext.parseMulti(searchDto.getDistrictIds(), true);
		List<ObjectId> locationIds = new ArrayList<>();
		
		ObjectId provinceId = dbContext.parse(searchDto.getProvinceId(), null);
		if (provinceId != null) {
			provinceIds.add(provinceId);
		}
		
		if (!ownerIds.isEmpty()) {
			authorName = null;
		}
		
		List<ObjectId> authorIds = dbContext.parseMulti(searchDto.getAuthorIds(), true);
		if (authorIds != null && !authorIds.isEmpty()) {
			ownerIds.addAll(authorIds);
		}
		
		List<ObjectId> withMySelfIds = new ArrayList<>();
		boolean withoutExpired = true;
		Boolean active = null;
		
		if (currentUser != null && currentUser.isActive() 
				&& (currentUser.isAdministrator() 
						|| currentUser.isDepartmentAdministrator())) {
			withoutExpired = !StringUtils.isNullOrEmpty(searchDto.getWithoutClosed()) 
					&& ConstantUtils.YES_VALUE.equalsIgnoreCase(searchDto.getWithoutClosed());
			
			if (searchStatus == ConstantUtils.SEARCH_STATUS_ALL) {
				withMySelfIds.add(currentUser.getId());
			}
		} else {
			active = true;
		}
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		
		Pageable pageable = new PageRequest(page, pageSize);
		Integer searchSort = searchDto.getSort();
		
		Sort sort = projectRepository.getSort(dbContext);
		if (searchSort == null || searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, News.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, News.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, News.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, News.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, News.COLUMNNAME_STATUS_VALUE),
					new Order(Direction.DESC, News.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, News.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_STATUS_VALUE_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, News.COLUMNNAME_STATUS_VALUE),
					new Order(Direction.DESC, News.COLUMNNAME_STATUS_DATE_VALUE),
					new Order(Direction.ASC, News.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_ASC) {
			sort = new Sort(
					new Order(Direction.ASC, News.COLUMNNAME_NAME)
			);
		} else if (searchSort == ConstantUtils.SEARCH_SORT_NAME_DESC) {
			sort = new Sort(
					new Order(Direction.DESC, News.COLUMNNAME_NAME)
			);
		}
		
		List<News> domains = newsRepository.getList(dbContext, text, newsName, authorName, 
				newsIds, ignoreNewsIds, ownerIds, categoryIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				statuses, withMySelfIds, withoutExpired, active, pageable, sort);
		
		long itemCount = newsRepository.count(dbContext, text, newsName, authorName, 
				newsIds, ignoreNewsIds, ownerIds, categoryIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				statuses, withMySelfIds, withoutExpired, active);
		
		return new SearchResult<News>(domains, page, itemCount);
	}
	
	private boolean processSearchStatus(int searchStatus, User currentUser, String mainUserRole,
			List<ObjectId> ownerIds, List<Integer> statuses) {
		if (currentUser != null && currentUser.isActive()) {
			if ((RoleUtils.ROLE_COMPANY.equalsIgnoreCase(mainUserRole) && currentUser.isCompany())
					|| (RoleUtils.ROLE_FARMER.equalsIgnoreCase(mainUserRole) && currentUser.isFarmer())
					|| (RoleUtils.ROLE_SCIENTIST.equalsIgnoreCase(mainUserRole) && currentUser.isScientist())
					|| (RoleUtils.ROLE_ADMIN.equalsIgnoreCase(mainUserRole) 
							&& (currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()))
					|| (RoleUtils.ROLE_DEPARTMENT_ADMIN.equalsIgnoreCase(mainUserRole)
							&& (currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()))) {
				// Search draft item: owner, status is pending
				if (searchStatus == ConstantUtils.SEARCH_STATUS_DRAFT) {
					ownerIds.clear();
					ownerIds.add(currentUser.getId());

					statuses.add(INeedApprove.APPROVE_STATUS_PENDING);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_PROGRESS) {
					ownerIds.clear();
					
					if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
						ownerIds.add(currentUser.getId());
					}
					
					statuses.add(INeedApprove.APPROVE_STATUS_IN_PROGRESS);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_APPROVED) {
					ownerIds.clear();
					
					if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
						ownerIds.add(currentUser.getId());
					}

					statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_REJECTED) {
					ownerIds.clear();
					
					if (!currentUser.isAdministrator() && !currentUser.isDepartmentAdministrator()) {
						ownerIds.add(currentUser.getId());
					}

					statuses.add(INeedApprove.APPROVE_STATUS_REJECTED);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_OWNER) {
					ownerIds.clear();
					ownerIds.add(currentUser.getId());
				} else {
					statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				}

				return true;
			} else if (currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()) {
				if (searchStatus == ConstantUtils.SEARCH_STATUS_DRAFT
						|| searchStatus == ConstantUtils.SEARCH_STATUS_OWNER) {
					return false;
				}

				if (searchStatus == ConstantUtils.SEARCH_STATUS_PROGRESS) {
					statuses.add(INeedApprove.APPROVE_STATUS_IN_PROGRESS);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_APPROVED) {
					statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				} else if (searchStatus == ConstantUtils.SEARCH_STATUS_REJECTED) {
					statuses.add(INeedApprove.APPROVE_STATUS_REJECTED);
				} else {
					statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
				}

				return true;
			}
		}

		if (searchStatus == ConstantUtils.SEARCH_STATUS_DRAFT 
				|| searchStatus == ConstantUtils.SEARCH_STATUS_OWNER
				|| searchStatus == ConstantUtils.SEARCH_STATUS_PROGRESS
				|| searchStatus == ConstantUtils.SEARCH_STATUS_REJECTED) {
			return false;
		}

		statuses.add(INeedApprove.APPROVE_STATUS_APPROVED);
		return true;
	}

	private Boolean changeBooleanValue(String value) {
		if (ConstantUtils.YES_VALUE.equalsIgnoreCase(value)) {
			return true;
		}
		
		if (ConstantUtils.NO_VALUE.equalsIgnoreCase(value)) {
			return false;
		}
		
		return null;
	}
	
	protected class SearchResult<T> {
		private List<T> items;
		private int page;
		private long count;
		
		public SearchResult() {
			this(new ArrayList<>(), 0, 0);
		}

		public SearchResult(List<T> items, int page, long count) {
			super();
			this.items = items;
			this.page = page;
			this.count = count;
		}

		public List<T> getItems() {
			return items;
		}

		public void setItems(List<T> items) {
			this.items = items;
		}

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}
	}
}
