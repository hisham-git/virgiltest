package tests.StudyDashboard;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.ChartsViewSteps;
import steps.Tests.StudyDashboardSteps;
import tests.Abstract.AbstractTest;

/**
 * Test Class for the Study Dashboard's Chart View Mode. Mostly checks if the
 * Data between the Charts View and Cards View are consistent. Does not perform
 * tests on the actual chart diagrams since they are made from canvas and
 * WebDriver has some limitations with canvas elements
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "StudyDashboard", "EnvironmentIndependent", "Sanity" })
public class ChartsViewTests extends AbstractTest {

	/*
	 * Study for which all the tests will be done
	 */
//	private final String studyName = "AA Pharmaceutical - AA Study 2";
	private String studyName;
	private final String siteName = "All Sites";
	private ChartsViewSteps chartsViewSteps;
	private StudyDashboardSteps studyDashboardSteps;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		studyDashboardSteps = new StudyDashboardSteps();
		chartsViewSteps = new ChartsViewSteps();
		beforeSteps.loginAs(adminLogin, adminPasword);
		commonSteps.getToStudyDashboard();
//		studyName = studyDashboardSteps.selectStudyWithMaxFilterValuesForAllCategories();
		studyName = "PharmaCo - MetaCrystal 2";
		if (null != studyName) {
			beforeSteps.chooseStudy(studyName);
			studyDashboardSteps.selectSiteWithMaxForms();
//			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("No study found with Maximum Filter values for all Categories. Skipping tests...");
		}
	}

	/**
	 * Aftwe executing any of the methods in a class log the class name
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	/**
	 * Test Method - Checks for persistent data between the Cards View Mode and
	 * Charts View Mode, and checks if the calculated % values are OK.
	 * 
	 * Steps -
	 *  1. Go to Study Dashboard's Card View Mode
	 *  2. Create a List of Dashboard Cards
	 *  3. Go to Study Dashboard's Charts View Mode 
	 *  4. Match the counts from the Dashboard Cards List with the Chart Card's Data
	 *  5. Verify that the % on the Charts are correct
	 */
	@Test(groups = { "ChartsData",
			"JamaNA" }, description = "Checks for persistent data between the Cards View Mode and Charts View Mode, and checks if the calculated % values are OK.")
	public void chartsDataTest() {
		chartsViewSteps.refreshDashboard();
		chartsViewSteps.changeView("Cards View Mode");
		chartsViewSteps.getCardViewItems();
		chartsViewSteps.changeView("Charts View Mode");
		chartsViewSteps.verifyChartsViewData();
	}

	/**
	 * Test Method - Checks for persistent data on Charts after changing sites
	 * 
	 * Steps - 
	 * 	1. Go to Study Dashboard's Card View Mode
	 *  2. Loop through all the Sites and store the list of the Site's Dashboard Cards
	 *  3. Go to Study Dashboard's Charts View Mode 
	 *  4. For every Site 
	 *  	a. Check that the Dashboard View Mode did not change to Cards View
	 *  	b. Get the Site's corresponding Dashboard Card List 
	 *  	c. Match the Dashboard Card List with the Site's Chart Data
	 * 
	 */
	@Test(groups = { "ChartsDataOnSiteChange",
			"JamaNA" }, description = "Checks for persistent data on Charts after changing sites")
	public void chartsDataOnSiteChangeTest() {
		studyDashboardSteps.selectRandomSite();
		chartsViewSteps.changeView("Cards View Mode");
		chartsViewSteps.collectCardViewDataForEverysite();
		chartsViewSteps.changeView("Charts View Mode");
		chartsViewSteps.verifyChartsViewDataForEverySite();
	}

	/**
	 * Test Method - Checks a random Filter's Link from a random Charts Card
	 * 
	 * Steps -
	 *  1. Go to a Site's Study Dashboard's Charts View Mode
	 *  2. Select a Random Category Filter
	 *  3. Click on the Filter's link 
	 *  4. Check if the Dashboard List page opened is the correct one
	 *  
	 */
	@Test(groups = { "FilterLinks", "JamaNA" }, description = "Checks a random Filter's Link from a random Charts Card")
	public void filterLinksTest() {
		chartsViewSteps.changeView("Charts View Mode");
		chartsViewSteps.selectRandomCategoryFilter();
		chartsViewSteps.clickOnRandomFilter();
		chartsViewSteps.verifyDashboardListOpenedWithCorrectType();
		chartsViewSteps.verifyDashboardListOpenedWithCorrectFiter();
		chartsViewSteps.verifyDashboardListHasCorrectItems();
	}
}