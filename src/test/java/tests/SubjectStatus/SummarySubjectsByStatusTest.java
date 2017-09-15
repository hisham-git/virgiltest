package tests.SubjectStatus;

import java.util.List;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import mt.siteportal.pages.studyDashboard.Filters;
import mt.siteportal.utils.data.URLsHolder;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Configuration.CommonSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

@Test(groups = { "SummarySubjectsByStatus" })
public class SummarySubjectsByStatusTest extends AbstractSubjectStatus{
	
	private final String loginPageURL = URLsHolder.getHolder().getSiteportalURL();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		Log.logTestClassStart(this.getClass());
		beforeSteps = new BeforeSteps();
		Nav.toURL(loginPageURL);
		beforeSteps.loginAs(adminLogin, adminPasword);
		if (commonSteps.getToStudyDashboard()) {
			beforeSteps.chooseStudy(study);
			beforeSteps.chooseSite(site);
		} else {
			throw new SkipException("Couldn't open Study Dashboard. Skipping tests...");
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	/**
	 * Name: Summary of Subjects by Status 
	 * 
	 * Jama ID: SFB-TC-743
	 * 
	 * Test Objective(s):
	 * 	Verify default names and sequence of the statuses on dashboard.
	 * 
	 * Prerequisite(s):
	 * 	1. A study having subjects with the following statues exists:
	 * 		New, Screened, Enrolled, Completed and Discontinued
	 * 	2. At least one custom filter exists for the Study
	 * 
	 * Action & Expected Result:
	 * 	1. Navigate to Dashboard and select the Study identified in the prerequisites.
	 * 		- Dimensions of subjects, visits, and assessments are summarized for display on the dashboard.
	 * 
	 *	2. Verify the sequence of the subject statuses on dashboard for selected study in step #1
	 * 		- Status filters are displayed in the following sequence: 
	 * 			New > Screened > Enrolled > Completed > Discontinued > Custom filters specified in prerequisetes
	 */
	@Test(groups = { "SubjectStatusOrderTest",
			"SFB-TC-743" }, description = "Checks default names and sequence of the statuses on dashboard.")
	public void subjectStatusOrderTest() {
		subjectSteps.elementsOnDashboardVerification();
		List<String> expectedStatuses = Filters.subjectFilters();
		subjectSteps.dashboardStatusesOrderVerification(expectedStatuses);
	}
}
