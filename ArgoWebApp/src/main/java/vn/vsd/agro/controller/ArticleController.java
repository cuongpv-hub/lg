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
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.BaseItem;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.IdCodeNameEmbed;
import vn.vsd.agro.domain.IdNameEmbed;
import vn.vsd.agro.domain.Organization;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.ArticleCreateDto;
import vn.vsd.agro.dto.ArticleDto;
import vn.vsd.agro.dto.ArticleSearchDto;
import vn.vsd.agro.dto.ArticleSimpleDto;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.embed.StatusEmbedDto;
import vn.vsd.agro.repository.ArticleRepository;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.OrganizationRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.FileUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;
import vn.vsd.agro.validator.ArticleValidator;

@Controller
@RequestMapping(value = "/article")
public class ArticleController extends BaseItemController {
	
	private static final String PAGE_NAME = "article";
	
	private static final String LIST_PAGE = "article-list";
	private static final String FORM_PAGE = "article-form";
	private static final String VIEW_PAGE = "article-detail";

	public static final String SEARCH_SESSION_KEY = "articleSearch";

	@Autowired
	private ArticleRepository mainRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArticleValidator validator;

	@Override
	protected BasicRepository<? extends BaseItem, ObjectId> getMainRepository() {
		return this.mainRepository;
	}

	@Override
	protected File saveUploadImage(HttpSession session, MultipartFile imageFile) {
		return FileUtils.saveUploadArticleImage(session, imageFile);
	}

	@Override
	protected boolean deleteUploadImage(HttpSession session, String imageName) {
		return FileUtils.deleteUploadArticleFile(session, imageName);
	}

	@Override
	protected String gotoListPage() {
		return "redirect:/article";
	}

	@Override
	protected String gotoViewPage(String id) {
		return "redirect:/article/" + id + "/view";
	}

	@Override
	protected String[] getProcessRoles() {
		//return new String[] { RoleUtils.ROLE_COMPANY, RoleUtils.ROLE_SCIENTIST };
		return new String[] { RoleUtils.ROLE_SCIENTIST };
	}

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (ArticleCreateDto.class.isAssignableFrom(target.getClass())) {
			dataBinder.setValidator(validator);
		}
	}

	@RequestMapping({ "", "/list", "/search" })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") ArticleSearchDto searchDto) {

		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		if (searchDto == null) {
			Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
			if (lastSearch != null && lastSearch instanceof ArticleSearchDto) {
				searchDto = (ArticleSearchDto) lastSearch;
			} else {
				searchDto = new ArticleSearchDto();
			}
		}
		
		Integer searchStatus = searchDto.getStatus();
		if (searchStatus == null) {
			searchStatus = ConstantUtils.SEARCH_STATUS_ALL;
			
			if (currentUser != null) {
				if (currentUser.isAdministrator() || currentUser.isDepartmentAdministrator()) {
					searchStatus = ConstantUtils.SEARCH_STATUS_PROGRESS;
				} else if (currentUser.isScientist()) {
					searchStatus = ConstantUtils.SEARCH_STATUS_OWNER;
				} 
			}
			
			searchDto.setStatus(searchStatus);
		}
		
		session.setAttribute(SEARCH_SESSION_KEY, searchDto);

		int pageSize = ConstantUtils.ARTICLE_PAGE_ARTICLE_LIST_PAGE_SIZE;
		SearchResult<Article> result = this.searchArticles(dbContext, currentUser, searchDto, pageSize);
		
		List<Article> domains = result.getItems();
		int page = result.getPage();
		long itemCount = result.getCount();
		
		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}
		
		List<ArticleSimpleDto> dtos = new ArrayList<ArticleSimpleDto>(domains.size());
		for (Article domain : domains)
		{
			boolean canApprove = this.canApprove(dbContext, domain, currentUser);
			ArticleSimpleDto dto = DomainConverter.createArticleSimpleDto(domain, currentUser, 
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
				CommonCategory.TYPE_ARTICLE_TYPE, null, null);
		for (CommonCategory category : categories) {
			categoryMap.put(category.idAsString(), category.getName());
		}
		
		List<Organization> organizations = organizationRepository.getAll(dbContext);
		for (Organization organization : organizations) {
			organizationMap.put(organization.idAsString(), organization.getName());
		}
		
		statusMap.put(ConstantUtils.SEARCH_STATUS_ALL, "Tất cả trạng thái");
		if (currentUser != null && currentUser.isActive()) {
			if (currentUser.isCompany()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_DRAFT, "Chưa gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đã gửi phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã được phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Bị từ chối phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_OWNER, "Bài viết của cá nhân");
			} else if (currentUser.isAdministrator() 
					|| currentUser.isDepartmentAdministrator()) {
				statusMap.put(ConstantUtils.SEARCH_STATUS_PROGRESS, "Đang chờ phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_APPROVED, "Đã phê duyệt");
				statusMap.put(ConstantUtils.SEARCH_STATUS_REJECTED, "Đã từ chối phê duyệt");
			}
		}
		
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_DESC, "Ngày duyệt giảm dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_APPROVE_DATE_ASC, "Ngày duyệt tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_ASC, "Tên bài viết tăng dần");
		sortMap.put(ConstantUtils.SEARCH_SORT_NAME_DESC, "Tên bài viết giảm dần");
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("currentPage", (page + 1));
		model.addAttribute("pageCount", pageCount);
		
		model.addAttribute("categories", categoryMap);
		model.addAttribute("organizations", organizationMap);
		model.addAttribute("statuses", statusMap);
		model.addAttribute("sorts", sortMap);
		
		model.addAttribute("rndValue", Math.abs((new Random()).nextInt()));
		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_ARTICLE);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);

		return LIST_PAGE;
	}

	@RequestMapping("/page")
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		ArticleSearchDto searchDto = null;

		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof ArticleSearchDto) {
			searchDto = (ArticleSearchDto) lastSearch;
		} else {
			searchDto = new ArticleSearchDto();
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

		Article domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) {
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
		ArticleDto dto = DomainConverter.createArticleDto(domain, currentUser, authorAvatar, canApprove,
				ConstantUtils.DEFAULT_DATE_FORMAT);

		model.addAttribute("dto", dto);

		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_ARTICLE);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());
		model.addAttribute("pageName", PAGE_NAME);
		
		return VIEW_PAGE;
	}

	private String editForm(Model model, User currentUser, ArticleCreateDto dto, 
			List<IdNameDto> imageDtos, StatusEmbedDto statusDto) {
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);

		Map<String, String> categoryMap = new LinkedHashMap<String, String>();
		Map<String, String> organizationMap = new LinkedHashMap<>();

		int[] categoryTypes = new int[] { 
				CommonCategory.TYPE_ARTICLE_TYPE
		};
		
		List<CommonCategory> categories = categoryRepository.getList(dbContext, categoryTypes, null, null);
		for (CommonCategory category : categories) {
			if (CommonCategory.TYPE_ARTICLE_TYPE == category.getType()) {
				categoryMap.put(category.idAsString(), category.getName());
			}
		}

		List<Organization> organizations = organizationRepository.getAll(dbContext);
		for (Organization organization : organizations) {
			organizationMap.put(organization.idAsString(), organization.getName());
		}

		if (imageDtos == null || statusDto == null) {
			imageDtos = new ArrayList<>();

			ObjectId domainId = dbContext.parse(dto.getId(), null);
			if (domainId != null) {
				Article domain = mainRepository.getById(dbContext, domainId, Article.COLUMNNAME_IMAGES,
						Article.COLUMNNAME_STATUS);
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

		model.addAttribute("imagePath", ConstantUtils.UPLOAD_FOLDER_ARTICLE);
		model.addAttribute("dateFormat", ConstantUtils.DEFAULT_DATE_FORMAT);
		model.addAttribute("dateFormatLower", ConstantUtils.DEFAULT_DATE_FORMAT.toLowerCase());

		model.addAttribute("categories", categoryMap);
		model.addAttribute("organizations", organizationMap);

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

		if (!currentUser.isScientist()) {
			return this.gotoNoPermission();
		}

		ArticleCreateDto createDto = new ArticleCreateDto();
		createDto.setClosed(false);

		List<IdNameDto> imageDtos = new ArrayList<>();
		StatusEmbedDto statusDto = new StatusEmbedDto();
		statusDto.setStatus(Article.APPROVE_STATUS_PENDING);

		return this.editForm(model, currentUser, createDto, imageDtos, statusDto);
	}

	@RequestMapping("/{id}/edit")
	public String edit(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isScientist()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		ObjectId domainId = dbContext.parse(id, null);
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		Article domain = this.mainRepository.getById(dbContext, domainId);
		if (domain == null) {
			return this.gotoListPage();
		}
		
		if (domain.isApproved()) {
			return this.gotoViewPage(id);
		}

		if (domain.getAuthor() != null && !dbContext.equals(domain.getAuthor().getId(), currentUser.getId())) {
			return this.gotoViewPage(id);
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);

		ArticleCreateDto dto = DomainConverter.createArticleCreateDto(domain, currentUser, canApprove,
				ConstantUtils.DEFAULT_DATE_FORMAT);
		
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

		if (!currentUser.isScientist()) {
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
			@ModelAttribute("dto") @Validated ArticleCreateDto dto,
			BindingResult result, final RedirectAttributes redirectAttributes) {
		return this.saveEdit(session, model, dto, dto.getId(), result, redirectAttributes);
	}

	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST)
	public String saveEdit(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated ArticleCreateDto dto,
			@PathVariable(name = "id", required = true) String id, 
			BindingResult result, final RedirectAttributes redirectAttributes) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isScientist()) {
			return this.gotoNoPermission();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		List<ObjectId> categoryIds = dbContext.parseMulti(dto.getCategoryIds(), true);
		if (categoryIds == null || categoryIds.isEmpty()) {
			this.addModelError(result, "dto", "categoryIds", dto.getCategoryIds(), "article.message.not.empty.category");
		}
		
		if (result.hasErrors()) {
			dto.setId(id);

			return this.editForm(model, currentUser, dto, null, null);
		}
		
		List<CommonCategory> categories = categoryRepository.getListByIds(dbContext, categoryIds);
		if (categories == null || categories.isEmpty()) {
			this.addModelError(result, "dto", "categoryIds", dto.getCategoryIds(), "article.message.not.empty.category");
            
            return this.editForm(model, currentUser, dto, null, null);
		}
		
		String name = dto.getName().trim();
		if (this.mainRepository.nameExists(dbContext, name, domainId)) {
			this.addModelError(result, "dto", "name", name, "article.message.exists.name");

			return this.editForm(model, currentUser, dto, null, null);
		}
		
		Article domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);

			if (domain == null) {
				ObjectError error = new ObjectError("error", "article.message.not.found");
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
			domain = new Article();
		}

		domain.setName(name);
		domain.setDescription(dto.getDescription());
		domain.setClosed(dto.isClosed());
		domain.setApproveName(dto.getApproveName());
		
		IdNameEmbed author = new IdNameEmbed(currentUser.getId(), currentUser.getName());
		domain.setAuthor(author);
		
		domain.clearCategories();
		for (CommonCategory category : categories) {
			IdCodeNameEmbed categoryEmbed = new IdCodeNameEmbed(category, category.getCode(),
					category.getName());
			domain.addCategory(categoryEmbed);
		}
		
		this.updateBaseItem(session, dbContext, currentUser, domain, dto);

		this.mainRepository.save(dbContext, domain);

		return this.gotoListPage();
	}
}
