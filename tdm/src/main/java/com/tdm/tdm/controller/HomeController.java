package com.tdm.tdm.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tdm.tdm.dao.UserRepository;
import com.tdm.tdm.entity.ProfileMaster;
import com.tdm.tdm.entity.User;
import com.tdm.tdm.service.ProfileMasterService;

@Controller
public class HomeController {

	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileMasterService profileMasterService;	
	
	@ModelAttribute
	public void addAttributes(Model themodel, HttpSession session, HttpServletRequest request) {

		String dataLoginEmailID = "";
		String dataLoginUserName = "";
		try {

			try {
				if (request.getSession().getAttribute("dataLoginEmailID").toString().equals(null)) {
					dataLoginUserName = getLoginuserName();
					request.getSession().setAttribute("dataLoginUserName", dataLoginUserName);
					dataLoginEmailID = getLoginemailID();
					request.getSession().setAttribute("dataLoginEmailID", dataLoginEmailID);
				}
			} catch (NullPointerException e) {
				dataLoginUserName = getLoginuserName();
				request.getSession().setAttribute("dataLoginUserName", dataLoginUserName);
				dataLoginEmailID = getLoginemailID();
				request.getSession().setAttribute("dataLoginEmailID", dataLoginEmailID);
			}

			dataLoginEmailID = request.getSession().getAttribute("dataLoginEmailID").toString();
			dataLoginUserName = request.getSession().getAttribute("dataLoginUserName").toString();

		} catch (Exception e) {

		} finally {
			themodel.addAttribute("dataLoginEmailID", dataLoginEmailID);
			themodel.addAttribute("dataLoginUserName", dataLoginUserName);
		}

	}

	@GetMapping("/")
	public String home(Model theModel, HttpSession session, HttpServletRequest request) {

		if (logintype("ROLE_TDMADMIN")) {

			return "index";
		} else {
			return "redirect:logout";
		}
	}

	private boolean logintype(String expectedrole) {

		@SuppressWarnings("unchecked")
		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		boolean RoleStatus = false;

		for (SimpleGrantedAuthority simpleGrantedAuthority : authorities) {

			if (simpleGrantedAuthority.getAuthority().toString().contains(expectedrole)) {
				RoleStatus = true;
			}
		}

		return RoleStatus;

	}

	public String getLoginemailID() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User user2 = userRepository.findByEmail(currentPrincipalName);
		return user2.getEmail();

	}

	public String getLoginuserName() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User user2 = userRepository.findByEmail(currentPrincipalName);
		return user2.getuserName(); 

	}

	public String getLoginaccountName() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		User user2 = userRepository.findByEmail(currentPrincipalName);
		return user2.getaccountName();

	}

	@GetMapping("/index")
	public String index(Model theModel) {

		if (logintype("ROLE_TDMADMIN")) {
			return "index";
		} else {
			return "redirect:logout";
		}

	}

	@GetMapping("login")
	public String login(Model model) {

		return "login";
	}

	@GetMapping("/newprofile")
	public String profileMasterAdd(Model model) {
		
		ProfileMaster obj=new ProfileMaster();
		/*obj.setProfileName("IFSI");
		obj.setDescription("Claims");*/
		model.addAttribute("ProfileMaster", obj);
		return "profileMasterAdd";
	}
	
	@PostMapping("saveprofile")
	public String saveprofile(HttpServletRequest request, @ModelAttribute("ProfileMaster") ProfileMaster profile, Model model)
	{
		profileMasterService.save(profile);
		model.addAttribute("ProfileMaster", new ProfileMaster());
		model.addAttribute("savestatus","Saved");
		return "profileMasterAdd";
	}
	
	@GetMapping("list")
	public String profileListView(Model model) {
		List<ProfileMaster> list= profileMasterService.findAll();
		
		model.addAttribute("profileList",list);		
		return "profileList";
	}
	
	@GetMapping("profileLayout")
	public String profileStructureEdit(Model model)
	{	
		return "profilestructure";
	}
}
