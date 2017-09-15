package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 *  
 * Test Objective(s):
 * - To test all the possible navigations through Assessment Details page
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "Navigation" })
public class NavigationTests extends AbstractAssessmentDetails {
	
	/**
	 * Before Test class navigate to Assessments List
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName,
				siteName);
	}
	
	
	/**
	 * After every test:
	 * 1. Get to StudyDashboard from Breadcrumbs study link 
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		commonSteps.getToStudyDashboardViaStudyLink();
		Log.logTestMethodEnd(method, result);
	}
	
	
	/**
	 * @return Assessment Form Name, Visit Name and Form Thumbnail index 
	 */
	@DataProvider
	public Object[][] navViaSubjectListData(){
		return new Object[][] { 
			{ "Visit1", 0 },
			{ "Visit8", 0 }
		};
	}
	
	/**
	 * Steps:
	 * 1. Goto Subject List
	 * 2. Search & select the respective subject
	 * 3. Select the respective visit
	 * 4. Select the respective Form thumnail using the index
	 * 
	 * @param subjectName to be used to Search in the Subject List
	 * @param visitName Visit  to be used to pick to assessment form
	 * @param index index of the Form thumbnail
	 */
	@Test(groups = { "NavigationViaSubjectList",
			"JamaNA" }, description = "Check the navigation to Assessment Details through a Subject.", dataProvider = "navViaSubjectListData")
	public void navigationViaSubjectListTest(String visitName, int index) {
//		dashboardListSteps.getToAllSubjectsList();
		dashboardListSteps.openAllSubjects();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		detailsSteps.clickVisitRowFor(visitName);
		detailsSteps.clickAssessmentThumbnailAtIndex(index);
		detailsSteps.verifyAssessmentDetailsIsOpenedVia("Subject");
	}
	
	/**
	 * 
	 * @return SVID as Column Name and the SVID value 
	 */
	@DataProvider
	public Object[][] navViaVisitListData(){
		return new Object[][] { 
			{ "Visit1", 0 },
			{ "Visit8", 0 }
		};
	}
	
	/**
	 * Steps:
	 * 1. Go to Visit List
	 * 2. Search the respective visit
	 * 3. Select the appropriate Form thumbnail to go to Assessment details page
	 *  
	 * @param colName Visit List column Name to search the visit
	 * @param value Value of the column to be used in the search
	 */
	@Test(groups = { "NavigationViaVisitList",
			"JamaNA" }, description = "Check the navigation to Assessment Details through a Visit.", dataProvider = "navViaVisitListData")
	public void navigationViaVisitListTest(String visitName, int index) {
//		dashboardListSteps.getToAllVisitsList();
		dashboardListSteps.openAllVisits();
//		detailsSteps.getVisitRow(colName, value);
//		detailsSteps.getToVisitDetails(value);
		detailsSteps.getToVisitDetails(visitName, subjectName);
		detailsSteps.verifyVisitDetailsIsOpened(visitName);
		detailsSteps.clickAssessmentThumbnailAtIndex(index);
		detailsSteps.verifyAssessmentDetailsIsOpenedVia("Visit");
	}

	/**
	 * 
	 * @return Assessment Form Name and SVID 
	 */
	@DataProvider
	public Object[][] navViaAssessmentListData() {
		return new Object[][] {
			{ "Visit1", "CDR" },
			{ "Visit8", "CGI-I" } 
		};
	}
	
	/**
	 * Steps:
	 * 1. Goto the Assessment List
	 * 2. Search and select the assessment using form name and svid
	 * 
	 * @param formName Assessment Form Name
	 * @param svid 
	 */
	@Test(groups = { "NavigationViaAssessmentList",
			"JamaNA" }, description = "Check the navigation to Assessment Details through the Assessment List.", dataProvider = "navViaAssessmentListData")
	public void navigationViaAssessmentListTest(String visitName, String assessmentName) {
		// dashboardListSteps.getToAllAssessmentsList();
		dashboardListSteps.openAllAssessments();
		// detailsSteps.getToAssessmentDetails(formName, svid);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
		detailsSteps.verifyAssessmentDetailsIsOpenedVia("Assessmet List");
	}

	/**
	 * Steps:
	 * 1. From Dashboard open the Query panel
	 * 2. Search and select any Assessment Query
	 * 3. Verify that the respective assessment details page has been loaded
	 */
	@Test(groups = { "NavigationViaAssessmentQuery",
			"JamaNA" }, description = "Check the navigation to Assessment Details through an Assessment Query.")
	public void navigationViaAssessmentQueryTest() {
//		commonSteps.getToStudyDashboard();
		detailsSteps.openQuerySidePanel();
		if (detailsSteps.getAssessmentQuery()) {
			detailsSteps.verifyAssessmentDetailsIsOpenedVia("Assessment Query");
		} else {
			Log.logInfo("No Assessment Query found in the Query list.");
			detailsSteps.closeQuerySidePanel();
		}
	}	
}
