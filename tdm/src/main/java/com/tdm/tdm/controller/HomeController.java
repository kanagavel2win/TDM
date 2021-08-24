package com.tdm.tdm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdm.tdm.dao.UserRepository;
import com.tdm.tdm.entity.ProfileLayout;
import com.tdm.tdm.entity.ProfileLayoutFields;
import com.tdm.tdm.entity.ProfileMaster;
import com.tdm.tdm.entity.User;
import com.tdm.tdm.service.ProfileLayoutFieldsService;
import com.tdm.tdm.service.ProfileLayoutService;
import com.tdm.tdm.service.ProfileMasterService;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileMasterService profileMasterService;

	@Autowired
	private ProfileLayoutService profileLayoutService;

	@Autowired
	private ProfileLayoutFieldsService profileLayoutFieldsService;

	@ModelAttribute
	public void addAttributes(Model themodel, HttpSession session, HttpServletRequest request) {
		
		GetLoginDetail(themodel, session, request); 

	}

	public Model GetLoginDetail(Model themodel, HttpSession session, HttpServletRequest request) {
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
		return themodel;
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

		ProfileMaster obj = new ProfileMaster();
		/*
		 * obj.setProfileName("IFSI"); obj.setDescription("Claims");
		 */
		model.addAttribute("ProfileMaster", obj);
		return "profileMasterAdd";
	}

	@PostMapping("saveprofile")
	public String saveprofile(HttpServletRequest request, @ModelAttribute("ProfileMaster") ProfileMaster profile,
			Model model) {
		profileMasterService.save(profile);
		model.addAttribute("ProfileMaster", new ProfileMaster());
		model.addAttribute("savestatus", "Saved");
		return "profileMasterAdd";
	}

	@GetMapping("list")
	public String profileListView(Model model) {
		List<ProfileMaster> list = profileMasterService.findAll();

		model.addAttribute("profileList", list);
		return "profileList";
	}

	@GetMapping("profileLayout")
	public String profileStructureEdit(Model model, @RequestParam("id") int profileID) {
		List<ProfileLayout> Objprl = profileLayoutService.findByProfileID(profileID);
		Collections.sort(Objprl);
		model.addAttribute("profilelayouts", Objprl);
		model.addAttribute("profileID", profileID);
		return "profilestructure";
	}

	@PostMapping("profileLayoutSaveJS")
	@ResponseBody
	public String profileLayoutSaveJS(Model model, @RequestParam Map<String, String> profileLayoutData) {

		try {
			ProfileLayout objPL = new ProfileLayout();
			String lineID = profileLayoutData.get("id");

			if (lineID.contains("new") == false) {
				objPL.setId(Integer.parseInt(lineID));
			}
			objPL.setProfileID(Integer.parseInt(profileLayoutData.get("profileID")));
			objPL.setOrderID(Integer.parseInt(profileLayoutData.get("orderID")));
			objPL.setLineType(profileLayoutData.get("lineType"));
			objPL.setLineTitle(profileLayoutData.get("lineTitle"));
			objPL.setLineLength(profileLayoutData.get("lineLength"));
			profileLayoutService.save(objPL);
			return "Changes are Saved";

		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}

	}

	@PostMapping("profileLayoutDeleteJS")
	@ResponseBody
	public String profileLayoutDeleteJS(Model model, @RequestParam("id") String ID) {
		try {
			profileLayoutService.deleteByID(Integer.parseInt(ID));
			return "Layout has been Deleted";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}

	}

	@PostMapping("getProfileLayoutFieldJS")
	public String getProfileLayoutFieldJS(Model model, @RequestParam("profileid") String profileID,
			@RequestParam("lineID") String lineID) {
		try {
			int profileID_temp = Integer.parseInt(profileID);
			int lineID_temp = Integer.parseInt(lineID);
			List<ProfileLayoutFields> objList = profileLayoutFieldsService.findByProfileIDAndLineID(profileID_temp,
					lineID_temp);
			Collections.sort(objList);
			model.addAttribute("columnList", objList);
			return "profileLayoutFieldsJS";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "profileLayoutFieldsJS";
		}

	}
	
	@PostMapping("profileLayoutFieldsSaveJS")
	@ResponseBody
	public String profileLayoutFieldsSaveJS(Model model, @RequestParam Map<String, String> profileLayoutFieldsData) {

		try {
			ProfileLayoutFields obj =new ProfileLayoutFields();
			
			if (profileLayoutFieldsData.get("selectItemID").contains("new") == false) {
				int selectItemID= Integer.parseInt(profileLayoutFieldsData.get("selectItemID"));
				obj.setID(selectItemID);
			}
			int selectProfileID= Integer.parseInt(profileLayoutFieldsData.get("selectProfileID"));
			int selectLineID= Integer.parseInt(profileLayoutFieldsData.get("selectLineID"));
			int COLUMN_NO= Integer.parseInt(profileLayoutFieldsData.get("COLUMN_NO"));
			String FIELDNAME= profileLayoutFieldsData.get("FIELDNAME");
			String START_POS= profileLayoutFieldsData.get("START_POS");
			String END_POS= profileLayoutFieldsData.get("END_POS");
			String F_DATATYPE= profileLayoutFieldsData.get("F_DATATYPE");
			String DEFAULT_VALUE= profileLayoutFieldsData.get("DEFAULT_VALUE");
			String F_MIN= profileLayoutFieldsData.get("F_MIN");
			String F_MAX= profileLayoutFieldsData.get("F_MAX");
			String GENERATE_TYPE= profileLayoutFieldsData.get("GENERATE_TYPE");
			String CUSTOM_DATA_FORMAT= profileLayoutFieldsData.get("CUSTOM_DATA_FORMAT");
	
			
			obj.setCOLUMN_NO(COLUMN_NO);
			obj.setProfileID(selectProfileID);
			obj.setLineID(selectLineID);
			obj.setFIELDNAME(FIELDNAME);
			obj.setSTART_POS(START_POS);
			obj.setEND_POS(END_POS);
			obj.setF_DATATYPE(F_DATATYPE);
			obj.setDEFAULT_VALUE(DEFAULT_VALUE);
			obj.setF_MAX(F_MAX);
			obj.setF_MIN(F_MIN);
			obj.setGENERATE_TYPE(GENERATE_TYPE);
			obj.setCUSTOM_DATA_FORMAT(CUSTOM_DATA_FORMAT);
			
			profileLayoutFieldsService.save(obj);
			return "Changes are Saved";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
			
	}
	
	@PostMapping("profileLayoutFieldsDeleteJS")
	@ResponseBody
	public String profileLayoutFieldsDeleteJS(Model model, @RequestParam("selectItemID") String selectItemID) {
		try {
			int ID=Integer.parseInt(selectItemID); 
			profileLayoutFieldsService.deleteByID(ID);
			return "Item has been Deleted";
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return "Error";
		}
		
	}
	
	@GetMapping("profileLayoutDownloadTemplete")	
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response) throws IOException {

		final String EXTERNAL_FILE_PATH = request.getContextPath()+ "/Doc/";
		String fileName="TDMTemplate.xlsx";
		
		File file = new File(EXTERNAL_FILE_PATH + fileName);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			 //Here we have mentioned it to show as attachment
			 //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}
	
}
