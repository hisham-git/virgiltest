package tests.StudyDashboardLists;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * 1. To test if all the columns specified in the requirement are present in the
 * Visit List.
 * 2. To test if the sorting feature works  
 * @author anoor
 *
 */
@Test(groups = { "VisitList" })
public class VisitListTests extends AbstractStudyDashboardLists {

	/**
	 * Before all test method execution for this class,
	 * make sure that the page shown is the Visits List Page
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		listSteps.getToStudyDashboard();
		listSteps.chooseStudy(studyName);
		listSteps.chooseSite(siteName);
		listSteps.getToAllVisitsList();
	}
	
	/*
	 * Data Providers
	 */	
	@DataProvider
	public Object[][] colTestsData() {
		return new Object[][] { { "Visit" }, { "Visit Start Date" }, { "Status" }, { "SVID" }, {"Subject"}, { "Language" },
				{ "Site" }, { "Subject Status" }, { "Feedback" } };
	}

	/**
	 * Test Method: Select user defined Study and Site -> goto Visit List
	 * 
	 * Objective - To check if the Visit list contains the required columns
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study 
	 * 3. Select user defined Site 
	 * 4. Clicks on the Visits card 
	 * 5. Cross check the Visit List column headers between user data and UI
	 * 
	 */
	@Test(groups = { "VisitListColumns",
			"JamaNA" }, description = "Cross check the Visit List column headers between user data and UI", dataProvider = "colTestsData")
	public void visitListColumnsTest(String columnName) {
		listSteps.verifyColumnExists(columnName);
	}
	
	@DataProvider
	public Object[][] sortTestData() {
		return new Object[][] { { "Visit", true }, { "Visit", false }, { "Visit Start Date", true }, { "Visit Start Date", false },
				{ "Status", true }, { "Status", false }, { "SVID", true }, { "SVID", false }, /*{ "Subject", true },
				{ "Subject", false },*/ { "Language", true }, { "Language", true }, { "Site", true }, { "Site", false },
				{ "Subject Status", true }, { "Subject Status", false }, { "Feedback", true }, { "Feedback", false } };
	}

	/**
	 * Test Method: Select user defined Study and Site -> goto Visit List
	 * 
	 * Objective - To check if the Visit list is sorted by different columns
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study 
	 * 3. Select user defined Site 
	 * 4. Clicks on the Visits card 
	 * 5. Sort the visit list as per user data 
	 * 6. Validate the sorted list
	 * 
	 */
	@Test(groups = { "VisitSorting",
			"JamaNA" }, description = "Validate if the sorting feature works for the Visit list", dataProvider = "sortTestData")
	public void visitSortingTest(String column, boolean increasingDownwards) {
		listSteps.sort(column, increasingDownwards);
		listSteps.verifySort(column, increasingDownwards);
	}
}
