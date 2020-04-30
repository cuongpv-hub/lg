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
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.Company;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.dto.ArticleSimpleDto;
import vn.vsd.agro.dto.CompanySearchDto;
import vn.vsd.agro.dto.ProfileDto;
import vn.vsd.agro.dto.ProjectSimpleDto;
import vn.vsd.agro.repository.ArticleRepository;
import vn.vsd.agro.repository.CompanyRepository;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.RoleUtils;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {
	
	private static final String SEARCH_SESSION_KEY = "CompanySearch";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@RequestMapping()
	public String company(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> companyDtos = new ArrayList<>();
		List<ProjectSimpleDto> projectDtos = new ArrayList<>();
		List<ArticleSimpleDto> articleDtos = new ArrayList<>();
		
		// Companies
		PageRequest companyPageable = new PageRequest(0, ConstantUtils.COMPANY_PAGE_COMPANY_LIST_PAGE_SIZE);
		List<Company> companies = companyRepository.getList(dbContext, null, 
				true, companyPageable, companyRepository.getSort(dbContext));
		
		if (companies != null && !companies.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Company company : companies) {
				userIds.add(company.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Company company : companies) {
				User user = userMaps.get(company.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						company, null, null, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					companyDtos.add(dto);
				}
			}
		}
		
		// Projects
		PageRequest projectPageable = new PageRequest(0, ConstantUtils.COMPANY_PAGE_PROJECT_LIST_PAGE_SIZE);
		Collection<Integer> statuses = Arrays.asList(Project.APPROVE_STATUS_APPROVED);
		
		List<Project> projects = projectRepository.getList(dbContext, null, 
				null, null, null, null, null, null, null, null, null, 
				statuses, null, null, null, null, false, true, 
				projectPageable, projectRepository.getSort(dbContext));
		
		if (projects != null && !projects.isEmpty()) {
			Set<ObjectId> companyIds = new HashSet<>();
			for (Project project : projects) {
				ContactEmbed company = project.getContact();
				if (company != null) {
					companyIds.add(company.getId());
				}
			}
			
			List<User> projectContacts = userRepository.getListByIds(dbContext, companyIds);
			Map<ObjectId, User> projectContactMaps = new HashMap<>();
			for (User projectContact : projectContacts) {
				projectContactMaps.put(projectContact.getId(), projectContact);
			}
			
			for (Project project : projects) {
				String authorAvatar = null;
				
				ContactEmbed company = project.getContact();
				if (company != null) {
					User projectContact = projectContactMaps.get(company.getId());
					
					if (projectContact != null) {
						authorAvatar = projectContact.getAvatar();
					}
				}
				
				ProjectSimpleDto dto = DomainConverter.createProjectSimpleDto(project, 
						currentUser, authorAvatar, false, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					projectDtos.add(dto);
				}
			}
		}
		
		PageRequest articlePageable = new PageRequest(0, ConstantUtils.COMPANY_PAGE_ARTICLE_LIST_PAGE_SIZE);
		Collection<String> authorRoles = Arrays.asList(RoleUtils.ROLE_COMPANY);
		
		List<Article> articles = articleRepository.getListByAuthorRoles(dbContext, authorRoles, 
				null, articlePageable, articleRepository.getSort(dbContext), 
				articleRepository.getSimpleFields(dbContext));
		
		if (articles != null && !articles.isEmpty()) {
			for (Article article : articles) {
				ArticleSimpleDto dto = DomainConverter.createArticleSimpleDto(article, 
						currentUser, false, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					articleDtos.add(dto);
				}
			}
		}
		
		model.addAttribute("projectDtos", projectDtos);
		model.addAttribute("companyDtos", companyDtos);
		model.addAttribute("articleDtos", articleDtos);
		
		return "company";
	}
	
	@RequestMapping({ "/list", "/search" })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") CompanySearchDto searchDto) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> dtos = new ArrayList<>();
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		PageRequest companyPageable = new PageRequest(0, ConstantUtils.COMPANY_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		
		List<Company> companies = companyRepository.getList(dbContext, null, 
				true, companyPageable, companyRepository.getSort(dbContext));
		
		// TODO: 
		/*long itemCount = companyRepository.count(dbContext, 
				projectName, companyName, ownerIds, categoryIds, scientistIds, 
				approveDeptIds, districtIds, statuses, 
				periodValue, withoutClosed, active);*/
		long itemCount = 100;
		long pageCount = itemCount / ConstantUtils.COMPANY_LIST_PAGE_COMPANY_LIST_PAGE_SIZE;
		if (itemCount % ConstantUtils.COMPANY_LIST_PAGE_COMPANY_LIST_PAGE_SIZE != 0) {
			pageCount++;
		}
		
		if (companies != null && !companies.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Company company : companies) {
				userIds.add(company.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Company company : companies) {
				User user = userMaps.get(company.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						company, null, null, ConstantUtils.DEFAULT_DATE_FORMAT);
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
		
		return "company-list";
	}
	
	@RequestMapping("/page")
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		CompanySearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof CompanySearchDto) {
			searchDto = (CompanySearchDto) lastSearch;
		} else {
			searchDto = new CompanySearchDto();
		}
		
		searchDto.setPage(page);
		return list(session, model, searchDto);
	}
	
	/*@RequestMapping("/{id}/view")
	public String companyDetail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String companyId) {
		User currentUser = this.getCurrentUser(session, model);
		
		return "company-detail";
	}*/
}
