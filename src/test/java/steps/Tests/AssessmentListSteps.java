package steps.Tests;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.SkipException;

import mt.siteportal.details.AssessmentDetails;
import mt.siteportal.pages.studyDashboard.Dashboard;
import mt.siteportal.tables.AssessmentsTable;
import mt.siteportal.utils.Browser;
import mt.siteportal.utils.helpers.UiHelper;
import mt.siteportal.utils.tools.HardVerify;
import mt.siteportal.utils.tools.Log;
import nz.siteportal.pages.studydashboard.ListPages.AssessmentList;
import steps.Configuration.CommonSteps;

/**
 * Created by Abdullah Al Hisham on 07/20/2016
 */
public class AssessmentListSteps extends CommonSteps{

	public AssessmentListSteps() {
		assessmentDetails = PageFactory.initElements(Browser.getDriver(), AssessmentDetails.class);
		assessmentsTable = PageFactory.initElements(Browser.getDriver(), AssessmentsTable.class);
		assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
	}

	public void verifyAssessmentsTableColumnHeaderElement(String colName) {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		HardVerify.NotNull(assessmentList.getColumnHeaderFor(colName), "Test if the Column [" + colName + "] exists...",
				"Test Passed", "The Column Header called " + colName + " was not found.");
	}

	private String assessmentName;
	private WebElement row;

	public WebElement getRandomAssessment() {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		row = assessmentList.getRandomRow();
		assessmentName = assessmentList.getColumnData("Assessment", row);
		return row;
	}
	
	public void getAssessmentDetailsOfRandomAssessment() {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		assessmentDetails = assessmentList.clickAnAssessment(getRandomAssessment());
		UiHelper.checkPendingRequests(Browser.getDriver());
		if (assessmentDetails == null)
			throw new org.testng.SkipException("No such assessment found in the list.");
	}

	public void verifyClickedAssessmentDetailsPage() {
        assessmentDetails = PageFactory.initElements(Browser.getDriver(),AssessmentDetails.class);
		HardVerify.True(assessmentName.equalsIgnoreCase(assessmentDetails.getAssessmentName()),
				"Test if the Assessment Details Page shows the Assessment clicked from the List....", "Test Passed",
				"The Assessment Details page's text was " + assessmentDetails.getAssessmentName()
						+ " but should have been " + assessmentName + ".");
	}
	
	public void sortAssessmentListForColumn(String column, boolean increasingDownwards) {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		assessmentList.sortColumn(column, increasingDownwards);
	}

	public void verifySorting(String column, boolean increasingDownwards) {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		HardVerify.True(assessmentList.isSorted(column, increasingDownwards), "Test if the list is sorted....",
				"Test Passed", "The Sorting was not correct.");
	}	
	
	public boolean getUserAssessment(String colName, String colValue) {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		boolean foundAssessment = false;
		Log.logStep("Getting user defined Assessments with " + colName + "=" + colValue + ".");
		WebElement assessmentRow = assessmentList.getRowForAssessment(colName, colValue);
		if (null != assessmentRow) {
			Log.logInfo("The Assessment with " + colName + "=" + colValue + " found in the list.");
			assessmentDetails = assessmentList.clickAnAssessment(assessmentRow);
			foundAssessment = true;
			return foundAssessment;
		} else {
			Log.logInfo("The Assessment with " + colName + "=" + colValue + " not found in the list.");
			throw new SkipException("The Assessment with " + colName + "=" + colValue + " not found in the list.");
//			Log.logInfo("The Assessment with " + colName + "=" + colValue + " not found in the list.");
//			return foundAssessment;
		}
	}
	
	public void verifyClickedAssessmentDetails(String colName, String colValue) {
        assessmentDetails = PageFactory.initElements(Browser.getDriver(),AssessmentDetails.class);
		HardVerify.True(assessmentDetails.isOpened(),
				"Test if the Assessment Details Page shows the Assessment clicked from the List....",
				"The link to Assessment details page for " + colName + "=" + colValue + " is working.",
				"The link to Assessment details page for " + colName + "=" + colValue + " is NOT working.");
	}
	
	public boolean getColumnHeaderForType() {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		boolean foundColumnHeaderForType = false;
		Log.logStep("Verifying if the column 'Type' exists in Assessment List...");
		WebElement colHeader = assessmentList.getColumnHeaderFor("Type");
		if (null != colHeader) {
			Log.logInfo("The column 'Type' exists in Assessment List");
			foundColumnHeaderForType = true;
			return foundColumnHeaderForType;
		} else {
			Log.logInfo("The column 'Type' doesn't exist in Assessment List");
			throw new SkipException("The column 'Type' doesn't exist in Assessment List");
		}
	}
	
	public void verifyDifferentTypeAssessmentsForTypeColumn() {
        assessmentList = PageFactory.initElements(Browser.getDriver(), AssessmentList.class);
		HardVerify.True(assessmentList.diffTypesOfAssessmentExists(),
				"Verifying if the Assessment list contains different assessment types...",
				"The list contains different assessment types.[Test Passed]",
				"The list doesn't contain any assessments or all three possible assessment types.");
	}
	
	public boolean getAssessmentsListWithItemsFor(String filter) {

		if (getToStudyDashboard()) {
			StudyDashboardSteps studyDashboardSteps = new StudyDashboardSteps();
			dashboard = PageFactory.initElements(Browser.getDriver(), Dashboard.class);
			int count = dashboard.getCountFor("Assessments", filter);
			if (count == 0) {
				String study = studyDashboardSteps.selectStudyWithItemsForCategoryFilter("Assessments", filter);
				chooseStudy(study);
			}
			dashboardList = dashboard.clicksOnCard("Assessments-" + filter);
		}

		if (dashboardList.isListOpen()) {
			String headerName = dashboardList.getListTypeNameWithFilter();
			if ( (headerName.equals("Assessments: " + filter)) || (headerName.equals("Assessments: All")) ) {
				Log.logInfo("The Header text is " + dashboardList.getListTypeNameWithFilter()
						+ ", indicating that this is Assessments: " + filter + " page.");
				return true;
			} else {
				Log.logInfo("The Header text is " + dashboardList.getListTypeNameWithFilter()
				+ ", indicating that this is not Assessments: " + filter + " page.");
				return false;
			}
		}
		Log.logInfo("The Assessments: " + filter + " List is not Opened");
		return false;
	}
}
