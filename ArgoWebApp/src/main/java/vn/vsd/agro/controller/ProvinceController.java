package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.Province;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.ProvinceCreateDto;
import vn.vsd.agro.dto.ProvinceDto;
import vn.vsd.agro.dto.SearchDto;
import vn.vsd.agro.repository.ProvinceRepository;
import vn.vsd.agro.util.ConstantUtils;



@Controller
@RequestMapping(value = "/province")
public class ProvinceController extends BaseController {
	private static final String LIST_PAGE = "provinceList";
	private static final String FORM_PAGE = "provinceForm";
	
	@Autowired
	private ProvinceRepository mainRepository;
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (target.getClass() == ProvinceDto.class 
				|| target.getClass() == ProvinceCreateDto.class) {
			//dataBinder.setValidator(areaValidator);
		}
	}
	
	
	@RequestMapping("/")
	public String list(HttpSession session, Model model, 
			@ModelAttribute("searchDto") SearchDto searchDto) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}

		int page = searchDto.getPage();
		page = page < 1 ? 0 : page - 1;
		Pageable pageable = new PageRequest(page, ConstantUtils.ADMIN_PAGE_SIZE);
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		List<Province> domains = mainRepository.getList(dbContext, 
				searchDto.getText(), null, pageable, 
				mainRepository.getSort(dbContext));
		
		List<ProvinceDto> dtos = new ArrayList<ProvinceDto>(domains.size());
		for (Province domain : domains)
		{
			ProvinceDto dto = DomainConverter.createProvinceDto(domain);
			if (dto != null) {
				dtos.add(dto);
			}
		}
		
		model.addAttribute("dtos", dtos);
		model.addAttribute("searchDto", searchDto);
		
		return LIST_PAGE;
	}
	
	private String editForm(Model model, User currentUser, ProvinceCreateDto dto) {
		model.addAttribute("dto", dto);
		
		/*if (dto.getId() == null) {
			model.addAttribute("formTitle", "Create Area");
		} else {
			model.addAttribute("formTitle", "Edit Area");
		}*/

		return FORM_PAGE;
	}
	
	
	@RequestMapping("/create")
	public String create(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}

		ProvinceCreateDto createDto = new ProvinceCreateDto();

		return this.editForm(model, currentUser, createDto);
	}
	
	
	@RequestMapping("/edit")
	public String edit(HttpSession session, Model model, @RequestParam("id") String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}

		ProvinceCreateDto dto = null;
		
		if (id != null) {
			IContext<ObjectId> dbContext = this.getDbContext(currentUser);
			ObjectId domainId = dbContext.parse(id, null);
			
			if (domainId != null) {
				Province domain = this.mainRepository.getById(dbContext, domainId);
				
				if (domain != null) {
					dto = new ProvinceCreateDto();
					dto.setId(domain.idAsString());
					dto.setCode(domain.getCode());
					dto.setName(domain.getName());
					dto.setCountryId(domain.getCountry().getId().toString());
				}
			}
		}

		if (dto == null) {
			return this.gotoList();
		}

		return this.editForm(model, currentUser, dto);
	}
	
	@RequestMapping("/delete")
	public String delete(HttpSession session, Model model, @RequestParam("id") String id) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}

		if (id != null) {
			IContext<ObjectId> dbContext = this.getDbContext(currentUser);
			ObjectId domainId = dbContext.parse(id, null);
			
			if (domainId != null) {
				this.mainRepository.delete(dbContext, domainId);
			}
		}

		return this.gotoList();
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(HttpSession session, Model model, 
			@ModelAttribute("dto") @Validated ProvinceCreateDto dto, 
			BindingResult result, final RedirectAttributes redirectAttributes) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		if (!currentUser.isAdministrator()) {
			return this.gotoNoPermission();
		}

		if (result.hasErrors()) {
			return this.editForm(model, currentUser, dto);
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(dto.getId(), null);
		
		String code = dto.getCode().trim();
		
		if (this.mainRepository.codeExists(dbContext, code, domainId)) {
			ObjectError error = new ObjectError("code", "register.exists.phone");
            result.addError(error);
            
            return this.editForm(model, currentUser, dto);
		}
		
		Province domain = null;
		if (domainId != null) {
			domain = this.mainRepository.getById(dbContext, domainId);
			
			if (domain == null) {
				ObjectError error = new ObjectError("code", "register.exists.phone");
	            result.addError(error);
	            
	            return this.editForm(model, currentUser, dto);
			}
		} else {
			domain = new Province();
		}
		
		domain.setCode(code);
		domain.setName(dto.getName().trim());
		
		this.mainRepository.save(dbContext, domain);

		redirectAttributes.addFlashAttribute("message", "Save area ssuccessful");
		return this.gotoList();
	}
	
	
	
	private String gotoList() {
		return "redirect:/province";
	}
}
