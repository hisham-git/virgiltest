package tests.Breadcrumbs;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.StudyDashboardSteps;

/**
 * Test Breadcrumbs after using the Subject, Visit or Assessment List pages to
 * navigate to the corresponding Details Page
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "DetailsBreadcrumbs" })
public class DetailsBreadcrumbsTest extends AbstractBreadcrumbs {
	
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
//		beforeSteps.loginAs(USERNAME, PASSWORD);
//		studyName = studyDashboardSteps.selectRandomStudy();
		commonSteps.getToStudyDashboard();
	}
	
	@DataProvider
	public Object[][] categoryData() {
		return new Object[][] { { "Subjects" }, { "Visits" }, { "Assessments" } };
	}

	/**
	 * Objective - Test that the Breadcrumbs being generated are accurate and redirect
	 * properly when navigating from Subject, Visit or Assessment List pages
	 * 
	 * Steps - 
	 * 	5. Select the first row from the list of Subjects/Visit/Assessment
	 * 	6. Click the last link from the the toolbar of the Details Page
	 * 	7. Check if the correct List Page is opened
	 */
	@Test(groups = { "ListDetailsBreadcrumbs",
			"JamaNA" }, description = "Checks links and texts of the breadcrumbs on the list pages", dataProvider = "categoryData")
	public void checkListDetailsBreadcrumbs(String category) {
		studyName = studyDashboardSteps.selectStudyWithAllFilterValuesFor(category);
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
			availableFilters = breadcrumbsSteps.getAvailableFiltersFor(category);
//			siteName = breadcrumbsSteps.selectSiteOtherThan("All Sites");
			for (String selectedFilter : availableFilters) {
				siteName = studyDashboardSteps.selectSiteForFilterWithValues(category, selectedFilter);
				beforeSteps.chooseSite(siteName);
				studyDashboardSteps.selectDashboardCard(category, selectedFilter);
//				randomDashboardCard = studyDashboardSteps.selectRandomCardForCategory(category);
				breadcrumbsSteps.getToFirstDetailsPageInList();
//				breadcrumbsSteps.verifyBreadcrumbLinkTexts(studyName, siteName, randomDashboardCard);
				breadcrumbsSteps.verifyBreadcrumbLinkTexts(studyName, siteName, category + "-" + selectedFilter);
				breadcrumbsSteps.clickLastLinkFromToolbar();
//				breadcrumbsSteps.verifyDashboardListIsOpened(randomDashboardCard);
				breadcrumbsSteps.verifyDashboardListIsOpened(category + "-" + selectedFilter);
				commonSteps.clickStudyLinkFromBreadcrumbs();
			}	
		}
	}
	
	/**
	 * After every test: 
	 * 1. Log the name of the method that was run
	 * 2. Get to StudyDashboard
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		commonSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method, result);
	}
}
