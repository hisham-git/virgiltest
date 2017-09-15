package mt.siteportal.pages.studyDashboard;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ToolBarFull extends ToolBar{
	
    private By site = new By.ByXPath("//button[@class='btn dropdown-toggle btn-default']");
//	public By siteNumberList = new By.ByXPath("//button[@class='btn dropdown-toggle btn-default']/following-sibling::*//span");
	public By siteNumberList = new By.ByXPath("//div[@data-items='selectedStudy.sites']//li/span");
	public By studyProfile = new By.ByXPath("//a[@title='Study Profile']");

	@FindBy(how = How.XPATH, using = "//a[@title='Raters']")
	private WebElement Raters;

	@FindBy(how = How.XPATH, using = "//a[@title='Show queries']")
	private WebElement Queries;

	@FindBy(how = How.XPATH, using = "//button[@class='btn dropdown-toggle btn-active']/following-sibling::*//li")
	public List<WebElement> StudyNumberList;
		
	public Dashboard chooseSite(String nameOfSite){
		Log.logInfo("Selecting Site "+"'"+nameOfSite+"'");
		UiHelper.scrollToElementWithJavascript(site, Browser.getDriver()); //Hisham
		UiHelper.selectInDropdownBtn(site, nameOfSite);
		return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	public String getSiteName(){
		Log.logInfo("Getting Site Name");
		return UiHelper.getText(site);
	}
	
	public List<String> getSites(){
		UiHelper.click(site);
		List<String> sites = TextHelper.getElementTextContent(UiHelper.findElements(siteNumberList));
		UiHelper.click(site);
		return sites;
	}

	public int getSiteCount(){
		Log.logInfo("Getting Site Count");
		int count = UiHelper.findElements(siteNumberList).size();
		return count;
	}
	
	public Dashboard returnToDashboard(String byName) {
		Log.logInfo("Returning to dashboard");
		UiHelper.click(new By.ByXPath("//a[text()='" + byName + "']"));
		return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	public Dashboard returnToDashboard() {
		Log.logInfo("Returning to dashboard");
		clickStudyLinkFromBreadcrumbs();
		return PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	public SubjectsTable returnToAllSubjects(){
		Log.logInfo("Returning to Subject List");
		UiHelper.click(new By.ByXPath("//a[text()='Subjects: All']"));
		return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
		
	}
	
	/**
	 * @author HISHAM
	 * @param String name 
	 * 				- Subject name
	 * @return SubjectDetails 
	 * 				- Subject details page
	 */
	public SubjectDetails returnToSubject(String name) {
		Log.logInfo("Returning to details page for Subject: " + name);
		UiHelper.click(new By.ByXPath("//a[text()='Subject : " + name + "']"));
		return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
	}
	
	public VisitTable returnToAllVisits(){
		UiHelper.sleep(2000);
		Log.logInfo("Returning to Visit List");
		UiHelper.click(new By.ByXPath("//a[text()='Visits: All']"));
		return PageFactory.initElements(Browser.getDriver(), VisitTable.class);
		
	}

	public AssessmentsTable returnToAllAssesments(){
		UiHelper.sleep(2000);
		Log.logInfo("Returning to Assessment List");
		UiHelper.click(new By.ByXPath("//a[text()='Assessments: All']"));
		return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
	}
	
	/**
	 * @author HISHAM
	 * @param String name 
	 * 				- Assessment list page name
	 * @return AssessmentsTable 
	 * 				- AssessmentList page
	 */
	public AssessmentsTable returnToAssesmentList(String name){
		Log.logInfo("Returning to Assessment List: " + name);
		UiHelper.click(new By.ByXPath("//a[text()='Assessments: "+ name +"']"));
		return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
	}
	
	public StudyProfile openStudyProfile(){
		Log.logInfo("Opening StudyProfile");
		UiHelper.click(studyProfile);
		return PageFactory.initElements(Browser.getDriver(), StudyProfile.class);
	}
	public StudyRaters openRaters(){
		Log.logInfo("Opening Rates");
		UiHelper.click(Raters);
		return PageFactory.initElements(Browser.getDriver(), StudyRaters.class);
	}
	
	public QueriesSidePanel openQueries(){
		Log.logInfo("Opening Queries");
		UiHelper.click(Queries);
		return PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
	}
	
	
	public String getRatersCount(){
		try {
		Log.logInfo("Getting Raters Count");
		String raters = Raters.getText();
		String count = TextHelper.splitParentheses(raters);
		return count;
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "Unable to get count";
		}
	}
	
	public String getQueriesCount(){
		try {
		Log.logInfo("Getting Queries Count");
		String queries = Queries.getText();
		String count = TextHelper.splitParentheses(queries);
		return count;
		}
		catch(ArrayIndexOutOfBoundsException e){
			return "Unable to get count";
		}
	}
	
	//-------------------------------------------NZ Team------------------------------------//
	
	/*
	 * Breadcrumb links
	 */
	@FindBy(css = "#breadcrumbs>ul a.a-color")
	private List<WebElement> breadcrumbLinks;
	
	/*
	 * Breadcrumb Links parent element, used to check for visiblity
	 */
	@FindBy(id="breadcrumbs")
	private WebElement breadcrumb;
	
	/**
	 * Returns the breadcrumb;
	 * @return WebElement, breadcrumb parent element
	 */
	public WebElement getBreadcrumb() {
		return breadcrumb;
	}

	/**
	 * Returns the text from the first breadcrumbs link
	 * @return studyName
	 */
	public String getStudyNameFromBreadcrumbs(){
		UiHelper.waitForVisibility(By.id("breadcrumbs"));
		return breadcrumbLinks.get(0).getText();
	}
	
	/**
	 * Returns the first part of the text from the second breadcrumbs link 
	 * @return siteNumber
	 */
	public String getSiteNumberFromBreadcrumbs(){
		UiHelper.waitForVisibility(By.id("breadcrumbs"));
		if(breadcrumbLinks.size()<2) return null;
		return getFullSiteNameFromBreadcrumbs().split(" - ")[0];
	}
	

	/**
	 * Returns the second part of the text from the second breadcrumbs link 
	 * @return siteName
	 */
	public String getSiteNameFromBreadcrumbs(){
		UiHelper.waitForVisibility(By.id("breadcrumbs"));
		if(breadcrumbLinks.size()<2) return null;
		return getFullSiteNameFromBreadcrumbs().split(" - ")[1];
	}
	
	/**
	 * Returns the text from the second breadcrumbs link 
	 * @return siteName
	 */
	public String getFullSiteNameFromBreadcrumbs(){
		UiHelper.waitForVisibility(By.id("breadcrumbs"));
		if(breadcrumbLinks.size()<2) return null;
		return breadcrumbLinks.get(1).getText();
	}
	
	/**
	 * Returns the text from the second breadcrumbs link 
	 * @return siteName
	 */
	public String getFilterFromBreadcrumbs(){
		UiHelper.waitForVisibility(By.id("breadcrumbs"));
		if(breadcrumbLinks.size()<3) return null;
		return breadcrumbLinks.get(2).getText();
	}

	/**
	 * Returns the Text of the breadCrumbs links
	 * @return ArrayList<String> of breadcrumb's links tests
	 */
	public List<String> getBreadcrumbTexts() {
		UiHelper.waitFor(getBreadcrumb());
		return TextHelper.getElementTextContent(breadcrumbLinks);
	}

	/**
	 * Clicks on the Study Link from the Breadcrumbs
	 */
	public void clickStudyLinkFromBreadcrumbs() {
		breadcrumbLinks.get(0).click();
	}

	/**
	 * Clicks on the Second link in the breadcrumbs navigation links
	 * Since the type of page the link will redirect to differs from page to page, there is no fixed return type
	 */
	public void clickSecondLinkFromBreadcrumbs() {
		if(breadcrumbLinks.size()>1){
			breadcrumbLinks.get(1).click();
			return;
		}
		Log.logWarning("There is less than two links in the the Breadcrumbs. Could not click the second link since it is non-existant");
	}

	/**
	 * Clicks on the Third link in the breadcrumbs navigation links Since the
	 * type of page the link will redirect to differs from page to page, there
	 * is no fixed return type
	 */
	public void clickThirdLinkFromBreadcrumbs() {
		if (breadcrumbLinks.size() > 2) {
			breadcrumbLinks.get(2).click();
			return;
		}
		Log.logWarning(
				"There are less than three links in the the Breadcrumbs. Could not click the third link since it is non-existant");
	}

	/**
	 * Clicks on the Last link in the breadcrumbs navigation links Since the
	 * type of page the link will redirect to differs from page to page, there
	 * is no fixed return type
	 */
	public void clickLastLinkFromBreadcrumbs() {
		UiHelper.waitFor(getBreadcrumb());
		Log.logStep("Clicking the last link from the breadcrumbs...");
		breadcrumbLinks.get(breadcrumbLinks.size() - 1).click();
		return;
	}

	/**
	 * Selects a random Site from the Sites Dropdown list and clicks on it.
	 *
	 * @return - String - the site that was randomly chosen
	 */
	public String clickRandomSite() {
		UiHelper.click(site);
		List<String> sites = TextHelper.getElementTextContent(UiHelper.findElements(siteNumberList));
		String randomlySelectedSite = UiHelper.getRandomObjectFromList(sites);
		Log.logStep("Selecting random site - " + randomlySelectedSite);
		UiHelper.click(new By.ByXPath("//span[text()='  " + randomlySelectedSite + "']/.."));
		return randomlySelectedSite;
	}
	
	/**
	 * @author HISHAM
	 * Selects Site according to index from the Site Dropdown list and clicks on it.
	 *
	 * @return - String - the Site that was chosen
	 */
	public String clickSiteNo(int i) {
		UiHelper.click(site);
		List<String> sites = TextHelper.getElementTextContent(UiHelper.findElements(siteNumberList));
		String selectedSite = sites.get(i);
		Log.logStep("Selecting site - " + selectedSite);
		UiHelper.click(new By.ByXPath("//span[text()='  " + selectedSite + "']/.."));
		return selectedSite;
	}

	/**
	 * Selects a random Study from the Study Dropdown list and clicks on it.
	 *
	 * @return - String - the Study that was randomly chosen
	 */
	public String chooseRandomStudy() {
		UiHelper.click(studyDropdown);
		List<String> studies = TextHelper.getElementTextContent(StudyNumberList);
		String randomlySelectedStudy = UiHelper.getRandomObjectFromList(studies);
		Log.logStep("Selecting random study - " + randomlySelectedStudy);
		UiHelper.click(new By.ByXPath("//span[text()='  " + randomlySelectedStudy + "']/.."));
		return randomlySelectedStudy;
	}
	
	/**
	 * @author HISHAM
	 * Selects Study according to index from the Study Dropdown list and clicks on it.
	 *
	 * @return - String - the Study that was chosen
	 */
	public String chooseStudyNo(int i) {
		UiHelper.click(studyDropdown);
		List<String> studies = TextHelper.getElementTextContent(StudyNumberList);
		String selectedStudy = studies.get(i);
		Log.logStep("Selecting study - " + selectedStudy);
		UiHelper.click(new By.ByXPath("//span[text()='  " + selectedStudy + "']/.."));
		return selectedStudy;
	}
	
	
	
}
