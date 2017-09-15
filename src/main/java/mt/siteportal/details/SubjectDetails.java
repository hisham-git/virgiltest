package mt.siteportal.details;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.studyDashboard.NewEditSubject;
import mt.siteportal.pages.studyDashboard.ObserversInSubject;
import mt.siteportal.pages.studyDashboard.VisitsInSubject;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class SubjectDetails extends BasePage {

	public VisitsInSubject visitTable;
	public AttachmentsTab attachmentsTab;
	public Map<String, String> details;
	// public String subjectName;

	public ObserversInSubject observersTable;

	private String detailsXpath = "//div[@class='details-grid row']/div[not(@class='col-xs-24 top-indent')]/div[not(@id='popup_content_container')][not(@class='row collapsed-row ng-hide')]/div[not(@class='row ng-hide')][not(@class='row')]";

	private By subjectName = new By.ByXPath("//div[@id='page-title']/h1/span");

	private By editButton = new By.ByXPath("//a[@title = 'Edit']/span");
	
	@FindBy(how = How.XPATH, using = "//a[@title = 'Save']/span")
	public WebElement SaveButton;

	private By cancelButton = new By.ByXPath("//a[@data-ng-click='cancelChanges()']/span");
	public By deleteButton = new By.ByCssSelector(".icon-small.icon-delete");

	@FindBy(how = How.XPATH, using = "//button[@title='Remove']")
	private WebElement RemoveButton;
	
	///////////////////////////////////////
	/**
	 * @author ubiswas
	 */
	private By cancelComment = new By.ByCssSelector("textarea[data-ng-model='crSchedulingCancelComment']");
	private By onCrSchedulingCancellationConfirmButton = new By.ByCssSelector(
			"div[data-ng-click='onCrSchedulingCancellationConfirmButtonClick()']");
	private By cancellationOkButton = new By.ByCssSelector("div[data-ng-click='okBtnClick()']");
	private By viewOrRescheduleIcon = new By.ByCssSelector("a:nth-child(7) > span");
	private By cancelSchedulerIcon = new By.ByCssSelector("a:nth-child(8) > span");

	@FindBy(css = "a:nth-child(6)")
	private WebElement addCrVisitButton;

	@FindBy(css = "div#cancel-self-cr-modal")
	private WebElement cancelCrModal;

	/////////////////////////////////////////

	public SubjectDetails(WebDriver driver) {
		super(driver);
		visitTable = PageFactory.initElements(Browser.getDriver(), VisitsInSubject.class);
		observersTable = PageFactory.initElements(Browser.getDriver(), ObserversInSubject.class);
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
	}

	public Map<String, String> readDetailsFromUi() {
		Log.logInfo("Getting details of current Subject");
		Map<String, String> detailsData = new HashMap<String, String>();
		List<WebElement> elementList = UiHelper.findElements(new By.ByXPath(detailsXpath));
		String key;
		String value;
		for (int i = 0; i < elementList.size(); i = i + 2) {
			key = elementList.get(i).findElement(new By.ByXPath("label")).getText();
			if (key.equals("Status") || key.equals("Disable ObsRO") || key.equals("Disable PRO")) {
				value = elementList.get(i + 1).findElement(new By.ByXPath("./div/label")).getText();
			} else {
				value = elementList.get(i + 1).findElement(new By.ByXPath(".//label")).getText();
			}
			detailsData.put(key, value);
		}
		return detailsData;
	}

	public String getSubjectStatus() {
		Log.logInfo("Getting current status of Subject");
		this.details = readDetailsFromUi();
		return details.get("Status");
	}

	public boolean detailsIsOpened(String subjectName){
		WebElement element = UiHelper.findElement(this.subjectName);
		if(subjectName.equalsIgnoreCase(element.getText())){
			return true;
		}
		return false;
	}

	public NewEditSubject subjectToEdit() {
		Log.logInfo("Opening the subject edit screen");
		UiHelper.click(editButton);
		return PageFactory.initElements(Browser.getDriver(),NewEditSubject.class);
	}

	public void saveEditedSubject() {
		Log.logInfo("Saving edited subject");
		UiHelper.click(SaveButton);
		this.details = readDetailsFromUi();
	}

	public void cancelEditedSubject() {
		Log.logInfo("Cancel edited subject");
		UiHelper.click(cancelButton);
		this.details = readDetailsFromUi();
	}

	public SubjectsTable deleteSubject() {
		Log.logInfo("Deleting subject");
		UiHelper.click(deleteButton);
		UiHelper.click(RemoveButton);
		return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
	}

	public boolean isContainDisabledForm(String reason) {
		Log.logInfo("Does subject contain disabled form");
		return UiHelper.isPresent(new By.ByXPath("//label[@title='" + reason + "']"));
	}

	public void refreshSubject() {
		Log.logInfo("Refreshing the details of current subject");
		UiHelper.fluentWaitForElementVisibility(refreshButton, 10).click();
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
	}

	/*----------------------------------------NZ Team-----------------------------------------------------*/
	@FindBy(css = "#page-title>h1")
	private WebElement subjName;

	@FindBy(css = "a.thumb")
	private List<WebElement> thumbNails;

	@FindBy(css = "#virgilForms>div.section")
	private List<WebElement> formSections;

	@FindBy(css = "#virgilForms")
	private WebElement virgilForms;
	
	@FindBy(css = "input[data-ng-model='editingSubjectDetails.proDisabled']")
	private WebElement DisablePROchkbox;
	
	@FindBy(css = "input[data-ng-model='editingSubjectDetails.obsRoDisabled']")
	private WebElement DisableObsROchkbox;

	/**
	 * Gets the header as Text
	 * 
	 * @return String header text
	 */
	public String getHeader() {
		return subjName.getText();
	}

	/**
	 * 
	 * @return Subject Form Name from the Header
	 */
	public String getSubjNameFromHeader() {
		UiHelper.waitForVisibility(subjName);
		String text = TextHelper.getPartsFromDetails(subjName.getText())[1];
		return text;
	}

	public boolean isOpened() {
		WebElement header = driver.findElement(By.xpath("//div[@id='page-title']/h1"));
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(header));
		if (header.getText().contains("Subject:"))
			return true;
		return false;
	}

	/**
	 * 
	 * @param label
	 * @return corresponding value
	 */
	public String getSubjectDetailsItemValue(String label) {
		String value = "";
		Map<String, String> assessDetails = this.readDetailsFromUi();
		value = assessDetails.get(label);
		return value;
	}

	/**
	 * 
	 * @return List of Form Thumnails
	 */
	public List<WebElement> getFormThumbnails() {
		UiHelper.waitForVisibility(virgilForms);
		return thumbNails;
	}
	
	/**
	 * 
	 * @return Count of Form Thumnails
	 */
	public int getFormCount() {
		int count = getFormThumbnails().size();
		if (count > 0)
			return count;
		Log.logWarning("No assessment(s) found in list");
		return count;
	}

	/**
	 * 
	 * @param index
	 * @return AssessmentDetails page
	 */
	public AssessmentDetails clickFormThumbnail(int index){
		WebElement element = thumbNails.get(index);
		Log.logStep("Clicking on a Virgil Form thumbnail...");
		UiHelper.click(element);
		Log.logStep("Clicked on the Form thumbnail.");
		Log.logStep("Redirecting to the Assesment Details page...");
		return PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
	}

	/**
	 * Clicks on a Submitted Assessment Form at the given index. Indexing starts
	 * at 0, and indicates the index relative to all the Submitted Forms, not
	 * every form. For example, for a total of 10 forms, if three of them have
	 * the Submitted Status, then index 2 will click on the third Submitted
	 * Forms. Any number greater than 2 will return null
	 * 
	 * @param index,
	 *            int -> the index of the Submitted Form, starting at 0 and
	 *            relative to the other Submitted Forms.
	 * @return AssesmentDetails
	 */
	public AssessmentDetails clickSubmittedOrAssignedFormThumbnail(int index){
		int count = 0;
		for (WebElement thumbnail : getFormThumbnails()) {
			WebElement parentDiv = thumbnail.findElement(By.xpath("../../.."));
			String names = parentDiv.findElement(By.cssSelector(".assessment-preview>div.names")).getText();
			WebElement assignedText = parentDiv.findElement(By.cssSelector(".assessment-preview>div.administered-row"));
			if (names.contains("Submitted by:")) {
				if (count == index) {
					Log.logStep("Found the form. Clicking on the form thumbnail...");
					UiHelper.click(thumbnail);
					Log.logInfo("Clicked on the thumbnail.");
					Log.logStep("Redirecting to Assesment Details page...");
					return PageFactory.initElements(driver, AssessmentDetails.class);
				}else{
					count++;
				}
			} else if (assignedText.isDisplayed() && !assignedText.getText().contains("Not Assigned")) {
				if (count == index) {
					Log.logStep("Found the form. Clicking on the form thumbnail...");
					UiHelper.click(thumbnail);
					Log.logInfo("Clicked on the thumbnail.");
					Log.logStep("Redirecting to Assesment Details page...");
					return PageFactory.initElements(driver, AssessmentDetails.class);
				}else{
					count++;
				}
			}
		}
		Log.logWarning("A Submitted Form at index : " + index + " could not be found. Returning null");
		return null;
	}

	/*---------------TODO: Replace the Visit table with appropriate methods-----------------------------*/

	@FindBy(css = ".portal-grid.ng-isolate-scope")
	private WebElement visitGrid;

//	@FindBy(css = ".refresh-btn-wrapper>a")
	@FindBy(css = "div.btn-wrapper > a.circle-button")
	protected WebElement refreshButton;

	private By visitTableRowsLocator = By.cssSelector(".row.grid-row.ng-scope");

	/**
	 * 
	 * @return First item in the list
	 */
	public WebElement getFirstItemFromList() {
		UiHelper.click(refreshButton);
		UiHelper.checkPendingRequests(driver);
		List<WebElement> list = visitGrid.findElements(visitTableRowsLocator);
		if (list.size() > 0) {
			return list.get(0);
		}
		Log.logInfo("Number of Items was 0 so returning null.");
		return null;
	}

	/**
	 * Clicks the visit with the appropriate name in the Visits Table
	 *
	 * @param String
	 *            - visitName - the name of the visit to click
	 * @return boolean true if a visit of the name was found, false otherwise
	 */
	public boolean clickVisitRow(String visitName) {
		Log.logInfo("Trying to click row for visit [" + visitName + "]");
		WebElement row = getVisitFromVisitTable(visitName);
		return clickVisitRow(row);
	}

	/**
	 * Clicks the visit with the appropriate name in the Visits Table
	 *
	 * @param WebElement
	 *            - row - the row to click
	 * @return boolean true if a visit of the name was found, false otherwise
	 */
	public boolean clickVisitRow(WebElement row) {
		if (row == null) {
			Log.logWarning("Did not click row because it was not found.");
			return false;
		}
		UiHelper.click(row);
		Log.logStep("Clicked row.");
		return true;
	}

	/**
	 * Get the WebElement of the Row form the visit table. Returns null if no
	 * row with the given name exists
	 *
	 * @param String
	 *            visitName - the name of the visit to look for
	 * @return WebElement - the row WebElement, null if not found;
	 */
	public WebElement getVisitFromVisitTable(String visitName) {
		List<WebElement> visitTableRows = visitGrid.findElements(visitTableRowsLocator);
		for (WebElement row : visitTableRows) {
			String currentRowsVisitName = row.findElement(By.xpath("div/div/div[1]")).getText();
			if (currentRowsVisitName.equalsIgnoreCase(visitName)) {
				return row;
			}
		}
		Log.logWarning("Did not find visit called [" + visitName + "] in the Visits Table. Returning null.");
		return null;
	}

	/**
	 * Get the value of the Status column of a particular Visit from the Visit
	 * Table
	 *
	 * @param visitName
	 * @return String - the Status of the specified visit from the Table.
	 */
	public String getStatusOfVisit(String visitName) {
		WebElement row = getVisitFromVisitTable(visitName);
		if (row == null) {
			Log.logWarning("No Status can be returned for non-existant Visit. Returning empty String.");
			return "";
		}
		return row.findElement(By.cssSelector(" div.extraTabletColumn>span")).getText();
	}

	/**
	 * Clicks the add button for a particular Visits
	 *
	 * @param visitName
	 */
	public void clickAddButtonForVisit(String visitName) {
		WebElement row = getVisitFromVisitTable(visitName);
		clickVisitRow(row);
		if (row == null) {
			Log.logInfo("Row not found so could not click Add Button.");
			return;
		}
		WebElement addButton = row.findElement(By.xpath(".//a[@title='Add']"));
		UiHelper.click(addButton);
		String visitStatus = getStatusOfVisit(visitName);
		if (!visitStatus.equalsIgnoreCase("Skipped") || !visitStatus.equalsIgnoreCase(""))
			Log.logInfo("Successfully added Visit [" + visitName + "]  to Subject.");
		else
			Log.logInfo("Did not successfully add Visit [" + visitName + "]  to Subject.");
	}

	/**
	 * @author HISHAM
	 * 
	 * Click the specified Form Type (ClinRO, ObsRO, PRO) from 'Enter Paper Transcription' Link
	 *
	 * @param string
	 *            - the type of form (ClinRO, PRO, ObsRO)
	 * @return AssesmentDetails
	 */
	public AssessmentDetails clickEnterPaperTranscriptionLinkFor(String formType) {
		// return clickEnterPaperTranscriptionLinkFor(formType, 1);
		List<WebElement> formsOfParticularType = getAllFormsOfType(formType);
		if (null != formsOfParticularType) {
			Log.logInfo("Forms with type: [" + formType + "] found");
			formsOfParticularType = filterInAllFormsWithPaperTranscription(formsOfParticularType);
			if (null != formsOfParticularType) {
				Log.logInfo("Paper transcription link found for [" + formType + "] forms");
				return clickEnterPaperTranscriptionLink(formsOfParticularType.get(0));
			}
		}
		Log.logInfo("No forms with type: [" + formType + "] found. Returnnig null...");
		return null;
	}

	/**
	 * Click the specified Form Type (ClinRO, ObsRO, PRO) form's Enter Paper
	 * Transcription Link. If there are multiple forms of the same type, then
	 * click the one at the specified index
	 *
	 * @param formType
	 *            - String - the type of form, could only be ClinRO, ObsRO and
	 *            PRO
	 * @param index
	 *            - int - the index of the Form
	 * @return AssessmentDetails
	 */
	public AssessmentDetails clickEnterPaperTranscriptionLinkFor(String formType, int index) {
		List<WebElement> sections = getAllFormsOfType(formType);
		sections = filterInAllFormsWithPaperTranscription(sections);
		try {
			WebElement form = sections.get(index - 1);
			return clickEnterPaperTranscriptionLink(form);
		} catch (IndexOutOfBoundsException ioob) {
			Log.logWarning("No Form of the type [" + formType
					+ "] with \"Enter Paper Transcription\" link could be found at index [" + index + "]");
			return null;
		}
	}

	/**
	 * From the list of forms of a particular visit for a particular Subject
	 * Details, filter out only the forms which have the Paper Transcription
	 * Link
	 *
	 * @param sections
	 *            - List<WebElement> - the Assessment Forms list
	 * @return List<WebElement> - the Assessment Forms with only Paper
	 *         Transcription
	 */
	public List<WebElement> filterInAllFormsWithPaperTranscription(List<WebElement> sections) {
		List<WebElement> forms = new ArrayList<WebElement>();
		for (WebElement section : sections) {
			WebElement enterPaperTranscriptionLink = section
					.findElement(By.cssSelector("a[data-ng-click*='createPaperTranscription']"));
			// WebElement enterPaperTranscriptionLink =
			// section.findElement(By.xpath(".//a[not(@class='thumb')]"));
			if (null != enterPaperTranscriptionLink && UiHelper.isClickable(enterPaperTranscriptionLink))
				forms.add(section);
		}
		return forms;
	}

	/**
	 * Clicks the Enter Paper Transcription Link for a given Assessment Form
	 *
	 * @param form
	 *            - WebElement - The Paper Form's WebElement
	 * @return = AssessmentDetails - since it is expected that clicking the link
	 *         will redirect to the Assessments Details page
	 */
	public AssessmentDetails clickEnterPaperTranscriptionLink(WebElement form) {
//		WebElement thumbnail = form.findElement(By.xpath(".//a[not(@class='thumb')]"));
		WebElement pptLink = form.findElement(By.cssSelector("a[data-ng-click*='createPaperTranscription']"));
		UiHelper.scrollToElementWithJavascript(pptLink, driver);
		UiHelper.click(pptLink);
		return PageFactory.initElements(driver, AssessmentDetails.class);
	}

	/**
	 * Takes the list of Assessment Forms from the subject detail's right tab
	 * and filters in the forms of a particular type (ClinRO, ObsRO, PRO)
	 *
	 * @param formType -
	 * @return
	 */
	public List<WebElement> getAllFormsOfType(String formType) {
		List<WebElement> formsOfAParticularType = new ArrayList<WebElement>();
		for (WebElement formSection : formSections) {
			String formTypeFromSection = formSection.findElement(By.xpath("div[contains(@class, 'small')]")).getText();
			if (formType.equals(formTypeFromSection))
				formsOfAParticularType.add(formSection);
		}
		return formsOfAParticularType;
	}
	
	/**
	 * Takes the list of Assessment Forms from the subject detail's right tab
	 * and filters in the forms of a given type one/multiple (ClinRO, ObsRO, PRO)
	 *
	 * @param formTypes
	 * 			- List<String>, name of the types(ClinRO, ObsRO, PRO) to search. could be one/multiple.
	 * @return formsOfTypes
	 * 			- List<WebElement>, all form elements of given type(s)
	 * @author HISHAM
	 */
	public List<WebElement> getAllFormsOfTypes(List<String> formTypes) {
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		List<WebElement> formsOfTypes = new ArrayList<WebElement>();
		for (String formType : formTypes) {
			for (WebElement formSection : formSections) {
				String formTypeFromSection = formSection.findElement(By.xpath("div[contains(@class, 'small')]"))
						.getText();
				if (formType.equals(formTypeFromSection))
					formsOfTypes.add(formSection);
			}
		}
		return formsOfTypes;
	}
	
	/**
	 * @author Hisham
	 * 
	 * Description: Searches assignment status for ClinRO Assessment
	 * 
	 * Steps:
	 * 	a. If user has claim for 'can assign to me' 
	 * 		then it searches for Assignment drop-down for first Assessment
	 *	b. If user doesn't have claim for 'can assign to me' 
	 *		then it searches for 'Not Assigned' text for first Assessment
	 * 
	 * @param hasClaim
	 *          	- User access rights for 'can assign to me' claim
	 * @return boolean 
	 * 				- true if Assignment drop-down/'Not Assigned' text found, false otherwise
	 */
	public boolean getAssignmentStatus(boolean hasClaim, int formIndex) {
		List<WebElement> clinROforms = getAllFormsOfType("ClinRO");
		if (!clinROforms.isEmpty()) {
			if (hasClaim) {
				try {
					UiHelper.scrollToElementWithJavascript(clinROforms.get(formIndex), Browser.getDriver());
					WebElement dropDownElement = clinROforms.get(formIndex)
							.findElement(By.cssSelector("button.btn.btn-default.dropdown-toggle"));
					if (UiHelper.isClickable(dropDownElement)) {
						UiHelper.click(dropDownElement);
						List<WebElement> dropDownValues = clinROforms.get(formIndex)
								.findElements(By.cssSelector("span.pull-left.ng-binding.ng-scope"));
						Log.logInfo("Dropdown has: " + dropDownValues.size() + " value(s)");
						if ( dropDownValues.size() > 0 ) {
							for (WebElement dropDownValue : dropDownValues) {
									Log.logInfo("Assignment dropdown found value: " + dropDownValue.getText());
							}
							return true;
						} else {
							Log.logError("Assignment dropdown doesn't contain 'Me'/'Other Raters' value");
							return false;
						}
					}
					Log.logError("Assignment dropdown not clickable");
					return false;
				} catch (NoSuchElementException nse) {
					Log.logError("Assignment dropdown not found");
					return false;
				}
			} else {
				try {
					UiHelper.scrollToElementWithJavascript(clinROforms.get(0), Browser.getDriver());
					WebElement assignmentStatus = clinROforms.get(0)
							.findElement(By.cssSelector("label.disabled.ng-binding.ng-scope"));
					if (UiHelper.isVisible(assignmentStatus)) {
						if (assignmentStatus.getText().equalsIgnoreCase("Not Assigned")) {
							Log.logInfo("Assignment status found value: " + assignmentStatus.getText());
							return true;
						}
						Log.logError("Assignment status doesn't contain value 'Not Assigned'");
						return false;
					}
					Log.logError("Assignment status not visible");
					return false;
				} catch (NoSuchElementException nse) {
					Log.logError("Assignment status not found");
					return false;
				}
			}
		} else {
			throw new SkipException("ClinRO type forms not found");
		}
	}
	
	/**
	 * @author Hisham
	 * 
	 *         Description: Searches for Enter PaperTranscription Link
	 * 
	 * @return boolean 
	 * 		- true if found, false if any one link is not clickable
	 */
	public boolean enterPaperTranscriptionLinkIsClickable() {
		for (WebElement formSection : formSections) {
			WebElement status = formSection.findElement(By.cssSelector("a[data-ng-click*='createPaperTranscription']"));
			if (!UiHelper.isClickable(status)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @author Hisham
	 * 
	 *		Description: Check/Uncheck Disable PRO checkbox
	 * 
	 * @return boolean 
	 * 		- true if clickable & can be checked/unchecked 
	 * 		- false if not clickable
	 */
	public boolean checkUncheckDisablePROchkbox() {
		if (UiHelper.checkUncheck(DisablePROchkbox))
			return true;
		return false;
	}
	
	/**
	 * @author Hisham
	 * 
	 *		Description: Check/Uncheck Disable ObsRO checkbox
	 * 
	 * @return boolean 
	 * 		- true if clickable & can be checked/unchecked 
	 * 		- false if not clickable
	 */
	public boolean checkUncheckDisableObsROchkbox() {
		if (UiHelper.checkUncheck(DisableObsROchkbox))
			return true;
		return false;
	}
	
	/**
	 * @author Hisham
	 * 
	 *		Description: Getter for Subject Edit button locator 
	 * 
	 * @return By 
	 * 		- Subject Edit button locator if present & visible, null otherwise
	 */
	public By getSubjectEditButton() {
		if (UiHelper.isClickable(editButton))
			return editButton;
		return null;
	}
	
	/**
	 * @author Hisham
	 * 
	 *		Description: Getter for Subject Edit cancel button locator 
	 * 
	 * @return By 
	 * 		- Subject Edit cancel button locator if present & visible, null otherwise
	 */
	public By getSubjectEditCancelButton() {
		if (UiHelper.isClickable(cancelButton))
			return cancelButton;
		return null;
	}
	
	//////////////////////////////////////////
	/**
	 * @author ubiswas Description: Click on
	 */

	public void clickAddButtonForCrVisit(String visitName, String path) {
		WebElement row = getVisitFromVisitTable(visitName);
		clickVisitRow(row);
		if (row == null) {
			Log.logInfo("Row not found so could not click Add Button.");
			return;
		}
		WebElement addButton = row.findElement(new By.ByCssSelector(path));

		if (addButton != null)
			UiHelper.click(addButton);
	}

	public void addCrVisit(String visitName) {
		UiHelper.click(refreshButton);
		clickAddButtonForCrVisit(visitName, "a:nth-child(6");
		UiHelper.sleep();
	}

	public WebElement getViewOrReschedularIcon(String visitName) {
		WebElement row = getVisitFromVisitTable(visitName);
		clickVisitRow(row);
		return row.findElement(viewOrRescheduleIcon);
	}

	public WebElement getCancelSchedularIcon(String visitName) {
		WebElement row = getVisitFromVisitTable(visitName);
		clickVisitRow(row);
		return row.findElement(cancelSchedulerIcon);
	}

	public void rescheduleCrAppointment(String visitName) {
		UiHelper.click(refreshButton);
		clickAddButtonForCrVisit(visitName, "a:nth-child(7)>span");
		UiHelper.sleep();

	}

	public void cancelCrAppointment(String visitName, String comment) {
		UiHelper.click(refreshButton);
		clickAddButtonForCrVisit(visitName, "a:nth-child(8)>span");
		UiHelper.sleep();
		enterComentForCancelAppointment(comment);
		confirmCancellation();
		UiHelper.click(cancellationOkButton);

	}

	public void enterComentForCancelAppointment(String comment) {
		cancelCrModal.findElement(cancelComment).sendKeys(comment);

	}

	public void confirmCancellation() {
		UiHelper.click(cancelCrModal.findElement(onCrSchedulingCancellationConfirmButton));
	}

	////////////////////////////////////////////////
}