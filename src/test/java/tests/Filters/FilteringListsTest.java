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

@Test(groups = { "Filters", "FilteringLists" })
public class FilteringListsTest extends AbstractTest{

	String study = "Auto - Subject";
	String site = "Site_1 - Francis Gibbes";

    List<ArrayList<String>> expectedStatuses = new ArrayList<ArrayList<String>>();

    FiltersSteps steps = new FiltersSteps();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
//		dbSteps.insertAssignStatusAliases(Constants.SUBJECTSTASUSES);
		beforeSteps = new BeforeSteps();
		beforeSteps.loginAndOpenAllSubjects(adminLogin,adminPasword,study,site);
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[1],Constants.SUBJECTSTASUSES[2],Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[2],Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.SUBJECTSTASUSES[3],Constants.SUBJECTSTASUSES[4])));
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.SUBJECTSTASUSES[4])));
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(){
//		dbSteps.insertAssignStatusAliases(new String[0]);
        afterSteps.logout();
        Log.logTestClassEnd(this.getClass());
	}


	@Test(groups = { "FilteringAssessmentListsBySubjectStatuses",
			"SFB-TC-742" }, description = "Verify filtering Subjects, Visits and Assessments Lists by Subject Statuses")
	public void filteringAssessmentListsBySubjectStatusesTest() {
/*		steps.filterByAssessmentsSubjectStatus(Constants.SUBJECTSTASUSES[0]);
		steps.filtersByAssessmentSubjectStatusVerification(Constants.SUBJECTSTASUSES[4], expectedStatuses.get(0));
		steps.resetFilter();*/
		
		for (int i = 0; i < expectedStatuses.size() - 1; i++) {
			steps.filterByAssessmentsSubjectStatus(Constants.SUBJECTSTASUSES[i]);
			steps.filtersByAssessmentSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.resetFilter();
		}
	}

	@Test(groups = { "FilteringVisitsListsBySubjectStatuses",
			"SFB-TC-742" }, description = "Verify filtering Subjects, Visits and Assessments Lists by Subject Statuses")
	public void filteringVisitsListsBySubjectStatusesTest() {
		
		/*steps.filterByVisitsSubjectStatus(Constants.SUBJECTSTASUSES[0]);
		steps.filterByVisitSubjectStatusVerification(Constants.SUBJECTSTASUSES[4], expectedStatuses.get(0));
		steps.resetFilter();
		
		for (int i = 0; i < expectedStatuses.size() - 1; i++) {
			steps.filterByVisitsSubjectStatus(Constants.SUBJECTSTASUSES[i]);
			steps.filterByVisitSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.resetFilter();
		}*/

		for (int i = 0; i < expectedStatuses.size() - 1; i++) {
			steps.filterByVisitsSubjectStatus(Constants.SUBJECTSTASUSES[i]);
			steps.filterByVisitSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.resetFilter();
		}
	}

	@Test(groups = { "FilteringSubjectsListsBySubjectStatuses",
			"SFB-TC-742" }, description = "Verify filtering Subjects, Visits and Assessments Lists by Subject Statuses")
	public void filteringSubjectsListsBySubjectStatusesTest() {
		
		/*expectedStatuses.get(0).add(Constants.SUBJECTSTASUSES[0]);
		steps.filterBySubjectsSubjectStatus(Constants.SUBJECTSTASUSES[0]);
		steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[0], expectedStatuses.get(0));
		steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[4], expectedStatuses.get(0));
		steps.resetFilter();
		for (int i = 1; i < expectedStatuses.size() - 1; i++) {
			steps.filterBySubjectsSubjectStatus(Constants.SUBJECTSTASUSES[i]);
			steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.resetFilter();
		}*/

		for (int i = 0; i < expectedStatuses.size() - 1; i++) {
			steps.filterBySubjectsSubjectStatus(Constants.SUBJECTSTASUSES[i]);
			steps.filterBySubjectSubjectStatusVerification(Constants.SUBJECTSTASUSES[i], expectedStatuses.get(i));
			steps.resetFilter();
		}
	}
}
