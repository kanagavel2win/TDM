package com.tdm.tdm.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
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

		for (ProfileLayout profileLayoutObj : listObj) {
			if (profileLayoutObj.getLineType().equals("Detail")) {
				GenerateDataDetailSection(profileLayoutObj.getId(), profileLayoutObj.getProfileID());
			}
		}

		themodel = getData(themodel, id);
		return "profileData";
	}

	private List<String> GenerateDataDetailSection(int Lineid, int ProfileID) {
		List<ProfileLayoutFields> layoutFieldsList = profileLayoutFieldsService.findByProfileIDAndLineID(ProfileID,
				Lineid);
		Collections.sort(layoutFieldsList);
		List<String> dataList = new ArrayList();

		// Handle If data list comes as Empty
		if (dataList.size() == 0) {
			dataList.add("");
		}

		for (ProfileLayoutFields layoutFields : layoutFieldsList) {
			String fielddataType = layoutFields.getF_DATATYPE();
			String DefaultValue = layoutFields.getDEFAULT_VALUE();
			String GenerateType = layoutFields.getGENERATE_TYPE();

			if (fielddataType.equals("DefaultValue")) {
				if (!DefaultValue.isEmpty()) {
					dataList = splitDefaultValue(dataList, layoutFields);
				}

			} else if (fielddataType.equals("Text")) {

				if (GenerateType.contains("Random")) {
					dataList = generateString(dataList, layoutFields);
				}

			} else if (fielddataType.equals("Number")) {

				if (GenerateType.contains("Random")) {
					dataList = generateNumber(dataList, layoutFields);
				}
			}else if (fielddataType.equals("AlphaNumeric")) {

				if (GenerateType.contains("Random")) {
					dataList = generateAlphaNumeric(dataList, layoutFields);
				}
			}else if(fielddataType.equals("Date")) {
				dataList = generateDate(dataList, layoutFields);
				
			}

		}

		dataList.forEach(System.out::println);

		return dataList;

	}
	private List<String> generateDate(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int strLength = Integer.parseInt(layoutFields.getEND_POS());
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();
	
		for (String str : dataList) {
			TempdataList.add(str.concat(equalizationFieldLength(
					String.valueOf(randomValueInDate(Integer.parseInt(MinValue), Integer.parseInt(MaxValue))), strLength)));
		}
		
		return TempdataList;
	
	}

	private List<String> generateNumber(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int strLength = Integer.parseInt(layoutFields.getEND_POS());
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();

		for (String str : dataList) {
			TempdataList.add(str.concat(equalizationFieldLength(
					String.valueOf(randomValueInNumeric(Integer.parseInt(MinValue), Integer.parseInt(MaxValue))), strLength)));
		}

		return TempdataList;
	}

	private List<String> generateAlphaNumeric(List<String> dataList, ProfileLayoutFields layoutFields){
		List<String> TempDataList= new ArrayList();
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();
		int strLength = Integer.parseInt(layoutFields.getEND_POS());
		
		if (!(MinValue.isEmpty() || MinValue.isEmpty())) {
			for (String str : dataList) {
				TempDataList.add(str.concat(equalizationFieldLength(
						randomValueInAlphanumeric(Integer.parseInt(MinValue), Integer.parseInt(MaxValue)), strLength)));
			}
		} else {
			for (String str : dataList) {
				TempDataList.add(str.concat(equalizationFieldLength(randomValueInAlphanumeric(strLength), strLength)));
			}
		}
		
		return TempDataList;
	}
	private List<String> generateString(List<String> dataList, ProfileLayoutFields layoutFields) {
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		List<String> TempdataList = new ArrayList();

		if (!(MinValue.isEmpty() || MinValue.isEmpty())) {
			for (String str : dataList) {
				TempdataList.add(str.concat(equalizationFieldLength(
						randomValueInString(Integer.parseInt(MinValue), Integer.parseInt(MaxValue)), strLength)));
			}
		} else {
			for (String str : dataList) {
				TempdataList.add(str.concat(equalizationFieldLength(randomValueInString(strLength), strLength)));
			}
		}

		return TempdataList;
	}

	private List<String> splitDefaultValue(List<String> dataList, ProfileLayoutFields layoutFields) {
		String defaultValue = layoutFields.getDEFAULT_VALUE().toString().trim();
		int strLength = Integer.parseInt(layoutFields.getEND_POS());
		List<String> splitdata = new ArrayList();
		if (!defaultValue.contains("|")) {
			splitdata.add(defaultValue);
		} else {
			String[] items = defaultValue.split("\\|");
			// splitdata = Arrays.asList(items);
			Collections.addAll(splitdata, items);
		}
		List<String> TempdataList = new ArrayList();

		// Handle If data list comes as Empty
		if (dataList.size() == 0) {
			dataList.add("");
		}

		for (Object Str : splitdata) {
			for (String ListStr : dataList) {
				TempdataList.add(ListStr.concat(equalizationFieldLength(Str.toString(), strLength)));
			}
		}

		return TempdataList;
	}

	private String equalizationFieldLength(String str, int Length) {

		if (Length > str.length()) {

			return String.format("%" + Length + "s", str);
		} else {
			return str;
		}
	}

	private int randomValueInNumeric(int minVal, int maxVal) {

		int generatedString = ((int) (Math.random() * (maxVal - minVal))) + minVal;
		return generatedString;
	}

	private String randomValueInString(int strlength) {

		String generatedString = RandomStringUtils.randomAlphabetic(strlength);
		return generatedString;
	}

	private String randomValueInString(int minVal, int maxVal) {

		String generatedString = RandomStringUtils.randomAlphabetic(minVal, maxVal);
		return generatedString;
	}

	private String randomValueInAlphanumeric(int strlength) {
		String generatedString = RandomStringUtils.randomAlphanumeric(strlength);
		return generatedString;
	}

	private String randomValueInAlphanumeric(int minVal, int maxVal) {

		String generatedString = RandomStringUtils.randomAlphanumeric(minVal, maxVal);
		return generatedString;
	}

	private String randomValueInDate(int startYear, int endYear)
	{
		int year = (int)(Math.random()*(endYear-startYear+1))+startYear;	//Random year
		int month= (int)(Math.random()*12)+1;								//Random Month
		Calendar c = Calendar.getInstance();				//Create Calendar objects
		c.set(year, month, 0);								//Setting Date
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);		//How many days to get the corresponding year and month
		int day=(int)(Math.random()*dayOfMonth+1)	;		//Generating random days
		return year+""+month+""+day;
		
		
	}
}
