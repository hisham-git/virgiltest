package tests.Queries;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * Test Class to Check for Delete Query Functionality
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "QueryItemQueryDelete" })
public class QueryItemQueryDeleteTests extends AbstractQueries {
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		commonSteps.getToStudyDashboard();
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
	}
	
	@DataProvider
	public Object[][] confirmData() {
		return new Object[][] { { true }, { false } };
	}

	@Test(groups = { "QueryDeleteWithYesNoConfirmationTest",
			"JamaNA" }, description = "Checks deletion of opened queries if selected 'yes/no' on confirmation pop-up", dataProvider = "confirmData")
	public void queryDeleteWithYesNoConfirmationTest(boolean isConfirmed) {
		queriesSteps.createOpenedQueryIfUnavailable();
		queriesSteps.getQueryCountFor("Open");
		queriesSteps.deleteOpenQuery();
		queriesSteps.verifyConfirmationPopUp();
		queriesSteps.confimQueryAction(isConfirmed);
		queriesSteps.verifyOpenQueriesCount(isConfirmed);
	}
	
	// TODO: Will be implemented after requirement clarification
	/*@Test(groups = { "Query Panel", "Query Item",
			"Query Delete Controls" }, description = "Checks if the Delete Button is not Enabled while editing")
	public void checkDeleteButtonWhileEditing() {
		navigateToSubject("112");// Subject which has no responses
		panel.checkAllCheckBoxesTo(false);
		panel.setCheckBoxValue("Open", true);
		int number_of_queries = panel.getNumberOfQueriesInList();
		if(number_of_queries==0)
			panel.addNewRandomQuery();
		number_of_queries = panel.getNumberOfQueriesInList();
		QueryPanelItem item = panel.getRandomQuery();
		item.expand();
		item.clickEditButton();
		HardVerify.False(item.getDeleteButton().isEnabled(), "Test if the Delete Button is not enabled while editing",
				"Test Passed", "The delete button should not be enabled while Editing.");
	}*/
}
