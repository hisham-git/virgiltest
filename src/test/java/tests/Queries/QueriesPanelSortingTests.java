package tests.Queries;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * QeriesPanelSortingTests
 * Automation Scope Sheet -> Queries Side Bar -> Sort By
 * 
 * Status : Functionality ->  100% complete
 * @author Syed Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueriesPanelSorting", "GroupDependent" })
public class QueriesPanelSortingTests extends AbstractQueries {
	
	/**
	 * Test Method
	 * Queries Side Bar -> Sort By -> Presence of the Sorting or Grouping links
	 * 
	 * Objective - Checks for the correct Sorting or Grouping Functionality for the SortBy button
	 * Steps : 
	 * 	1. Checks if the Sort By Button is Visible
	 * 	2. Checks if the Sort By Button is Clickable
	 * 	3. Click the Sort By Button
	 * 	4. Check that the Sort By Links contain Age and Context
	 */
	@Test(groups = { "PresenceOfSortingButton",
			"JamaNA" }, description = "Checks if Queries Panel Sorting Button Exits")
	public void presenceOfSortingButtonTest() {
		queriesSteps.verifySortingControlVisibleAndEnable();
		queriesSteps.verifySortingControlOptions();
	}
	
	/**
	 * Data Provider
	 * 
	 * @return Object[][] where Object = [{String type, boolean ascending_or_descending, String message}, {......}, ....]
	 */
	@DataProvider
	public Object[][] sortTypes(){
		return new Object[][]{
			{"Age" , false, "Sort by the most recent set to last"},
			{"Age" , true,  "Sort by the most recent set to first"},
//			 TODO: UNCOMMENT or gives ERROR otherwise, due to bug
//			{"Context" , true, "Group By Score > Assessment > Visit > Subject > Site"},
//			{"Context" , false, "Group By Site > Subject > Visit > Assessment > Score" }
		};
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Sort By -> Sort By Age, Group By Context
	 * 
	 * Objective - Checks for the correct Sorting or Grouping Functionality for the SortBy button
	 * Steps : 
	 * 	1. Set all Filters to TRUE
	 * 	2. Click the Sort By button to the type and ascending or descending button
	 * 	3. Get the Queries List
	 * 	4. If the type is Age, then go to sortingTest
	 * 	5. If the type is Context, then go to groupingTest
	 * 
	 * @param type STRING - The type to sort or group with, currently they are Age and Context
	 * @param ascending BOOLEAN - FOR AGE
	 * 							  -> TRUE  -> The queries should be ordered with the most recent query first
	 * 							  -> FALSE -> The queries should be ordered with the most latest query first
	 * 							- FOR CONTEXT
	 * 							  -> TRUE  -> Group BY : Site > Subject > Visit > Assessment > Score
	 * 							  -> FALSE -> Group BY : Score > Assessment > Visit > Subject > Site
	 * @paran message STRING - The message to display
	 */
	@Test(groups = { "SortingWithButton",
			"JamaNA" }, description = "Checks if Queries Panel Sorting Button Sorts Correctly", dependsOnGroups = {
					"PresenceOfSortingButton" }, dataProvider = "sortTypes")
	public void sortingWithButtonTest(String type, boolean sortOrder, String message) {
		queriesSteps.checkPrimaryCheckBoxesToTrue();
		queriesSteps.clickSortByButtonAndVerifySorting(type, sortOrder);
	}
}
