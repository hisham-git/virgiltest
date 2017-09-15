package nz.siteportal.pages.studydashboard.ListPages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Contains the page objects of Visit List
 * 
 * @author anoor
 *
 */
public class VisitList extends DashboardList {
	private HashMap<String, WebElement> vColLocators = new HashMap<String, WebElement>();
	private HashMap<String, Integer> vColIndex = new HashMap<String, Integer>();

	@FindBy(css = ".row.text-center.text-muted>span")
	private WebElement paginationStr;

	@FindBy(css = "#portal-grid-page-header>div>div>div>label")
	private List<WebElement> visitColumns;

	@FindBy(xpath = "//a[@title='Order by Feedback']")
	private WebElement colFeedback;

	public VisitList(WebDriver driver) {
		super(driver);
	}

	/**
	 * checks if the page is of Visit List
	 * 
	 * @return True/False
	 */
	public boolean isVisitListOpened() {
		Log.logStep("Checking if the Visit List Page is Opened....");
		if (!getListTypeName().equals("Visits")) {
			Log.logInfo("The Header text is " + getListTypeName()
					+ " and does not show Visits, indicating that this is not the Visit List Page.");
			return false;
		}
		Log.logInfo("The Visit List is Opened");
		return true;
	}

	/**
	 * clicks on the visit that matches the SVID
	 * 
	 * @param svid
	 */
	public WebElement getRowForVisit(String colName, String colValue) {
		Log.logStep("Searching the visit with:"+colName+" ="+colValue+"...");
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		if(visit == null)
			return null;
		actions.moveToElement(visit).perform();
		this.setVisitColumns();
		HashMap<String, Integer> vColNum = getVisitColIndex();
		int colNum = vColNum.get(colName);
		while (visit != null) {
			actions.moveToElement(visit).perform();
			if (visit.findElement(By.xpath("div[" + colNum + "]")).getText().equalsIgnoreCase(colValue)) {
				Log.logInfo("Found a visit with:"+colName+" ="+colValue);
				return visit;
			}
			visit = nextRow(visit);
			while (UiHelper.getNumberOfOpenAjaxConnections(driver) != 0) {
				UiHelper.sleep(100);
			}
		}
		Log.logInfo("No such visit found.");
		return null;
	}

	/**
	 * 
	 * @param visitRow
	 * @return the Visit Details page
	 */
	public VisitDetails clickAVisit(WebElement visitRow) {
		Log.logStep("Clicking on the Visit... ");
		UiHelper.click(visitRow);
		Log.logInfo("Clicked on the Visit.");
		Log.logStep("Redirecting to Visit Details page...");
		return PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
	}


	/**
	 * Creates two HashMap to keep (ColName,WebElement) and (ColName,Index)
	 */
	public void setVisitColumns() {
		int i = 0;
		for (i = 0; i < visitColumns.size(); i++) {
			vColLocators.put(visitColumns.get(i).getText().trim(), visitColumns.get(i));
			vColIndex.put(visitColumns.get(i).getText().trim(), (i + 1));
		}
		vColLocators.put("Feedback", colFeedback);
		vColIndex.put("Feedback", (i + 1));
	}

	/**
	 * 
	 * @return HashMap of (ColName,WebElement)
	 */
	public HashMap<String, WebElement> getVisitColWebElement() {
		return vColLocators;
	}

	/**
	 * 
	 * @return HashMap of (ColName,ColNumber)
	 */
	public HashMap<String, Integer> getVisitColIndex() {
		return vColIndex;
	}

	/**
	 * 
	 * @return Item Count in the list for the matching column-value
	 */
	public int getItemCountFromList(String colName, String colValue) {
		Log.logStep("Getting the item count from the list for "+colName+" = "+colValue);
		int count = 0;
		this.setVisitColumns();
		Actions actions = new Actions(Browser.getDriver());
		WebElement item = getFirstItemFromList();
		actions.moveToElement(item);
		HashMap<String, Integer> vColNum = getVisitColIndex();
		int colNum = vColNum.get(colName);
		while (item != null) {
			actions.moveToElement(item).perform();
			if (!item.findElement(By.xpath("div[" + colNum + "]")).getText().equalsIgnoreCase(colValue)) {
				return -1;
			}
			item = nextRow(item);
			while (UiHelper.getNumberOfOpenAjaxConnections(driver) != 0) {
				UiHelper.sleep(100);
			}
			count++;
		}
		Log.logInfo("Got the item count from the list for "+colName+" = "+colValue+" as:"+count);
		return count;
	}
	
	/**
	 * Iterates over the full List until a row with the Visit Name and SVID as given in the parameters can be found.
	 * If found, click the row and return the VisitDetails Page.
	 * If not found, return null
	 * 
	 * @param visitName String - the name of the visit to click
	 * @return Visit Details if the desired row is found
	 * 		   null otherwise
	 */
	public VisitDetails clickRow(String visitName, int svid) {
		Log.logInfo("Trying to click an Visit with: [" + visitName + "] and SVID : [" + svid + "]");
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		while (visit != null) {
			actions.moveToElement(visit).perform();
			String currentRowsVisitName = visit.findElement(By.xpath("div[" + getIndexOf("Visit") + "]")).getText();
			System.out.println("currentRowsVisitName: " + currentRowsVisitName);
			String currentRowsSVID = visit.findElement(By.xpath("div[" + getIndexOf("SVID") + "]")).getText();
			if (currentRowsVisitName.equalsIgnoreCase(visitName)
					&& currentRowsSVID.equalsIgnoreCase(String.valueOf(svid))) {
				Log.logStep("Found the row with the matching Visit, clicking it....");
				UiHelper.click(visit);
				Log.logStep("Redirecting to Visit Details page...");
				return PageFactory.initElements(driver, VisitDetails.class);
			}
			visit = nextRow(visit);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Visit with these values.");
		return null;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * @param String subjectName
	 * @param String visitName

	 * @return VisitDetails page object
	 */
	public VisitDetails clickRow(String subjectName, String visitName) {
		Actions actions = new Actions(Browser.getDriver());
		WebElement visit = getFirstItemFromList();
		while (visit != null) {
			actions.moveToElement(visit).perform();
			String currentRowsVisitName = visit.findElement(By.xpath("div[" + getIndexOf("Visit") + "]")).getText();
			String currentRowsSubjectName = visit.findElement(By.xpath("div[" + getIndexOf("Subject") + "]")).getText();
			if (currentRowsVisitName.equalsIgnoreCase(visitName)
					&& currentRowsSubjectName.equalsIgnoreCase(subjectName)) {
				Log.logStep("Found the row with the matching Visit, clicking it....");
				UiHelper.click(visit);
				UiHelper.fastWait(driver);
				Log.logStep("Redirecting to Visit Details page...");
				return PageFactory.initElements(driver, VisitDetails.class);
			}
			visit = nextRow(visit);
			UiHelper.fastWait(driver);
		}
		Log.logInfo("Could not find any Visit with these values.");
		return null;
	}
}
