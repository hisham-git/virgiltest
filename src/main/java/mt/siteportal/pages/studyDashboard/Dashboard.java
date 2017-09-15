package mt.siteportal.pages.studyDashboard;

import mt.siteportal.pages.BasePage;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.tables.SubjectsTable;
import mt.siteportal.tables.Table;
import mt.siteportal.tables.VisitTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.DashboardCards;
import nz.siteportal.objects.DashboardChartCard;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class Dashboard extends BasePage{
	
	private String entitiesLocator = "//div[@class='dashboard-wrapper-col']/child::div/a[not(contains (@class, 'circle-button'))]";
		
	@FindBy(how = How.XPATH, using = "//*[@class='dashboard-tile-rows-count']")
    public WebElement Counts;

	public By subjects = new By.ByXPath("//span[text()='Subjects']/..");

	public By visits =  new By.ByXPath("//span[text()='Visits']/..");

	public By assessments = new By.ByXPath("//span[text()='Assessments']/..");
    
    //The presence of the dashboardHeadline and the viewModeButton are used to determine
    //if the Dashboard is Opened or not.
	public By dashboardHeadline = new By.ById("page-title");
    
    @FindBy(css=".dashboard-view-modes-switcher")
    public WebElement veiwModeButton;
    
	public Dashboard(WebDriver driver) {
	    super(driver);
	}
	
	public SubjectsTable openAllSubjects(){
		Log.logInfo("Opening All Subjects");
		UiHelper.click(subjects);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
	}
	
	public VisitTable openAllVisits(){
		Log.logInfo("Opening All Visits");
		UiHelper.click(visits);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), VisitTable.class);
	}
	
	public AssessmentsTable openAllAssesments(){
		Log.logInfo("Opening All Assesments");
		UiHelper.click(assessments);
		UiHelper.waitForSpinnerEnd(Browser.getDriver());
		return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
	}
	
	public Table openFilter(String name){
		Log.logInfo("Openning Filter "+ name);
		UiHelper.click(getElementByFilterName(name));
		List<String> subjLst = Filters.subjectFilters();
		List<String> visitLst = Filters.visitFilters();
		List<String> assesLst = Filters.assesmentFilters();
		if(subjLst.contains(name)){
			return PageFactory.initElements(Browser.getDriver(), SubjectsTable.class);
		}
		else if(visitLst.contains(name)){
			return PageFactory.initElements(Browser.getDriver(), VisitTable.class);
		}
		else if(assesLst.contains(name)){
			return PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		}
		return null;
		 
	}
	private List<WebElement> getEntities(){
		Log.logInfo("Getting Entities");
		return UiHelper.findElements(new By.ByXPath(entitiesLocator));
	}
	
	public Map<String,String> nameCountHolder(){
		Map<String,String> nameCount = new HashMap<String, String>();
		List<WebElement> elementList = getEntities();
		for(WebElement element: elementList){
			String elementName = element.findElement(new By.ByCssSelector(".dashboard-tile-name.ng-binding")).getText();
			String elementCount = element.findElement(new By.ByCssSelector(".dashboard-tile-rows-count")).getText();
			nameCount.put(elementName, elementCount);
		}
		return nameCount;
	}
	
	public WebElement getElementByFilterName(String name){
		Map<String,WebElement> nameElement = new HashMap<String, WebElement>();
		List<WebElement> elementList = getEntities();
		for(WebElement element: elementList){
			String elementName = element.findElement(new By.ByCssSelector(".dashboard-tile-name.ng-binding")).getText();
			nameElement.put(elementName, element);
		}
		WebElement filterElement = nameElement.get(name);
		return filterElement;
	}

	public boolean isDashboardOpened() {
		Log.logInfo("Checking if 'Dashboard' is opened...");
//		if (UiHelper.isPresent(dashboardHeadline)) {
		if (UiHelper.isVisible(dashboardHeadline)) {
			WebElement element = UiHelper.findElement(dashboardHeadline);
			try {
				boolean isDashboardOpened = element.findElement(By.xpath("h1")).getText().equals("Dashboard");
				return isDashboardOpened;
			} catch (NoSuchElementException nse) {
				Log.logInfo("No header was found for the Page...");
			}
		}
		Log.logInfo("'Dashboard' is not opened...");
		return false;
	}

	public List<String> getStatusesOfEntity(String name) {
		Log.logInfo("Getting Statuses of " + name);
		List<String> lst = TextHelper
				.getElementTextContent(UiHelper
						.findElements(new By.ByXPath(
								"//span[text()='"
										+ name
										+ "']/../../following-sibling::*/*/span[@class='dashboard-tile-name ng-binding']")));
		return lst;
	}
	
	
	//----------------NZ TEAM------------------------------------------------------------------//


	@FindBy(how = How.CSS, using = "div[data-items='selectedStudy.sites'] button")
	private WebElement siteDropdownButton;

	@FindBy(how = How.CSS, using = "div[data-items='selectedStudy.sites'] ul>li")
	private List<WebElement> sitesAvailable;

	@FindBy(xpath = "//a[@title='Show queries']")
	private WebElement showQueriesButton;

	@FindBy(css = ".dashboard-view-modes-switcher>button")
	private WebElement viewModeSwitcherButton;

	@FindBy(how = How.XPATH, using = "//div[@data-items='sitesByStudies']/button")
	private WebElement selectedStudy;

	// Return the selected Site name
	@FindBy(how = How.XPATH, using = "//div[@data-items='selectedStudy.sites']/button")
	private WebElement selectedSite;

	// List of Dashboard card objects including All Subject, All Visit, All
	// Assessment
	@FindBy(how = How.CSS, using = ".dashboard-tile")
	private List<WebElement> cardList;

	@FindBy(css = ".dashboard-wrapper-col>div.row")
	private List<WebElement> chartCardList;
	
	@FindBy(css = ".btn-wrapper > a")
	private WebElement refreshButton;
	
	@FindBy(css = "div[data-ng-if='selectedStudy'] div:nth-child(1) div.dashboard-wrapper-col")
	private WebElement subjectTiles;
	
	@FindBy(css = "div[data-ng-if='selectedStudy'] div:nth-child(2) div.dashboard-wrapper-col")
	private WebElement visitTiles;
	
	@FindBy(css = "div[data-ng-if='selectedStudy'] div:nth-child(3) div.dashboard-wrapper-col")
	private WebElement assessmentTiles;
	
	
	public boolean selectStudy(String studyName) {
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		try {
			UiHelper.waitFor(selectedStudy);
			tbf.chooseStudy(studyName);
			return true;
		} catch (NoSuchElementException ne) {
			Log.logInfo("No Study called " + studyName + " could be found.");
			return false;
		}
	}

	public boolean selectSite(String siteName) {
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		try {
			UiHelper.waitFor(selectedSite);
			tbf.chooseSite(siteName);
			return true;
		} catch (NoSuchElementException ne) {
			Log.logInfo("No Site called " + siteName + " could be found.");
			return false;
		}
	}

	public String selectRandomSite(){
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		return tbf.clickRandomSite();
	}

	public QueriesSidePanel openQueriesPanel() {
		UiHelper.checkPendingRequests(driver);
		if (UiHelper.isClickable(showQueriesButton)) {
			Log.logStep("Clicking Queries Panel Button...");
			UiHelper.click(showQueriesButton);
			Log.logInfo("Queries Panel button is clicked.");
			Log.logInfo("Opening Queries Panel...");
			return PageFactory.initElements(driver, QueriesSidePanel.class);
		} else {
			Log.logError("Queries Panel button not clickable/visible");
			return null;
		}
	}

	// Return the selected Study name
	public String getSelectedStudy() {
		String studyName = selectedStudy.getText();
		return studyName;
	}

	public String getSelectedSite() {
		String siteName = selectedSite.getText();
		return siteName;
	}

	public HashMap<String, DashboardCards> getDashboardCards() {
		UiHelper.checkPendingRequests(driver);
		HashMap<String, DashboardCards> dashboardCards = new HashMap<String, DashboardCards>();
		DashboardCards ds = null;
		int cardCount = cardList.size();
		String category = "";
		for (int i = 0; i < cardCount; i++) {
			ds = new DashboardCards(cardList.get(i));
			if (ds.getLabelTotal() != null)
				category = ds.getCardTitle();
			ds.setCardCategory(category);
			dashboardCards.put(category + "-" + ds.getCardTitle(), ds);
		}
		return dashboardCards;
	}

	// Clicks on the card based on Title(Type-FilterName)
	public DashboardList clicksOnCard(String card) {
		Log.logStep("Clicking on dashboard card " + card + "...");
		HashMap<String, DashboardCards> dashboardCards = this.getDashboardCards();
		if (dashboardCards.get(card) != null)
			dashboardCards.get(card).clicksOnACard();
		else
			Log.logInfo("No such card found.");
		Log.logInfo("Dashboard card " + card + " is clicked.");
		Log.logInfo("Redirecting to the Item List view...");
		return PageFactory.initElements(driver, DashboardList.class);
	}

	public HashMap<String, String> getListOfSitesAndSNumber() {
		HashMap<String, String> dSites = new HashMap<String, String>();
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		List<WebElement> sites = UiHelper.findElements(tbf.siteNumberList);
		for (int i = 0; i < sites.size(); i++) {
			String[] str = sites.get(i).getText().trim().split(" - ");
			if (str.length > 1) {
				dSites.put(str[0], str[1]);
			}
		}
		return dSites;
	}

	// List of Full Site String
	public List<String> getListOfSites() {
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		Log.logInfo("Number of sites found: " + tbf.getSiteCount());
		return tbf.getSites();
	}

	// Clicks on the Site Dropdown button
	public void clicksSiteDropdown() {
		UiHelper.click(siteDropdownButton);
	}

	/**
	 * Clicks on a random Card on the Dashboard
	 * 
	 * @return DashboardList
	 */
	public DashboardList clickOnRandomCard() {
		return clicksOnCard(getRandomCard());
	}

	/**
	 * Getting a random card from all the dashboard cards available
	 *
	 * @return String - the name of the card in the form of {TYPE}-{FILTER}
	 */
	public String getRandomCard(){
		Set<String> keys = getDashboardCards().keySet();
		int random_int = (new Random()).nextInt(keys.size());
		int count = 0;
		for (String key : keys) {
			if (count == random_int)
				return key;
			count++;
		}
		return "Subjects-Subjects";
	}


	/**
	 * Clicks the refresh button
	 */
	public void clickRefresh() {
		UiHelper.click(refreshButton);
	}

	/*
	 * Locators for Dashboard Cards.
	 */
	private By filterCardsLocator = By.cssSelector(".dashboard-tile.sub-dashboard-tile"),
			chartCardsLocator = By.cssSelector(".dashboard-charts-view canvas");

	/**
	 * Returns the kind of Dashboard View Mode is active. It does so by
	 * evaluating a few attributes on the Web Page. It firsts checks the icon of
	 * the View Mode Switcher button. Then it checks for the title pop-up on
	 * hover over the View Mode Switcher button Finally, checks if the Chart
	 * cards or the Filter cards are Visible. If these conditions are not
	 * coherent (e.g. The View Mode Switcher shows Cards icon but the Filter
	 * Cards are visible then the function will return "Unkown View Mode".
	 * 
	 * @return String - "Charts View Mode"/"Cards View Mode"/"Unknown View Mode"
	 */
	public String getCurrentViewMode() {
		String viewModeSwitcherTitleText = viewModeSwitcherButton.getAttribute("title");
		String viewModeSwitcherClass = viewModeSwitcherButton.getAttribute("class");
		boolean areFilterCardsVisible = UiHelper.isPresentAndVisible(filterCardsLocator);
		boolean areChartCardsVisible = UiHelper.isPresentAndVisible(chartCardsLocator);
		if (viewModeSwitcherClass.contains("charts-view-mode") && viewModeSwitcherTitleText.contains("Cards View Mode")
				&& areChartCardsVisible && !areFilterCardsVisible)
			return "Charts View Mode";
		if (viewModeSwitcherClass.contains("cards-view-mode") && viewModeSwitcherTitleText.contains("Charts View Mode")
				&& !areChartCardsVisible && areFilterCardsVisible)
			return "Cards View Mode";
		Log.logInfo(
				"Could not determine the Dashboard View Mode. The evaluated attributes were [ View Mode Switcher Button's Title Text : "
						+ viewModeSwitcherTitleText + ", View Mode Switcher Button's Class attribute : "
						+ viewModeSwitcherClass + ", Are Filter Cards Visible : " + areFilterCardsVisible
						+ ", Are Chart Cards Visible : " + areChartCardsVisible);
		return "Unkown View Mode";
	}

	/**
	 * Checks if the Dashboard's View Mode is Card View Mode or not
	 * 
	 * @return boolean true if View Mode is Cards, false otherwise
	 */
	public boolean isCardMode() {
		return getCurrentViewMode().equals("Cards View Mode");
	}

	/**
	 * Checks if the Dashboard's View Mode is Chart View Mode or not
	 * 
	 * @return boolean true if View Mode is Charts, false otherwise
	 */
	public boolean isChartsMode() {
		return getCurrentViewMode().equals("Charts View Mode");
	}

	/**
	 * Clicks the View Mode Button until the desired View Mode is achieved
	 * Limits to 3 clicks. If the View Mode does not change to the correct Mode
	 * within 3 clicks, log it
	 * 
	 * @param viewMode
	 *            String - Either "Charts View Mode" or "Cards View Mode"
	 */
	public void toggleViewModeTo(String viewMode) {
		Log.logStep("Toggling Dashboard View Mode -> " + viewMode);
		int tries = 1;
		while (!getCurrentViewMode().equals(viewMode) && tries <= 3) {
			Log.logStep("Clicking Toggle View Mode Button...");
			UiHelper.click(viewModeSwitcherButton);
			UiHelper.sleep(5000);
			tries++;
		}
		if (tries >= 4)
			Log.logInfo("The View Mode button was clicked over 3 times, but the Mode : " + viewMode
					+ " was not achieved. Current View Mode is : " + getCurrentViewMode());
	}

	/**
	 * Creates a List of Dashboard Cards objects from the Web Page and returns
	 * them.
	 * 
	 * @return List<Dashboard Cards>
	 */
	public List<DashboardCards> getCardList() {
		List<DashboardCards> answer = new ArrayList<DashboardCards>();
		String category = null;
		for (WebElement card : cardList) {
			DashboardCards temp = new DashboardCards(card);
			if (temp.getCardType().equals("Root Filter"))
				category = temp.getCardTitle();
			temp.setCardCategory(category);
			answer.add(temp);
		}
		return answer;
	}

	/**
	 * Generates a List of ChartCard Objects. Should only be used for the
	 * Dashboard Charts View
	 * 
	 * @return List<DashboardChartCard>
	 */
	private List<DashboardChartCard> getChartCardList() {
		List<DashboardChartCard> cards = new ArrayList<DashboardChartCard>();
		for (WebElement chartCard : chartCardList) {
			DashboardChartCard temp = new DashboardChartCard(chartCard);
			cards.add(temp);
		}
		return cards;
	}

	/**
	 * Takes a List of DashboardCards which contains the Counts for every filter
	 * and checks those counts against the values in the Charts Cards
	 * 
	 * @param dsCards
	 *            - List<DashboardCards> the DashboardCards that are expected in
	 *            the Charts View
	 * @return boolean true if all the Cards are present and correct, false
	 *         otherwise
	 */
	public boolean validateChartsData(List<DashboardCards> dsCards) {
		for (DashboardChartCard chartCard : getChartCardList()) {
			List<DashboardCards> cardsfilteredByCategory = filterIn(chartCard.getChartCategory(), dsCards);
			if (!chartCard.validateChartsData(cardsfilteredByCategory))
				return false;
		}
		return true;
	}

	/**
	 * For every filter in the Chart Cards, makes sure that the % values of the
	 * counts are correct
	 * 
	 * @return boolean true if all the values are correct, false otherwise
	 */
	public boolean validatePercentages() {
		for (DashboardChartCard chartCard : getChartCardList()) {
			if (!chartCard.validatePercentages())
				return false;
		}
		return true;
	}

	/**
	 * Filter function. Takes a String and a List of DashboardCards. Returns a
	 * new List that only contains the Dashboard Cards from the input List which
	 * are of the Category as specified by the category argument
	 * 
	 * @param category
	 *            - String - Category by which to filter the DashboardCards
	 * @param list
	 *            - the original List from which to filter form
	 * 
	 * @return List<DashboardCards> - the filtered List
	 */
	private List<DashboardCards> filterIn(String category, List<DashboardCards> list) {
		List<DashboardCards> answer = new ArrayList<DashboardCards>();
		for (DashboardCards card : list) {
			if (card.getCardCategory().equals(category) && !card.getCardType().equals("Root Filter"))
				answer.add(card);
		}
		return answer;
	}

	/**
	 * Gets a Random Category from the Charts View
	 * 
	 * @return String - the name of the randomly selected Category
	 */
	public String getRandomCategory() {
		List<DashboardChartCard> dsChartCards = getChartCardList();
		int index = (int)Math.floor(Math.random()*dsChartCards.size());
		return dsChartCards.get(index).getChartCategory();
	}

	/**
	 * Gets a Random Filter from the given Category
	 * @param category - the category from which to select he filter
	 * @return String - the name of the random filter
	 */
	public String getRandomFilterForCategory(String category) {
		DashboardChartCard dsChartCard = getChartCard(category);
		return dsChartCard.getRandomRow();
	}
	
	/**
	 * Returns the Chart Card WebElement Wrapper Object as specified
	 * 
	 * @param category
	 *            - String - the name of the Chart's Category
	 * @return DashboardChartCard - WebElement Page Wrapper Object for the given
	 *         Chart Category, returns null if not found
	 */
	private DashboardChartCard getChartCard(String category) {
		List<DashboardChartCard> dsChartCards = getChartCardList();
		for (DashboardChartCard dsCC : dsChartCards) {
			if (dsCC.getChartCategory().equals(category)) {
				return dsCC;
			}
		}
		Log.logInfo("Could not find Chart Card for category " + category);
		return null;
	}

	/**
	 * Returns the count for the given category's filter
	 * 
	 * @param category
	 *            - String - the Category's name
	 * @param filter
	 *            - String - the filter for the category
	 * @return int - the count for the specified filter
	 */
	public int getCountFor(String category, String filter) {
		DashboardChartCard dsChartCard = getChartCard(category);
		return dsChartCard.getCount(filter);
	}

	/**
	 * Clicks on the Filter on the Chart Card as specified
	 *
	 * @param category
	 *            - String - the Category's name
	 * @param filter
	 *            - String - the filter for the category
	 * @return - DashboardList
	 */
	public DashboardList clickOnChartCardFilter(String category, String filter) {
		DashboardChartCard dsChartCard = getChartCard(category);
		dsChartCard.clickFilterLink(filter);
		return PageFactory.initElements(driver, DashboardList.class);
	}

	/**
	 * Clicks on a random Study from the list of Studies dropdown on the Study
	 * Dashboard
	 *
	 * @return
	 */
	public String selectRandomStudy() {
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		return tbf.chooseRandomStudy();
	}

	/**
	 * Selects Site "All Sites" if there are more than one Sites available in
	 * the Sites dropdown menu. If only one Site is available, then selects that
	 * Site.
	 */
	public void selectAllSites() {
		ToolBarFull tbf = PageFactory.initElements(driver, ToolBarFull.class);
		List<String> sitesAvailable = tbf.getSites();
		if(sitesAvailable.size() > 1)
			tbf.chooseSite("All Sites");
		else
			tbf.chooseSite(sitesAvailable.get(0));
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return By - The loactor of Headline 
	 */
	public By getDashboardHeadline() {
		return dashboardHeadline;
	}
	
	/**
	 * @author Hisham
	 * 
	 * @return WebElement - The loactor of Queries Button 
	 */
	public WebElement getQueriesButton() {
		return showQueriesButton;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Calculates all filter values for given categories as Subjects/Visits/Assessments
	 * 
	 * @param String category 
	 * 			- Name of the category(i.e. Subjects/Visits/Assessments) to calculate all filter values
	 * @return Map<String, String> nameCount 
	 * 			- Filter name as key, Filter value as value
	 */
	public Map<String, String> getCategoryFiltersNameValueFor(String category) {

		String categoryFiltersLocator = "a.sub-dashboard-tile";
		String nameLocator = "span.dashboard-tile-name";
		String valueLocator = "span.dashboard-tile-rows-count";

		String elementName, elementCount;
		Map<String, String> nameCount = new HashMap<String, String>();
		List<WebElement> categoryFilterElement = null;
		
		try {
			switch (category) {
			case "Subjects":
				if (UiHelper.isClickable(subjectTiles))
					categoryFilterElement = subjectTiles.findElements(new By.ByCssSelector(categoryFiltersLocator));
				break;
			case "Visits":
				if (UiHelper.isClickable(visitTiles))
					categoryFilterElement = visitTiles.findElements(new By.ByCssSelector(categoryFiltersLocator));
				break;
			case "Assessments":
				if (UiHelper.isClickable(assessmentTiles))
					categoryFilterElement = assessmentTiles.findElements(new By.ByCssSelector(categoryFiltersLocator));
				break;
			default:
				break;
			}

			for (WebElement element : categoryFilterElement) {
				elementName = element.findElement(new By.ByCssSelector(nameLocator)).getText();
				elementCount = element.findElement(new By.ByCssSelector(valueLocator)).getText();
				nameCount.put(elementName, elementCount);
			}
			return nameCount;
		} catch (NoSuchElementException nse) {
			Log.logException(category + " block not available for interaction", nse);
			return null;
		}	
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Calculates all values for given categories as Subjects/Visits/Assessments
	 * 
	 * @param String category 
	 * 			- Name of the category(i.e. Subjects/Visits/Assessments) to calculate values
	 * @return Map<String, String> nameCount 
	 * 			- Category name as key, Category value as value
	 */
	public Map<String, String> getCategoryNameValueFor(String category) {

		String categoryNameLocator = "a.root-filter span.dashboard-tile-name";
		String categoryValueLocator = "a.root-filter span.dashboard-tile-rows-count";

		String elementName, elementCount;
		Map<String, String> nameCount = new HashMap<String, String>();

		try {
			switch (category) {
			case "Subjects":
				if (UiHelper.isClickable(subjectTiles)) {
					elementName = subjectTiles.findElement(new By.ByCssSelector(categoryNameLocator)).getText();
					elementCount = subjectTiles.findElement(new By.ByCssSelector(categoryValueLocator)).getText();
					nameCount.put(elementName, elementCount);
				}
				break;

			case "Visits":
				if (UiHelper.isClickable(visitTiles)) {
					elementName = visitTiles.findElement(new By.ByCssSelector(categoryNameLocator)).getText();
					elementCount = visitTiles.findElement(new By.ByCssSelector(categoryValueLocator)).getText();
					nameCount.put(elementName, elementCount);
				}
				break;

			case "Assessments":
				if (UiHelper.isClickable(assessmentTiles)) {
					elementName = assessmentTiles.findElement(new By.ByCssSelector(categoryNameLocator)).getText();
					elementCount = assessmentTiles.findElement(new By.ByCssSelector(categoryValueLocator)).getText();
					nameCount.put(elementName, elementCount);
				}
				break;

			default:
				break;
			}
			return nameCount;
		} catch (NoSuchElementException nse) {
			Log.logException(category + " block not available for interaction", nse);
			return null;
		}
	}
}