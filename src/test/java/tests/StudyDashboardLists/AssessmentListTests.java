package tests.StudyDashboardLists;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Tests.AssessmentListSteps;

/**
 * All Major Tests for the Assessments List Page from the Study Dashboard
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "AssessmentList" })
public class AssessmentListTests extends AbstractStudyDashboardLists {

	private final String[] assessmentcards = new String[] { "Assessments", "Complete", "With Overrides",
			"Paper Transcription", "Paper Transcription w/o Source", "With Queries", "Unread Feedback" }; 	
	
	/*
	 * Reference for Step classes used
	 */
	private AssessmentListSteps assessmentListSteps = new AssessmentListSteps();
	
	/**
	 * Before all test method execution for this class,
	 * select appropriate study  & site
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		listSteps.getToStudyDashboard();
		listSteps.chooseStudy(studyName);
		listSteps.chooseSite(siteName);
	}
	
	@DataProvider
	public Object[][] columnHeaders() {
		return new Object[][] { { "Assessment" }, { "Type" }, { "Blinded" }, { "Version" }, { "Status" }, { "Rater" },
				{ "Visit" }, { "Initial Version Start Date" }, { "SVID" }, { "Subject" }, { "Site" }, { "Subject Status" },
				{ "Feedback" } };
	}
	
	/**
	 * Objective - To check for the presence of an array of columns that should be present in the Assessment List
	 * Steps :
	 * 	1.	Try to locate the Column with the text that is given in the parameter and return it.
	 *		If the Column has no text, then locate it by the title
	 * 	2.	Check if the returned value is null or not
	 * 
	 * @param columnName String - The name of the Column to check for. Requires DataProvider
	 */
	@Test(groups = { "AssessmentsTableColumnHeaderElement",
			"SFB-REQ-32" }, description = "Checks for the presence of a given column as per the requirements on SFB-REQ-32", dataProvider = "columnHeaders")
	public void assessmentsTableColumnHeaderElementTest(String columnName) {
		listSteps.getToAllAssessmentsList();
		assessmentListSteps.verifyAssessmentsTableColumnHeaderElement(columnName);
	}
	
	@DataProvider
	public Object[][] sortData() {
		return new Object[][] {
			{ "Assessment", true }, { "Assessment", false }, { "Type", true }, { "Type", false },
			{ "Version", true }, { "Version", false }, { "Status", true }, { "Status", false },
			{ "Rater", true }, { "Rater", false }, { "Visit", true }, { "Visit", false },
			{ "Initial Version Start Date", true }, { "Initial Version Start Date", false }, { "SVID", true }, { "SVID", false },
			/*
			 * TODO: UNCOMMENT, need to clarify sorting { "Subject", true },
			 * { "Subject", false },
			 */ 
			{ "Site", true }, { "Site", false }, { "Subject Status", true }, { "Subject Status", false },
			{ "Feedback", true }, { "Feedback", false } 
		};
	}
	
	/**
	 * Objective - To check for the correct redirection after clicking the column for the desired Sort Order
	 * Steps :
	 * 	1. Click the Column to be sorted to get the desired sort order
	 * 	2. Iteratively check if every item maintains the sorting order
	 * 
	 * @param assessmentName String - The name of the column to sort. Requires DataProvider
	 * @param increasingDownwards boolean - true - The values should increase as the list moves downwards
	 * 										false - The values should decrease as the list moves downwards. Requires DataProvider
	 */
	@Test(groups = { "AssessmentsTableSorting",
			"SFB-TC-915" }, dependsOnGroups = "AssessmentsTableColumnHeaderElement", description = "Checks for the correct sorting of a given column in the List as per the Test Case SFB-TC-915", dataProvider = "sortData")
	public void assessmentsTableSortingTest(String column, boolean increasingDownwards) {
		assessmentListSteps.getToStudyDashboard();
		listSteps.getToAllAssessmentsList();
		assessmentListSteps.sortAssessmentListForColumn(column, increasingDownwards);
		assessmentListSteps.verifySorting(column, increasingDownwards);
	}
	
	/**
	 * Test Method:  
	 * -> Select user defined Study and Site -> goto Assessment List 
	 * 
	 * Objective - To check if the Assessment list contains the Type column and all Types of Forms
	 * are shown
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study 
	 * 3. Select user defined Site 
	 * 4. Clicks on the Assessments card 
	 * 5. Search for an Assessment column Type 
	 * 6. If exists verify that the list has different types of assessments 
	 * 
	 */
	@Test(enabled = false, groups = { "TypeColumnContainsDifferentAssessmentTypes",
			"JamaNA" }, dependsOnGroups = "AssessmentsTableColumnHeaderElement", description = "Verify that the list has different types of assessments")
	public void typeColumnContainsDifferentAssessmentTypesTest() {
		listSteps.getToAllAssessmentsList();
		assessmentListSteps.verifyDifferentTypeAssessmentsForTypeColumn();
	}
	
	/**
	 * Objective - To check for the correct redirection after clicking a given item (chosen through the Assessment Name and SVID) in the list
	 * Steps :
	 * 	1. Locate the Row matching the NAME and SVID and click it
	 * 	2. Check if the returned Page is the Assessment Details Page or not
	 * 
	 * @param assessmentName String - The name of the Assessment to click. Requires DataProvider
	 * @param svid String - The SVID of the Assessment to click. Requires DataProvider
	 * 
	 */	
	@Test(groups = { "AssessmentsTableLinks",
			"JamaNA" }, description = "Checks for the correct redirection after clicking a given item in the Table", invocationCount = 3)
	public void assessmentsTableLinksTest() {
		assessmentListSteps.getToStudyDashboard();
		assessmentListSteps.getAssessmentsListFor(assessmentcards[0]);
		assessmentListSteps.getAssessmentDetailsOfRandomAssessment();
		assessmentListSteps.verifyClickedAssessmentDetailsPage();
	}
	
	@DataProvider
	public Object[][] assessmentColVal() {
		return new Object[][] { { "Type", "PRO" }, { "Type", "ClinRO" }, { "Type", "ObsRO" } };
	}
	
	/**
	 * Test Data: User provides the Assessment Type to search and verify details of that type.  
	 * Test Method:  
	 * -> Select user defined Study and Site
	 * -> goto Assessment List
	 * -> Search and clicks on the Assessment
	 * 
	 * Objective - To check if the Assessment list contains an specific Assessment type
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Select user defined Study 
	 * 3. Select user defined Site 
	 * 4. Clicks on the Assessments card 
	 * 5. Search for an Assessment with user defined Type 
	 * 6. Clicks on the Assessments if found 
	 * 7. Verify that the details info loads the specific Assessment
	 */	
	@Test(groups = { "UserDefinedAssessmentType",
			"JamaNA" }, description = "Verify that the details page loads the specific Assessment", dataProvider = "assessmentColVal")
	public void userDefinedAssessmentTypeTest(String colName, String colValue) {
		assessmentListSteps.getToStudyDashboard();
		assessmentListSteps.getAssessmentsListFor(assessmentcards[0]);
		// assessmentListSteps.getAssessmentsListWithItemsFor(assessmentcards[3]);
		if (assessmentListSteps.getUserAssessment(colName, colValue))
			assessmentListSteps.verifyClickedAssessmentDetails(colName, colValue);
	}
}
