package mt.siteportal.details;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

public class VisitDetails extends BasePage {

	public Map<String, String> details;
	public Map<String, String> subjectDetails;
	public String visitName;
	public AttachmentsTab attachmentsTab;
	
	private String detailsXpath = "//div[@class='details-grid row']/*/div[not(@id='popup_content_container')]/*";
	private String subjectXpath = "//h2[text()='Subject']/../following-sibling::*/*";
	
	@FindBy(how = How.CSS, using = "#page-title>h1")
	public WebElement VisitName;
	
	@FindBy(how = How.CSS, using = "a.thumb")
	private List<WebElement> thumbNail;

	public VisitDetails(WebDriver driver) {
		super(driver);
		attachmentsTab = PageFactory.initElements(Browser.getDriver(), AttachmentsTab.class);
	}
	public void waitForDetails(){
		//------------------------NZ TEAM--------------------------------//
		UiHelper.waitForVisibility(By.cssSelector("div.details-grid"));
		UiHelper.checkPendingRequests(driver);//Wait for all the forms to load
		//---------------------------------------------------------------//
	}
	
	private Map<String, String> getDetails() {
		Log.logInfo("Getting details of current Visit");
		Map<String,String> detailsData = new HashMap<String, String>();
		List<WebElement> elementList = UiHelper.findElements(new By.ByXPath(detailsXpath));
		String key;
		String value;
		for(int i = 0; i<elementList.size();i=i+2){
			 key = elementList.get(i).findElement(new By.ByXPath("label")).getText();
			if (key.equals("Status")){
				 value = elementList.get(i+1).findElement(new By.ByXPath("div/label")).getText();
			}
			else{
				 value = elementList.get(i+1).findElement(new By.ByXPath("label")).getText();
			}
			
			detailsData.put(key, value);
		}
		return detailsData;
		
	}
	
	private Map<String, String> getSubjectDetails() {
		Map<String,String> detailsData = new HashMap<String, String>();
		List<WebElement> elementList = UiHelper.findElements(new By.ByXPath(subjectXpath));
		String key;
		String value;
		for(int i = 0; i<elementList.size();i=i+2){
			 key = elementList.get(i).findElement(new By.ByXPath("label")).getText();
			if (key.equals("Subject")){
				value = elementList.get(i+1).findElement(new By.ByXPath("label/a")).getText();
			}
			else {
				value = elementList.get(i + 1).findElement(new By.ByXPath("label")).getText();
			}
			
			detailsData.put(key, value);
		}
		return detailsData;
		
	}
	
	public String getSubjectStatus(){
		this.subjectDetails=getSubjectDetails();
		return subjectDetails.get("Status");
	}
	
/*--------------------------------------NZ Team------------------------------------------*/	
//	private By detailsGrid = By.className(".details-grid");
	private By detailsGrid = By.cssSelector("div.details-grid");
	
	@FindBy(css = "#virgilForms")
	private WebElement virgilForms;

	/**
	 * Gets the header as text
	 * @return
	 */
	public String getHeader() {
		return VisitName.getText();
	}
	/**
	 * 
	 * @return Visit Form Name from the Header
	 */
	public String getVisitNameFromHeader() {
		UiHelper.waitForVisibility(VisitName);
		String text = TextHelper.getPartsFromHeader(VisitName.getText())[1];
		return text;
	}

	/**
	 * Verify the Visit Details page with respect to an specific SVID
	 * @param paramSVID
	 * @return
	 */
	public boolean detailsIsOpened(int paramSVID) {
		System.out.println(paramSVID);
		String svidUI = this.getVisitDetailsItemValue("SVID");
		if (String.valueOf(paramSVID).equals(svidUI) && detailsIsOpened()) {
			Log.logInfo("Current Page is found as the visit details of SVID:" + paramSVID);
			return true;
		}
		return false;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Verify the Visit Details page with respect to an specific Visit name
	 * 
	 * @param String visitName
	 * @return boolean
	 */
	public boolean detailsIsOpened(String visitName) {
		String visitNameUI = this.getVisitDetailsItemValue("Visit");
		if (visitName.equalsIgnoreCase(visitNameUI) && detailsIsOpened()) {
			Log.logInfo("Current Page is found as the visit details of Visit:" + visitName);
			return true;
		}
		return false;
	}

	/**
	 * Verify the Visit Details page
	 * @return
	 */
	public boolean detailsIsOpened(){
	Log.logStep("Checking if the current page is Visit Details...");
		String visitStr = TextHelper.getPartsFromDetails(VisitName.getText())[0];
		if(visitStr.equals("Visit") && UiHelper.isPresentAndVisible(detailsGrid)){
			Log.logInfo("Visit Details page is open.");
			return true;
		}
		Log.logInfo("Current Page is Not Visit Details.");
		return false;
	}
	
	@FindBy(css = ".details-grid>div.row")
	private List<WebElement> visitGrid;
	
	public HashMap<String,String> getVisitDetailsInfo(){
		HashMap<String,String> visitDetails = new HashMap<String,String>();
		for(int i=0;i<visitGrid.size();i++){	
			if(!visitGrid.get(i).isDisplayed()) continue;
			String label = this.gridVisitItemLabel(visitGrid.get(i));
			if(!label.equals("")){
				String value = visitGrid.get(i).findElement(By.cssSelector("div.value.col-xs-14")).getText().trim();
				visitDetails.put(label, value);
			}
		}			
		return visitDetails;			
	}
	/**
	 * 
	 * @param element
	 * @return label string if exists
	 */
	public String gridVisitItemLabel(WebElement element) {
		String itemLabel = "";
		try {
			Browser.getDriver().manage().timeouts().implicitlyWait(80, TimeUnit.MILLISECONDS);
			itemLabel = element.findElement(By.cssSelector("div.caption.col-xs-10>label")).getText().trim();
			Browser.resetTimeOuts();
			return itemLabel;
		} catch (org.openqa.selenium.NoSuchElementException ne) {
		}
		return itemLabel;
	}
	/**
	 * 
	 * @param label
	 * @return corresponding value
	 */
	//TODO: optimization for all Grid data validation, keep Hash outside
	public String getVisitDetailsItemValue(String label){
		String value = "";
		HashMap<String,String> assessDetails = this.getVisitDetailsInfo();
		value = assessDetails.get(label);
		return value;
	}
	/**
	 * 
	 * @return List of Form Thumnails
	 */
	public List<WebElement> getFormThumbnails(){		
		UiHelper.waitForVisibility(virgilForms);
		return thumbNail;
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
//		System.out.println("Yes1");
		WebElement element = thumbNail.get(index);
		Log.logStep("Clicking on the Form thumbnail...");
		UiHelper.click(element);
		Log.logStep("Clicked on the Form thumbnail.");
		Log.logStep("Redirecting to the Assesment Details page...");
		return PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
	}

	/**
	 * Clicks on a Submitted Assessment Form at the given index. Indexing starts at 0, and indicates the index relative to all the Submitted Forms, not every form.
	 * For example, for a total of 10 forms, if three of them have the Submitted Status, then index 2 will click on the third Submitted Forms. Any number greater than 2 will return null
	 * 
	 * @param index, int -> the index of the Submitted Form, starting at 0 and relative to the other Submitted Forms.
	 * @return AssesmentDetails
	 */
	public AssessmentDetails clickSubmittedOrAssignedFormThumbnail(int index){
		int count = 0;
		for(WebElement thumbnail : getFormThumbnails()){
			WebElement parentDiv = thumbnail.findElement(By.xpath("../../.."));
			String names = parentDiv.findElement(By.cssSelector(".assessment-preview>div.names")).getText();
			WebElement assignedText = parentDiv.findElement(By.cssSelector(".assessment-preview>div.administered-row"));
			if(names.contains("Submitted by:")){
				if(count==index){
					UiHelper.click(thumbnail);
					return PageFactory.initElements(driver, AssessmentDetails.class);
				}else{
					count++;
				}
			}else if(assignedText.isDisplayed() && !(assignedText.getText().contains("Not Assigned") || assignedText.getText().equals(""))){
				if(count==index){
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
		Log.logWarning("A Submitted Form at index : "+index + " could not be found. Returning null");
		return null;
	}
	
	/**
	 * @author ubiswas 
	 * 
	 * Description: open visit details page of selected visit
	 * 
	 * @param assessmentName
	 * @param visitName2
	 * @return
	 */
	public boolean detailsOfSelectedVisit(String assessmentName, String visitName) {
		String visitUI = this.getVisitDetailsItemValue("Visit");
		// String assessmentUI = this.getVisitDetailsItemValue("Assessment");
		if (visitUI.equals(visitName) && detailsIsOpened()) {
			Log.logInfo("Current Page is found as the visit details of Visit:" + visitName);
			return true;
		}
		return false;
	}

}
