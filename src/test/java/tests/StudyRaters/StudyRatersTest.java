package tests.StudyRaters;

import mt.siteportal.utils.Enums.SortBy;
import mt.siteportal.utils.tools.Log;
import steps.Tests.StudyRatersSteps;
import tests.Abstract.AbstractTest;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Test(groups = { "StudyRaters" })
public class StudyRatersTest extends AbstractTest{

	private String studyName = "Auto - SubjectStatusLabel";
	private String siteName = "Site_2 - Bill Miles";
	private String personName = "John Smyth";
	private String raterName = "John Smyth PhD";
	private Integer ratersCountExpected = 5;
	private Map<String, String> personDetails = new HashMap<String,String>();
	StudyRatersSteps steps = new StudyRatersSteps();

	@BeforeClass(alwaysRun = true)
	public void loginAsAdmin() {
		Log.logTestClassStart(this.getClass());	
		beforeSteps.loginAs(adminLogin, adminPasword);
		steps.openAdministration();
		steps.selectPerson(personName+" ("+adminLogin+")");
		personDetails = steps.getPersonDetails();
		steps.openHomePage();
		steps.openDashboard();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		afterSteps.logout();
		Log.logTestClassEnd(this.getClass());
	}

	@Test(groups = { "ViewRaterProfile",
			"SFB-TC-606" }, description = "Verify the ability to view the Rater profile from Study Dashboard")
	public void viewRaterProfileTest() throws Exception {
		steps.chooseStudy(studyName);
		steps.chooseSite(siteName);
		steps.ratersCountVerification(ratersCountExpected, "button");
		steps.openRaters();
		steps.ratersCountVerification(ratersCountExpected, "section");
		steps.ratersCountVerification(ratersCountExpected, "table");

		steps.sortingVerification(SortBy.SCHEDULED);
		steps.sortingVerification(SortBy.COMPLETED);
		steps.sortingVerification(SortBy.EDITED);
		steps.sortingVerification(SortBy.RATER);

		//TODO: verification will fail due to probable issue. Clarification needed
		steps.raterDetailsVerification(raterName, personDetails);
	}
}
