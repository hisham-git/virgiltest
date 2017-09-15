package tests.Queries;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * QueriesPanelFilterTests
 * Automation Scope Sheet -> Queries Side Bar -> Query Filters
 * 
 * Status : Functionality - 100% Complete
 * TO DO :: Some parameterizations for the tests, 
 * 			Logging functions  
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueriesPanelFilters" })
public class QueriesPanelFiltersTests extends AbstractQueries {
	
	/**
	 * Data provider for the checkQueryFilters method
	 * An array of values for the parameter of (String, int)
	 * where String is the name of the filter and int is the number of queries expected from the filter
	 * 
	 * @return Object[][] where Object is [{String, int}]
	 */
	@DataProvider
	public Object[][] getPrimaryFilters(){
		return new Object[][]{
			{"Open"},
			{"Responded"},
			{"Closed"},
		};
	}
	
	/**
	 * Test Method
	 * Queries Side bar -> Query Filters -> Open, Responded and Closed Test Cases
	 * 
	 * Objective - To check the functionality of the Open, Responded and Closed Filters
	 * Steps :
	 * 	1. Un-Check all available filters
	 * 	2. Check if the Filter exists
	 * 	3. Check if the number of queries in the list is equal to the expected number
	 * 	4. Check if the Query Panel Header shows the correct number of Queries
	 * 	5. For every Query in the Query list, check if the query status is the same as the filter 
	 * 
	 * @param filter - The name of the filter to be tested
	 * 
	 * @param numberOfExpectedQueries - The number of queries that are expected to be available after
	 * 									application of the filter
	 */
	@Test(groups = { "PrimaryFilters",
			"JamaNA" }, description = "Check the presence and functionality of the Query Filters - Open, Closed and Responded", dataProvider = "getPrimaryFilters")
	public void primaryFiltersTest(String status) {
		queriesSteps.verifyCheckBoxExistFor(status);
		queriesSteps.verifyQueryCountWithPanelHeader();
		queriesSteps.verifyEachQueryStatusIs(status);	
	}

	/**
	 * Test Method
	 * Queries Side bar -> Query Filters -> Mine Filter Test Cases
	 * 
	 * Objective - To check the functionality of the Mine Filter
	 * Steps :
	 * 	1. Check all available filter checkboxes
	 * 	2. Check if the Filter exists and Select it
	 * 	3. For every Query in the Query list, check if the query contains atleast one
	 * 	   post by the current user
	 */
	@Test(groups = { "MineFilter",
			"JamaNA" }, description = "Check the presence and functionality of Mine Query Filter")
	public void mineFilterTest() {
		queriesSteps.verifyCheckBoxExistFor("Mine");
		queriesSteps.verifyQueryFor(adminName);
	}
	
	/**
	 * Test Method
	 * Queries Side bar -> Query Filters -> All Queries Filter Test Cases
	 * 
	 * Objective - To check that the All Query Filter is disabled/enabled in the right pages
	 * 			   and check the Correct WebElements are shown on selecting/unselecting the filter
	 * 
	 * Steps : 
	 * 	 1. Set all Filters to TRUE
	 * 	 2. Check if All Queries button is selected but disabled
	 * 	 3. Navigate to the All Subjects Table
	 * 	 4. Open the Queries Panel
	 * 	 5. Check if All Queries button is selected but disabled
	 * 	 6. Close the Queries Panel
	 * 	 7. Click on first Subject in the All : Subject Table to Navigate to a particular Study
	 * 	 8. Check if All Queries button is unselected but enabled
	 * 	 9. Select All Queries button
	 *  10. Check that the queries are correctly displayed
	 */
	@Test(groups = { "AllQueriesFilter",
			"JamaNA" }, description = "Check the All Queries button disabled/enabled state and count")
	public void allQueriesFilterTest() {
		queriesSteps.verifyAllQueriesCheckBoxOnPage("Dashboard");
		queriesSteps.verifyAllQueriesCheckBoxOnPage("SubjectList");
		queriesSteps.verifyAllQueriesCheckBoxOnPage("SubjectDetails");
		queriesSteps.selectAllQueriesCheckBox();
		queriesSteps.verifyQueryCountWithPanelHeader();
	}
}
