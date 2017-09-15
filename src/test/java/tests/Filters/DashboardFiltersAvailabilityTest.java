package tests.Filters;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Configuration.BeforeSteps;
import steps.Tests.FiltersSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test(groups = { "Filters", "DashboardFiltersAvailability" })
public class DashboardFiltersAvailabilityTest  extends AbstractTest{

	String site = "Site_1 - Francis Gibbes";
	String study = "Auto - Subject";
    List<ArrayList<String>> expectedStatuses = new ArrayList<ArrayList<String>>();
    FiltersSteps steps = new FiltersSteps();

	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
//        dbSteps.insertAssignStatusAliases(new String[0]);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndChooseStudySite(adminLogin,adminPasword,study,site);
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[0],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[1],Constants.SUBJECTSTASUSES[2],Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[2],Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.SUBJECTSTASUSES[4])));
	}
	
	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	@Test(groups = { "FiltersAvailabilityforSubjectStatusesTest",
			"SFB-TC-740" }, description = "Verify the filters shown on Study Dashboard for each subject status configured for a study")
	public void filtersAvailabilityforSubjectStatusesTest() {
		steps.dashboardSubjectFiltersVerification();
		steps.openSubjectsByStatusFromDashboard(Constants.SUBJECTSTASUSES[0]);
		steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[4], expectedStatuses.get(0));
		steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[0], expectedStatuses.get(0));
		steps.returnToDashboard(study);
		for (int i = 1; i < expectedStatuses.size() - 1; i++) {
			steps.openSubjectsByStatusFromDashboard(Constants.SUBJECTSTASUSES[i]);
			steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.returnToDashboard(study);
		}
	}
}
