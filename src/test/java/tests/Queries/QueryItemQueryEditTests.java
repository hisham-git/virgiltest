package tests.Queries;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * QueriesPanelQuerEditTests
 * Automation Scope Sheet -> Queries Side Bar -> Edit
 * Checks conformity with requirements - SFB-REQ-7, as in https://k8d.jamacloud.com/perspective.req#/items/4194624?projectId=29316
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "QueryItemQueryEdit" })
public class QueryItemQueryEditTests extends AbstractQueries {
	
	private final String edit_entry = "AUTOMATED EDITED TEXT : " + UiHelper.generateRandonUUIDString();
	private final String cancel_edit_query = "This Text should never appear";
	private int editedQueryIndex;
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
//		commonSteps.getToStudyDashboard();
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
	}
	
	/**
	 * Objective : To check for the Save Button Functionality
	 * 
	 * Steps : 
	 * 	1. Navigate to the Appropriate Subject
	 * 	2. Select all Open and Mine Queries from the Filters
	 * 	3. Get a random query from the list
	 * 	4. Click on its Edit button
	 * 	5. Check if the Text in the Text Area is equal to the one the Query showed before expansion
	 * 	6. Enter the new Text in the Edit Text Area
	 * 	7. Click Save
	 * 	8. Check that the Query Shows the text from the previous step
	 * 	9. Check that the Query post time and person did not change
	 */
	@Test(groups = { "SaveEdittedQuery",
			"SFB-REQ-7" }, description = "Checks for the correct functionality for a Query Panel Edit Save")
	public void saveEdittedQueryTest(){	
		queriesSteps.selectQueriesCheckBox(true, "Open", "Mine");
		editedQueryIndex = queriesSteps.createQueryIfUnavailable("Edit");
		queriesSteps.getQueryHeaderAndText(editedQueryIndex);
		queriesSteps.verifyClickingEditButtonNotChangesQueryText();
		queriesSteps.saveQueryWithText(edit_entry, true);
		queriesSteps.verifyQueryTextChanged(edit_entry, true);
		queriesSteps.verifyQueryHeaderNotChanged();
	}
	
	/**
	 * Objective : To check for the Save Button Functionality
	 * 
	 * Steps : 
	 * 	1. Navigate to the Appropriate Subject
	 * 	2. Select all Open and Mine Queries from the Filters
	 * 	3. Get a random query from the list
	 * 	4. Click on its Edit button
	 * 	5. Check if the Text in the Text Area is equal to the one the Query showed before expansion
	 * 	6. Enter the new Text in the Edit Text Area
	 * 	7. Click Cancel
	 * 	8. Check that the Query does not show the text from the previous step
	 */
	@Test(groups = { "CancelEdittedQuery",
			"SFB-REQ-7" }, description = "Checks for the correct functionality for a Query Panel Edit Cancel")
	public void cancelEdittedQueryTest(){
		queriesSteps.selectQueriesCheckBox(true, "Open", "Mine");
		editedQueryIndex = queriesSteps.createQueryIfUnavailable("Edit");
		queriesSteps.getQueryHeaderAndText(editedQueryIndex);
		queriesSteps.verifyClickingEditButtonNotChangesQueryText();
		queriesSteps.saveQueryWithText(cancel_edit_query, false);
		queriesSteps.verifyQueryTextChanged(cancel_edit_query, false);
	}
}
