package nz.siteportal.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.utils.DateFormatter;

/**
 * This class is used as a helper to work with individual Queries from the Query Panel
 * Essentially, this class contains a WebElement called "panel" which is the base reference for every other element
 * inside it, and so the locators are all called from panel.findElement() instead of driver.findElement(). 
 * This class is used as a helper to work with individual Queries from the Query
 * Panel Essentially, this class contains a WebElement called "panel" which is
 * the base reference for every other element inside it, and so the locators are
 * all called from panel.findElement() instead of driver.findElement().
 * 
 * @author Syed A. Zawad
 */
public class QueryPanelItem{
	/*
	 * Since this class is not a PageObject, the @FindBy cannot be used
	 * Relying on String Locators to select WebElement using findElement(By)
	 */
	private final String siteXPATH = "div[2]/div/div[1]/div/div/div[1]/span[2]";
	private final String subjectXPATH = "div[2]/div/div[1]/div/div/div[2]/span[2]";
	private final String visitXPATH = "div[2]/div/div[1]/div/div/div[3]/span[2]";
	private final String assessmentXPATH = "div[2]/div/div[1]/div/div/div[4]/span[2]";
	private final String scoreXPATH = "div[2]/div/div[1]/div/div/div[5]/span[2]";

	private WebDriver getBrowserDriver;
	private WebElement queryItem;
	private String queryStatus;
	private String queryAge;
	private String querySite;
	private String queryText;
	public Date queryCreationTime;
	public String querySenderName;
	private String querySubject;
	private String queryVisit;
	private String queryAssessment;
	private String queryScoreItem;
	private WebElement queryMetaData;
	private WebElement queryBody;
	private List<WebElement> name_and_timeStamp;

	public QueryPanelItem(WebElement item) {
		queryItem = item;
		getBrowserDriver = Browser.getDriver();
		/*
		 * Set the Status of the Query
		 */
		String statusColorClass = queryItem.findElement(By.xpath("div[1]")).getAttribute("class");
		if (statusColorClass.contains("numgreen"))
			queryStatus = "Closed";
		else if (statusColorClass.contains("numyellow"))
			queryStatus = "Responded";
		else
			queryStatus = "Open";
		/*
		 * Set Query creation time and Sender Name
		 */
		/*WebElement name_and_timeStamp = queryItem.findElements(By.cssSelector(".caption.small.ng-binding")).get(0);
		String time_posted = name_and_timeStamp.getText().split(" \\| ")[0];
		queryCreationTime = DateFormatter.toDate(time_posted, "dd-MMM-yyyy hh:mm a");
		querySenderName = name_and_timeStamp.getText().split(" \\| ")[1];*/
		name_and_timeStamp = queryItem.findElements(By.cssSelector("div.thread-text:not(.ng-hide) label.caption.small.ng-binding"));
		String time_posted = name_and_timeStamp.get(0).getText().split(" \\| ")[0];
		queryCreationTime = DateFormatter.toDate(time_posted, "dd-MMM-yyyy hh:mm a");
		querySenderName = name_and_timeStamp.get(0).getText().split(" \\| ")[1];
		/*
		 * Set Query MetaData webElement
		 */
		queryMetaData = queryItem.findElement(By.cssSelector("div.col-xs-10"));
		/*
		 * Set query body web element
		 */
		queryBody = queryItem.findElement(By.cssSelector("div.col-xs-12"));
		/*
		 * Set Query Age
		 */
		queryAge = queryItem.findElement(By.cssSelector(".age-text.ng-binding")).getText();
		/*
		 * Set Query Site name
		 */
		querySite = queryItem.findElement(By.xpath(siteXPATH)).getText();
		/*
		 * Set Query Subject
		 */
		querySubject = queryItem.findElement(By.xpath(subjectXPATH)).getText();
		/*
		 * Set Query visit name
		 */
		queryVisit = queryItem.findElement(By.xpath(visitXPATH)).getText();
		/*
		 * Set Query Assessment name
		 */
		queryAssessment = queryItem.findElement(By.xpath(assessmentXPATH)).getText();
		/*
		 * Set Query Score/Form Item
		 */
		queryScoreItem = queryItem.findElement(By.xpath(scoreXPATH)).getText();

	}

	public QueryPanelItem(ITable table, int row) {
		/*
		 * Set Query creation time and Sender Name
		 */
		ITableMetaData data = table.getTableMetaData();
		Column[] col = null;
		try {
			col = data.getColumns();
			queryAge = table.getValue(row, col[2].getColumnName()).toString() + " days old";
			queryText = (String) table.getValue(row, col[3].getColumnName());
			queryCreationTime = (Date) table.getValue(row, col[4].getColumnName());
			querySenderName = (String) table.getValue(row, col[5].getColumnName());
			querySite = (String) table.getValue(row, col[6].getColumnName());
			querySubject = (String) table.getValue(row, col[7].getColumnName());
			queryVisit = (String) table.getValue(row, col[8].getColumnName());
			queryAssessment = (String) table.getValue(row, col[9].getColumnName());
			queryScoreItem = (String) table.getValue(row, col[10].getColumnName());
		} catch (DataSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Object comparison between database query item and UI query item
	@Override
	public boolean equals(Object o) {
		if (!QueryPanelItem.class.getCanonicalName().equals(o.getClass().getCanonicalName()))
			return false;
		QueryPanelItem temp = (QueryPanelItem) o;
		if (!this.getQueryAge().equals(temp.getQueryAge())) {
			Log.logInfo("Query Age doesn't match.Age in UI is:" + this.getQueryAge() + ", Age in DB is:"
					+ temp.getQueryAge());
			System.out.println("Query Age doesn't match.Age in UI is:" + this.getQueryAge() + ", Age in DB is:"
					+ temp.getQueryAge());
			return false;
		}
		if (!this.getFirstQueryText().equals(temp.getFirstQueryText())) {
			Log.logInfo("Query Text doesn't match. Text in UI is:" + this.getFirstQueryText()
					+ ", Text in DB is:" + temp.getFirstQueryText());
			return false;
		}
		if (!this.getQueryTimeStamp().equals(temp.getQueryTimeStamp())) {
			Log.logInfo("Query DateTime doesn't match.DateTime in UI is:" + this.getQueryTimeStamp()
					+ ", DateTime in DB is:" + temp.getQueryTimeStamp());
			return false;
		}
		if (!this.getQuerySenderName().equals(temp.getQuerySenderName())) {
			Log.logInfo("Query SenderName doesn't match. In UI is:" + this.getQuerySenderName() + ", In DB is:"
					+ temp.getQuerySenderName());
			return false;
		}
		if (!this.getQuerySite().equals(temp.getQuerySite())) {
			Log.logInfo(
					"Query Site doesn't match.In UI is:" + this.getQuerySite() + ", In DB is:" + temp.getQuerySite());
			return false;
		}
		if (!this.getQuerySubjName().equals(temp.getQuerySubjName())) {
			Log.logInfo("Query Subject doesn't match.In UI is:" + this.getQuerySubjName() + ", In DB is:"
					+ temp.getQuerySubjName());
			return false;
		}
		if (!this.getQueryAssessmentName().equals(temp.getQueryAssessmentName())) {
			Log.logInfo("Query Assessment doesn't match.In UI is:" + this.getQueryAssessmentName()
					+ ", In DB is:" + temp.getQueryAssessmentName());
			return false;
		}
		if (!this.getQueryScoreItem().equals(temp.getQueryScoreItem())) {
			Log.logInfo("Query ScoreItem doesn't match.In UI is:" + this.getQueryScoreItem() + ", In DB is:"
					+ temp.getQueryScoreItem());
			return false;
		}
		return true;
	}

	// Returns the Meta Data(age, site, SubjName, Visit, Assessment, Score)
	// of a Query
	public WebElement getQueryMetaData() {
		return queryMetaData;
	}

	// Returns the Body (TimeStamp & Text) of a Query
	public WebElement getQueryBody() {
		return queryBody;
	}

	// Clicks on the Expand button of a Query
	public void clickQueryExpandButton() {
		Log.logInfo("Expanding the Query");
		UiHelper.click(queryItem.findElement(By.cssSelector(".expander-button.arrowup.circle-button")));
	}

	// Returns the Query age
	public String getQueryAge() {
		return queryAge;
	}

	//
	// Returns the Query Site
	public String getQuerySite() {
		return querySite;
	}

	// Returns the Query Subject Name
	public String getQuerySubjName() {
		return querySubject;
	}

	// Returns the Query Visit
	public String getQueryVisit() {
		if (queryVisit == null)
			return "";
		return queryVisit;
	}

	// Returns the Query Assessment
	public String getQueryAssessmentName() {
		if (queryAssessment == null)
			return "";
		return queryAssessment;
	}

	// Returns the Query Score item
	public String getQueryScoreItem() {
		if (queryScoreItem == null)
			return "";
		return queryScoreItem;
	}

	// returns the Query Category(assessment/subject/visit/score)
	public String getQueryCategory() {
		String queryCategory = "Subject";
		if (!this.getQueryScoreItem().equals(""))
			return "Score";
		if (!this.getQueryAssessmentName().equals(""))
			return "Assessment";
		if (!this.getQueryVisit().equals(""))
			return "Visit";
		return queryCategory;
	}

	// Returns the Query Text
	public String getFirstQueryText() {
		/*
		 * Set Query Text
		 */
		return getQueryBody().findElement(By.cssSelector(".thread-text>span")).getText().trim();
	}
	// Returns the TimeStamp of a Query
	public Date getQueryTimeStamp() {
		return queryCreationTime;
	}

	// Returns the Sender Name of a Query
	public String getQuerySenderName() {
		return querySenderName;
	}

	// Clicks on the Query item
	public QueriesSidePanel clicksOnQuery() {
		Log.logInfo("Clicking on the Query...");
		UiHelper.click(queryItem);
		Log.logInfo("Clicked on the Query.");
		return PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
	}

	public String getStatus() {
		return queryStatus;
	}

	/**
	 * Gets the names of all People who have interacted with this query and is
	 * visible on the query panel list of queries.
	 * 
	 * @return Set<String> A HashSet containing the full names of the Persons
	 *         interacted for this particular query
	 */
	public Set<String> getViewableQueryResponders() {
		List<WebElement> labels = queryItem.findElements(By.cssSelector("label.caption.small.ng-binding"));
		HashSet<String> names = new HashSet<String>();
		for (WebElement label : labels) {
			if (!label.isDisplayed())
				continue;
			String[] name = label.getText().split(" \\| ");
			names.add(name[name.length - 1].trim());
		}
		return names;
	}

	/**
	 * Expands the Query and collects all the names that are visible in the
	 * query response list and the Query Poster. Normally, only the name of the
	 * Query Poster and the last responder is visible in the query panel. The
	 * expand() function reveals the names of all the other responders, allowing
	 * for the getViewableQueryResponders() to collect them
	 * 
	 * @return Set<String> A HashSet containing the full names of all the people
	 *         interacted for this particular query
	 */
	public Set<String> getAllQueryResponders() {
		expand();
		return getViewableQueryResponders();
	}

	/**
	 * Expands the Query by clicking on the expand button.
	 */
	public void expand() {
		WebElement expandBtn = getExpandButton();
		if (null != expandBtn) {
			UiHelper.scrollToElementWithJavascript(expandBtn, getBrowserDriver);
			Log.logStep("Expanding the query");
			UiHelper.click(expandBtn);
		} else {
			Log.logInfo("Query item already in exapnded view");
		}
	}

	/**
	 * Checks if the Query Panel Item is valid or not by checking for the
	 * presence and/or valid value of the Attributes - Age(x days), Site name
	 * and Subject name.
	 * 
	 * The Age must be present,
	 * 
	 * @return boolean - False if any one of the Site name, Subject name or Age
	 *         is incorrect/non-existent True if Site name, Subject name and Age
	 *         present and correct
	 */
	/*public boolean hasValidMetaData() {
		Date now = new Date();
		long time_difference = now.getTime() - queryCreationTime.getTime();
		time_difference = (long) Math.ceil(time_difference / (1000 * 60 * 60 * 24));
		if(getQueryAge() == null || getQueryAge().equals("")){
			Log.logInfo("The query's Age is missing.");
			return false;
		}
		String age_should_be1 = String.valueOf((time_difference + 1)) + " " + ((time_difference > 1) ? "days" : "day")
				+ " old";
		String age_should_be2 = String.valueOf((time_difference - 1)) + " " + ((time_difference > 1) ? "days" : "day")
				+ " old";
		String age_should_be = String.valueOf((time_difference)) + " " + ((time_difference > 1) ? "days" : "day")
				+ " old";
		if ( time_difference > 7 && (!getQueryAge().contentEquals(age_should_be) && !!getQueryAge().contentEquals(age_should_be1) && !getQueryAge().contentEquals(age_should_be2))) {
			Log.logInfo("There is a discrepancy between the displayed age and the calculated Age." + " Expected ["
					+ String.valueOf(time_difference) + " days old" + "] but got" + " [" + getQueryAge() + "]");
			return false;
		}
		if (getQuerySubjName() == null || getQuerySubjName().equals("")) {
			Log.logInfo("The Subject name could not be found.");
			return false;
		}
		if (getQuerySite() == null || getQuerySite().equals("")) {
			Log.logInfo("The Site name could not be found.");
			return false;
		}
		return true;
	}*/
	/**
	 * @author HISHAM
	 * 
	 * Improved version
	 *  
	 * Description:
	 * Checks if the Query Panel Item is valid or not by checking for the
	 * presence and/or valid value of the Attributes - Age(x days), Site name
	 * and Subject name.
	 * 
	 * The Age must be present,
	 * 
	 * @return boolean - False if any one of the Site name, Subject name or Age
	 *         is incorrect/non-existent True if Site name, Subject name and Age
	 *         present and correct
	 */
	public boolean hasValidMetaData() {
		String queryAge = getQueryAge().split(" ")[0];
		if (queryAge == null || queryAge.equals("")) {
			Log.logInfo("The query's Age is missing.");
			return false;
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		// Create a calendar object with today date.
		Calendar calendar = Calendar.getInstance();
		// Move calendar back to query age
		calendar.add(Calendar.DATE, -(Integer.valueOf(queryAge)));
		// Get current date of calendar which point to the query age
		String queryCreatedExpected = dateFormat.format(calendar.getTime());
		String queryCreatedActual = dateFormat.format(queryCreationTime);
		Log.logDebugMessage("Query creation date calculated: [" + queryCreatedExpected + "]");
		Log.logDebugMessage("Query creation date found: [" + queryCreatedActual + "]");
		if (false == queryCreatedExpected.equalsIgnoreCase(queryCreatedActual)) {
			Log.logInfo("There is a discrepancy between the displayed and calculated creation date." + " Expected ["
					+ queryCreatedExpected + "] but got [" + queryCreatedActual + "]");
			return false;
		}
		if (getQuerySubjName() == null || getQuerySubjName().equals("")) {
			Log.logInfo("The Subject name could not be found.");
			return false;
		}
		if (getQuerySite() == null || getQuerySite().equals("")) {
			Log.logInfo("The Site name could not be found.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if a particular sub-WebElement of the Query Item WebElement is
	 * present and then return it
	 * 
	 * @param locator
	 *            - By - The locator for the element in relation to the Query
	 *            Item WebElement <div>
	 * @param webElementName
	 *            - String - The name of the sub-WebElement
	 * @return - WebElement - The sub-WebElement
	 */
	public WebElement getInternalWebElement(By locator, String webElementName) {
		try {
			WebElement element = queryItem.findElement(locator);
			Log.logInfo("Looking for the WebElement " + webElementName + " : FOUND.");
			return element;
		} catch (NoSuchElementException ne) {
			Log.logInfo("Looking for the WebElement " + webElementName + " : NOT FOUND.");
			return null;
		}
	}

	/**
	 * Get the Date of the Latest Reply posted for this query
	 * 
	 * @return Date - Date of the Latest Reply of this query null - if no Reply
	 *         was found or a parse error occurred
	 */
	public Date getLatestReplyDate() {
		Log.logDebugMessage("Getting the labels of all visible Replies");
		List<WebElement> queryLabels = queryItem.findElements(By.cssSelector(".thread-text.forceright label"));
		Log.logDebugMessage("Found " + queryLabels.size() + " Replies");
		if (queryLabels.size() == 0) {
			Log.logInfo("Since there are 0 replies, no Date could be given");
			return null;
		}
		String labelText_for_latest_query = queryLabels.get(queryLabels.size() - 1).getText();
		String timePosted = labelText_for_latest_query.split(" \\| ")[0];
		Log.logInfo("The latest Reply time was " + timePosted);
		return DateFormatter.toDate(timePosted, "dd-MMM-yyyy hh:mm a");
	}

	/**
	 * Get the Name of the Poster of the Latest Reply posted for this query
	 * 
	 * @return String - Name of the Poster of the Latest Reply of this query
	 *         null - if no Reply was found
	 */
	public String getLatestReplyUsername() {
		List<WebElement> queryLabels = queryItem.findElements(By.cssSelector(".thread-text.forceright label"));
		if (queryLabels.size() == 0) {
			Log.logInfo("Since there are 0 replies, no Name could be given");
			return null;
		}
		String labelText_for_latest_query = queryLabels.get(queryLabels.size() - 1).getText();
		String [] temp = labelText_for_latest_query.split(" \\| ");
		String posterName = labelText_for_latest_query.split(" \\| ")[temp.length - 1];
		return posterName;
	}

	/**
	 * Get the Text of the Latest Reply posted for this query
	 * 
	 * @return String - Text of the Latest Reply of this query null - if no Text
	 *         was found
	 */
	public String getLatestReplyText() {
		List<WebElement> queryLabels = queryItem.findElements(By.cssSelector(".thread-text.forceright span"));
		if (queryLabels.size() == 0) {
			Log.logInfo("Since there are 0 texts, no Texts could be given");
			return null;
		}
		String text_for_latest_query = queryLabels.get(queryLabels.size() - 1).getText();
		return text_for_latest_query;
	}

	/**
	 * Returns a WebElement with the specified By locator IN RELATION to the
	 * Query Item.
	 * 
	 * @param By
	 *            - Locator in relation to the parent object Query Item (which
	 *            has the CSS selector '.row.query-item-row')
	 * @return WebElement - the resultant WebElement, or null if not found
	 */
	public WebElement getSubElement(By by) {
		try {
			return queryItem.findElement(by);
		} catch (NoSuchElementException ne) {		
			Log.logInfo("No Element with the locator '" + by.toString() + "' was found. Returning null");
			return null;
		}
	}

	/*
	 * The following functions simply return the Locators of important elements
	 */
	public WebElement getExpandButton() {
		return getSubElement(By.cssSelector(" a.expander-button.circle-button.arrowup"));
	}

	public WebElement getReplyTextBox() {
		Log.logStep("Getting the Text Area's Locator");
		return getSubElement(By.cssSelector(" div.textarea-query textarea"));
	}
	
	/**
	 * @author Hisham
	 * @return boolean
	 * 			- true if visible and clickable, false otherwise
	 */
	public boolean isReplyTextBoxVisible() {
		Log.logStep("Checking Reply Text area's visiblity");
		By replyTextBox = By.cssSelector("div.textarea-query textarea");
		if (UiHelper.isPresentAndVisible(replyTextBox)) {
			Log.logInfo("Reply Text area found visible");
			return true;
		}
		Log.logInfo("Reply Text area found not visible");
		return false;
	}

	public WebElement getReplyButton() {
		Log.logStep("Getting the Reply Button's Locator");
		return getSubElement(By.xpath(".//a[@title='Reply']"));
	}
	
	/**
	 * @author Hisham
	 * @return boolean
	 * 			- true if visible and clickable, false otherwise
	 */
	public boolean isReplyButtonVisible() {
		Log.logStep("Checking Reply Button's visiblity");
		return UiHelper.isClickable(By.xpath(".//a[@title='Reply']"));
	}

	public WebElement getCollapseButton() {
		Log.logStep("Getting the Collapse Button's Locator");
		return getSubElement(By.cssSelector(" a.expander-button.circle-button.arrowdown"));
	}

	public void collapse() {
		WebElement collapseBtn = getCollapseButton();
		if (null != collapseBtn) {
			UiHelper.scrollToElementWithJavascript(collapseBtn, getBrowserDriver);
			Log.logStep("Collapsing the query item.");
			UiHelper.click(collapseBtn);
		} else {
			Log.logInfo("Query item already in collapsed view");
		}
	}

	public void clickReply() {
		Log.logStep("Clicking Reply");
		WebElement reply_button = getReplyButton(); 
		reply_button.click();
		UiHelper.checkPendingRequests(Browser.getDriver());
	}
	public WebElement getEditButton(){
		return getSubElement(By.xpath(".//a[@title='Edit']"));
	}
	public WebElement getEditQueryTextArea() {
		return getSubElement(By.xpath(".//div[@data-ng-show='isEdited']//textarea"));
	}
	public WebElement getEditSaveButton() {
		return getSubElement(By.xpath(".//a[@title='Save']"));
	}
	public WebElement getEditCancelButton() {
		Log.logInfo("Getting the Edit Cancel Button for the Query");
		return getSubElement(By.xpath(".//a[@title='Cancel']"));
	}

	public void clickEditSaveButton() {
		Log.logStep("Clicking Edit Save button");
		if (UiHelper.scrollToElementWithJavascript(getEditSaveButton(), getBrowserDriver)) {
			getEditSaveButton().click();
			UiHelper.checkPendingRequests(getBrowserDriver);
		}
	}

	public void clickEditCancelButton() {
		Log.logStep("Clicking Edit Cancel button");
		if (UiHelper.scrollToElementWithJavascript(getEditCancelButton(), getBrowserDriver)) {
			getEditCancelButton().click();
			UiHelper.checkPendingRequests(getBrowserDriver);
		}
//		getEditCancelButton().click();
//		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public void clickEditButton() {
		Log.logStep("Clicking Edit Button");
		getEditButton().click();
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public WebElement getShowHistoryButton() {
		Log.logInfo("Getting the History Toggle Button for the Query");
		return getSubElement(By.xpath(".//a[@title='Show history']"));
	}

	public boolean isShowHistoryOn() {
		Log.logInfo("Getting the Show History Button State.");
		return getShowHistoryButton().getAttribute("class").contains("active");
	}

	public void clickShowHistoryButton() {
		Log.logStep("Clicking Show History Button");
		getShowHistoryButton().click();
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public WebElement getHistoryPanel() {
		Log.logInfo("Getting the History Panel.");
		return getQueryBody().findElement(By.cssSelector("div.history-block"));
	}

	public List<Date> getHistoryItemDates() {
		List<Date> dates = new ArrayList<Date>();
		for(WebElement historyElement_label : getHistoryPanel().findElements(By.cssSelector(".caption.small.ng-binding"))){
			String date = historyElement_label.getText().split(" \\| ")[0];
			dates.add(DateFormatter.toDate(date, "dd-MMM-yyyy hh:mm a"));
		}
		return dates;
	}

	public void clickDeleteButton() {
		Log.logStep("Clicking the Delete Button");
		getDeleteButton().click();
	}

	public WebElement getDeleteButton() {
		Log.logInfo("Getting the Delete Button for the Query");
		return getSubElement(By.xpath(".//a[@title='Delete Query']"));
	}
	public WebElement getCloseButton(){
		Log.logInfo("Getting the Close Button for the Query");
		return getSubElement(By.xpath(".//a[@title='Close Query']"));
	}

	public void clickCloseButton() {
		Log.logStep("Clicking the Close Button");
		getCloseButton().click();
	}

	public String getFirstQueryHeader() {
		return queryItem.findElements(By.cssSelector(".caption.small.ng-binding")).get(0).getText().trim();
	}

	public boolean isReplyEnabled() {
		WebElement replyB = getReplyButton();
		boolean first_condition = replyB.isEnabled();
		String disabled = replyB.getAttribute("disabled");
		boolean second_condition = !(disabled != null && disabled.equals("true"));
		System.out.println(first_condition + " " + second_condition + (disabled != null) + " "  +disabled.equals("disabled") );
		return first_condition && second_condition;
	}

	public boolean isCloseButtonDisabled(){
		WebElement closeBtn = getCloseButton();
		if(closeBtn.getAttribute("disabled") != null && closeBtn.getAttribute("disabled").equals("true")) return true;
		return !closeBtn.isEnabled();
	}
	
	public List<WebElement> getQueryNamesAndTimeStamps() {
		Log.logDebugMessage("Name & Time stamp size found: " + name_and_timeStamp.size());
		return name_and_timeStamp;
	}

}
