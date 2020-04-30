package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.CategorySearchDto;
import vn.vsd.agro.dto.CommonCategoryDto;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.StringUtils;

@Controller
@RequestMapping(value = "/manager")
public class AdminController extends BaseController {
	
	public static final String SEARCH_SESSION_KEY = "AdminSearch";
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		/*if (target.getClass() == GatewayDto.class) {
			dataBinder.setValidator(validator);
		}*/
	}
	
	@RequestMapping()
	public String adminPage(HttpSession session, Model model, 
			@ModelAttribute(name = "searchDto") CategorySearchDto searchDto) {
		return commonCategory(session, model, searchDto);
	}
	
	@RequestMapping(value = { "/common-category", "/common-category/list", "/common-category/search" }, 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String commonCategory(HttpSession session, Model model,
			@ModelAttribute(name = "searchDto") CategorySearchDto searchDto) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return "redirect:/login";
		}

		// Neu ko phai la admin thi thong bao ko co quyen
		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}
		
		if (searchDto == null) {
			searchDto = new CategorySearchDto();
		}
		
		if (searchDto.getType() == null) {
			searchDto.setType(CommonCategory.TYPE_COMPANY_TYPE);
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);
		
		int page = searchDto.getPage();
		if (page < 1) {
			page = 1;
		}
		
		Map<Integer, String> typeMaps = this.getAllCategoryTypes();
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		int categoryType = searchDto.getType().intValue();
		String searchText = searchDto.getText();
		
		int pageSize = ConstantUtils.ADMIN_PAGE_SIZE;
		Pageable pageable = new PageRequest(page - 1, pageSize);
		
		List<CommonCategory> domains = categoryRepository.getList(dbContext, 
				categoryType, searchText, null, pageable, null);
		
		long itemCount = categoryRepository.count(dbContext, 
				categoryType, searchText, null);
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<CommonCategoryDto> dtos = new ArrayList<>();
		if (domains != null && !domains.isEmpty()) {
			for (CommonCategory domain : domains) {
				String typeName = typeMaps.get(domain.getType());
				if (typeName == null) {
					typeName = "";
				}
				
				CommonCategoryDto dto = DomainConverter.createCommonCategoryDto(domain, typeName);
				if (dto != null) {
					dtos.add(dto);
				}
			}
		}
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("itemStartIndex", (page - 1) * pageSize);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageCount", pageCount);
		
		model.addAttribute("typeDtos", typeMaps);
		model.addAttribute("searchDto", searchDto);
		
		return "common-category-list";
	}
	
	@RequestMapping("/common-category-page")
	public String pageCategory(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		CategorySearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof CategorySearchDto) {
			searchDto = (CategorySearchDto) lastSearch;
		} else {
			searchDto = new CategorySearchDto();
		}
		
		searchDto.setPage(page);
		return commonCategory(session, model, searchDto);
	}
	
	@RequestMapping(value = "/common-category/create", 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String createCategory(HttpSession session, Model model) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return "redirect:/login";
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch == null || !(lastSearch instanceof CategorySearchDto)) {
			return "redirect:/manager/common-category/list";
		}
		
		CategorySearchDto searchDto = (CategorySearchDto) lastSearch;
		if (searchDto.getType() == null) {
			return "redirect:/manager/common-category/list";
		}
		
		Map<Integer, String> typeMaps = this.getAllCategoryTypes();
		
		int categoryType = searchDto.getType().intValue();
		String typeName = typeMaps.get(categoryType);
		
		CommonCategoryDto dto = new CommonCategoryDto();
		dto.setType(categoryType);
		dto.setTypeName(typeName);
		
		return editCategoryForm(session, model, currentUser, dto);
	}
	
	@RequestMapping(value = "/common-category/{id}/edit", 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String editCategory(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return "redirect:/login";
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoCategoryList(session);
		}
		
		CommonCategory domain = categoryRepository.getById(dbContext, domainId);
		if (domain == null) {
			return this.gotoCategoryList(session);
		}
		
		Map<Integer, String> typeMaps = this.getAllCategoryTypes();
		String typeName = typeMaps.get(domain.getType());
		
		CommonCategoryDto dto = DomainConverter.createCommonCategoryDto(domain, typeName);
		return editCategoryForm(session, model, currentUser, dto);
	}
	
	@RequestMapping(value = "/common-category/{id}/delete", 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteCategory(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return "redirect:/login";
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoCategoryList(session);
		}
		
		categoryRepository.setActive(dbContext, domainId, false);
		return this.gotoCategoryList(session);
	}
	
	@RequestMapping(value = "/common-category/{id}/active", 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String activeCategory(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		
		User currentUser = this.getCurrentUser(session, model);
		if (currentUser == null || !currentUser.isActive()) {
			return "redirect:/login";
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoCategoryList(session);
		}
		
		categoryRepository.setActive(dbContext, domainId, true);
		return this.gotoCategoryList(session);
	}
	
	private String editCategoryForm(HttpSession session, Model model, User currentUser, CommonCategoryDto dto) {
		int currentPage = 1;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof CategorySearchDto) {
			currentPage = ((CategorySearchDto) lastSearch).getPage().intValue();
		}
		
		model.addAttribute("dto", dto);
		model.addAttribute("currentPage", currentPage);
		
		return "common-category-form";
	}
	
	@RequestMapping(value = "/common-category/save", method = RequestMethod.POST)
	public String saveCategory(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated CommonCategoryDto dto, 
			BindingResult result, final RedirectAttributes redirectAttributes)
	{
		return this.saveEditCategory(session, model, dto, dto.getId(), result, redirectAttributes);
	}
	
	@RequestMapping(value = "common-category/{id}/save", method = RequestMethod.POST)
	public String saveEditCategory(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated CommonCategoryDto dto, 
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

		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editCategoryForm(session, model, currentUser, dto);
		}

		String code = dto.getCode();
		if (StringUtils.isNullOrEmpty(code)) {
			ObjectError error = new ObjectError("code", "category.message.not.empty.code");
            result.addError(error);
		}
		
		String name = dto.getName();
		if (StringUtils.isNullOrEmpty(name)) {
			ObjectError error = new ObjectError("name", "category.message.not.empty.name");
            result.addError(error);
		}
		
		if (result.hasErrors()) {
			dto.setId(id);
			
			return this.editCategoryForm(session, model, currentUser, dto);
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		int type = dto.getType();
		
		code = code.trim();
		name = name.trim();
		
		if (categoryRepository.codeExists(dbContext, type, code, domainId)) {
			ObjectError error = new ObjectError("code", "category.message.exists.code");
            result.addError(error);
            
            return this.editCategoryForm(session, model, currentUser, dto);
		}
		
		if (categoryRepository.nameExists(dbContext, type, name, domainId)) {
			ObjectError error = new ObjectError("code", "category.message.exists.name");
            result.addError(error);
            
            return this.editCategoryForm(session, model, currentUser, dto);
		}
		
		CommonCategory category = null;
		if (domainId == null) {
			category = new CommonCategory();
			category.setType(type);
		} else {
			category = categoryRepository.getById(dbContext, domainId);
			
			if (category == null) {
				ObjectError error = new ObjectError("error", "category.message.not.found");
	            result.addError(error);
	            
	            return this.editCategoryForm(session, model, currentUser, dto);
			}
		}
		
		category.setCode(code);
		category.setName(name);
		category.setIndex(dto.getIndex());
		category.setMain(dto.isMain());
		
		CommonCategory savedCategory = categoryRepository.save(dbContext, category);
		if (savedCategory == null) {
			ObjectError error = new ObjectError("error", "error.unknown");
            result.addError(error);
            
            return this.editCategoryForm(session, model, currentUser, dto);
		}
		
		if (savedCategory.isMain()) {
			categoryRepository.resetMainProperty(dbContext, type, savedCategory.getId());
		}
		
		return this.gotoCategoryList(session);
	}
	
	private String gotoCategoryList(HttpSession session) {
		int currentPage = 1;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof CategorySearchDto) {
			currentPage = ((CategorySearchDto) lastSearch).getPage().intValue();
		}
		
		return "redirect:/manager/common-category-page?page=" + currentPage;
	}
	
	private Map<Integer, String> getAllCategoryTypes() {
		Map<Integer, String> typeMaps = new LinkedHashMap<>();
		
		typeMaps.put(CommonCategory.TYPE_COMPANY_TYPE, "Loại hình công ty");
		typeMaps.put(CommonCategory.TYPE_COMPANY_FIELD, "Lĩnh vực hoạt động của công ty");
		typeMaps.put(CommonCategory.TYPE_COMPANY_COOPERATE, "Hình thức hợp tác của công ty");
		typeMaps.put(CommonCategory.TYPE_PROJECT_TYPE, "Loại dự án");
		typeMaps.put(CommonCategory.TYPE_PRODUCT_TYPE, "Loại nông sản");
		typeMaps.put(CommonCategory.TYPE_PRODUCT_COOPERATE, "Hình thức hợp tác nông sản");
		typeMaps.put(CommonCategory.TYPE_FARMER_COOPERATE, "Hình thức hợp tác của nhà nông");
		typeMaps.put(CommonCategory.TYPE_LAND_TYPE, "Loại thửa đất");
		typeMaps.put(CommonCategory.TYPE_LAND_PURPOSE, "Mục đích sử dụng đất");
		typeMaps.put(CommonCategory.TYPE_LAND_NEAR, "Vị trí thửa đất");
		typeMaps.put(CommonCategory.TYPE_LAND_COOPERATE, "Hình thức hợp tác sử dụng đất");
		typeMaps.put(CommonCategory.TYPE_SCIENTIST_MAJOR, "Lĩnh vực tư vấn");
		typeMaps.put(CommonCategory.TYPE_SCIENTIST_COOPERATE, "Hình thức hợp tác của nhà khoa học");
		typeMaps.put(CommonCategory.TYPE_LITERACY, "Trình độ văn hóa");
		typeMaps.put(CommonCategory.TYPE_ARTICLE_TYPE, "Lĩnh vực bài viết");
		typeMaps.put(CommonCategory.TYPE_NEWS_TYPE, "Loại tin tức");
		typeMaps.put(CommonCategory.TYPE_SQUARE_UOM, "Đơn vị tính diện tích");
		typeMaps.put(CommonCategory.TYPE_VOLUME_UOM, "Đơn vị tính sản lượng");
		typeMaps.put(CommonCategory.TYPE_MONEY_UOM, "Đơn vị tiền tệ");
		
		return typeMaps;
	}
}
