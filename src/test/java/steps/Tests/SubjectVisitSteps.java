package steps.Tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import hu.siteportal.pdfreport.PdfLog;
import hu.siteportal.pdfreport.StepVerify;
import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.objects.Assessment;
import mt.siteportal.objects.Subject;
import mt.siteportal.objects.Visit;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.EsignDialog;
import mt.siteportal.pages.studyDashboard.NewEditSubject;
import mt.siteportal.pages.studyDashboard.Templates;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.Enums.EsignReasonFields;
import mt.siteportal.utils.Enums.SubjectDetailsFields;
import mt.siteportal.utils.helpers.Required;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

/**
 * Created by maksym.tkachov on 6/27/2016.
 */
public class SubjectVisitSteps extends AbstractStep{

	private String errorMessage = "the status of %s is incorrect";
	private final String successStatus = "Passed";
	private final String failStatus = "Failed";
	private EsignSteps esignSteps = new EsignSteps();

    public SubjectVisitSteps(){
        subjectTable = PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
        subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
        visitTable = PageFactory.initElements(Browser.getDriver(), VisitTable.class);
        visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
        assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
        assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
        newSubject = PageFactory.initElements(Browser.getDriver(), NewEditSubject.class);
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
    }
    
////////////////////////////////////////////////////////////////////////////////////////////
	
	public void verifyVisitConditions(String visitName, int visitPos, String expStatus, boolean shouldDelBtnPresent,
			boolean shouldBeNotAssigned) {
		PdfLog.setStep("Verify conditions for visit: [" + visitName + "] in position:[ " + visitPos);
		TemplateSteps templateSteps = new TemplateSteps();
		boolean statusVerified = visitStatusVerification(expStatus, visitName, visitPos);
		boolean deleteBtnVerified = deleteButtonVisitTableVerification(shouldDelBtnPresent);
		boolean notAssignedVerified = templateSteps.notAssignedOptionVerification(shouldBeNotAssigned);
		StepVerify.True(Arrays.asList(new Boolean[] { statusVerified, deleteBtnVerified, notAssignedVerified }),
				Arrays.asList(new String[] { "Visit status is: " + expStatus, "Delete button is displayed",
						"Visit is not assigned" }),
				Arrays.asList(new String[] { "Visit status found not: " + expStatus,
						"Delete button found not displayed", "Visit found assigned" }));
	}
	
	public void verifyVisitStatus(String visitName, int visitPos, String expStatus) {
		PdfLog.setStep("Verify status for visit: [" + visitName + "] in position:[ " + visitPos);
		boolean statusVerified = visitStatusVerification(expStatus, visitName, visitPos);
		StepVerify.True(statusVerified, "Visit status is: " + expStatus, "Visit status found not: " + expStatus);
	}
////////////////////////////////////////////////////////////////////////////////////////////


	public boolean deleteButtonVisitTableVerification(Boolean isDisplayed) {
		if (isDisplayed) {
			return HardVerify.NotNull(subjectDetails.visitTable.getRemoveVisitButton(),
					"Verifying 'Delete button' is displayed", successStatus, "Delete button is not displayed. [FAILED]");
		} else {
			return HardVerify.Null(subjectDetails.visitTable.getRemoveVisitButton(),
					"Verifying 'Delete button' is not displayed", successStatus, "Delete button is displayed. [FAILED]");
		}
	}

	public void deleteButtonSubjectDetailsVerification(Boolean isDisplayed) {
		if (isDisplayed) {
			Log.logVerify("Delete button is displayed");
			Verify.True(UiHelper.isPresent(subjectDetails.deleteButton), "The delete button is not displayed");
		} else {
			Log.logVerify("Delete button is not displayed");
			Verify.False(UiHelper.isPresent(subjectDetails.deleteButton), "The delete button is displayed");
		}
	}

    /**
     * @author HISHAM
     * 
     */
	Map<String, Subject> subjectTableData = new HashMap<String, Subject>();

	public void getSubjectTableData() {
		Log.logStep("Getting available subject(s) list data...");
		Map<String, Subject> table = subjectTable.getTable();
		if (null != table) {
			subjectTableData = table;
		} else {
			throw new SkipException("Found 0 subject in list page. Skipping test...");
		}
	}
    
	public void subjectTableSubjectStatusVerification(String subject, String status) {
		Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] displayed on list page");
		Verify.Equals(status, subjectTableData.get(subject).getStatus(), String.format(errorMessage, subject));
	}

	public void subjectDetailsSubjectStatusVerification(String subject, String status) {
		Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] displayed on details page");
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		String statusFound = subjectDetails.getSubjectStatus();
		Log.logDebugMessage("Subject status found: " + statusFound);
		Verify.Equals(status, statusFound, String.format(errorMessage, subject));
	}
	
	/**
     * @author HISHAM
     * 
     */
	Map<String, Visit> visitTableData = new HashMap<String, Visit>();

	public void getVisitTableData() {
		Log.logStep("Getting available visit(s) list data...");
		Map<String, Visit> table = visitTable.getTable();
		if (null != table) {
			visitTableData = table;
		} else {
			throw new SkipException("Found 0 visit in list page. Skipping test...");
		}
	}
    
    /**
     * @author HISHAM
     * Description: Improved version
     */
	public void visitTableSubjectStatusVerification(String subject, String visit, String status, Boolean isDisplayed) {
		// List<Visit> listOfVisits = visitTable.getListOfVisitObjects();
//		Map<String, Visit> visits = visitTable.getTable();
		String keyInTable = TextHelper.appendValuesWithParentheses(visit, subject);
		String subjectName = visitTableData.get(keyInTable).getSubject();
		if (subjectName.equals(subject)) {
			if (isDisplayed) {
				Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] for Visit: [" + visit
						+ "] is displayed on Visit table");
				Verify.True(visitTableData.get(keyInTable).getSubjectStatus().equals(status),
						String.format(errorMessage, subject));
			} else {
				Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] for Visit: [" + visit
						+ "] is not displayed on Visit table");
				Verify.False(visitTableData.get(keyInTable).getSubjectStatus().equals(status),
						String.format(errorMessage, subject));
			}
		} else {
			Log.logError("There is no " + keyInTable + " in the Visit table");
		}
	}

    public void visitDetailsSubjectStatusVerification(String subject, String status){
		Log.logVerify("Verifying subject: [" + subject + "] has status: [" + status + "] in visit details");
		String statusFound = visitDetails.getSubjectStatus();
        Verify.True(statusFound.equals(status), String.format(errorMessage,subject));
    }
    
    public void subjectDetailsSubjectNameVerification(String subjectName) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		String nameFound = subjectDetails.getSubjectDetailsItemValue("TemporaryID");
		HardVerify.True(nameFound.equalsIgnoreCase(subjectName),
				"Verifying Subject Name: [" + subjectName + "] is present on subject details", successStatus,
				"Expected Subject Name: [" + subjectName + "] but found: [" + nameFound + "] [FAILED]");
	}
    
	public void visitDetailsSubjectNameVerification(String subjectName) {
		visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
		String nameFound = visitDetails.getVisitDetailsItemValue("Subject");
		HardVerify.True(nameFound.equalsIgnoreCase(subjectName),
				"Verifying Subject Name: [" + subjectName + "] is present on visit details", successStatus,
				"Expected Subject Name: [" + subjectName + "] but found: [" + nameFound + "] [FAILED]");
	}

	public void assessmentDetailsSubjectNameVerification(String subjectName) {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		String nameFound = assessmentDetails.getAssessmentDetailsItemValue("Subject");
		HardVerify.True(nameFound.equalsIgnoreCase(subjectName),
				"Verifying Subject Name: [" + subjectName + "] is present on assessment details", successStatus,
				"Expected Subject Name: [" + subjectName + "] but found: [" + nameFound + "] [FAILED]");
	}
    
    /**
     * @author HISHAM
     * 
     */
	Map<String, Assessment> assessmentTableData = new HashMap<String, Assessment>();

	public void getAssessmentTableData() {
		Log.logStep("Getting available assessment(s) list data...");
		Map<String, Assessment> table = assessmentsTable.getTable();
		if (null != table) {
			assessmentTableData = table;
		} else {
			throw new SkipException("Found 0 assessment in list page. Skipping test...");
		}
	}
    
    /**
     * @author HISHAM
     * Description: Improved version
     */
	public void assessmentTableSubjectStatusVerification(String subject, String visit, String status,
			Boolean isDisplayed) {
		// List<Visit> listOfVisits = visitTable.getListOfVisitObjects();
//		Map<String, Assessment> assessments = assessmentsTable.getTable();
		String keyInTable = TextHelper.appendValuesWithParentheses(visit, subject);
		String subjectName = assessmentTableData.get(keyInTable).getSubject();
		if (subjectName.equals(subject)) {
			if (isDisplayed) {
				Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] for Visit: [" + visit
						+ "] is displayed on Assessment table");
				Verify.True(assessmentTableData.get(keyInTable).getSubjectStatus().equals(status),
						String.format(errorMessage, subject));
			} else {
				Log.logVerify("Verifying Subject: [" + subject + "] has Status: [" + status + "] for Visit: [" + visit
						+ "] is not displayed on Assessment table");
				Verify.False(assessmentTableData.get(keyInTable).getSubjectStatus().equals(status),
						String.format(errorMessage, subject));
			}
		} else {
			Log.logError("There is no " + keyInTable + " in the Assessment table");
		}
	}

	public void addNewSubjectButtonVerification() {
		StepVerify.True(UiHelper.isPresent(subjectTable.addNewSubject), "Navigating to subject grid of the study",
				"Option to add subject is available", "Option to add subject found not available");
	}
    
    public void openNewSubjectForm(String site){
        subjectTable.openNewSubjectForm(site);
    }

	public void openNewSubjectFormAndVerify(String site) {
		subjectTable.openNewSubjectForm(site);
		// Release 2016.2
		if (UiHelper.isEnabled(newSubject.getConsentDropdown())) {
			StepVerify.True(
					Arrays.asList(new Boolean[] { newSubject.isDisplayed(), Required.isRequired(newSubject.screening),
							Required.isDropdownRequired(newSubject.languageDropdown),
							Required.isDropdownRequired(newSubject.consentDropdown) }),
					"Clicking on add subject by selecting the site: [" + site + "]",
					Arrays.asList(new String[] { "New subject entry screen is displayed",
							"Required field 'Screening #' stays highlighted",
							"Required dropdown 'Language' stays highlighted",
							"Required dropdown 'Conscent to record' stays highlighted" }),
					Arrays.asList(new String[] { "New subject entry screen found not displayed",
							"Required field 'Screening #' found not highlighted",
							"Required dropdown 'Language' found not highlighted",
							"Required dropdown 'Conscent to record' found not highlighted" }));
		} else {
			StepVerify.True(
					Arrays.asList(new Boolean[] { newSubject.isDisplayed(), Required.isRequired(newSubject.screening),
							Required.isDropdownRequired(newSubject.languageDropdown), }),
					"Clicking on add subject by selecting the site: [" + site + "]",
					Arrays.asList(new String[] { "New subject entry screen is displayed",
							"Required field 'Screening #' stays highlighted",
							"Required dropdown 'Language' stays highlighted", }),

					Arrays.asList(new String[] { "New subject entry screen found not displayed",
							"Required field 'Screening #' found not highlighted",
							"Required dropdown 'Language' found not highlighted", }));
		}
	}

	/*public void requiredFieldsVerification(Boolean isRequired) {
		if (isRequired) {
			HardVerify.True(Required.isRequired(newSubject.screening),
					"Verifying Screening field is" + (isRequired ? "" : " not") + " required", successStatus,
					"Screening field found not required [FAILED]");
			HardVerify.True(Required.isDropdownRequired(newSubject.languageDropdown),
					"Verifying Language dropdown is" + (isRequired ? "" : " not") + " required", successStatus,
					"Language dropdown found not required [FAILED]");
			if (UiHelper.isClickable(newSubject.consentDropdown)) { // Release
																	// 2016.2
				HardVerify.True(Required.isDropdownRequired(newSubject.consentDropdown),
						"Verifying 'Consent To Record' dropdown is" + (isRequired ? "" : " not") + " required",
						successStatus, "'Consent To Record' dropdown found not required [FAILED]");
			}


		} else {
			HardVerify.False(Required.isRequired(newSubject.screening),
					"Verifying Screening field is" + (isRequired ? "" : " not") + " required", successStatus,
					"Screening field found required [FAILED]");
			HardVerify.False(Required.isDropdownRequired(newSubject.languageDropdown),
					"Verifying Language dropdown is" + (isRequired ? "" : " not") + " required", successStatus,
					"Language dropdown found required [FAILED]");
			if (UiHelper.isClickable(newSubject.consentDropdown)) { // Release
																	// 2016.2
				HardVerify.False(Required.isDropdownRequired(newSubject.consentDropdown),
						"Verifying 'Consent To Record' dropdown is" + (isRequired ? "" : " not") + " required",
						successStatus, "'Consent To Record' dropdown found required [FAILED]");
			}
		}
	}*/
	
	/**
	 * Release 2016.3
	 * 
	 * @param fields
	 * @author HISHAM
	 */
	public void verifyFieldsAreRequired(List<String> fields, boolean isRequired) {
		for (String field : fields) {
			WebElement fieldElement = newSubject.getInputByName(field);
			if (isRequired) {
				HardVerify.True(Required.isRequired(fieldElement), "Verifying field: [" + field + "] is required",
						successStatus, "Field: [" + field + "] found not required [FAILED]");
			} else {
				HardVerify.False(Required.isRequired(fieldElement), "Verifying field: [" + field + "] is not required",
						successStatus, "Field: [" + field + "] found required [FAILED]");
			}
		}
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param fields
	 * @author HISHAM
	 */
	public void verifyFieldIsRequired(String field, boolean isRequired) {
		WebElement fieldElement = newSubject.getInputByName(field);
		if (isRequired) {
			HardVerify.True(Required.isRequired(fieldElement), "Verifying field: [" + field + "] is required",
					successStatus, "Field: [" + field + "] found not required [FAILED]");
		} else {
			HardVerify.False(Required.isRequired(fieldElement), "Verifying field: [" + field + "] is not required",
					successStatus, "Field: [" + field + "] found required [FAILED]");
		}
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * @param isVisible
	 * 
	 * @author HISHAM
	 */
	public void verifyFieldIsVisible(String field, boolean isVisible) {
		WebElement fieldElement = newSubject.getInputByName(field);
		if (isVisible) {
			HardVerify.True(UiHelper.isVisible(fieldElement), "Verifying field: [" + field + "] is visible",
					successStatus, "Field: [" + field + "] found not visible [FAILED]");
		} else {
			HardVerify.False(UiHelper.isVisible(fieldElement), "Verifying field: [" + field + "] is not visible",
					successStatus, "Field: [" + field + "] found visible [FAILED]");
		}
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * @param isDisabled
	 * 
	 * @author HISHAM
	 */
	public void verifyFieldIsDisabled(String field, boolean isDisabled) {
		WebElement fieldElement = newSubject.getInputByName(field);
		if (isDisabled) {
			HardVerify.False(UiHelper.isEnabled(fieldElement), "Verifying field: [" + field + "] is disabled",
					successStatus, "Field: [" + field + "] found enabled [FAILED]");
		} else {
			HardVerify.True(UiHelper.isEnabled(fieldElement), "Verifying field: [" + field + "] is enabled",
					successStatus, "Field: [" + field + "] found disabled [FAILED]");
		}
	}

    public void fillingRequiredFields(String subjectName){
        newSubject.fillRequiredFields(subjectName);
    }
    
	/*public void fillNotAllRequiredFields(String fieldName) {
		fillingRequiredFields(fieldName);
		if (UiHelper.isClickable(newSubject.consentDropdown)) {
			StepVerify.True(
					newSubject.getInputByName("Screening#").getAttribute("value").equalsIgnoreCase(fieldName)
							&& Required.isDropdownRequired(newSubject.languageDropdown)
							&& Required.isDropdownRequired(newSubject.consentDropdown)
							&& newSubject.isSaveButtonDisabled(),
					"Entering not all required subject data",
					"- Data are entered\n- Not filled required fields stay highlighted\n- Save options is not enabled​",
					successStatus,
					"- Data not entered\n- Not filled required fields found not highlighted\n- Save options found enabled​",
					failStatus);
		} else {
			StepVerify.True(
					newSubject.getInputByName("Screening#").getAttribute("value").equalsIgnoreCase(fieldName)
							&& Required.isDropdownRequired(newSubject.languageDropdown)
							&& newSubject.isSaveButtonDisabled(),
					"Entering not all required subject data",
					"- Data are entered\n- Not filled required fields stay highlighted\n- Save options is not enabled​",
					successStatus,
					"- Data not entered\n- Not filled required fields found not highlighted\n- Save options found enabled​",
					failStatus);
		}
	}*/
    
	public void fillNotAllRequiredFields(String fieldName) {
		newSubject.fillRequiredField("Screening#", fieldName);
		if (UiHelper.isEnabled(newSubject.getConsentDropdown())) {
			StepVerify.True(
					Arrays.asList(
							new Boolean[] {
									newSubject.getInputByName("Screening#").getAttribute("value").equalsIgnoreCase(
											fieldName),
									Required.isDropdownRequired(newSubject.languageDropdown),
									Required.isDropdownRequired(newSubject.consentDropdown),
									newSubject.isSaveButtonDisabled() }),
					"Entering not all required subject data",
					Arrays.asList(new String[] { "Data are entered",
							"Required field language dropdown stays highlighted",
							"Required field conscent dropdown stays highlighted", "Save options is not enabled" }),
					
					Arrays.asList(new String[] { "Data are not entered",
							"Required field language dropdown stays not highlighted",
							"Required field conscent dropdown stays not highlighted", "Save options is enabled" })
					);
		} else {
			StepVerify.True(Arrays.asList(new Boolean[] {
					newSubject.getInputByName("Screening#").getAttribute("value").equalsIgnoreCase(fieldName),
					Required.isDropdownRequired(newSubject.languageDropdown),

					newSubject.isSaveButtonDisabled() }),
					"Entering not all required subject data",
					Arrays.asList(new String[] { "Data are entered",
							"Required field language dropdown stays highlighted", "Save options is not enabled" }),
					
					Arrays.asList(new String[] { "Data are not entered",
							"Required field language dropdown stays not highlighted", "Save options is enabled" })
					);
		}
	}
    
    /**
	 * Release 2016.3
	 * 
	 * @param fields
	 * @author HISHAM
	 */
	public void fillingRequiredField(String field, String subjectName) {
		newSubject.fillRequiredField(field, subjectName);
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * @author HISHAM
	 */
	public void clearRequiredField(String field) {
		newSubject.clearRequiredField(field);
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * @author HISHAM
	 */
	public void selectRequiredDropdown(String dropdown) {
		newSubject.selectRequiredDropdown(dropdown);
	}

    public void invalidSymbolsInFieldVerification(String fieldName){
        Log.logVerify("Check verification of invalid symbols in initial field");        
        String[] array = {"!","@","#","$","%","^","&","*","(",")","_","+","=","[","]",";",",",".","/","{","}","|",":","<",">","?`","~","ï¿½","1","2","3","4","5","6","7","8","9","0"};
        WebElement field = newSubject.getInputByName(fieldName);
		for (int i = 0; i < array.length; i++) {
			field.sendKeys(array[i]);
			Verify.True(Required.isInputInvalid(field), array[i] + " should be invalid");
			field.clear();
		}
    }

    public void validSymbolsInFieldVerification(String fieldName){
		Log.logVerify(fieldName + "field valid input");
		PdfLog.setStep("Verify subject field: [" + fieldName + "] valid input");
		WebElement field = newSubject.getInputByName(fieldName);
		UiHelper.sendKeys(field, "a-bZ");
		StepVerify.False(Required.isInputInvalid(field), "The Initials is valid", "The Initials found not valid");
    }

	public void maxLengthFieldVerification(String field, String length) {
		Log.logVerify(field + " field max length=" + length);
		PdfLog.setStep("Verify subject field: [" + field + "] max length");
		String actualLength = UiHelper.getAttributeValue(newSubject.getInputByName(field), "maxlength");
		StepVerify.Equals(actualLength, length, "The max length is: " + length,
				"The max length found: " + actualLength);
	}

	public void cancelButtonActionVerification() {
		newSubject.cancelNewSubject();
		StepVerify.True(UiHelper.isPresent(subjectTable.addNewSubject),
				"Verifying subject list is opened after click on cancel button", "Subject list found opened",
				"Subject list found not opened");
	}

    public void saveNewChanges(){
        newSubject.saveNewSubject();
    }
    
    public void saveChangesWithEsign(String userName, String pass){
        newSubject.saveEdittedSubject();
        if (esignSteps.dialogIsOpenedVerification()) {
			esignSteps.esignDialogPredefinedReason(EsignReasonFields.ERROR.getValue());
			esignSteps.esignDialogConfirm(userName, pass);
		}
    }
    
    public void addSubjectToSite(String subject, String site){
        openNewSubjectForm(site);
        fillingRequiredFields(subject);
        saveNewChanges();
    }
    
	public void saveEditedSubjectAndVerify(String name, String lang, String userName, String pass) {
		PdfLog.setStep("Fill all required fields and save editted subject");
		editSubjectFieldValue(SubjectDetailsFields.SCREENING.getValue(), name);
		editSubjectFieldValue(SubjectDetailsFields.LANGUAGE.getValue(), lang);
		saveChangesWithEsign(userName, pass);

		String nameFound = subjectDetails.readDetailsFromUi().get(SubjectDetailsFields.SCREENING.getValue());
		String languageFound = subjectDetails.readDetailsFromUi().get(SubjectDetailsFields.LANGUAGE.getValue());
		StepVerify.True(
				Arrays.asList(new Boolean[] { name.equalsIgnoreCase(nameFound), lang.equalsIgnoreCase(languageFound) }),
				Arrays.asList(new String[] { "Screening# is updated", "Language is updated" }),
				Arrays.asList(new String[] { "Screening# found not updated", "Language found not updated" }));
	}

	public void editSubject(String subjectName, List<String> subjectDetailsFieldsList, List<String> fieldsNewValues,
			Boolean isSave, String userName, String pass) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		if (false == subjectDetails.isOpened()) {
			subjectTable.openSubjectDetails(subjectName);
		}

		if (false == newSubject.isSubjectEditable()) {
			clickOnEditSubjectButton();
		}

		for (int i = 0; i < subjectDetailsFieldsList.size(); i++) {
			editSubjectFieldValue(subjectDetailsFieldsList.get(i), fieldsNewValues.get(i));
		}
		if (isSave) {
			saveChangesWithEsign(userName, pass);
		} else {
			subjectDetails.cancelEditedSubject();
		}
	}
	
	public void openSubjectDetailsAndVerify(String subjectName) {
		subjectTable = PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
		PdfLog.setStep("Open Subject: [" + subjectName + "] Details");
		subjectDetails = subjectTable.openSubjectDetails(subjectName);
		StepVerify.True(subjectDetails.detailsIsOpened(subjectName), "Details for subject: ["+subjectName+"] is displayed",
				"Details for subject: ["+subjectName+"] found not displayed");
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}
	
    public void clickOnEditSubjectButton(){
		Log.logStep("Click on Edit subject");
        subjectDetails.subjectToEdit();
    }
    
	public void clickOnEditSubjectAndVerify() {
		PdfLog.setStep("Click on Edit subject");
		subjectDetails.subjectToEdit();
		StepVerify.True(newSubject.isSubjectEditable(), "Fields that can be changed is editable",
				"Fields that can be changed found not editable");
	}
    
	public void clearFieldAndVerifyIsRequired(String field) {
		PdfLog.setStep("Clear: [" + field + "] field");
		newSubject.getInputByName(field).clear();
		StepVerify.True(
				Arrays.asList(
						new Boolean[] { Required.isRequired(newSubject.screening), newSubject.isSaveButtonDisabled() }),
				Arrays.asList(new String[] { "Required field 'Screening #' stays highlighted",
						"Save option is not enabled" }),
				Arrays.asList(new String[] { "Required field 'Screening #' found not highlighted",
						"Save option found enabled" }));
	}

	public void editSubjectFieldValue(String field, String value) {
		Log.logStep("Editting subject field: [" + field + "]");
		newSubject.editSubject(field, value);
	}
	
	public void editSubjectFieldValueAndVerify(String field, String value) {
		PdfLog.setStep("Editting subject field: [" + field + "]");
		newSubject.editSubject(field, value);
		
		switch (field) {
		case "Screening#":
			StepVerify.True(newSubject.getInputByName(field).getAttribute("value").equalsIgnoreCase(value),
					"Subject field: [" + field + "] is editted", "Field: [" + field + "] found not editted");
			break;
		case "Language":
			Log.logDebugMessage(newSubject.getDropDownByName(field).getText());
			StepVerify.True(newSubject.getDropDownByName(field).getText().equalsIgnoreCase(value),
					"Subject field: [" + field + "] is editted", "Field: [" + field + "] found not editted");
			break;

		default:
			break;
		}
	}

    public void cancelChanges(){
        subjectDetails.cancelEditedSubject();
    }
    
	public void cancelChangesAndVerify(String name, String lang) {
		PdfLog.setStep("Click on Cancel");
		cancelChanges();
		String nameFound = subjectDetails.readDetailsFromUi().get(SubjectDetailsFields.SCREENING.getValue());
		String languageFound = subjectDetails.readDetailsFromUi().get(SubjectDetailsFields.LANGUAGE.getValue());
		StepVerify.True(
				Arrays.asList(new Boolean[] { name.equalsIgnoreCase(nameFound), lang.equalsIgnoreCase(languageFound) }),
				Arrays.asList(new String[] { "Screening# is not changed", "Language is not changed" }),
				Arrays.asList(new String[] { "Screening# found changed", "Language found changed" }));
	}

    public void discontinuedStatusAreaVerification(Boolean isDisplayed){
        Log.logVerify("Text area display verification");
        if(isDisplayed){
            Verify.True(UiHelper.isPresent(newSubject.textArea), "Text area is not displayed");
        } else {
            Verify.False(UiHelper.isPresent(newSubject.textArea), "Text area is displayed but should not");
        }
    }

	public void createNewSubject(String subjectName) {
		Log.logStep("Creating new Subject Screening no. as: [" + subjectName + "]");
		newSubject = subjectTable.openNewSubjectForm("");
		newSubject.fillRequiredFields(subjectName);
		newSubject.saveNewSubject();
	}

	public void selectAnsScheduleVisit(String visitName) {
		selectVisit(visitName);
		scheduleVisit();
	}
    
	/*public void selectAndScheduleVisitInPosition(String visitName, int position) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName, position);
		scheduleVisit();
	}*/
	
	public void selectAndScheduleVisitInPosition(String visitName, int position) {
		PdfLog.setStep("Schedule visit: [" + visitName + "] in position:[" + position + "]");
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName, position);
		scheduleVisit();
		StepVerify.True(subjectDetails.visitTable.isVisitScheduled(visitName),
				"Visit: [" + visitName + "] in position:[" + position + "] is scheduled.",
				"Visit: [" + visitName + "] in position:[" + position + "] found not scheduled.");
	}
	
	
	
	public void selectVisitInPosition(String visitType, String visitName, String position) {
		if (visitType.equalsIgnoreCase("Scheduled")) {
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			subjectDetails.visitTable.selectVisit(visitName, Integer.parseInt(position));
		}
	}
	
	public void selectVisitByName(String visitType, String visitName) {
		if (visitType.equalsIgnoreCase("Scheduled")) {
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			subjectDetails.visitTable.selectVisit(visitName);
		}
	}
	
	public void selectVisitWithStatus(String visitName, String visitStatus) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName, visitStatus);
	}
	
	public void selectUnscheduledVisitByName(String visitName) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		subjectDetails.visitTable.selectVisit(visitName);
	}

    public void discontinuedVerification(){
        newSubject = subjectDetails.subjectToEdit();
        Verify.Equals("true", UiHelper.getAttributeValue(UiHelper.findElement(newSubject.statusDropdown), "disabled"),
                "The DropDown should be inactive");
    }

    public void saveButtonVerification(Boolean isDisabled){
        if (isDisabled){
            Log.logVerify("Save button is disabled");
            Verify.True(newSubject.isSaveButtonDisabled(), "The save button is enabled");
        }
        else {
            Log.logVerify("Save button is not disabled");
            Verify.False(newSubject.isSaveButtonDisabled(), "The save button is not enabled");
        }
    }

    public void subjectEditableVerification(){
        Log.logVerify("details can be edit");
        Verify.True(newSubject.isSubjectEditable(),"the subject is not editable");
        saveButtonVerification(true);
    }

    public void addUnscheduledVisit(String visitName){
        subjectDetails.visitTable.addUnscheduleVisit(visitName);
        subjectDetails.refreshSubject(); //to get updated DOM structure for newly added visit element
    }
    
	public void addUnscheduledVisit(String type, String crType, String visitName) {
		if (type.equalsIgnoreCase("Unscheduled")) {
			subjectDetails.visitTable.addUnscheduleVisit(visitName);
			if (crType.equalsIgnoreCase("NonCR"))
				// to get updated DOM structure for newly added visit element
				subjectDetails.refreshSubject();
		}
	}

    public void subjectFieldValueVerification(SubjectDetailsFields subjectDetailsFields, String valueToVerify){
        String name = subjectDetailsFields.getValue();
        Log.logVerify(name + " is not changed");
        Verify.Equals(valueToVerify, subjectDetails.readDetailsFromUi().get(name), name + " name is incorrect");
    }

	public void subjectNotInSubjectTableVerification(String subjectName) {
		List<Subject> subjects = subjectTable.getListOfSubjectObjects();
		if (subjects.size() > 0) { // Modified to handle subject deletion for a study-site with single subject 
			for (Subject subj : subjects) {
				Verify.False(subj.getSubjectName().equals(subjectName), "The subject is not deleted");
			}
		} else {
			Verify.Equals(0, subjects.size(), "The subject is not deleted");
		}
	}

    public void deleteSubject(){
        subjectDetails.deleteSubject();
    }

    public void dashboardStatusesOrderVerification(List<String> expectedStatuses ){
        Verify.Equals(expectedStatuses, dashboard.getStatusesOfEntity("Subjects"),
                "Subject statuses have incorrect order");
    }

    public boolean visitSchedulingVerification(String pendingVisitName){
        return Verify.True(subjectDetails.visitTable.isVisitScheduled(pendingVisitName),
                "The icon is not displayed");
    }

    public void visitSkippedVerification(String pendingVisitName){
        Verify.True(subjectDetails.visitTable.isVisitSkipped(pendingVisitName),
                "The icon is not displayed");
    }
	
	/**
	 * @author ahisham
	 * 
	 * @param status
	 * @param visitName
	 * @param position
	 */
	public boolean visitStatusVerification(String status, String visitName, int position) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		String actualStatus = subjectDetails.visitTable.getVisitStatus(visitName, position);
		return Verify.True(status.equalsIgnoreCase(actualStatus), "Verifying visit status: [" + status + "]", successStatus,
				"Expected status: [" + status + "], but found: [" + actualStatus + "] [FAILED]");
	}

	public void visitDeletionVerification() {
		HardVerify.Null(subjectDetails.visitTable.getRemoveVisitButton(), "Verifying visit is deleted/unscheduled",
				successStatus, "The visit is not deleted/unscheduled. [FAILED]");
	}

	public void reasonFieldVerification(SubjectDetailsFields subjectDetailsFields, Boolean isDisplayed) {
		String name = subjectDetailsFields.getValue();
		String formType = name.replace("Disable", "").trim();
		UiHelper.click(newSubject.getInputByName(name));
		WebElement reasonPro = newSubject.getTextAreaByName("Reason for disabling " + formType);
		if (isDisplayed) {
			Log.logVerify("Reason " + name + " field is displayed");
			Verify.True(UiHelper.isPresent(reasonPro), "The reason input is not displayed");
			Log.logVerify("Reason field required");
			Verify.True(Required.isRequired(reasonPro), "The reason input is not required");
		} else {
			Log.logVerify("Reason " + name + " field is not displayed");
			Verify.False(UiHelper.isPresent(reasonPro), "The reason input is displayed");
			Log.logVerify("Reason field required");
			Verify.False(Required.isRequired(reasonPro), "The reason input is required");
		}
	}

    public void reasonContentVerification(SubjectDetailsFields subjectDetailsFields, String content){
        String name = subjectDetailsFields.getValue();
        Log.logVerify(name+" field value");
        WebElement element = UiHelper.findElementByXpath("//label[@title='"+content+"']");
        Verify.Equals(content, UiHelper.getText(element),"The value in reason input is not displayed");
    }

	public void fillTheReasonTextArea(SubjectDetailsFields subjectDetailsFields, String reason) {
		String name = subjectDetailsFields.getValue();
		if (name.equals(SubjectDetailsFields.DISABLEPRO.getValue())
				&& UiHelper.isClickable(newSubject.disableProArea)) {
			UiHelper.sendKeys(newSubject.disableProArea, reason);
		}
		if (name.equals(SubjectDetailsFields.DISABLEOBSRO.getValue())
				&& UiHelper.isClickable(newSubject.disableObsroArea)) {
			UiHelper.sendKeys(newSubject.disableObsroArea, reason);
		}
	}
	
    public void disableForm(SubjectDetailsFields subjectDetailsFields){
        newSubject.disableForm(subjectDetailsFields.getValue());
    }
    
    public void enableForm(SubjectDetailsFields subjectDetailsFields){
        UiHelper.click(newSubject.getInputByName(subjectDetailsFields.getValue()));
    }
    
	/**
	 * </p>
	 * Description: This function verifies the clickability of 'Consent To
	 * Record' dropdown in subject details page based on isEnabled parameter
	 * value.
	 * </p>
	 * 
	 * @param isEnabled
	 *            - Conscent dropdown should be clickable if true, false
	 *            otherwise
	 * 
	 * @author HISHAM
	 */
	public void consentDropdownVerification(boolean isEnabled) {
		if (isEnabled) {
			StepVerify.True(UiHelper.isClickable(newSubject.consentDropdown),
					"Verifying 'Consent To Record' dropdown is enabled for clicking...",
					"'Consent To Record' dropdown is enabled for clicking",
					"'Consent To Record' dropdown found disabled");
		} else {
			StepVerify.False(UiHelper.isClickable(newSubject.consentDropdown),
					"Verifying 'Consent To Record' dropdown is not enabled for clicking...",
					"'Consent To Record' dropdown is enabled for clicking",
					"'Consent To Record' dropdown found enabled");
		}
	}
	
	/**
	 * </p>
	 * Description: This function verifies the clickability & text can be
	 * entered in 'Comments' area of subject details page
	 * </p>
	 * 
	 * @param comment
	 *            - Comments to be entered by user
	 * 
	 * @author HISHAM
	 */
	/*public void commentsFieldVerification(String comment) {
		WebElement commentArea = UiHelper.findElement(newSubject.textArea);
		try {
			HardVerify.True(UiHelper.isClickable(commentArea), "Verifying Comments text-box is enabled for editing...",
					successStatus, "Comments text-box found disabled. [FAILED]");
			Log.logStep("Entering text [" + comment + "] to Comments area");
			UiHelper.sendKeys(commentArea, comment);
			String actualComment = commentArea.getAttribute("value");
			HardVerify.EqualsIgnoreCase(actualComment, comment, "Verifying user can enter comments for a subject...",
					successStatus, "Entered comments doesn't match. Expected: [" + comment + "] but found: ["
							+ actualComment + "]. [FAILED]");
		} catch (Exception e) {
			throw new SkipException("Comments field not found or disabled", e);
		}
	}*/
	public void commentsFieldVerification(String comment) {
		WebElement commentArea = UiHelper.findElement(newSubject.textArea);

		StepVerify.True(UiHelper.isClickable(commentArea), "Verifying Comments text-box is enabled for editing...",
				"Comments text-box is enabled for editing", "Comments text-box found disabled");
		Log.logStep("Entering text [" + comment + "] to Comments area");
		UiHelper.sendKeys(commentArea, comment);
		String actualComment = commentArea.getAttribute("value");
		StepVerify.EqualsIgnoreCase(actualComment, comment, "Verifying user can enter comments for a subject...",
				"User can enter comments for a subject", successStatus,
				"Entered comments doesn't match. Expected: [" + comment + "] but found: [" + actualComment + "]",
				failStatus);
	}
	
	/**
	 * </p>
	 * Description: This function verifies Temporary ID appears as a subject
	 * identifier in Subject List page
	 * </p>
	 * 
	 * @param tempId
	 * @author HISHAM
	 */
	public void verifySubjectWithTemporaryIdInSubjectList(String tempId) {
		HardVerify.True(subjectTable.isSubjectPresent(tempId),
				"Verifying Temporary ID: [" + tempId + "] appears as Subject Number in Subject List page", successStatus,
				"Expected Subject Number: [" + tempId + "] doesn't exist in list [FAILED]");
	}
	
	/**
	 * </p>
	 * Description: This function verifies Temporary ID appears as a subject
	 * identifier in Visit List page
	 * </p>
	 * 
	 * @param tempId
	 * @author HISHAM
	 */
	public void verifySubjectWithTemporaryIdInViistList(String tempId) {
		HardVerify.True(visitTable.isSubjectPresent(tempId),
				"Verifying Temporary ID: [" + tempId + "] appears as Subject Number in Visit List page", successStatus,
				"Expected Subject Number: [" + tempId + "] doesn't exist in list [FAILED]");
	}
	
	/**
	 * </p>
	 * Description: This function verifies Temporary ID appears as a subject
	 * identifier in Assessment List page
	 * </p>
	 * 
	 * @param tempId
	 * @author HISHAM
	 */
	public void verifySubjectWithTemporaryIdInAssessmentList(String tempId) {
		HardVerify.True(assessmentsTable.isSubjectPresent(tempId),
				"Verifying Temporary ID: [" + tempId + "] appears as Subject Number in Assessment List page", successStatus,
				"Expected Subject Number: [" + tempId + "] doesn't exist in list [FAILED]");
	}
	
	/**
	 * </p>
	 * DescriptIon; This function selects specific visit action based on given
	 * type(i.e: Add/Edit/Remove/ViewReschedule/Cancel/AddUnscheduled)
	 * </p>
	 * 
	 * @param action
	 * 			- String, name of the visit action
	 * 
	 * @author HISHAM
	 */
	public void selectVisitAction(String visitType, String action) {
		if (visitType.equalsIgnoreCase("Scheduled")) {
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			Log.logStep("Clicking: [" + action + "] for selected visit");

			switch (action) {
			case "Add":
				subjectDetails.visitTable.getAddVisitButton().click();
				break;

			case "Edit":
				subjectDetails.visitTable.getEditVisitButton().click();
				break;

			case "Remove":
				subjectDetails.visitTable.getRemoveVisitButton().click();
				break;

			case "ViewReschedule":
				subjectDetails.visitTable.getViewRescheduleAppointmentButton().click();
				break;

			case "Cancel":
				subjectDetails.visitTable.getCancelAppointmentButton().click();
				break;

			/*
			 * case "AddUnscheduled":
			 * subjectDetails.visitTable.getAddUnscheduledVisitButton().click();
			 * break;
			 */

			default:
				// Log.logWarning("Action type didn't match...");
				break;
			}
		}
	}
	
	/**
	 * @author HISHAM
	 */
	public void revertVisitStatus(String visitType, String visitName, String revertAction, String login, String pass){
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		
		Log.logStep("Selecting : [" + revertAction + "] for selected visit to revert back...");

		switch (revertAction) {
		case "AddScheduled":
			selectVisitByName(visitType, visitName);
			subjectDetails.visitTable.getAddVisitButton().click();
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			break;
			
		case "AddUnscheduled":
			addUnscheduledVisit("Unscheduled", "NonCR", visitName);
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			break;

		case "RemoveScheduled":
			selectVisitByName(visitType, visitName);
			subjectDetails.visitTable.getRemoveVisitButton().click();
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			break;
			
		case "RemoveUnscheduled":
			selectUnscheduledVisitByName(visitName);
			subjectDetails.visitTable.getRemoveVisitButton().click();
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			break;

		case "UncheckNotAdministered":
			selectVisitByName(visitType, visitName);
			subjectDetails.visitTable.getCancelAppointmentButton().click();
			UiHelper.waitForSpinnerEnd(Browser.getDriver());
			assessmentDetails = subjectDetails.clickSubmittedOrAssignedFormThumbnail(0);
			if(assessmentDetails.isNotAdministered()){
				UiHelper.click(assessmentDetails.getNotAdminsteredCheckbox());
				EsignDialog esign = assessmentDetails.confirmAssesment();
				esign.inputPredefinedReason("Technical difficulties");
				esign.loginAs(login, pass);
				UiHelper.waitForSpinnerEnd(Browser.getDriver());
				ToolBarFull toolbar = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
				toolbar.clickLastLinkFromBreadcrumbs();
				UiHelper.waitForSpinnerEnd(Browser.getDriver());
			}
			break;
			
		default:
			Log.logWarning("Revert Action type didn't match...");
			break;
		}
//		subjectDetails.refreshSubject();
	}
	
	public int getFormsCountForTypes(List<String> types) {
		subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
		int formsFound = subjectDetails.getAllFormsOfTypes(types).size();
		Log.logInfo("Found: [" + formsFound + "] form(s) for type: " + types.toString());
		return formsFound;
	}
	
	}

