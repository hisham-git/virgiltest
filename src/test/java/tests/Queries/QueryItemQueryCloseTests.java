package tests.Queries;

import java.lang.reflect.Method;

import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.Queries.QueriesSidePanel;
import steps.Configuration.CommonSteps;
import steps.Tests.QueriesSteps;

/**
 * Test Class to check for the Close Query functionalities
 * 
 * @author Syed A. Zawad, Abdullah Al Hisham
 */
@Test(groups = { "QueryItemQueryClose" })
public class QueryItemQueryCloseTests extends AbstractQueries {
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		// beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
		commonSteps = new CommonSteps();
		queriesSteps = new QueriesSteps();
		
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(siteportalUserAccountName, siteportalUserAccountPassword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
		commonSteps.getToStudyDashboard();
		commonSteps.openAllSubjects();
//		queriesSteps.selectRandomItemForContext("Subject");
		queriesSteps.openQuerySidePanel();
		if (queriesSteps.createRespondedQueryIfUnavailable() ) {
			afterSteps.logoutByClearingCookie();
			Nav.toURL(loginPageURL);
		} else {
			throw new SkipException("Couldn't find any open queries to respond. Skipping tests...");
		}
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		Log.logTestMethodStart(method);
		qSidePanel = PageFactory.initElements(Browser.getDriver(), QueriesSidePanel.class);
		dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
//		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(studyName);
			beforeSteps.chooseSite(siteName);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
		commonSteps.getToStudyDashboard();
//		queriesSteps.selectRandomItemForContext("Subject");
		commonSteps.openAllSubjects();
		queriesSteps.openQuerySidePanel();
	}

	/**
	 * Objective : To check the functionality of 
	 * the Close button on a random query
	 * 
	 * Steps:
	 * 	1. Navigate to a random subject
	 * 	2. Open Query Side panel & check only 'Closed' check-box
	 * 	3. Store count for Closed queries
	 * 	4. Check only 'Responded' & 'Open' check-box from Query Side panel
	 *	5. If there is 0 query in the list, then create a random query & make a reply
	 *	6. Get a random query from query panel 
	 *	7. Store count for Responded queries
	 *	8. Verify if the Closed Button is disabled before a reply is entered
	 *	9. Reply to previously selected random query
	 *	10. Verify if the Closed Button is enabled after a reply is entered
	 *	11. Close previously selected random query
	 *	12. Verify if Confirmation Pop-Up was displayed.
	 *	13. Click yes from pop-up
	 *	14. Verify if the number of queries with the responded status decreases
	 *	15. From Query Side panel check only 'Closed' check-box
	 *	16. Verify if the number of queries with the closed status increases
	 */
	@Test(groups = { "CloseRespondedQuery",
			"JamaNA" }, description = "Checks if a Reply correctly gets Closed Status on a random Responded or Open Query")
	public void closeRespondedQueryTest() {
		queriesSteps.getQueryCountFor("Closed");
		queriesSteps.getQueryCountFor("Responded");
		queriesSteps.getRandomRespondedQueryIndex();
		queriesSteps.verifyDisabledCloseButtonBeforeReply();
		queriesSteps.verifyEnabledCloseButtonAfterReply();
		queriesSteps.verifyConfirmationPopUpWhileClosingQuery();
		queriesSteps.confimQueryAction(true);
		queriesSteps.verifyDecreaseInRespondedQueries();
		queriesSteps.selectQueriesCheckBox(true, "Closed");
		queriesSteps.verifyIncreaseInClosedQueries();
	}
}
