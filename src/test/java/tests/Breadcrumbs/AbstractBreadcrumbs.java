package tests.Breadcrumbs;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.utils.data.DataHolder;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.CommonSteps;
import steps.Tests.BreadcrumbsNavigationSteps;
import steps.Tests.StudyDashboardSteps;
import tests.Abstract.AbstractTest;

/**
 * Super class for All Breadcrumbs Tests
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 *
 */
@Test(groups = { "Breadcrumbs", "EnvironmentIndependent", "Sanity" })
public abstract class AbstractBreadcrumbs extends AbstractTest {

	/*
	 * Login Credentials
	 */
	protected final String USERNAME = DataHolder.getHolder().getSiteportalUserAccountName();
	protected final String PASSWORD = DataHolder.getHolder().getSiteportalUserAccountPassword();

	/*
	 * Step classes
	 */
	protected CommonSteps commonSteps;
	protected BreadcrumbsNavigationSteps breadcrumbsSteps;
	protected StudyDashboardSteps studyDashboardSteps;
	
	/**
	 * Test parameters
	 */
	protected String studyName;
	protected String siteName;
	protected String randomDashboardCard;
	protected String selectedFilterCard;
	protected String context;
	protected String filter;
	protected List<String> availableFilters;
	
	/**
	 * Login as Any User with access to the given study and go to Study
	 * Dashboard
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAs(adminLogin, adminPasword);
//		beforeSteps.loginAs(USERNAME, PASSWORD);
		commonSteps = new CommonSteps();
		breadcrumbsSteps = new BreadcrumbsNavigationSteps();
		studyDashboardSteps = new StudyDashboardSteps();
	}

	/**
	 * Log that the Test Class is ending
	 */
	@AfterClass(alwaysRun = true)
	public void afterClass() {
//		afterSteps.logout();
		afterSteps.logoutByClearingCookie();
		Log.logTestClassEnd(this.getClass());
	}
}
