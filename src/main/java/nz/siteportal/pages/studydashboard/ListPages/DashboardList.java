package nz.siteportal.pages.studydashboard.ListPages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import mt.siteportal.pages.BasePage;
import mt.siteportal.pages.HomePage;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.StudyProfile;
import mt.siteportal.pages.studyDashboard.StudyRaters;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.utils.DateFormatter;

/**
 * The Parent Class that contains the common functionalities found
 * in the Subjects, Assessments and Visits Page Lists.
 * 
 * @author Syed Amer Zawad
 *
 */
public class DashboardList extends BasePage {

	/*
	 * WebElement Locators
	 */
	@FindBy(css = ".home")
	protected WebElement homeLink;

	@FindBy(xpath = "//*[@id='breadcrumbs']//a[@href='/study']")
	protected WebElement study_dashboard_link;
	
	@FindBy(xpath = "//a[@title='Raters']")
	protected WebElement raters;

	@FindBy(xpath = "//a[@title='Show queries']")
	protected WebElement queries;

	@FindBy(xpath = "//*[@id='breadcrumbs']//a[@title='Study Profile']")
	protected WebElement studyProfile;

	@FindBy(css = ".last>a")
	protected WebElement siteName;

	@FindBy(css = "#page-title>h1")
	protected WebElement headerText;

	@FindBy(css = "a.refresh-page-content.btn")
	protected WebElement refreshButton;

	@FindBy(css = ".row.grid-row.ng-scope")
	protected List<WebElement> listItems;

	@FindBy(id = "portal-grid-page-header")
	private WebElement columnHeadersContainer;
	
	
	/*
	 * The three-part String that makes up the XPATH locator for the column headers
	 */
	private final String columnHeaderXpathLocator_FirstPart = ".//label[text()='",
			columnHeaderXpathLocator_MiddlePart = "']/.. | .//div[@title='Order by ",
			columnHeaderXpathLocator_LastPart = "']/..";
	
	/*
	 * The HashMap that is dynamically generated once through the constructor.
	 * Key : String - Column Name
	 * Value : Integer - Column Index (as in the number of the column from the left, starting at 1)
	 */
	private HashMap<String, Integer> columnIndexes;
	
	/*
	 * Constructor. Populates the Index HashMap
	 */
	public DashboardList(WebDriver driver) {
		super(driver);
	}

	/**
	 * Dynamically generates a Column's corresponding index from the page.
	 * String KEY is the name of the column (columns with only icons are known through their titles)
	 * Integer VALUE is the column number which can be added to XPATH for access
	 * 
	 * @return HashMap<String, Integer>
	 */
	/*public HashMap<String, Integer> populateIndexes() {
		WebDriverWait exWait = new WebDriverWait(Browser.getDriver(), 30);
		try {
			exWait.until(ExpectedConditions.elementToBeClickable(By.id("portal-grid-page-header")));
		} catch (Exception e) {
			Log.logInfo(
					"The Assessment list's table headers could not be successfully located, returning null for column-index map...");
			return null;
		}
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		List<WebElement> rows = driver.findElements(By.cssSelector("#portal-grid-page-header div.row"));
		for (WebElement row : rows) {
			Integer index = 1;
			List<WebElement> columns = row.findElements(By.xpath("div"));
			for (WebElement column : columns) {
//				if (column.getAttribute("class").contains("sortable")) {
				String attribute = column.getAttribute("class");
				if ( attribute.contains("sortable") || attribute.contains("extraTabletColumn")) {
				
					WebElement subElement = column.findElement(By.xpath("label | div"));
					if (!(subElement.getAttribute("data-ng-click") == null
							|| subElement.getAttribute("data-ng-click").equals(""))) {
						String columnName = subElement.getText();
						Log.logDebugMessage("columnName found: " + columnName + " at index: " + index);
						if (columnName.equals("") || columnName == null) {
//							WebElement someElem = column.findElement(By.xpath(".//*[contains(@title, 'Order by')]"));
							WebElement someElem = subElement.findElement(By.xpath(".//*[contains(@title, 'Order by')]"));
							columnName = someElem.getAttribute("title").replace("Order by ", "");
						}
						indexMap.put(columnName, index);
					}
				}
				index++;
			}
		}
		return indexMap;
	}*/
	
	/**
	 * @author HISHAM
	 * 
	 * Dynamically generates a Column's corresponding index from the page.
	 * String KEY is the name of the column (columns with only icons are known through their titles)
	 * Integer VALUE is the column number which can be added to XPATH for access
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> populateIndexes() {
		HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
		WebElement listHeader = driver.findElement(By.cssSelector("#portal-grid-page-header"));
		if (null == UiHelper.fluentWaitForElementClickability(listHeader, 10)) {
			Log.logInfo(
					"The list's table headers could not be successfully located, returning null for column-index map...");
			return null;
		}
		Integer index = 1;
		List<WebElement> columns = new ArrayList<WebElement>();
		String listType = getListTypeName();

		if (listType.equalsIgnoreCase("Subjects")) {
			columns = listHeader.findElements(By.cssSelector("div.sortable:not(.group)"));
//			Log.logDebugMessage("Subjects Columns found: " + columns.size());
		} else if (listType.equalsIgnoreCase("Visits")) {
			columns = listHeader.findElements(By.cssSelector("div.sortable"));
//			Log.logDebugMessage("Visits Columns found: " + columns.size());
		} else if (listType.equalsIgnoreCase("Assessments")) {
			columns = listHeader.findElements(By.cssSelector("div.row > div:not(.header-icon)"));
//			Log.logDebugMessage("Assessments Columns found: " + columns.size());
		}
		for (WebElement column : columns) {
			String columnName;
			try {
				WebElement foundElement = column.findElement(By.cssSelector("[title^='Order by']"));
				columnName = foundElement.getAttribute("title").replace("Order by ", "");
			} catch (NoSuchElementException nse) {
//				Log.logDebugMessage("Column name not found using 'title' attribute. Trying to find using 'label' instead...");
				columnName = column.findElement(By.cssSelector("label")).getText();
			}
//			Log.logDebugMessage("columnName found: " + columnName + " at index: " + index);
			if (!indexMap.containsKey(columnName)) {
				indexMap.put(columnName, index);
				index++;
			}
		}
//		Log.logDebugMessage("indexMap size: " + indexMap.size());
		return indexMap;
	}

	/**
	 * If the Column Indexes map is not initialized, initialize it Then return
	 * the Map
	 * 
	 * @return HashMap<String, Integer>
	 */
	private HashMap<String, Integer> getColumnIndexes() {
		if (columnIndexes == null)
			columnIndexes = populateIndexes();
		return columnIndexes;
	}
	
	/**
	 * Returns the corresponding Column Name's Table Header WebElement
	 * 
	 * @param columnName String, the name of the column
	 * @return WebElement
	 */
	public WebElement getColumnHeaderFor(String columnName) {
		try {
			String locator = columnHeaderXpathLocator_FirstPart + columnName + columnHeaderXpathLocator_MiddlePart
					+ columnName + columnHeaderXpathLocator_LastPart;
			WebElement columnHeader = columnHeadersContainer.findElement(By.xpath(locator));
			return columnHeader;
		} catch (NoSuchElementException ne) {
			Log.logInfo("----> Could not locate the element " + columnName + ", returning null.");
			return null;
		}
	}
	
	/**
	 * Descrition: Works same as getColumnIndexes() but with an explicit wait
	 * for visibility. If the Column Indexes map is not initialized, initialize
	 * it Then return the Map.
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> getColumns(){
		UiHelper.waitForVisibility(By.id("portal-grid-page-header"));
		if(columnIndexes == null)
			columnIndexes = populateIndexes();
		return columnIndexes;
	}
	
	/**
	 * Returns the Integer index of the given Column Name
	 * @param column String
	 * @return Integer
	 */
	public Integer getIndexOf(String column) {
		Integer ans = getColumns().get(column);
		if (ans == null) {
			Log.logStep("Could not find index for column called " + column);
			return -1;
		}
		return ans;
	}

	public void sortColumn(String columnName, boolean shouldIncreaseDownwards) {
		Log.logStep("Sorting Column [" + columnName + "] to "
				+ ((shouldIncreaseDownwards) ? "Increasing" : "Decreasing") + " downwards...");
		int numberOfClicks = 0;
		while (!getColumnSortStatus(getColumnHeaderFor(columnName)).equals("" + shouldIncreaseDownwards)) {
			if (numberOfClicks > 4) {
				Log.logInfo(
						"The Column Header was clicked more than 4 times but still could not achieve the desired sort status of "
								+ ((shouldIncreaseDownwards) ? "Increasing" : "Decreasing")
								+ " downwards. This indicates that the column " + columnName
								+ " clicks do not perform the desired sort funcitons. Stopping clicking...");
				Log.logInfo("The list has been sorted by column:" + columnName);
				return;
			}
			UiHelper.click(getColumnHeaderFor(columnName).findElement(By.xpath("label|.//a")));
			numberOfClicks++;
		}
	}

	public boolean isSorted(String column, boolean shouldBeIncreasingDownwards) {
		String increasingOrDecreasing = ((shouldBeIncreasingDownwards) ? "Increasing" : "Decreasing") + " downwards";
		Log.logStep("Starting to check if " + column + " column data are " + increasingOrDecreasing);
		String columnXpathLocator = "div[" + getIndexOf(column) + "]";
		Actions actions = new Actions(Browser.getDriver());
		WebElement currentRow = getFirstItemFromList();
		actions.moveToElement(currentRow);
		WebElement nextRow = null;
		while (nextRow(currentRow) != null) {
			nextRow = nextRow(currentRow);
			String currentRowsColumnValue = currentRow.findElement(By.xpath(columnXpathLocator)).getText();
			String nextRowsColumnValue = nextRow.findElement(By.xpath(columnXpathLocator)).getText();
			int comparisonValue = customComparator(column, currentRowsColumnValue, nextRowsColumnValue);
			if (comparisonValue != 0) {
				boolean isIncreasingDownwards = comparisonValue < 0;
				if (isIncreasingDownwards != shouldBeIncreasingDownwards) {
					Log.logInfo("Incorrect Sort Order. Current Column:[" + currentRowsColumnValue + "], Next Column:["
							+ nextRowsColumnValue + "]. The values should be " + increasingOrDecreasing);
					return false;
				}
			}
			actions.moveToElement(nextRow).perform();
			UiHelper.fastWait(Browser.getDriver());
			currentRow = nextRow;
		}
		Log.logInfo("The " + column + " column data are found " + increasingOrDecreasing);
		return true;
	}

	/**
	 * 
	 * @return
	 */
	/*public HomePage clickHomeForAdmin() {
		Log.logStep("Clicking Home Link....");
		UiHelper.click(homeLink);
		Log.logInfo("Clicked the Home Link.");
		Log.logStep("Redirecting to Home Page...");
		return PageFactory.initElements(driver, HomePage.class);
	}

	public Dashboard clickHomeForSiteUser() {
		Log.logStep("Clicking Home Link....");
		UiHelper.click(homeLink);
		Log.logInfo("Clicked the Home Link.");
		return PageFactory.initElements(driver, Dashboard.class);
	}*/
	
	public HomePage clickHomeLink() {
		Log.logStep("Clicking Home Link....");
		UiHelper.click(homeLink);
		Log.logInfo("Clicked the Home Link.");
		Log.logStep("Redirecting to Home Page...");
		return PageFactory.initElements(driver, HomePage.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public Dashboard clickStudyLink() {
		Log.logStep("Clicking Study Link....");
		UiHelper.click(study_dashboard_link);
		Log.logInfo("Clicked the Study Link.");
		Log.logStep("Redirecting to Study Dashboard...");
		return PageFactory.initElements(driver, Dashboard.class);
	}

	/**
	 * 
	 * @return
	 */
	public String getStudyDashboardLinkText() {
		Log.logStep("Getting Study Link Text from UI..... ");
		String study_text = study_dashboard_link.getText().trim();
		Log.logInfo("Got the Study Link Text from UI as: " + study_text);
		return study_text;
	}

	/**
	 * 
	 * @return
	 */
	public StudyRaters clickRaters() {
		Log.logStep("Clicking Raters Link to open the Raters Side Panel...");
		UiHelper.click(raters);
		Log.logInfo("The Raters Link is clicked.");
		Log.logStep("Opening the Raters Side Panel...");
		return PageFactory.initElements(driver, StudyRaters.class);
	}

	/**
	 * 
	 * @return
	 */
	public StudyProfile clickStudyProfile() {
		Log.logStep("Clicking Study Profile Link to open the Study Profile Side Panel");
		UiHelper.click(studyProfile);
		Log.logInfo("The Study Profile Link is clicked.");
		Log.logStep("Opening the Study Profile Side Panel...");
		return PageFactory.initElements(driver, StudyProfile.class);
	}

	/**
	 * 
	 * @return
	 */
	public QueriesSidePanel clickQueries() {
		Log.logStep("Clicking on the Queries Link to open the Queries Side Panel");
		UiHelper.click(queries);
		Log.logInfo("The Queries Link is clicked.");
		Log.logStep("Opening the Queries Side Panel...");
		return PageFactory.initElements(driver, QueriesSidePanel.class);
	}

	/**
	 * 
	 * @return
	 */
	public Dashboard clickSite() {
		try {
			if(!siteName.isDisplayed()){
				Log.logInfo("There is no Site Link available.");
				return null;				
			}
			Log.logStep("Clicking Site Link....");
			UiHelper.click(siteName);
			Log.logInfo("Clicked on the Site Link.");
			Log.logStep("Redirecting to Study Dashboard....");
			return PageFactory.initElements(driver, Dashboard.class);
		} catch (NoSuchElementException ne) {
			Log.logInfo("There was no Site Link available in the breadcrumbs. Could not click on Site");
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getSiteName() {
		try {
			Log.logStep("Getting Site Name text from UI.... ");
			String site_name_text = siteName.getText().trim();
			Log.logInfo("Got the site name from UI: " + site_name_text);
			return site_name_text;
		} catch (NoSuchElementException ne) {
			Log.logInfo("There was no Site Link available in the breadcrumbs. Could not get Site Name.");
			return "";
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getFilter() {
		Log.logStep("Getting the Filter from UI....");
		UiHelper.waitForVisibility(headerText);
		String text = TextHelper.getPartsFromDetails(headerText.getText())[1];
		Log.logInfo("Filter from UI is:" + text);
		return text;
	}

	/**
	 * 
	 * @return
	 */
	public String getListTypeName() {
		Log.logInfo("Getting the List type name from UI....");
		UiHelper.waitForVisibility(headerText);
		String text = TextHelper.getPartsFromDetails(headerText.getText())[0];
		Log.logInfo("List type name is from UI:" + text);
		return text;
	}
	
	/**
	 * @author HISHAM
	 * @return - String List Type Name with filter
	 */	
	public String getListTypeNameWithFilter() {
		Log.logStep("Getting the List type name from UI....");
		UiHelper.waitForVisibility(headerText);
		String listType = TextHelper.getPartsFromDetails(headerText.getText())[0];
		String filter = TextHelper.getPartsFromDetails(headerText.getText())[1];
		String listTypeName = listType +": " + filter; 
		Log.logInfo("List type name is from UI: [" + listTypeName + "]");
		return listTypeName;
	}

	/**
	 * 
	 * @return
	 */
	public int getListCount() {
		Log.logStep("Getting the item count from the List Title...");
		String temp = null;
		try {
			String headerParts [] = TextHelper.getPartsFromDetails(headerText.getText());
			temp = headerParts[headerParts.length-1];
			int number = Integer.parseInt(temp);
			Log.logInfo("The item count from the List Title is:"+number);			
			return number;
		} catch (NumberFormatException ne) {
			Log.logInfo("Could not parse String [" + temp + "]. Returning List Count as 0.");
			return 0;
		} catch (IndexOutOfBoundsException ie) {
			Log.logInfo("Index out of bounds at index 3 for Array -> " + Arrays.toString(TextHelper.getPartsFromDetails(headerText.getText()))
					+ ", returning List Count as 0.");
			return 0;
		}
	}

	public void clickRefresh() {
		// NOTE : The UiHelper was intentionally not used in this method since
		// it was necessary to count the number of AJAX calls
		// after clicking the refresh button in the Test Methods.
		UiHelper.waitForVisibility(refreshButton);
		refreshButton.click();
	}

	/**
	 * Checks if the given List Page is opened
	 * It does so by first checking if the header contains the desired listType as passed as the parameter and then checks
	 * the presence of the List Table. If either one fails the function returns false.
	 * Otherwise it returns true.
	 * 
	 * @param listType String 
	 * @return boolean true if the header contains the String listType and the column headers are present,
	 * 					false otherwise.
	 */
	public boolean isOpened(String listType) {
		Log.logInfo("Checking if the " + listType + " List Page is Opened....");
		boolean isListFound = isListOpen();
		if (!isListFound) {
			Log.logInfo("The List Table is not present......");
			return false;
		}
		if (!getListTypeName().equals(listType)) {
			Log.logInfo("The Header text is " + getListTypeName() + " and does not show " + listType
					+ ", indicating that this is not the " + listType + ".");
			return false;
		}
		Log.logInfo("The " + listType + " List is Opened");
		return true;
	}
	
	/**
	 * Checks if the page opened contains the Assessment/Visit/Subjects table
	 * 
	 * @return boolean
	 */
	public boolean isListOpen(){
		try {
//			UiHelper.waitForSpinnerEnd(driver);
//			UiHelper.fluentWaitForSpinnerEnd(5);
			return columnHeadersContainer.isDisplayed();
//			return UiHelper.isClickable(columnHeadersContainer);
		} catch (NoSuchElementException ne) {
			return false;
		}
	}

	/**
	 * 
	 * @return First item in the list
	 */
	public WebElement getFirstItemFromList() {
		UiHelper.fluentWaitForElementVisibility(refreshButton, 15);
		UiHelper.scrollToElementWithJavascript(refreshButton, driver);
		UiHelper.click(refreshButton);
		UiHelper.checkPendingRequests(driver);
		
		if (listItems.size() > 0) {
			return listItems.get(0);
		}
		Log.logInfo("Number of Items was 0 so returning null.");
		return null;
	}

	/**
	 * 
	 * @param pointer
	 * @return the Next item on the list
	 */
	public WebElement nextRow(WebElement pointer) {
		try {
			WebElement next = pointer.findElement(By.xpath("following-sibling::div"));
			return next;
		} catch (NoSuchElementException ne) {
			return null;
		}
	}

	/**
	 * 
	 * @return Item Count in the list
	 */
	public int getItemCountFromList() {
		Log.logStep("Getting the item count from list...");
		int count = 0;
		Actions actions = new Actions(Browser.getDriver());
		WebElement item = getFirstItemFromList();
		actions.moveToElement(item);
		while (item != null) {
			actions.moveToElement(item).perform();
			item = nextRow(item);
			while (UiHelper.getNumberOfOpenAjaxConnections(driver) != 0) {
				UiHelper.sleep(100);
			}
			count++;
		}
		Log.logStep("The item count of the list is:" + count);
		return count;
	}

	/**
	 * Checks the sort status of the given column by looking at the image source.
	 * The column will either have no image, an image arrow pointing downwards, or an image arrow pointing upwards
	 * According to the specifications, the pointing-upwards means that the column values are sorted in decreasing order, with the highest values first.
	 * The pointing-downwards arrow indicates that the values are sorted in increasing order, with the lowest values first.
	 * 
	 * @param columnElement WebElement the column header Web Element containing the arrow images
	 * @return String - the state of the arrows, can either be "true" if the arrow is pointing down, "false" if it is pointing up or "No sort status" if no pointer images are present
	 */
	protected String getColumnSortStatus(WebElement columnElement) {
		try {
			WebElement sortIndicator = columnElement.findElement(By.xpath(".//img[contains(@class, 'sort')]"));
			if (sortIndicator.getAttribute("src").contains("down")) {
				Log.logInfo("List is now sorted by: Decreasing Downwards.");
				return "false";
			} else {
				Log.logInfo("List is now sorted by: Increasing Downwards.");
				return "true";
			}
		} catch (NoSuchElementException ne) {
			Log.logInfo("Could not find the sort button.");
			return "No Sort Status";
		}
	}

	/**
	 * Comparator function that takes the column name and two values to compare each other against to return an integer depending on the comparison
	 * Currently the value checks are very simple. The column names Date of Birth and Complete will have their String values converted to Date values and compared
	 * while the other columns will be compared as Strings.
	 * 
	 * @param typeOfData String, essentially the column name, used to distinguish between the types of data
	 * @param value1 String the first value
	 * @param value2 String the second value
	 * @return negative if value1<value2, 0 if value1 == value2, positive if value1>value2
	 */	
	private int customComparator(String typeOfData, String value1, String value2) {
		if (typeOfData.equalsIgnoreCase("Visit Start Date") || typeOfData.equalsIgnoreCase("Date of Birth")) {
			if (value1 == null || value1.equals(""))
				value1 = "01-JAN-1971";
			if (value2 == null || value2.equals(""))
				value2 = "01-JAN-1971";
			Date date1 = DateFormatter.toDate(value1, "dd-MMM-yyyy");
			Date date2 = DateFormatter.toDate(value2, "dd-MMM-yyyy");
			return date1.compareTo(date2);
		}else if(typeOfData.equalsIgnoreCase("SVID")){
			Integer integer1 = Integer.parseInt(value1);
			Integer integer2 = Integer.parseInt(value2);
			return integer1.compareTo(integer2);
		}
		return value1.compareToIgnoreCase(value2);
	}

	/**
	 * 
	 * @return Raters Count
	 */
	public int getRatersCount(){
		String countStr = raters.getText().split(" ")[1];
		countStr = TextHelper.splitParentheses(countStr);
		int count = Integer.parseInt(countStr);
		return count;
	}
	/**
	 * 
	 * @return Query Count
	 */
	public int getQueryCount(){
		UiHelper.waitForVisibility(queries);
		String countStr = queries.getText().split(" ")[1];
		countStr = TextHelper.splitParentheses(countStr);
		int count = Integer.parseInt(countStr);
		return count;
	}

	/**
	 * Returns a Random Row from the List of Assessments
	 * 
	 * @return WebElement
	 */
	public WebElement getRandomRow() {
		int index = 1;
		int random_index = (int)(Math.ceil(Math.random()* getListCount()));
		Log.logInfo("Got random row index -> " + random_index);
		Actions actions = new Actions(Browser.getDriver());
		WebElement assessment = getFirstItemFromList();
		while (assessment != null && index<random_index) {
			actions.moveToElement(assessment).perform();
			assessment = nextRow(assessment);
			index++;
		}
		return assessment;
	}
	
	/**
	 * Gets the value of a particular column from the given row as a String
	 * Returns null if the column was not found, or an empty String if no values are available
	 * 
	 * @param column The name of the column
	 * @param row The row from the List
	 * @return String
	 */
	public String getColumnData(String column, WebElement row) {
		try{
			return row.findElement(By.xpath("div[" + getIndexOf(column) + "]")).getText();
		}catch(Exception e){
			Log.logInfo("No column called "+column+" was found for the given row.");
			return null;
		}
	}

	/**
	 * Clicks the first row from the list
	 */
	public void clickFirstRow() {
		//TODO SHOULD RETURN THE APPRORIATE KIND OF DETAILS PAGE
		Log.logStep("Clicking on the first item in the table...");
		UiHelper.click(getFirstItemFromList());
	}
	
	public void clickRandomRow(){
		Log.logStep("Clicking on a random row from the list.");
		int rowCount = getListCount();
		int randomRowIndex = (int) Math.ceil(Math.random()*rowCount);
		WebElement rowElement = getRowAtIndex(randomRowIndex);
		Log.logStep("Clicking on row from the list at position: [" + (randomRowIndex) + "]");
		UiHelper.click(rowElement);
	}

	private WebElement getRowAtIndex(int randomRowIndex) {
		int count = 1;
		Actions actions = new Actions(Browser.getDriver());
		WebElement item = getFirstItemFromList();
		actions.moveToElement(item);
		while (item != null && count<randomRowIndex) {
			actions.moveToElement(item).perform();
			item = nextRow(item);
			while (UiHelper.getNumberOfOpenAjaxConnections(driver) != 0) {
				UiHelper.sleep(100);
			}
			count++;
		}
		if(item == null)
			Log.logWarning("There was no query found at index [" + randomRowIndex + "]. Returning null.");
		return item;
	}
	
}
