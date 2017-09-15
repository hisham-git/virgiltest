package steps.Tests;

import org.openqa.selenium.support.PageFactory;

import mt.siteportal.pages.CrVisitList;
import mt.siteportal.pages.HomePage;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Verify;
import steps.Abstract.AbstractStep;

public class CrVisitListSteps extends AbstractStep {
	
	
	
	public void navToCentralRating() {
		homePage = PageFactory.initElements(Browser.getDriver(), HomePage.class);
		if (homePage.isHomePageOpened()) {
			crVisitList =	homePage.openCentralRating();
		}

		HardVerify.True(crVisitList.isCentralRatingOpened(), "Checking Central Rating page is opened.",
				"PASSED: Central rating page is opened.", "FAILED: Central rating page is not opened");
	}

	public void verifyCrAssessmentLabelisDispalyed() {		
		HardVerify.True(crVisitList.crAssessmentLabelisDispalyed(),
				"Checking CR Assessment lable is found on CR visit list page", "PASSED: CR Assessment label is found",
				"FAILED : CR Assessment label is not found");
	}

	public void veifyRefreshIconIsdisplayed() {		
		HardVerify.True(crVisitList.crRefreshIconIsdisplayed(),
				"Checking Refresh Icon is present in CR visit list page", "PASSED: Refresh icon is found",
				"FAILED : Refresh icon is not found");
	}

	public void verifyDatePickerIsDisplayedWithCurrentDate(String dateFrom, String dateTo,
			String currentDate) {
		boolean scheduleDateFrom = crVisitList.crScheduleDatePickerIsDisplayed(dateFrom);
		boolean scheduleDateTo = crVisitList.crScheduleDatePickerIsDisplayed(dateTo);

		HardVerify.True(scheduleDateFrom && scheduleDateTo, "Checking Schedule Date From date picker is present",
				"PASSED: Schedule Date From/To date picker is present",
				"FAILED: Schedule Date From/To date picker is not present");

		String scheduleDateFromValue = crVisitList.crScheduleDatePickerGetSelectedDate(dateFrom);
		String scheduleDateToValue = crVisitList.crScheduleDatePickerGetSelectedDate(dateTo);

		HardVerify.True(
				scheduleDateFromValue.equalsIgnoreCase(currentDate)
						&& scheduleDateToValue.equalsIgnoreCase(currentDate),
				"Checking Selected  date From: "+scheduleDateFromValue+"/To: "+scheduleDateToValue+" date picker is same as "+ currentDate,
				"PASSED: Selected date From: "+scheduleDateFromValue+"/To: "+scheduleDateToValue+" date picker is mathced with "+ currentDate,
				"FAILED: Selected date From: "+scheduleDateFromValue+"/To: "+scheduleDateToValue+" date picker is not mathced with "+ currentDate);
	}

	public void verifyListIsShowingAllColoumns(String[] expectedColHeaders) {
		for (String colName : expectedColHeaders) {
			HardVerify.True(crVisitList.crExpectedColHeaderIsPresent(colName),
					"Checking " + colName + " column is present", "PASSED: Coloumn " + colName + " is present",
					"FAILED: Coloumn " + colName + " is not present");
		}
	}

	public void verifyListIsShowingOnlyCurrentDatesVisits(String currentDate) {
		crVisitList = PageFactory.initElements(Browser.getDriver(), CrVisitList.class);
		HardVerify.True(crVisitList.crVisitsAreForCurrentDateDispalyed(currentDate),
				"Checking Visit list contains only current date's visits",
				"PASSED : Contains only current dates visits", "FAILED: Showing other dates visits also");
	}

	public void verifyResetButtonIsDisplayed() {
		HardVerify.True(crVisitList.crResetButtonIsdisplayed(),
				"Checking Reset Button is present in CR visit list page", "PASSED: Resent button is found",
				"FAILED : Resent Button  is not found");
	}

	public void verifyListIsShowingAllTextSearchBoxes(String[] expectedTextSearchBoxes) {
		for (String colName : expectedTextSearchBoxes) {
			HardVerify.True(crVisitList.crExpectedSearchBoxIsPresent(colName),
					"Checking " + colName + " search box is present", "PASSED: Search box " + colName + " is present",
					"FAILED: Search box " + colName + " is not present");
		}
	}

	public void verifyListIsShowingAllStatusDropdownMenus(String[] expectedStatusDropdowns) {
		for (String colName : expectedStatusDropdowns) {
			HardVerify.True(crVisitList.crExpectedStatusDropdownIsPresent(colName),
					"Checking " + colName + "  drop down menu is present",
					"PASSED: Drop down menu " + colName + " is present",
					"FAILED:  Drop down menu " + colName + " is not present");
		}
	}

	public void verifyDateFromShouldBeSmallerThanTo(String scheduledDateFrom, String currentDate, String nextDayDate) {
		crVisitList = crVisitList.chooseDatePicker(nextDayDate, scheduledDateFrom);
		HardVerify.True(
				!crVisitList.crScheduleDatePickerGetSelectedDate(scheduledDateFrom).equalsIgnoreCase(nextDayDate),
				"Checking Schedule DateFrom Cant Be Greater Than DateTo",
				"PASSED: Date From can not be selected greater that Date To",
				"FAILED: Date From can not be selected greater that Date To");
	}

	public void verifyDateToShouldBeGreaterThanFrom(String scheduledDateTo,String currentDate, String yesterdayDate) {
		crVisitList = crVisitList.chooseDatePicker(yesterdayDate, scheduledDateTo);
		HardVerify.True(
				!crVisitList.crScheduleDatePickerGetSelectedDate(scheduledDateTo).equalsIgnoreCase(yesterdayDate),
				"Checking Schedule DateTo Cant Be Less Than DateFrom",
				"PASSED: Date To can not be selected less that Date From",
				"FAILED: Date To can be selected less that Date From");
	}

	public void verifyListIsShowingVisitsForSelectedDate(String scheduledDateTo, String currentDate,
			String selectedDate) {
		crVisitList.chooseDatePicker(selectedDate, scheduledDateTo);
		HardVerify.True(crVisitList.crGetExpectedVisitList(currentDate, selectedDate),
				"Checking visit list based on selected date", "PASSED:Visits are displayed on based on selected date",
				"FAILED:Visits are dispalyed from different dates not as selected date");

	}

	public void verifyDatePickerIsSetToCurrentDateAfterClickOnResetButton(String scheduledDateFrom,
			String scheduledDateTo, String currentDate) {

		crVisitList = crVisitList.crScheduleDateFromAndToSetTocurrentDateAfterClickOnResetButton();
		HardVerify.True(crVisitList.crScheduleDatePickerGetSelectedDate(scheduledDateFrom).equalsIgnoreCase(currentDate)
				&& crVisitList.crScheduleDatePickerGetSelectedDate(scheduledDateTo).equalsIgnoreCase(currentDate),
				"Checking schedule date from/to set to current date after clicking reset button",
				"PASSED:schedule date from/to set to current date after clicking reset button",
				"FAILED:schedule date from/to is not set to current date after clicking reset button");

	}

	public void verifyVisitListIsRefreshedAfterClickOnRefreshIcon() {
		HardVerify.True(crVisitList.crRVisitListIsRefreshedAfterClickOnRefreshIcon(),
				"Checking CR visit list is refreshed on clicking refresh icon", "PASSED: Rfreshed CR visit list",
				"FALIED: to refresh page");
	}

	public void verifyVisitCommentsAreDispalyedAsTooltip() {
		Verify.True(crVisitList.crVisitCommentsAreDispalyedAsTooltip(), "Checking Visit tooltip comments",
				"PASSED: Visit tooltip is present with comments", "No Tooltip is present for cr visit list");

	}

	public void verifyVisitListIsFilteredBasedOnSubjectStatus(String statusDropDown, String[] subjectStatus) {
		for (String subStatus : subjectStatus) {
			crVisitList = crVisitList.chooseStatus(statusDropDown, subStatus);

			Verify.True(
					crVisitList.crVisitListAreFilteredBasedOnSubjectStatus(crVisitList.getCurrentListSize(), subStatus),
					"Checking Subject Status drop down menues with status= " + subStatus,
					"PASSED: Cr Subject visits with status " + subStatus + " present in list",
					"There is no Cr Subject visits with status " + subStatus + " in list");
		}
	}

	public void verifyScheuledTimeAndSiteTimeZonesAreDispalyedForScheduledVisit() {
		Verify.True(crVisitList.crScheuledTimeSiteTimeZonesAreDispalyedForScheduledVisit(),
				"Checking Scheduled tooltip comments present",
				"PASSED: Scheuled Time & SiteTime Zones are Dispalyed for Scheduled Visit",
				"No Tooltip is present for cr Scheduled visit");
	}

	public void verifyVisitListIsFilteredBasedOnVisitStatus(String statusDropDown, String[] subjectVisitStatasuses) {
		crVisitList  = crVisitList.crClikResetButton();
		for (String visitStatus : subjectVisitStatasuses) {
			crVisitList = crVisitList.chooseStatus(statusDropDown, visitStatus);
			Verify.True(
					crVisitList.crVisitListAreFilteredBasedOnSubjectStatus(crVisitList.getCurrentListSize(),
							visitStatus),
					"Checking Visit Status drop down menus with status= " + visitStatus,
					"PASSED: Cr visits with status " + visitStatus + " present in list",
					"There is no Cr visits with status= " + visitStatus + " in list");
		}

	}

	public void verifyStudyNameFormatIsCorrect() {
		crVisitList  = crVisitList.crClikResetButton();
		Verify.True(crVisitList.crStudyNameFormatIsCorrect(), "Checking Study Name format",
				"PASSED: All study names are corrent format", "FAILED");
	}

	public void verifyVisitListIsFilteredBasedOnSearchItems(String[] expectedTextSearchBoxes, int[] searchIndex) {
		int i = 0;
		for (String searchbox : expectedTextSearchBoxes) {
			String getSearchOption = crVisitList.getSearchItem(searchIndex[i]);
			if (getSearchOption != null) {
				crVisitList = crVisitList.filteredVisitListBySearch(getSearchOption, searchbox);
				Verify.True(crVisitList.checkingFilteringListBySearchOption(getSearchOption, searchIndex[i]),
						"Filtering visit list by " + searchbox + "= " + getSearchOption,
						"PASSED : Filtered Visit list are displayed based on " + searchbox + "= " + getSearchOption,
						"FAILED");
			}
			crVisitList  = crVisitList.crClikResetButton();
			i++;
		}

	}
	
	
}
