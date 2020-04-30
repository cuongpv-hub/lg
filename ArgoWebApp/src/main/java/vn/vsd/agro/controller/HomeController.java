package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.District;
import vn.vsd.agro.domain.Land;
import vn.vsd.agro.domain.News;
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;
import vn.vsd.agro.dto.ArticleSearchDto;
import vn.vsd.agro.dto.ArticleSimpleDto;
import vn.vsd.agro.dto.HomeSearchDto;
import vn.vsd.agro.dto.IdNameDto;
import vn.vsd.agro.dto.LandSearchDto;
import vn.vsd.agro.dto.LandSimpleDto;
import vn.vsd.agro.dto.NewsSearchDto;
import vn.vsd.agro.dto.NewsSimpleDto;
import vn.vsd.agro.dto.ProductSearchDto;
import vn.vsd.agro.dto.ProductSimpleDto;
import vn.vsd.agro.dto.ProjectSearchDto;
import vn.vsd.agro.dto.ProjectSimpleDto;
import vn.vsd.agro.dto.SearchDto;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.repository.DistrictRepository;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.util.ConstantUtils;

@Controller
public class HomeController extends BaseController {

	private final static String SEARCH_HOME = "/home";
	private final static String SEARCH_COM = "/company/list";
	private final static String SEARCH_FAR = "/farmer/list";
	private final static String SEARCH_DAD = "/news/list";
	private final static String SEARCH_SCI = "/article/list";
	private final static String SEARCH_PROD = "/product/list";
	private final static String SEARCH_PROJ = "/project/list";
	private final static String SEARCH_LADN = "/land/list";
	private final static String SEARCH_ARTICLE = "/article/list";
	private static final String SEARCH_SESSION_KEY = "homeSearch";

	public enum SEARCH_REDIRECT {
		COM, // cong ty
		FAR, // nong dan
		SCI, // nha khoa hoc
		DAD, // tin nha nuoc
		LAND, // thua dat
		PRODUCT, // nong san
		HOME
	}

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@RequestMapping("/")
	public String homePage(HttpSession session, Model model, @ModelAttribute("searchDto") SearchDto searchDto) {
		return home(session, model, searchDto);
	}

	@RequestMapping("/home")
	public String home(HttpSession session, Model model, @ModelAttribute("searchDto") SearchDto searchDto) {
		User currentUser = this.getCurrentUser(session, model);

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);

		Map<SEARCH_REDIRECT, String> searchTypes = new LinkedHashMap<SEARCH_REDIRECT, String>();
		Map<String, String> countryMap = new LinkedHashMap<String, String>();
		Map<String, IdNameDto> districtMap = new LinkedHashMap<String, IdNameDto>();

		// ProvinceId => { CountryId, Province Name}
		Map<String, IdNameDto> provinceMap = new LinkedHashMap<String, IdNameDto>();
		
		searchDto.setStatus(ConstantUtils.SEARCH_STATUS_ALL);

		searchTypes.put(SEARCH_REDIRECT.COM, "Doanh nghiệp");
		searchTypes.put(SEARCH_REDIRECT.SCI, "Nhà khoa học");
		// searchTypes.put(SEARCH_REDIRECT.FAR, "Nông dân");
		searchTypes.put(SEARCH_REDIRECT.LAND, "Nông dân - Thửa đất");
		searchTypes.put(SEARCH_REDIRECT.PRODUCT, "Nông dân - Nông sản");
		searchTypes.put(SEARCH_REDIRECT.DAD, "Tin chính quyền");

		List<District> districts = districtRepository.getAll(dbContext);
		for (District district : districts) {
			ProvinceEmbed province = district.getProvince();
			String provinceId = province.idAsString();

			districtMap.put(district.idAsString(), new IdNameDto(provinceId, district.getName()));

			if (!provinceMap.containsKey(provinceId)) {
				CountryEmbed country = province.getCountry();
				String countryId = country.idAsString();

				provinceMap.put(provinceId, new IdNameDto(countryId, province.getName()));

				if (!countryMap.containsKey(countryId)) {
					countryMap.put(countryId, country.getName());
				}
			}
		}

		int pageSize = ConstantUtils.HOME_PAGE_PROJECT_LIST_PAGE_SIZE;

		

		
		/*int page = projectResult.getPage();
		long itemCount = projectResult.getCount();

		long pageCount = itemCount / pageSize;
		if (itemCount % pageSize != 0) {
			pageCount++;
		}*/
		ProjectSearchDto projectSearchDto = new ProjectSearchDto(searchDto);
		//projectSearchDto.setStatus(ConstantUtils.SEARCH_STATUS_APPROVED);
		SearchResult<Project> projectResult = this.searchProjects(dbContext, currentUser, projectSearchDto, pageSize);
		List<Project> projectDomains = projectResult.getItems();
		List<ProjectSimpleDto> projectDtos = new ArrayList<ProjectSimpleDto>(projectDomains.size());
		for (Project domain : projectDomains) {
			String authorAvatar = null;
			boolean canApprove = false;
			
			ProjectSimpleDto dto = DomainConverter.createProjectSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				projectDtos.add(dto);
			}
		}
		
		// land 
		LandSearchDto landSearchDto = new LandSearchDto(searchDto);
		//landSearchDto.setStatus(Project.APPROVE_STATUS_APPROVED);
		SearchResult<Land> landtResult = this.searchLands(dbContext, currentUser, landSearchDto, pageSize);
		List<Land> landtDomains = landtResult.getItems();
		List<LandSimpleDto> landtDtos = new ArrayList<LandSimpleDto>(landtDomains.size());
		for (Land domain : landtDomains) {
			String authorAvatar = null;
			boolean canApprove = false;
			
			LandSimpleDto dto = DomainConverter.createLandSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				landtDtos.add(dto);
			}
		}
		
		
		// product 
		ProductSearchDto productSearchDto = new ProductSearchDto(searchDto);
		//productSearchDto.setStatus(Project.APPROVE_STATUS_APPROVED);
		SearchResult<Product> productResult = this.searchProducts(dbContext, currentUser, productSearchDto, pageSize);
		List<Product> productDomains = productResult.getItems();
		List<ProductSimpleDto> productDtos = new ArrayList<ProductSimpleDto>(productDomains.size());
		for (Product domain : productDomains) {
			String authorAvatar = null;
			boolean canApprove = false;
			
			ProductSimpleDto dto = DomainConverter.createProductSimpleDto(domain, currentUser, 
					authorAvatar, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);
			if (dto != null) {
				productDtos.add(dto);
			}
		}			
		
		ArticleSearchDto articleSearchDto = new ArticleSearchDto(searchDto);
		//articleSearchDto.setStatus(Project.APPROVE_STATUS_APPROVED);
		SearchResult<Article> articleResult = this.searchArticles(dbContext, currentUser, articleSearchDto, pageSize);
		List<Article> articleDomains = articleResult.getItems();
		List<ArticleSimpleDto> articleDtos = new ArrayList<ArticleSimpleDto>(articleDomains.size());
		for (Article domain : articleDomains) {
			//String authorAvatar = null;
			boolean canApprove = false;
			
			ArticleSimpleDto dto = DomainConverter.createArticleSimpleDto(domain, currentUser, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);						
			if (dto != null) {
				articleDtos.add(dto);
			}
		}
		
		NewsSearchDto newsSearchDto = new NewsSearchDto(searchDto);
		//newsSearchDto.setStatus(Project.APPROVE_STATUS_APPROVED);
		SearchResult<News> newsResult = this.searchNews(dbContext, currentUser, newsSearchDto, pageSize);
		List<News> newsDomains = newsResult.getItems();
		List<NewsSimpleDto> newsDtos = new ArrayList<NewsSimpleDto>(newsDomains.size());
		for (News domain : newsDomains) {
			//String authorAvatar = null;
			boolean canApprove = false;
			
			NewsSimpleDto dto = DomainConverter.createNewsSimpleDto(domain, currentUser, canApprove, ConstantUtils.DEFAULT_DATE_FORMAT);						
			if (dto != null) {
				newsDtos.add(dto);
			}
		}
		
		
		ArticleSimpleDto articleDto = new ArticleSimpleDto();
		if(articleDtos != null && ! articleDtos.isEmpty()) {
			articleDto = articleDtos.get(0); 
		}
		
		model.addAttribute("article", projectDtos);
		
		

		model.addAttribute("searchDto", searchDto);
		model.addAttribute("categories", searchTypes);
		model.addAttribute("districts", districtMap);
		
		model.addAttribute("projects", projectDtos);
		model.addAttribute("lands", landtDtos);
		model.addAttribute("products", productDtos);
		model.addAttribute("articles", articleDtos);
		model.addAttribute("article", articleDto);
		model.addAttribute("news", newsDtos);

		return "home";
	}
	
	@RequestMapping("/home/search-redirect")
	public String searchDirect(HttpSession session, 
			@ModelAttribute("searchDto") HomeSearchDto searchDto, Model model) {
		String redirect = "redirect:";
		SEARCH_REDIRECT searDirect = SEARCH_REDIRECT.HOME;
		if (searchDto.getCategoryId() != null && !searchDto.getCategoryId().isEmpty()) {
			searDirect = SEARCH_REDIRECT.valueOf(searchDto.getCategoryId());
		}

		switch (searDirect) {
		case COM:
			ProjectSearchDto projectSearchDto = new ProjectSearchDto();
			projectSearchDto.setName(searchDto.getName());
			projectSearchDto.setDistrictIds(searchDto.getDistrictIds());
			projectSearchDto.setStatusSearch(1);
			session.setAttribute(ProjectController.SEARCH_SESSION_KEY, projectSearchDto);
			redirect += SEARCH_PROJ;
			break;
		case FAR:
			LandSearchDto farmerSearchDto = new LandSearchDto();
			farmerSearchDto.setName(searchDto.getName());
			farmerSearchDto.setDistrictIds(searchDto.getDistrictIds());
			farmerSearchDto.setStatusSearch(1);
			session.setAttribute(LandController.SEARCH_SESSION_KEY, farmerSearchDto);
			redirect += SEARCH_LADN;
			break;
		case DAD:
			NewsSearchDto newsSearchDto = new NewsSearchDto();
			newsSearchDto.setName(searchDto.getName());
			newsSearchDto.setDistrictIds(searchDto.getDistrictIds());
			newsSearchDto.setStatusSearch(1);
			session.setAttribute(NewsController.SEARCH_SESSION_KEY, newsSearchDto);
			redirect += SEARCH_DAD;
			break;
		case SCI:
			ArticleSearchDto articleSearchDto = new ArticleSearchDto();
			articleSearchDto.setName(searchDto.getName());
			articleSearchDto.setDistrictIds(searchDto.getDistrictIds());
			articleSearchDto.setStatusSearch(1);
			session.setAttribute(ArticleController.SEARCH_SESSION_KEY, articleSearchDto);
			redirect += SEARCH_SCI;
			break;
		case LAND:
			LandSearchDto landSearchDto = new LandSearchDto();
			landSearchDto.setName(searchDto.getName());
			landSearchDto.setDistrictIds(searchDto.getDistrictIds());
			landSearchDto.setStatusSearch(1);
			session.setAttribute(LandController.SEARCH_SESSION_KEY, landSearchDto);
			redirect += SEARCH_LADN;
			break;
		case PRODUCT:
			ProductSearchDto productSearchDto = new ProductSearchDto();
			productSearchDto.setName(searchDto.getName());
			productSearchDto.setDistrictIds(searchDto.getDistrictIds());
			productSearchDto.setStatusSearch(1);
			session.setAttribute(ProductController.SEARCH_SESSION_KEY, productSearchDto);
			redirect += SEARCH_PROD;
			break;
		default:
			redirect += SEARCH_HOME;
			break;
		}

		model.addAttribute("searchDto", searchDto);

		return redirect;
	}

	@RequestMapping("/profile")
	public String profile(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoHome();
		}

		if (currentUser.isCompany()) {
			// TODO: Load company's projects
		}

		if (currentUser.isFarmer()) {
			// TODO: Load farmer's product & land
		}

		if (currentUser.isScientist()) {
			// TODO: Load scientist's projects, posts, ...
		}

		return "profile";
	}

	/************* farmer *************//*
	@RequestMapping("/farmer")
	public String farmer(HttpSession session, Model model) {
		User user = this.getCurrentUser(session, model);

		return "farmer";
	}

	@RequestMapping("/farmer/{id}")
	public String farmerDetail(HttpSession session, Model model,
			@PathVariable(name = "id", required = true) String farmerId) {
		User user = this.getCurrentUser(session, model);

		return "farmer-detail";
	}
	
	*//************* scientist *************//*
	@RequestMapping("/scientist")
	public String scientist(HttpSession session, Model model) {
		User user = this.getCurrentUser(session, model);

		return "scientist";
	}

	@RequestMapping("/scientist/{id}")
	public String scientistDetail(HttpSession session, Model model,
			@PathVariable(name = "id", required = true) String scientistId) {
		User user = this.getCurrentUser(session, model);

		return "scientist-detail";
	}

	*//************* administrative *************//*
	@RequestMapping("/administrative")
	public String administrative(HttpSession session, Model model) {
		User user = this.getCurrentUser(session, model);

		return "administrative";
	}

	@RequestMapping("/news/{id}")
	public String newsDetail(HttpSession session, Model model,
			@PathVariable(name = "id", required = true) String newsId) {
		User user = this.getCurrentUser(session, model);

		return "news-detail";
	}*/
}
