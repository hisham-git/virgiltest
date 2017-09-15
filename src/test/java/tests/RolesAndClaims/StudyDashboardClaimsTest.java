package tests.RolesAndClaims;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.utils.dataProvider.ExcelDataMapper;
import steps.Tests.RolesAndClaimsSteps;

public class StudyDashboardClaimsTest extends AbstractRolesAndClaimsTest {
	
	/*
	 * The user's credentials
	 */
//	private String userType, userFullNmae, userName, userPassword, portalURL;
	
	/*
	 * The Excel File Reader for the Roles and Claims matrix
	 */
//	private ExcelDataReader xlReader;
	
	/*
	 * The Steps Class as specified by the MedAvante Test Creation Guide
	 */
	private RolesAndClaimsSteps rolesAndClaimsSteps;
	private boolean hasClaim;
	private Map<String, String> userData;
	
	/**
	 * Sets the User Type and credentials that will be required for a particular instance of the test
	 * 
	 * @param userType 
	 * 			- String - the User Role, E.g. "MedAvante User - Type 2" or "Site Rater - Type 3"		
	 */
//	@Factory(dataProvider = "getExcelData", dataProviderClass = ExcelDataMapper.class)
	public StudyDashboardClaimsTest(Map<String, String> userData) {
		this.userData = userData;
	}
	
	/**
	 * Before all methods, log
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		Log.logInfo(String.format("Starting with [%s : %s]", userData.get("UserType"), userData.get("UserFullName")));
		rolesAndClaimsSteps = new RolesAndClaimsSteps(Browser.getDriver());
		rolesAndClaimsSteps.goToPortalLoginPage(userData.get("Portal"));
		rolesAndClaimsSteps.loginWithCredentials(userData.get("UserName"), userData.get("Pass"));
		detailsSteps.setTestCredentials(userData.get("UserName"), userData.get("Pass")); //TODO: Will be excluded later
		detailsSteps.getToDashboard();
		rolesAndClaimsSteps.selectStudyAndSite(userData.get("Study"), userData.get("Site"));
	}
	
	/**
	 * Log before every method & parse value from excel sheet for Claim under test
	 * 
	 * @param method - Method - the method that will execute next
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		try {
			hasClaim = Boolean.parseBoolean( userData.get(method.getName()) );
			Log.logInfo(String.format("[%s : %s] has access to [%s] claim found [%b]", userData.get("UserType"), userData.get("UserFullName"),
					method.getName(), hasClaim));
		} catch (Exception e) {
			throw new SkipException("Couldn't parse claim values from 'ExcelDataMapper' matrix. Skipping tests...");
		}
	}
	
	/**
	 * Objective - Check user with claim can add subject from Subject list page  
	 * 
	 * Steps:
	 * 1. Navigate to Subject list
	 * 2. Verify if user has claim:
	 * 		a. 'Add Subject' button is visible
	 */
	@Test(enabled = false, priority = 1, groups = { "Roles And Claims", "Subject Details",
			"JamaNA" }, description = "Verify user can manage any Subject")
	public void canManageSubjects() {
		studyDashboardSteps.getToAllSubjectsList();
		rolesAndClaimsSteps.verifyAddSubjectIsPresent(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject name, Visit to be deleted, Visit to be added 
	 */
	@DataProvider
	public Object[][] canManageVisitsData(){
		return new Object[][] { 
			{ "Subject_081216a", "Visit 3", "Visit 5" }
		};
	}
	/**
	 * Objective - Visibility of add unscheduled visit, add, delete, edit or cancel buttons for a visit.
	 * 
	 * Steps:
	 * 1. Navigate to Subject list
	 * 2. Select given subject and go to details page 
	 * 3. Verify if user has claim:
	 * 		a). 'Add unscheduled visit' button is visible
	 * 		b). 'Add Visit' button is visible for unassigned visit
	 * 		c). 'Delete Visit' button is visible for pending visit
	 */
	@Test(enabled = false, priority = 2, groups = { "Roles And Claims", "Subject Details",
	"JamaNA" }, description = "Verify user can manage any Visit", dataProvider = "canManageVisitsData")
	public void canManageVisits(String subjectName, String deleteVisitName, String addVisitName) {
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		rolesAndClaimsSteps.verifyAddUnscheduledVisitIsPresent(hasClaim);
		detailsSteps.clickVisitRowFor(deleteVisitName);
		rolesAndClaimsSteps.verifyRemoveVisitIsPresent(hasClaim);
		detailsSteps.clickVisitRowFor(addVisitName);
		rolesAndClaimsSteps.verifyAddVisitIsPresent(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject name, Visit to be deleted, Visit to be added 
	 */
	@DataProvider
	public Object[][] canDisableEnableProObsroFormsData(){
		return new Object[][] { 
			{ "Subject_081216a" }
		};
	}
	
	/**
	 * Objective - User can Disable/Enable PRO/ObsRO forms in Subject details page for claim
	 * 
	 * Steps:
	 * 1. Navigate to Subject list
	 * 2. Select given subject and go to details page
	 * 3. Click 'Edit details' button
	 * 3. Verify if user has claim:
	 * 		a). 'Disable PRO' check-box can be checked/unchecked
	 * 		b). 'Disable ObsRO' check-box can be checked/unchecked
	 */
	@Test(enabled = true, priority = 3, groups = { "Roles And Claims", "Subject Details",
			"JamaNA" }, description = "Verify user can Disable/Enable PRO/ObsRO forms in Subject details page", dataProvider = "canDisableEnableProObsroFormsData")
	public void canDisableEnableProObsroForms(String subjectName) {
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		rolesAndClaimsSteps.verifyDisableProObsroCheckBoxIsActive(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject & Visit name 
	 */
	@DataProvider
	public Object[][] canAssignToMeData(){
		return new Object[][] { 
			{ "Subject_081216a", "Visit 3" }
		};
	}
	
	/**
	 * Steps:
	 * 1. Goto Subject List
	 * 2. Search & select the respective subject
	 * 3. Select the respective visit
	 * 4. If user has claim for canAssignToMe on 
	 *  "Roles and Claims Matrix Excel", then verify
	 *  	a. 'Assignment' drop-down is available for selection
	 *  	b.  It contains 'Me' as a value
	 *  5. 'Assignment' drop-down will be disabled otherwise
	 *  
	 * @param colName Visit List column Name to search the visit
	 * @param value Value of the column to be used in the search
	 */
	@Test(enabled = false, priority = 4, groups = { "Roles And Claims", "Subject Details",
			"JamaNA" }, description = "Check Can assign Forms from the Subject Page to Self/ME option (for ClinRO assessments)", dataProvider = "canAssignToMeData")
	public void canAssignToMe(String subjectName, String visitName) {
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		detailsSteps.clickVisitRowFor(visitName);
		rolesAndClaimsSteps.verifyCanAssignToMe(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject & Visit name 
	 */
	@DataProvider
	public Object[][] canAssignToOthersData(){
		return new Object[][] { 
			{ "Subject_081216a", "Visit 3" }
		};
	}
	
	@Test(enabled = false, priority = 5, groups = { "Roles And Claims", "Subject Details",
			"JamaNA" }, description = "Check Can assign Forms from the Subject Page to Self/ME option (for ClinRO assessments)", dataProvider = "canAssignToOthersData")
	public void canAssignToOthers(String subjectName, String visitName) {
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		detailsSteps.clickVisitRowFor(visitName);
		rolesAndClaimsSteps.verifyCanAssignToOthers(hasClaim);
	}
	
	/**
	 * @return Form Name, SVID
	 */
	@DataProvider
	public Object[][] canDoPaperTranscriptionData(){
		return new Object[][] {
			{ "Test_081116a", "Visit 7" }
		};
	}
	
	/**
	 * Objective - Check visibility of Paper Transcription link for the user for defining particular form as Paper Transcription. 
	 * 
	 * Steps:
	 * 1. Goto Subject List
	 * 2. Search & select the respective subject
	 * 3. Select the respective visit
	 * 4. If user has claim for canAssignToMe on 
	 *  "Roles and Claims Matrix Excel", then verify
	 * 		a. Visibility of Paper Transcription link
	 * 		b. Invisibility of Paper Transcription link otherwise
	 */
	@Test(enabled = false, priority = 6, groups = { "Roles And Claims", "Subject Details",
			"JamaNA" }, description = "Verify user can access Paper Transcription link", dataProvider = "canDoPaperTranscriptionData")
	public void canDoPaperTranscription(String subjectName, String visitName) {
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		detailsSteps.clickVisitRowFor(visitName);
		rolesAndClaimsSteps.verifyEnterPaperTranscriptionLinkIsClickable(hasClaim);
	}
	
	/**
	 * @return Video Form Name, SVID, Audio Form Name, SVID
	 */
	@DataProvider
	public Object[][] canAccessRecordingData(){
		return new Object[][] {
			{ "CGI-I", "214", "CGI-I", "210" }
		};
	}
	
	@Test(enabled = false, priority = 7, groups = { "Roles And Claims", "Assessment Management",
	"JamaNA" }, description = "Verify user can access Audio/Video related Assessments", dataProvider = "canAccessRecordingData")
	public void canAccessRecording(String vFormName, String vSvid, String aFormName, String aSvid) {
		detailsSteps.getToDashboard();
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(vFormName, vSvid);
		rolesAndClaimsSteps.verifyCanAccessVideoRecording(hasClaim);
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(aFormName, aSvid);
		rolesAndClaimsSteps.verifyCanAccessAudioRecording(hasClaim);
	}
	
	/**
	 * 
	 * @return Form Name, SVID, View Mode
	 */
	@DataProvider
	public Object[][] assessmentDataScores(){
		return new Object[][] { 
			{ "MMSE", "215", "Source + Scores" }
		};
	}

	/**
	 * Test Objectives: View 'PDF' template assessment details
	 * page Layout of 'Source+Scores' mode Switching between 'Source+Scores'
	 * mode and 'All details' mode
	 * 
	 * Steps : 
	 * 	1. Select the specified Assessment from the Assessment List Page
	 * 	2. Check that the Default view mode is "All Details"
	 * 	3. Check that the View Modes in the View Mode Dropdown are correct
	 * 	4. Select View Mode Source + Scores from the View Modes Dropdown
	 * 	5. Check that the Scores Tab and the PDF Viewer are available
	 *  6. Click on the Form PDF
	 */
	@Test(enabled = false, priority = 8, groups = { "Roles And Claims", "Assessment Management",
			"JamaNA" }, description = "Check PDF Viewer and Scores Tab from the Assessment Details Source+Score View Mode", dataProvider = "assessmentDataScores")
	public void canAccessSource(String formName, String svid, String viewMode) {
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(formName, svid);
		rolesAndClaimsSteps.verifyCanAccessSource(hasClaim);
	}
	
	
	
	/**
	 * @return Form Name, SVID
	 */
	@DataProvider
	public Object[][] canManageAssessmentsData() {
		return new Object[][] { 
			{ "MMSE", "439" }
		};
	}
	
	/**
	 * Description: Visibility of ‘assessment edit’ button on Assessment details screen.
	 */
	@Test(enabled = false, priority = 9, groups = { "Roles And Claims", "Assessment Management", "JamaNA" }, description = "Verify user can manage any completed Assessments", dataProvider = "canManageAssessmentsData")
	public void canManageAssessments(String formName, String svid) {
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(formName, svid);
		rolesAndClaimsSteps.verifyManageAssessmentControls(hasClaim);
	}
	
	/**
	 * Test Case - Verify user Can View Attachments on Assessments page if he has claim 
	 * Objective - To check if the given user has the correct access to View Attachments on Completed Assessment page
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the Siteportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Go to Study dashboard
	 *  4. Select Study and site
	 *  5. Select Complete Assessments card
	 *  6. Select an Assessmnet with speific svid
	 *  7. If user has claim for canViewAttachments on 
	 *  "Roles and Claims Matrix Excel", it verifies
	 *  	7a. attachment link(s) displayed on attachment tab of Assessment details page
	 *  	7b. user can click on attachment link(s) for viewing attached PDF 
	 */
	@Test(enabled = false, priority = 10, groups = { "Roles And Claims", "Assessment Management", "JamaNA" }, description = "Verify user can view Attachments")
	public void canViewAttachments() {
		rolesAndClaimsSteps.verifyViewAttachmentControls(hasClaim);
	}
	
	/**
	 * Test Case - Verify user Can Manage Attachments on Assessments page if he has claim
	 * Objective - To check if the given user has the correct access to Manage Attachments on Completed Assessment page
	 * Steps - 
	 * 	1. Clear all Browser cookies and navigate to the Siteportal's Login Page
	 *  2. Login using the given user's credentials
	 *  3. Go to Study dashboard
	 *  4. Select Study and site
	 *  5. Select Complete Assessments card
	 *  6. Select an Assessmnet with speific svid
	 *  7. If user has claim for canManageAttachments on 
	 *  "Roles and Claims Matrix Excel", it verifies
	 *  	7a. Add attachment control displayed on Attachments tab of Assessment details page
	 *  	7b. Delete attachment control displayed on Attachments tab of Assessment details page
	 */
	@Test(enabled = false, priority = 11, groups = { "Roles And Claims", "Assessment Management", "JamaNA" }, description = "Verify user can manage Attachments")
	public void canManageAttachments() {
		rolesAndClaimsSteps.verifyManageAttachmentControls(hasClaim);
	}
	
	/**
	 * Objective - Check if User has visibility of query side panel from all Pages.
	 * 
	 * Steps:
	 * 1. Go to Study dashboard
	 * 2. Verify Query panel is visible if user has claim
	 * 3. Navigate to All subject list
	 * 4. Verify Query panel is visible if user has claim
	 * 5. Navigate to All visit list
	 * 6. Verify Query panel is visible if user has claim
	 * 7. Navigate to All Assessment list
	 * 8. Verify Query panel is visible if user has claim
	 * 
	 */
	@Test(enabled = false, priority = 12, groups = { "Roles And Claims", "Query Management",
			"JamaNA" }, description = "Verify user can access query side panel link from all Pages ")
	public void canViewQueries() {
		detailsSteps.getToDashboard();
		rolesAndClaimsSteps.verifyCanViewQueries(hasClaim);
		studyDashboardSteps.getToAllSubjectsList();
		rolesAndClaimsSteps.verifyCanViewQueries(hasClaim);
		studyDashboardSteps.getToAllVisitsList();
		rolesAndClaimsSteps.verifyCanViewQueries(hasClaim);
		detailsSteps.getToAssessmentList();
		rolesAndClaimsSteps.verifyCanViewQueries(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject & Visit name 
	 */
	@DataProvider
	public Object[][] canManageQueriesData(){
		return new Object[][] { 
			{ "Subject_081216a", "Visit 3", 444, "MMSE", "439" }
		};
	}
	
	/**
	 * Objective - Check if User can Add/Edit/Delete Queries from Subjects/Visits/Assessments Details Pages
	 * 
	 * Steps:
	 * 1. Navigate to Subject details
	 * 2. Open 'Queries' side panel
	 * 3. Verify if user has claim:
	 * 		a. 'Add Query' button is visible
	 * 		b. 'Edit Query' button is visible
	 * 		c. 'Delete Query' button is visible
	 * 4. Navigate to Visit details
	 * 5. Open 'Queries' side panel
	 * 6. Verify if user has claim:
	 * 		a. 'Add Query' button is visible
	 * 		b. 'Edit Query' button is visible
	 * 		c. 'Delete Query' button is visible
	 * 7. Navigate to Assessment details
	 * 8. Open 'Queries' side panel
	 * 9. Verify if user has claim:
	 * 		a. 'Add Query' button is visible
	 * 		b. 'Edit Query' button is visible
	 * 		c. 'Delete Query' button is visible
	 */
	@Test(enabled = false, priority = 13, groups = { "Roles And Claims", "Query Management",
			"JamaNA" }, description = "Verify user can manage any Subject/Visit/Assessment Queries", dataProvider = "canManageQueriesData")
	public void canManageQueries(String subjectName, String visitName, int visitSVID, String formName, String formSVID) {
		detailsSteps.getToDashboard();
		studyDashboardSteps.getToAllSubjectsList();
		detailsSteps.getToSubjectDetails(subjectName);
		detailsSteps.verifySubjectDetailsIsOpened();
		rolesAndClaimsSteps.verifyCanManageQueries(hasClaim);
		studyDashboardSteps.getToAllVisitsList();
		detailsSteps.getToVisitDetails(visitName, visitSVID);
		detailsSteps.verifyVisitDetailsIsOpened(visitSVID);
		rolesAndClaimsSteps.verifyCanManageQueries(hasClaim);
		detailsSteps.getToAssessmentList();
		detailsSteps.getToAssessmentDetails(formName, formSVID);
		detailsSteps.verifyAssessmentDetailsIsOpenedVia("Assessmet List");
		rolesAndClaimsSteps.verifyCanManageQueries(hasClaim);
	}
	
	/**
	 * 
	 * @return Subject & Visit name 
	 */
	@DataProvider
	public Object[][] canReplyToQueriesIfQualifiedData(){
		return new Object[][] { 
			{ "MMSE", true },
			{ "CAM", false }
//			{ "MMSE", "439", true },
//			{ "CAM", "437", false }
		};
	}
	
	/**
	 * Objective - Check user with specific Assessment/Form qualification can reply to that Assessment Query  
	 * 
	 * Steps:
	 * 1. Navigate to specific Assessment details
	 * 8. Open 'Queries' side panel
	 * 9. Verify if user has claim:
	 * 		a. 'Reply' text box is visible
	 * 		b. 'Reply' button is visible
	 */
	@Test(enabled = false, priority = 14, groups = { "Roles And Claims", "Query Management",
			"JamaNA" }, description = "Verify user visibility for 'reply' text box and 'reply' button from any page", dataProvider = "canReplyToQueriesIfQualifiedData")
	public void canReplyToQueriesIfQualified(String formName, boolean hasFormQualification) {
//		detailsSteps.getToDashboard();
		rolesAndClaimsSteps.expandQualifiedQuery(formName);
//		detailsSteps.getToAssessmentList();
//		detailsSteps.getToAssessmentDetails(formName, formSVID);
//		detailsSteps.verifyAssessmentDetailsIsOpenedVia("Assessmet List");
		rolesAndClaimsSteps.verifyCanReplyToQueriesIfQualified(hasClaim, hasFormQualification, formName);
	}
	
	@Test(enabled = false, groups = { "Roles And Claims", "Query Management",
	"JamaNA" }, description = "Verify user can manage any Subject/Visit/Assessment Queries", dataProvider = "")
	public void canReplyToQueries() {
		
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		Log.logInfo(String.format("Ending with User Type [%s], with username : [%s]", userData.get("UserType"),
				userData.get("UserName")));
		Log.logTestClassEnd(this.getClass());
	}

}
