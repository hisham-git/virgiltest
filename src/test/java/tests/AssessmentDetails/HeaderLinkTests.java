package tests.AssessmentDetails;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.details.RaterDetails;
import mt.siteportal.utils.tools.Log;

/**
 *  
 * Test Objective(s):
 * - To test the general assessment info(status, language)
 * - To test the general page actions (refresh, links)
 * 
 * @author Abdullah Al Hisham
 *
 */
@Test(groups = { "HeaderLink" })
public class HeaderLinkTests extends AbstractAssessmentDetails {
	
	protected RaterDetails raterDetails;
//	private final String ASSESSMENT_NAME = "CGI-I";
	private final String assessmentName = "CGI-I";
//	private final String SVID = "214";
	private final String visitName = "Visit8";
	
	/**
	 * Before Test class 
	 * 1. Go to assessment list
	 * 2. click an assessment with given name & svid 
	 */
	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAndChooseStudySite(siteportalUserAccountName, siteportalUserAccountPassword, studyName, siteName);
/*		commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList();
//		detailsSteps.getToAssessmentDetails(ASSESSMENT_NAME, SVID);
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);*/
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		commonSteps.getToStudyDashboard();
		detailsSteps.getToAssessmentList("Complete");
		detailsSteps.getToAssessmentDetails(subjectName, visitName, assessmentName);
	}

	@DataProvider
	public Object[][] assessmentViewMode(){
		return new Object[][] { 
			{ "All Details"},
			{ "Video + Source"},
			{ "Video + Scores"},
			{ "Source + Scores"}
		};
	}
	
	/**
	 * @param paramMode
	 */
	@Test(groups = { "AssessmentPageViewMode",
			"JamaNA" }, description = "Verify the Assessment Details page View Mode.", dataProvider = "assessmentViewMode")
	public void assessmentPageViewModeTest(String paramMode) {
		if (paramMode.equals("All Details")) {
			Log.logInfo("Verifying Default view mode is All Details");
			detailsSteps.verifyDefaultViewModeAllDetails();
		} else {
			detailsSteps.selectViewMode(paramMode);
			detailsSteps.getAssessmentDetailsCurrentViewMode();
			detailsSteps.verifyCurrentViewMode(paramMode);
		}
	}
	
	/**
	 * @param paramMode
	 */
	@Test(groups = { "VisitLinkOnDetailsGridHeader",
			"JamaNA" }, description = "Verify Header Grid Visit link on the Assessment Details grid for Different View Modes.", dataProvider = "assessmentViewMode")
	public void visitLinkOnDetailsGridHeaderTest(String paramMode) {
		if (paramMode.equals("All Details")) {
			Log.logInfo("Header Grid Visit link is not available in '" + paramMode + "' mode");
		} else {
			if (detailsSteps.selectViewMode(paramMode)) {
				detailsSteps.clickGridHeaderVisitLink();
//				detailsSteps.verifyVisitDetailsOpened(SVID, "Header Grid");
				detailsSteps.verifyVisitDetailsOpened(assessmentName, visitName, "Header Grid");
				detailsSteps.navigateBackToAssessmentDetails();
			}
		}
	}
	
	/**
	 * @param paramMode
	 */
	@Test(groups = { "RaterLinkOnDetailsGridHeader",
			"JamaNA" }, description = "Verify Header Grid Rater link on the Assessment Details grid for Different View Modes.", dataProvider = "assessmentViewMode")
	public void raterLinkOnDetailsGridHeaderTest(String paramMode) {
		if (paramMode.equals("All Details")) {
			Log.logInfo("Header Grid Rater link is not available in '" + paramMode + "' mode");
		} else {
			if (detailsSteps.selectViewMode(paramMode)) {
				detailsSteps.getRaterNameOnGridHeaderLink();
				detailsSteps.clickGridHeaderRaterLink();
				detailsSteps.verifyRaterDetailsOpened("Header Grid");
				detailsSteps.navigateBackToAssessmentDetails();
			}
		}
	}

	
	@DataProvider
	public Object[][] assessmentRefresh(){
		return new Object[][] { 
			{ "Video + Source"},
			{ "Video + Scores"},
			{ "Source + Scores"}
		};
	}
	
	/**
	 * @param paramMode
	 */
	@Test(groups = { "AssessmentPageRefreshButton",
			"JamaNA" }, description = "Verify the refresh button function of the Assessment Details page.", dataProvider = "assessmentRefresh")
	public void assessmentPageRefreshButtonTest(String paramMode) {
		if (detailsSteps.selectViewMode(paramMode)) {
			detailsSteps.clickRefresh();
			detailsSteps.verifyRefreshOnAssessmentDetails(paramMode);
		}
	}
	
	/**
	 * @param paramMode
	 */
	@Test(groups = { "AssessmentModeCancelButton",
			"JamaNA" }, description = "Verify the cancel button function of the Assessment Details page.", dataProvider = "assessmentViewMode")
	public void assessmentModeCancelButtonTest(String paramMode) {
		if (paramMode.equals("All Details"))
			Log.logInfo("Cancel button is not available in '" + paramMode + "' mode");
		else {
			if (detailsSteps.selectViewMode(paramMode)) {
				detailsSteps.clickCancelModeButton(paramMode);
				detailsSteps.verifyDefaultViewModeAllDetails();
			}
		}
	}
}
