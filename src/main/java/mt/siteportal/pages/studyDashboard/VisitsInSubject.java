package mt.siteportal.pages.studyDashboard;

import mt.siteportal.details.SubjectDetails;
import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import java.util.List;

public class VisitsInSubject extends BasePage{
	
	public VisitsInSubject(WebDriver driver) {
	    super(driver);
	    }
	private By visitRows = new By.ByCssSelector("div.portal-grid.ng-isolate-scope div.row.grid-row");
	private By addUnscheduledVisitBtn = new By.ByCssSelector("a[title='Add Unscheduled Visit'][data-ng-show='isAddUnscheduledEnable()']");
	
	private By addVisitBtn = new By.ByCssSelector("div.visit-buttons a[title='Add']:not(.ng-hide)");
	private By removeVisitBtn = new By.ByCssSelector("div.visit-buttons a[title='Remove']:not(.ng-hide)");
	private By editVisitBtn = new By.ByCssSelector("div.visit-buttons a[title='Edit']:not(.ng-hide)");

	private By viewRescheduleApmntBtn = new By.ByCssSelector("div.visit-buttons a[title='View/Reschedule appointment']:not(.ng-hide)");
	private By cancelApmntBtn = new By.ByCssSelector("div.visit-buttons a[title='Cancel appointment']:not(.ng-hide)");
	 
	/**
	 * @return count
	 * 			- int, number of visits found in VisitTable in Subject Details page
	 * 
	 * @author HISHAM
	 */
	public int getVisitCount() {
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		int count = visitList.size();
		if (count > 0)
			return count;
		Log.logWarning("No visit(s) found in visit table");
		return count;
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getAddVisitButton() {
		return UiHelper.findElement(addVisitBtn);
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getEditVisitButton() {
		return UiHelper.findElement(editVisitBtn);
	}

	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getRemoveVisitButton() {
		return UiHelper.findElement(removeVisitBtn);
	}

	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getAddUnscheduledVisitButton() {
		return UiHelper.findElement(addUnscheduledVisitBtn);
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getViewRescheduleAppointmentButton() {
		return UiHelper.findElement(viewRescheduleApmntBtn);
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElement
	 */
	public WebElement getCancelAppointmentButton() {
		return UiHelper.findElement(cancelApmntBtn);
	}

	/**
	 * </p>
	 * Description: This method selects a visit from subject details page based
	 * on position in the list.
	 * 
	 * @param index
	 *            - index position in visit list table
	 * @return - Templates page-object
	 * @author HISHAM
	 */
	public Templates selectVisit(int index) {
		Log.logInfo("Selecting visit at index: " + index);
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		if (visitList.size() > 0) {
			UiHelper.click(visitList.get(index));
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		return PageFactory.initElements(Browser.getDriver(), Templates.class);
	}
	
	/**
	 * </p>
	 * Description: This method selects a visit from subject details page based
	 * on name of the visit & index position in the list. Position is used because
	 * there may have duplicated visit name for same subject.
	 * </p>
	 * 
	 * @param visitName
	 *            - name of the visit to search for
	 * @param index
	 *            - index position in visit list table
	 * @return Templates - Templates page-object
	 * @author HISHAM
	 */
	public Templates selectVisit(String visitName, int index) {
		Log.logInfo("Selecting visit: [" + visitName + "] at index: [" + index + "]");
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		if (visitList.size() > 0) {
			String visitNameFound = visitList.get(index).findElement(new By.ByCssSelector("div.col-xs-20 > span"))
					.getText().trim();
			if (visitNameFound.equalsIgnoreCase(visitName)) {
				UiHelper.click(visitList.get(index));
			} else {
				Log.logError("Expected Visit name: [" + visitName + "] but found: [" + visitNameFound + "]");
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		return PageFactory.initElements(Browser.getDriver(), Templates.class);
	}
	
	/**
	 * </p>
	 * Description: This method selects a visit from subject details page based
	 * on visit name & current visit status. Status is used because
	 * there may have duplicated visit names but only with unique status.
	 * </p>
	 * 
	 * @param visitName
	 *			- name of the visit to search for
	 * @param visitStatus
	 * 			- current status of the visit
	 * @return Templates 
	 *			- Templates page-object
	 * @author HISHAM
	 */
	public Templates selectVisit(String visitName, String visitStatus) {
		Log.logInfo("Selecting visit: [" + visitName + "] with status: [" + visitStatus + "]");
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		if (visitList.size() > 0) {
			for (WebElement visit : visitList) {
				String visitNameFound = visit.findElement(new By.ByCssSelector("div.col-xs-20 > span")).getText()
						.trim();
				if (visitNameFound.equalsIgnoreCase(visitName)) {
					String visitStatusFound = visit
							.findElement(new By.ByCssSelector("div.extraTabletColumn.col-sm-3 > span")).getText()
							.trim();
					if (visitStatusFound.equalsIgnoreCase(visitStatus)) {
						UiHelper.click(visit);
						return PageFactory.initElements(Browser.getDriver(), Templates.class);
					}
				}
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests...");
		}
		Log.logWarning(
				"No visit found with name: [" + visitName + "] with status: [" + visitStatus + "]. Returning null...");
		return null;
	}
	
	/**
	 * </p>
	 * Description: This method selects a visit from subject details page based
	 * on name of the visit
	 * </p>
	 * 
	 * @param visitName
	 *            - name of the visit to search for

	 * @return Templates - Templates page-object
	 * @author HISHAM
	 */
	public Templates selectVisit(String visitName) {
		Log.logStep("Selecting visit by name: [" + visitName + "]");
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		if (visitList.size() > 0) {
			for (WebElement visit : visitList) {
				String visitNameFound = visit.findElement(new By.ByCssSelector("div.col-xs-20 > span")).getText()
						.trim();
				if (visitNameFound.equalsIgnoreCase(visitName)) {
//					UiHelper.scrollToElementWithJavascript(visit, Browser.getDriver());
					UiHelper.click(visit);
					return PageFactory.initElements(Browser.getDriver(), Templates.class);
				}
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		Log.logWarning("Visit: [" + visitName + "] not found in the visit list. Returning null...");
		return null;
	}
	
	public WebElement nextRow(WebElement pointer) {
		try {
			WebElement next = pointer.findElement(By.xpath("following-sibling::div"));
			return next;
		} catch (NoSuchElementException ne) {
			return null;
		}
	}
	
	public Templates selectVisitIteratively(String visitName) {
		Log.logStep("Selecting visit by name: [" + visitName + "]");
		List<WebElement> visitList = UiHelper.findElements(visitRows);
		if (visitList.size() > 0) {
			Actions actions = new Actions(Browser.getDriver());
			WebElement visit = visitList.get(0);
			while (visit != null) {
				actions.moveToElement(visit).perform();
				String currentRowVisitName = visit.findElement(new By.ByCssSelector("div.col-xs-20 > span")).getText()
						.trim();
				if (currentRowVisitName.equalsIgnoreCase(visitName)) {
					Log.logStep("Found the row with the matching Visit, clicking it....");
					UiHelper.click(visit);
					return PageFactory.initElements(Browser.getDriver(), Templates.class);
				}
				visit = nextRow(visit);
				UiHelper.fastWait(Browser.getDriver());
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		Log.logWarning("Visit: [" + visitName + "] not found in the visit list. Returning null...");
		return null;
	}
	
	public void scheduleVisit(){
		Log.logInfo("Scheduling visit for subject");
		UiHelper.click(getAddVisitButton());
	}
	
	public void viewReScheduleAppointment() {
		Log.logInfo("Viewing/ReScheduling Appiontment...");
		UiHelper.click(getViewRescheduleAppointmentButton());
	}

	public void unscheduleVisit(){
		Log.logStep("UnScheduling visit for subject");
		UiHelper.click(getRemoveVisitButton());
	}

	public void addUnscheduleVisit(String name) {
		Log.logStep("Adding unscheduled visit: [" + name + "]");
		try {
			UiHelper.fluentWaitForElementClickability(getAddUnscheduledVisitButton(), 10).click();
			WebElement visit = UiHelper
					.fluentWaitForElementVisibility(new By.ByXPath("//a[starts-with(text(),'" + name + "')]"), 10);
			Actions actions = new Actions(Browser.getDriver());
			actions.moveToElement(visit).perform();
			actions.click().perform();
		} catch (Exception e) {
			Log.logWarning("Unschedule visit button not found for clicking...");
		}
	}

	/**
	 * </p>
	 * Description: This method gets a visit status from subject details page
	 * based on name of the visit & position in the list. Position is used
	 * because there may have duplicated visit name for same subject.
	 * </p>
	 * 
	 * @param visitName
	 *            - name of the visit to search for
	 * @param index
	 *            - index position in visit list table
	 * @return visitStatus - Status found for the visit in position
	 * 
	 * @author HISHAM
	 */
	public String getVisitStatus(String visitName, int index) {
		String visitStatus = "";
		Log.logInfo("Checking status for: [" + visitName + "] at index: [" + index + "]");
		List<WebElement> visitList = UiHelper.findElements(new By.ByCssSelector("div.row.grid-row"));
		if (visitList.size() > 0) {
			String visitNameFound = visitList.get(index).findElement(new By.ByCssSelector("div.col-xs-20 > span"))
					.getText();
			if (visitNameFound.equalsIgnoreCase(visitName)) {
				visitStatus = visitList.get(index)
						.findElement(new By.ByCssSelector("div.extraTabletColumn.col-sm-3 > span")).getText();

			} else {
				Log.logError("Expected Visit name: [" + visitName + "] but found: [" + visitNameFound + "]");
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		return visitStatus;
	}

	public boolean isVisitScheduled(String visitName) {
		Log.logInfo("Checking the icon of Scheduled visit is displayed");
		return UiHelper.isPresent(new By.ByXPath(
				"//span[text()='" + visitName + "']/../preceding-sibling::*/img[@alt='scheduled visit'][not(@class)]"));
	}

	public boolean isVisitSkipped(String visitName) {
		Log.logInfo("Checking the icon of Scheduled visit is displayed");
		return UiHelper.isPresent(new By.ByXPath(
				"//span[text()='" + visitName + "']/../preceding-sibling::*/img[@alt='skipped visit'][not(@class)]"));
	}
	
	/**
	 * </p>
	 * Description: This method gets visit element from subject details page
	 * based on name of the visit & position in the list. Position is used
	 * because there may have duplicated visit name for same subject.
	 * </p>
	 * 
	 * @param visitName
	 *            - name of the visit to search for
	 * @param index
	 *            - index position in visit list table
	 * @return visitRow 
	 * 			- WebElement found for the visit row in position
	 * 
	 * @author HISHAM
	 */
	public WebElement getVisitRow(String visitName, int index) {
		WebElement visitRow = null;
		Log.logInfo("Getting element for visit: [" + visitName + "] at index: [" + index + "]");
		List<WebElement> visitList = UiHelper.findElements(new By.ByCssSelector("div.row.grid-row"));
		if (visitList.size() > 0) {
			String visitNameFound = visitList.get(index).findElement(new By.ByCssSelector("div.col-xs-20 > span"))
					.getText().trim();
			if (visitNameFound.equalsIgnoreCase(visitName)) {
				visitRow = visitList.get(index);
			} else {
				Log.logError("Expected Visit name: [" + visitName + "] but found: [" + visitNameFound + "]");
			}
		} else {
			throw new SkipException("Visit list found empty. Skipping tests");
		}
		return visitRow;
	}
}

