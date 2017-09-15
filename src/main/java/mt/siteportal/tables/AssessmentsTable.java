package mt.siteportal.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.objects.Assessment;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;

public class AssessmentsTable extends BasePage implements Table{

	@FindBy(how = How.XPATH, using = "//a[@title='Refresh']")
    private WebElement Refresh;
	private Map <String,Assessment> table = new HashMap<String, Assessment>();
	private List <Assessment> listOfAssessmentObjects = new ArrayList<Assessment>();
	private String headerLocator = "//div[@id='portal-grid-page-header']//div[contains(@class,'sortable')]/*[@title]";
	private String tableLocator = "//div[@id='portal-grid-page-content']/div[not(@style)][%s]/div";
	
	public AssessmentsTable(WebDriver driver) {
	    super(driver);
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */
	public List<Assessment> getListOfAssessmentObjects() {
		WebDriver driver = Browser.getDriver();
		DashboardList dashboardList = PageFactory.initElements(driver, DashboardList.class);
		List<WebElement> headers = UiHelper.findElements(new By.ByXPath(headerLocator));
		List<String> textFromHeaders = TextHelper.getAtributeValues(headers, "title");
		int assessmentCount = Integer
				.parseInt(TextHelper.splitParentheses(UiHelper.findElementByXpath("//h1").getText()));
		Log.logInfo("Assessments available: [" + assessmentCount + "]");
		Actions actions = new Actions(driver);
		WebElement assessmentRow = dashboardList.getFirstItemFromList();
		while (assessmentRow != null) {
			actions.moveToElement(assessmentRow).perform();
			List<WebElement> elements = assessmentRow.findElements(new By.ByXPath("div/div/label"));
			List<String> textFromElements = TextHelper.getElementTextContent(elements);
			listOfAssessmentObjects.add(new Assessment(textFromHeaders, textFromElements));
			assessmentRow = dashboardList.nextRow(assessmentRow);
		}
		return listOfAssessmentObjects;
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */
	public Map<String, Assessment> getTable() {
		List<Assessment> assessmentObjectList = getListOfAssessmentObjects();
		if (assessmentObjectList.size() > 0) {
			for (Assessment assessmentObject : assessmentObjectList) {
				table.put(TextHelper.appendValuesWithParentheses(assessmentObject.getVisit(),
						assessmentObject.getSubject()), assessmentObject);
			}
			return table;
		}
		Log.logError("No assessment found. Couldn't create table");
		return null;
	}
	
	public List<String> getListSubjectStatus() {
		List<String> lst = new ArrayList<String>();
		for(int i = 0; i< listOfAssessmentObjects.size(); i++){
			lst.add(listOfAssessmentObjects.get(i).getSubjectStatus());
		}
		return lst;
	}
	
	/**
	 * @author UTTOM
	 *  
	 * Description: Found assessment based on subject & visit
	 * name. Click on Assessment once it is found...
	 * 
	 * @param subjectName
	 * @param visitName
	 * 
	 * @return AssessmentDetails
	 * 		-  AssessmentDetails page object
	 */

	public AssessmentDetails openAssessmentDetails(String subjectName, String visitName) {
		Log.logInfo("Trying to click an Assessment with Subject Name : " + subjectName + " and Visit : " + visitName);
		DashboardList dashboardList = PageFactory.initElements(driver, DashboardList.class);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = dashboardList.getFirstItemFromList();
		while (assessment != null) {
			actions.moveToElement(assessment).perform();
			String currentRowVisitName = assessment
					.findElement(By.xpath("div[" + dashboardList.getIndexOf("Visit") + "]")).getText();
			String currentRowSubjectName = assessment
					.findElement(By.xpath("div[" + dashboardList.getIndexOf("Subject") + "]")).getText();
			if (currentRowSubjectName.equalsIgnoreCase(subjectName)
					&& currentRowVisitName.equalsIgnoreCase(visitName)) {
				Log.logStep("Found the row with the matching Assessment, clicking it....");
				UiHelper.click(assessment);
				Log.logStep("Redirecting to Assessment Details page...");
				return PageFactory.initElements(driver, AssessmentDetails.class);
			}
			assessment = dashboardList.nextRow(assessment);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Assessment with these values.");
		return null;
	}
	
	/**
	 * 
	 * @param subjectName
	 * @return
	 * 
	 * @author HISHAM
	 */
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
}
