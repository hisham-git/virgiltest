package tests.CrVisitList;

import org.testng.annotations.Test;

import mt.siteportal.utils.data.Constants;
import mt.siteportal.utils.tools.Log;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

@Test(groups={"CrVisitListFunction"})
public class CrVisitListFunctionTest extends AbstractCrVisitList{
  
  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
	  Log.logTestClassStart(this.getClass());
	  beforeSteps.loginAs(adminLogin, adminPasword);
  }
  
	@Test(groups = { "crVisitListItemsFunctionlity",
			"SF2016_3-TC-1377" }, description = "Elements and data on Central Rating Visits list")
	public void crVisitListItemsFunctionlityTest() {
		crVisitStep.navToCentralRating();
		crVisitStep.verifyDateToShouldBeGreaterThanFrom(scheduledDateTo, getCurrentDate(), getYesterdayDate());
		crVisitStep.verifyDateFromShouldBeSmallerThanTo(scheduledDateFrom, getCurrentDate(), getNextdayDate());
		crVisitStep.verifyListIsShowingVisitsForSelectedDate(scheduledDateTo, getCurrentDate(), getNextdayDate());
		crVisitStep.verifyDatePickerIsSetToCurrentDateAfterClickOnResetButton(scheduledDateFrom, scheduledDateTo,
				getCurrentDate());
		crVisitStep.verifyVisitListIsRefreshedAfterClickOnRefreshIcon();
		crVisitStep.verifyVisitCommentsAreDispalyedAsTooltip();
		crVisitStep.verifyScheuledTimeAndSiteTimeZonesAreDispalyedForScheduledVisit();
		crVisitStep.verifyVisitListIsFilteredBasedOnSubjectStatus(expectedStatusDropdowns[0],
				Constants.SUBJECTSTASUSES);
		crVisitStep.verifyVisitListIsFilteredBasedOnVisitStatus(expectedStatusDropdowns[1],
				Constants.SUBJECTVISITSTATASUSES);
		crVisitStep.verifyVisitListIsFilteredBasedOnSearchItems(expectedTextSearchBoxes, searchboxIndex);
		crVisitStep.verifyStudyNameFormatIsCorrect();
	}

  @AfterClass(alwaysRun = true)
  public void afterClass() {
	  Log.logTestClassEnd(this.getClass());
	  afterSteps.logout();
  }

}
