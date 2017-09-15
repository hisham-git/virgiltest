package tests.Queries;

import org.testng.annotations.Test;

/**
 * QueriesPanelControlsTest
 * Automation Scope Sheet -> Queries Side Bar -> Sidebar Identity, HyperLink to Pull Right, Queries panel heading,
 *												 Refresh button
 * Status : COMPLETE
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "QueriesPanelControls", "GroupDependent" })
public class QueriesPanelControlsTests extends AbstractQueries {
	
	/**
	 * Test Method
	 * Queries Side Bar -> Sidebar Identity
	 * 
	 * Objective - To check if the Query Panel Opens Correctly
	 * Steps : 
	 * 	1. Select Predefined Study
	 * 	2. Select Predefined Site
	 * 	3. Click on the Queries Panel Link in the Toolbar
	 * 	4. Check if the Query Panel has opened
	 */
	@Test(groups = { "PresenceOfQueryPanel", "JamaNA" }, description = "Checks if the query panel opens correctly")
	public void presenceOfQueryPanelTest() {
		queriesSteps.verifyQueryPanelIsOpen();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Queries Panel Heading
	 * 
	 * Objective - To check that the Panel's Heading displays the correct count
	 * Steps :
	 * 	1. Check if the query panel is Open, if not then open it
	 * 	2. Check if the Queries Heading is correct
	 */
	@Test(groups = { "QueryPanelHeadingText",
			"JamaNA" }, description = "Checks Queries Panel Heading", dependsOnGroups = { "PresenceOfQueryPanel" })
	public void queryPanelHeadingTextTest() {
		queriesSteps.verifyQueryPanelHeaderText();
	}
	
	/**
	 * Test Method
	 * Queries Side Bar -> Hyperlink to pull right
	 * 
	 * Objective - To check that the Panel's Close button works correctly
	 * Steps :
	 * 	1. Check if the query panel is Open, if not then open it
	 * 	2. Click the Panel's Close button
	 * 	3. Check if the Query Panel has closed
	 */
	@Test(groups = { "QueryPanelClose",
			"JamaNA" }, description = "Checks if the query panel closes correctly", dependsOnGroups = {
					"PresenceOfQueryPanel" })
	public void queryPanelCloseTest() {
		queriesSteps.verifyQueryPanelIsClosed();
	}

	/**
	 * Test Method
	 * Queries Side Bar -> Refresh Button
	 * 
	 * Objective - To check that the Panel's Refresh button functions correctly
	 * Steps :
	 * 	1. Open the Query Panel
	 * 	2. Click the Refresh Button
	 * 	3. Check if there have been any new Connections while refreshing
	 * 	4. Check if the number of Queries after refreshing are the same
	 */
	@Test(groups = { "QueryPanelRefreshButton",
			"JamaNA" }, description = "Checks if Queries Panel Refresh Button Exists and Works Correctly", dependsOnGroups = {
					"PresenceOfQueryPanel" })
	public void queryPanelRefreshButtonTest() {
		queriesSteps.verifyPresenceOfQueryRefreshButton();
		queriesSteps.storeQueryAndConnectionDataBeforeRefresh();
		queriesSteps.updateQueryAndConnectionDataByRefresh();
		queriesSteps.verifyNewAjaxCallsAfterRefresh();
		queriesSteps.verifyQueryCountUnchangedAfterRefresh();	
	}
}
