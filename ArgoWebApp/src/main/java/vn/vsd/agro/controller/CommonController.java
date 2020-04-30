package vn.vsd.agro.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
public class CommonController extends BaseController {
	
	@RequestMapping("/notPermission")
	public String notPermission(HttpSession session, Model model) {
		return "notPermission";
	}
}
