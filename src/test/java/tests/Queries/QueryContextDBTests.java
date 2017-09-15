package tests.Queries;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.objects.QueryPanelItem;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import nz.siteportal.pages.studydashboard.ListPages.SubjectList;
import nz.siteportal.pages.studydashboard.ListPages.VisitList;
import nz.siteportal.utils.db.DBQueries;
import steps.Configuration.CommonSteps;
import steps.Tests.QueriesSteps;

import org.dbunit.dataset.ITable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.gargoylesoftware.htmlunit.javascript.host.Set;
import com.google.common.collect.Multimap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * QueryContextFilterViewTests will verify if the queries loaded on query panel
 * are contextual or not for various filtered levels with respect to Study,
 * Subject, Site, Visit, Assessment
 * 
 * @author Ayman, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryContextDB" })
public class QueryContextDBTests extends AbstractQueries {
	
	private static List<Object[]> studySiteList;

	/**
	 * Log that the Test Class is beginning
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		queriesSteps = new QueriesSteps();
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	/**
	 * Before every test method: 
	 * 1. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	/**
	 * After every test method: 
	 * 1. Navigate to Study Dashboard
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
//		qSidePanel.clickClose();
		commonSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method, result);
	}

	@DataProvider
	public Iterator<Object[]> studySiteList() {
		studySiteList = queriesSteps.getSitesWithQueriesForAllStudies();
		return studySiteList.iterator();
	}

	/**
	 * Test Method
	 * Go to Study Dashboard -> Select a Study -> Select a Site -> Open Query Panel
	 * 
	 * Objective - Compares the Query count and Query metadata between UI and Database for an specific Study and  Site 
	 * Steps : 
	 * 	1. Go to Study Dashboard
	 * 	2. Select the user provided Study
	 *  3. Select the user provided Site
	 *  4. Open Query Panel and construct a list of Query objects
	 *  5. Fetch dataset for the selected Study and Site
	 *  6. Construct a list of Query objects for the fetched dataset
	 *  7. Check if each object(row of dataset) exists in UI Query object list   
	 * 		-> Check if Displayed
	 * 		-> Check if Enabled
	 * 		-> Check if Clicking fires a Javascript Event (Substitute for a hyperlink, since AngularJS is being used to actually navigate) 
	 */
	@Test(groups = { "UiToDbQueryMetadata",
			"JamaNA" }, description = "Checks if Queries from UI match with those from DB for a selected Study-site", dataProvider = "studySiteList")
	public void uiToDbQueryMetadataTest(Multimap<String, String> studySiteMap) {
		for (String study : studySiteMap.keySet()) {
			queriesSteps.selectStudyAndGetStudyName(study);
			for (String site : studySiteMap.get(study)) {
				queriesSteps.selectSiteAndGetSiteName(site);
				queriesSteps.openQuerySidePanel();
				queriesSteps.verifyUiToDbQueryCount();
				queriesSteps.verifyDbQueryObjectWithUi();
			}
		}
	}

	/**
	 * @return Site Number, Subject Name and Query Count
	 *//*
	@DataProvider
	public Object[][] subjectList() {
		// returns Site Number, Subject Name and Query Count
		return new Object[][] { 
			{ "zSK_SpName - zSK-StudyForm","1upd", "123Sacred", 0 },
			{ "zSK_SpName - zSK-StudyForm","1upd", "_rand#333", 7 }
			};
	}
	
	@Test(groups = { "Query Panel",	"Query Context" }, 
			description = "Checks if Queries from UI match with those from DB for a selected Study-Site-Subject", 
			dataProvider = "subjectList")
	public void checkQueriesFromDBForASubject(String paramStudy, String paramSite, String paramSubj, int count) {
		if (panel != null && panel.isOpened())
			panel.clickClose();
		// Clicks on Subject card
		listPage = dashboard.clicksOnCard("Subjects-Subjects");
		subjectList = PageFactory.initElements(Browser.getDriver(), SubjectList.class);
		// Clicks on the specific Subject
		subjectDetails = subjectList.clickRowForSubject(paramSubj);
		// Open Query panel
		panel = dashboard.openQueriesPanel();
		String subjectName = subjectDetails.getSubjNameFromHeader();
		// Get the list of queries
		ArrayList<QueryPanelItem> queries = panel.getQueriesFromList();
		Log.logStep("Checking if all entries of the Query list belongs to selected site(s)...");
		// Initialize the class object and get the dataset for the selected
		// Study-Site
		DBQueries dbq = new DBQueries();
		// Get dataset as ITable object
//		ITable table = dbq.getQueriesByStudySiteSubject(paramStudy, paramSite,subjectName,"Subject");
		ITable table = dbq.getQueriesByStudySite("E2609-G000-202", "1001","MST01", "steps/Subject");
		// Check if the Query count match between UI and DB 
		HardVerify.Equals(table.getRowCount(), queries.size(),
				"Verifying the query count...",
				"Query count matched. From UI:"+queries.size()+",From DB:"+table.getRowCount(),
				"Query count mismatch. From UI:"+queries.size()+",From DB:"+table.getRowCount());
		ArrayList<QueryPanelItem> dbQueries = new ArrayList<QueryPanelItem>();
		// Create list of Query objects for each database row 		
		for (int i = 0; i < table.getRowCount(); i++) {
			dbQueries.add(new QueryPanelItem(table, i));
		}
		// Get each dataset row and check if the UI Query list has this object 
		Log.logStep("Verifying all query items...");
		for (int i = 0; i < table.getRowCount(); i++) { 
			HardVerify.True(queries.contains(dbQueries.get(i)),
					"Queries on Query Panel didnt match with those from DB.");
		}
		Log.logStep("All Queries on Query Panel matched with those from DB.");
	}
	*//**
	 * @return Study Name, Site Number, SVID and Query Count
	 *//*
	public Object[][] visitList() {
		// returns Site Number, Subject Name and Query Count
		return new Object[][] { 
			{ "zSK_SpName - zSK-StudyForm","1upd", "993", 0 },
			{ "zSK_SpName - zSK-StudyForm","1upd", "4053", 7 }
			};
	}

	@Test(groups = { "Query Panel",	"Query Context" }, 
			description = "Checks if Queries from UI match with those from DB for a selected Study-Site-Visit", 
			dataProvider = "visitList")
	public void checkQueriesFromDBForAVisit(String paramStudy, String paramSite, String paramSVID, int queryCount) {
		if (panel != null && panel.isOpened())
			panel.clickClose();
		// Clicks on Subject card
		listPage = dashboard.clicksOnCard("Visits-Visits");
		visitList = PageFactory.initElements(Browser.getDriver(), VisitList.class);
		// Clicks on the specific Visit
		WebElement visitRow = visitList.getRowForVisit("SVID", paramSVID);
		visitDetails = visitList.clickAVisit(visitRow);		
		// Open Query panel
		panel = dashboard.openQueriesPanel();
		String visitNameUI = visitDetails.getVisitNameFromHeader();
		// Get the list of queries
		ArrayList<QueryPanelItem> queries = panel.getQueriesFromList();
		Log.logStep("Checking if all entries of the Query list belongs to selected site(s)...");
		// Initialize the class object and get the dataset for the selected
		// Study-Site
		DBQueries dbq = new DBQueries();
		// Get dataset as ITable object
//		ITable table = dbq.getQueriesByStudySiteSubject(paramStudy, paramSite,visitNameUI,"Visit");
		ITable table = dbq.getQueriesByStudySite("E2609-G000-202", "1001","11","Visit");
		// Check if the Query count match between UI and DB 
		HardVerify.Equals(table.getRowCount(), queries.size(),
				"Verifying the query count...",
				"Query count matched. From UI:"+queries.size()+",From DB:"+table.getRowCount(),
				"Query count mismatch. From UI:"+queries.size()+",From DB:"+table.getRowCount());
//		System.out.println("After2"+table.getRowCount());
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
	}

	*//**
	 * @return Study Name, Site Number, SVID and Query Count
	 *//*
	public Object[][] assessmentList() {
		// returns Site Number, Subject Name and Query Count
		return new Object[][] { 
			{ "zSK_SpName - zSK-StudyForm","1upd","CDR", "993", 0 },
			{ "zSK_SpName - zSK-StudyForm","1upd","FAQ", "4053", 7 }
			};
	}
	@Test(groups = { "Query Panel",	"Query Context" }, 
			description = "Checks if Queries from UI match with those from DB for a selected Study-Site-Assessment", 
			dataProvider = "assessmentList")
	public void checkQueriesFromDBForAnAssessment(String paramStudy, String paramSite, String formName, String paramSVID, int queryCount) {
		if (panel != null && panel.isOpened())
			panel.clickClose();
		listPage = dashboard.clicksOnCard("Assessments-Assessments");
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		// Clicks on the specific assessment
		assessmentDetails = assessmentList.clickRow(formName, paramSVID);
		// Open Query panel
		panel = dashboard.openQueriesPanel();
		String assessmentName = assessmentDetails.getAssessmentName();
		// Get the list of queries
		ArrayList<QueryPanelItem> queries = panel.getQueriesFromList();
		Log.logStep("Checking if all entries of the Query list belongs to selected site(s)...");
		// Initialize the class object and get the dataset for the selected
		// Study-Site
		DBQueries dbq = new DBQueries();
		// Get dataset as ITable object
//		ITable table = dbq.getQueriesByStudySiteSubject(paramStudy, paramSite,formName,"Assessment");
		ITable table = dbq.getQueriesByStudySite("E2609-G000-202", "1001","CDR V2", "steps/Assessment");
		// Check if the Query count match between UI and DB 
//		HardVerify.Equals(table.getRowCount(), queries.size(),"Query count mismatch. From UI:"+queries.size()+",From DB:"+table.getRowCount());
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
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterEveryMethod(Method method) {
		Log.logInfo("Finished method -> " + method.getName());
	}

	@AfterClass(alwaysRun = true)
	public void afterAllMethods() {
		Log.logInfo("Finished class -> " + this.getClass().getName());
	}
*/
}
