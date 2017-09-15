package tests.Queries;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import steps.Configuration.CommonSteps;
import steps.Tests.QueriesSteps;

/**
 * QuerySiteContextTests will verify if the queries loaded on query panel
 * belong to the selected Study-site
 * 
 * @author Ayman Noor, Abdullah Al Hisham
 *
 */
@Test(groups = { "QuerySiteContext" })
public class QuerySiteContextTests extends AbstractQueries {
	private List<Object[]> querySites = new ArrayList<Object[]>();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		queriesSteps = new QueriesSteps();
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	/**
	 * Before every test method: 
	 * 1. Open Query Panel
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
	}
	
	/**
	 * After every test method: 
	 * 1. Navigate to Study Dashboard
	 * 2. Log the name of the method that was run
	 * 
	 * @param method
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
		queriesSteps.closeQuerySidePanel();
	}
	
	/**
	 * Returns sites with query as data for given study
	 * 
	 * @return Iterator<Object[]> querySites
	 * 			- sites with queries
	 */
	@DataProvider
	public Iterator<Object[]> siteData() {
		querySites = queriesSteps.getSitesWithQueriesForStudy(studyName);
		return querySites.iterator();
	}

	@Test(groups = { "QueriesContextSite",
			"JamaNA" }, description = "Checks if Queries are contextual for a selected Study-site", dataProvider = "siteData")
	public void queriesContextSiteTest(String querySite) {
		queriesSteps.selectSiteAndGetSiteName(querySite);
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryForSite();
	}
}
