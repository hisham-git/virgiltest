package mt.siteportal.pages.studyDashboard;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.BasePage;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Required;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;

public class NewEditSubject extends BasePage{
	private static final String SCREENING = "Screening#";
	private static final String TEMPID = "TemporaryID";
	private static final String STATUS = "Status";
	private static final String LANGUAGE = "Language";

	public By screening = new By.ByXPath("//input[@name='screeningNum']");
	public By tempId = new By.ByXPath("//input[@name='temporaryId']");
	public By languageDropdown = new By.ByXPath("//div[@data-ng-required='isCultureRequired()']//button");
	public By consentDropdown = new By.ByXPath("//div[@data-ng-required='isConsentToRecordRequired()']//button");
	private By saveButton = new By.ByCssSelector(".icon-save");
	private By cancelNew = new By.ByXPath("//a[@data-ng-click='cancelToBack()']/span");
	public By disableProArea = new By.ByXPath("//textarea[parent::div[preceding-sibling::div/label[contains(text(),'PRO')]]]");
	public By disableObsroArea = new By.ByXPath("//textarea[parent::div[preceding-sibling::div/label[contains(text(),'ObsRO')]]]");
	@FindBy(how = How.XPATH, using = "//a[@data-ng-click='cancelChanges()']/span")
	private WebElement CancelChanges;
	public By statusDropdown = new By.ByXPath("//label[text()='Status']/../following-sibling::div//button");

	public By textArea = new By.ByXPath("//textarea[@class='textarea-details ng-pristine ng-untouched ng-valid']");

	private String requiredFieldNames = "//*[@required]/../preceding-sibling::div";
	private String isEditableLocator = "//div[@class='details-grid row edit']";
	private By header = new By.ByCssSelector("div#page-title h1[data-ng-show='isNewModel']"); 

	public NewEditSubject(WebDriver driver) {
		super(driver);
	    }
	
	private String consentToRecords = "Yes";
	
	public String getHeader() {
		return UiHelper.fluentWaitForElementVisibility(header, 10).getText().trim();
	}
	
	public WebElement getConsentDropdown() {
		return UiHelper.fluentWaitForElementVisibility(consentDropdown, 10);
	}
	
	public void fillRequiredFields(String nameSubject) {
		String defaultLanguage = "English (US)";
		Log.logStep("Filling required field 'Screening#' with: [" + nameSubject + "]");
		UiHelper.sendKeys(screening, nameSubject);
		Log.logStep("Selecting required dropdown 'Language' with: [" + defaultLanguage + "]");
		UiHelper.selectInDropdownBtn(languageDropdown, defaultLanguage);
		if (UiHelper.isClickable(consentDropdown))
			UiHelper.selectInDropdownBtn(consentDropdown, consentToRecords);
	}
	
	public void fillRequiredFields(String nameSubject, String language){
		Log.logInfo("Filling required fields with Default "+language+" language");
		UiHelper.sendKeys(screening, nameSubject);
		UiHelper.selectInDropdownBtn(languageDropdown, language);
		if (UiHelper.isClickable(consentDropdown)) 
			UiHelper.selectInDropdownBtn(consentDropdown, consentToRecords);
	}
	
	public void fillRequiredFieldsWithConsent(String nameSubject, String language){
		Log.logInfo("Filling required fields with Default "+language+" language");
		UiHelper.sendKeys(screening, nameSubject);
		UiHelper.selectInDropdownBtn(languageDropdown, language);
		if (UiHelper.isClickable(consentDropdown)) 
			UiHelper.selectInDropdownBtn(consentDropdown, consentToRecords);
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * @param subjectName
	 * 
	 * @author HISHAM
	 */
	public void fillRequiredField(String field, String subjectName) {
		WebElement fieldElement = getInputByName(field);
		Log.logStep("Filling required field: [" + field + "]  with: [" + subjectName + "]");
		UiHelper.sendKeys(fieldElement, subjectName);
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param field
	 * 
	 * @author HISHAM
	 */
	public void clearRequiredField(String field) {
		WebElement fieldElement = getInputByName(field);
		Log.logStep("Clearing required field: [" + field + "]");
		fieldElement.clear();
	}
	
	/**
	 * Release 2016.3
	 * 
	 * @param dropdown
	 * 
	 * @author HISHAM
	 */
	public void selectRequiredDropdown(String dropdown) {
		String defaultLanguage = "English (US)";
		WebElement dropdownElement = getDropDownByName(dropdown);
		Log.logStep("Selecting required dropdown '" + dropdown + "' with: [" + defaultLanguage + "]");
		UiHelper.selectInDropdownBtn(dropdownElement, defaultLanguage);
	}

	public void disableForm(String fieldName){
		Log.logInfo("Disabling form type "+fieldName);
		UiHelper.click(getInputByName(fieldName));
		String reason = "The form is obsolete";
		if(fieldName.equals("Disable PRO")){
			UiHelper.sendKeys(getTextAreaByName("Reason for disabling PRO"), reason);
		}
		else if(fieldName.equals("Disable ObsRO")){
			UiHelper.sendKeys(getTextAreaByName("Reason for disabling ObsRO"), reason);
		}

	}
	
	public SubjectDetails saveNewSubject(){
		Log.logInfo("Saving new subject");
		UiHelper.click(saveButton);
		return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}
	
	public EsignDialog saveEdittedSubject() {
		Log.logInfo("Saving editted subject");
		UiHelper.click(saveButton);
		return PageFactory.initElements(Browser.getDriver(),EsignDialog.class);
	}
	
	public SubjectsTable cancelNewSubject(){
		Log.logInfo("Cancel new subject");
		UiHelper.click(cancelNew);
		return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
	}

	public void cancelEcitedSubject(){
		Log.logInfo("Cancel new subject");
		UiHelper.click(CancelChanges);
	}
	
	public List<String> getRequiredFieldNames(){
		Log.logInfo("Getting required field names");
		List <WebElement> requiredElements = UiHelper.findElements(new By.ByXPath(requiredFieldNames));
		return TextHelper.getElementTextContent(requiredElements);
	}
	
	public WebElement getInputByName(String name){
		Log.logInfo("Getting input field");
		return UiHelper.findElementByXpath("//label[text()='"+name+"']/../following-sibling::div//input");
	}

	public WebElement getTextAreaByName(String name){
		Log.logInfo("Getting input field");
		return UiHelper.findElementByXpath("//label[text()='"+name+"']/../following-sibling::div//textarea");
	}
	
	public WebElement getDropDownByName(String name){
		Log.logInfo("Getting dropdown for: [" + name + "]");
		return UiHelper.findElementByXpath("//label[text()='"+name+"']/../following-sibling::div//button");
	}

	public boolean isSubjectEditable() {
		if (UiHelper.isPresent(new By.ByXPath(isEditableLocator))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSaveButtonDisabled(){
		return UiHelper.findElement(new By.ByXPath("//a[@title='Save']")).getAttribute("class").contains("disabled");
	}

	public void editSubject(String field, String value) {
		Log.logInfo("Changing the " + field + " to " + value);
		switch (field) {
		case SCREENING:
			UiHelper.clear(screening);
			UiHelper.sendKeys(screening, value);
			break;
		case TEMPID:
			UiHelper.clear(tempId);
			UiHelper.sendKeys(tempId, value);
			break;
		case STATUS:
			UiHelper.selectInDropdownBtn(statusDropdown, value);
			if (value.equals("Discontinued")) {
				WebElement textArea = UiHelper.findElement(new By.ByCssSelector("textarea"));
				textArea.clear();
				UiHelper.sendKeys(textArea, "the subject is gone");
			}
			break;
		case LANGUAGE:
			UiHelper.selectInDropdownBtn(languageDropdown, value);
			break;
		}
	}
	
	public boolean isDisplayed() {
		if (getHeader().equalsIgnoreCase("Subject: New")) {
			return true;
		} else {
			return false;
		}
	}
}
