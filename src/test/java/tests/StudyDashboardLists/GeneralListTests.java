package tests.StudyDashboardLists;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Test Class that contains all the Tests that are common for all three List Pages
 */
@Test(groups = { "GeneralList" })
public class GeneralListTests extends AbstractStudyDashboardLists {

	/*
	 * Data Provider
	 */
	@DataProvider
	public Object[][] testData() {
		return new Object[][] { { "Subjects-Subjects" }, { "Visits-Visits" }, { "Assessments-Assessments" } };
	}
	
	/**
	 * Objective - To Cross check the Refresh button feature for every List Page
	 * Steps :
	 * 	1. Select Study and Site from the Dashboard
	 *  2. Click the Dashboard Card from the parameter
	 *  3. Click on the Refresh button and verify
	 * 
	 * @param paramCard
	 */
	@Test(groups = { "RefreshButton",
			"JamaNA" }, description = "Cross check the Refresh button for all 3 lists", dataProvider = "testData")
	public void refreshButtonTest(String dashboardList) {
		dashboardSteps.goToList(dashboardList);
		listSteps.verifyRefreshFunctionality();
	}
	
	/**
	 * Objective - To check if the Home page link works for all 3 list pages
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study and user defined Site 
	 * 3. Clicks on the Dashboard card as determined by the user
	 * 4. Clicks on the Home page link 
	 * 5. Verify that the Landing/Home page is opened
	 * 
	 * @param studyName
	 * @param siteName
	 * @param listType Type - Subject/Visit/Assessment
	 * @param filter
	 */
	@Test(groups = { "HomePageLink",
			"JamaNA" }, description = "Cross check the Home page link for 3 lists", dataProvider = "testData")
	public void homePageLinkTest(String dashboardList) {
		dashboardSteps.goToList(dashboardList);
		listSteps.verifyHomeLink();
	}
	
	
	/**
	 * Objective - To check if the Study Profile, Queries and Raters panel link works for all 3 list pages
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study and user defined Site 
	 * 3. Clicks on the Dashboard card as determined by the user
	 * 4. Clicks on the Study Profile panel link 
	 * 5. Verify that the Study Profile panel is opened
	 * 
	 * 
	 * @param studyName
	 * @param siteName
	 * @param listType
	 * @param filter
	 */
	@Test(groups = { "PanelButtons",
			"JamaNA" }, description = "Cross check the Study Profile link for 3 lists", dataProvider = "testData")
	public void panelButtonsTest(String dashboardList) {
		dashboardSteps.goToList(dashboardList);
		listSteps.verifyStudyProfileButton();
		listSteps.verifyRatersButton();
		listSteps.verifyQueriesButton();
	}
	
	/**
	 * Objective - To check if the item counts between header and the list match
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study and user defined Site 
	 * 3. Clicks on the Dashboard card as determined by the user
	 * 4. Count the items on the List 
	 * 5. Compares item counts between header and the list
	 * 
	 * @param studyName
	 * @param siteName
	 * @param listType
	 * @param filter
	 */
	@Test(groups = { "ItemCountFromList",
			"JamaNA" }, dataProvider = "testData", description = "Verify that the Visit counts between header and the list match")
	public void itemCountFromListTest(String dashboardList) {
		dashboardSteps.goToList(dashboardList);
		listSteps.verifyItemCount();
	}
	
	/**
	 * After Every test method, go to study dashboard
	 * 
	 * @param method
	 *            - Method that ended
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		dashboardSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method, result);
	}
}
