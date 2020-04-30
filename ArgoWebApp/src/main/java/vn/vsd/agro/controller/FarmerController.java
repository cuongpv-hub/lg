package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;

import vn.vsd.agro.domain.Farmer;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;

import vn.vsd.agro.dto.FarmerSearchDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ProductSimpleDto;

import vn.vsd.agro.repository.FarmerRepository;
import vn.vsd.agro.repository.LandRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;


@Controller
@RequestMapping("/farmer")
public class FarmerController extends BaseController {
	
	private static final String SEARCH_SESSION_KEY = "FarmerSearch";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FarmerRepository farmerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private LandRepository landRepository;
	
	@RequestMapping()
	public String Farmer(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> farmerDtos = new ArrayList<>();
		List<ProductSimpleDto> productDtos = new ArrayList<>();
		List<LandSimpleDto> landDtos = new ArrayList<>();
		
		// farmers
		PageRequest farmerPageable = new PageRequest(0, ConstantUtils.FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		List<Farmer> farmers = farmerRepository.getList(dbContext, null, 
				true, farmerPageable, farmerRepository.getSort(dbContext));
		
		if (farmers != null && !farmers.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Farmer Farmer : farmers) {
				userIds.add(Farmer.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Farmer Farmer : farmers) {
				User user = userMaps.get(Farmer.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						null, Farmer, null, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					farmerDtos.add(dto);
				}
			}
		}
		
		// products
		PageRequest productPageable = new PageRequest(0, ConstantUtils.FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		Collection<Integer> statuses = Arrays.asList(Product.APPROVE_STATUS_APPROVED);
		
		
		
		List<Product> products = productRepository.getList(dbContext, 
				null, null, null, null, null, null, null, null, null, null, null, statuses, null, false, true, productPageable, productRepository.getSort(dbContext));
		
		if (products != null && !products.isEmpty()) {
			Set<ObjectId> FarmerIds = new HashSet<>();
			for (Product product : products) {
				ContactEmbed Farmer = product.getContact();
				if (Farmer != null) {
					FarmerIds.add(Farmer.getId());
				}
			}
			
			List<User> productContacts = userRepository.getListByIds(dbContext, FarmerIds);
			Map<ObjectId, User> productContactMaps = new HashMap<>();
			for (User productContact : productContacts) {
				productContactMaps.put(productContact.getId(), productContact);
			}
			
			for (Product product : products) {
				String authorAvatar = null;
				
				ContactEmbed Farmer = product.getContact();
				if (Farmer != null) {
					User productContact = productContactMaps.get(Farmer.getId());
					
					if (productContact != null) {
						authorAvatar = productContact.getAvatar();
					}
				}
				
				ProductSimpleDto dto = DomainConverter.createProductSimpleDto(product, 
						currentUser, authorAvatar, false, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					productDtos.add(dto);
				}
			}
		}
		
		
		List<Land> lands = landRepository.getList(dbContext, 
				null, null, null, null, null, null, null, null, null, null, null,
				statuses, null, false, true, productPageable, landRepository.getSort(dbContext));
		
		if (lands != null && !lands.isEmpty()) {
			for (Land land : lands) {
				LandSimpleDto dto = DomainConverter.createLandSimpleDto(land, 
						currentUser, "", false, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					landDtos.add(dto);
				}
			}
		}
		
		model.addAttribute("productDtos", productDtos);
		model.addAttribute("farmerDtos", farmerDtos);
		model.addAttribute("landDtos", landDtos);
		
		return "farmer";
	}
	
	@RequestMapping({ "/list", "/search" })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") FarmerSearchDto searchDto) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> dtos = new ArrayList<>();
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		PageRequest farmerPageable = new PageRequest(0, ConstantUtils.FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		
		List<Farmer> farmers = farmerRepository.getList(dbContext, null, 
				true, farmerPageable, farmerRepository.getSort(dbContext));
		
		// TODO: 
		/*long itemCount = farmerRepository.count(dbContext, 
				productName, FarmerName, ownerIds, categoryIds, scientistIds, 
				approveDeptIds, districtIds, statuses, 
				periodValue, withoutClosed, active);*/
		long itemCount = 100;
		long pageCount = itemCount / ConstantUtils.FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE;
		if (itemCount % ConstantUtils.FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE != 0) {
			pageCount++;
		}
		
		if (farmers != null && !farmers.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Farmer Farmer : farmers) {
				userIds.add(Farmer.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Farmer Farmer : farmers) {
				User user = userMaps.get(Farmer.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						null, Farmer, null, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					dtos.add(dto);
				}
				
			}
		}
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_AVATAR);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		
		return "Farmer-list";
	}
	
	@RequestMapping("/page")
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		FarmerSearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof FarmerSearchDto) {
			searchDto = (FarmerSearchDto) lastSearch;
		} else {
			searchDto = new FarmerSearchDto();
		}
		
		searchDto.setPage(page);
		return list(session, model, searchDto);
	}
	
	/*@RequestMapping("/{id}/view")
	public String FarmerDetail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String FarmerId) {
		User currentUser = this.getCurrentUser(session, model);
		
		return "Farmer-detail";
	}*/
}
