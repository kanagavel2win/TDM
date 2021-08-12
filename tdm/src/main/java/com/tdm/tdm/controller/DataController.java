package com.tdm.tdm.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.tdm.tdm.entity.DataMaster;
import com.tdm.tdm.entity.ProfileLayout;
import com.tdm.tdm.entity.ProfileLayoutFields;
import com.tdm.tdm.entity.ProfileMaster;
import com.tdm.tdm.service.DataMasterService;
import com.tdm.tdm.service.ProfileLayoutFieldsService;
import com.tdm.tdm.service.ProfileLayoutService;
import com.tdm.tdm.service.ProfileMasterService;

@Controller
public class DataController {

	@Autowired
	private ProfileMasterService profileMasterService;

	@Autowired
	private ProfileLayoutService profileLayoutService;

	@Autowired
	private ProfileLayoutFieldsService profileLayoutFieldsService;
	
	@Autowired
	private DataMasterService dataMasterService;

	@Autowired
	private HomeController homeController;

	@ModelAttribute
	public void addAttributes(Model themodel, HttpSession session, HttpServletRequest request) {
		homeController.GetLoginDetail(themodel, session, request);
	}

	@GetMapping("profileDataView")
	public String viewData(Model themodel, @RequestParam("id") int id) {
		themodel = getData(themodel, id);
		return "profileData";
	}

	public Model getData(Model themodel, int id) {
		ProfileMaster profileMasterObj = profileMasterService.findByProfileID(id);
		int ObjProfileID = profileMasterObj.getProfileid();
		List<ProfileLayout> listObj = profileLayoutService.findByProfileID(ObjProfileID);
		List<DataMaster> dataObj = dataMasterService.FindbyProfileID(ObjProfileID);
		themodel.addAttribute("profileMasterObj", profileMasterObj);
		themodel.addAttribute("profileLayoutObj", listObj);
		themodel.addAttribute("dataMasteObj", dataObj);
		return themodel;
	}

	@GetMapping("GenerateData")
	public String GenerateData(Model themodel, @RequestParam("id") int id) {
		
		ProfileMaster profileMasterObj = profileMasterService.findByProfileID(id);
		int ObjProfileID = profileMasterObj.getProfileid();
		List<ProfileLayout> listObj = profileLayoutService.findByProfileID(ObjProfileID);
		
		for(ProfileLayout profileLayoutObj:listObj)
		{
			if(profileLayoutObj.getLineType().equals("Detail"))
			{
				GenerateDataDetailSection(profileLayoutObj.getId(),profileLayoutObj.getProfileID());
			}
		}
		
		
		themodel = getData(themodel, id);
		return "profileData";
	}
	
	private List<String> GenerateDataDetailSection(int Lineid, int ProfileID)
	{
		List<ProfileLayoutFields> layoutFieldsList=profileLayoutFieldsService.findByProfileIDAndLineID(ProfileID,Lineid);
		List<String> dataList = new ArrayList();;
		
		for(ProfileLayoutFields layoutFields: layoutFieldsList)
		{
			String fielddataType=layoutFields.getF_DATATYPE();
			String DefaultValue=layoutFields.getDEFAULT_VALUE();
			
			if(fielddataType.equals("Text"))
			{
				if(!DefaultValue.isEmpty()) {
					dataList=splitDefaultValue(dataList,layoutFields);
				}
				
			}
			
		}
		
		return dataList;
		
	}

	private List<String> splitDefaultValue(List<String> dataList, ProfileLayoutFields layoutFields) {
		String defaultValue=layoutFields.getDEFAULT_VALUE().toString().trim();
		int strLength=Integer.parseInt(layoutFields.getEND_POS());
		List<String> splitdata = new ArrayList();
		if(!defaultValue.contains("|"))
		{
			splitdata.add(defaultValue);
		}else
		{	
			String[] items = defaultValue.split("\\|");
			//splitdata = Arrays.asList(items);
			Collections.addAll(splitdata, items);
		}
		List<String> TempdataList= new ArrayList();
		
		// Handle If data list comes as Empty 
		if(dataList.size()==0)
		{
			dataList.add("");
		}
		
		for (Object Str:splitdata)
		{
			for (String ListStr:dataList)
			{
				TempdataList.add(ListStr.concat(equalizationFieldLength(Str.toString(),strLength)));
			}			
		}
		
		return TempdataList;
	}

	private String equalizationFieldLength(String str, int Length) {
		
		if (Length> str.length()) {
		
			return String.format("%"+  Length +"s", str);
		}else
		{
			return str;
		}	
	}
}
