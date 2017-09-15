package tests.StudyDashboardLists;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 * All Major Tests for the Subject List Page from the Study Dashboard
 * 
 * @author Syed A. Zawad
 *
 */
@Test(groups = { "SubjectList" })
public class SubjectListTests extends AbstractStudyDashboardLists {

	/**
	 * Before all test method execution for this class,
	 * make sure that the page shown is the Subject List Page
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		listSteps.getToStudyDashboard();
		listSteps.chooseStudy(studyName);
		listSteps.chooseSite(siteName);
		expectedSites = listSteps.storeSitesAvailable();
		listSteps.getToAllSubjectsList();
	}
	
	@DataProvider
	public Object[][] columns() {
		return new Object[][] { { "Subject" }, { "Completed Visits" }, { "Language" }, /*TODO: UNCOMMENT FOR ERROR : { "Initials" },
				{ "Date of Birth" }, { "Age" },*/{ "Site" }, { "Status" }, { "Active Visits" }, { "Visits in Edit" } };
	}
	
	/**
	 * Objective - To check for the presence of an array of columns that should
	 * be present in the Subject List
	 * 
	 * Steps : 
	 * 	1. Try to locate the Column with the text that is given in the parameter and return it. If the Column has no text, then locate it by the title
	 *  2. Check if the returned value is null or not
	 * 
	 * @param columnName
	 *            String - The name of the Column to check for. Requires
	 *            DataProvider
	 */
	@Test(groups = { "SubjectsTableColumnHeaderElement",
			"JamaNA" }, description = "Checks for the presence of a given column as per the requirements on SFB-REQ-34", dataProvider = "columns")
	public void subjectsTableColumnHeaderElementTest(String columnName) {
		listSteps.verifyColumnExists(columnName);
	}
	
	/*
	 * Data Providers
	 */
	@DataProvider
	public Object[][] sortData() {
		return new Object[][] { /*TODO: UNCOMMENT : { "Subject", true }, { "Subject", false },*/ 
			{ "Completed Visits", true }, { "Completed Visits", false }, { "Language", true }, { "Language", false },
			/*TODO: UNCOMMENT -> { "Initials", true }, { "Initials", false },
			{ "Date of Birth", true }, { "Date of Birth", false }, { "Age", true }, { "Age", false },*/
			{ "Site", true }, { "Site", false }, { "Status", true }, { "Status", false }, { "Active Visits", true }, 
			{ "Active Visits", false }, { "Visits in Edit", true }, { "Visits in Edit", false } };
	}
	
	/**
	 * Objective - To check for the correct redirection after clicking the
	 * column for the desired sort order
	 * 
	 * Steps : 
	 * 	1. Click the Column to be sorted to get the desired sort order
	 *  2. Iteratively check if every item maintains the sorting order
	 * 
	 * @param assessmentName
	 *            String - The name of the column to sort. Requires DataProvider
	 * @param increasingDownwards
	 *            boolean - true - The values should increase as the list moves
	 *            downwards false - The values should decrease as the list moves
	 *            downwards. Requires DataProvider
	 */
	@Test(groups = { "SubjectsTableSort",
			"JamaNA" }, description = "Checks for the correct sorting of a given column in the List as per the Test Case SFB-TC-912", dataProvider = "sortData")
	public void subjectsTableSortTest(String column, boolean increasingDownwards) {
		listSteps.sort(column, increasingDownwards);
		listSteps.verifySort(column, increasingDownwards);
	}
	
	/**
	 * Objective - To check for the correct redirection after clicking a given
	 * item (chosen through the Subject Name) in the list 
	 * Steps :
	 *  1. Locate the Row matching the Subject name and click it
	 *  2. Check if the returned Page is the Subject Details Page or not
	 * 
	 * @param assessmentName
	 *            String - The name of the Subject to click. Requires
	 *            DataProvider
	 */
	@Test(groups = { "SubjectsTableLinks",
			"JamaNA" }, description = "Checks for the correct redirection after clicking a given item in the Table", invocationCount = 3)
	public void subjectsTableLinksTest() {
		listSteps.clickOnRandomRow();
		listSteps.verifySubjectDetailsPageIsOpen();
		listSteps.getToAllSubjectsList();
	}
	
	/**
	 * Objective - To check for the correct functionality of the Add Subject
	 * button in the Subject Lists Page
	 * 
	 * Steps : 
	 * 	1. Check the presence of the Add Subject button 
	 * 	2. Check for the correct list of Sites available for the Add Subject button 
	 * 	3. Select a chosen site
	 * 	4. Check if the correct page was returned
	 * 
	 */
	@Test(groups = { "SubjectsTableAddButton",
			"JamaNA" }, description = "Checks for the correct functionality of the Add Subject button and its Site Options in the Subject Lists Page")
	public void subjectsTableAddButtonTest() {
		listSteps.verifySubjectAddButtonExists();
		listSteps.verifyAddSubjectButtonsSiteList(expectedSites);
//		listSteps.addSubjectToRandomSite();
		listSteps.addSubjectToSite("");
		listSteps.verifyAddNewSubjectPageIsOpened();
		listSteps.getToAllSubjectsList();
	}
	
	/**
	 * Objective - To check for the presence of the gender icon in the Subject
	 * Lists Page
	 * 
	 * Steps : 
	 * 	1. Get the first Row from the List 
	 * 	2. Check if the row contains and image with a source filename containing "gender"
	 * 
	 */
	@Test(groups = { "SubjectsTableGenderIcon",
			"JamaNA" }, description = "Checks for the presence of the gender icon in the Subject Lists Page")
	public void subjectsTableGenderIconTest() {
		listSteps.verifyGenderIcon();
	}
}
