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
 * Test Breadcrumbs after using the Subject or Visit Details pages to navigate
 * to the Assessment Page through forms
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "FormsBreadcrumbs" })
public class FormsBreadcrumbsTest extends AbstractBreadcrumbs {
	
	/**
	 * Login as Any User with access to the given study and go to Study
	 * Dashboard
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		breadcrumbsSteps = new BreadcrumbsNavigationSteps();
		studyDashboardSteps = new StudyDashboardSteps();
		beforeSteps.loginAs(adminLogin, adminPasword);		
		commonSteps.getToStudyDashboard();
		studyName = studyDashboardSteps.selectStudyWithMaxFilterValuesForAllCategories();
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
		} else {
			throw new SkipException("No study found with filters for all categories. Skipping tests...");
		}
	}
	
	/**
	 * 1. Navigate to study dashboard
	 * 2. Get name for a random Study
	 * 3. Get name for a random Site
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	@DataProvider
	public Object[][] categoryData() {
		return new Object[][] { { "Subjects" }, { "Visits" } };
	}

	/**
	 * Test that the Breadcrumbs being generated are accurate and correctly redirect when clicked
	 * from Subject or Visit Details pages
	 */
	@Test(groups = { "BreadcrumbsForForms",
			"JamaNA" }, description = "Check that the Breadcrumbs text are correct and redirect correctly when clicked from Subject or Visit Details pages", dataProvider = "categoryData")
	public void checkBreadcrumbsForForms(String category) {
		
			availableFilters = breadcrumbsSteps.getAvailableFiltersFor(category);
			
			for (String selectedFilter : availableFilters) {
				siteName = studyDashboardSteps.selectSiteForFilterWithValues(category, selectedFilter);
				if (null == siteName)
					continue;
//				siteName = breadcrumbsSteps.selectSiteOtherThan("All Sites");
				beforeSteps.chooseSite(siteName);
				
//				breadcrumbsSteps.clickRandomCard(randomDashboardCard);
				studyDashboardSteps.selectDashboardCard(category, selectedFilter);
				
//				breadcrumbsSteps.verifyExpectedBreadcrumbsForForms(studyName, siteName, context, filter);
				breadcrumbsSteps.verifyExpectedBreadcrumbsForForms(studyName, siteName, category, selectedFilter);
				breadcrumbsSteps.clickLastLinkFromBreadcrumbs();
				breadcrumbsSteps.verifyCorrectContextRedirection(category);
				commonSteps.clickStudyLinkFromBreadcrumbs();
			}
		}
	
	/**
	 * 1. Navigate to study dashboard
	 * 2. Log the end of the test method
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		commonSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method, result);
	}
}
