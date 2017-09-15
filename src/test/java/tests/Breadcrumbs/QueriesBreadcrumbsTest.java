package tests.Breadcrumbs;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.StudyDashboardSteps;

/**
 * QueriesBreadcrumbsTest - Tests for the correct Breadcrumbs Navigation and
 * Text after navigating through Queries
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueriesBreadcrumbs" })
public class QueriesBreadcrumbsTest extends AbstractBreadcrumbs {
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		breadcrumbsSteps = new BreadcrumbsNavigationSteps();
		studyDashboardSteps = new StudyDashboardSteps();
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
		studyName = studyDashboardSteps.selectStudyWithQueryType("All", false);
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
			siteName = studyDashboardSteps.selectSiteWithMaxQueries();
			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("No study found with all possible queries. Skipping tests...");
		}
	}

	@DataProvider
	public Object[][] queryType() {
		return new Object[][] { { "Subject" }, { "Visit" }, { "Assessment" }, { "Score" } };
	}
	
	/**
	 * Test for the correct links generation
	 * 
	 * @param studyName
	 * @param siteName
	 * @param queryIndex
	 */
	@Test(groups = { "QueriesBreadcrumbsNavigation",
			"JamaNA" }, description = "Checks the breadcrumbs links and texts after being redirected from a Query click for specific Query Category", dataProvider = "queryType")
	public void checkQueriesBreadcrumbsNavigation(String type) {
		breadcrumbsSteps.openListPageFor(type); // Introduced for bug in breadcrumb generation for Visit type queries from StudyDashboard
		breadcrumbsSteps.openQuerySidePanel();
		breadcrumbsSteps.findAndClickQueryWithType(type);
		breadcrumbsSteps.verifyExpectedBreadcrumbsForQueries(studyName, siteName);
		breadcrumbsSteps.clickLastLinkFromBreadcrumbs();
		breadcrumbsSteps.verifyCorrectContextRedirection();
	}
	
	/**
	 * After every test, Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		breadcrumbsSteps.returnToDashboard(studyName);
		Log.logTestMethodEnd(method, result);
	}
}
