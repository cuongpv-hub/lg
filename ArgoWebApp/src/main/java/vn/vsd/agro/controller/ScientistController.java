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
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;

import vn.vsd.agro.dto.ArticleSimpleDto;
import vn.vsd.agro.dto.ScientistSearchDto;
import vn.vsd.agro.dto.ProfileDto;

import vn.vsd.agro.repository.ArticleRepository;
import vn.vsd.agro.repository.ScientistRepository;

import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.ConstantUtils;
import vn.vsd.agro.util.RoleUtils;

@Controller
@RequestMapping("/scientist")
public class ScientistController extends BaseController {
	
	private static final String SEARCH_SESSION_KEY = "ScinetistSearch";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScientistRepository scientistRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@RequestMapping()
	public String scientist(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> scientistDtos = new ArrayList<>();
		
		List<ArticleSimpleDto> articleDtos = new ArrayList<>();
		
		// scientists
		PageRequest scientistPageable = new PageRequest(0, ConstantUtils.SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		List<Scientist> scientists = scientistRepository.getList(dbContext, null, 
				true, scientistPageable, scientistRepository.getSort(dbContext));
		
		if (scientists != null && !scientists.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Scientist scientist : scientists) {
				userIds.add(scientist.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Scientist scientist : scientists) {
				User user = userMaps.get(scientist.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						null, null, scientist, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					scientistDtos.add(dto);
				}
			}
		}
		
		
		PageRequest articlePageable = new PageRequest(0, ConstantUtils.SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);		
		Collection<Integer> statuses = Arrays.asList(Article.APPROVE_STATUS_APPROVED);
		
		
		
		List<Article> articles = articleRepository.getList(dbContext, null, null, null, 
				null, null, null, null, statuses, 
				null, false, true, articlePageable, articleRepository.getSort(dbContext));
		
		if (articles != null && !articles.isEmpty()) {
			for (Article article : articles) {
				ArticleSimpleDto dto = DomainConverter.createArticleSimpleDto(article, 
						currentUser, false, ConstantUtils.DEFAULT_DATE_FORMAT);
				if (dto != null) {
					articleDtos.add(dto);
				}
			}
		}
		
		
		model.addAttribute("scientistDtos", scientistDtos);
		model.addAttribute("articleDtos", articleDtos);
		
		return "scientist";
	}
	
	@RequestMapping({ "/list", "/search" })
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") ScientistSearchDto searchDto) {
		User currentUser = this.getCurrentUser(session, model);
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<ProfileDto> dtos = new ArrayList<>();
		
		Integer searchPage = searchDto.getPage();
		int page = searchPage == null || searchPage < 1 ? 0 : searchPage.intValue() - 1;
		PageRequest scientistPageable = new PageRequest(0, ConstantUtils.SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE);
		
		List<Scientist> scientists = scientistRepository.getList(dbContext, null, 
				true, scientistPageable, scientistRepository.getSort(dbContext));
		
		// TODO: 
		/*long itemCount = scientistRepository.count(dbContext, 
				projectName, scientistName, ownerIds, categoryIds, scientistIds, 
				approveDeptIds, districtIds, statuses, 
				periodValue, withoutClosed, active);*/
		long itemCount = 100;
		long pageCount = itemCount / ConstantUtils.SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE;
		if (itemCount % ConstantUtils.SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE != 0) {
			pageCount++;
		}
		
		if (scientists != null && !scientists.isEmpty()) {
			Set<ObjectId> userIds = new HashSet<>();
			
			for (Scientist scientist : scientists) {
				userIds.add(scientist.getUserId());
			}
			
			List<User> users = userRepository.getListByIds(dbContext, userIds);
			Map<ObjectId, User> userMaps = new HashMap<>();
			for (User user : users) {
				userMaps.put(user.getId(), user);
			}
			
			for (Scientist scientist : scientists) {
				User user = userMaps.get(scientist.getUserId());
				ProfileDto dto = DomainConverter.createProfileDto(user, 
						null, null, scientist, ConstantUtils.DEFAULT_DATE_FORMAT);
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
		
		return "scientist-list";
	}
	
	@RequestMapping("/page")
	public String list(HttpSession session, Model model, 
			@RequestParam(name = "page", required = true) Integer page) {
		ScientistSearchDto searchDto = null;
		
		Object lastSearch = session.getAttribute(SEARCH_SESSION_KEY);
		if (lastSearch != null && lastSearch instanceof ScientistSearchDto) {
			searchDto = (ScientistSearchDto) lastSearch;
		} else {
			searchDto = new ScientistSearchDto();
		}
		
		searchDto.setPage(page);
		return list(session, model, searchDto);
	}
	
	/*@RequestMapping("/{id}/view")
	public String scientistDetail(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String scientistId) {
		User currentUser = this.getCurrentUser(session, model);
		
		return "scientist-detail";
	}*/
}
