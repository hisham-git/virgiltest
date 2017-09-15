package steps.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.ITable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.details.SubjectDetails;
import mt.siteportal.details.VisitDetails;
import mt.siteportal.pages.Header;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.pages.studyDashboard.ToolBarFull;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.TextHelper;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import mt.siteportal.utils.tools.Verify;
import nz.siteportal.objects.DeleteConfirmationPopUp;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.pages.Queries.AddQueryElement;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.DashboardList;
import nz.siteportal.utils.db.DBQueries;
import steps.Abstract.AbstractStep;
import steps.Configuration.AfterSteps;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;

/**
 * @author Abdullah Al Hisham
 */
public class QueriesSteps extends AbstractStep{

    public QueriesSteps(){
        assessmentDetails = PageFactory.initElements(Browser.getDriver(),AssessmentDetails.class);
        assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
        dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
        dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
        qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
    }
    
    CommonSteps commonSteps = new CommonSteps();
    StudyDashboardSteps studyDashboardSteps = new StudyDashboardSteps();
    
    
  //-------------------------------- Common steps --------------------------------//
  	public void checkPrimaryCheckBoxesToTrue() {
  		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
  		qSidePanel.checkPrimaryCheckBoxesTo(true);
  		qSidePanel.setCheckBoxValue("Mine", false);
  	}
    
  //-------------------------------- Steps for [QueriesSteps]--------------------------------//
  	public void verifyAllQueriesCheckBoxOnPage(String context) {
  		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
  		switch (context) {
  		case "Dashboard":
  			qSidePanel.checkPrimaryCheckBoxesTo(true);
  			verifyAllQueriesCheckBoxSelectedDisabled(context, true, true);
  			break;
  		case "SubjectList":
  			dashboardList = dashboard.clicksOnCard("Subjects-Subjects");
  			qSidePanel = dashboardList.clickQueries();
  			verifyAllQueriesCheckBoxSelectedDisabled(context, true, true);
  			qSidePanel.clickClose();
  			break;
  		case "SubjectDetails":
  			Log.logStep("Clicking first Subject from Subjects list");
  			dashboardList.getFirstItemFromList().click();
  			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
  			Log.logStep("Opening the Query Panel");
  			qSidePanel = dashboard.openQueriesPanel();
  			UiHelper.checkPendingRequests(Browser.getDriver());
  			verifyAllQueriesCheckBoxSelectedDisabled(context, false, false);
  			break;
  		default:
  			Log.logError("Context doesn't matched");
  			break;
  		}
  	}
  	
    /**
	 * Test Helper function
	 * 
	 * Objective - Create a LinkedList of a particular Query Item's Context Hierarchy depending on the
	 * 			   E.g. Given that the groupingChain shows |Site|->|Subject|->|Visit|->|Assessment|->|Score|
	 *			   will return the Query's Context Chain in the same way, namely like
	 *			   |"Query's Site"|->|"Query's Subject"|->|"Query's Visit"|->|"Query's Assessment"|->|"Query's Score"|
	 * 
	 * @param queryPanelItem QueryPanelItem - the Query Item who's Context hierarchy will be returned
	 * @param groupingChain LinkedLIst<String> - The Ordered List which provides the order with which the context hierarchy
	 * 											 is built
	 * 
	 * @return LinkedList<String> - the ordered List of Context Hierarchy
	 */
	private LinkedList<String> getGroupingHierarchy(QueryPanelItem queryPanelItem, LinkedList<String> groupingChain) {
		LinkedList<String> groupingHierarchy = new LinkedList<String>();
		for(String category : groupingChain){
			switch(category){
				case "Site" : 
					groupingHierarchy.add(queryPanelItem.getQuerySite());
					break;
				case "steps/Subject":
					groupingHierarchy.add(queryPanelItem.getQuerySubjName());
					break;
				case "Visit" : 
					groupingHierarchy.add(queryPanelItem.getQueryVisit());
					break;
				case "steps/Assessment":
					groupingHierarchy.add(queryPanelItem.getQueryAssessmentName());
					break;
				case "Score" : 
					groupingHierarchy.add(queryPanelItem.getQueryScoreItem());
					break;
				default : 
					break;
			}
		}
		return groupingHierarchy;
	}

	/**
	 * Test Helper function
	 * 
	 * Objective - Test if a list of query items are ordered in ascending or descending order
	 * 
	 * @param queries List<QueryPanelItem> - the Ordered List of Query Items to be checked
	 * @param ascending boolean - Informs if to check whether ascending or descending
	 * 
	 */
	public void checkSorting(List<QueryPanelItem> queries, boolean ascending){
		Log.logStep("Test Age Sort (" + ((ascending) ? "Earliest" : "Latest") + " first)..");
		for (int index = 1; index < queries.size(); index++) {
			Date previousItem = queries.get(index - 1).getQueryTimeStamp();
			Date thisItem = queries.get(index).getQueryTimeStamp();
			if(thisItem.compareTo(previousItem) == 0) continue;
			boolean isAscending = thisItem.compareTo(previousItem) < 0;
			HardVerify.Equals(isAscending, ascending, "The List was not sorted correctly. This date is "
					+ thisItem.toString() + " and the previous date is " + previousItem.toString());
		}
	}
	
	/**
	 * Test Helper function
	 * 
	 * Objective - Test if a list of query items are grouped from Site first to Scores last or vice versa
	 * Explanation :
	 * 
	 *  Start with an Array which will contain the LinkedList (representing Grouping Hierarchies) that have already been grouped before. It starts out as empty.
	 *  This Array is called the COMPLETED_GROUPS_LIST
	 *  Initialize a LinkedList, which is the grouping hierarchy for the first Query. This is called the CURRENT_GROUP
	 * 	For every Query ->
	 * 		1. Create a Linked List of its grouping hierarchy based on its ascending flag. This LinkedList is called the COMPARISON_GROUP
	 * 			For E.g. if the Ascending Flag is TRUE, then the Hierarchy will be |Site|->|Subject|->|Visit|->|Assessment|->|Score|
	 * 			So it will result in something like |SITE_1|->|SUBJECT_1|->|VISIT_1|->|ASSESSMENT_1|->|SCORE_1|
	 * 		2. Check if the COMPARISON_GROUP is equal to the CURRENT_GROUP
	 * 			If YES -> GOT TO THE BEGINNING OF THE LOOP 
	 * 		3. Check if the COMPARISON_GROUP is present in the COMPLETED_GROUPS_LIST Array
	 * 			If YES -> FAIL THE TEST
	 * 		4. Find the index at which the COMPARISON_GROUP and CURRENT_GROUP differ
	 * 		5. Create a new LinkedList for every Grouping Chain Item that differs and put it in the Completed Groups Hierarchy
	 * 			E.G. Say there are two LinkedLists here CURRENT_GROUP = |A|->|B|->|C|->|D|->|E|->|F|, and COMPARISON_GROUP = |A|->|B|->|C|->|J|->|K|->|L|
	 * 			The difference starts at index 3, where CURRENT_GROUP has D and COMPARISON_GROUP has J. Therefore, three new LinkedLists will be added to the COMPLETED_GROUPS_LIST
	 * 			They are :
	 * 				|A|->|B|->|C|->|D|
	 * 				|A|->|B|->|C|->|D|->|E|
	 * 				|A|->|B|->|C|->|D|->|E|->|F|
	 * 		6. Set CURRENT_GROUP = COMPARISON_GROUP
	 * 
	 * @param queries List<QueryPanelItem> - the Ordered List of Query Items to be checked
	 * @param ascending boolean - TRUE -> Site to Score
	 * 							  FALSE-> Score to Site
	 * 
	 */
	public void checkGrouping(List<QueryPanelItem> queries, boolean ascending) {
		Log.logInfo("Testing Grouping by Context (" + ((ascending) ? "Site first" : "Score first") + ")..");
		List<LinkedList<String>> completedGroups = new ArrayList<LinkedList<String>>();
		LinkedList<String> groupingChain = new LinkedList<String>();
		if (ascending) {
			groupingChain.add("Site");
			groupingChain.add("Subject");
			groupingChain.add("Visit");
			groupingChain.add("Assessment");
			groupingChain.add("Score");
		} else {
			groupingChain.add("Score");
			groupingChain.add("Assessment");
			groupingChain.add("Visit");
			groupingChain.add("Subject");
			groupingChain.add("Site");
		}
		LinkedList<String> currentGroup = getGroupingHierarchy(queries.get(0), groupingChain);
		for (int j = 0; j < queries.size(); j++) {
			LinkedList<String> group = getGroupingHierarchy(queries.get(j), groupingChain);
			if (!currentGroup.equals(group)) {
				LinkedList<String> comparer = new LinkedList<String>();
				for (String context : group) {
					comparer.add(context);
					HardVerify.False(completedGroups.contains(comparer), "The grouping for query : " + j
							+ " with group " + group + " was already completed for item " + comparer + ". completedGroups.contains(comparer)");
				}
				int i = 0;
				while (i < group.size() && i < currentGroup.size() && (currentGroup.get(i).equals(group.get(i)))) {
					i++;
				}
				comparer = new LinkedList<String>();
				int temp_index = 0;
				while (temp_index < i) {
					comparer.add(currentGroup.get(temp_index));
					temp_index++;
				}
				while (i < currentGroup.size()) {
					comparer.add(currentGroup.get(i));
					completedGroups.add(new LinkedList<String>(comparer));
					i++;
				}
				currentGroup = group;
			}
		}
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Following method returns site list which has items > 0 
	 * in given category(Subjects/Visits/Assessments) for given Study
	 * 
	 * @param studyName
	 * @param category
	 * @return
	 */
	public List<Object[]> getSitesWithCategoryItemsForStudy(String studyName, String category) {
		List<Object[]> siteList = new ArrayList<Object[]>();
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		if (commonSteps.getToStudyDashboard()) {
			dashboard.selectStudy(studyName);
			List<String> availableSites = dashboard.getListOfSites();
			for (String site : availableSites) {
				if (site.equals("All Sites"))
					continue;
				dashboard.selectSite(site);
				Map<String, String> nameCount = dashboard.getCategoryNameValueFor(category);
				if (0 == Integer.parseInt(nameCount.get(category))) {
					continue;
				} else {
					Log.logInfo("Adding site: " + site);
					siteList.add(new Object[] { site });
				}
			}
			if (0 == siteList.size())
				throw new SkipException("No site found with subjects. Skipping test...");
		} else {
			throw new SkipException("Dashborad couldn't be opened. Skipping test...");
		}
		return siteList;
	}
	
	/**
	 * @author HISHAM
	 * 
	 * The following method selects all sites for a study with more than zero queries.
	 * And returns as a list of object array for dataprovider. The list is constructed as
	 * Multivalued map (i.e. Study name as key and available sites as value )
	 * 
	 * @return List<Object[]> finalList
	 * 			- Multivalued map
	 */
	public List<Object[]> getSitesWithQueriesForAllStudies() {
		int queryCount;
		List<Object[]> finalList = new ArrayList<Object[]>();
		Multimap<String, String> studySiteMap = ArrayListMultimap.create();
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		if (commonSteps.getToStudyDashboard()) {
			toolbarFull.clickStudyDropdown();
			List<String> availableStudies = TextHelper.getElementTextContent(toolbarFull.StudyNumberList);
			toolbarFull.clickStudyDropdown();
			if (availableStudies.size() > 0) {
				for (String selectedStudy : availableStudies) {
					List<String> querySites = new ArrayList<String>();
					toolbarFull.chooseStudy(selectedStudy);

					// to refresh the query list count for every study
					qSidePanel = toolbarFull.openQueries();
					qSidePanel.checkPrimaryCheckBoxesTo(true);
					qSidePanel.clickClose();

					queryCount = Integer.parseInt(toolbarFull.getQueriesCount());
					if (queryCount < 1) {
						Log.logInfo("Study: [" + selectedStudy + "] contains 0 queries. Searching next study...");
						continue;
					}
					Log.logInfo("Study: [" + selectedStudy + "] contains [" + queryCount + "] queries.");
					List<String> availableSites = dashboard.getListOfSites();
					for (String selectedSite : availableSites) {
						if (selectedSite.equals("All Sites"))
							continue;
						toolbarFull.chooseSite(selectedSite);
						queryCount = Integer.parseInt(toolbarFull.getQueriesCount());
						if (queryCount < 1) {
							Log.logInfo("Site: [" + selectedSite + "] contains 0 queries. Searching next site...");
							continue;
						}
						querySites.add(selectedSite);
					}
					studySiteMap.putAll(selectedStudy, querySites);
					finalList.add(new Object[] { studySiteMap });
				}
				return finalList;
			} else {
				throw new SkipException("No study found. Skipping test...");
			}
		} else {
			throw new SkipException("Dashborad couldn't be opened. Skipping test...");
		}
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Following method returns site list with queries for given Study 
	 * 
	 * @param studyName
	 *
	 * @return List<Object[]>
	 * 			- site list with queries for dataprovider
	 */
	public List<Object[]> getSitesWithQueriesForStudy(String study) {
		int queryCount;
		List<Object[]> querySites = new ArrayList<Object[]>();
		toolbarFull = PageFactory.initElements(Browser.getDriver(), ToolBarFull.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
		if (commonSteps.getToStudyDashboard()) {
			dashboard.selectStudy(study);

			// to refresh the query list count for selected study
			qSidePanel = toolbarFull.openQueries();
			qSidePanel.checkPrimaryCheckBoxesTo(true);
			qSidePanel.clickClose();

			queryCount = Integer.parseInt(toolbarFull.getQueriesCount());
			if (queryCount < 1) {
				throw new SkipException("Study: [" + study + "] contains 0 query. Skipping test...");
			}

			List<String> availableSites = dashboard.getListOfSites();
			for (String site : availableSites) {
				if (site.equals("All Sites"))
					continue;
				dashboard.selectSite(site);
				queryCount = Integer.parseInt(toolbarFull.getQueriesCount());
				if (queryCount < 1) {
					Log.logInfo("Site: [" + site + "] contains 0 query. Searching queries on next site...");
					continue;
				}
				Log.logInfo("Adding site: " + site);
				querySites.add(new Object[] { site });
			}
		} else {
			throw new SkipException("Dashborad couldn't be opened. Skipping test...");
		}
		return querySites;
	}
	
	//-------------------------------- Steps for [QueryCategoryContextPanel] --------------------------------//
	private String mainWindowSubjStr;
	private String mainWindowSiteStr;
	private String mainWindowVisitStr;
	private String mainWindowAssessmentStr;
	
	private String querySubjName;
	private String querySiteName;
	private String queryVisitName;
	private String queryAssessName;
	
	private ArrayList<QueryPanelItem> queries;
	
	public void getQueryList(QueriesSidePanel panel) {
		Log.logStep("Getting the list of queries present on Query Panel...");
		queries = panel.getQueriesFromList();
	}
	
	public boolean findAndClickQueryType(String type, QueriesSidePanel panel) {
		Log.logStep("Searching for a " + type + " Query...");
		for (int i = 0; i < queries.size(); i++) {
			if (queries.get(i).getQueryCategory().equals(type)) {
				Log.logInfo("A " + type + " Query is found.");
				querySubjName = queries.get(i).getQuerySubjName();
				querySiteName = queries.get(i).getQuerySite();
				switch (type) {
				case "Subject":
					subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
					break;
				case "Visit":
					queryVisitName = queries.get(i).getQueryVisit();
					visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
					break;
				case "Assessment":
					queryVisitName = queries.get(i).getQueryVisit();
					queryAssessName = queries.get(i).getQueryAssessmentName();
					assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
					break;
				case "Score":
					queryVisitName = queries.get(i).getQueryVisit();
					queryAssessName = queries.get(i).getQueryAssessmentName();
					assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
					break;
				default:
					break;
				}
				// Clicks on the current Query
				panel = queries.get(i).clicksOnQuery();

				// Get the new list of queries
				queries = panel.getQueriesFromList();
				return true;
			}
		}
		return false;
	}
	
	// TODO; will be used later
	public void verifyExpectedQueryCount(int queryCount) {
		Log.logInfo("Checking if the Query count matches with the expected result...");
		Verify.True(queryCount == queries.size(),
				"Query count doesn't match.From UI:" + queries.size() + ",From User:" + queryCount);
		Verify.True(queryCount != queries.size(),
				"The Query count matches between UI[" + queries.size() + "] and user input[" + queryCount + "].");
	}
	
	public void verifySujectDetailsDataFor(String data) {
		switch (data) {
		case "Subject":
			// Get the Subject name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSubjStr = subjectDetails.getSubjNameFromHeader();
			mainWindowSubjStr = mainWindowSubjStr.split(" - ")[0];
			HardVerify.True(mainWindowSubjStr.equalsIgnoreCase(querySubjName),
					"Verifying the specific Subject's name...",
					"Subject name matched. Main-window[" + mainWindowSubjStr + "] and Query Panel[" + querySubjName
							+ "].",
					"Subject name doesn't match. Main-window[" + mainWindowSubjStr + "],Query Panel[" + querySubjName
							+ "]");
			break;
		case "Site":
			// Get the Site name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSiteStr = subjectDetails.getSubjectDetailsItemValue("Site");
			mainWindowSiteStr = mainWindowSiteStr.split(" - ")[0];
			HardVerify.True(mainWindowSiteStr.equalsIgnoreCase(querySiteName),
					"Verifying the specific Subject's site number...",
					"Site number matched. Main-window[" + mainWindowSiteStr + "] and Query Panel[" + querySiteName
							+ "].",
					"Site number doesn't match.Main-window[" + mainWindowSiteStr + "],Query Panel[" + querySiteName
							+ "]");
			break;
		default:
			break;
		}
	}
	
	public void verifyVisitDetailsDataFor(String data) {
		switch (data) {
		case "Site":
			// Get the Site name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSiteStr = visitDetails.getVisitDetailsItemValue("Site");
			HardVerify.True(mainWindowSiteStr.contains(querySiteName),
					"Verifying the specific Assessment's site number...", "Site numer matched. Main-window["
							+ mainWindowSiteStr + "] and Query Panel[" + querySiteName + "].",
					"Site number didn't match between loaded subject and slected Query");
			break;
		case "Subject":
			// Get the Subject name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSubjStr = visitDetails.getVisitDetailsItemValue("Subject");
			HardVerify.True(mainWindowSubjStr.contains(querySubjName),
					"Verifying the specific Assessment's site number...",
					"Subject name matched. Main-window[" + mainWindowSubjStr + "] and Query Panel[" + querySubjName
							+ "].",
					"Subject name didn't match.Main-window[" + mainWindowSubjStr + "],Query Panel[" + querySubjName
							+ "]");
			break;
		case "Visit":
			// Get the Visit name specific to this query and check if those data
			// match with the item loaded on main window
			mainWindowVisitStr = visitDetails.getVisitDetailsItemValue("Visit");
			HardVerify.True(mainWindowVisitStr.contains(queryVisitName),
					"Verifying the specific Assessment's visit name...",
					"Visit name matched. Main-window[" + mainWindowVisitStr + "] and Query Panel[" + queryVisitName
							+ "].",
					"Visit name didn't match.Main-window[" + mainWindowVisitStr + "],Query Panel[" + queryVisitName
							+ "]");
			break;
		default:
			break;
		}
	}
	
	public void verifyAssessMentDetailsDataFor(String data) {
		switch (data) {
		case "Site":
			// Get the Site name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSiteStr = assessmentDetails.getAssessmentDetailsItemValue("Site");
			HardVerify.True(mainWindowSiteStr.contains(querySiteName),
					"Verifying the specific Assessment's site number...", "Site numer matched. Main-window["
							+ mainWindowSiteStr + "] and Query Panel[" + querySiteName + "].",
					"Site number didn't match between loaded subject and slected Query");
			break;
		case "Subject":
			// Get the Subject name specific to this query and check if those
			// data match with the item loaded on main window
			mainWindowSubjStr = assessmentDetails.getAssessmentDetailsItemValue("Subject");
			HardVerify.True(mainWindowSubjStr.contains(querySubjName),
					"Verifying the specific Assessment's site number...",
					"Subject name matched. Main-window[" + mainWindowSubjStr + "] and Query Panel[" + querySubjName
							+ "].",
					"Subject name didn't match.Main-window[" + mainWindowSubjStr + "],Query Panel[" + querySubjName
							+ "]");
			break;
		case "Visit":
			// Get the Visit name specific to this query and check if those data
			// match with the item loaded on main window
			mainWindowVisitStr = assessmentDetails.getAssessmentDetailsItemValue("Visit");
			HardVerify.True(mainWindowVisitStr.contains(queryVisitName),
					"Verifying the specific Assessment's visit name...",
					"Visit name matched. Main-window[" + mainWindowVisitStr + "] and Query Panel[" + queryVisitName
							+ "].",
					"Visit name didn't match.Main-window[" + mainWindowVisitStr + "],Query Panel[" + queryVisitName
							+ "]");
			break;
		case "Assessment":
			// Get the Assessment name specific to this query and check if those
			// data
			// match with the item loaded on main window
			mainWindowAssessmentStr = assessmentDetails.getAssessmentDetailsItemValue("Assessment");
			HardVerify.True(mainWindowAssessmentStr.contains(queryAssessName),
					"Verifying the specific Assessment's name...",
					"Assessment name matched. Main-window[" + mainWindowAssessmentStr + "] and Query Panel["
							+ queryAssessName + "].",
					"Assessment name didn't match..Main-window[" + mainWindowAssessmentStr + "],Query Panel["
							+ queryAssessName + "]");
			break;
		default:
			break;
		}
	}
	
	//-------------------------------- Steps for [QueriesPanelControls] --------------------------------//
	
	public void verifyQueryPanelIsOpen() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.True(qSidePanel.isOpened(), "Verifying if the Query Side Panel is open....", "[Test Passed]",
				"The Queries Panel was not found. [Test Failed]");
	}
	
	public void verifyQueryPanelHeaderText() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		String textShouldBe = "Queries (" + qSidePanel.getNumberOfQueriesInList() + ")";
		String textIs = qSidePanel.getHeaderText();
		HardVerify.Equals(textIs, textShouldBe, "Test if the header text equals : " + textShouldBe, "[Test Passed]",
				"The Header Text is not correct. Should have been [" + textShouldBe + "] but was ["
						+ qSidePanel.getHeaderText() + "] [Test Failed]");
	}
	
	public void verifyQueryPanelIsClosed() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		qSidePanel.clickClose();
		HardVerify.False(qSidePanel.isOpened(), "Test if the Query Panel is closed....", "[Test Passed]",
				"The Queries Panel was still found to be Open. [Test Failed]");
	}
	
	public void verifyPresenceOfQueryRefreshButton() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.NotNull(qSidePanel.getRefreshButton(), "Test if the refresh button exits....", "[Test Passed]",
				"Refresh Button found to be null. [Test Failed]");
	}
	
	int numberOfQuery_before_refresh;
	Long numberOfConn_before_refresh;
	Long numberOfConn_after_refresh;
	public void storeQueryAndConnectionDataBeforeRefresh() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		numberOfQuery_before_refresh = qSidePanel.getNumberOfQueriesInList();
		numberOfConn_before_refresh = UiHelper.getNumberOfOpenAjaxConnections(Browser.getDriver());
	}
	
	public void updateQueryAndConnectionDataByRefresh() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		qSidePanel.refresh();
		numberOfConn_after_refresh = UiHelper.getNumberOfOpenAjaxConnections(Browser.getDriver());
		if (numberOfConn_before_refresh < 0 || numberOfConn_after_refresh < 0) {
			Log.logInfo(
					"There was an issue with trying to get the number of AJAX calls. Skipping the rest of the test...");
			throw new SkipException("ERROR in getting the number of AJAX calls");
		}
	}
	
	public void verifyNewAjaxCallsAfterRefresh() {
		HardVerify.NotEquals(numberOfConn_after_refresh, numberOfConn_before_refresh,
				"Test if there were any new AJAX calls after clicking the refresh button....", "[Test Passed]",
				"There are no new Ajax Calls. The page was not refreshed. [Test Failed]");
		UiHelper.checkPendingRequests(Browser.getDriver());
	}
	
	public void verifyQueryCountUnchangedAfterRefresh() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), numberOfQuery_before_refresh,
				"Test if the # of queries are correct after clicking the refresh button....", "[Test Passed]",
				"The number of Queries Changed from [" + numberOfQuery_before_refresh + "] to ["
						+ qSidePanel.getNumberOfQueriesInList() + "] [Test Failed]");
	}
	
	//-------------------------------- Steps for [QueriesPanelFilters] --------------------------------//
	
	public void verifyCheckBoxExistFor(String status) {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		if (status.equalsIgnoreCase("Mine")) {
			qSidePanel.checkAllCheckBoxesTo(false);
			qSidePanel.checkPrimaryCheckBoxesTo(true);
		} else {
			qSidePanel.checkAllCheckBoxesTo(false);
		}
		HardVerify.True(qSidePanel.setCheckBoxValue(status, true), "Test if the Checkbox for " + status + " exists....",
				"[Test Passed]", "The checkbox called " + status + " could not be found. [Test Failed]");
	}
	
	public void verifyQueryCountWithPanelHeader() {
//		checkPrimaryCheckBoxesToTrue();
		int numberOfQueriesInPanel = qSidePanel.getNumberOfQueriesInList();
		HardVerify.Equals(qSidePanel.getHeaderText(), "Queries (" + numberOfQueriesInPanel + ")",
				"Test if the header text is updated correctly.....", "[Test Passed]",
				"The header text was not right. Should have been [" + "Queries (" + numberOfQueriesInPanel + ")"
						+ "] but was [" + qSidePanel.getHeaderText() + "] [Test Failed]");
	}
	
	public void verifyEachQueryStatusIs(String status) {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.True(qSidePanel.checkIfAllQueriesStatus(status), "Test that every query is of the status " + status,
				"[Test Passed]", "One or more of the queries had a different filter. [Test Failed]");
	}
	
	public void verifyQueryFor(String adminName) {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.True(qSidePanel.checkIfAllInteractionsBy(adminName),
				"Test if the Query Panel contains queries submitted/replied by current user (" + adminName + ")...",
				"[Test Passed]",
				"One or more queries not submitted/replied by current user " + adminName + "[Test Failed]");
	}
	
	public void verifyAllQueriesCheckBoxSelectedDisabled(String context, boolean isSelected, boolean isDisabled) {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		if (isSelected == true && isDisabled == true) {
			HardVerify.Equals("true", qSidePanel.getCheckBoxes().get("All Queries").getAttribute("checked"),
					"Test if All Queries checkbox is selected for the " + context + " page....", "[Test Passed]",
					"The All Queries button is not selected on the " + context + " page [Test Failed]");
			
			HardVerify.Equals("true", qSidePanel.getCheckBoxes().get("All Queries").getAttribute("disabled"),
					"Test if All Queries checkbox is disabled for the " + context + " page....", "[Test Passed]",
					"The All Queries button is not disabled on the " + context + " page [Test Failed]");
	
		} else if (isSelected == false && isDisabled == false) {
			HardVerify.False(qSidePanel.getCheckBoxes().get("All Queries").isSelected(),
					"Test if All Queries checkbox is not selected for the " + context + " page....", "[Test Passed]",
					"The All Queries button is selected on the " + context + " page [Test Failed]");
			HardVerify.True(qSidePanel.getCheckBoxes().get("All Queries").isEnabled(),
					"Test if All Queries checkbox is enabled for the " + context + " page....", "[Test Passed]",
					"The All Queries button is disabled on the " + context + " page [Test Failed]");
		}
	}
	
	public void selectAllQueriesCheckBox() {
		checkPrimaryCheckBoxesToTrue();
		qSidePanel.setCheckBoxValue("All Queries", true);
	}
	
	//-------------------------------- Steps for [QueriesPanelSorting] --------------------------------//
	public void verifySortingControlVisibleAndEnable() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		HardVerify.True(UiHelper.isPresent(qSidePanel.getSortByButton()),
				"Verifying if the Sort By Control is present...", "[Test Passed]",
				"Sort By Button is not Visible. [Test Failed]");
		HardVerify.True(qSidePanel.getSortByButton().isEnabled(), "Verifying if the Sort By Control is enabled...",
				"[Test Passed]", "Sort By Button is Disabled. [Test Failed]");
	}
	
	public void verifySortingControlOptions() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		qSidePanel.getSortByButton().click();
		HardVerify.Equals(qSidePanel.getSortByLinks().keySet(), new HashSet<String>(Arrays.asList("Age", "Context")),
				"Test if the Sort By Options are correct.....", "[Test Passed]",
				"The Links did not match. Expected the links to contain Age and Context, but was "
						+ qSidePanel.getSortByLinks().keySet() + " [Test Failed]");
	}
	
	public void clickSortByButtonAndVerifySorting(String type, boolean sortOrder) {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		qSidePanel.clickSortBy(type, sortOrder);
		UiHelper.sleep(1000); // Give browser time to sort
		List<QueryPanelItem> queries = qSidePanel.getQueriesFromList();

		if (type.equalsIgnoreCase("Age")) {
			checkSorting(queries, sortOrder);
		} else if (type.equalsIgnoreCase("Context")) {
			checkGrouping(queries, sortOrder);
		}
	}
	
	//-------------------------------- Steps for [QuerySiteContext] --------------------------------//
	String siteName;
	public void selectSiteAndGetSiteName(String site) {
		Log.logInfo("From dashboard selecting Site: [" + site + "]");
		dashboard.selectSite(site);
		// TODO : REMOVE THIS FIX-----------
		dashboard.clickRefresh();
		// THIS FIX^^^^^^^^^^^^^^
		siteName = site.split(" - ")[0];
		Log.logInfo("Sitename extracted as: [" + siteName + "]");
	}
	
	public void openQuerySidePanel() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		if (!qSidePanel.isOpened())
			qSidePanel = dashboard.openQueriesPanel();
		qSidePanel.checkPrimaryCheckBoxesTo(true);
	}
	
	public void closeQuerySidePanel() {
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		if (qSidePanel.isOpened())
			qSidePanel.clickClose();
	}
	
	public void verifyQueryPanelHasQueryForSite() {
		ArrayList<QueryPanelItem> queries = qSidePanel.getQueriesFromList();
		if (queries.size() > 0) {
			Log.logInfo("Site: [" + siteName + "] contains [" + queries.size() + "] query(s)");
			Log.logStep(
					"Test if the Query Panel for Site [" + siteName + "] only contains the Queries for this site...");
			for (QueryPanelItem query : queries) {
				HardVerify.Equals(siteName, query.getQuerySite(), "The Query Site did not match. Expected site to be ["
						+ siteName + "] but was [" + query.getQuerySite() + "] [Test Failed]");
			}
			Log.logInfo("[Test Passed]");
			qSidePanel.clickClose();
		} else {
			throw new SkipException("Query panel contains 0 item. Skipping tests...");
		}
	}
	
	//-------------------------------- Steps for [QueryItemQueryCloseTests] --------------------------------//
	
	public void selectQueriesCheckBox(boolean value, String... statuses) {
//		qSidePanel.checkAllCheckBoxesTo(!value);
		qSidePanel.checkPrimaryCheckBoxesTo(!value);
		for (String status : statuses) {
			if (status.equalsIgnoreCase("All")) {
				qSidePanel.setCheckBoxValue("Open", value);
				qSidePanel.setCheckBoxValue("Responded", value);
				qSidePanel.setCheckBoxValue("Closed", value);
				qSidePanel.setCheckBoxValue("Mine", value);
				break;
			} else {
				qSidePanel.setCheckBoxValue(status, value);
			}
		}
		qSidePanel.refreshAndWait();
	}
	
	int queries_count_before_closing_for_closed_queries;
	int queries_count_before_closing_for_responded_queries;
	int queries_count_before_deleting_for_opened_queries;

	public void getQueryCountFor(String status) {
		selectQueriesCheckBox(true, status);
		switch (status) {
		case "Closed":
			queries_count_before_closing_for_closed_queries = qSidePanel.getNumberOfQueriesInList();
			break;
		case "Responded":
			queries_count_before_closing_for_responded_queries = qSidePanel.getNumberOfQueriesInList();
			break;
		case "Open":
			queries_count_before_deleting_for_opened_queries = qSidePanel.getNumberOfQueriesInList();
			break;
		default:
			break;
		}
	}
	
	/*public void createRespondedQueryIfUnavailable() {
		if (qSidePanel.getNumberOfQueriesInList() == 0) {
			qSidePanel.addNewRandomQuery();
			qSidePanel.respondToRandomQuery();
		}

		if (qSidePanel.getNumberOfQueriesInList() < 1)
			throw new SkipException("No Queries were available in the Query Panel for closing. Skipping test.");
	}*/
	
	public void createNewQueryIfUnavailable() {
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount == 0) {
			Log.logInfo("Query panel contains 0 query. Adding new random query...");
			qSidePanel.addNewRandomQuery();
		}
	}
	
	public boolean createRespondedQueryIfUnavailable() {
		int queryCount;
		final String reply = "RANDOM QUERY REPLY : " + UiHelper.generateRandonUUIDString();
		selectQueriesCheckBox(true, "Responded");
		queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount == 0) {
			selectQueriesCheckBox(true, "Open");
			queryCount = qSidePanel.getNumberOfQueriesInList();
			if (queryCount > 0) {
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(0));
				item.expand();
				WebElement text_box = item.getReplyTextBox();
				text_box.sendKeys(reply);
				item.clickReply();
//				item.collapse();
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 1. Returns index for first found query without history record
	 * 
	 * 2. Creates a new unedited Query if:
	 * 	a. All queries in Query panel contains history record
	 * 	or,
	 * 	b. Query panel contains 0 query
	 * 
	 * @return int index
	 */
	public int createQueryWithoutHistoryIfUnavailable() {
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		boolean queryHasHisory = true;
		int queryWithoutHistoryIndex = -1;
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) {
				Log.logInfo("Searching for query without history in queryPanel at index: [" + i + "]");
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				item.expand();
				if (false == UiHelper.isPresent(item.getShowHistoryButton())) {
					Log.logInfo("Query without history found in queryPanel at index: [" + i + "]");
					queryHasHisory = false;
					item.collapse();
					queryWithoutHistoryIndex = i;
					break;
				}
				item.collapse();
			}
			if (queryHasHisory == false) {
				Log.logInfo("Query without history record found at index: [" + queryWithoutHistoryIndex + "]");
				return queryWithoutHistoryIndex;
			} else {
				Log.logInfo("All queries in Query panel contains history record");
				Log.logInfo("Adding new random query...");
				qSidePanel.addNewRandomQuery();
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(0));
				return 0; // return index of newly created query
			}
		} else {
			Log.logInfo("Query panel contains 0 query. Adding new random query...");
			qSidePanel.addNewRandomQuery();
			item = new QueryPanelItem(qSidePanel.getQueryAtIndex(0));
		}
		return 0; // return index of newly created query
	}
	
	/*int queries_count_before_closing_for_responded_queries;
	public int getRespondedQueryCount() {
		queries_count_before_closing_for_responded_queries = qSidePanel.getNumberOfQueriesInList();
		return queries_count_before_closing_for_responded_queries;
	}*/
	
	int index;
	QueryPanelItem item;
	
	public void getRandomRespondedQueryIndex() {
		index = qSidePanel.getRandomQueryIndex();
		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(index));
//		item.expand();
		/*if ( null == item.getCloseButton() ) {
			Log.logInfo("Query item doesn't have 'Close Query' button");
			qSidePanel.respondToQuery(item);
		}*/
	}

	public void verifyDisabledCloseButtonBeforeReply() {
		item.expand();
		HardVerify.True(item.isCloseButtonDisabled(),
				"Test if the Closed Button is disabled before a reply is entered....", "[Test Passed]",
				"The Close Query Button should be disabled for before entering a Response, but was not. [Test Failed]");
	}
	
	public void verifyEnabledCloseButtonAfterReply() {
		item.getReplyTextBox().sendKeys("Some Reply");
		HardVerify.False(item.isCloseButtonDisabled(),
				"Test if the Closed Button is enabled after a reply is entered....", "[Test Passed]",
				"The Close Query Button should be enabled for after entering a Response, but was not. [Test Failed]");
	}
	
	public void verifyConfirmationPopUpWhileClosingQuery() {
		item.clickCloseButton();
		HardVerify.True(UiHelper.isPresent(By.cssSelector("div#queryConfirmation div.modal-content")),
				"Test if the Pop-Up is displayed after clicking the close button...", "[Test Passed]",
				"The Confirmation Pop Up was not displayed. [Test Failed]");
	}
	
	public void confimQueryAction(boolean isConfirmed) {
		DeleteConfirmationPopUp pop_up = new DeleteConfirmationPopUp(
				Browser.getDriver().findElement(By.cssSelector("div#queryConfirmation div.modal-content")));
		if (isConfirmed) {
			pop_up.clickYes();
		} else {
			pop_up.clickNo();
		}
	}
	
	public void verifyDecreaseInRespondedQueries() {
		HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), queries_count_before_closing_for_responded_queries - 1,
				"Verify if the number of queries with the responded status decreases after closing a Responded Query...",
				"[Test Passed]",
				"The number of queries in the responded list was not reduced by 1 after the query at : " + index
						+ " was closed. [Test Failed]");
	}
	
	public void verifyIncreaseInClosedQueries() {
		HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), queries_count_before_closing_for_closed_queries + 1,
				"Verify if the number of queries with the Closed status increases after closing a Responded Query...",
				"[Test Passed]", "The number of queries in the closed list was not increased by 1 after the query at : "
						+ index + " was closed. [Test Failed]");
	}
	
	String subjectName, visitName, assessmentName;

	public void selectRandomItemForContext(String context) {
		switch (context) {
		case "Subject":
			studyDashboardSteps.openAllSubjects();
			if (dashboardList.getListCount() > 0) {
				dashboardList.clickRandomRow();
				subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
				subjectName = subjectDetails.getSubjNameFromHeader();
			} else {
				throw new SkipException("Subject list table contains 0 item. Skipping tests...");
			}
			break;

		case "Visit":
			studyDashboardSteps.openAllVisits();
			if (dashboardList.getListCount() > 0) {
				dashboardList.clickRandomRow();
				visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
				subjectName = visitDetails.getVisitDetailsItemValue("Subject");
				visitName = visitDetails.getVisitNameFromHeader();
			} else {
				throw new SkipException("Visit list table contains 0 item. Skipping tests...");
			}
			break;

		case "Assessment":
			studyDashboardSteps.openAllAssessments();
			if (dashboardList.getListCount() > 0) {
				dashboardList.clickRandomRow();
				assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
				subjectName = assessmentDetails.getAssessmentDetailsItemValue("Subject");
				visitName = assessmentDetails.getAssessmentDetailsItemValue("Visit");
				assessmentName = assessmentDetails.getAssessmentName();
			} else {
				throw new SkipException("Assessment list table contains 0 item. Skipping tests...");
			}
			break;

		default:
			break;
		}
	}
	
	//-------------------------------- Steps for [QueryItemQueryAddTests] --------------------------------//
	public void verifyAddQueryButtonIsDisabled() {
		qSidePanel = dashboard.openQueriesPanel();
		HardVerify.False(qSidePanel.isAddQueryEnabled(),
				"Verifying if 'Add Query' Button is disabled while in Study Dashboard...", "[Test Passed]",
				"'Add Query' Button is enabled while in Study Dashboard [Test Failed]");
		qSidePanel.clickClose();
	}
	
	public void verifyDisabledAddQueryButtonInListPage(String context) {
		DashboardList dashboardList = dashboard.clicksOnCard(context + "-" + context);
		qSidePanel = dashboardList.clickQueries();
		HardVerify.False(qSidePanel.isAddQueryEnabled(),
				"Verifying 'Add Query' Button in All " + context + " List Page is disabled...", "[Test passed]",
				"'Add Query' Button in All " + context + " List Page found enabled. [Test Failed]");
		qSidePanel.clickClose();
	}
	
	String site_name, subject_name, visit_name, assessment_name, rater_name;
	public void getDetailsAndClickFirstItemFromList(String context) {
		dashboardList = PageFactory.initElements(Browser.getDriver(), DashboardList.class);
		switch (context) {
		case "Subject":
			WebElement subject = dashboardList.getFirstItemFromList();
			site_name = subject.findElement(By.xpath("div["+ dashboardList.getIndexOf("Site") + "]")).getText();
			subject_name = subject.findElement(By.xpath("div["+ dashboardList.getIndexOf("Subject") + "]")).getText();
			Log.logInfo("Site name : " + site_name + ", Subject name : " + subject_name);
			Log.logStep("Opening the first Subject's Details Page from the All Subjects List.");
			subject.click();
			subjectDetails = PageFactory.initElements(Browser.getDriver(), SubjectDetails.class);
			//TODO: Fix the Subject Details to contain an openQueriesPanel() function....
			break;
		case "Visit":
			WebElement visit = dashboardList.getFirstItemFromList();
			visit_name = visit.findElement(By.xpath("div["+ dashboardList.getIndexOf("Visit") + "]")).getText().trim();
			site_name = visit.findElement(By.xpath("div["+ dashboardList.getIndexOf("Site") + "]")).getText().trim();
			subject_name = visit.findElement(By.xpath("div["+ dashboardList.getIndexOf("Subject") + "]")).getText().trim();
			Log.logInfo("Visit name : "+visit_name + ", Site name : " + site_name + ", Subject name : " + subject_name);
			Log.logStep("Opening the first Visit's Details Page from the All Visits List.");
			visit.click();
			visitDetails = PageFactory.initElements(Browser.getDriver(), VisitDetails.class);
			break;
		case "Assessment":
			WebElement assessment = dashboardList.getFirstItemFromList();
			Log.logStep("Opening the first Assessment's Details Page from the All Assessments List.");
			visit_name = assessment.findElement(By.xpath("div["+ dashboardList.getIndexOf("Visit") + "]")).getText().trim();
			site_name = assessment.findElement(By.xpath("div["+ dashboardList.getIndexOf("Site") + "]")).getText().trim();
			subject_name = assessment.findElement(By.xpath("div["+ dashboardList.getIndexOf("Subject") + "]")).getText().trim();
			assessment_name = assessment.findElement(By.xpath("div["+ dashboardList.getIndexOf("Assessment") + "]")).getText().trim();
			rater_name = assessment.findElement(By.xpath("div["+ dashboardList.getIndexOf("Rater") + "]")).getText().trim();
			Log.logInfo("Selecting Assessment where Assessment : " + assessment_name + ", Rater : " + rater_name + ", Visit : " + visit_name + ", Subject : " + subject_name + ", Site : " + site_name);
			assessment.click();
			UiHelper.checkPendingRequests(Browser.getDriver());
	
			break;

		default:
			break;
		}
		
		
	}
	
	public void verifyEnabledAddQueryButtonInDetailsPage(String context) {
		Log.logStep("Opening the Query Panel in " + context + " details Page");
		qSidePanel = dashboard.openQueriesPanel();
		HardVerify.True(qSidePanel.isAddQueryEnabled(),
				"Verifying 'Add Query' Button in " + context + " details Page is enabled...", "[Test Passed]",
				"Add Query Button in " + context + " details Page found not enabled. [Test Failed]");
	}
	
	AddQueryElement add_query_panel;

	public void verifyPresenceOfAddQueryPanelElementsInDetailsPage(String context) {
		add_query_panel = qSidePanel.addQuery();
		HardVerify.True(add_query_panel.commonChecks(), "Verifying presence of Add Query Panel's elements...",
				"[Test Passed]", "There was a failure in the checks. [Test Failed]");
		switch (context) {
		case "Subject":
			HardVerify.False(add_query_panel.isContextDropdownPresent(), "Verifying Context Dropdown is not present...",
					"[Test Passed]", "Context Dropdown found for a "+context+" Query. [Test Failed]");
			HardVerify.False(add_query_panel.isScoreItemsDropdownPresent(),
					"Verifying Score Items Dropdown is not present...", "[Test Passed]",
					"Score Items Dropdown found for a "+context+" Query. [Test Failed]");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Subject",
					"Verifying Add Query Panel's Context is Subject...", "[Test Passed]",
					"The context level available for the "+context+" Level Query was [" + add_query_panel.getLastLevelContext()
							+ "][Test Failed]");
			break;
		case "Visit":
			HardVerify.True(add_query_panel.isContextDropdownPresent(), "Verifying Context Dropdown is present...",
					"[Test Passed]", "Context Dropdown not found for a "+context+" Query. [Test Failed]");
			HardVerify.False(add_query_panel.isScoreItemsDropdownPresent(),
					"Verifying Score Items Dropdown is not present...", "[Test Passed]",
					"Score Items Dropdown found for a "+context+" Query. [Test Failed]");
			HardVerify.True(add_query_panel.contextDropdownOptionsEqual("Subject", "Visit"),
					"Verifying Context Dropdown contains the appropriate options...", "[Test Passed]",
					"Context Dropdown Options did not match. [Test Failed]");

			Log.logInfo("Test if changes in the Context Dropdown changes the new Query's contexts appropriately...");
			add_query_panel.selectFromDropdownContext("Subject");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Subject", "Getting Subject from Context Dropdown",
					"The Last level context found Subject. [Test Passed]",
					"The Last level context was not Subject [Test Failed].");
			add_query_panel.selectFromDropdownContext("Visit");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Visit", "Getting Visit from Context Dropdown",
					"The Last level context found Visit. [Test Passed]",
					"The Last level context was not Visit. [Test Failed]");
			break;
		case "Assessment":
			HardVerify.True(add_query_panel.isContextDropdownPresent(), "Verifying Context Dropdown is present...",
					"[Test Passed]", "Context Dropdown not found for a "+context+" Query. [Test Failed]");
			HardVerify.False(add_query_panel.isScoreItemsDropdownPresent(),
					"Verifying Score Items Dropdown is not present...", "[Test Passed]",
					"Score Items Dropdown found for a "+context+" Query. [Test Failed]");
			HardVerify.True(add_query_panel.contextDropdownOptionsEqual("Subject", "Visit", "Assessment", "Score"),
					"Verifying Context Dropdown contains the appropriate options...", "[Test Passed]",
					"Context Dropdown Options did not match. [Test Failed]");
			
			Log.logInfo("Test if changes in the Context Dropdown changes the new Query's contexts appropriately...");
			add_query_panel.selectFromDropdownContext("Subject");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Subject", "Getting Subject from Context Dropdown",
					"The Last level context found Subject. [Test Passed]",
					"The Last level context was not Subject [Test Failed].");
			add_query_panel.selectFromDropdownContext("Visit");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Visit", "Getting Visit from Context Dropdown",
					"The Last level context found Visit. [Test Passed]",
					"The Last level context was not Visit. [Test Failed]");
			add_query_panel.selectFromDropdownContext("Assessment");
			HardVerify.Equals(add_query_panel.getLastLevelContext(), "Assessment", "Getting Assessment from Context Dropdown",
					"The Last level context found Assessment. [Test Passed]",
					"The Last level context was not Assessment. [Test Failed]");
			/*
			UNCOMMENT THIS FOR SCORE ITEMS CHECKS
			add_query_panel.selectFromDropdownContext("Score");
			HardVerify.True(add_query_panel.isScoreItemsDropdownPresent(), "Getting Score dropdown after selecting Context Dropdown",
					"Score dropdown found. [Test Passed]",
					"Score dropdown not found. [Test Failed]");
			*/

			//			 UNCOMMENT THIS FOR SCORE ITEMS CHECKS
			/*HardVerify.True(add_query_panel.scoreItemDropdownOptionsEqual("OPTIONS HERE"));
			add_query_panel.selectFromDropdownScoreItem("SOME OPTION");  
			HardVerify.Equals( add_query_panel.getLastLevelContext(), "Score", "The Last level subject was not Score." );*/
			break;

		default:
			break;
		}
	
	}
	
	public void verifyCorrectContextValues(String context) {
		Log.logInfo("Verifying if the context values are correct...");
		java.util.HashMap<String, String> contexts = add_query_panel.getAllContexts();
		if (context.equalsIgnoreCase("Visit")) {
			HardVerify.Equals(contexts.get("Visit"), visit_name,
					"Verifying Context value for 'Visit name' is correct...", "[Test Passed]",
					"The Visit shown in the Add Query Panel should be [" + visit_name + "] but was ["
							+ contexts.get("Visit") + "] [Test Failed]");
		} else if (context.equalsIgnoreCase("Assessment")) {
			HardVerify.Equals(contexts.get("Visit"), visit_name,
					"Verifying Context value for 'Visit name' is correct...", "[Test Passed]",
					"The Visit shown in the Add Query Panel should be [" + visit_name + "] but was ["
							+ contexts.get("Visit") + "] [Test Failed]");
			HardVerify.Equals(contexts.get("Assessment"), assessment_name,
					"Verifying Context value for 'Assessment name' is correct...", "[Test Passed]",
					"The Assessment shown in the Add Query Panel should be [" + assessment_name + "] but was ["
							+ contexts.get("Visit") + "] [Test Failed]");
		}
		HardVerify.Equals(contexts.get("Subject"), subject_name,
				"Verifying Context value for 'Subject name' is correct...", "[Test Passed]",
				"The Subject shown in the Add Query Panel should be [" + subject_name + "] but was ["
						+ contexts.get("Subject") + "] [Test Failed]");

		HardVerify.Equals(contexts.get("Site"), site_name, "Verifying Context value for 'Site name' is correct...",
				"[Test Passed]", "The Site shown in the Add Query Panel should be [" + site_name + "] but was ["
						+ contexts.get("Site") + "] [Test Failed]");
	}
	
	public void verifyDisabledCreateButtonByDefault() {
		qSidePanel = dashboard.openQueriesPanel();
		add_query_panel = qSidePanel.addQuery();
		HardVerify.False(add_query_panel.isCreateButtonEnabled(),
				"Verifying 'Create Button' is disabled by default in Add Query box...", "[Test Passed]",
				"The Create button was enabled by default. [Test Failed]");
	}
	
	public void verifyEnabledCreateButtonAfterEnteringText(String input) {
		add_query_panel.setTextareaText(input);
		HardVerify.True(add_query_panel.isCreateButtonEnabled(),
				"Verifying 'Create Button' is enabled after entering some text...", "[Test Passed]",
				"The Create button was disabled. [Test Failed]");
	}
	
	public void verifyDisabledCreateButtonAfterClearingText(String input) {
		add_query_panel.setTextareaText(input);
		HardVerify.False(add_query_panel.isCreateButtonEnabled(),
				"Verifying 'Create Button' is disabled after clearing text...", "[Test Passed]",
				"The Create button was enabled. [Test Failed]");
	}
	
	public void verifyCreateNewQuery(String input) {
		int queries_before_create_count = qSidePanel.getNumberOfQueriesInList();
		add_query_panel.setTextareaText(input);
		add_query_panel.clickCreate();
		HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), queries_before_create_count + 1,
				"Verifying new Query was added to the panel...", "[Test Passed]",
				"The number of queries after clicking Create did not increase by 1. [Test Failed]");
	}
	
	public void verifyClosingOfAddQueryPanel(String mode) {
		HardVerify.False(add_query_panel.isOpened(),
				"Verifying 'Add Query' panel was closed after " + mode + " a new query...", "[Test Passed]",
				"'Add Query' panel was open after " + mode + " a new query. [Test Failed]");
	}
	
	public void verifyQueryNotAddedAfterCancel(String input) {
		qSidePanel = dashboard.openQueriesPanel();
		int queries_before_cancel_count = qSidePanel.getNumberOfQueriesInList();
		add_query_panel = qSidePanel.addQuery();
		add_query_panel.setTextareaText(input);
		add_query_panel.clickCancel();
		HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), queries_before_cancel_count,
				"Verifying new Query was not added to the panel after clicking cancel...", "[Test Passed]",
				"The number of queries before and after clicking Cancel did not match. [Test Failed]");
	}
	
	//-------------------------------- Steps for [QueryItemQueryDeleteTests] --------------------------------//
	/*public void createOpenedQueryIfUnavailable() {
		if (qSidePanel.getNumberOfQueriesInList() == 0) {
			qSidePanel.addNewRandomQuery();
			queries_count_before_deleting_for_opened_queries = qSidePanel.getNumberOfQueriesInList();
		}

		if (qSidePanel.getNumberOfQueriesInList() < 1)
			throw new SkipException("No Queries were available in the Query Panel for deleting. Skipping test.");
	}*/
	
	int openQueryIndex;
	public int createOpenedQueryIfUnavailable() {
		boolean openedQueryAvailable = false;
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) { // Search for queries without replies
				Log.logInfo("Searching for open query in queryPanel at index: [" + i + "]");
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				if (item.getQueryNamesAndTimeStamps().size() == 1) {
					openedQueryAvailable = true;
					item.expand();
					if (null != item.getDeleteButton()) {
						Log.logInfo("'Open query' without replies found at index: [" + i + "]");
						item.collapse();
						openQueryIndex = i;
						return i;
					} else {
						Log.logInfo("Non deletable query found at index: [" + i + "]");
						item.collapse();
						Log.logInfo("Adding new query...");
						qSidePanel.addNewRandomQuery();
						openQueryIndex = 0;
						return 0;
					}
				}
			}
			if (openedQueryAvailable == false) { // create new one if all of the queries has replies
				Log.logInfo("No opened query found with delete button. Adding new query...");
				qSidePanel.addNewRandomQuery();
				openQueryIndex = 0;
				return 0;
			}

		} else {
			Log.logInfo("Found 0 query in panel. Adding new query...");
			qSidePanel.addNewRandomQuery();
			openQueryIndex = 0;
		}
		return 0; // return index of newly created query
	}
	
	public void deleteOpenQuery() {
		List<QueryPanelItem> openQueries = qSidePanel.getQueriesFromList();
		for (QueryPanelItem query : openQueries) {
			if (query.getQueryNamesAndTimeStamps().size() == 1) {
				query.expand();
				query.clickDeleteButton();
				break;
			}
		}
	}
	
	public void verifyConfirmationPopUp() {
		HardVerify.True(UiHelper.isPresent(By.cssSelector("div#queryConfirmation div.modal-content")),
				"Verifying Pop-Up is displayed after clicking Delete button...", "[Test Passed]",
				"The Confirmation Pop Up was not displayed. [Test Failed]");
	}
	
	public void verifyOpenQueriesCount(boolean isConfirmed) {
		if (isConfirmed) {
			HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(),
					queries_count_before_deleting_for_opened_queries - 1,
					"Verifying number of Queries decreases after clicking Delete...", "[Test Passed]",
					"The number of Queries after deletion did not decrease by one. Number of Queries after delete should have been ["
							+ (queries_count_before_deleting_for_opened_queries - 1) + "] but was ["
							+ qSidePanel.getNumberOfQueriesInList() + "] [Test Failed]");
		} else {
			HardVerify.Equals(qSidePanel.getNumberOfQueriesInList(), queries_count_before_deleting_for_opened_queries,
					"Verifying number of Queries do not change after clicking cancel on confirmation pop-up...",
					"[Test Passed]",
					"The number of Queries before and after clicking 'No' on confirmation pop-up is not the same. [Test Failed]");
		}
	}
	
	//-------------------------------- Steps for [QueryItemQueryEditTests] --------------------------------//
	
	public void getRandomEdittedQueryIndex() {
		boolean notFound = true;
		int random_index;
		while (notFound) {
			random_index = (int) Math.floor(Math.random() * (qSidePanel.getNumberOfQueriesInList()));
			item = new QueryPanelItem(qSidePanel.getQueryAtIndex(random_index));
			if (item.getQueryNamesAndTimeStamps().size() == 1) {
				index = random_index;
				notFound = false;
			}
		}
		if (notFound == false) {
			Log.logInfo("Query with edit button found at index: [" + index + "]");
		}
	}
	
	public void getRandomOpenedQueryIndex() {
		index = qSidePanel.getRandomQueryIndex();
		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(index));

		// qSidePanel = PageFactory.initElements(Browser.getDriver(),
		// QueriesSidePanel.class);
		// item = qSidePanel.getQueriesFromList().get(index);

		if (null != item.getCloseButton()) {
			Log.logInfo("Query item contains 'Close Query' button. Searching for a query without replies...");
			getRandomOpenedQueryIndex();
		}
	}
	
	String query_Text, query_Label;

	public void getQueryHeaderAndText(int editedQueryIndex) {
		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(editedQueryIndex));
		query_Text = item.getFirstQueryText();
		query_Label = item.getFirstQueryHeader();
	}

	public void verifyClickingEditButtonNotChangesQueryText() {
		item.expand();
		if (UiHelper.isVisible(item.getEditButton())) {
			item.clickEditButton();
		} else {
			throw new SkipException("Edit button not visible. Skipping test...");
		}
		HardVerify.Equals(item.getEditQueryTextArea().getAttribute("value"), query_Text,
				"Test if the Edit Query Text area shows the same text as the Query before expanding...",
				"[Test Passed]",
				"The Query Text and the Query Edit's Text Box text did not match. Should have been [" + query_Text
						+ "] but was [" + item.getEditQueryTextArea().getAttribute("value") + "] [Test Failed]");
	}
	
	public void saveQueryWithText(String text, boolean shouldSave) {
		Log.logStep("Entering Text [" + text + "] in the Edit Text Box...");
		item.getEditQueryTextArea().clear();
		item.getEditQueryTextArea().sendKeys(text);
		if (shouldSave) {
			item.clickEditSaveButton();
		} else {
			item.clickEditCancelButton();
		}
	}

	public void verifyQueryTextChanged(String text, boolean shouldChange) {
		if (shouldChange) {
			HardVerify.Equals(item.getFirstQueryText(), text, "Test if the Query text changed...", "[Test Passed]",
					"The Query Entry did not change to the entered Text [" + text + "] after clicking Save but was ["
							+ item.getFirstQueryText() + "] [Test Failed]");
		} else {
			HardVerify.NotEquals(item.getFirstQueryText(), text,
					"Test if the Query Text did not change after clicking Cancel...", "[Test Passed]",
					"The Query Entry changed to the entered Text [" + text + "] after clicking Cancel. [Test Failed]");
		}

	}

	public void verifyQueryHeaderNotChanged() {
		HardVerify.Equals(item.getFirstQueryHeader(), query_Label,
				"Test if the Query poster name and time posted did not change...", "[Test Passed]",
				"The Query Entry Labels changed from [" + query_Label + "] to [" + item.getFirstQueryHeader()
						+ "] [Test Failed]");
	}
	
	//-------------------------------- Steps for [QueryItemQueryHistoryTests] --------------------------------//
	public void createEditQueryAt(int index, String edit_entry) {
		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(index));
		item.expand();
		if (UiHelper.isClickable(item.getEditButton())) {
			item.clickEditButton();
			saveQueryWithText(edit_entry, true);
		} else {
			Log.logDebugMessage("Query at index: [" + index + "] not editable because Edit button not clickable");
		}
		item.collapse();
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Creats an editted query with history:
	 *  1. If query panel contains zero queries, or
	 *  2. If items in query panel doesn't have any editted query
	 *  
	 *  Steps:
	 *  1. If there are more than 0 queries
	 *  	a). Checks if current query has only one time-stamp
	 *  	b). If found than query is edited 
	 *  
	 *  2. If there is 0 query
	 *  	a). A new random query is generated with comments 
	 *  	b). Query is edited again to create history record
	 * 
	 * @param text
	 */
	public int createQueryIfUnavailable(String type) {
		final String edit_entry = "AUTOMATED EDITED TEXT : " + UiHelper.generateRandonUUIDString();
		boolean editableQueryAvailable = false;
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) {
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				if (type.equals("Edit") || type.equals("History")) {
					Log.logInfo("Searching for edited query with history in queryPanel at index: [" + i + "]");
					if (item.getQueryNamesAndTimeStamps().size() == 1) {
						editableQueryAvailable = true;
						item.expand();
						if (UiHelper.isVisible(item.getShowHistoryButton())) {
							Log.logInfo("Editable query with history found at index: " + i);
							item.collapse();
							return i;
						} else {
							Log.logInfo("Editable query without history found at index: " + i);
							Log.logInfo("Editing query for generating history record");
							createEditQueryAt(i, edit_entry);
							return i;
						}
					}
				}
				if (type.equals("History")) {
					// if editable query not found. search for history button
					if (item.getQueryNamesAndTimeStamps().size() > 1) {
						item.expand();
						if (UiHelper.isVisible(item.getShowHistoryButton())) {
							Log.logInfo("Non-editable query with history found at index: " + i);
							item.collapse();
							return i;
						}
						item.collapse();
					}
				}
			}
			if (editableQueryAvailable == false) {
				Log.logInfo("Found 0 editable query in panel. Adding new query...");		
				qSidePanel.addNewRandomQuery();
				Log.logInfo("Editing query for generating history record...");
				createEditQueryAt(0, edit_entry);
				return 0;
			}
		} else {
			Log.logInfo("Found 0 query in panel. Adding new query...");
			qSidePanel.addNewRandomQuery();
			Log.logInfo("Editing query for generating history record...");
			createEditQueryAt(0, edit_entry);
		}
		return 0; // return index of newly created query
	}
	
	/**
	 * @author HISHAM
	 * 
	 * Returns query panel index for a query with/without history
	 * 
	 * @param boolean hasHistory 
	 * 			- true returns index for Query with history
	 * 			- false returns index for Query without history
	 * 
	 * @return int index 
	 * 			- index value for query with / without index 
	 */
	/*public int getIndexForQueryHasHistory(boolean hasHistory) {
		int index = -1;
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) {
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				if (hasHistory) {
					if (item.getQueryNamesAndTimeStamps().size() == 1) {
						Log.logDebugMessage("Found 1 timestamp");
						item.expand();
						if (UiHelper.isPresent(item.getShowHistoryButton())) {
							index = i;
							Log.logStep("Query with history found at index: " + i);
							item.collapse();
							return index;
						}
						item.collapse();
					}
				} else {
					if (item.getQueryNamesAndTimeStamps().size() > 1) {
						Log.logDebugMessage("Found more than 1 timestamp");
						item.expand();
						if (!UiHelper.isPresent(item.getShowHistoryButton())) {
							index = i;
							Log.logStep("Query without history found at index: " + i);
							item.collapse();
							return index;
						}
						item.collapse();
					}
				}
			}
			Log.logStep("No Query with/without history found");
			Log.logDebugMessage("Returning -1");
			return index;
		}
		Log.logStep("No Query found");
		Log.logDebugMessage("Returning -1");
		return index;
	}*/
	public int getIndexForQueryHasHistory(boolean hasHistory) {
		int index = -1;
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) {
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				if (hasHistory) {
					item.expand();
					if (UiHelper.isPresent(item.getShowHistoryButton())) {
						index = i;
						Log.logStep("Query with history found at index: " + i);
						item.collapse();
						return index;
					}
					item.collapse();
				} else {
					item.expand();
					if (!UiHelper.isPresent(item.getShowHistoryButton())) {
						index = i;
						Log.logStep("Query without history found at index: " + i);
						item.collapse();
						return index;
					}
					item.collapse();
				}
			}
			Log.logStep("No Query with/without history found");
			Log.logDebugMessage("Returning -1");
			return index;
		}
		Log.logStep("No Query found");
		Log.logDebugMessage("Returning -1");
		return index;
	}

	public void verifyInvisibleHistoryButtonForNotExpandedQuery(int index) {
//		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(index));
//		item.collapse();
		HardVerify.False(UiHelper.isPresent(item.getShowHistoryButton()),
				"Verifying History Button is not visible before the Query is expanded...", "[Test Passed]",
				"History Button is visible before the Query is expanded [Test Failed]");
	}
	
	public void verifyVisibilityOfHistoryButtonForExpandedQuery(int index, boolean isVisible) {
//		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(index));
		item.expand();
		if (isVisible) {
			HardVerify.True(UiHelper.isPresent(item.getShowHistoryButton()),
					"Verifying History Button is visible after the Query is expanded...", "[Test Passed]",
					"History Button is not visible after the Query is expanded [Test Failed]");
		} else {
			HardVerify.False(UiHelper.isPresent(item.getShowHistoryButton()),
					"Verifying History Button is not visible after the Query is expanded...", "[Test Passed]",
					"History Button is visible after the Query is expanded [Test Failed]");

		}
//		item.collapse();
	}
	
	public void verifyToggledOffHistoryButton() {
		HardVerify.False(item.isShowHistoryOn(), "Verifying History Button is toggled off by default...",
				"[Test Passed]", "The Show History Button is toggled on by default. [Test Failed]");
	}
	
	public void verifyClickingHistoryButtonOpensHistoryList() {
		item.clickShowHistoryButton();
		HardVerify.True(UiHelper.isPresent(item.getHistoryPanel()),
				"Verifying History panel is visible after clicking history button...", "[Test Passed]",
				"The History List could not be seen after clicking the Show History Button. [Test Failed]");
	}
	
	public void verifyToggledOnHistoryButton(String button) {
		HardVerify.True(item.isShowHistoryOn(), "Verifying History Button is toggled on after "+button+" button was clicked...",
				"[Test Passed]", "The Show History Button is toggled off after "+button+" button was clicked. [Test Failed]");
	}
	
	public void verifyNewestToOldestHistoryOrder() {
		List<Date> historyDates = item.getHistoryItemDates();
		Log.logStep("Verifying history display order is newest to oldest...");
		for (int index = 1; index < historyDates.size(); index++) {
			if (historyDates.get(index).compareTo(historyDates.get(index - 1)) == 0)
				continue;
			HardVerify.False(historyDates.get(index).after(historyDates.get(index - 1)),
					"The current date : " + historyDates.get(index).toString() + " is not before than "
							+ historyDates.get(index - 1).toString());
		}
		Log.logStep("[Test Passed]");
	}
	
	public void verifyTogglingExpandButtonRetainsHistoryList() {
		item.collapse();
		item.expand();
		Log.logStep("Test if History List is toggled off after collapsing and expanding the query...");
		HardVerify.True(UiHelper.isPresent(item.getHistoryPanel()),
				"Verifying History panel is visible after toggling expand button...", "[Test Passed]",
				"The History List could not be seen after toggling expand button. [Test Failed]");
	}
	
	public void verifyTogglingHistoryButtonClosesHistoryList() {
		item.clickShowHistoryButton();
		Log.logStep("Test if Show History is toggled off after clicking the Show History Button...");
		HardVerify.False(item.isShowHistoryOn(),
				"Verifying History panel is not visible after toggling history button...", "[Test Passed]",
				"History panel is visible after toggling history button. [Test Failed]");
	}
	
	//-------------------------------- Steps for [QueryItemReplyAndHyperlinkTests] --------------------------------//
	int closedQueryIndex;
	
	public void createClosedQuery() {
		item = new QueryPanelItem(qSidePanel.getQueryAtIndex(0));
		item.expand();
		item.getReplyTextBox().sendKeys("Test Reply");
		item.clickReply();
		item.getReplyTextBox().sendKeys("Closing query...");
		item.clickCloseButton();
		confimQueryAction(true);
	}

	public int createClosedQueryIfUnavailable() {
		boolean closedQueryAvailable = false;
		int queryCount = qSidePanel.getNumberOfQueriesInList();
		if (queryCount > 0) {
			for (int i = 0; i < queryCount; i++) {	// Search for queries with close status
				Log.logInfo("Searching for closed query in queryPanel at index: [" + i + "]");
				item = new QueryPanelItem(qSidePanel.getQueryAtIndex(i));
				if (item.getStatus().equalsIgnoreCase("Closed")) {
					Log.logInfo("Closed query found at index: [" + i + "]");
					closedQueryAvailable = true;
					closedQueryIndex = i;
					return i;
				}
			}
			if (closedQueryAvailable == false) { // create new one & close if none of the query has close status
				Log.logInfo("No query found with close status");
				Log.logInfo("Adding new query...");
				selectQueriesCheckBox(true, "Open");
				qSidePanel.addNewRandomQuery();
				createClosedQuery();
				selectQueriesCheckBox(true, "Closed");
				closedQueryAvailable = true;
				closedQueryIndex = 0; // return index of newly closed query
				return 0;
			}
		} else { // create new one & close
			Log.logInfo("No query found in panel. Adding new query...");
			selectQueriesCheckBox(true, "Open");
			qSidePanel.addNewRandomQuery();
			createClosedQuery();
			selectQueriesCheckBox(true, "Closed");
			closedQueryAvailable = true;
			closedQueryIndex = 0;
		}
		return 0; // return index of newly closed query
	}
	
	int rndIdx;
	WebElement randQueryElement;
	QueryPanelItem randQueryItem;
	public void getRandomQueryFromPanel() {
		rndIdx = qSidePanel.getRandomQueryIndex();
		randQueryElement = qSidePanel.getQueryAtIndex(rndIdx);
		randQueryItem = new QueryPanelItem(randQueryElement);
	}
	
	public void verifyQueryItemVisibility() {
		Log.logStep("Test the random query for the presence of a hyperlink and accessability.....");
		HardVerify.True(randQueryElement.isDisplayed(),
				"Verifying visibility of randomly picked query at position: [" + (rndIdx + 1) + "]", "[Test Passed]",
				"The query at " + (rndIdx + 1) + " is not visible. [Test Failed]");
	}
	
	public void verifyQueryItemClickability() {
		HardVerify.True(randQueryElement.isEnabled(),
				"Verifying clickability of randomly picked query at position: [" + (rndIdx + 1) + "]", "[Test Passed]",
				"The query at " + (rndIdx + 1) + " is not clickable. [Test Failed]");
	}
	
	public void verifyClickEventContainsJavaScriptCall() {
		HardVerify.Equals(randQueryElement.getAttribute("data-ng-click"), "itemClick(query, $event)",
				"Verifying click event for randomly picked query at position: [" + (rndIdx + 1)
						+ "] contains JavaScript call",
				"[Test Passed]", "Click event for randomly picked query at position: [" + (rndIdx + 1)
						+ "] doesn't contain JavaScript call. [Test Failed]");
	}
	
	public void verifyQueryItemMetaData() {
		HardVerify.True(randQueryItem.hasValidMetaData(),
				"Verifying metadata for randomly picked query at position: [" + (rndIdx + 1) + "]", "[Test Passed]",
				"The query at position: [" + (rndIdx + 1) + "] doesn't have valid metadata. [Test Failed]");
	}
	
	public void verifyQueryItemExpandButton() {
		HardVerify.NotNull(randQueryItem.getExpandButton(),
				"Verifying presence of expand button for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "The Expand Button (with a up-arrow) could not be found for query at position: ["
						+ (rndIdx + 1) + "]. [Test Failed]");
	}
	
	public void expandRandomQueryItem() {
		Log.logStep("Expanding random query found at position: [" + (rndIdx + 1) + "]");
		randQueryElement = qSidePanel.getQueryAtIndex(rndIdx);// introduced to handle Stale exception
		randQueryItem = new QueryPanelItem(randQueryElement);
		randQueryItem.expand();
	}
	
	WebElement replyTextArea;
	public void verifyQueryItemReplyTextBox() {
		replyTextArea = randQueryItem.getReplyTextBox();
		HardVerify.NotNull(replyTextArea,
				"Verifying presence of Reply Text Box for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "Reply Text Box not found. [Test Failed]");

		HardVerify.True(replyTextArea.isDisplayed(),
				"Verifying visibility of Reply Text Box for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "Reply Text Box found not visible. [Test Failed]");
		
		HardVerify.True(replyTextArea.isEnabled(),
				"Verifying clickability of Reply Text Box for randomly picked query at position: [" + (rndIdx + 1)
						+ "]",
				"[Test Passed]", "Reply Text Box found not clickable. [Test Failed]");
		
		HardVerify.Equals(replyTextArea.getAttribute("value"),
				"", "Verifying Reply Text Box is empty by default for randomly picked query at position: ["
						+ (rndIdx + 1) + "]",
				"[Test Passed]", "Reply Text Box found not empty by default. [Test Failed]");
	}
	
	WebElement replyButton;
	public void verifyQueryItemReplyButton() {
		replyButton = randQueryItem.getReplyButton();
		HardVerify.NotNull(replyButton,
				"Verifying presence of 'Reply' button for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "'Reply' button not found. [Test Failed]");

		(new Actions(Browser.getDriver())).moveToElement(replyButton).perform();
		HardVerify.True(replyButton.isDisplayed(),
				"Verifying visibility of 'Reply' button for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "'Reply' button found not visible. [Test Failed]");

		HardVerify.False(randQueryItem.isReplyEnabled(),
				"Verifying disabled 'Reply' button before filling up text box for randomly picked query at position: ["
						+ (rndIdx + 1) + "]",
				"[Test Passed]", "'Reply' button found enabled. [Test Failed]");
		/*HardVerify.False(randQueryItem.isReplyEnabled(), "The Reply Button was enabled for query : " + (rndIdx + 1)
				+ " after the Expand Button was clicked but before any keys were entered in the text box.");
		Log.logStep("Test Passed.");*/
	}
	
	String textarea_text;

	public void fillupReplyTextBox(String editText) {
		Log.logStep("Entering text to reply textbox: " + editText);
		replyTextArea = randQueryItem.getReplyTextBox();
		replyTextArea.sendKeys(editText);
		textarea_text = editText;
	}
	
	public void verifyEnabledReplyButton() {
		HardVerify.Equals(replyTextArea.getAttribute("value"), textarea_text,
				"Verifying values can be entered in 'Reply' text box ", "[Test Passed]",
				"Text Area was empty after entering keys. [Test Failed]");

		HardVerify.True(replyButton.isEnabled(), "Verifying enabled 'Reply' button after filling up text box",
				"[Test Passed]", "'Reply' Button found disabled [Test Failed]");
		
//		textArea.sendKeys("");
	}
	
	public void verifyQueryItemCollapseButton() {
		HardVerify.NotNull(randQueryItem.getCollapseButton(),
				"Verifying presence of collapse button for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "The collapse button (with a down-arrow) could not be found for query at position: ["
						+ (rndIdx + 1) + "]. [Test Failed]");
	}
	
	public void collapseRandomQueryItem() {
		Log.logStep("Collapsing random query found at index: [" + rndIdx + "]");
		randQueryItem.collapse();
	}
	
	public void verifyQueryItemHiddenReplyTextBox() {
		HardVerify.False(replyTextArea.isDisplayed(),
				"Verifying hidden 'Reply' Text Box for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "Reply Text Box found visible. [Test Failed]");
	}
	
	public void verifyQueryItemHiddenReplyButton() {
		HardVerify.False(replyButton.isDisplayed(),
				"Verifying hidden 'Reply' button for randomly picked query at position: [" + (rndIdx + 1) + "]",
				"[Test Passed]", "Reply button found visible. [Test Failed]");
	}
	
	public void verifyInvisibleReplyTextBox() {
		HardVerify.Null(randQueryItem.getReplyTextBox(),
				"Verifying 'Reply' Text Box not presnet for randomly picked closed query at position: [" + (rndIdx + 1)
						+ "]",
				"[Test Passed]", "Found 'Reply' Text Box. [Test Failed]");
	}
	
	public void verifyInvisibleReplyButton() {
		HardVerify.Null(randQueryItem.getReplyButton(),
				"Verifying 'Reply' Button not presnet for randomly picked closed query at position: [" + (rndIdx + 1)
						+ "]",
				"[Test Passed]", "Found 'Reply' Button. [Test Failed]");
	}
	
	public void clickReplyForRandomQueryItem() {
		Log.logStep("Clicking Reply button for random query at position: [" + (rndIdx + 1) + "]");
		randQueryItem.clickReply();
		randQueryElement = qSidePanel.getQueryAtIndex(rndIdx);// introduced to handle Stale exception
        randQueryItem = new QueryPanelItem(randQueryElement);
		UiHelper.checkPendingRequests(Browser.getDriver());
	}

	public void verifyLatestReplyUserName() {
		header = PageFactory.initElements(Browser.getDriver(), Header.class);
		String userName = header.getuserName();
		HardVerify.Equals(userName, randQueryItem.getLatestReplyUsername(), 
				"Verifying Replier's name matches with current logged in user name", "[Test Passed]",
				"The Replier's Name did not match. The Reply name should be [" + userName + "] but was ["
						+ randQueryItem.getLatestReplyUsername() + "] [Test Failed]");
	}

	public void verifyLatestReplyText(String text) {
		HardVerify.Equals(randQueryItem.getLatestReplyText(), text,
				"Verifying Replied text matches with current text in reply box", "[Test Passed]",
				"The Latest Reply Texts did not match. Text should be [" + text + "] but was ["
						+ randQueryItem.getLatestReplyText() + "] [Test Failed]");
	}
	
	//-------------------------------- Steps for [QueryCategoryContextTests] --------------------------------//
	public void verifyQueryPanelHasQueryFor(String context) {
		ArrayList<QueryPanelItem> queries = qSidePanel.getQueriesFromList();
		if (queries.size() > 0) {
			switch (context) {
			case "Subject":
				Log.logInfo("Subject: [" + subjectName + "] contains [" + queries.size() + "] query(s)");
				Log.logStep("Verifying Query Panel for Subject: [" + subjectName
						+ "] only contains the Queries for this subject...");
				for (QueryPanelItem query : queries) {
					HardVerify.Equals(subjectName, query.getQuerySubjName(),
							"The Query Subject did not match. Expected subject to be [" + subjectName + "] but was ["
									+ query.getQuerySubjName() + "] [Test Failed]");
				}
				Log.logInfo("[Test Passed]");
				break;

			case "Visit":
				Log.logInfo("Visit: [" + visitName + "] contains [" + queries.size() + "] query(s)");
				Log.logStep("Verifying Query Panel for Visit: [" + visitName
						+ "] contains the Queries for this visit...");
				for (QueryPanelItem query : queries) {
					String queryCategory = query.getQueryCategory();
					if (queryCategory.equals("Subject")) {
						HardVerify.Equals(subjectName, query.getQuerySubjName(),
								"The Query Subject did not match. Expected subject to be [" + subjectName
										+ "] but was [" + query.getQuerySubjName() + "] [Test Failed]");
					}
					if (queryCategory.equals("Visit") || queryCategory.equals("Assessment")) {
						HardVerify.Equals(subjectName, query.getQuerySubjName(),
								"The Query Subject did not match. Expected subject to be [" + subjectName
										+ "] but was [" + query.getQuerySubjName() + "] [Test Failed]");
						// May contain queries for other visits related to same subject
						Verify.Equals(visitName, query.getQueryVisit(),
								"The Query Visit did not match. Expected visit to be [" + visitName + "] but was ["
										+ query.getQueryVisit() + "]");
					}
				}
				Log.logInfo("[Test Passed]");
				break;

			case "Assessment":
				Log.logInfo("Assessment: [" + assessmentName + "] contains [" + queries.size() + "] query(s)");
				Log.logStep("Verifying Query Panel for Assessment: [" + assessmentName
						+ "] contains the Queries for this Assessment...");
				for (QueryPanelItem query : queries) {

					String queryCategory = query.getQueryCategory();
					if (queryCategory.equals("Subject")) {
						HardVerify.Equals(subjectName, query.getQuerySubjName(),
								"The Query Subject did not match. Expected subject to be [" + subjectName
										+ "] but was [" + query.getQuerySubjName() + "]");
					}
					if (queryCategory.equals("Visit")) {
						HardVerify.Equals(subjectName, query.getQuerySubjName(),
								"The Query Subject did not match. Expected subject to be [" + subjectName
										+ "] but was [" + query.getQuerySubjName() + "]");
						// May contain queries for other visits related to same subject  
						Verify.Equals(visitName, query.getQueryVisit(),
								"The Query Visit did not match. Expected visit to be [" + visitName + "] but was ["
										+ query.getQueryVisit() + "]");
					}
					if (queryCategory.equals("Assessment")) {
						HardVerify.Equals(subjectName, query.getQuerySubjName(),
								"The Query Subject did not match. Expected subject to be [" + subjectName
										+ "] but was [" + query.getQuerySubjName() + "]");
						// May contain queries for other visits related to same subject
						Verify.Equals(visitName, query.getQueryVisit(),
								"The Query Visit did not match. Expected visit to be [" + visitName + "] but was ["
										+ query.getQueryVisit() + "]");
						// May contain queries for other assessments related to same subject
						Verify.Equals(assessmentName, query.getQueryAssessmentName(),
								"The Query Assessment did not match. Expected Assessment to be [" + assessmentName
										+ "] but was [" + query.getQueryAssessmentName() + "]");
					}
				}
				Log.logInfo("[Test Passed]");
				break;

			default:
				break;
			}
			qSidePanel.clickClose();
		} else {
			throw new SkipException(
					"The query panel for " + context + " contains 0 query. Skipping test on " + context + "...");
		}
	}
	
	public void openItemListForContext(String context) {
		commonSteps.getToStudyDashboard();
		switch (context) {
		case "Subject":
			studyDashboardSteps.openAllSubjects();
//			if (dashboardList.getListCount() == 0)
//				throw new SkipException("Subject list table contains 0 item. Skipping tests...");
			break;

		case "Visit":
			studyDashboardSteps.openAllVisits();
//			if (dashboardList.getListCount() == 0)
//				throw new SkipException("Visit list table contains 0 item. Skipping tests...");
			break;

		case "Assessment":
			studyDashboardSteps.openAllAssessments();
//			if (dashboardList.getListCount() == 0)
//				throw new SkipException("Assessment list table contains 0 item. Skipping tests...");
			break;

		default:
			break;
		}
	}
	
	//-------------------------------- Steps for [QueryContextDBTests] --------------------------------//
	String studyName;
	public void selectStudyAndGetStudyName(String study) {
		Log.logInfo("From dashboard selecting Study: [" + study + "]");
		dashboard.selectStudy(study);
		// TODO : REMOVE THIS FIX-----------
		dashboard.clickRefresh();
		// THIS FIX^^^^^^^^^^^^^^
		studyName = study.split(" - ")[1];
		Log.logInfo("Study name extracted as: [" + studyName + "]");
	}
	
	ITable table;
	public void verifyUiToDbQueryCount() {
		ArrayList<QueryPanelItem> queries = qSidePanel.getQueriesFromList();
		DBQueries dbq = new DBQueries();
		// Get dataset as ITable object
		table = dbq.getQueriesByStudySite(studyName, siteName);
		HardVerify.Equals(queries.size(), table.getRowCount(), "Verifying the query count matches between UI and DB...",
				"[Test Passed]", "Query count mismatch. From UI: [" + queries.size() + "], From DB: ["
						+ table.getRowCount() + "] [Test Failed");
	}
	
	public void verifyDbQueryObjectWithUi() {
		ArrayList<QueryPanelItem> dbQueries = new ArrayList<QueryPanelItem>();
		// Create list of Query objects for each database row 		
		for (int i = 0; i < table.getRowCount(); i++) {
			dbQueries.add(new QueryPanelItem(table, i));
		}
		// Get each dataset row and check if the UI Query list has this object 
		Log.logStep("Verifying all query items...");
		for (int i = 0; i < table.getRowCount(); i++) { // TODO Change Size to
			HardVerify.True(queries.contains(dbQueries.get(i)),
					"Queries on Query Panel didnt match with those from DB.");
		}
		Log.logStep("All Queries on Query Panel matched with those from DB.");
		qSidePanel.clickClose();
	}
	
}
