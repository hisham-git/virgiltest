package tests.CrVisitList;



import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import mt.siteportal.utils.tools.Log;

@Test(groups={"CrVisitListElement"})
public class CrVisitListElementTest extends AbstractCrVisitList {
	
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		Log.logTestClassStart(this.getClass());
		beforeSteps.loginAs(adminLogin, adminPasword);
	}

	@AfterClass(alwaysRun = true)
	  public void afterClass() {
		  Log.logTestClassEnd(this.getClass());
		  afterSteps.logout();
	  }

	@Test(groups = {
			"CrVisitListElement" }, description = "Check that all elements are present on CR Visit List screen")
	public void crVisitListElementsTest() {
		crVisitStep.navToCentralRating();
		crVisitStep.verifyCrAssessmentLabelisDispalyed();
		crVisitStep.veifyRefreshIconIsdisplayed();
		crVisitStep.verifyDatePickerIsDisplayedWithCurrentDate(scheduledDateFrom,scheduledDateTo, getCurrentDate());
		crVisitStep.verifyListIsShowingAllColoumns(expectedColHeaders);
		crVisitStep.verifyListIsShowingOnlyCurrentDatesVisits(getCurrentDate());
		crVisitStep.verifyResetButtonIsDisplayed();
		crVisitStep.verifyListIsShowingAllTextSearchBoxes(expectedTextSearchBoxes);
		crVisitStep.verifyListIsShowingAllStatusDropdownMenus(expectedStatusDropdowns);
		
	}
	
}
