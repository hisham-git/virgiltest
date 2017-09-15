package nz.siteportal.pages.Queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.BasePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.QueryPanelItem;

public class QueriesSidePanel extends BasePage{
	
	@FindBy(css="div.queries-dialog-content")
	private WebElement panel;
	
	@FindBy(id="queries-dialog-trigger")
	private WebElement close;
	
	@FindBy(css="div.queries-dialog-header-title")
	private WebElement queriesHeader;
	
	@FindBy(id="refresh-queries-content")
	private WebElement refreshButton;
	
	@FindBy(css=".portal-grid.query-list")
	private WebElement query_list;
	
	@FindBy(css=".queries-controls-horisontal a[title='Add Query']")
	private WebElement add_query_button;
	
//	@FindBy(xpath="//a[@title='Add Query']")
//	private WebElement add_query_button;

	@FindBy(css=".query-new-item")
	private WebElement add_query_box;
	
	private WebElement sortByButton;
	private Map<String, WebElement> checkBoxes;
	
	public QueriesSidePanel(WebDriver driver){
		super(driver);
	}
	
	public boolean isOpened() {
		try {
			if (panel.isDisplayed() && panel.isEnabled()) {
				Log.logInfo("Queries Panel is Open.");
				return true;
			}
			Log.logInfo("Queries Panel is NOT Open.");
			return false;
		} catch (NoSuchElementException ne) {
			Log.logInfo("Panel element could not be found.");
			return false;
		}
	}
	
	public boolean populateCheckBoxes(){
		sortByButton = panel.findElement(By.xpath("div[@class='checkbox-row']//button"));
		List<WebElement> checkBoxDivs = panel.findElements(By.xpath(".//div[@class='checkbox-query']"));
		checkBoxes = new HashMap<String, WebElement>();
		for(WebElement checkBoxDiv : checkBoxDivs){
			WebElement checkBox = checkBoxDiv.findElement(By.xpath("input"));
			String name = checkBoxDiv.getText();
			checkBoxes.put(name, checkBox);
		}
		return true;
	}
	
	public Map<String, WebElement> getCheckBoxes(){
		if(checkBoxes == null) populateCheckBoxes();
		return checkBoxes;
	}

	public void clickClose() {
		if( isOpened() ){	//@author: Hisham
			Log.logStep("Closing the Query Panel.");
			UiHelper.click(close);
		}
	}

	public String getHeaderText() {
		String s = queriesHeader.getText();
		Log.logStep("The Queries Panel Header Text shows : " + s);
		return s;
	}
	
	public WebElement getRefreshButton(){
		Log.logStep("Gettting Refresh Button");
		return refreshButton;
	}
	
	public int getNumberOfQueriesInList() {
		int size = query_list.findElements(By.cssSelector(".row.query-item-row")).size();
		Log.logInfo("Found [" + size + "] Query(s) in Panel");
		return size;
	}

	public ArrayList<QueryPanelItem> getQueriesFromList() {
		ArrayList<QueryPanelItem> items = new ArrayList<QueryPanelItem>();
		List<WebElement> queries = query_list.findElements(By.cssSelector(".row.query-item-row"));
		for(WebElement query : queries){
			items.add(new QueryPanelItem(query));			
		}
		return items;
	}

	public void refresh() {
		Log.logStep("Clicking Refresh Button...");
		refreshButton.click();
	}

	public WebElement getSortByButton() {
		sortByButton = panel.findElement(By.xpath(".//div[@class='checkbox-row']//button"));
		return sortByButton;
	}

	public Map<String, WebElement> getSortByLinks(){
		Map<String, WebElement> sortByLinksLocal = new HashMap<String, WebElement>();
		List<WebElement> links = panel.findElements(By.xpath(".//div[@class='checkbox-row']//ul/li"));
		for( WebElement link : links )
			sortByLinksLocal.put(link.getText(), link);
		return sortByLinksLocal;
	}
	
	public void clickSortBy(String type, boolean shouldBeIncreasing){
		Log.logInfo("Attempting to click Sort By button to show " + type + " : " + ((shouldBeIncreasing)? "Down Arrow" : "Up Arrow"));
		String currentType = getSortByButton().findElement(By.xpath("h3")).getText().trim();
		boolean increasing = getSortByButton()
				.findElement(By.xpath("h3/span[1]"))
				.isDisplayed();
		while(!currentType.equalsIgnoreCase(type) || increasing != shouldBeIncreasing ){
			if(!isSortByLinksVisible()){
				Log.logInfo("Clicking the Sort By Button");
				getSortByButton().click();
			}
			getSortByLinks().get(type).click();
			UiHelper.sleep(2000);
			currentType = getSortByButton().findElement(By.xpath("h3")).getText().trim();
			increasing = getSortByButton().findElement(By.xpath("h3/span[1]")).isDisplayed();
		}
	}
	
	public boolean isSortByLinksVisible(){
		return isVisible(panel.findElement(By.xpath(".//div[@class='checkbox-row']//ul")));
	}

	private boolean isVisible(WebElement findElement) {
		try{
			return findElement.isDisplayed();						
		}catch(NoSuchElementException ne){
			Log.logError("Cannot find the element.");
			return false;
		}
	}
	
	public boolean setCheckBoxValue(String status, boolean value){
		if(checkBoxes == null)
			populateCheckBoxes();
		Log.logInfo("Changing " + status + " to " + value);
		try{
			WebElement checkBox = checkBoxes.get(status);
			if( checkBox == null ){
				Log.logInfo("No checkbox called "+status+" was found.");
				return false;
			}
			if( checkBox.isSelected() != value ){
				checkBox.click();
				UiHelper.fastWait(driver);
			}
			return true;
		}catch(NoSuchElementException ne){
			Log.logInfo("No checkbox with "+ status+" found.");
			return false;
		}
	}
	
	public void checkAllCheckBoxesTo(boolean check_or_uncheck) {
		Set<String> keys = getCheckBoxes().keySet();
		for(String key : keys){
			setCheckBoxValue(key, check_or_uncheck);
		}
		UiHelper.checkPendingRequests(Browser.getDriver());
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Checks only primary filters: Open, Responded, Closed
	 * 
	 * @param check_or_uncheck
	 */
	public void checkPrimaryCheckBoxesTo(boolean check_or_uncheck) {
		Set<String> keys = getCheckBoxes().keySet();
		for(String key : keys){
			switch (key) {
			case "Open":
				setCheckBoxValue(key, check_or_uncheck);
				break;
			case "Responded":
				setCheckBoxValue(key, check_or_uncheck);
				break;
			case "Closed":
				setCheckBoxValue(key, check_or_uncheck);
				break;
			default:
				break;
			}
		}
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public List<WebElement> getAllQueries() {
		List<WebElement> queries = query_list.findElements(By.cssSelector(".row.query-item-row"));
		return queries;
	}

	public QueryPanelItem getRandomQuery() {
		return new QueryPanelItem(getQueryAtIndex(getRandomQueryIndex()));
	}
	
	/**
	 * Click on the Add Query Button from the Query Panel and return the PageObject for the Add Query Box that appears in the Query Panel
	 * If the Add Query Panel could not be clicked for any reason, then return null.
	 * 
	 * @return AddQueryElement if successfully able to click the Add Query Button on the Auery Panel, null otherwise 
	 */
	public AddQueryElement addQuery(){
		Log.logStep("Clicking on Add Query from the Query Panel");
		try{
			if(add_query_button.isDisplayed() && add_query_button.isEnabled()){
				add_query_button.click();
				return PageFactory.initElements(Browser.getDriver(), AddQueryElement.class);
			}
			Log.logInfo("The Add Query button was not activated");
			return null;
		}catch(NoSuchElementException ne){
			Log.logInfo("Could not find the Add Query button");
			return null;
		}
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Test if the Add Query Button is enabled
	 * 
	 * @return boolean
	 */
	public boolean isAddQueryEnabled() {
		/*if (add_query_button.getAttribute("disabled") != null
				&& add_query_button.getAttribute("disabled").equals("disabled"))
			return false;
		return add_query_button.isEnabled();*/
		
		if (UiHelper.isEnabled(add_query_button)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Test if the Add Query Button is enabled
	 * 
	 * @return boolean
	 */
	public boolean isAddQueryVisibleAndDisabled() {
		/*if (add_query_button.getAttribute("disabled") != null
				&& add_query_button.getAttribute("disabled").equals("disabled"))
			return false;
		return add_query_button.isEnabled();*/
		
		if (UiHelper.isVisible(add_query_button) && !UiHelper.isEnabled(add_query_button)) {
			return true;
		}
		return false;
	}

	/**
	 * Generates a random number ranging between 0 and the number of queries in the list
	 * 
	 * @return int
	 */
	public int getRandomQueryIndex() {
		int random_index = (int)Math.floor(Math.random()*(getNumberOfQueriesInList()));
		Log.logInfo("Generating Random Query Index -> Got " + random_index);
		return random_index;
	}
	
	/**
	 * Returns a query WebElement from the query list at the given index
	 * @param index int
	 * @return WebElement
	 */
	public WebElement getQueryAtIndex(int index){
//		return query_list.findElement(By.xpath("div/div[contains(@class, 'row query-item-row')][" + (index+1) + "]"));
		return query_list.findElements(By.cssSelector("div:not(.query-new-item).row.query-item-row")).get(index);
	}

	/**
	 * Iterates over all the queries in the Panel List and returns false if any of the Queries is of a different status than the one given
	 * 
	 * @param status String the name of the status that every one of the Queries should be
	 * @return true, if all of the Queries of the status equals the parameter value
	 * 			false otherwise
	 */
	public boolean checkIfAllQueriesStatus(String status) {
		ArrayList<QueryPanelItem> queries = getQueriesFromList();
		int index = 1;
		for (QueryPanelItem query : queries) {
			if(!query.getStatus().equals(status)){
				Log.logInfo("There was a query that has a different status. The query at index " + index + " had status " + status);
				return false;
			}
			index++;
		}
		return true;
	}
	
	/**
	 * Iterates over all the queries in the Panel List and returns false if any of the Queries is of a different status than the one given
	 * 
	 * @param status String the name of the status that every one of the Queries should be
	 * @return true, if all of the Queries of the status equals the parameter value
	 * 			false otherwise
	 */
	public boolean checkIfAllInteractionsBy(String userName) {
		ArrayList<QueryPanelItem> queries = getQueriesFromList();
		int index = 1;
		for (QueryPanelItem query : queries) {
			if(query.getViewableQueryResponders().contains(userName)) continue;
			if(!query.getAllQueryResponders().contains(userName)){
				Log.logInfo("The query does not contain any interaction from " +userName
						+ " in query # " + index + ".");
				return false;
			}
			index++;
		}
		return true;
	}

	/**
	 * Clicks the refresh Button and Waits
	 */
	public void refreshAndWait() {
		refresh();
		UiHelper.checkPendingRequests(driver);
	}

	/**
	 * A sequence of steps that adds a random new Query to the Query Panel.
	 */
	public void addNewRandomQuery() {
		AddQueryElement add_query_box = addQuery();
		if (add_query_box == null) {
			Log.logInfo(
					"Could not create a new random query as part of the pre-test configurations because the query panel was closed or the add query button was unavailable.");
			return;
		}
		add_query_box.setTextareaText("Automated Random Query.");
		add_query_box.clickCreate();
	}
	
	/**
	 * Function that simply posts a Reply to a random Query
	 */
	public void respondToRandomQuery(){
		QueryPanelItem queryItem = getRandomQuery();
		queryItem.clickQueryExpandButton();
		WebElement text_box = queryItem.getReplyTextBox();
		text_box.sendKeys("RANDOM QUERY REPLY.");
		queryItem.clickReply();
		queryItem.collapse();
	}
	/**
	 * @author HISHAM
	 * 
	 * @param queryItem
	 */
	public void respondToQuery(QueryPanelItem queryItem){
		queryItem.clickQueryExpandButton();
		WebElement text_box = queryItem.getReplyTextBox();
		text_box.sendKeys("RANDOM QUERY REPLY.");
		queryItem.clickReply();
		queryItem.collapse();
	}
}
