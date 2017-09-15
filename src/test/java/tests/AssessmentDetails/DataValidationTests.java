package tests.AssessmentDetails;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

/**
 *  
 * Test Objective(s):
 * - To test the data validation of an specific Assessment
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "DataValidation" })
public class DataValidationTests extends AbstractAssessmentDetails{	
	
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
//		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
		studyName = studyDashboardSteps.selectStudyWithQueryType("Assessment", false);
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
		} else {
			throw new SkipException("No study found with Assessment queries. Skipping tests...");
		}
	}
	
	/**
	 * Description - Test to validate data of a specific Assessment
	 * 
	 * Steps : 
	 * 	1. Open Query Side Panel from Study Dashboard
	 *  2. Search for an Assessment type Query
	 *  3. Store Site, Subject, Visit, Assessment Name values
	 *  4. Click on found Assessment type query
	 *  5. Verify stored Site, Subject, Visit, Assessment Name values 
	 *  with the item loaded on main window
	 */
	@Test(groups = { "AssessmentGridDataViaQuery",
			"JamaNA" }, description = "Verify the Grid Data on the Assessment Details page through an Assessment Query.")
	public void assessmentGridDataViaQueryTest() {
		detailsSteps.getToStudyDashboard();
		detailsSteps.openQuerySidePanel();
		if (detailsSteps.getAssessmentQuery()) {
			detailsSteps.verifyQuerySiteName();
			detailsSteps.verifyQuerySubjectName();
			detailsSteps.verifyQueryVisitName();
			detailsSteps.verifyQueryAssessmentName();
			Log.logInfo("Assessment data on the Data Grid match with those of the assessment Query.");
		} else {
			Log.logInfo("No Assessment Query found in the Query list.");
			detailsSteps.closeQuerySidePanel();
		}
	}

	/**
	 * Description - Test to validate data of a specific Assessment
	 * 
	 * Steps : 
	 * 	1. Open "All Assessments" list
	 *  2. Choose a random Assessment for collecting data 
	 *  3. Store Form name, Type, Version, Status, Rater, Visit, Site, Subject values
	 *  4. Click on found random Assessment
	 *  5. Verify stored values on Assessment Details screen 
	 */
	@Test(groups = { "AssessmentGridDataViaList",
			"JamaNA" }, description = "Verify the Grid Data on the Assessment Details page.")
	public void assessmentGridDataViaListTest() {
		detailsSteps.getToAssessmentList();
		detailsSteps.getRandomAssessmentDataFromList();
		detailsSteps.clickRandomAssessment();
		detailsSteps.verfyRandomAssessmentDetails();
	}
}