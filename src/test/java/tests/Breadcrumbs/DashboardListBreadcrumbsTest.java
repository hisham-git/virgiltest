package tests.Breadcrumbs;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.StudyDashboardSteps;

/**
 * Test Breadcrumbs after navigating to the List Pages
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "DashboardListBreadcrumbs" })
public class DashboardListBreadcrumbsTest extends AbstractBreadcrumbs {
	
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
		commonSteps.getToStudyDashboard();
		studyName = studyDashboardSteps.selectRandomStudy();
		if (method.getName().equalsIgnoreCase("checkDashboardListBreadcrumbLinksForSite")) {
			siteName = breadcrumbsSteps.selectSiteOtherThan("All Sites");
		} else {
			siteName = studyDashboardSteps.selectRandomSite();
		}
		randomDashboardCard = studyDashboardSteps.selectRandomCard();
	}

	/**
	 * Objective - Test to check if the breadcrumb links are correctly generated
	 * 
	 * Steps - 
	 * 	1. Select a random Study
	 * 	2. Select a random Site
	 * 	3. Select a random Card
	 * 	4. Check that the List page opened has the correct breadcrumbs
	 * 	   based on the Site and Study chosen
	 */
	@Test(groups = { "DashboardListBreadcrumbTexts",
			"JamaNA" }, description = "Checks if the List's breadcrumbs appear correctly", invocationCount = 3)
	public void checkDashboardListBreadcrumbTexts() {
		breadcrumbsSteps.verifyBreadcrumbLinkTexts(studyName, siteName);
	}

	/**
	 * Objective - Test to check if the breadcrumb Study links correctly redirect
	 * 
	 * Steps -
	 * 	1. Select a random Study
	 * 	2. Select a random Site
	 * 	3. Select a random Card
	 * 	4. Click on the Study link from the breadcrumbs
	 * 	5. Check if the Dashboard has been opened
	 */
	@Test(groups = { "DashboardListBreadcrumbLinksForStudy",
			"JamaNA" }, description = "Checks if the List's breadcrumbs Study links work correctly", invocationCount = 3)
	public void checkDashboardListBreadcrumbLinksForStudy() {
		breadcrumbsSteps.clickStudyLinkFromToolbar();
		breadcrumbsSteps.verifyDashboardIsOpened();
	}

	/**
	 * Test to check if the breadcrumb Site links correctly redirect
	 * 
	 * Steps -
	 * 	1. Select a random Study
	 * 	2. Select a random Site
	 * 	3. Select a random Card
	 * 	4. Click on the Site link from the breadcrumbs
	 * 	5. Check if the Dashboard has been opened
	 */
	@Test(groups = { "DashboardListBreadcrumbLinksForSite",
			"JamaNA" }, description = "Checks if the List's breadcrumbs Site links work correctly", invocationCount = 3)
	public void checkDashboardListBreadcrumbLinksForSite() {
		breadcrumbsSteps.clickSecondLinkFromToolbar();
		breadcrumbsSteps.verifyDashboardIsOpened();
	}
}
