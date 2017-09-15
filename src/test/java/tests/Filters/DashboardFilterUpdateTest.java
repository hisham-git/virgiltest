package tests.Filters;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;
import steps.Tests.FiltersSteps;
import steps.Tests.SubjectSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test(groups = { "Filters", "DashboardFilterUpdate" })
public class DashboardFilterUpdateTest extends AbstractTest {
	
	private String study  = "Auto - Subject";
	private String site = "Site_1 - Francis Gibbes";
	private String subject = "Subject%s";
    private String visit = "Visit ";
    List<ArrayList<String>> expectedStatuses = new ArrayList<ArrayList<String>>();

    FiltersSteps steps = new FiltersSteps();
    SubjectSteps subjectSteps = new SubjectSteps();

	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
        dbSteps.insertAssignStatusAliases(Constants.ALIASES);
        beforeSteps.loginAndChooseStudySite(adminLogin,adminPasword,study,site);
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[1],Constants.ALIASES[2],Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[2],Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Arrays.asList(Constants.ALIASES[3],Constants.ALIASES[4])));
        expectedStatuses.add(new ArrayList<>(Collections.singletonList(Constants.ALIASES[4])));
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(){
		dbSteps.insertAssignStatusAliases(new String [0]);
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}
	
	@Test(groups = { "SubjectStatusFiltersUpdate",
			"SFB-TC-741" }, description = "Verify that Study Dashboard Subject Status filters are updated after study configuration is changed")
	public void subjectStatusFiltersUpdateTest() {
		for (int i = 0; i < Constants.SUBJECTSTASUSES.length; i++) {
			String subjectName = String.format(subject, Constants.SUBJECTSTASUSES[i]);
			String statusExpected = Constants.ALIASES[i];
			steps.openAllSubjects();
			subjectSteps.getSubjectTableData();
			subjectSteps.subjectTableSubjectStatusVerification(subjectName, statusExpected);
			steps.openSubjectDetails(subjectName);
			subjectSteps.subjectDetailsSubjectStatusVerification(subjectName, statusExpected);
			steps.returnToDashboard(study);

			if (i > 0) {
				steps.openAllVisits();
				subjectSteps.getVisitTableData();
				subjectSteps.visitTableSubjectStatusVerification(subjectName, visit + (i + 1), statusExpected, true);
				steps.openVisitDetails(subjectName, visit + (i + 1));
				subjectSteps.visitDetailsSubjectStatusVerification(subjectName, statusExpected);
				steps.returnToDashboard(study);
				steps.openAllAssessments();
				subjectSteps.getAssessmentTableData();
				subjectSteps.assessmentTableSubjectStatusVerification(subjectName, visit + (i + 1), statusExpected,
						true);
				steps.returnToDashboard(study);
			}
		}
	}
}
