package tests.Queries;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;
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
 * To tests whether the relevant queries appear on the Query Panel based on the
 * users selected item(Subject/Visit/Assessment)
 * 
 * @author Ayman Noor, Abdullah Al Hisham
 *
 */
@Test(groups = { "QueryCategoryContext" })
public class QueryCategoryContextTests extends AbstractQueries {
	
	private static List<Object[]> siteList;
	
	/**
	 * Log that the Test Class is beginning
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		commonSteps = new CommonSteps();
		queriesSteps = new QueriesSteps();
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
	}
	
	/**
	 * Before every test method: 
	 * 1. Log the name of the method that was run
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
	/*@AfterMethod(alwaysRun = true)
	public void afterMethod(Method method) {
//		qSidePanel.clickClose();
		commonSteps.getToStudyDashboard();
		Log.logTestMethodEnd(method);
	}*/
	
	/**
	 * @return Site Name
	 */
	@DataProvider
	public Iterator<Object[]> subjectSiteData() {
		siteList = queriesSteps.getSitesWithCategoryItemsForStudy(studyName, "Subjects");
		return siteList.iterator();
	}

	/**
	 * Test Method Clicks on All Subject/Visit/Assessment -> checks the Query
	 * list Clicks on single Subject/Visit/Assessment -> checks the Query list
	 * 
	 * Objective - To check if the Query list contains queries with respect to
	 * the items on main window.
	 * 
	 * Steps : 
	 * 1. Goto Study Dashboard 
	 * 2. Click on All
	 * 3. Clicks on All Subjects/Visits/Assessments
	 * 4. Check the Query list with respect to Subject/Visit/Assessment 
	 * OR
	 * 4. Click on a single Subjects/Visits/Assessments 
	 * 5. Check the Query list with respect to single Subject/Visit/Assessment
	 */

	@Test(groups = { "QueryContextForSingleSubject",
			"SFB-UC-113" }, description = "Checks if the Query list contains queries with respect to the selected Subject.", dataProvider = "subjectSiteData")
	public void queryContextForSingleSubjectTest(String siteName) {
		queriesSteps.selectSiteAndGetSiteName(siteName);
		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.createNewQueryIfUnavailable();
		queriesSteps.verifyQueryPanelHasQueryForSite();
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryFor("Subject");
	}
	
	/**
	 * @return Site Name
	 */
	@DataProvider
	public Iterator<Object[]> visitSiteData() {
		siteList = queriesSteps.getSitesWithCategoryItemsForStudy(studyName, "Visits");
		return siteList.iterator();
	}

	@Test(groups = { "QueryContextForSingleVisit",
			"SFB-UC-114" }, description = "Checks if the Query list contains queries with respect to the selected Visit.", dataProvider = "visitSiteData")
	public void queryContextForSingleVisitTest(String siteName) {
		queriesSteps.selectSiteAndGetSiteName(siteName);
		queriesSteps.selectRandomItemForContext("Visit");
		queriesSteps.openQuerySidePanel();
		queriesSteps.createNewQueryIfUnavailable();
		queriesSteps.verifyQueryPanelHasQueryForSite();
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryFor("Visit");
	}

	/**
	 * @return Site Name
	 */
	@DataProvider
	public Iterator<Object[]> assessmentSiteData() {
		siteList = queriesSteps.getSitesWithCategoryItemsForStudy(studyName, "Assessments");
		return siteList.iterator();
	}

	@Test(groups = { "QueryContextForSingleAssessment",
			"SFB-UC-115" }, description = "Checks if the Query list contains queries with respect to the selected Assessment.", dataProvider = "assessmentSiteData")
	public void queryContextForSingleAssessmentTest(String siteName) {	
		queriesSteps.selectSiteAndGetSiteName(siteName);
		queriesSteps.selectRandomItemForContext("Assessment");
		queriesSteps.openQuerySidePanel();
		queriesSteps.createNewQueryIfUnavailable();
		queriesSteps.verifyQueryPanelHasQueryForSite();
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryFor("Assessment");
	}
	
	/**
	 * 
	 * @return Site number, Card title, Query Count
	 */

	@DataProvider
	public Iterator<Object[]> siteData() {
		siteList = queriesSteps.getSitesWithCategoryItemsForStudy(studyName, "Assessments");
		return siteList.iterator();
	}

	@Test(groups = { "QueryContextForList",
			"JamaNA" }, description = "Checks if the Query list contains queries with respect to the items on main window.", dataProvider = "siteData")
	public void queryContextForListTest(String siteName) {
		queriesSteps.selectSiteAndGetSiteName(siteName);
		queriesSteps.openItemListForContext("Subject");
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryForSite();
		queriesSteps.openItemListForContext("Visit");
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryForSite();
		queriesSteps.openItemListForContext("Assessment");
		queriesSteps.openQuerySidePanel();
		queriesSteps.verifyQueryPanelHasQueryForSite();
	}
}
