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

import mt.siteportal.details.VisitDetails;
import mt.siteportal.objects.Visit;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;

public class VisitTable extends BasePage implements Table{
	
	@FindBy(how = How.XPATH, using = "//a[@title='Refresh']")
    private WebElement Refresh;
	private Map <String,Visit> table = new HashMap<String, Visit>();
	private List <Visit> listOfVisitObjects = new ArrayList<Visit>();
	private String headerLocator = "//div[@id='portal-grid-page-header']//div[contains(@class,'sortable')]/*[@title]";
	private String tableLocator = "//div[@id='portal-grid-page-content']/div[%s]/div";
	
	public String getTableLocator() {
		return tableLocator;
	}

	public void setTableLocator(String tableLocator) {
		this.tableLocator = tableLocator;
	}

	public VisitTable(WebDriver driver) {
	    super(driver);
	}
	
	/*public VisitDetails openVisitDetails(String subjectName, String visitName){
		Log.logInfo("Opening "+subjectName+" Visit details");
		UiHelper.click(new By.ByXPath("//div//label[text()='"+subjectName+"']/../../preceding-sibling::div//label[text()='"+visitName+"']"));
		return PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
	}*/
	    
	/**
	 * @author HISHAM
	 * 
	 * Improved version
	 * 
	 * @param subjectName
	 * @param visitName
	 * @return
	 */
	public VisitDetails openVisitDetails(String subjectName, String visitName) {
		DashboardList dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		Log.logInfo("Trying to click a Visit with Subject name: [" + subjectName + "] and Visit: [" + visitName + "]");
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = dashboardList.getFirstItemFromList();
		while (visit != null) {
			actions.moveToElement(visit).perform();
			String currentRowVisitName = visit.findElement(By.xpath("div[" + dashboardList.getIndexOf("Visit") + "]"))
					.getText();
			String currentRowSubjectName = visit
					.findElement(By.xpath("div[" + dashboardList.getIndexOf("Subject") + "]")).getText();
			if (currentRowVisitName.equalsIgnoreCase(visitName)
					&& currentRowSubjectName.equalsIgnoreCase(subjectName)) {
				Log.logStep("Found the row with the matching Visit, clicking it....");
				UiHelper.click(visit);
				Log.logStep("Redirecting to Visit Details page...");
				return PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
			}
			visit = dashboardList.nextRow(visit);
			UiHelper.fastWait(Browser.getDriver());
		}
		Log.logInfo("Could not find any Visit with these values.");
		return null;
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */	
	public List<Visit> getListOfVisitObjects() {
		WebDriver driver = Browser.getDriver();
		DashboardList dashboardList = PageFactory.initElements(driver, DashboardList.class);
		List<WebElement> headers = UiHelper.findElements(new By.ByXPath(headerLocator));
		List<String> textFromHeaders = TextHelper.getAtributeValues(headers, "title");
		int visitCount = Integer.parseInt(TextHelper.splitParentheses(UiHelper.findElementByXpath("//h1").getText()));
		Log.logInfo("Visit(s) available: [" + visitCount + "]");
		Actions actions = new Actions(driver);
		WebElement visitRow = dashboardList.getFirstItemFromList();
		while (visitRow != null) {
			actions.moveToElement(visitRow).perform();
			List<WebElement> elements = visitRow.findElements(new By.ByXPath("div/div/label"));
			List<String> textFromElements = TextHelper.getElementTextContent(elements);
			listOfVisitObjects.add(new Visit(textFromHeaders, textFromElements));
			visitRow = dashboardList.nextRow(visitRow);
		}
		return listOfVisitObjects;
	}
	
	/**
     * @author HISHAM
     * Description: Improved version
     */
	public Map<String, Visit> getTable() {
		List<Visit> visitObjectList = getListOfVisitObjects();
		if (visitObjectList.size() > 0) {
			for (Visit visitObject : visitObjectList) {
				table.put(TextHelper.appendValuesWithParentheses(visitObject.getVisit(), visitObject.getSubject()),
						visitObject);
			}
			return table;
		}
		Log.logError("No visit found. Couldn't create table");
		return null;
	}

	public List<String> getListSubjectStatus() {
		List<String> lst = new ArrayList<String>();
		for(int i = 0; i< listOfVisitObjects.size(); i++){
			lst.add(listOfVisitObjects.get(i).getSubjectStatus());
		}
		return lst;
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
