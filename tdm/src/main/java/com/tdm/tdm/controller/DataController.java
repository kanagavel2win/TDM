package com.tdm.tdm.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	int orderInc = 1;
	int detailsectionlineID=0;

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
		List<DataMaster> dataObj = dataMasterService.findByProfileIDOrderbyorderID(ObjProfileID);
		themodel.addAttribute("profileMasterObj", profileMasterObj);
		themodel.addAttribute("profileLayoutObj", listObj);
		themodel.addAttribute("dataMasteObj", dataObj);

		/*
		 * for(DataMaster obj:dataObj) { System.out.println(obj.getData_Details()); }
		 */
		return themodel;
	}

	@GetMapping("GenerateData")
	public String GenerateData(Model themodel, @RequestParam("id") int id) {

		ProfileMaster profileMasterObj = profileMasterService.findByProfileID(id);
		int ObjProfileID = profileMasterObj.getProfileid();
		List<ProfileLayout> listObj = profileLayoutService.findByProfileID(ObjProfileID);
		Collections.sort(listObj);

		List<String> ls_FileHeader = new ArrayList();
		List<String> ls_PharmacyHeader = new ArrayList();
		List<String> ls_Detail = new ArrayList();
		List<String> ls_PharmacyTrailer = new ArrayList();
		List<String> ls_FileTrailer = new ArrayList();

		for (ProfileLayout profileLayoutObj : listObj) {
			if (profileLayoutObj.getLineType().equals("FileHeader")) {
				ls_FileHeader = GenerateDataDetailSection(profileLayoutObj.getId(), profileLayoutObj.getProfileID());
				saveGenerateData(ls_FileHeader, profileLayoutObj.getId(), profileLayoutObj.getProfileID());
			} else if (profileLayoutObj.getLineType().equals("PharmacyHeader")) {
				ls_PharmacyHeader = GenerateDataDetailSection(profileLayoutObj.getId(),
						profileLayoutObj.getProfileID());
				saveGenerateData(ls_PharmacyHeader, profileLayoutObj.getId(), profileLayoutObj.getProfileID());
			} else if (profileLayoutObj.getLineType().equals("Detail")) {
				detailsectionlineID=profileLayoutObj.getId();
				ls_Detail = GenerateDataDetailSection(profileLayoutObj.getId(), profileLayoutObj.getProfileID());
				saveGenerateData(ls_Detail, profileLayoutObj.getId(), profileLayoutObj.getProfileID());
			} else if (profileLayoutObj.getLineType().equals("PharmacyTrailer")) {
				ls_PharmacyTrailer = GenerateDataDetailSection(profileLayoutObj.getId(),
						profileLayoutObj.getProfileID());
				saveTailerGenerateData(ls_Detail, ls_PharmacyTrailer, profileLayoutObj.getId(),
						profileLayoutObj.getProfileID());
			} else if (profileLayoutObj.getLineType().equals("FileTrailer")) {
				ls_FileTrailer = GenerateDataDetailSection(profileLayoutObj.getId(), profileLayoutObj.getProfileID());
				saveTailerGenerateData(ls_PharmacyTrailer,ls_FileTrailer, profileLayoutObj.getId(),
						profileLayoutObj.getProfileID());
			}
		}

		themodel = getData(themodel, id);
		themodel.addAttribute("DataGenerated", true);
		return "profileData";
	}

	private void saveGenerateData(List<String> dataList, int Lineid, int ProfileID) {
		dataMasterService.deleteByProfileIDAndLineID(ProfileID, Lineid);
		for (String str : dataList) {
			DataMaster dataMasterObj = new DataMaster();
			dataMasterObj.setLineID(Lineid);
			dataMasterObj.setProfileID(ProfileID);
			dataMasterObj.setData_Details(str);
			dataMasterObj.setOrderID(orderInc);
			dataMasterService.Save(dataMasterObj);
			orderInc++;
		}
	}

	private void saveTailerGenerateData(List<String> detaildataList, List<String> trailerdataList, int Lineid,
			int ProfileID) {

		List<ProfileLayoutFields> layoutFieldsList = profileLayoutFieldsService.findByProfileIDAndLineID(ProfileID,
				Lineid);
		Collections.sort(layoutFieldsList);

		for (ProfileLayoutFields layoutFields : layoutFieldsList) {
			String fielddataType = layoutFields.getF_DATATYPE();
			int startPos = Integer.parseInt(layoutFields.getSTART_POS());
			int strLength = Integer.parseInt(layoutFields.getEND_POS());

			if (fielddataType.equals("CustomDataType")) {

				String customDataformate = layoutFields.getCUSTOM_DATA_FORMAT();

				if (customDataformate.contains("Count")) {
					String rcdcount = String.valueOf(detaildataList.size());
					trailerdataList = equalizationFieldLengthwithReplace(trailerdataList, rcdcount, startPos,
							strLength);
				}
				if (customDataformate.contains("Sum")) {
					int sumval=0;
					//Get sum Column details-------------------------
					customDataformate = customDataformate.substring(customDataformate.indexOf("(") + 1);
					customDataformate = customDataformate.substring(0, customDataformate.indexOf(")"));
					final String sumcolFieldsName=customDataformate;
					List<ProfileLayoutFields> SumlayoutFieldsList = profileLayoutFieldsService.findByProfileIDAndLineID(ProfileID,
							detailsectionlineID).stream().filter(obj -> obj.getFIELDNAME().equals(sumcolFieldsName)).collect(Collectors.toList());
					
					ProfileLayoutFields   SumObj= SumlayoutFieldsList.get(0);
					int beginIndex=Integer.parseInt(SumObj.getSTART_POS());
					int endIndex=Integer.parseInt(SumObj.getEND_POS());
					//---------------------------------------------
					for(String str:detaildataList)
					{
						sumval=sumval+Integer.parseInt(str.substring(beginIndex,beginIndex+endIndex-1));
					}
					
					trailerdataList = equalizationFieldLengthwithReplace(trailerdataList, String.valueOf(sumval), startPos,
							strLength);
				}
			}
		}

		dataMasterService.deleteByProfileIDAndLineID(ProfileID, Lineid);
		for (String str : trailerdataList) {
			DataMaster dataMasterObj = new DataMaster();
			dataMasterObj.setLineID(Lineid);
			dataMasterObj.setProfileID(ProfileID);
			dataMasterObj.setData_Details(str);
			dataMasterObj.setOrderID(orderInc);
			dataMasterService.Save(dataMasterObj);
			orderInc++;
		}
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
				} else if (GenerateType.contains("Increment")) {
					dataList = generateIncrementNumber(dataList, layoutFields);
				} else if (GenerateType.contains("Decrement")) {
					dataList = generateDecrementNumber(dataList, layoutFields);
				}

			} else if (fielddataType.equals("AlphaNumeric")) {

				if (GenerateType.contains("Random")) {
					dataList = generateAlphaNumeric(dataList, layoutFields);
				}
			} else if (fielddataType.equals("Date")) {
				dataList = generateDate(dataList, layoutFields);

			}else if (fielddataType.equals("CustomDataType")) {

				if (layoutFields.getCUSTOM_DATA_FORMAT().contains("Count")) {
					dataList = generateEmpty(dataList, layoutFields);
				}else if (layoutFields.getCUSTOM_DATA_FORMAT().contains("Sum")) {
					dataList = generateEmpty(dataList, layoutFields);
				}
			}

		}
		// Print in Console All list
		// dataList.forEach(System.out::println);

		return dataList;

	}

	private List<String> generateDate(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();

		for (String str : dataList) {
			TempdataList.add(equalizationFieldLength(str,
					String.valueOf(randomValueInDate(Integer.parseInt(MinValue), Integer.parseInt(MaxValue))), startPos,
					strLength));
		}

		return TempdataList;

	}

	private List<String> generateNumber(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();

		for (String str : dataList) {
			TempdataList.add(equalizationFieldLength(str,
					String.valueOf(randomValueInNumeric(Integer.parseInt(MinValue), Integer.parseInt(MaxValue))),
					startPos, strLength));
		}

		return TempdataList;
	}

	private List<String> generateIncrementNumber(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		String MinValue = layoutFields.getF_MIN();
		int incre_i = 0;
		for (String str : dataList) {
			TempdataList.add(equalizationFieldLength(str, String.valueOf(Integer.parseInt(MinValue) + incre_i),
					startPos, strLength));
			incre_i++;
		}

		return TempdataList;
	}

	private List<String> generateDecrementNumber(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempdataList = new ArrayList();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		String MinValue = layoutFields.getF_MIN();
		int incre_i = Integer.parseInt(MinValue);
		for (String str : dataList) {
			TempdataList.add(equalizationFieldLength(str, String.valueOf(incre_i), startPos, strLength));
			incre_i--;
		}

		return TempdataList;
	}

	private List<String> generateAlphaNumeric(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempDataList = new ArrayList();
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		if (!(MinValue.isEmpty() || MinValue.isEmpty())) {
			for (String str : dataList) {
				TempDataList.add(equalizationFieldLength(str,
						randomValueInAlphanumeric(Integer.parseInt(MinValue), Integer.parseInt(MaxValue)), startPos,
						strLength));
			}
		} else {
			for (String str : dataList) {
				TempDataList
						.add(equalizationFieldLength(str, randomValueInAlphanumeric(strLength), startPos, strLength));
			}
		}

		return TempDataList;
	}

	private List<String> generateEmpty(List<String> dataList, ProfileLayoutFields layoutFields) {
		List<String> TempDataList = new ArrayList();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		for (String str : dataList) {
			TempDataList.add(equalizationFieldLength(str, "", startPos, strLength));
		}
		return TempDataList;
	}

	private List<String> generateString(List<String> dataList, ProfileLayoutFields layoutFields) {
		String MinValue = layoutFields.getF_MIN();
		String MaxValue = layoutFields.getF_MAX();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
		int strLength = Integer.parseInt(layoutFields.getEND_POS());

		List<String> TempdataList = new ArrayList();

		if (!(MinValue.isEmpty() || MinValue.isEmpty())) {
			for (String str : dataList) {
				TempdataList.add(equalizationFieldLength(str,
						randomValueInString(Integer.parseInt(MinValue), Integer.parseInt(MaxValue)), startPos,
						strLength));
			}
		} else {
			for (String str : dataList) {
				TempdataList.add(equalizationFieldLength(str, randomValueInString(strLength), startPos, strLength));
			}
		}

		return TempdataList;
	}

	private List<String> splitDefaultValue(List<String> dataList, ProfileLayoutFields layoutFields) {
		String defaultValue = layoutFields.getDEFAULT_VALUE().toString().trim();
		int startPos = Integer.parseInt(layoutFields.getSTART_POS());
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
				TempdataList.add(equalizationFieldLength(ListStr, Str.toString(), startPos, strLength));
			}
		}

		return TempdataList;
	}

	private String equalizationFieldLength(String srcStr, String str, int startPos, int Length) {

		int strlen = srcStr.length();

		if ((startPos - 1) > strlen) {
			srcStr = String.format("%-" + (startPos - 1) + "s", srcStr);
		}

		return srcStr.concat(String.format("%" + Length + "s", str));

	}

	private List<String> equalizationFieldLengthwithReplace(List<String> srcStrList, String str, int startPos,
			int Length) {

		List<String> templist = new ArrayList();

		str = String.format("%" + Length + "s", str);
  		for (String srcStr : srcStrList) {
  			
  			 StringBuffer buf = new StringBuffer(srcStr);
  			buf.replace(startPos-1,  startPos + Length - 2, str);
			templist.add(buf.toString());
		}

		return templist;
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

	private String randomValueInDate(int startYear, int endYear) {
		int year = (int) (Math.random() * (endYear - startYear + 1)) + startYear; // Random year
		int month = (int) (Math.random() * 12) + 1; // Random Month
		Calendar c = Calendar.getInstance(); // Create Calendar objects
		c.set(year, month, 0); // Setting Date
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH); // How many days to get the corresponding year and month
		int day = (int) (Math.random() * dayOfMonth + 1); // Generating random days
		return year + "" + month + "" + day;

	}

	@GetMapping("uploadprofile")
	public String uploadprofile(Model themodel, @RequestParam("profileid") int profileid,
			@RequestParam("lineid") int lineid) {

		themodel.addAttribute("profileid", profileid);
		themodel.addAttribute("lineid", lineid);
		return "profileLayoutUpload";
	}

	@PostMapping("uploadprofile")
	public String postuploadprofile(@RequestParam("file") MultipartFile file, Model themodel,
			@RequestParam("profileid") int profileid, @RequestParam("lineid") int lineid) throws IOException {

		if (file.isEmpty()) {
			themodel.addAttribute("message", "Please select a xlsx file to upload.");
			themodel.addAttribute("status", false);
		} else {
			DataFormatter formatter = new DataFormatter(Locale.US);
			try {

				Workbook wb = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = wb.getSheetAt(0);
				Header header = sheet.getHeader();

				int rowsCount = sheet.getLastRowNum();
				// System.out.println("Total Number of Rows: " + (rowsCount + 1));
				for (int i = 1; i <= rowsCount; i++) {
					Row row = sheet.getRow(i);
					int colCounts = row.getLastCellNum();
					// System.out.println("Total Number of Cols: " + colCounts);

					ProfileLayoutFields ObjLayoutFields = new ProfileLayoutFields();

					/*
					 * for (int j = 0; j < colCounts; j++) { Cell cell = row.getCell(j);
					 * System.out.println("[" + i + "," + j + "]=" +
					 * (formatter.formatCellValue(cell)));
					 * 
					 */

					ObjLayoutFields.setProfileID(profileid);
					ObjLayoutFields.setLineID(lineid);
					ObjLayoutFields.setCOLUMN_NO(Integer.parseInt(formatter.formatCellValue(row.getCell(0))));
					ObjLayoutFields.setFIELDNAME(formatter.formatCellValue(row.getCell(1)));
					ObjLayoutFields.setSTART_POS(formatter.formatCellValue(row.getCell(2)));
					ObjLayoutFields.setEND_POS(formatter.formatCellValue(row.getCell(3)));
					ObjLayoutFields.setF_DATATYPE(formatter.formatCellValue(row.getCell(4)));
					ObjLayoutFields.setDEFAULT_VALUE(formatter.formatCellValue(row.getCell(5)));
					ObjLayoutFields.setF_MIN(formatter.formatCellValue(row.getCell(6)));
					ObjLayoutFields.setF_MAX(formatter.formatCellValue(row.getCell(7)));
					ObjLayoutFields.setGENERATE_TYPE(formatter.formatCellValue(row.getCell(8)));
					ObjLayoutFields.setCUSTOM_DATA_FORMAT(formatter.formatCellValue(row.getCell(9)));
					profileLayoutFieldsService.save(ObjLayoutFields);
					// System.out.println(ObjLayoutFields);
				}

				themodel.addAttribute("dataSaveStatus", "Saved");

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		themodel.addAttribute("profileid", profileid);
		themodel.addAttribute("lineid", lineid);
		return "profileLayoutUpload";
	}

	@GetMapping("exporttxt")

	public void writeFileContentInResponse(HttpServletResponse response, @RequestParam("id") int profileID)
			throws IOException {
		java.util.Date date = new Date();
		String fileName = profileMasterService.findByProfileID(profileID).getProfileName() + date.getTime();

		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");

		try {
			List<DataMaster> dataObj = dataMasterService.findByProfileIDOrderbyorderID(profileID);

			for (DataMaster obj : dataObj) {
				response.getWriter().println(obj.getData_Details());
			}
		} catch (FileNotFoundException e) {
			// e.printStackTrace();

		} finally {
			response.getWriter().close();
		}

	}
}
