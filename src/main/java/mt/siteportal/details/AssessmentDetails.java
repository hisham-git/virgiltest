package mt.siteportal.details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import mt.siteportal.controls.DayPickerWidget;
import mt.siteportal.controls.TimePickerWidget;
import mt.siteportal.objects.UploadFilesPopUp;
import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.studyDashboard.EsignDialog;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class AssessmentDetails extends BasePage{

	public By notAdministered = new By.ByXPath("//div[@class='checkbox pull-left']//input");
//	public By confirm = new By.ByXPath("//div[@data-message]/a");
	public By confirm = new By.ByXPath("//a[contains(text(), 'Confirm')]");
	private By headline = new By.ByXPath("//div[@id='page-title']/h1");
	public By mtAttachments = new By.ById("attachments");
	public By mtAddAttachment = new By.ByXPath("//a[@title='Upload Attachments']");
	public By notComplete = new By.ByXPath("//div[@class='row double-row-block']//input[@data-ng-model='isNotAdministered']");

	public AssessmentDetails(WebDriver driver) {
	    super(driver);
	   }
	public void waitForAssessmentDetailsPage(){
		UiHelper.waitForVisibility(By.xpath("//div[@id='page-title']/h1"));
		UiHelper.waitForVisibility(By.cssSelector("div.details-grid"));
	}
	public void asNotAdministered(){
		Log.logInfo("Setting assessmentDetails as not administered");
		UiHelper.fluentWaitForElementVisibility(notAdministered, 5).click();
		/*if (UiHelper.isClickable(notAdministered)) {
			UiHelper.click(notAdministered);
//			UiHelper.waitFor(confirm);
		}*/
	}
	public SubjectDetails returnToSubject(String nameSubject){
		Log.logStep("Returning to Subject");
		UiHelper.click(new By.ByXPath("//a[text()='Subject : "+nameSubject+"']"));
		UiHelper.waitFor(new By.ByXPath("//span[contains(text(),'"+nameSubject+"')]"));
		return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}
	public EsignDialog confirmAssesment(){
		Log.logInfo("Confirming assessmentDetails as not administered");
		UiHelper.fluentWaitForElementVisibility(confirm, 5).click();
//		UiHelper.click(confirm);
//		UiHelper.checkPendingRequests(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), EsignDialog.class);
	}

	public By getNotAdminsteredCheckbox() {
		return notAdminsteredCheckbox;
	}
/*------------------------------------------NZ Team----------------------------------------*/

	/**
	 * Returns the Headline Text
	 * @return String Header Text
	 */
	public String getHeader() {
		return UiHelper.getText(headline);
	}
	
	/*
	 * The Locator for the WebElement used to check for the Assessment Details Page's correct loading 
	 */
	private By detailsGrid = By.cssSelector("div.details-grid>div.row");
	/*
	 * Video Container 
	 */
	private By videoContainer = By.cssSelector("div.fp-ui");
	/*
	 * PDF Container 
	 */
	private By pdfContainer = By.cssSelector(".embedPdf");

	/*
	 * The Not Administered checkbox
	 */

//	private By notAdminsteredCheckbox = new By.ByXPath("//input[@data-ng-model='isNotAdministered']");
	private By notAdminsteredCheckbox = new By.ByXPath("//label[not(@disabled='disabled')]/input[@data-ng-model='isNotAdministered']");
	
	
	/*
	 * Spinner locator using By
	 */
	private By bySpinner= By.cssSelector(".spinner");
	
	/**
	 * Assessment status from Image
	 * 
	 */
	private By byAssessmentImgStatus= By.cssSelector("div.assessment-status.ng-binding.ng-scope");
	
	
	@FindBy(css="div.mode-header.grid-header")
	private WebElement gridHeader;
	
	/*
	 * The Edit, Save and Cancel buttons
	 */
	@FindBy(xpath="//a[@title='Save']")
	private WebElement saveButton;

	@FindBy(xpath="//a[@title='Edit']")
	private WebElement editButton;

	@FindBy(xpath="//a[@title='Cancel']")
	private WebElement cancelButton;

	protected By refreshButton = new By.ByXPath("//span[@class='icon-small icon-refresh-b']");

	/*
	 * The DatePicker and TimePicker buttons
	 */
	@FindBy(xpath="//div[@id='datepicker']/a")
	private WebElement datePickerButton;

	@FindBy(css="#timepicker a.datepickerbutton>span.icon-time")
	private WebElement timePickerButton;
	
	/*
	 * Duration Picker Button
	 */
	@FindBy(css="#timepicker a.datepickerbutton>span.icon-duration")
	private WebElement durationPickerButton;

	/*
	 * The Details Grid's rows
	 */
	@FindBy(css=".details-grid div.row")
	private List<WebElement> detailRows;
	
	@FindBy(how = How.CSS, using = "div.view-mode-dropdown>div>button")
	private WebElement viewModeButton;
	
	@FindBy(css="div.right-pane.static")
	private WebElement rightPane;

	private By assessmentImage = new By.ByCssSelector("div.form-cover-big-mask>img");

	@FindBy(css="div.row.assesment-image")
	private WebElement assesmentImageContainer;

	@FindBy(css="div.assessment-version-list")
	protected WebElement versionList;
	
	@FindBy(css=".spinner")
	private List<WebElement> spinner;

	/*
	 * The DatePickerWidget WebElement
	 */
	@FindBy(css=".picker-open .datepicker>div.datepicker-days>table")
	private WebElement datePickerElement;
	
	@FindBy(css=".picker-open .timepicker>div.timepicker-picker>table")
	private WebElement timePickerElement;
	/**
	 * 
	 * @return Assessment Form Name from the Header
	 */
	public String getAssessmentName() {
		UiHelper.waitForVisibility(headline);
		String text = TextHelper.getPartsFromDetails(UiHelper.getText(headline))[1];
		return text;
	}
	public HashMap<String,String> getAssessmentDetailsInfo(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(detailRows.get(0));
		HashMap<String,String> assessDetails = new HashMap<String,String>();
		for(int i=0;i<detailRows.size();i++){	
			String label = this.gridAssessmentItemLabel(detailRows.get(i));
			if(!label.equals("")){
				String value = detailRows.get(i).findElement(By.cssSelector("div.value.col-xs-14")).getText().trim();
				assessDetails.put(label, value);
			}
		}			
		return assessDetails;			
	}
	/**
	 * 
	 * @param element
	 * @return label string if exists
	 */
	public String gridAssessmentItemLabel(WebElement element){
		String itemLabel = "";
		try{
			Browser.getDriver().manage().timeouts().implicitlyWait(80, TimeUnit.MILLISECONDS);
			itemLabel = element.findElement(By.cssSelector("div.caption.col-xs-10>label")).getText().trim();
			Browser.resetTimeOuts();
			return itemLabel;
		}catch(org.openqa.selenium.NoSuchElementException ne){
		}
		return itemLabel;
	}
	/**
	 * 
	 * @param label
	 * @return corresponding value
	 */
	public String getAssessmentDetailsItemValue(String label){
		HashMap<String,String> assessDetails = new HashMap<String,String>();
		assessDetails = this.getAssessmentDetailsInfo();
		return assessDetails.get(label);
	}
	
	/**
	 * Gets the Type of Assessment(ClinRO, ObsRO or PRO) from the header text
	 * 
	 * @return String
	 */
	public String getAssessmentTypeFromHeader(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(headline);
		String [] parts = TextHelper.getPartsFromDetails(UiHelper.getText(headline));
//		System.out.println(Arrays.toString(parts));
		if(parts.length<3){
			Log.logStep("Could not get the Assessment type from header text [" + UiHelper.getText(headline) + "]");
			return null;
		}
		return parts[2];
	}
	
	/**
	 * Checks if the Assessment is "Not Administered" by checking if the check box button is ticked and the Assessment type is ClinRO
	 * 
	 * @return boolean true if "Not Administered"
	 * 				   false if otherwise
	 */
	public boolean isNotAdministered() {
		if (!getAssessmentTypeFromHeader().equals("ClinRO")) {
			Log.logInfo(
					"The Assessment is not of type ClinRO, so there is no option to check for \"Not Administered\"");
			return false;
		}
		return UiHelper.findElement(notAdminsteredCheckbox).isSelected();
	}


	/**
	 * Checks if the Header shows Assessment as the type of Page and if the details grid is present
	 * 
	 * @return boolean
	 */
	public boolean isOpened() {
		UiHelper.checkPendingRequests(driver);
//		UiHelper.waitFor(headline);
		
		if ( UiHelper.isClickable(headline)) { // @author: Hisham 
			String detailsType = TextHelper.getPartsFromDetails(UiHelper.getText(headline))[0];
			return (detailsType.equals("Assessment") && UiHelper.isPresent(By.cssSelector("div.details-grid")));
		}
		return false;
	}

	/**
	 * Returns the WebElement for the Edit Button
	 * 
	 * @return WebElement
	 */
	public WebElement getEditButton() {
		return editButton;
	}

	public WebElement getSaveButton() {
		return saveButton;
	}
	
	public WebElement getCancelButton() {
		return cancelButton;
	}
	
	public WebElement getDatePickerButton() {
		return datePickerButton;
	}
	
	public WebElement getTimePickerButton() {
		return timePickerButton;
	}
	
	public WebElement getDurationPickerButton() {
		return durationPickerButton;
	}


	/**
	 * Clicks on the Edit Button
	 */
	public void clickEditButton() {
		UiHelper.click(getEditButton());
	}

	/**
	 * Checks if the page is in edit mode by checking for the presence and visibility of the Save Button,
	 * the Cancel Button and the Date Time pickers. Also checks if the Edit Button is hidden
	 * 
	 * @return boolean true if the above conditions are met, false otherwise
	 */
	public boolean isInEditMode() {
		final int timeoutSec = 10;
		if (null == UiHelper.fluentWaitForElementClickability(saveButton, timeoutSec)) {
			Log.logInfo("Save Button was not found");
			return false;
		}
		if (null == UiHelper.fluentWaitForElementClickability(cancelButton, timeoutSec)) {
			Log.logInfo("Cancel Button was not found");
			return false;
		}
		if (null == UiHelper.fluentWaitForElementClickability(datePickerButton, timeoutSec)) {
			Log.logInfo("Date Picker was not found");
			return false;
		}
		if (null == UiHelper.fluentWaitForElementClickability(timePickerButton, timeoutSec)) {
			Log.logInfo("Time Picker was not found");
			return false;
		}
		if (null != UiHelper.fluentWaitForElementClickability(editButton, timeoutSec)) {
			Log.logInfo("Edit Button was visible");
			return false;
		}
		return true;
	}

	
	/**
	 * @return HashMap of Assessment Details Grid as (Label,Value)
	 */

	public HashMap<String, String> getAllDetails(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(detailRows.get(0));
		HashMap<String, String> details = new HashMap<String, String>();
		for(WebElement row : detailRows){
			try{
				WebElement label = row.findElement(By.cssSelector("div.caption"));
				WebElement value = row.findElement(By.cssSelector("div.value"));
				details.put(label.getText(), value.getText());
			}catch(NoSuchElementException ne){
				continue;
			}
		}
		return details;
	}
	/**	
	 * Uses the Date Picker Widget from the "Started" input and changes it to
	 * the input as specified
	 * 
	 * @param date String, ranges from 01 to 31, also depending on the month
	 * @param month String, ranges from JAN to DEC
	 * @param year String, of the format YYYY
	 */
	public void setStartedDate(String date, String month, String year) {
//		if (!UiHelper.isPresent(datePickerElement))
		if ( null != UiHelper.fluentWaitForElementVisibility(datePickerButton, 5))
			UiHelper.click(datePickerButton);
		DayPickerWidget datePicker = new DayPickerWidget(datePickerElement);
		datePicker.setYear(year);
		datePicker.setMonth(month);
		datePicker.setDate(date);
	}

	/**
	 * Uses the Time Picker Widget from the "Started" input and changes it to
	 * the input as specified
	 * 
	 * @param hours String, ranging from 01 to 12
	 * @param minutes String, ranging from 00 to 59
	 * @param amOrPm String, can either be AM or PM
	 */
	public void setStartedTime(String hours, String minutes, String amOrPm) {
//		if(!UiHelper.isPresent(timePickerElement))
		if ( null != UiHelper.fluentWaitForElementVisibility(timePickerElement, 5)) {
//			UiHelper.click(timePickerButton);
			TimePickerWidget timePicker = new TimePickerWidget(timePickerElement);
			timePicker.setHour(hours);
			timePicker.setMinutes(minutes);
			timePicker.setAmOrPm(amOrPm);
			UiHelper.click(timePickerButton);
		}
	}

	/**
	 * Uses the Time Picker Widget from the "Duration" input and changes it to
	 * the input as specified
	 *
	 * @param hours String, ranging from 00 to 23
	 * @param minutes String, ranging from 00 to 59
	 */
	public void setDuration(String hours, String minutes) {
//		if(!UiHelper.isPresent(timePickerElement))
		if ( null != UiHelper.fluentWaitForElementVisibility(durationPickerButton, 5))
			UiHelper.click(durationPickerButton);
		TimePickerWidget timePicker = new TimePickerWidget(timePickerElement);
		timePicker.setHour(hours);
		timePicker.setMinutes(minutes);
		UiHelper.click(durationPickerButton);
	}

	/**
	 * Clicks the Save Button
	 */
	public void clickSaveButton() {
		UiHelper.click(saveButton);
	}

	/**
	 * Clicks the Cancel Button
	 */
	public void clickCancelButton() {
		UiHelper.click(cancelButton);
	}
	
	/**
	 * 
	 * @return  View Mode text
	 */
	public String getViewMode(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(viewModeButton);
		String viewModeStr = viewModeButton.findElement(By.cssSelector("span.mode")).getText().trim();
		return viewModeStr;
	}

	/**
	 * 
	 * @param paramMode
	 * @return
	 */
	public boolean verifyViewMode(String paramMode){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(viewModeButton);
		String viewModeUI = this.getViewMode();
		if(!paramMode.equals(viewModeUI)){
			Log.logInfo("The View Mode Button Text and the Expected View Mode do not match. Expected [" + paramMode + "] but got [" + viewModeUI + "]");
			return false;
		}
		switch (paramMode) {
			case "All Details":
				if (!UiHelper.isPresentAndVisible(detailsGrid)){
					Log.logInfo("Could not find the Details Grid.");
					return false;
				}
				return true;
			case "Video + Source":
				if (!UiHelper.isPresentAndVisible(videoContainer)){
					Log.logInfo("Could not find the Video Container.");
					return false;
				}
				if (!UiHelper.isPresentAndVisible(pdfContainer)){
					Log.logInfo("Could not find the PDF Container.");
					return false;
				}
				return true;
			case "Video + Scores":
				if (!UiHelper.isPresentAndVisible(videoContainer)){
					Log.logInfo("Could not find the Video Container.");
					return false;
				}
				if (!UiHelper.isPresentAndVisible(scoresTab)){
					Log.logInfo("Could not find the Scores Tab.");
					return false;
				}
				return true;
			case "Source + Scores":
				if (!UiHelper.isPresentAndVisible(pdfContainer)){
					Log.logInfo("Could not find the Video Container.");
					return false;
				}
				if (!UiHelper.isPresentAndVisible(scoresTab)){
					Log.logInfo("Could not find the Scores Tab.");
					return false;
				}
				return true;
			default:
				Log.logInfo("No suuch mode found.");
				return false;
			}
	}

	
	/**
	 * List of View Modes
	 */
	@FindBy(how=How.CSS, using="div.view-mode-dropdown>div>ul>li")
	private List<WebElement> viewModeList;
	
	/**
	 * Clicks the View Mode Button and returns a List of Strings form the View Mode Selection Dropdown
	 * @return List<String> containing the Text from the View Mode Dropdown
	 */
	public List<String> getAllViewModes(){
		UiHelper.click(viewModeButton);
		List<String> texts = TextHelper.getElementTextContent(viewModeList);
		UiHelper.click(viewModeButton);
		return texts;
	}

	/**
	 * Returns the value of the Source Document Grid value
	 *
	 * @return String
	 */
	public String getSourceDocumentFileName() {
		WebElement row = getDetailsGridRowWebElementFor("Source Document");
		WebElement value = row.findElement(By.xpath(".//div[contains(@class, 'value')]"));
		return value.getText();
	}
	
	/**
	 * @author Hisham
	 * 
	 * Returns the WebElement for Source Document Grid
	 *
	 * @return WebElement
	 */
	public WebElement getSourceDocumentElement() {
		WebElement row = getDetailsGridRowWebElementFor("Source Document");
		WebElement value = row.findElement(By.xpath(".//div[contains(@class, 'value')]"));
		return value;
	}

	/*
	 * THe view mode Cancel button
	 */
	@FindBy(css = ".mode-header button[title='Cancel']")
	private WebElement closeViewMode;	
	
	/**
	 * Click the Close Button on the Header for View Modes other than
	 * All Details
	 */
	public void closeViewMode() {
		UiHelper.click(closeViewMode);
	}
	
	/**
	 * Selects the View Mode that matches with the parameter
	 * @param viewMode
	 * @return True if selected
	 */
	public boolean selectViewMode(String viewMode){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(viewModeButton);
		Log.logStep("Selecting View Mode : " + viewMode +"...");
		String currentMode = this.getViewMode();
		if( currentMode.equals(viewMode) ){
			Log.logInfo("View Mode:"+viewMode + " is already selected in the menu.");
			return true;
		}
		UiHelper.click(viewModeButton);
		for(WebElement mode : viewModeList){
			if( mode.getText().trim().equals(viewMode)) {
				UiHelper.click(mode);
				UiHelper.waitForSpinnerEnd(spinner.get(0), 50);
				Log.logInfo("View Mode :" + viewMode +" is selected");				
				return true;
			}
		}		
		UiHelper.click(viewModeButton);
		Log.logInfo("Could not find the Mode " + viewMode + " in dropdown list.");
		return false;
	}
 
	/**
	 * @author Hisham
	 * 
	 * @return WebElement 
	 * 					- Assessment pdf form element
	 */
	public WebElement getAssessmentPdfFormElement() {
		return assesmentImageContainer;
	}
	
	/**
	 * 
	 * @return Assessment Status on the PDF image
	 */
	public String getAssessmentStatus(){
		UiHelper.waitForVisibility(assessmentImage);
//		String status = UiHelper.getText(new By.ByCssSelector("div.assessment-status"));
//		String status = assesmentImageContainer.findElement(By.cssSelector("div.assessment-status")).getText().trim();	
		if (UiHelper.isClickable(byAssessmentImgStatus)) {
			String status = UiHelper.getText(byAssessmentImgStatus).trim();
			return status; // @author: Hisham
		}
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * @return String status
	 */
	public String getStatusFromThumbnail() {
		By statusLocator = new By.ByCssSelector("div.form-cover > div.assessment-status");
		String statusFound = UiHelper.fluentWaitForElementVisibility(statusLocator, 10).getText().trim();
		if (null == statusFound || statusFound.equalsIgnoreCase(""))
			return null;
		return statusFound;
	}
	
	/**
	 * 
	 * @return Assessment Language on the PDF image
	 */
	public String getAssessmentLanguage(){
		UiHelper.waitForVisibility(assessmentImage);
		String language = assesmentImageContainer.findElement(By.cssSelector("div.ribbon-side.ng-binding")).getText().trim();
		return language;
	}
	
	/**
	 * Right-pane tabs (tabName, WebElement)
	 */
	private HashMap<String,WebElement> tabInfo;
	
	/**
	 * Sets the right-pane tabs
	 */
	public void setRightPaneTabs(){
		UiHelper.waitForVisibility(rightPane);
		tabInfo = new HashMap<String,WebElement>();
		List<WebElement> tabList = rightPane.findElements(By.cssSelector("ul.nav-tabs>li>a"));
		for(int i=0;i<tabList.size();i++){	
			String tabName = tabList.get(i).getText().trim();
			tabInfo.put(tabName, tabList.get(i));
		}			
	}
	/**
	 * Gets the Tabs HashMap
	 * @return
	 */
	public HashMap<String,WebElement> getRightPaneTabs(){
		return tabInfo;
	}
	
	/**
	 * clicks on the Right-pane tab that matches with the parameter
	 * @param tabName
	 */
	public void clickOnRightPaneTab(String tabName){
		this.setRightPaneTabs();
		HashMap<String,WebElement> tabs = this.getRightPaneTabs();
		WebElement element = tabs.get(tabName);
		UiHelper.click(element);
	}
	
	/**
	 * 
	 * @return the HashMap of ScoreItem,Score pair
	 */
	public HashMap<String, String> getAssessmentScores(){
		UiHelper.waitForVisibility(rightPane);
		HashMap<String, String> scores = new HashMap<String, String>();
		List<WebElement> rows = rightPane.findElements(By.cssSelector(".caption.col-xs-24.ng-scope"));
		for(WebElement row : rows){
			try{
				WebElement label = row.findElement(By.cssSelector("div.col-xs-9"));
				WebElement value = row.findElement(By.cssSelector("div.col-xs-6"));
				scores.put(label.getText(), value.getText());
			}catch(NoSuchElementException ne){
				continue;
			}
		}
		return scores;
	}
	
	/**
	 * 
	 * @param label of Form ScoreItem
	 * @return corresponding value
	 */
	public String getAssessmentScoreValue(String label){
		String value = "";
		HashMap<String,String> assessScores = this.getAssessmentScores();
		value = assessScores.get(label);
		return value;
	}
	/**
	 * clicks on the Changes checkbox on Right-Pane
	 */
	public void checkChanges(){
		WebElement checkBox = rightPane.findElement(By.cssSelector(".ng-valid.ng-dirty"));
		UiHelper.click(checkBox);
	}
	/**
	 * clicks on the Overrides checkbox on Right-Pane
	 */
	public void checkOverrides(){
		WebElement checkBox = rightPane.findElement(By.cssSelector(".ng-valid.ng-dirty"));
		UiHelper.click(checkBox);
	}
	
	/**
	 * 
	 * @return Grid-details HashMap(label,hyperLink) w/t hyperlinks only
	 */
	public HashMap<String, WebElement> getAssessmentLinks(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(detailRows.get(0));
		HashMap<String, WebElement> links = new HashMap<String, WebElement>();
		for(WebElement row : detailRows){
			try{
				Browser.getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				WebElement label = row.findElement(By.cssSelector("div.caption"));
				WebElement link = row.findElement(By.cssSelector("a.a-color.ng-binding"));
				Browser.resetTimeOuts();
				if(link!=null)					
					links.put(label.getText(), link);
			}catch(NoSuchElementException ne){
				continue;
			}
		}
		return links;
	}

	/**
	 * clicks on the corresponding Link of the label on Assessment Grid Details 
	 * @param label
	 */
	public void clickOnGridLink(String label){
		HashMap<String,WebElement> tabs = this.getAssessmentLinks();
		WebElement element = tabs.get(label);
		UiHelper.click(element);
	}
	/**
	 * clicks on the Form PDF image thumbnail
	 */
	public void clickOnFormPDF(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(assessmentImage);
		UiHelper.click(assesmentImageContainer);
		UiHelper.waitForSpinnerEnd(spinner.get(0), 50);
	}

	/**
	 * Clicks  on Refresh button
	 */
	public void clickRefresh(){
		UiHelper.click(refreshButton);
	}
	
	/**
	 * checks the Refresh button functionality
	 */
	/*public AssessmentDetails checkRefresh(){
		Log.logStep("Clicking the Refresh button...");
		Long calls1 = UiHelper.getNumberOfOpenAjaxConnections(driver);
//		System.out.println(calls1);
		UiHelper.click(refreshButton);
		Long calls2 = UiHelper.getNumberOfOpenAjaxConnections(driver);
//		System.out.println(calls2);
		Log.logInfo("Refresh button is clicked.");
		if(calls1 == calls2){
			Log.logInfo("Refresh button is not working.");
			return null;			
		}
		if(!UiHelper.checkSpinner(driver)){
			Log.logInfo("Refresh button is not working.");
			return null;
		}
		UiHelper.waitForSpinnerEnd(spinner.get(0), 50);
		return PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
	}*/
	
	/**
	 * @author Hisham
	 * checks the Refresh button functionality
	 */
	public AssessmentDetails checkRefresh() {
		Log.logStep("Clicking the Refresh button...");
		UiHelper.click(refreshButton);
		UiHelper.checkPendingRequests(driver);
		Log.logInfo("Refresh button is clicked.");
		UiHelper.explicitWaitForSpinnerEnd(bySpinner, 30);
		if (UiHelper.isClickable(gridHeader) == true) {
			return PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		} else {
			return null;
		}
	}

	/**
	 * clicks on the Cancel button to cancel the PDF/Video/Score view mode
	 */
	public void clickCancelModeButton(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(gridHeader);
		WebElement element = gridHeader.findElement(By.cssSelector("div>button.btn.circle-button.btn-white"));
		UiHelper.click(element);
	}

	/**
	 * 
	 * @return Grid-Header-details HashMap(label,hyperLink)
	 */

	public HashMap<String, WebElement> getGridHeaderDetails(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(gridHeader);
		HashMap<String, WebElement> headerDetails = new HashMap<String, WebElement>();
		List<WebElement> labelList = gridHeader.findElements(By.cssSelector("div.caption"));
		List<WebElement> valueList = gridHeader.findElements(By.cssSelector("div.value>label>a"));		
		for(int i=0;i<labelList.size();i++){	
			try{
				headerDetails.put(labelList.get(i).getText(), valueList.get(i));		
			}catch(NoSuchElementException ne){
				continue;
			}
		}
		return headerDetails;
	}
	
	/**
	 * Clicks on the respective Grid-Header link
	 * @param label 
	 */
	public void clickOnGridHeaderLink(String label){
		HashMap<String,WebElement> tabs = this.getGridHeaderDetails();
		WebElement element = tabs.get(label);
		try{
			WebDriverWait wait = new WebDriverWait(Browser.getDriver(),10);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			UiHelper.click(element);			
			}catch(NoSuchElementException ne){
				Log.logInfo("No such link found on Header Grid.");
			}
		}

	/**
	 * returns on the respective Grid-Header value
	 * @param label 
	 * @return corresponding value of the Header label
	 */
	public String getGridHeaderValue(String label){
		UiHelper.checkSpinner(driver);
		HashMap<String,WebElement> tabs = this.getGridHeaderDetails();
		WebElement element = tabs.get(label);
		String value = element.getText().trim();
		return value;
	}

	/**
	 * Gets the WebElement Row for a particular label data
	 *
	 * @param gridLabel
	 *            - String - the name of the label for which the WebElement is
	 *            needed
	 * @return WebElement - row element of the specified grid
	 */
	public WebElement getDetailsGridRowWebElementFor(String gridLabel) {
		By rowLocator = By
				.xpath(".//div[contains(@class, 'details-grid')]/div[contains(@class,'row')]/div[contains(@class,'caption')]/label[text()='"
						+ gridLabel + "']/../..");
		try {
			WebElement row = driver.findElement(rowLocator);
			return row;
		} catch (NoSuchElementException nse) {
			return null;
		} 
	}

	/**
	 * Gets the Rater's selection dropdown button
	 *
	 * @return WebElement - the button
	 */
	public WebElement getRaterDropdownButton() {
		WebElement gridRow = getDetailsGridRowWebElementFor("Rater");
		return gridRow.findElement(By.xpath(".//button[contains(@class, 'dropdown-toggle')]"));
	}

	/**
	 * Gets the Observer's selection dropdown button
	 *
	 * @return WebElement - the button
	 */
	public WebElement getObserverDropdownButton() {
		WebElement gridRow = getDetailsGridRowWebElementFor("Observer");
		return gridRow.findElement(By.xpath(".//button[contains(@class, 'dropdown-toggle')]"));
	}

	/**
	 * Gets the Rater's Dropdown list element
	 *
	 * @return WebElement - Dropdown List element
	 */
	public WebElement getRaterDropdownList() {
		WebElement gridRow = getDetailsGridRowWebElementFor("Rater");
		return gridRow.findElement(By.xpath(".//ul[contains(@class, 'dropdown-menu')]"));
	}

	/**
	 * Gets the Observer's Dropdown list element
	 *
	 * @return WebElement - Dropdown List element
	 */
	public WebElement getObserverDropdownList() {
		WebElement gridRow = getDetailsGridRowWebElementFor("Observer");
		return gridRow.findElement(By.xpath(".//ul[contains(@class, 'dropdown-menu')]"));
	}

	/**
	 * Select the Rater from the Rater's dropdown selection menu
	 *
	 * @param rater
	 *            - String - name of the rater to select
	 */
	public void chooseRater(String rater) {
		WebElement dropdownList = getRaterDropdownList();
		if(!dropdownList.isDisplayed())
			UiHelper.click(getRaterDropdownButton());
		List<WebElement> listItems = dropdownList.findElements(By.xpath("li"));
		for(WebElement element : listItems){
			if(element.getText().equals(rater)){
				UiHelper.click(element);
				return;
			}
		}
		Log.logStep("Could not find Rater called \"" + rater + "\" in the Raters Dropdown...");
		UiHelper.click(dropdownList);
	}

	/**
	 * Selects a random rater from the dropdown list of raters in the form edit
	 * section
	 *
	 * @return String - the name of the rater chosen randomly
	 */
	public String chooseRandomRater() {
		WebElement dropdownList = getRaterDropdownList();
		if (!dropdownList.isDisplayed())
			UiHelper.click(getRaterDropdownButton());
		List<WebElement> listItems = dropdownList.findElements(By.xpath("li"));
		WebElement randomlyChosenRater = UiHelper.getRandomObjectFromList(listItems);
		String raterName = randomlyChosenRater.getText();
		UiHelper.click(randomlyChosenRater);
		return raterName;
	}

	/**
	 * Selects a random rater from the dropdown list of raters in the form edit
	 * section
	 *
	 * @return String - the name of the rater randomly chosen
	 */
	public String chooseRandomObserver() {
		WebElement dropdownList = getObserverDropdownList();
		if (!dropdownList.isDisplayed())
			UiHelper.click(getObserverDropdownButton());
		List<WebElement> listItems = dropdownList.findElements(By.xpath("li"));
		WebElement randomlyChosenRater = UiHelper.getRandomObjectFromList(listItems);
		String raterName = randomlyChosenRater.getText();
		UiHelper.click(randomlyChosenRater);
		return raterName;
	}

	/**
	 * Returns the Upload Source Document Button
	 *
	 * @return WebElement - Upload Source Document Button
	 */
	public WebElement getUploadSourceDocumentButton() {
		WebElement sourceDocumentRow = getDetailsGridRowWebElementFor("Source Document");
		return sourceDocumentRow.findElement(By.xpath(".//a[@title='Upload Source Document']"));
	}

	/**
	 * Clicks the Upload Source Document Button
	 *
	 * @return UploadFilesPopUp object
	 */
	public UploadFilesPopUp clickUploadSourceDocumentButton() {
		UiHelper.click(getUploadSourceDocumentButton());
		return new UploadFilesPopUp(driver.findElement(By.id("sourceDocument")));
	}

	/**
	 * @param type
	 * @param study
	 * @param site
	 * @param subject
	 * @param form
	 * @param visit
	 * @param rater
	 * @param version
	 * @param status
	 * @param completionDate
	 * @return True-if all matched
	 *  
	 */
	//TODO: Get the hashMap populated once and then retrieve the data
	public boolean verifyGridData(String type, String study, String site, String subject, String form, String visit,
			String rater, String version, String status) {
		HashMap<String, String> assessDetails = new HashMap<String, String>();
		assessDetails = this.getAssessmentDetailsInfo();
		Log.logStep("Verifying the Assessment Grid data ...");
		String typeFromGrid = this.getAssessmentTypeFromHeader();
		
		if(type.equals(typeFromGrid))
			Log.logInfo("Assessment type name matched. From Grid:"+ typeFromGrid +",From Assessment List:"+ type);
		else{
			Log.logInfo("Assessment type didn't match. From Grid:"+ typeFromGrid +",From Assessment List:"+ type);
			return false;
		}
		String studyFromGrid = assessDetails.get("Study");
		if(studyFromGrid.contains(study))
			Log.logInfo("Study name matched. From Grid:"+ studyFromGrid +",From Assessment List:"+ study);
		else{
			Log.logInfo("Study name didn't match. From Grid:"+ studyFromGrid +",From Assessment List:"+ study);
			return false;
		}
		String siteFromGrid = assessDetails.get("Site");
		if(siteFromGrid.contains(site))
			Log.logInfo("Site name matched. From Grid:"+ siteFromGrid +",From Assessment List:"+ site);
		else{
			Log.logInfo("Site name didn't match. From Grid:"+ siteFromGrid +",From Assessment List:"+ site);
			return false;
		}
		String formFromGrid = assessDetails.get("Assessment");
		if(form.equalsIgnoreCase(formFromGrid))
			Log.logInfo("Form name matched. From Grid:"+ formFromGrid +",From Assessment List:"+ form);
		else{
			Log.logInfo("Form name didn't match. From Grid:"+ formFromGrid +",From Assessment List:"+ form);
			return false;
		}
		String visitFromGrid = assessDetails.get("Visit");
		if(visit.equalsIgnoreCase(visitFromGrid))
			Log.logInfo("Visit name matched. From Grid:"+ visitFromGrid +",From Assessment List:"+ visit);
		else{
			Log.logInfo("Visit name didn't match. From Grid:"+ visitFromGrid +",From Assessment List:"+ visit);
			return false;
		}
		
		if (type.equalsIgnoreCase("ClinRO")) {
			String raterFromGrid = assessDetails.get("Rater");
			if (!rater.equals("")) {
				if (rater.equalsIgnoreCase(raterFromGrid))
					Log.logInfo("Rater's name matched. From Grid:" + raterFromGrid + ",From Assessment List:" + rater);
				else {
					Log.logInfo(
							"Rater's name didn't match. From Grid:" + raterFromGrid + ",From Assessment List:" + rater);
					return false;
				}
			}
		} else if (type.equalsIgnoreCase("ObsRO")) {
			String observerFromGrid = assessDetails.get("Observer");
			if (!rater.equals("")) {
				if (rater.equalsIgnoreCase(observerFromGrid))
					Log.logInfo(
							"Rater's name matched. From Grid:" + observerFromGrid + ",From Assessment List:" + rater);
				else {
					Log.logInfo("Rater's name didn't match. From Grid:" + observerFromGrid + ",From Assessment List:"
							+ rater);
					return false;
				}
			}
		}
		
		String statusFromGrid = this.getAssessmentStatus();
		if(status.equalsIgnoreCase(statusFromGrid))
			Log.logInfo("Status matched. From Grid:"+ statusFromGrid +",From Assessment List:"+ status);
		else{
			Log.logInfo("Status didn't match. From Grid:"+ statusFromGrid +",From Assessment List:"+ status);
			return false;
		}
		Log.logInfo("Assessment Grid data matched between Details Grid and Assessment List.");			
		return true;
	}

	/**
	 * 
	 * @return the hashtable of (form version,date) pair
	 */
	public HashMap<String, String> getVersionTableDates() {
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(versionList);
		HashMap<String, String> versionDates = new HashMap<String, String>();
		List<WebElement> list = versionList.findElements(By.cssSelector(".grid-row.row"));
		for (WebElement row : list) {
			try {
				String version = row.findElement(By.cssSelector("div.col-md-2.col-xs-2")).getText();
				String date = row.findElement(By.cssSelector("div.col-md-6.col-xs-8")).getText();
				// System.out.println(version + " " + date);
				versionDates.put(version, date);
			} catch (NoSuchElementException ne) {
				continue;
			}
		}
		return versionDates;
	}

	/**
	 * 
	 * @return the hashtable of (form version,Raters) pair
	 */
	public HashMap<String, String> getVersionTableRaters() {
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(versionList);
		HashMap<String, String> versionRaters = new HashMap<String, String>();
		List<WebElement> list = versionList.findElements(By.cssSelector(".grid-row.row"));
		for (WebElement row : list) {
			try {
				String version = row.findElement(By.cssSelector("div.col-md-2.col-xs-2")).getText();
				String rater = row.findElement(By.cssSelector("div.col-md-14.col-xs-12")).getText();
				// System.out.println(version + " " + rater);
				versionRaters.put(version, rater);
			} catch (NoSuchElementException ne) {
				continue;
			}
		}
		return versionRaters;
	}

	/**
	 * Returns version table date
	 * @param version
	 * @return Date
	 */
	public String getVersionTableDate(String version){
		HashMap<String,String> dates = this.getVersionTableDates();
		String date = dates.get(version);
		return date;
	}
	/**
	 * Returns version  table rater
	 * @param version
	 * @return Rater
	 */
	public String getVersionTableRater(String version){
		HashMap<String,String> raters = this.getVersionTableRaters();
		String rater = raters.get(version);
		return rater;
	}
	
	/**
	 * 
	 * @return Grid-details Checkboxes(label,WebElement)
	 */

	public HashMap<String, WebElement> getGridDetailsCheckboxes(){
		UiHelper.checkPendingRequests(driver);
		UiHelper.waitForVisibility(detailRows.get(0));
		HashMap<String, WebElement> checkBoxes = new HashMap<String, WebElement>();
		for(WebElement row : detailRows){
			try{
				Browser.getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			/*	String label = row.findElement(By.cssSelector(".ng-scope>div.caption")).getText().trim();
				WebElement element = row.findElement(By.cssSelector(".ng-scope>div.value>input"));*/
				String label = row
						.findElement(By.cssSelector(".double-row-block.ng-scope>.checkbox.pull-left:not(.ng-hide)>label"))
						.getText().trim();
				WebElement element = row
						.findElement(By.cssSelector(".double-row-block.ng-scope>.checkbox.pull-left:not(.ng-hide)>label>input"));
				Browser.resetTimeOuts();
				if(element!=null)					
					checkBoxes.put(label, element);
			}catch(NoSuchElementException ne){
				continue;
			}
		}
		return checkBoxes;
	}

	/**
	 * Checks if the Assessment is "Paper Transcription" by checking if the check box button is ticked
	 * 
	 * @return boolean true if "Paper Transcription"
	 * 				   false if otherwise
	 */
	public boolean isPaperTranscription() {
		HashMap<String, WebElement> checkBoxes = this.getGridDetailsCheckboxes();
		WebElement element = checkBoxes.get("Paper Transcription");
		if (element == null)
			throw new SkipException("No checkbox named Paper Transcription found in the list.");
		return element.isSelected();
	}
	
	/*
	 * Attachments Tab and its controls----------------------------------------------------------------------------------
	 */
	private final By attachmentsTabLink = By.cssSelector("a[href='#attachments']");
	
	/**
	 * Checks if the Attachments Tab Link exists and is visible
	 * @return boolean - true if exists and is visible, false otherwise
	 */
	public boolean isAttachmentsTabLinkPresent(){
		return UiHelper.isPresentAndVisible(attachmentsTabLink);
	}
	
	/**
	 * Clicks on the Attachments Tab Link
	 * @return AttachementsTab PageObject
	 */
	public AttachmentsTab clickAttachmentsTabLink(){
		UiHelper.waitForVisibility(attachmentsTabLink);
		UiHelper.click(UiHelper.findElement(attachmentsTabLink));
		return PageFactory.initElements(driver, AttachmentsTab.class);
	}
	
	
	/*
	 * END Section-----------------------------------------------------------------------------------------------------------
	 */

	/*
	 * This section contains functions to access the Versions List-----------------------------------------------------------
	 */
	/**
	 * Locators
	 */
	private By versionTableHeaderRow = By.cssSelector(".assessment-version-list div.portal-grid>div.grid-header.row"),
			versionTableRows = By.cssSelector(".assessment-version-list div.portal-grid>div.grid-row.row");
	/**
	 * CONSTANTS that contain xpaths indexes
	 */
	private final String VERSION_TABLE_VERSION_COLUMN_INDEX = "1", VERSION_TABLE_DATECOMPLETED_COLUMN_INDEX = "2",
			VERSION_TABLE_RATERS_COLUMN_INDEX = "3";

	/**
	 * Function that returns a list of Rater Names as String
	 * @return List<String> of names
	 */
	public List<String> getRaterNamesFromVersionList() {
		return getValuesFromVersionTableColumn(VERSION_TABLE_RATERS_COLUMN_INDEX);
	}
	
	/**
	 * Function that returns a list of Versions as String
	 * @return List<String> of versions
	 */
	public List<String> getVersionsFromVersionList() {
		return getValuesFromVersionTableColumn(VERSION_TABLE_VERSION_COLUMN_INDEX);
	}

	/**
	 * Function that returns a list of Completion Dates as String
	 * @return List<String> of completed dates
	 */
	public List<String> getCompletionDatesFromVersionList() {
		return getValuesFromVersionTableColumn(VERSION_TABLE_DATECOMPLETED_COLUMN_INDEX);
	}

	/**
	 * Returns a list of the Versions Table Headers as String
	 * @return List<String> of Header Names
	 */
	public List<String> getHeaderColumnsFromVersionList() {
		if(!UiHelper.isPresentAndVisible(versionTableHeaderRow)){
			Log.logInfo("While trying to retrieve data from the Versions Table Headers, no rows could be found. Returning an empty list of data...");
			return new ArrayList<String>();
		}
		return TextHelper.getElementTextContent(driver.findElements(versionTableHeaderRow));
	}

	/**
	 * Returns a List of WebElements from the Table of Rows
	 * @return List<WebElement> of version table rows
	 */
	public List<WebElement> getVersionTableRows() {
		if(!UiHelper.isPresentAndVisible(versionTableRows)){
			Log.logInfo("While trying to retrieve data from the Versions Table no rows could be found. Returning an empty list of data...");
			return new ArrayList<WebElement>();
		}
		return driver.findElements(versionTableRows);
	}

	/**
	 * Helper function for the table columns to get the values of the appropriate columns
	 * @return List<String> of the appropriate column values
	 */
	private List<String> getValuesFromVersionTableColumn(String index) {
		List<WebElement> rows = getVersionTableRows();
		List<String> texts = new ArrayList<String>();
		for (WebElement row : rows) {
			texts.add(row.findElement(By.xpath("div[" + index + "]")).getText());
		}
		return texts;
	}
	
	/**
	 * Returns an array list of Strings containing the data for a particular row in the Visits Table
	 * The list has three Strings. Index 0 gives the version number, Index 1 gives the Completion Date and index 2 gives the Rater's name.
	 *  
	 * @param rowIndex int, the row to get the data from
	 * @return List<String> of the data, has only three Strings
	 */
	public List<String> getDataForRow(int rowIndex){
		List<WebElement> rows = getVersionTableRows();
		List<String> texts = new ArrayList<String>();
		try{
			WebElement row = rows.get(rowIndex);
			texts.add(row.findElement(By.xpath("div[" + VERSION_TABLE_VERSION_COLUMN_INDEX + "]")).getText());
			texts.add(row.findElement(By.xpath("div[" + VERSION_TABLE_DATECOMPLETED_COLUMN_INDEX + "]")).getText());
			texts.add(row.findElement(By.xpath("div[" + VERSION_TABLE_RATERS_COLUMN_INDEX + "]")).getText());
		}catch(IndexOutOfBoundsException ie){
			Log.logInfo("Could not locate data for row [" + rowIndex + "] for the Version Table. Returning empty array...");
			return new ArrayList<String>();
		}catch(NoSuchElementException ne){
			Log.logInfo("Could not locate an element from the Versions Table. Returning empty array...");
			return new ArrayList<String>();
		}catch(NullPointerException ne){
			Log.logInfo("Could not find the row from the Versions Table. Returning an empty set of data...");
			return new ArrayList<String>();
		}
		return texts;
	}
	/*
	 * Section ENDS---------------------------------------------------------------------------------------------------
	 */

	/*
	 * SECTION START--------------------------------------------------------------------------------------------------
	 * Contains functions that deal with the Scores Tab Page Elements
	 */
	private final By scoresTabLink = By.cssSelector("a[href='#scores']"), scoresTab = By.id("scores");
	/**
	 * Click scores tab link on the Assessment Page
	 * 
	 * @return ScoresTab PageObject
	 */
	public ScoresTab clickScoresTabLink(){
		UiHelper.waitForVisibility(scoresTabLink);
		UiHelper.click(UiHelper.findElement(scoresTabLink));
		return PageFactory.initElements(driver, ScoresTab.class);
	}
	/*
	 * SECTION END----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * SECTION START--------------------------------------------------------------------------------------------------
	 * Functions that check for the correct elements for a given type of Assessment
	 */
	/**
	 * Checks if the header is of the format Assessments: {NAME} (PRO)
	 * 
	 * @return boolean if matches, false otherwise
	 */
	public boolean checkHeader(String type) {
		String header = getHeader();
		return header.matches("Assessment:\\s\\S+\\s?\\-?\\s?\\S+\\s?\\(" + type + "\\)");
	}

	/**
	 * Checks if the details grid have links for the appropriate fields
	 * 
	 * @return true if Subject, Visit and Study are linked, false otherwise
	 */
	public boolean checkDetailsGrid(String type) {
		HashMap<String, String> map = getAllDetails();
		if (!(map.containsKey("Subject") && map.containsKey("Site") && map.containsKey("Study")
				&& map.containsKey("Visit") && map.containsKey("Assessment") && map.containsKey("Started")
				&& map.containsKey("Duration"))) {
			Log.logInfo(
					"One or more of the Details Fields were not present. The Fields were " + map.keySet().toString());
			return false;
		}
		if ("ObsRO".equals(type)) {
			if (!map.containsKey("Observer")) {
				Log.logInfo("One or more of the Details Fields were not present. The Fields were "
						+ map.keySet().toString());
				return false;
			}
		}
		HashMap<String, WebElement> linksMap = getAssessmentLinks();
		if ("ClinRO".equals(type)) {
			if (!map.containsKey("Rater")) {
				Log.logInfo("One or more of the Details Fields were not present. The Fields were "
						+ map.keySet().toString());
				return false;
			}
			if (!(linksMap.containsKey("Rater"))) {
				Log.logInfo(
						"The Rater field dosen't have a link. The Linked Fields were " + linksMap.keySet().toString());
				return false;
			}
		}
		if (!(linksMap.containsKey("Subject") && linksMap.containsKey("Study") && linksMap.containsKey("Visit"))) {
			Log.logInfo("One or more of the Study, Visit and Subject fields don't have links. The Linked Fields were "
					+ linksMap.keySet().toString());
			return false;
		}
		return true;
	}

	/**
	 * Checks if the Raters from the Versions Table are correct depending on the
	 * type of Assessment
	 * 
	 * @return boolean, false if even one Rater contains an incorrect Name format else
	 *         true
	 */
	public boolean checkVersionsTable(String type) {
		List<String> raters = getRaterNamesFromVersionList();
		List<String> completionDates = getCompletionDatesFromVersionList();
		for (int i = 0; i < completionDates.size(); i++) {
			String date = completionDates.get(i);
			if (date == null || date.equals("")) {
				Log.logInfo("The date was empty for the Versions Table.");
				return false;
			}
			String rater = raters.get(i);
			switch (type) {
			case "ClinRO":
				if (rater == null || rater.equals("")) {
					Log.logInfo("The rater field was empty for the Versions Table.");
					return false;
				}
				break;
			case "ObsRO":
				if (rater == null || rater.equals("")) {
					Log.logInfo("The rater field was empty for the Versions Table.");
					return false;
				}
				/*if (!rater.matches("\\S+.+ \\(\\S+.+\\)")) {
					Log.logInfo("The Observer name should be of the format [{NAME} ({RELATIONSHIP})] but was [" + rater
							+ "]");
					return false;
				}*/
				break;
			case "PRO":
				if (rater == null || rater.equals("")) {
					Log.logInfo("The rater field was empty for the Versions Table.");
					return false;
				}
				/*if (!rater.equals("-")) {
					Log.logInfo("The PRO name should be of the format [-] but was [" + rater + "]");
					return false;
				}*/
				break;
			default:
				Log.logInfo("The Type of Assessment was not recognized : " + type);
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the Scores Tab contains Changes and Overrides/Skipped Questions
	 * checkboxes only
	 * 
	 * @return boolean true if condition is met, false otherwise
	 */
	public boolean checkScoresTab(String type) {
		String checkboxShouldBe = (type.equals("ClinRO") ? "Overrides" : "Skipped Questions");
		HashMap<String, WebElement> checkBoxes = clickScoresTabLink().getCheckboxes();
		if (checkBoxes.size() != 2) {
			Log.logInfo(
					"There number of checkboxes in the Scores Tab were not 2. They were [" + checkBoxes.keySet() + "]");
			return false;
		}
		boolean checkBox_overrides = false, checkBox_changes = false;
		for(String keys : checkBoxes.keySet()){
			if(keys.equals(checkboxShouldBe))
				checkBox_overrides = true;
			if(keys.contains("Changes"))
				checkBox_changes = true;
		}
		if (!(checkBox_overrides && checkBox_changes)) {
			Log.logInfo("The checkbox labels were not correct. They were [" + checkBoxes.keySet() + "]");
			return false;
		}
		return true;
	}

	/**
	 * Checks for the presence of the Refresh button, Scores and Attachments
	 * Tabs, Details Grid and Version Table
	 * 
	 * @return boolean true if all of the above elements are present, false
	 *         otherwise
	 */
	public boolean checkGeneralControls() {
		if (!getRightPaneTabs().containsKey("Scores")) {
			Log.logInfo("The Scores tab was not available.");
			return false;
		}
		if (!getRightPaneTabs().containsKey("Attachments")) {
			Log.logInfo("The Attachments tab was not available.");
			return false;
		}
		if (UiHelper.isPresent(refreshButton)) {
			Log.logInfo("The Refresh Button was not available.");
			return false;
		}
		if (!UiHelper.isPresent(By.cssSelector("div.details-grid"))) {
			Log.logInfo("The Details Grid was not available.");
			return false;
		}
		if (!UiHelper.isPresent(versionList)) {
			Log.logInfo("The Versions List was not available.");
			return false;
		}
		return true;
	}
	/*
	 * END SECTION-----------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * SECTION START---------------------------------------------------------------------------------------------------
	 * Audio Controls
	 */
	/*
	 * Audio Player locators
	 */
	private By audio_player_container = By.id("audio-player-container");
	
	/**
	 * Check if the Audio player exists and is visible
	 * @return boolean true if it exists and is visible, false otherwise
	 */
	public boolean audioPlayerExists(){
		return UiHelper.isPresentAndVisible(audio_player_container);
	}
	
	/*
	 * SECTION END-----------------------------------------------------------------------------------------------------
	 */

	/*
	 * SECTION START---------------------------------------------------------------------------------------------------
	 * Video Player Controls
	 */
	
	/*
	 * Video Player Icon
	 */
	@FindBy(css=".icon-video")
	private WebElement playVideoIcon;
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElemnt - playVideoIcon WebElement
	 */
	public WebElement getPlayVideoIcon() {
		return this.playVideoIcon;
	}
	
	/**
	 * Click on the Video Icon on the Assessment Details Page, right below the Assessment form Icon
	 */
	public void clickPlayVideo() {
		UiHelper.click(playVideoIcon);
		UiHelper.waitForSpinnerEnd(driver.findElement(By.cssSelector(".spinner")), 30);
	}
	
	/*
	 * Video Player Close Button
	 */
	@FindBy(css=".pull-right.text-right.col-xs-2 > .btn.circle-button.btn-white")
	private WebElement cancelVideoIcon;
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElemnt - cancelVideoIcon WebElement
	 */
	public WebElement getCancelVideoIcon() {
		return this.cancelVideoIcon;
	}
	
	/**
	 * Click on the Cancel Video Icon on the Assessment Details Video Page
	 */
	public void clickCancelVideo() {
		UiHelper.click(cancelVideoIcon);
	}
	
	/*
	 * SECTION END------------------------------------------------------------------------------------------------------
	 */

	public SubjectDetails clickSubjectLink() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @author UTTAM
	 *  Description - CLick on check box for assessment flag
	 */
	public void clinkOnCheckbox(String type) {
		Log.logInfo("Clicking assessment flag checkbox");
		if (type.equalsIgnoreCase("Paper")) {
			UiHelper.click(notAdministered);
		} else {
			UiHelper.click(notComplete);
		}
	}

	public boolean checkConfirmBtnIsVisible() {
		return UiHelper.isPresent(confirm);
	}
}

