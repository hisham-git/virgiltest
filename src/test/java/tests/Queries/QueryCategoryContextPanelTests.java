package tests.Queries;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * @author Ayman Noor, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryCategoryContextPanel" })
public class QueryCategoryContextPanelTests extends AbstractQueries {
	
	/**
	 * Before every test method: 
	 * 1. Log the name of the method that is running
	 * 2. Open Query Panel
	 * 3. Check primary filters 
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		qSidePanel = dashboard.openQueriesPanel();
		qSidePanel.checkPrimaryCheckBoxesTo(true);
	}
	
	/**
	 * After every test method: 
	 * 1. Close Query Panel
	 * 2. Go to Study Dashboard 
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		qSidePanel.clickClose();
		commonSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method, result);
	}

	/**
	 * Test Method: Find a Subject Query -> Clicks on the
	 * Query -> Verify the Respective Subject on Main Window
	 * 
	 * Objective - To check that the respective Subject appears
	 * on main window if a user clicks on a Subject Query
	 * 
	 * Steps : 
	 * 1. Search a Subject Query from the existing list of Queries 
	 * 2. Clicks on the Subject Query 
	 * 3. Checks the query count against the user input
	 * 4. Checks if the respective Subject has been loaded on main window
	 */
	@Test(priority = 1, groups = { "SubjectQueryContext",
			"JamaNA" }, description = "Checks if the respective Subject has been loaded on main window once the user clicks on a subject Query.")
	public void subjectQueryContextTest() {
		queriesSteps.getQueryList(qSidePanel);
		if (queriesSteps.findAndClickQueryType("Subject", qSidePanel)) {
			// TODO; will be implemented later
			// queriesSteps.verifyExpectedQueryCount();
			queriesSteps.verifySujectDetailsDataFor("Subject");
			queriesSteps.verifySujectDetailsDataFor("Site");
		} else {
			throw new SkipException("No Subject Query found in the Query list. Skipping test...");
		}
	}

	/**
	 * 
	 * @return QueryCount of VisitQuery
	 */
	@DataProvider
	public Object[][] visitQueryCount() {
		return new Object[][] { { 1 } };
	}
	
	/**
	 * Test Method: Find a Visit Query -> Clicks on the
	 * Query -> Verify the Respective Visit on Main Window
	 * 
	 * Objective - To check that the respective Visit appears
	 * on main window if a user clicks on a Visit Query
	 * 
	 * Steps : 
	 * 1. Search a Visit Query from the existing list of Queries 
	 * 2. Clicks on the Visit Query 
	 * 3. Checks the query count against the user input
	 * 4. Checks if the respective Visit has been loaded on main window
	 */
	@Test(priority = 2, groups = { "VisitQueryContext",
			"JamaNA" }, description = "Checks if the respective Visit has been loaded on main window once the user clicks on a visit Query.", dataProvider = "visitQueryCount")
	public void visitQueryContextTest(int queryCount) {
		queriesSteps.getQueryList(qSidePanel);
		if (queriesSteps.findAndClickQueryType("Visit", qSidePanel)) {
			// TODO; will be implemented later
			// queriesSteps.verifyExpectedQueryCount(queryCount);
			queriesSteps.verifyVisitDetailsDataFor("Site");
			queriesSteps.verifyVisitDetailsDataFor("Subject");
			queriesSteps.verifyVisitDetailsDataFor("Visit");

		} else {
			throw new SkipException("No Visit Query found in the Query list. Skipping test...");
		}
	}

	/**
	 * 
	 * @return QueryCount of AssessmentQuery
	 */

	@DataProvider
	public Object[][] assessQueryCount() {
		return new Object[][] { { 6 } };
	}
	
	/**
	 * Test Method: Find a Assessment Query -> Clicks on the
	 * Query -> Verify the Respective Assessment on Main Window
	 * 
	 * Objective - To check that the respective Assessment appears
	 * on main window if a user clicks on a Assessment Query
	 * 
	 * Steps : 
	 * 1. Search a Assessment Query from the existing list of Queries 
	 * 2. Clicks on the Assessment Query
	 * 3. Checks the query count against the user input
	 * 4. Checks if the respective Assessment has been loaded on main window
	 */
	@Test(priority = 3, groups = { "AssessmentQueryContext",
			"JamaNA" }, description = "Checks if the respective Assessment has been loaded on main window once the user clicks on a assessment Query.", dataProvider = "assessQueryCount")
	public void assessmentQueryContextTest(int queryCount) {
		queriesSteps.getQueryList(qSidePanel);
		if (queriesSteps.findAndClickQueryType("Assessment", qSidePanel)) {
			// TODO; will be implemented later
			// queriesSteps.verifyExpectedQueryCount(queryCount);
			queriesSteps.verifyAssessMentDetailsDataFor("Site");
			queriesSteps.verifyAssessMentDetailsDataFor("Subject");
			queriesSteps.verifyAssessMentDetailsDataFor("Visit");
			queriesSteps.verifyAssessMentDetailsDataFor("Assessment");
		} else {
			throw new SkipException("No Assessment Query found in the Query list. Skipping test...");
		}
	}

	/**
	 * 
	 * @return QueryCount of AssessmentQuery
	 */

	@DataProvider
	public Object[][] scoreQueryCount() {
		return new Object[][] { { 6 } };
	}
	
	/**
	 * Test Method: Find a Score Query -> Clicks on the
	 * Query -> Verify the Respective Assessment on Main Window
	 * 
	 * Objective - To check that the respective Assessment appears
	 * on main window if a user clicks on a Score Query
	 * 
	 * Steps : 
	 * 1. Search a Score Query from the existing list of Queries 
	 * 2. Clicks on the Score Query 
	 * 3. Checks the query count against the user input
	 * 4. Checks if the respective Assessment has been loaded on main window
	 */
	@Test(priority = 4, groups = { "ScoreQueryContext",
			"JamaNA" }, description = "Checks if the respective Assessment has been loaded on main window if the user clicks on a score Query.", dataProvider = "scoreQueryCount")
	public void scoreQueryContextTest(int queryCount) {
		queriesSteps.getQueryList(qSidePanel);
		if (queriesSteps.findAndClickQueryType("Score", qSidePanel)) {
			// TODO; will be implemented later
			// queriesSteps.verifyExpectedQueryCount(queryCount);
			queriesSteps.verifyAssessMentDetailsDataFor("Site");
			queriesSteps.verifyAssessMentDetailsDataFor("Subject");
			queriesSteps.verifyAssessMentDetailsDataFor("Visit");
			queriesSteps.verifyAssessMentDetailsDataFor("Assessment");
		} else {
			throw new SkipException("No Score Query found in the Query list. Skipping test...");
		}
	}
}
