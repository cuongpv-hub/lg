package vn.vsd.agro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.vsd.agro.domain.User;
import vn.vsd.agro.dto.Envelope;

@Controller
@RequestMapping(value ="/mobile")
public class MobileController extends BaseController {
	
	@RequestMapping(value ="/test/json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> mobileAreaList(HttpServletRequest request, @RequestHeader HttpHeaders httpHeader) {
		List<Object> dtos = new ArrayList<>();
		dtos.add(new User());
		
		return new Envelope(dtos).toResponseEntity(HttpStatus.OK);
	}
}
