package tests.Queries;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * QueriesPanelQuerEditTests
 * Automation Scope Sheet -> Queries Side Bar -> Edit History
 * Checks conformity with requirements - SFB-RQ-27, as in https://k8d.jamacloud.com/perspective.req#/items/4194844?projectId=29316
 * 
 * STATUS : FUNCTIONALITY - NEEDS TO BE CHECKED AGAINST A BETTER DEPLOYMENT
 * 			NEEDS COMMENTS
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryItemQueryHistory" })
public class QueryItemQueryHistoryTests extends AbstractQueries {

	private int queryWithHistoryIndex, queryWithoutHistoryIndex;
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
//		commonSteps.getToStudyDashboard();
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.selectQueriesCheckBox(true, "Open", "Mine");	
	}
	
	/**
	 * Test Method
	 * Automation Scope Sheet -> Queries Side Bar -> Edit History
 	 * 
	 * Objective - To check for the presence of the Query History List and History Display Toggle
	 * Steps :
	 * 	1. Select a SPECIFIED Query (which has edited history)
	 * 	2. Check that no History List is shown
	 * 	3. Click on its Expand Button
	 * 	4. Check if Show History Button is present
	 * 	5. Click on Show History
	 * 	6. Check if Edit History list is displayed
	 * 	7. Check if the history is Sorted from newest to oldest
	 * 	8. Click Edit History button
	 * 	9. Check if History is NOT displayed
	 */

	@Test(groups = { "HistoryPanelInQueryWithEditedHistory",
			"SFB-REQ-27" }, description = "Checks for the presence of the correct elements and functionality for a valid Query History")
	public void historyPanelInQueryWithEditedHistoryTest() {
		queryWithHistoryIndex = queriesSteps.createQueryIfUnavailable("History");
		queriesSteps.verifyInvisibleHistoryButtonForNotExpandedQuery(queryWithHistoryIndex);
		queriesSteps.verifyVisibilityOfHistoryButtonForExpandedQuery(queryWithHistoryIndex, true);
		queriesSteps.verifyToggledOffHistoryButton();
		queriesSteps.verifyClickingHistoryButtonOpensHistoryList();
		queriesSteps.verifyToggledOnHistoryButton("History");
		queriesSteps.verifyNewestToOldestHistoryOrder();
		queriesSteps.verifyTogglingExpandButtonRetainsHistoryList();
		queriesSteps.verifyToggledOnHistoryButton("Expand");
		queriesSteps.verifyTogglingHistoryButtonClosesHistoryList();
	}

	/**
	 * Test Method
	 * Automation Scope Sheet -> Queries Side Bar -> Edit History
 	 * 
	 * Objective - To check for the presence of the Query History List and History Display Toggle
	 * Steps :
	 * 	1. Select a SPECIFIED Query (which DOES NOT have edited history) and click on its Expand Button
	 * 	2. Check if Show History Button is NOT present
	 */

	@Test(groups = { "HistoryPanelInQueryWithoutEditedHistory",
			"SFB-REQ-27" }, description = "Checks for the presence of the correct elements and functionality for an Invalid Query History")
	public void historyPanelInQueryWithoutEditedHistoryTest() {
		queryWithoutHistoryIndex = queriesSteps.createQueryWithoutHistoryIfUnavailable();
		queriesSteps.verifyInvisibleHistoryButtonForNotExpandedQuery(queryWithoutHistoryIndex);
		queriesSteps.verifyVisibilityOfHistoryButtonForExpandedQuery(queryWithoutHistoryIndex, false);
	}
}
