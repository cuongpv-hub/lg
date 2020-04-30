package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.validator.routines.EmailValidator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.domain.Company;
import vn.vsd.agro.domain.Farmer;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.dto.BaseResourceCreateDto;
import vn.vsd.agro.dto.Envelope;
import vn.vsd.agro.dto.ErrorDto;
import vn.vsd.agro.dto.IdSelectDto;
import vn.vsd.agro.dto.LoginAndLinkDto;
import vn.vsd.agro.dto.LoginedUserDto;
import vn.vsd.agro.dto.Meta;
import vn.vsd.agro.dto.RegisterAndLinkDto;
import vn.vsd.agro.repository.CompanyRepository;
import vn.vsd.agro.repository.FarmerRepository;
import vn.vsd.agro.repository.LandRepository;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.ScientistRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.AddressUtils;
import vn.vsd.agro.util.PasswordUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;

public abstract class BaseResourceController extends BaseItemController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private FarmerRepository farmerRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private LandRepository landRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
	protected abstract int getResourceType();
	
	protected abstract boolean joinWhenLogin(User user, int linkType, String resourceId); 
	
	protected abstract String gotoViewPage(String id, String target);
	
	public abstract String detail(HttpSession session, Model model, String id);
	
	@RequestMapping(value = "/{id}/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> submitLogin(HttpSession session, 
			@PathVariable(name = "id", required = true) String id,
			@RequestBody LoginAndLinkDto loginDto) {

		if (loginDto == null) {
			return this.responseErrorEntity(new Exception("Invalid parameter"));
		}

		String account = loginDto.getAccount();
		String password = loginDto.getPassword();
		if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(password)) {
			return this.responseErrorEntity(new Exception("Invalid parameter"));
		}
		
		User user = userRepository.findByEmailOrPhone(account);
		if (user == null || !user.isActive()) {
			return this.responseErrorEntity(new Exception("Cannot login"));
		}

		boolean validPassword = PasswordUtils.matches(password, user.getPassword());
		if (!validPassword) {
			return this.responseErrorEntity(new Exception("Cannot login"));
		}
		
		// Cập nhật access token
		String accessToken = UUID.randomUUID().toString();
		if (!userRepository.updateAccessToken(user.getId(), accessToken)) {
			return this.responseErrorEntity(new Exception("Cannot login"));
		}
		
		this.joinWhenLogin(user, loginDto.getLinkType(), id);
		
		session.setAttribute(SESSION_KEY_ACCESS_TOKEN, accessToken);
		
		LoginedUserDto userDto = new LoginedUserDto();
		userDto.setAccessToken(accessToken);
		
		userDto.setUserId(user.idAsString());
		userDto.setName(user.getName());
		userDto.setAddress(AddressUtils.getFullAddress(user.getAddress(), user.getLocation()));
		userDto.setEmail(user.getEmail());
		userDto.setPhone(user.getMobilePhone());
		
		if (StringUtils.isNullOrEmpty(userDto.getPhone())) {
			userDto.setPhone(user.getCompanyPhone());
			
			if (StringUtils.isNullOrEmpty(userDto.getPhone())) {
				userDto.setPhone(user.getHomePhone());
			}
		}
		
		userDto.setCompany(user.isCompany());
		userDto.setFarmer(user.isFarmer());
		userDto.setScientist(user.isScientist());
		userDto.setAdmin(user.isAdministrator());
		userDto.setDeptAdmin(user.isDepartmentAdministrator());
		
		return new Envelope(userDto).toResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/register", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> submitRegister(HttpSession session, 
			@PathVariable(name = "id", required = true) String id,
			@RequestBody RegisterAndLinkDto registerDto) {

		if (registerDto == null) {
			return this.responseErrorEntity(new Exception("Invalid parameter"));
		}

		IContext<ObjectId> dbContext = MongoContext.ROOT_CONTEXT;
		
		String name = registerDto.getName().trim();
		String email = registerDto.getEmail().trim();
		String phone = registerDto.getMobilePhone().trim();
		String password = registerDto.getPassword();
		String confirmPassword = registerDto.getConfirmPassword();
		
		if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(phone)
				|| StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(confirmPassword)
				|| StringUtils.isNullOrEmpty(email) || !emailValidator.isValid(email)) {
			return this.responseErrorEntity(new Exception("Invalid parameter"));
		}
		
		if (!password.equals(confirmPassword)) {
			ErrorDto dto = new ErrorDto("password", "Invalid parameter");
			return new Envelope(dto).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userRepository.existEmail(email)) {
			ErrorDto dto = new ErrorDto("email", "Invalid parameter");
			return new Envelope(dto).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (userRepository.existPhone(phone)) {
			ErrorDto dto = new ErrorDto("phone", "Invalid parameter");
			return new Envelope(dto).toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ObjectId rootId = dbContext.getRootId();
		String accessToken = UUID.randomUUID().toString();
		
		String encryptPassword = PasswordUtils.encode(registerDto.getPassword());
		
		User newUser = new User();
		newUser.setName(name);
		newUser.setPassword(encryptPassword);
		newUser.setEmail(email);
		newUser.setMobilePhone(phone);
		newUser.setCompanyPhone(registerDto.getCompanyPhone());
		newUser.setHomePhone(registerDto.getHomePhone());
		newUser.setFax(registerDto.getFax());
		newUser.setAddress(registerDto.getAddress());
		
		newUser.addRole(RoleUtils.ROLE_USER, false);
		for (String role : registerDto.getRoles()) {
			newUser.addRole(role, false);
		}
		
		newUser.addAccessOrg(rootId);
		newUser.setCurrentOrgId(rootId);
		newUser.setAccessToken(accessToken);
		newUser.setValidToken(accessToken);
		newUser.setActive(true);
		
		User registedUser = userRepository.save(dbContext, newUser);
		if (registedUser == null) {
			return this.responseErrorEntity(new Exception("Error when create new user"));
		}
		
		ObjectId newUserId = registedUser.getId();
		
		if (registedUser.isCompany()) {
			Company company = new Company();
			company.setId(newUserId);
			company.setUserId(newUserId);
			company.setSearchUsers(registedUser.getSearchValues());
			companyRepository.save(dbContext, company);
		}
		
		if (registedUser.isFarmer()) {
			Farmer farmer = new Farmer();
			farmer.setId(newUserId);
			farmer.setUserId(newUserId);
			farmer.setSearchUsers(registedUser.getSearchValues());
			farmerRepository.save(dbContext, farmer);
		}
		
		if (registedUser.isScientist()) {
			Scientist scientist = new Scientist();
			scientist.setId(newUserId);
			scientist.setUserId(newUserId);
			scientist.setSearchUsers(registedUser.getSearchValues());
			scientistRepository.save(dbContext, scientist);
		}
		
		this.joinWhenLogin(registedUser, registerDto.getLinkType(), id);
		
		session.setAttribute(SESSION_KEY_ACCESS_TOKEN, accessToken);
		
		LoginedUserDto userDto = new LoginedUserDto();
		userDto.setAccessToken(accessToken);
		
		userDto.setUserId(registedUser.idAsString());
		userDto.setName(registedUser.getName());
		userDto.setAddress(AddressUtils.getFullAddress(registedUser.getAddress(), registedUser.getLocation()));
		userDto.setEmail(registedUser.getEmail());
		userDto.setPhone(registedUser.getMobilePhone());
		
		if (StringUtils.isNullOrEmpty(userDto.getPhone())) {
			userDto.setPhone(registedUser.getCompanyPhone());
			
			if (StringUtils.isNullOrEmpty(userDto.getPhone())) {
				userDto.setPhone(registedUser.getHomePhone());
			}
		}
		
		userDto.setCompany(registedUser.isCompany());
		userDto.setFarmer(registedUser.isFarmer());
		userDto.setScientist(registedUser.isScientist());
		userDto.setAdmin(registedUser.isAdministrator());
		userDto.setDeptAdmin(registedUser.isDepartmentAdministrator());
		
		return new Envelope(userDto).toResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/link", method = { RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<?> addLink(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String _resourceId,
			@RequestBody IdSelectDto linkDto) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.responseErrorEntity(new Exception("Anonymous can not access this API."));
		}
		
		if (linkDto == null) {
			return this.responseErrorEntity(new Exception("Invalid input parameter."));
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId resourceId = dbContext.parse(_resourceId, null);
		if (resourceId == null) {
			return this.responseErrorEntity(new Exception("Invalid input parameter."));
		}
		
		List<ObjectId> linkIds = dbContext.parseMulti(linkDto.getIds(), true);
		if (linkIds == null || linkIds.isEmpty()) {
			return this.responseErrorEntity(new Exception("Invalid input parameter."));
		}
		
		Set<ObjectId> removeLinkIds = new HashSet<>();
		List<Link> newLinks = new ArrayList<>();
		Map<ObjectId, Link> currentLinkMap = new HashMap<>();
		
		List<Link> currentLinks = linkRepository.getList(dbContext, 
				Arrays.asList(resourceId), null, null, null, null, null, null, null, null, null, null);
		if (currentLinks != null) {
			for (Link link : currentLinks) {
				currentLinkMap.put(link.getLinkId(), link);
			}
		}
		
		int linkType = linkDto.getType();
		if (linkType == IdSelectDto.LINK_TYPE_PROJECT) {
			int resourceLinkType = -1;
			ObjectId farmerId = null;
			
			int recourceType = this.getResourceType();
			if (recourceType == IdSelectDto.LINK_TYPE_PRODUCT) {
				resourceLinkType = Link.LINK_TYPE_PRODUCT;
				
				Product domain = productRepository.getById(dbContext, resourceId, Product.COLUMNNAME_CONTACT);
				if (domain == null) {
					return this.responseErrorEntity(new Exception("Invalid input parameter."));
				}
				
				if (domain.getContact() != null) {
					farmerId = domain.getContact().getId();
				}
			} else if (recourceType == IdSelectDto.LINK_TYPE_LAND) {
				resourceLinkType = Link.LINK_TYPE_LAND;
				
				Land domain = landRepository.getById(dbContext, resourceId, Land.COLUMNNAME_CONTACT);
				if (domain == null) {
					return this.responseErrorEntity(new Exception("Invalid input parameter."));
				}
				
				if (domain.getContact() != null) {
					farmerId = domain.getContact().getId();
				}
			} else {
				return this.responseErrorEntity(new Exception("Invalid input parameter."));
			}
			
			this.linkProjects(dbContext, farmerId, resourceId, linkIds, 
					resourceLinkType, currentLinkMap, newLinks, removeLinkIds);
		} else {
			Project domain = projectRepository.getById(dbContext, resourceId, Project.COLUMNNAME_CONTACT);
			if (domain == null) {
				return this.responseErrorEntity(new Exception("Invalid input parameter."));
			}
			
			ObjectId companyId = null;
			if (domain.getContact() != null) {
				companyId = domain.getContact().getId(); 
			}
			
			if (linkType == IdSelectDto.LINK_TYPE_PRODUCT) {
				this.linkProducts(dbContext, companyId, resourceId, 
						linkIds, currentLinkMap, newLinks, removeLinkIds);
			} else if (linkType == IdSelectDto.LINK_TYPE_LAND) {
				this.linkLands(dbContext, companyId, resourceId, 
						linkIds, currentLinkMap, newLinks, removeLinkIds);
			} else if (linkType == IdSelectDto.LINK_TYPE_SCIENTIST) {
				this.linkScientists(dbContext, companyId, resourceId, 
						linkIds, currentLinkMap, newLinks, removeLinkIds);
			}
		}
		
		if (!removeLinkIds.isEmpty()) {
			linkRepository.deleteBatch(dbContext, removeLinkIds);
		}
		
		if (!newLinks.isEmpty()) {
			linkRepository.saveBatch(dbContext, newLinks);
		}
		
		return new Envelope(Meta.OK).toResponseEntity(HttpStatus.OK);
	}
	
	private Link getCanAccessLink(IContext<ObjectId> dbContext, User currentUser, 
			ObjectId resourceId, ObjectId linkId) {
		
		if (currentUser == null || !currentUser.isActive() || resourceId == null || linkId == null) {
			return null;
		}
		
		Link link = linkRepository.getById(dbContext, linkId);
		if (link == null) {
			return null;
		}
		
		ObjectId userId = currentUser.getId();
		
		int resourceType = this.getResourceType();
		if (resourceType == IdSelectDto.LINK_TYPE_PROJECT) {
			if (!dbContext.equals(resourceId, link.getProjectId())) {
				return null;
			}
			
			Project project = projectRepository.getById(dbContext, resourceId, Project.COLUMNNAME_CONTACT);
			if (project == null || project.isClosed()) {
				return null;
			}
			
			// Check link with own project
			if (project.getContact() != null) {
				ObjectId companyId = project.getContact().getId();
				
				if (currentUser.isCompany() && dbContext.equals(companyId, userId)) {
					return link;
				}
			}
		} else {
			if (!dbContext.equals(resourceId, link.getLinkId())) {
				return null;
			}
			
			if (resourceType == IdSelectDto.LINK_TYPE_PRODUCT) {
				Product product = productRepository.getById(dbContext, resourceId, Product.COLUMNNAME_CONTACT);
				if (product == null || product.isClosed()) {
					return null;
				}
				
				// Check link with own product
				if (product.getContact() != null) {
					ObjectId farmerId = product.getContact().getId();
					
					if (currentUser.isFarmer() && dbContext.equals(farmerId, userId)) {
						return link;
					}
				}
			} else if (resourceType == IdSelectDto.LINK_TYPE_LAND) {
				Land land = landRepository.getById(dbContext, resourceId, Land.COLUMNNAME_CONTACT);
				if (land == null || land.isClosed()) {
					return null;
				}
				
				// Check link with own land
				if (land.getContact() != null) {
					ObjectId farmerId = land.getContact().getId();
					
					if (currentUser.isFarmer() && dbContext.equals(farmerId, userId)) {
						return link;
					}
				}
			}
		}
		
		// Check link with own land/product/scientist
		if (dbContext.equals(link.getCompanyId(), userId)
				|| dbContext.equals(link.getLinkOwnerId(), userId)) {
			return link;
		}
		
		return null;
	}
	
	@RequestMapping(value = "/{resourceId}/link/{linkId}/delete", 
			method = { RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT })
	public String removeLink(HttpSession session, Model model, 
			@PathVariable(name = "resourceId", required = true) String _resourceId,
			@PathVariable(name = "linkId", required = true) String _linkId) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return detail(session, model, _resourceId);	
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId resourceId = dbContext.parse(_resourceId, null);
		ObjectId linkId = dbContext.parse(_linkId, null);
		
		Link link = this.getCanAccessLink(dbContext, currentUser, resourceId, linkId);
		
		// Delete link with own project or own land/product/scientist
		if (link != null) {
			linkRepository.delete(dbContext, linkId);
			
			String linkTarget = "addProject";
			
			if (this.getResourceType() == IdSelectDto.LINK_TYPE_PROJECT) {
				if (link.getLinkType() == Link.LINK_TYPE_PRODUCT) {
					linkTarget = "addProduct";
				} else if (link.getLinkType() == Link.LINK_TYPE_LAND) {
					linkTarget = "addLand";
				} else if (link.getLinkType() == Link.LINK_TYPE_SCIENTIST) {
					linkTarget = "addScientist";
				}
			}
			
			return this.gotoViewPage(_resourceId, linkTarget);
		}
		
		return this.gotoViewPage(_resourceId);
	}
	
	private Link updateLinkStatus(User currentUser, String _resourceId, String _linkId, int newStatusValue) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId resourceId = dbContext.parse(_resourceId, null);
		ObjectId linkId = dbContext.parse(_linkId, null);
		
		if (resourceId == null || linkId == null) {
			return null;
		}
		
		Link link = this.getCanAccessLink(dbContext, currentUser, resourceId, linkId);
		if (link == null) {
			return null;
		}
		
		StatusEmbed status = link.getStatus();
		if (status == null || status.getStatus() != newStatusValue) {
			StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, newStatusValue);
			link.setStatus(newStatus);
			
			if (status != null) {
				link.addStatusHistory(status);
			}
			
			// Update current link status
			linkRepository.save(dbContext, link);
			
			// Update other links if link resource is product or land
			if (link.getLinkType() != Link.LINK_TYPE_SCIENTIST && newStatusValue == Link.STATUS_APPROVED) {
				// Find links with other projects
				
				List<Link> otherLinks = linkRepository.getList(dbContext, null, Arrays.asList(link.getProjectId()), 
						null, null, Arrays.asList(link.getLinkId()), null, null, null, null, null, null);
				
				if (otherLinks != null && !otherLinks.isEmpty()) {
					List<ObjectId> linkIds = new ArrayList<>();
					
					for (Link otherLink : otherLinks) {
						linkIds.add(otherLink.getId());
					}
					
					// TODO: Delete other links
					linkRepository.deleteBatch(dbContext, linkIds);
					
					// OR: Update other links
					// linkRepository.updateStatus(dbContext, linkIds, Link.STATUS_APPROVED_OTHER_PROJECT);
				}
			}
		}
		
		return link;
	}
	
	@RequestMapping(value = "/{resourceId}/link/{linkId}/approve", 
			method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public String approveLink(HttpSession session, Model model, 
			@PathVariable(name = "resourceId", required = true) String _resourceId,
			@PathVariable(name = "linkId", required = true) String _linkId) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser != null && currentUser.isActive()) {
			Link link = this.updateLinkStatus(currentUser, _resourceId, _linkId, Link.STATUS_APPROVED);
			
			if (link != null) {
				String linkTarget = "addProject";
				
				if (this.getResourceType() == IdSelectDto.LINK_TYPE_PROJECT) {
					if (link.getLinkType() == Link.LINK_TYPE_PRODUCT) {
						linkTarget = "addProduct";
					} else if (link.getLinkType() == Link.LINK_TYPE_LAND) {
						linkTarget = "addLand";
					} else if (link.getLinkType() == Link.LINK_TYPE_SCIENTIST) {
						linkTarget = "addScientist";
					}
				}
				
				return this.gotoViewPage(_resourceId, linkTarget);
			}
		}
		
		return this.gotoViewPage(_resourceId);
	}
	
	@RequestMapping(value = "/{resourceId}/link/{linkId}/reject", 
			method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public String rejectLink(HttpSession session, Model model, 
			@PathVariable(name = "resourceId", required = true) String _resourceId,
			@PathVariable(name = "linkId", required = true) String _linkId) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser != null && currentUser.isActive()) {
			Link link = this.updateLinkStatus(currentUser, _resourceId, _linkId, Link.STATUS_REJECTED);
			
			if (link != null) {
				String linkTarget = "addProject";
				
				if (this.getResourceType() == IdSelectDto.LINK_TYPE_PROJECT) {
					if (link.getLinkType() == Link.LINK_TYPE_PRODUCT) {
						linkTarget = "addProduct";
					} else if (link.getLinkType() == Link.LINK_TYPE_LAND) {
						linkTarget = "addLand";
					} else if (link.getLinkType() == Link.LINK_TYPE_SCIENTIST) {
						linkTarget = "addScientist";
					}
				}
				
				return this.gotoViewPage(_resourceId, linkTarget);
			}
		}
		
		return this.gotoViewPage(_resourceId);
	}
	
	protected boolean linkProducts(IContext<ObjectId> dbContext, 
			ObjectId companyId, ObjectId projectId,
			Collection<ObjectId> linkIds, Map<ObjectId, Link> currentLinkMap, 
			List<Link> newLinks, Set<ObjectId> removeLinkIds) {
		if (linkIds == null || linkIds.isEmpty()) {
			return false;
		}
		
		List<Product> products = productRepository.getListByIds(dbContext, linkIds,
				Product.COLUMNNAME_CONTACT);
		if (products == null) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, Link.STATUS_PENDING);
		
		for (Product product : products) {
			ObjectId productId = product.getId();
			
			if (currentLinkMap != null && currentLinkMap.containsKey(productId)) {
				if (removeLinkIds != null) {
					Link link = currentLinkMap.get(productId);
					removeLinkIds.remove(link.getId());
				}
			} else {
				ObjectId linkOwnerId = null;
				
				ContactEmbed farmer = product.getContact();
				if (farmer != null) {
					linkOwnerId = farmer.getId();
				}
				
				Link newLink = new Link();
				newLink.setLinkType(Link.LINK_TYPE_PRODUCT);
				newLink.setCompanyId(companyId);
				newLink.setProjectId(projectId);
				newLink.setLinkId(productId);
				newLink.setLinkOwnerId(linkOwnerId);
				newLink.setStatus(newStatus);
				
				newLinks.add(newLink);
			}
		}
		
		return true;
	}
	
	protected boolean linkProjects(IContext<ObjectId> dbContext, 
			ObjectId farmerId, ObjectId resourceId, Collection<ObjectId> projectIds, 
			int resourceLinkType, Map<ObjectId, Link> currentLinkMap, 
			List<Link> newLinks, Set<ObjectId> removeLinkIds) {
		if (projectIds == null || projectIds.isEmpty()) {
			return false;
		}
		
		List<Project> projects = projectRepository.getListByIds(dbContext, projectIds,
				Project.COLUMNNAME_CONTACT);
		if (projects == null) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, Link.STATUS_PENDING);
		
		for (Project project : projects) {
			ObjectId projectId = project.getId();
			
			if (currentLinkMap != null && currentLinkMap.containsKey(projectId)) {
				if (removeLinkIds != null) {
					Link link = currentLinkMap.get(projectId);
					removeLinkIds.remove(link.getId());
				}
			} else {
				ObjectId companyId = null;
				
				ContactEmbed company = project.getContact();
				if (company != null) {
					companyId = company.getId();
				}
				
				Link newLink = new Link();
				newLink.setLinkType(resourceLinkType);
				newLink.setCompanyId(companyId);
				newLink.setProjectId(projectId);
				newLink.setLinkId(resourceId);
				newLink.setLinkOwnerId(farmerId);
				newLink.setStatus(newStatus);
				
				newLinks.add(newLink);
			}
		}
		
		return true;
	}
	
	protected boolean linkLands(IContext<ObjectId> dbContext, 
			ObjectId companyId, ObjectId projectId, 
			Collection<ObjectId> linkIds, Map<ObjectId, Link> currentLinkMap, 
			List<Link> newLinks, Set<ObjectId> removeLinkIds) {
		if (linkIds == null || linkIds.isEmpty()) {
			return false;
		}
		
		List<Land> lands = landRepository.getListByIds(dbContext, linkIds,
				Land.COLUMNNAME_CONTACT);
		if (lands == null) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, Link.STATUS_PENDING);
		for (Land land : lands) {
			ObjectId landId = land.getId();
			
			if (currentLinkMap != null && currentLinkMap.containsKey(landId)) {
				if (removeLinkIds != null) {
					Link link = currentLinkMap.get(landId);
					removeLinkIds.remove(link.getId());
				}
			} else {
				ObjectId linkOwnerId = null;
				
				ContactEmbed farmer = land.getContact();
				if (farmer != null) {
					linkOwnerId = farmer.getId();
				}
				
				Link newLink = new Link();
				newLink.setLinkType(Link.LINK_TYPE_LAND);
				newLink.setCompanyId(companyId);
				newLink.setProjectId(projectId);
				newLink.setLinkId(landId);
				newLink.setLinkOwnerId(linkOwnerId);
				newLink.setStatus(newStatus);
				
				newLinks.add(newLink);
			}
		}
		
		return true;
	}
	
	protected boolean linkScientists(IContext<ObjectId> dbContext, 
			ObjectId companyId, ObjectId projectId, 
			Collection<ObjectId> linkIds, Map<ObjectId, Link> currentLinkMap, 
			List<Link> newLinks, Set<ObjectId> removeLinkIds) {
		if (linkIds == null || linkIds.isEmpty()) {
			return false;
		}
		
		List<Scientist> scientists = scientistRepository.getListByIds(dbContext, linkIds,
				Scientist.COLUMNNAME_ID);
		if (scientists == null) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(dbContext, Link.STATUS_PENDING);
		for (Scientist scientist : scientists) {
			ObjectId scientistId = scientist.getId();
			
			if (currentLinkMap != null && currentLinkMap.containsKey(scientistId)) {
				if (removeLinkIds != null) {
					Link link = currentLinkMap.get(scientistId);
					removeLinkIds.remove(link.getId());
				}
			} else {
				ObjectId linkOwnerId = scientist.getUserId();
				
				Link newLink = new Link();
				newLink.setLinkType(Link.LINK_TYPE_SCIENTIST);
				newLink.setCompanyId(companyId);
				newLink.setProjectId(projectId);
				newLink.setLinkId(scientistId);
				newLink.setLinkOwnerId(linkOwnerId);
				newLink.setStatus(newStatus);
				
				newLinks.add(newLink);
			}
		}
		
		return true;
	}
	
	protected ContactEmbed createContactDomain(IContext<ObjectId> dbContext, 
			User owner, BindingResult result, BaseResourceCreateDto dto) {
		
		String prefixMsg = "";
		int resourceType = this.getResourceType();
		if (resourceType == IdSelectDto.LINK_TYPE_PRODUCT) {
			prefixMsg = "product";
		} else if (resourceType == IdSelectDto.LINK_TYPE_LAND) {
			prefixMsg = "land";
		} else if (resourceType == IdSelectDto.LINK_TYPE_PROJECT) {
			prefixMsg = "project";
		}
		
		ObjectId locationId = dbContext.parse(dto.getContactLocationId(), null);
		if (locationId == null) {
			this.addModelError(result, "dto", "contactLocationId", null, prefixMsg + ".message.invalid.contact.location");
		}
		
		String email = StringUtils.trimText(dto.getContactEmail());
		String address = StringUtils.trimText(dto.getContactAddress());
		String mobilePhone = StringUtils.trimText(dto.getContactMobile());
		String companyPhone = StringUtils.trimText(dto.getContactPhone());
		String homePhone = StringUtils.trimText(dto.getContactHomePhone());
		String contactName = StringUtils.trimText(dto.getContactName());
		String contactAlias = StringUtils.trimText(dto.getContactAlias());
		
		if (StringUtils.isNullOrEmpty(mobilePhone) 
				&& StringUtils.isNullOrEmpty(companyPhone)
				&& StringUtils.isNullOrEmpty(homePhone)) {
			this.addModelError(result, "dto", "contactMobile", null, prefixMsg + ".message.not.empty.contact.phone");
		}
		
		Location location = locationRepository.getById(dbContext, locationId);
		if (location == null) {
			this.addModelError(result, "dto", "contactLocationId", locationId.toString(), prefixMsg + ".message.invalid.contact.location");
		}
		
		if (result.hasErrors()) {
			return null;
		}
		
		ContactEmbed domain = new ContactEmbed();
		domain.setId(owner.getId());
		domain.setName(contactName);
		domain.setAlias(contactAlias);
		domain.setMobilePhone(mobilePhone);
		domain.setCompanyPhone(companyPhone);
		domain.setHomePhone(homePhone);
		domain.setFax(dto.getContactFax());
		domain.setEmail(email);
		domain.setAddress(address);
		domain.setLocation(new LocationEmbed(location));
		
		return domain;
	}
	
	protected void copyBaseResourceCreateDto(IContext<ObjectId> dbContext, 
			User user, BaseResourceCreateDto dto) {
		dto.setContactName(user.getName());
		dto.setContactAddress(user.getAddress());
		dto.setContactEmail(user.getEmail());
		dto.setContactMobile(user.getMobilePhone());
		dto.setContactPhone(user.getCompanyPhone());
		dto.setContactHomePhone(user.getHomePhone());
		dto.setContactFax(user.getFax());
		
		LocationEmbed location = user.getLocation();
		if (location != null) {
			dto.setContactLocationId(location.idAsString());
			
			DistrictEmbed district = location.getDistrict();
			if (district != null) {
				dto.setContactDistrictId(district.idAsString());
				
				ProvinceEmbed province = district.getProvince();
				if (province != null) {
					dto.setContactProvinceId(province.idAsString());
					
					CountryEmbed country = province.getCountry();
					if (country != null) {
						dto.setContactCountryId(country.idAsString());
					}
				}
			}
		}
		
		if (user.isCompany()) {
			Company company = companyRepository.getById(dbContext, user.getId(), Company.COLUMNNAME_BRAND_NAME);
			if (company != null) {
				dto.setContactAlias(company.getBrandName());
			}
		}
	}
}
