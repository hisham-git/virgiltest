package tests.Filters;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Tests.FiltersSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test(groups = { "Filters", "CustomFilters" })
public class CustomFiltersTest extends AbstractTest{

	String study = "Auto - Subject";
	String site = "Site_1 - Francis Gibbes";

    List<ArrayList<String>> expectedStatuses = new ArrayList<ArrayList<String>>();

    FiltersSteps steps = new FiltersSteps();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
		dbSteps.insertAssignStatusAliases(Constants.ALIASES);
		beforeSteps.loginAndOpenAllSubjects(adminLogin,adminPasword,study,site);
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[1],Constants.ALIASES[2],Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[2],Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.ALIASES[4])));
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(){
		dbSteps.insertAssignStatusAliases(new String[0]);
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
	}
		
	
	@Test(groups = { "FilteringByCustomSubjectStatus",
			"SFB-TC-744" }, description = "Verify filtering Subjects, Visits and Assessments Lists by Custom Subject Status")
	public void filteringByCustomSubjectStatusTest() {
        steps.filterByAssessmentsSubjectStatus(Constants.ALIASES[0]);
        steps.filtersByAssessmentSubjectStatusVerification(Constants.ALIASES[4], expectedStatuses.get(0));
        steps.resetFilter();
        for (int i = 1; i < expectedStatuses.size()-1; i++) {
            steps.filterByAssessmentsSubjectStatus(Constants.ALIASES[i]);
            steps.filtersByAssessmentSubjectStatusVerification(Constants.ALIASES[i], expectedStatuses.get(i));
            steps.resetFilter();
        }

        steps.filterByVisitsSubjectStatus(Constants.ALIASES[0]);
        steps.filterByVisitSubjectStatusVerification(Constants.ALIASES[4], expectedStatuses.get(0));
        steps.resetFilter();
        for (int i = 1; i < expectedStatuses.size()-1; i++) {
            steps.filterByVisitsSubjectStatus(Constants.ALIASES[i]);
            steps.filterByVisitSubjectStatusVerification(Constants.ALIASES[i], expectedStatuses.get(i));
            steps.resetFilter();
        }

        expectedStatuses.get(0).add(Constants.ALIASES[0]);
        steps.filterBySubjectsSubjectStatus(Constants.ALIASES[0]);
        steps.filterBySubjectSubjectStatusVerification(Constants.ALIASES[4], expectedStatuses.get(0));
        steps.resetFilter();
        for (int i = 1; i < expectedStatuses.size()-1; i++) {
            steps.filterBySubjectsSubjectStatus(Constants.ALIASES[i]);
            steps.filterBySubjectSubjectStatusVerification(Constants.ALIASES[i], expectedStatuses.get(i));
            steps.resetFilter();
        }

    }
}
