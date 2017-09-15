package mt.siteportal.tables;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.objects.Subject;
import mt.siteportal.objects.Visit;
import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.studyDashboard.NewEditSubject;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectsTable extends BasePage implements Table{

	public By addNewSubject = new By.ByXPath("//a[@title='Add Subject']");
	
	@FindBy(how = How.XPATH, using = "//a[@title='Refresh']")
    private WebElement Refresh;
	private By siteDropdown = new By.ByXPath("//ul[@class='dropdown-menu inline-dropdown']");

	private Map <String,Subject> table = new HashMap<String, Subject>();
	private List <Subject> listOfSubjectObjects = new ArrayList<Subject>();
	private String headerLocator = "//div[@id='portal-grid-page-header']//div[contains(@class,'sortable') and not(contains(@class,'group'))]/*[@title]";
	private String tableLocator = "//div[@id='portal-grid-page-content']/div[%s]/div";
	
	
	public SubjectsTable(WebDriver driver) {
	    super(driver);
	}
	    
	public NewEditSubject openNewSubjectForm(String siteName) {
		Log.logInfo("Opening New Subject Form");
		UiHelper.click(addNewSubject);
		if (UiHelper.isPresent(siteDropdown)) {
			UiHelper.click(new By.ByXPath("//a[text()='" + siteName + "']/.."));
		}
		return PageFactory.initElements(Browser.getDriver(), NewEditSubject.class);
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Improved version
	 * 
	 * @param subjectName

	 * @return
	 */
	public SubjectDetails openSubjectDetails(String subjectName) {
		DashboardList dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logInfo("Trying to click a Subject with Screening#: [" + subjectName + "]");
		Actions actions = new Actions(Browser.getDriver());
		WebElement subject = dashboardList.getFirstItemFromList();
		while (subject != null) {
			actions.moveToElement(subject).perform();
			String currentRowSubjectName = subject
					.findElement(By.xpath("div[" + dashboardList.getIndexOf("Subject") + "]")).getText();
			if (currentRowSubjectName.equalsIgnoreCase(subjectName)) {
				Log.logInfo("Found the row with the matching Subject, clicking it....");
				UiHelper.click(subject);
				Log.logInfo("Redirecting to Subject Details page...");
				return PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			}
			subject = dashboardList.nextRow(subject);
			UiHelper.fastWait(Browser.getDriver());
		}
		Log.logInfo("Could not find any Subject with these values.");
		return null;
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */	
	public List<Subject> getListOfSubjectObjects() {
		WebDriver driver = Browser.getDriver();
		DashboardList dashboardList = PageFactory.initElements(driver, DashboardList.class);
		List<WebElement> headers = UiHelper.findElements(new By.ByXPath(headerLocator));
		List<String> textFromHeaders = TextHelper.getAtributeValues(headers, "title");
		int subjectCount = Integer.parseInt(TextHelper.splitParentheses(UiHelper.findElementByXpath("//h1").getText()));
		Log.logInfo("Subject(s) available: [" + subjectCount + "]");
		Actions actions = new Actions(driver);
		WebElement subjectRow = dashboardList.getFirstItemFromList();
		while (subjectRow != null) {
			actions.moveToElement(subjectRow).perform();
			List<WebElement> elements = subjectRow.findElements(new By.ByXPath("div/div/label"));
			List<String> textFromElements = TextHelper.getElementTextContent(elements);
			listOfSubjectObjects.add(new Subject(textFromHeaders, textFromElements));
			subjectRow = dashboardList.nextRow(subjectRow);
		}
		return listOfSubjectObjects;
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */
	public Map<String, Subject> getTable() {
		List<Subject> subjectObjectList = getListOfSubjectObjects();
		if (subjectObjectList.size() > 0) {
			for (Subject subjectObject : subjectObjectList) {
				table.put(subjectObject.getSubjectName(), subjectObject);
			}
			return table;
		}
		Log.logError("No subject found. Couldn't create table");
		return null;
	}

	/**
	* Gets string near the add button on the page
	*
	* @return String like "Subjects: All (9)".
	*/
	public String subjectsThatOpened(){
		return UiHelper.getText(new By.ByXPath(".//*[@id='page-title']/h1"));
	}
	
	/*
	 * Checking if a subject is present on subject list..
	 */
	/*public boolean isSubjectPresent(String subjectName) {
		if (UiHelper.isPresent(new By.ByXPath("//div/label[text()='" + subjectName + "']"))) {
			return true;

		} else {
			return false;
		}
	}*/
	
	
	public boolean isSubjectPresent(String subjectName) {
		DashboardList dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logInfo("Trying to find a Subject Number with: [" + subjectName + "]");
		Actions actions = new Actions(Browser.getDriver());
		WebElement subject = dashboardList.getFirstItemFromList();
		while (subject != null) {
			actions.moveToElement(subject).perform();
			String currentRowSubjectName = subject
					.findElement(By.xpath("div[" + dashboardList.getIndexOf("Subject") + "]")).getText();
			if (currentRowSubjectName.equalsIgnoreCase(subjectName)) {
				Log.logStep("Found the matching Subject....");
				return true;
			}
			subject = dashboardList.nextRow(subject);
			UiHelper.fastWait(Browser.getDriver());
		}
		Log.logInfo("Could not find Subject Number with: [" + subjectName + "]");
		return false;
	}
	
	public List<String> getListSubjectStatus() {
		List<String> lst = new ArrayList<String>();
		for(int i = 0; i< listOfSubjectObjects.size(); i++){
			lst.add(listOfSubjectObjects.get(i).getStatus());
		}
		return lst;
	}
}
