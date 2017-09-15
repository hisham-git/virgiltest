package tests.Queries;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.Log;

/**
 * Test to Check for the Add Query Functionality
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryItemQueryAdd" })
public class QueryItemQueryAddTests extends AbstractQueries {

	private final String  INPUT_TEXT_QUERY = "AUTOMATED TEXT : " + UiHelper.generateRandonUUIDString();

	
	/**
	 * Before every test method: 
	 * 1. Open Query Panel
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		commonSteps.getToStudyDashboard();
	}
	
	/**
	 * Objective - Tests for the Add Query Button is Disabled /Enabled in the Study Dashboard
	 * 
	 * Steps :
	 * 	1. Get to the Study Dashboard
	 * 	2. Test if the Add Query Button is Disabled
	 */
	@Test(groups = { "AddQueryButtonInDashboard",
			"JamaNA" }, description = "Checks if the Add Query Button is Disabled in the Study Dashboard")
	public void addQueryButtonInDashboardTest() {
		queriesSteps.verifyAddQueryButtonIsDisabled();
	}
	
	/**
	 * Objective - Tests for the Add Query Button is Disabled /Enabled
	 * in the SubjectList & details page
	 * 
	 * Steps :
	 * 	1. Get to the Subject List page
	 * 	2. Verify if the Add Query Button is Disabled
	 * 	3. Store Site, Subject name for first item on subject list page
	 * 	4. Click on the first Subject item
	 * 	5. Verify 'Add Query' button is present on Subject details page
	 * 	6. Click on Add Query button.
	 * 	7. Verify new query panel elements for Subject query
	 * 	8. Verify new query panel contexts for Subject query
	 */
	@Test(groups = { "AddQueryButtonInSubjectListAndDetails",
			"JamaNA" }, description = "Checks for the Add Query Button's Enabled/Disabled properties in the List and Details Pages. Also Checks for the appropriate Elements' presence in these pages.")
	public void addQueryButtonInSubjectListAndDetailsTest() {
		queriesSteps.verifyDisabledAddQueryButtonInListPage("Subjects");
		queriesSteps.getDetailsAndClickFirstItemFromList("Subject");
		queriesSteps.verifyEnabledAddQueryButtonInDetailsPage("Subject");
		queriesSteps.verifyPresenceOfAddQueryPanelElementsInDetailsPage("Subject");
		queriesSteps.verifyCorrectContextValues("Subject");
	}

	/**
	 * Objective - Tests for the Add Query Button is Disabled /Enabled
	 * in the Visit list & details page
	 * 
	 * Steps :
	 * 	1. Get to the Visit List page
	 * 	2. Verify if the Add Query Button is Disabled
	 * 	3. Store Site, Subject, Visit name for first item on visit list page
	 * 	4. Click on the first Visit item
	 * 	5. Verify 'Add Query' button is present on Visit details page
	 * 	6. Click on Add Query button.
	 * 	7. Verify new query panel elements for Visit query
	 * 	8. Verify new query panel contexts for Visit query
	 */
	@Test(groups = { "AddQueryButtonInVisitListAndDetails",
			"JamaNA" }, description = "Checks for the Add Query Button's Enabled/Disabled properties in the Visit List and Visit Details Page. Also Checks for the appropriate Elements' presence in these pages.")
	public void addQueryButtonInVisitListAndDetailsTest() {
		queriesSteps.verifyDisabledAddQueryButtonInListPage("Visits");
		queriesSteps.getDetailsAndClickFirstItemFromList("Visit");
		queriesSteps.verifyEnabledAddQueryButtonInDetailsPage("Visit");
		queriesSteps.verifyPresenceOfAddQueryPanelElementsInDetailsPage("Visit");
		queriesSteps.verifyCorrectContextValues("Visit");
	}

	/**
	 * Objective - Tests for the Add Query Button is Disabled /Enabled
	 * in the Assessment list & details page
	 * 
	 * Steps :
	 * 	1. Get to the Assessment List page
	 * 	2. Verify if the Add Query Button is Disabled
	 * 	3. Store Site, Subject, Visit, Assessment, rater name for first item on Assessment list page
	 * 	4. Click on the first Assessment item
	 * 	5. Verify 'Add Query' button is present on Assessment details page
	 * 	6. Click on Add Query button.
	 * 	7. Verify new query panel elements for Assessment query
	 * 	8. Verify new query panel contexts for Assessment query
	 */
	@Test(groups = { "AddQueryButtonInAssessmentListAndDetails",
			"JamaNA" }, description = "Checks for the Add Query Button's Enabled/Disabled properties in the Assessment List and Assessment Details Page. Also Checks for the appropriate Elements' presence in these pages.")
	public void addQueryButtonInAssessmentListAndDetailsTest() {
		queriesSteps.verifyDisabledAddQueryButtonInListPage("Assessments");
		queriesSteps.getDetailsAndClickFirstItemFromList("Assessment");
		queriesSteps.verifyEnabledAddQueryButtonInDetailsPage("Assessment");
		queriesSteps.verifyPresenceOfAddQueryPanelElementsInDetailsPage("Assessment");
		queriesSteps.verifyCorrectContextValues("Assessment");

		/*
		 * HardVerify.Equals( contexts.get("Rater"), rater_name,
		 * "The Site selected and the Site shown in the Add Query Panel are different. The Site"
		 * );
		 *//*
		*//*
		 * HardVerify.Equals( contexts.get("SOME OPTION"), rater_name,
		 * "The Site selected and the Site shown in the Add Query Panel are different. The Site"
		 * );
		 */
	}
	
	@Test(groups = { "CreateQuery",
			"JamaNA" }, description = "Checks for the Add Query Button's Create button functionalities")
	public void createQueryTest() {
		// navigateToSubject("abc");
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.verifyDisabledCreateButtonByDefault();
		queriesSteps.verifyEnabledCreateButtonAfterEnteringText(INPUT_TEXT_QUERY);
		queriesSteps.verifyDisabledCreateButtonAfterClearingText("");
		queriesSteps.verifyCreateNewQuery(INPUT_TEXT_QUERY);
		queriesSteps.verifyClosingOfAddQueryPanel("Creating");
	}
	
	@Test(groups = { "CancelQuery",
			"JamaNA" }, description = "Checks for the Add Query Button's Cancel button functionalities")
	public void cancelQueryTest() {
		// navigateToSubject("abc");
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.verifyQueryNotAddedAfterCancel(INPUT_TEXT_QUERY);
		queriesSteps.verifyClosingOfAddQueryPanel("Cancelling");
	}
	
// TODO: Will be implemented after Requirement clarification
	/*@Test(groups = { "Query Panel", "Add Query"}, description = "Checks for the Add Query Panel's collapsing on clicking on another query item in the Query Panel")
	public void checkOtherQueryExpansion(){
		panel.clickClose();
		DashboardList dashboardList = dashboard.clicksOnCard("Subjects-Subjects");
		UiHelper.click(dashboardList.getFirstItemFromList());
		//TODO : Use PageObjects
		Browser.getDriver().findElement(By.xpath("//a[@title='Show queries']")).click();
		UiHelper.checkPendingRequests(Browser.getDriver());
		panel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		//END----------------------
		add_query_panel = panel.addQuery();
		QueryPanelItem random_query_from_list = panel.getQueriesFromList().get(panel.getRandomQueryIndex());
		random_query_from_list.expand();
		Log.logStep("Test if the the Add Query Panel's collapses on clicking on another query item in the Query Panel...");
		HardVerify.False(add_query_panel.isOpened(), "The Add Query panel was still open even after another Query was expanded.");
		Log.logStep("Test Passed.");
	}*/

}
