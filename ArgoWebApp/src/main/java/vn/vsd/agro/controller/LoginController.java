package vn.vsd.agro.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.converter.DomainConverter;
import vn.vsd.agro.domain.Company;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.ContactDto;
import vn.vsd.agro.dto.Envelope;
import vn.vsd.agro.dto.LoginDto;
import vn.vsd.agro.repository.CompanyRepository;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.PasswordUtils;
import vn.vsd.agro.validator.LoginValidator;

@Controller
public class LoginController extends BaseController {
	
	@Autowired
	private LoginValidator loginValidator;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		// Form mục tiêu
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}

		if (target.getClass() == LoginDto.class) {
			dataBinder.setValidator(loginValidator);
		}
	}
	
	@RequestMapping("/login")
	public String login(Model model, LoginDto loginDto) {
		model.addAttribute("loginDto", loginDto);

		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String submitLogin(HttpSession session, Model model,
			@ModelAttribute("loginDto") @Validated LoginDto loginDto, 
			BindingResult result, final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return this.login(model, loginDto);
		}

		User user = userRepository.findByEmailOrPhone(loginDto.getAccount());
		if (user == null) {
			//this.addModelError(result, "loginDto", "account", loginDto.getAccount(), "register.message.exists.email");
            
			// Không tìm thấy user
			return this.login(model, loginDto);
		}

		if (!user.isActive()) {
			// User chưa được active
			return this.login(model, loginDto);
		}

		boolean validPassword = PasswordUtils.matches(loginDto.getPassword(), user.getPassword());
		if (!validPassword) {
			return this.login(model, loginDto);
		}
		
		// Cập nhật access token
		String accessToken = UUID.randomUUID().toString();
		if (!userRepository.updateAccessToken(user.getId(), accessToken)) {
			return this.login(model, loginDto);
		}
		
		session.setAttribute(SESSION_KEY_ACCESS_TOKEN, accessToken);
		return this.gotoHome();
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser != null) {
			userRepository.removeAccessToken(currentUser.getId());
		}
		
		session.removeAttribute(SESSION_KEY_ACCESS_TOKEN);
		
		return this.gotoHome();
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getContactInfo(HttpSession session, Model model) {
		User currentUser = this.getCurrentUser(session, model);
		
		if (currentUser == null || !currentUser.isActive()) {
			return this.responseErrorEntity(new Exception("Anonymous can not access this API."));
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		
		String alias = "";
		if (currentUser.isCompany()) {
			Company company = companyRepository.getById(dbContext, currentUser.getId());
			
			if (company != null) {
				alias = company.getBrandName();
			}
		}
		
		ContactDto contactDto = DomainConverter.createContactDto(currentUser, alias);
		
		if (contactDto == null) {
			return this.responseErrorEntity(new Exception("Can not access contact information."));
		}
		
		return new Envelope(contactDto).toResponseEntity(HttpStatus.OK);
	}
}
